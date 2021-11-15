package com.example.animationstudy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class EditAreaFrame extends TextView {
    private float mGapWidth;
    private Paint mFramePaint;
    private Context mContext;
    private float mLeftWidth = 10;
    private float mStrokeWidth;

    public EditAreaFrame(Context context) {
        super(context, null);
    }

    public EditAreaFrame(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditAreaFrame(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EditAreaFrame(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initPaint();
    }


    private void initPaint() {
        mFramePaint = new Paint();
        mFramePaint.setColor(Color.BLACK);
        mStrokeWidth = mContext.getResources().getDimension(R.dimen.frame_line_width);
        mLeftWidth = mContext.getResources().getDimension(R.dimen.frame_left_width);
        mFramePaint.setStrokeWidth(mStrokeWidth);
        mFramePaint.setAntiAlias(true);
    }

    public void setGapWidth(float gapWidth) {
        this.mGapWidth = gapWidth;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int right = getRight();
        int height = getHeight();
        canvas.translate(0, mStrokeWidth / 2);
        canvas.drawLine(0, 0, mLeftWidth, 0, mFramePaint);
        canvas.drawLine(mLeftWidth + mGapWidth, 0, right, 0, mFramePaint);
        canvas.translate(-mStrokeWidth / 2, 0);
        canvas.drawLine(right, 0, right, height, mFramePaint);
        canvas.translate(0, -mStrokeWidth);
        canvas.drawLine(right, height, 0, height, mFramePaint);
        canvas.translate(mStrokeWidth, 0);
        canvas.drawLine(0, height, 0, 0, mFramePaint);
    }
}
