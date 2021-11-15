package com.example.animationstudy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    public MySurfaceView(Context context) {
        super(context);
    }

    float x;
    float y;
    float lx;
    float ly;

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    Canvas mCanvas;
    SurfaceHolder mSurfaceHolder;

    private void initView() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        showToast("surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        showToast("surface changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        showToast("surface destoryed");
    }

    @Override
    public void run() {
        Canvas canvas = mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.WHITE);
//        draw(canvas);
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        canvas.drawLine(lx, ly, x, y, paint);
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    boolean start = false;

    @Override
    public boolean callOnClick() {
        start = !start;
        return super.callOnClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        lx = x;
        ly = y;
        x = event.getX();
        y = event.getY();
        invalidate();
        return false;
    }
}
