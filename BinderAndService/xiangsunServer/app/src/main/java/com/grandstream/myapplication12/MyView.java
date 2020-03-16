package com.grandstream.myapplication12;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyView extends LinearLayout {
    List<TextView> textViews;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        textViews = new ArrayList<>();
        for(int i = 0; i <4; i++) {
            TextView textView = new TextView(getContext());
            textView.setText("aaaa");
            textViews.add(textView);
            final  int finalI = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("xiangsun", "onClick: i:" + finalI);
                    Log.d("xiangsun", "onClick: v:" + v);
                    if(finalI <3) {
                        textViews.get(finalI).setText("cccc");
                        Log.d("xiangsun", "onClick: v2" + textViews.get(finalI));
                    }
                }
            });
            Log.d("xiangsun", "onMeasure: v:" + textView + ", size:" + textViews.size());
            addView(textView);
        }
    }
}
