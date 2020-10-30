package com.grandstream.drawstudy.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class ProgressView extends View {

    private static final String TAG = ProgressView.class.getName();
    private Paint mPaint;
    private int mPaintWidth = 12;
    private int minAngle = -90;
    private int curAngle = 0;


    public ProgressView(Context context) {
        super(context);
        initPaint();
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        //线条形式
//        mPaint.setStyle(Paint.Style.STROKE);
        //会把起点和终点连起来与路线形成封闭图形，中间的会填充满
//        mPaint.setStyle(Paint.Style.FILL);
        //测试与Paint.Style.STROKE，看该参数的介绍，是不定的，以下是介绍
        /**
         * Geometry and text drawn with this style will be both filled and
         * stroked at the same time, respecting the stroke-related fields on
         * the paint. This mode can give unexpected results if the geometry
         * is oriented counter-clockwise. This restriction does not apply to
         * either FILL or STROKE.
         */
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mPaintWidth);
        mPaint.setAntiAlias(false);



        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mPaintWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }


    private int sweepAngle = 30;
    private int startAngle = 0;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int heght = getHeight();
        int radius = Math.min(width, heght)/2 - mPaintWidth/2 ;
        sweepAngle += 40;
        startAngle += 20;
        Log.d(TAG, "onDraw: sweetAngle:" + sweepAngle + ", startAngel:" + startAngle);

        if (startAngle == minAngle) {
            sweepAngle += 6;
        }
        if (sweepAngle >= 300 || startAngle > minAngle) {
            startAngle += 6;
            if (sweepAngle > 20) {
                //保持结束位置不变
                sweepAngle -= 6;
            }
        }
        if (startAngle > minAngle + 300) {
            startAngle %= 360;
            minAngle = startAngle;
            sweepAngle = 20;
        }

        canvas.translate(width/2, heght/2);
        canvas.rotate(curAngle += 4);
        canvas.drawArc(new RectF(-radius, -radius, radius, radius), startAngle, sweepAngle,false, mPaint);
//        canvas.drawCircle(0, 0, radius, mPaint);
        invalidate();
    }
}
