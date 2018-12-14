package com.example.sunxiang.oneframe.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
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

  private NetLoadDialogImpl(Activity activity) {
    this.activity = new WeakReference<>(activity);
    this.dialogMap = new LruCache<>(CACHE_NUM);
  }

  public static void initInstance(Activity activity) {
    if( instanse == null ) {
      synchronized (NetLoadDialogImpl.class) {
        if(instanse == null) {
          instanse = new NetLoadDialogImpl(activity);
        }
      }
    }
  }

  public static NetLoadDialogImpl getInstanse() {
    return instanse;
  }



  @Override public void show(String dialogFlag, String dialogMsg, boolean cancelable) {
    tryRemoveDialog(dialogFlag);
    if (!activity.get().isFinishing()) {
      Dialog dialog = createDialog(activity.get());
      dialog.setCanceledOnTouchOutside(false);
      dialog.setCancelable(cancelable);

      if(!TextUtils.isEmpty(dialogMsg)) {
        setMessage(dialog, dialogMsg);
      }

      dialogMap.put(dialogFlag, dialog);

      dialog.show();
    }
  }

  @Override public void dismiss(String dialogFlag) {
    tryRemoveDialog(dialogFlag);
  }

  /**
   * try remove it from map then dismiss it
   *
   * @param dialogFlag dialogMap's key
   */
  private void tryRemoveDialog(String dialogFlag) {
    if (dialogMap != null) {
      Dialog previousDialog = dialogMap.remove(dialogFlag);

      if (previousDialog != null) {
        previousDialog.setOnDismissListener(null);
        previousDialog.setOnCancelListener(null);
        if (previousDialog.isShowing()) {
          //get the Context object that was used to great the dialog
          Context context = ((ContextWrapper) previousDialog.getContext()).getBaseContext();
          // if the Context used here was an activity AND it hasn't been finished or destroyed
          // then dismiss it
          if (context instanceof Activity) {

            // Api >=17
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
              if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                dismissWithExceptionHandling(previousDialog);
              }
            } else {

              // Api < 17. Unfortunately cannot check for isDestroyed()
              if (!((Activity) context).isFinishing()) {
                dismissWithExceptionHandling(previousDialog);
              }
            }
          } else {
            // if the Context used wasn't an Activity, then dismiss it too
            dismissWithExceptionHandling(previousDialog);
          }
        }
        previousDialog = null;
      }
    }
  }

  private void dismissWithExceptionHandling(Dialog dialog) {
    try {
      dialog.dismiss();
    } catch (final IllegalArgumentException e) {
      // Do nothing.
    } catch (final Exception e) {
      // Do nothing.
    } finally {
      dialog = null;
    }
  }

  @Override public void cleanup() {
    // firstly cleanup all the dialogs (remove OnBackPressedListener from dialog)
    // firstly cleanup all the dialogs
    // then release the activity reference

    if (dialogMap != null) {
      for (Map.Entry<String, Dialog> entry : dialogMap.entrySet()) {
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

    //dialogMap = null;
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
