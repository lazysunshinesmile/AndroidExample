package com.example.animationstudy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends Fragment {

    private String TAG =  SecondFragment.class.getSimpleName();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_ThirdFragment);
            }
        });
        TextView none = view.findViewById(R.id.none);
        none.setOnClickListener(v->{
            showToast("匀速");
            startNone(v);
        });
        TextView acceTv = view.findViewById(R.id.accelerate);
        acceTv.setOnClickListener(v->{
            //加速
            showToast(AccelerateInterpolator.class.getSimpleName());
            startAcce(v);

        });

        TextView overshottTv = view.findViewById(R.id.overshot);
        overshottTv.setOnClickListener(v->{
            showToast(OvershootInterpolator.class.getSimpleName());
            startOvershoot(v);
        });

        TextView acc_de = view.findViewById(R.id.acc_de);
        acc_de.setOnClickListener(v->{
            showToast(AccelerateDecelerateInterpolator.class.getSimpleName());
            startAccDe(v);
        });

        TextView anti = view.findViewById(R.id.anti);
        anti.setOnClickListener(v -> {
            showToast(AnticipateInterpolator.class.getSimpleName());
            startAnti(v);
        });

        TextView antiOver = view.findViewById(R.id.anti_overshoot);
        antiOver.setOnClickListener(v ->{
            showToast(AnticipateOvershootInterpolator.class.getSimpleName());
            startAntiOverShoot(v);
        });
        TextView bounce = view.findViewById(R.id.bounce);
        bounce.setOnClickListener(v -> {
            showToast(BounceInterpolator.class.getSimpleName());
            startBounce(v);
        });

        TextView cycle = view.findViewById(R.id.cycle);
        cycle.setOnClickListener(v->{
            showToast(CycleInterpolator.class.getSimpleName());
            startCycle(v);
        });

        TextView decelerate = view.findViewById(R.id.decelerate);
        decelerate.setOnClickListener(v -> {
            showToast(DecelerateInterpolator.class.getSimpleName());
            startDece(v);
        });

        TextView mine =view.findViewById(R.id.mine);
        mine.setOnClickListener(v -> {
            showToast(MyInterpolator.class.getSimpleName());
            startMine(v);
        });




        view.findViewById(R.id.start).setOnClickListener(v->{
            startAcce(acceTv);
            startAccDe(acc_de);
            startAnti(anti);
            startAntiOverShoot(antiOver);
            startNone(none);
            startBounce(bounce);
            startOvershoot(overshottTv);
            startCycle(cycle);
            startDece(decelerate);
            startMine(mine);
        });



    }

    private void startMine(View v) {
        Interpolator de = new MyInterpolator();
        Animation an = new TranslateAnimation(v.getTranslationX(), width- v.getWidth(), v.getTranslationY(), v.getTranslationY());
        an.setInterpolator(de);
        an.setDuration(2000);
        v.startAnimation(an);
    }

    private void startDece(View v) {
        DecelerateInterpolator de = new DecelerateInterpolator();
        Animation an = new TranslateAnimation(v.getTranslationX(), width- v.getWidth(), v.getTranslationY(), v.getTranslationY());
        an.setInterpolator(de);
        an.setDuration(2000);
        v.startAnimation(an);
    }

    private void startCycle(View v) {
        Interpolator in = new CycleInterpolator(3f);
        Animation acceAnim = new TranslateAnimation(v.getTranslationX(), width-v.getWidth(), v.getTranslationY(), v.getTranslationY());
        acceAnim.setInterpolator(in);
        acceAnim.setDuration(2000);
        v.startAnimation(acceAnim);
    }

    private void startBounce(View v) {
        Interpolator in = new BounceInterpolator();
        Animation acceAnim = new TranslateAnimation(v.getTranslationX(), width-v.getWidth(), v.getTranslationY(), v.getTranslationY());
        acceAnim.setInterpolator(in);
        acceAnim.setDuration(2000);
        v.startAnimation(acceAnim);
    }


    private void startAntiOverShoot(View v) {
        Interpolator in = new AnticipateOvershootInterpolator();
        Animation acceAnim = new TranslateAnimation(v.getTranslationX(), width-v.getWidth(), v.getTranslationY(), v.getTranslationY());
        acceAnim.setInterpolator(in);
        acceAnim.setDuration(2000);
        v.startAnimation(acceAnim);
    }

    private void startNone(View v) {
        Interpolator in = new LinearInterpolator();
        Animation acceAnim = new TranslateAnimation(v.getTranslationX(), width-v.getWidth(), v.getTranslationY(), v.getTranslationY());
        acceAnim.setInterpolator(in);
        acceAnim.setDuration(2000);
        v.startAnimation(acceAnim);
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void startAnti(View v) {
        Interpolator in = new AnticipateInterpolator();
        Animation acceAnim = new TranslateAnimation(v.getTranslationX(), width-v.getWidth(), v.getTranslationY(), v.getTranslationY());
        acceAnim.setInterpolator(in);
        acceAnim.setDuration(2000);
        v.startAnimation(acceAnim);
    }

    private void startAccDe(View v) {
        Interpolator in = new AccelerateDecelerateInterpolator();
        Animation acceAnim = new TranslateAnimation(v.getTranslationX(), width-v.getWidth(), v.getTranslationY(), v.getTranslationY());
        acceAnim.setInterpolator(in);
        acceAnim.setDuration(2000);
        v.startAnimation(acceAnim);
    }

    private void startOvershoot(View view) {
        Interpolator in = new OvershootInterpolator();
        Animation acceAnim = new TranslateAnimation(view.getTranslationX(), width-view.getWidth(), view.getTranslationY(), view.getTranslationY());
        acceAnim.setInterpolator(in);
        acceAnim.setDuration(2000);
        view.startAnimation(acceAnim);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        width = getActivity().getWindowManager().getCurrentWindowMetrics().getBounds().width();
        Log.d(TAG, "onActivityCreated: width1:"+ width + ", width2:" + getActivity().getWindowManager().getDefaultDisplay().getWidth());
        height = getActivity().getWindowManager().getCurrentWindowMetrics().getBounds().height();
    }

    int width;
    int height;
    private void startAcce(View view) {
        Interpolator in = new AccelerateInterpolator();
        Animation acceAnim = new TranslateAnimation(view.getTranslationX(), width-view.getWidth(), view.getTranslationY(), view.getTranslationY());
        acceAnim.setInterpolator(in);
        acceAnim.setDuration(2000);
        view.startAnimation(acceAnim);
    }


    private class MyInterpolator implements Interpolator {

        @Override
        public float getInterpolation(float input) {
            return (float)(-Math.cos((input + 0.5d) * Math.PI));
        }
    }
}