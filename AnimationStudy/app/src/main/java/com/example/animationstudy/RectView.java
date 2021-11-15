package com.example.animationstudy;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Arrays;

public class RectView extends View {
    private Paint mPaint;
    private Paint mPaint1;
    private PathMeasure pathMeasure;
    private Path path = new Path();
    private Bitmap airplaneBitmap;
    private Context mContext;

    private float[] leftTop = new float[]{0, 0};
    private float[] rightDown = new float[]{0, 0};

    private float lastX;
    private float lastY;
    private float currentX;
    private float currentY;

    Path path1;
    Path path2;

    public RectView(Context context) {
        this(context, null);
    }

    public RectView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
//        mPaint.setStrokeWidth(80);
        mPaint.setTextSize(60);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(24);
        mPaint1 = new Paint();
        mPaint1.setColor(Color.BLACK);
        mPaint1.setAntiAlias(true);
//        mPaint1.setStrokeWidth(80);
        mPaint1.setTextSize(60);
        mPaint1.setStyle(Paint.Style.STROKE);
        mPaint1.setStrokeWidth(24);

        path1 = new Path();
        path2 = new Path();


    }

    boolean over1 = false;
    boolean over2 = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        if (!over1) {
            path1.lineTo(currentX, 0);
        } else {
            path1.lineTo(rightDown[0], currentY);
        }
        path2.lineTo(0, currentY);
        path2.lineTo(currentX, rightDown[1]);

        Log.d("sunxiang", "onDraw: " + "currentX:" + currentX + "currentY:" + currentY + "rightDown:" + Arrays.toString(rightDown));
        lastY = currentY;
        lastX = currentX;
        canvas.drawPath(path1, mPaint);
        canvas.drawPath(path2, mPaint);
    }

    public void start(float[] leftTop, float[] rightDown) {
        Log.d("sunxiang", "onDraw: leftTop:" + Arrays.toString(leftTop) + ", right:" + Arrays.toString(rightDown));
        path1.reset();
        path2.reset();
        this.leftTop = leftTop;
        this.rightDown = rightDown;
        lastX = leftTop[0];
        lastY = leftTop[1];
        currentX = leftTop[0];
        currentY = leftTop[1];
        path1.moveTo(lastX, lastY);
        path2.moveTo(lastX, lastY);
        ValueAnimator animator1 = ValueAnimator.ofFloat(leftTop[0], rightDown[0], leftTop[0]);
        ValueAnimator animator2 = ValueAnimator.ofFloat(leftTop[1], rightDown[1], leftTop[1]);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentX = (float) animation.getAnimatedValue();
                if (currentX == rightDown[0]) {
                    over1 = true;
                }
                invalidate();
            }
        });
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentY = (float) animation.getAnimatedValue();
                if (currentY == rightDown[1]) {
                    over2 = true;
                }
                invalidate();
            }
        });
        animator1.setDuration(20000);
        animator2.setDuration(20000);
        animator2.start();
        animator1.start();
        invalidate();

    }
}
