package com.example.animationstudy;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Arrays;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

public class FirstFragment extends Fragment {

    private String TAG = FirstFragment.class.getSimpleName();
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }
    boolean start = false;
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn = view.findViewById(R.id.button_first);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
                Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.set);
                anim.setFillAfter(false);
//                ScaleAnimation sc = new ScaleAnimation()
                btn.startAnimation(anim);

            }
        });

        ImageView img = view.findViewById(R.id.img);

        AnimationDrawable animationDrawable = new AnimationDrawable();
        for(int i=1; i<=7; i++) {
            int id = getResources().getIdentifier("a" + i, "drawable", getActivity().getPackageName());
            Drawable d = getResources().getDrawable(id);
            if(d == null) {
                Log.d("{sunxiang}", "onViewCreated: null");
            }
            animationDrawable.addFrame(d, 1000);
        }

        img.setOnClickListener((v) -> {
            /*img.setImageResource(R.drawable.zheng_ani);
            AnimationDrawable d = (AnimationDrawable) img.getDrawable();
            d.setOneShot(true);
            if(start) {
                d.stop();
                start =false;
            } else {
                d.start();
                start =true;
            }
*/

            animationDrawable.setOneShot(true);

            img.setImageDrawable(animationDrawable);
            if(start) {
                animationDrawable.stop();
                start =false;
            }
            
            else {
                start = true;
                animationDrawable.start();
            }

        });

        Button valueAnimOfInt = view.findViewById(R.id.value_anim_ofint);

        valueAnimOfInt.setOnClickListener(v->{
            ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 5);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int x = (int) animation.getAnimatedValue();
                    int oh = valueAnimOfInt.getHeight();
                    Log.d(TAG, "onAnimationUpdate: "+x +", " + oh);
                    Log.d(TAG, "onAnimationUpdate: animated fraction:" + animation.getAnimatedFraction());
                    valueAnimOfInt.setHeight(oh + x);
                    valueAnimOfInt.invalidate();
                    valueAnimator.setDuration(1000);
                }
            });
            valueAnimator.setDuration(1000);
            valueAnimator.start();


        });

        Button valueOfFloat = view.findViewById(R.id.value_anim_offloat);
        valueOfFloat.setOnClickListener(v -> {
            ValueAnimator animator = ValueAnimator.ofFloat(1, 7.4f);
            animator.addUpdateListener(animation -> {
                float f = (float) animation.getAnimatedValue();
                Log.d(TAG, "onViewCreated: animated fraction:" + animation.getAnimatedFraction());
                int width = (int) (valueOfFloat.getWidth() +  f);
                valueOfFloat.setText("当前宽度是：" + width);
                valueOfFloat.setWidth(width);

            });
            animator.start();
        });


        Button valueOfObj = view.findViewById(R.id.value_anim_of_obj);
        Point start = new Point(0f, valueOfObj.getHeight()*2f);
        Point end = new Point(getActivity().getWindowManager().getDefaultDisplay().getWidth() - valueOfObj.getWidth(),
        getActivity().getWindowManager().getDefaultDisplay().getHeight() - valueOfObj.getHeight());
        Log.d(TAG, "onViewCreated: start.x:" +start.x + ", start.y:" + start.y);
        Log.d(TAG, "onViewCreated: end.x:" +end.x + ", end.y:" + end.y);
        valueOfObj.setOnClickListener(v -> {
            ValueAnimator valueAnimator = ValueAnimator.ofObject(new TypeEvaluator() {
                @Override
                public Object evaluate(float fraction, Object startValue, Object endValue) {
                    Point startP = (Point) startValue;
                    Point endP = (Point) endValue;
//                    Log.d(TAG, "evaluate: startP.x:" + startP.x);
//                    Log.d(TAG, "evaluate: startP.y:" + startP.y);
                    Log.d(TAG, "evaluate: endP.x:" + endP.x);
//                    Log.d(TAG, "evaluate: endP.y:" + endP.y);
                    float x =  (startP.x + fraction*(endP.x - startP.x));
                    float y =  (startP.y + fraction*(endP.y - startP.y));
                    Log.d(TAG, "evaluate: .x:" + x + ", .y:" + y) ;

                    return new Point(x, y);
                }
            }, start, end);
            valueAnimator.setDuration(20);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Point p = (Point) animation.getAnimatedValue();
                    int[] locate = new int[2];
//                    valueOfObj.getLocationOnScreen(locate);
                    Rect globalRect = new Rect();
                    valueOfObj.getGlobalVisibleRect(globalRect);

                    Log.d(TAG, "onAnimationUpdate: .x:" + globalRect.left + ", top:" + globalRect.right);
                    Log.d(TAG, "onAnimationUpdate: locate:" + Arrays.toString(locate) + ", px:" + p.x + "p.y:" + p.y);
                    TranslateAnimation translateAnimation = new TranslateAnimation(valueOfObj.getTranslationX(), p.x, valueOfObj.getTranslationY(), p.y);
//                    valueOfObj.setAnimation(translateAnimation);
                    translateAnimation.setDuration(5000);
//                    translateAnimation.start();
                    valueOfObj.startAnimation(translateAnimation);

                }
            });
            valueAnimator.start();
        });

        Button objAnim = view.findViewById(R.id.obj_anim);
        objAnim.setOnClickListener(v->{
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(v,
                    "alpha",1f,0f,1f);
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofInt(v, "backgroundColor", GREEN,RED);
            ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(v, "rotation", 0f,360f);
            ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(v, "translationX", objAnim.getTranslationX(),
                    getActivity().getWindowManager().getDefaultDisplay().getWidth(), objAnim.getTranslationX());
            ObjectAnimator objectAnimator5 = ObjectAnimator.ofFloat(v, "scaleX", 1f,1.5f);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(objectAnimator1).before(objectAnimator2).before(objectAnimator3).before(objectAnimator4).with(objectAnimator5);
            animatorSet.setDuration(20000);
            animatorSet.start();
        });


        view.findViewById(R.id.interpolator_typeevaluator).setOnClickListener(v ->{
//            getActivity().getSupportFragmentManager().beginTransaction()
////                    .remove(FirstFragment.this)
//                    .replace(R.id.nav_host_fragment, new SecondFragment(), null)
//                    .commit();
            NavHostFragment.findNavController(FirstFragment.this)
                    .navigate(R.id.action_FirstFragment_to_SecondFragment);
        });







    }

    class Color {
        float val;

        public Color(float val) {
            this.val = val;
        }

        public float getVal() {
            return val;
        }

        public void setVal(float val) {
            this.val = val;
        }
    }

    class Point {
        float x = 0;
        float y = 0;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }
}