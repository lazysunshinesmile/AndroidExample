package com.example.animationstudy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.fragment.app.Fragment;

public class SpringAnimationFragment extends Fragment {

    private TextView mFirst;
    private TextView mSecond;
    private TextView mThird;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.spring_animation, container, false);
        mFirst = root.findViewById(R.id.first);
        mSecond = root.findViewById(R.id.second);
        mThird = root.findViewById(R.id.third);


        SpringForce force = new SpringForce();
        force.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY).setStiffness(SpringForce.STIFFNESS_VERY_LOW);
        SpringAnimation springXAnimation = new SpringAnimation(mFirst, SpringAnimation.TRANSLATION_X);
        SpringAnimation springYAnimation = new SpringAnimation(mFirst, SpringAnimation.TRANSLATION_Y);
        springXAnimation.setSpring(force);
        springYAnimation.setSpring(force);
        springXAnimation.setStartValue(0);
        springYAnimation.setStartValue(0);
        mFirst.setOnTouchListener(new View.OnTouchListener() {
            float x;
            float y;
            float startX;
            float startY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getRawX();
                        y = event.getRawY();
                        startX = x;
                        startY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:

//                        springXAnimation.animateToFinalPosition(event.getRawX() -startX);
//                        springYAnimation.animateToFinalPosition(event.getRawY() - startY);
                        springXAnimation.setStartValue(x- startX);
                        springXAnimation.animateToFinalPosition(event.getRawX()-startX);
                        springYAnimation.setStartValue(y-startY);
                        springYAnimation.animateToFinalPosition(event.getRawY()-startY);
//                        mFirst.setTranslationX(event.getRawX() - startX);
//                        mFirst.setTranslationY(event.getRawY() - startY);
                        x = event.getRawX();
                        y = event.getRawY();
                        break;

                    case MotionEvent.ACTION_UP:
                        springXAnimation.animateToFinalPosition(0);
                        springYAnimation.animateToFinalPosition(0);
                        break;


                }
                return true;
            }
        });
        return root;
    }
}
