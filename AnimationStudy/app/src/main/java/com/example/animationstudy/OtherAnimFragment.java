package com.example.animationstudy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class OtherAnimFragment extends Fragment {

    private String TAG = OtherAnimFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.other_anim, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UnderLineTextView underLineSpan = view.findViewById(R.id.underline_span);
        String content = underLineSpan.getText().toString();
        SpannableString ss = new SpannableString(content);

        underLineSpan.setOnClickListener(v -> {
            underLineSpan.startUnderlineAnimation();
        });

//        TodoStatusView todoStatusView = view.findViewById(R.id.circle);
//        todoStatusView.setOnClickListener(v -> {
//            todoStatusView.changeState();
//        });

        EditText et = view.findViewById(R.id.et);
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG, "onFocusChange: hasFocus:" + hasFocus + ". v:" + v);
            }
        });

       /* getActivity().getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch: sunxiang v:" + v + ", v.id:" + v.getId() + ", event:" + event.getAction());
                if(view.getId() != et.getId() && event.getAction() == MotionEvent.ACTION_UP){
                    Log.d(TAG, "onTouch: 点击了其他区域");
                    Toast.makeText(getActivity(), "点击了其他区域", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
*/
        Button heightBtn = view.findViewById(R.id.height_obj);
        heightBtn.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onViewCreated: heightBtn.getWidth():" + heightBtn.getWidth());

            }
        });
        TextView heightTV = view.findViewById(R.id.height_tv);
        int height = heightTV.getHeight();
        heightBtn.setOnClickListener(v -> {
            AnimatorSet animatorSet = new AnimatorSet();
            if(heightBtn.getText().equals("放大")) {
                heightTV.setVisibility(View.VISIBLE);
                ObjectAnimator alpha = ObjectAnimator.ofFloat(heightTV, View.ALPHA,  0,1);
                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(heightTV, "height", height, 600);
                objectAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        heightBtn.setText("缩小");
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                objectAnimator.setDuration(400);
                alpha.setDuration(400);
                animatorSet.playTogether(alpha, objectAnimator);
                animatorSet.start();
            } else {
                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(heightTV, "height", 600, height);
                ObjectAnimator alpha = ObjectAnimator.ofFloat(heightTV, View.ALPHA,  1,0);
                objectAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        heightBtn.setText("放大");
                        heightTV.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                objectAnimator.setDuration(400);
                alpha.setDuration(400);
                animatorSet.playTogether(alpha, objectAnimator);
                animatorSet.start();
            }
        });

        ConstraintLayout cl = view.findViewById(R.id.alpha_to_gone);
        cl.setOnClickListener(v -> {
            ObjectAnimator o = ObjectAnimator.ofFloat(cl, View.ALPHA, 1, 0);
            o.setDuration(400);
            o.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    cl.setVisibility(View.GONE);
                }
            });
            o.start();
        });

        EditAreaFrame editAreaFrame = view.findViewById(R.id.edit_area_frame);
        editAreaFrame.setGapWidth(144);


    }
}
