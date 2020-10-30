package com.grandstream.drawstudy.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class MyView extends View {
    public MyView(Context context) {
        super(context);
        initPaint();
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private Paint whitePaint;
    private Paint blackPaint;

    private int degree = 0;
    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint();

    }

    private void initPaint() {
        whitePaint = new Paint();
        whitePaint.setAntiAlias(true);
        whitePaint.setColor(Color.WHITE);
        blackPaint = new Paint();
        blackPaint.setAntiAlias(true);
        blackPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        canvas.translate(width/2, height/2);//将画布移动到中心
        canvas.drawColor(Color.GRAY);//绘制背景色

        canvas.rotate(degree);
        degree += 20;


        int radius = Math.min(width, height)/2;
        RectF rf = new RectF(-radius, -radius, radius, radius);
        canvas.drawArc(rf, -90, 180, true, whitePaint);
        canvas.drawArc(rf, 90, 180, true, blackPaint);

        int smallRadius = radius /2;
        canvas.drawCircle(0, -smallRadius, smallRadius, blackPaint);
        canvas.drawCircle(0, smallRadius,smallRadius,whitePaint);

        //太极的小圆
        int smallcicleRadius = smallRadius / 4; //小圆半径为大圆的一般
        canvas.drawCircle(0, -smallRadius, smallcicleRadius, whitePaint);
        canvas.drawCircle(0, smallRadius, smallcicleRadius, blackPaint);
//        invalidate();
    }
}
