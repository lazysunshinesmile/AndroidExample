package com.sun.selfdefinationview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Map;

public class PieChart extends View {

    private final String TAG = "PieChart";

    private int mWidth;
    private int mHeight;

    private Paint mPaint;
    private Map<String, Float> data;

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float radius = Math.min(mWidth, mHeight) / 2;
        canvas.drawCircle(mWidth / 2, mHeight / 2, radius, mPaint);
        mPaint.setColor(Color.YELLOW);
        mPaint.setStyle(Paint.Style.FILL);
        RectF oval = new RectF(mWidth / 2 - radius, mHeight / 2 - radius, mWidth / 2 + radius, mHeight / 2 + radius);
        float startAngle = 0;
        int start = 0;
        float total = 0f;
        if (data == null) {
            return;
        }
        for (String key : data.keySet()) {
            total += data.get(key);
        }
        for (String key : data.keySet()) {
            switch (start) {
                case 0:
                    mPaint.setColor(Color.GREEN);
                    break;
                case 1:
                    mPaint.setColor(Color.GRAY);
                    break;
                case 2:
                    mPaint.setColor(Color.YELLOW);
                    break;
                default:
                    mPaint.setColor(Color.BLUE);
                    break;

            }
            Log.d(TAG, "onDraw: 总共几个 " + key + "：" + start);
            start++;
            Log.d(TAG, "onDraw: start:" + startAngle + ", sweepAngle:" + data.get(key) / total * 360);
            canvas.drawArc(oval, startAngle, data.get(key) / total * 360, true, mPaint);
            startAngle += data.get(key) / total * 360;
        }

    }


    private double getRedFromdeg(float deg) {
        return (deg / 180 * Math.PI);
    }

    public void setData(Map<String, Float> data) {
        this.data = data;
        invalidate();
    }
}
