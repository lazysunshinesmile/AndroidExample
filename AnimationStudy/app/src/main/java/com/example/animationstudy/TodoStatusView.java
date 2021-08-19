package com.example.animationstudy;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

public class TodoStatusView extends androidx.appcompat.widget.AppCompatTextView {
    private final static String TAG = TodoStatusView.class.getSimpleName();

    private Paint mOutPaint;
    private Paint mInPaint;
    private float mOutRadius;
    private float mInRadius;
    boolean isTick;

    public TodoStatusView(Context context) {
        this(context, null);
    }

    public TodoStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TodoStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mOutPaint = new Paint();
        mOutPaint.setStyle(Paint.Style.FILL);
        mOutPaint.setColor(Color.BLACK);
        mInPaint = new Paint();
        mInPaint.setStyle(Paint.Style.FILL);
        mInPaint.setColor(Color.WHITE);

        isTick = false;
        mInRadius = -1;
        mOutPaint.setAntiAlias(true);
        mInPaint.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mOutRadius = Math.min(getWidth(), getHeight()) /2;
        if(mInRadius == -1) {
            mInRadius = mOutRadius * 2 / 5;
        }
        Log.d(TAG, "onLayout: mInRadius:" + mInRadius);
        Log.d(TAG, "onLayout: mOutRadius:" + mOutRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: mOutRadius:" + mOutRadius + ", mInRadius:" + mInRadius);
        float cx = getWidth()/2;
        float cy = getHeight()/2;
        canvas.drawCircle(cx, cy, mOutRadius, mOutPaint);
        canvas.drawCircle(cx, cy, mInRadius, mInPaint);
    }

    public void changeState() {
        float start = mOutRadius *2/5;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mInRadius = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(200);
        valueAnimator.start();


    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
