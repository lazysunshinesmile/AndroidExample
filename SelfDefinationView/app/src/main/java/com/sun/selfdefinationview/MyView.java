package com.sun.selfdefinationview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MyView extends View {
    private final static String TAG = "MyView";

    private Paint mPaint;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setARGB(255, 0, 0,0);
        mPaint.setStrokeWidth(12.0f);
        //封闭图形中间被颜色填满
//        mPaint.setStyle(Paint.Style.FILL);
        //封闭图形中间不填充颜色
        mPaint.setStyle(Paint.Style.STROKE);
        //
//        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "onMeasure: height:" + height + ", width:" + width);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private OnLayoutChangeListener listener = new OnLayoutChangeListener(){

        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPoint(12,12, mPaint);
        canvas.drawPoints(new float[]{20.0f,20.0f, 30.0f, 30.0f, 40.0f, 40.0f}, mPaint);
        canvas.drawRect(600, 600, 900, 900, mPaint);
//        canvas.drawLine(20f,20f, 400f, 400f, mPaint);
//        rx >= (right - left) /2 , ry >= (bottom - top)/2
//        canvas.drawRoundRect(600, 600, 900, 900, 300, 450, mPaint);
//        画圆
        canvas.drawCircle(250, 250, 100, mPaint);
//        保存当前画布状态
        canvas.save();
//        回滚到上次保存的状态
        canvas.restore();
//        相对于当前位置的平移
        canvas.translate(23,12);
//        旋转
        canvas.rotate(12f);




    }


}
