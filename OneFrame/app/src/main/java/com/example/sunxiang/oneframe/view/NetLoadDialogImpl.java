package com.example.sunxiang.oneframe.view;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.example.sunxiang.oneframe.R;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by sunxiang on 2018/12/13.
 */
public class NetLoadDialogImpl implements INetLoadDialog {

  private WeakReference<Activity> activity;

  private LruCache<String, Dialog> dialogMap;

  private int CACHE_NUM = 5;

  private static NetLoadDialogImpl instanse = null;

  private final String TAG = getClass().getSimpleName();

  private NetLoadDialogImpl(Activity activity) {
    this.activity = new WeakReference<>(activity);
    this.dialogMap = new LruCache<>(CACHE_NUM);
  }

  public static void initInstance(Activity activity) {
    if (instanse == null) {
      synchronized (NetLoadDialogImpl.class) {
        if (instanse == null) {
          instanse = new NetLoadDialogImpl(activity);
        }
      }
    }
  }

  public static NetLoadDialogImpl getInstanse() {
    return instanse;
  }


  @Override
  public void show(String dialogFlag, String dialogMsg, boolean cancelable) {


    if (!activity.get().isFinishing()) {
      //activity 没有结束，需要显示
      Dialog dialog = dialogMap.get(dialogFlag);
      //dialog 存在没有显示
      //dialog 不存在，
      //dialog 存在并显示了，不做处理

      if (dialog != null && !dialog.isShowing()) {
        dialog.show();
        return;
      } else if (dialog == null) {
        dialog = createDialog(activity.get());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(cancelable);

        if (!TextUtils.isEmpty(dialogMsg)) {
          setMessage(dialog, dialogMsg);
        }
        dialogMap.put(dialogFlag, dialog);
        dialog.show();
      }
    }
    //activity 结束了，不做显示
  }

  @Override
  public void dismiss(String dialogFlag) {
    tryRemoveDialog(dialogFlag);
  }

  /**
   * try remove it from map then dismiss it
   *
   * @param dialogFlag dialogMap's key
   */
  private void tryRemoveDialog(String dialogFlag) {
    Log.d(TAG, "tryRemoveDialog: size:" + dialogMap.size());

    if (dialogMap != null) {
      Dialog previousDialog = dialogMap.remove(dialogFlag);

      if (previousDialog != null) {
        previousDialog.setOnDismissListener(null);
        previousDialog.setOnCancelListener(null);
        if (previousDialog.isShowing()) {
          previousDialog.dismiss();
        }
      }

      Log.d(TAG, "tryRemoveDialog: size:" + dialogMap.size());
    }
  }

  @Override
  public void cleanup() {
    // firstly cleanup all the dialogs (remove OnBackPressedListener from dialog)
    // firstly cleanup all the dialogs
    // then release the activity reference
    Log.d(TAG, "cleanup: ");
    if (dialogMap != null) {
      Map<String, Dialog> map = dialogMap.snapshot();
      for (Map.Entry<String, Dialog> entry : map.entrySet()) {
        Dialog dialog = entry.getValue();
        if (dialog != null) {
          dialog.setOnDismissListener(null);
          dialog.setOnCancelListener(null);
          if (dialog.isShowing()) {
            dialog.dismiss();
          }
        }
      }
    }

    dialogMap = null;
    //activity = null;
  }


  private static Dialog createDialog(@NonNull Activity activity) {
    Dialog progress = new Dialog(activity, R.style.CustomProgressDialog);
    progress.setContentView(R.layout.view_loading);
    progress.getWindow().getAttributes().gravity = Gravity.CENTER;
    LinearInterpolator lir = new LinearInterpolator();
    Animation anim = AnimationUtils.loadAnimation(activity, R.anim.progress_loading);
    anim.setInterpolator(lir);
    progress.findViewById(R.id.loading_image_view).startAnimation(anim);
    return progress;
  }

  /**
   * [Summary]
   * setTitile 标题
   */
  public Dialog setTitile(Dialog progress, String strTitle) {
    // do set text
    return progress;
  }

  /**
   * [Summary]
   * setMessage 提示内容
   */
  public Dialog setMessage(Dialog progress, String strMessage) {
    TextView tvMsg = progress.findViewById(R.id.loading_msg_tv);

    if (tvMsg != null) {
      tvMsg.setText(strMessage);
      tvMsg.setVisibility(View.VISIBLE);
    }

    return progress;
  }
}
