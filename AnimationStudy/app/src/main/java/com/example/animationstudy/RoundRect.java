package com.example.animationstudy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class RoundRect extends View {

    private Paint mPaint;
    private Paint mRedPaint;

    public RoundRect(Context context) {
        this(context, null);
    }

    public RoundRect(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundRect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        mPaint.setStrokeWidth(10);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);

        mRedPaint = new Paint();
        mRedPaint.setColor(Color.RED);
        mRedPaint.setStyle(Paint.Style.STROKE);
        mRedPaint.setAntiAlias(true);
        Log.d("sunxiang", "init: ");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int radiusX = width / 8;
        int radiusY = width / 2;
        Log.d("sunxiang", "onDraw: width:" + width);

        RectF roundRect = new RectF(0, 0, width, getHeight());
        canvas.drawRoundRect(roundRect, radiusX, radiusY, mPaint);

        canvas.drawRect(roundRect, mRedPaint);
    }
}
