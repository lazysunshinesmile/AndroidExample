package com.example.animationstudy;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.provider.DocumentsContract;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

public class UnderLineTextView extends androidx.appcompat.widget.AppCompatTextView {

    private static final String TAG = UnderLineTextView.class.getSimpleName();

    private Paint mPaint;
    private DocumentsContract.Path mPath;
    private Rect mRect;
    private float mPercent;


    public UnderLineTextView(@NonNull Context context) {
        this(context, null);
    }

    public UnderLineTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public UnderLineTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mRect = new Rect();
        mPaint.setColor(Color.BLACK);
        mPercent = 0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//得到TextView显示有多少行
        super.onDraw(canvas);

        int count = getLineCount();

//得到TextView的布局
        final Layout layout = getLayout();

        float x_start, x_stop, x_diff;
        int firstCharInLine, lastCharInLine;

        for (int i = 0; i < count; i++) {

            //getLineBounds得到这一行的外包矩形,
            // 这个字符的顶部Y坐标就是rect的top 底部Y坐标就是rect的bottom
            int baseline = getLineBounds(i, mRect);
            firstCharInLine = layout.getLineStart(i);
            lastCharInLine = layout.getLineEnd(i);

            //要得到这个字符的左边X坐标 用layout.getPrimaryHorizontal
            //得到字符的右边X坐标用layout.getSecondaryHorizontal
            x_start = layout.getPrimaryHorizontal(firstCharInLine);
//            x_diff = layout.getPrimaryHorizontal(firstCharInLine + 1) - x_start;
//            x_stop = layout.getPrimaryHorizontal(lastCharInLine - 1) + x_diff;
            x_stop = layout.getLineRight(i);
//            x_start = layout.getLineStart(i);
//            x_stop= layout.getLineWidth(i);
            if(i == 1) {
                Log.d(TAG, "onDraw: firstCharInLine:" + firstCharInLine + " lastCharInLine:" + lastCharInLine);
                Log.d(TAG, "onDraw: xstop:" + x_stop);
            }
            x_stop *= mPercent;
            canvas.drawLine(x_start, baseline + 5/2f+3, x_stop, baseline + 5/2 + 3, mPaint);


        }
//        Bitmap bitmap = getBitmapFromDrawable(getContext(), R.drawable.vd_todo_red_full_stop_en);
//        canvas.drawBitmap(bitmap, getLeft(), getTop(), mPaint);
        int baseline = getLineBounds(count-1, mRect);
        Drawable drawable = getContext().getDrawable(R.drawable.vd_todo_red_full_stop_en);
        int left = (int) layout.getLineRight(count - 1);
        int top = layout.getLineBottom(count - 1) - drawable.getIntrinsicHeight();
        int right = (int) (layout.getLineRight(count - 1) + drawable.getIntrinsicWidth());
        int bottom = layout.getLineBottom(count - 1);
        drawable.setBounds(left, top, right, bottom);
        drawable.draw(canvas);
    }

    public static Bitmap getBitmapFromDrawable(Context context, @DrawableRes int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable || drawable instanceof VectorDrawableCompat) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    public void startUnderlineAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPercent = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(2000);
        valueAnimator.start();
    }

    public void setLineWidth(float x) {
        invalidate();
    }
}
