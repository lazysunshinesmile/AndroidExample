package com.example.animationstudy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TodoStatusView extends View {

    private Paint mOutPaint;
    private Paint mInPaint;

    public TodoStatusView(Context context) {
        super(context);
    }

    public TodoStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
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

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(20, 20, 10, mOutPaint);
        canvas.drawCircle(20, 20, 5, mInPaint);
    }
}
