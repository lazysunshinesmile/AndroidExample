/*
 * 
 */
package com.base.module.screensaver.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.base.module.screensaver.R;
import com.base.module.screensaver.ScreenSaverManager;
import com.base.module.screensaver.helper.AnimInHelper;
import com.base.module.screensaver.helper.AnimOutHelper;
import com.base.module.screensaver.ui.SettingPreference;
import com.base.module.screensaver.utils.FileUtils;
import com.base.module.screensaver.utils.HttpDownloader;
import com.base.module.screensaver.utils.ImageUtil;
import com.base.module.screensaver.utils.LanguageUtil;
import com.base.module.screensaver.utils.MyLog;
import com.base.module.screensaver.utils.ShellUtil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.service.dreams.DreamService;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The Class ScreenGuardView.
 * 
 */
public class ScreenSaverView extends FrameLayout implements Runnable, View.OnClickListener, View.OnTouchListener {

    private static final boolean DEBUG = true;
    private static final String TAG = "ScreenSaverView";

    /** The member context. */
    private Context mContext;

    /** The member animation out helper. */
    private AnimOutHelper mAnimOutHelper;

    /** The member animation in helper. */
    private AnimInHelper mAnimInHelper;

    /** The member image view. */
    private ImageView mPreImageView, mImageView;
    private TextView mTextView;

    /** The member bitmap. */
    private Bitmap mBitmap;

    /** The member default bitmap. */
    // private Bitmap mDefaultBitmap;

    /** The member handler. */
    private ScreenSaverHandler mHandler;

    /** The member current index. */
    private int mCurIndex = 0;

    /** The Constant mMaxIndex. */
    private int mMinIndex = 0, mMaxIndex = 0;

    /** The member is run. */
    private boolean mIsRun = false;
    private boolean mIsStop = true;

    /** The member thread duration. */
    private long mThreadDuration;

    /** The member thread. */
    private Thread mThread;

    /** The member file path. */
    private String mFilePath;

    /** The member files. */
    private File[] mFiles;

    /** The member toast. */
    private static Toast mToast = null;

    private int[] mRandomIndex;

    /**
     * Sets the files.
     * 
     * @param filePath
     *            the file path
     * @param files
     *            the files
     */
    public void setFiles(String filePath, File[] files) {
        if (files == null)
            return;
        mFilePath = filePath;
        mFiles = files;
        mMinIndex = 0;
        mMaxIndex = files.length - 1;
        mRandomIndex = new int[files.length];
        setRandom(mRandomIndex);
    }

    private void setRandom(int[] array){
        int size = array.length;
        int[] source = new int[size];
        for(int i=0; i<size; i++) {
            source[i] = i;
        }
        Random rd = new Random();
        int range = array.length;
        for(int i = 0; i <array.length; i++){
            int pos = rd.nextInt(range);
            array[i] = source[pos];
            source[pos] = source[range-1];
            range--;
        }
    }

    /**
     * Play.
     */
    public void play() {
        synchronized (this) {
            mIsRun = true;
            if (mIsStop == true) {
                mThread = new AnimThread();
                mThread.start();
            }
        }
    }

    /**
     * Stop.
     */
    public void stop() {
        synchronized (this) {
            mIsRun = false;
            if (mThread.isInterrupted() == false) {
                mThread.interrupt();
            }
        }
    }

    /**
     * Checks if is playing.
     * 
     * @return true, if is playing
     */
    public boolean isPlaying() {
        return mIsRun;
    }

    /**
     * The Class AnimThread.
     */
    class AnimThread extends Thread {

        /**
         * Run.
         * 
         * @Description:
         */
        public void run() {
            mIsStop = false;
            while (mIsRun && getContext() != null) {
                mBitmap = null;

                boolean useUrlPicture = getPref().getBoolean(SettingPreference.USE_HTTP, false);
                boolean isContinue = false;
                if (DEBUG) Log.i(TAG, "useUrlPicture = "+useUrlPicture);

                if (false == useUrlPicture) {
                    isContinue = doLocalScreenSaver();
                } else {
                    isContinue = doHttpScreenSaver();
                }

                if (isContinue == false) {
                    break;
                }

                if (mIsRun == false) {
                    mIsStop = true;
                    break;
                }
                SystemClock.sleep(mThreadDuration);
            }
            mIsStop = true;
        }

        private boolean doLocalScreenSaver() {
            if (DEBUG) Log.i(TAG, "doLocalScreenSaver");
            // read a picture from files
            mBitmap = null;
            if (mFiles == null || mCurIndex >= mFiles.length) {
                // no file in folder
                mHandler.obtainMessage(ScreenSaverManager.ERROR_NO_IMAGE_IN_FOLDER).sendToTarget();
                return false;
            }

            int recordIndex = mCurIndex;

            while (mCurIndex <= mMaxIndex) {
                String fileName = mFiles[mRandomIndex[mCurIndex]].getPath();
                if (ImageUtil.isImage(fileName)) {
                    mBitmap = ImageUtil.getImageFitScreen(mContext, fileName);
                    if (DEBUG) Log.i(TAG, fileName);
                }

                if (mBitmap == null) {
                    mCurIndex++;
                } else {
                    break;
                }
            }

            if (mCurIndex > mMaxIndex && recordIndex == 0) {
                // has file but no image in folder
                mHandler.obtainMessage(ScreenSaverManager.ERROR_NO_IMAGE_IN_FOLDER).sendToTarget();
                return false;
            }

            if (mBitmap != null) {
                mHandler.post(ScreenSaverView.this);
                if (++mCurIndex > mMaxIndex) {
                    mCurIndex = mMinIndex;
                    setRandom(mRandomIndex);
                }
            } else if (mCurIndex >= mMaxIndex) {
                mCurIndex = mMinIndex;
                setRandom(mRandomIndex);
                return doLocalScreenSaver();
            }

            return true;
        }

        private boolean doHttpScreenSaver() {
            if (DEBUG) Log.i(TAG, "doHttpScreenSaver");
            // read a picture from url
            String urlStr = getPref().getString(SettingPreference.HTTP_URL, "");
            try {
                if(TextUtils.isEmpty(urlStr)){
                    mHandler.obtainMessage(ScreenSaverManager.ERROR_OTHER).sendToTarget();
                    return false;
                }
                final String path = ScreenSaverManager.TMP_PATH;
                final String fileName = ScreenSaverManager.TMP_NAME;
                urlStr = HttpDownloader.getFullUrl(urlStr);
                if (DEBUG) Log.i(TAG, "url = "+urlStr);
                if(HttpDownloader.containsFile(urlStr)){
                    getBitmap(urlStr,path,fileName);
                    if (mBitmap != null) {
                        mHandler.post(ScreenSaverView.this);
                    } else {
                        mIsStop = true;
                        return false;
                    }
                }else{
                    List<String> imagList = new ArrayList<String>();
                    if(!HttpDownloader.getImageSrc(urlStr, imagList)) {
                        mHandler.obtainMessage(ScreenSaverManager.ERROR_OTHER).sendToTarget();
                        return false;
                    }
                    if(imagList.size()-1 != mMaxIndex) {
                        mMaxIndex = imagList.size()-1;
                        mRandomIndex = new int[imagList.size()];
                        setRandom(mRandomIndex);
                    }
                    if (imagList == null || mCurIndex >= imagList.size()) {
                        // no file in folder
                        mHandler.obtainMessage(ScreenSaverManager.ERROR_NO_IMAGE_IN_FOLDER).sendToTarget();
                        return false;
                    }

                    int recordIndex = mCurIndex;

                    while (mCurIndex <= mMaxIndex) {
                        getBitmap(imagList.get(mRandomIndex[mCurIndex]),path,fileName);

                        if (mBitmap == null) {
                            mCurIndex++;
                        } else {
                            break;
                        }
                    }

                    if (mCurIndex > mMaxIndex && recordIndex == 0) {
                        // has file but no image in folder
                        mHandler.obtainMessage(ScreenSaverManager.ERROR_NO_IMAGE_IN_FOLDER).sendToTarget();
                        return false;
                    }

                    if (mBitmap != null) {
                        mHandler.post(ScreenSaverView.this);
                        if (++mCurIndex > mMaxIndex) {
                            mCurIndex = mMinIndex;
                            setRandom(mRandomIndex);
                        }
                    } else if (mCurIndex >= mMaxIndex) {
                        mCurIndex = mMinIndex;
                        setRandom(mRandomIndex);
                        return doHttpScreenSaver();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    };

    public void getBitmap(String urlStr,String path,String fileName){
        int errorCode = HttpDownloader.downFileOverWrite(urlStr, path, fileName);
        mHandler.obtainMessage(errorCode).sendToTarget();
        if (errorCode == ScreenSaverManager.NO_ERROR) {
            // download ok
            ShellUtil.getInstance().execute("chmod 777 -R " + path);

            boolean isImage = FileUtils.isSupportImage(path + fileName);
            if (isImage == false) {
                mHandler.obtainMessage(ScreenSaverManager.ERROR_NOT_SUPPORT).sendToTarget();
            } else {
                Bitmap bm = ImageUtil.getImageFitScreen(mContext, path + fileName);
                if (bm == null) {
                    mHandler.obtainMessage(ScreenSaverManager.ERROR_PIC_TOO_BIG).sendToTarget();
                }
                mBitmap = bm;
            }
        }
    }
    /**
     * Instantiates a new screen guard view.
     * 
     * @param context
     *            the context
     */
    public ScreenSaverView(Context context, long idleDuration, long inDuration, long inDuration1, long inDuration2, long outDuration, long outDuration1,
            long outDuration2) {
        super(context);
        init(context);
        mAnimOutHelper = new AnimOutHelper(context, outDuration, outDuration1, outDuration2);
        mAnimInHelper = new AnimInHelper(context, inDuration, inDuration1, inDuration2);
        mThreadDuration = Math.max(outDuration, inDuration) + idleDuration;
    }

    /**
     * Inits the.
     * 
     * @param context
     *            the context
     */
    private void init(Context context) {
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.main, this);
        mHandler = new ScreenSaverHandler();
        setOnTouchListener(this);
        setOnClickListener(this);
    }

    /**
     * Run.
     * 
     * @Description:
     */
    @Override
    public void run() {
        if (mPreImageView != null) {
            ImageView iv = mPreImageView;
            mPreImageView = mImageView;
            mImageView = iv;
            mImageView.bringToFront();
            mPreImageView.startAnimation(mAnimOutHelper.getRandAnim());
        } else {
            mPreImageView = mImageView;
            mImageView = new ImageView(getContext());
            mImageView.setOnClickListener(this);
            mImageView.setOnTouchListener(this);
            mImageView.setMinimumHeight(ImageUtil.getDisplaySize(mContext).heightPixels);
            mImageView.setMinimumWidth(ImageUtil.getDisplaySize(mContext).widthPixels);
            addView(mImageView);

            // out animation of first image
            if (mPreImageView != null) {
                mPreImageView.startAnimation(mAnimOutHelper.getRandAnim());
            }
        }

        mImageView.setImageBitmap(mBitmap);
        mImageView.startAnimation(mAnimInHelper.getRandAnim());
    }

    /**
     * On click.
     * 
     * @param v
     *            the v
     * @Description:
     */
    @Override
    public void onClick(View v) {
        MyLog.i(TAG, "onClick");
        doFinish();
    }

    /**
     * On touch.
     * 
     * @param v
     *            the v
     * @param event
     *            the event
     * @return true, if successful
     * @Description: just exit ScreenSaver
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        MyLog.i(TAG, "onTouch");
        doFinish();
        return true;
    }

    private void doToast(String str) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, "", Toast.LENGTH_LONG);
        }
        mToast.setText(str);
        mToast.show();
    }

    public void doErrorShow(String str) {
        removeAllViews();
        if (mImageView != null) {
            mImageView.setLayoutParams(new LayoutParams(0, 0));
            mImageView.clearAnimation();
        }

        if (mPreImageView != null) {
            mImageView.setLayoutParams(new LayoutParams(0, 0));
            mImageView.clearAnimation();
        }
        stop();

        if (mTextView == null) {
            mTextView = new TextView(getContext());
            mTextView.setText(str);
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setTextColor(Color.WHITE);
            mTextView.setTextSize(getResources().getDimension(R.dimen.gs_error_text_size));
        }
        addView(mTextView);
    }
    public void doFinish() {
        if (mImageView != null) {
            mImageView.setLayoutParams(new LayoutParams(0, 0));
            mImageView.clearAnimation();
        }

        if (mPreImageView != null) {
            mImageView.setLayoutParams(new LayoutParams(0, 0));
            mImageView.clearAnimation();
        }
        stop();

        if (mContext instanceof Activity) {
            ((Activity) mContext).finish();
        } else if (mContext instanceof DreamService) {
            // stop Dream.
            ((DreamService) mContext).finish();
        }
    }

    private SharedPreferences getPref() {
        return mContext.getSharedPreferences(SettingPreference.PREFERENCES, 0);
    }

    public class ScreenSaverHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String urlStr;
            String toast;
            switch (msg.what) {
            case ScreenSaverManager.ERROR_NOT_A_PIC:
            case ScreenSaverManager.ERROR_NOT_SUPPORT:
                urlStr = getPref().getString(SettingPreference.HTTP_URL, "");
                if (DEBUG) Log.w(TAG, "ERROR_NOT_SUPPORT picture at url = " + urlStr);
                toast = LanguageUtil.getValue(LanguageUtil.TOAST_NOT_SUPPORTED_FILE);
                // doToast(toast);
                // doFinish();
                doErrorShow(toast);
                break;

            case ScreenSaverManager.ERROR_NOT_ENOUGH_SPACE:
                urlStr = getPref().getString(SettingPreference.HTTP_URL, "");
                if (DEBUG) Log.w(TAG, "ERROR_NOT_ENOUGH_SPACE picture at url = " + urlStr);
                toast = LanguageUtil.getValue(LanguageUtil.TOAST_SPACE_NOT_ENOUGH);
                // doToast(toast);
                // doFinish();
                doErrorShow(toast);
                break;

            case ScreenSaverManager.ERROR_PIC_TOO_BIG:
                toast = LanguageUtil.getValue(LanguageUtil.TOAST_4_LARGE_PICTURE);
                // doToast(toast);
                // doFinish();
                doErrorShow(toast);
                break;

            case ScreenSaverManager.ERROR_CONN_EXCEPTION:
            case ScreenSaverManager.ERROR_IO_EXCEPTION:
            case ScreenSaverManager.ERROR_OTHER:
                urlStr = getPref().getString(SettingPreference.HTTP_URL, "");
                if (DEBUG) Log.w(TAG, "ERROR_OTHER picture at url = " + urlStr);
                toast = LanguageUtil.getValue(LanguageUtil.TOAST_INTERNET_OR_ADDRESS_ERROR);
                // doToast(toast);
                // doFinish();
                doErrorShow(toast);
                break;

            case ScreenSaverManager.ERROR_NO_IMAGE_IN_FOLDER:
                if (DEBUG) Log.w(TAG, "ERROR_NO_IMAGE_IN_FOLDER picture in " + mFilePath);
                toast = LanguageUtil.getValue(LanguageUtil.TOAST_FOLDER_NO_PICTURE);
                // doToast(toast);
                // doFinish();
                doErrorShow(toast);
                break;

            default:
                break;
            }
        }
    }
}
