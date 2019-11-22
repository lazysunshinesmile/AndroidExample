package com.grandstream.myapplication;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class BaseTabView extends ScrollView {

    private static final String TAG = "BaseTabView";
    private LinearLayout parenLinearLayout = new LinearLayout(getContext());

    private LinearLayout linearLayout = new LinearLayout(getContext());

    private List<String> mTitles = new ArrayList<>();
    private List<TextView> mTitleView;
    private int lastSelected = -1;

    //属性
    private int mTextUnit = COMPLEX_UNIT_SP;
    private float mTextSize;
    private float mTextSizeAfterClick;
    private int mTitlePaddingBottom;
    private int mTitlePaddingTob;
    private int mTitlePaddingLeft;
    private int mTitlePaddingRight;
    private int mTitleMarginBottom;
    private int mTitleMarginTob;
    private int mTitleMarginLeft;
    private int mTitleMarginRight;
    private int mDividerMarginBottom;
    private int mDividerMarginTob;
    private int mDividerMarginLeft;
    private int mDividerMarginRight;
    private boolean mChangeTextSizeWithAnim;
    private int mSpaceTitleAndLine;

    //常态下的颜色
    private int mTextColor;
    private int mSelectedTextColor;
    private int mOriention;

    private int mDividerColor;
    private int mDividerHeight;
    private int mDividerWidth;


    private OnSelectedListener listener;

    public BaseTabView(Context context) {
        super(context);
        initView(null);
    }

    public BaseTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public BaseTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);

    }

    public BaseTabView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
    }

    private void initAttrs(AttributeSet attrs) {

        /**
         * attrs.xml写法
         * <?xml version="1.0" encoding="utf-8"?>
         * <resources>
         *     <declare-styleable name="BaseTabView">
         *         <attr name="android:textSize"/>
         *         <attr name="android:orientation"/>
         *         <attr name="myattr" format="string"/>
         *     </declare-styleable>
         *
         *
         * </resources>
         */


        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.BaseTabView);

        //获取方向
        mOriention = ta.getInt(R.styleable.BaseTabView_android_orientation, LinearLayout.HORIZONTAL);

        //获取普通字体大小
        mTextSize = ta.getDimensionPixelSize(R.styleable.BaseTabView_android_textSize, 60);

        //获取选择后的字体大小
        mTextSizeAfterClick = ta.getDimensionPixelSize(R.styleable.BaseTabView_textSizeAfterClick, (int) mTextSize);
        //获取是否需要动画控制标题变化
        mChangeTextSizeWithAnim = ta.getBoolean(R.styleable.BaseTabView_changeTextSizeWithAnim, false);

        //获取TextView 字体(选择/未选择)颜色
        mTextColor = ta.getColor(R.styleable.BaseTabView_android_textColor, getResources().getColor(R.color.color_label, null));
        mSelectedTextColor = ta.getColor(R.styleable.BaseTabView_selectedColor, getResources().getColor(R.color.warn_text, null));
        Log.d(TAG, "initAttrs: mTextColor:" + mTextColor);

        //获取title和分割线之间的差距
        mSpaceTitleAndLine = ta.getDimensionPixelSize(R.styleable.BaseTabView_spaceTitleAndLine, 0);

        //获取Divider参数
        mDividerColor = ta.getColor(R.styleable.BaseTabView_dividerColor, getResources().getColor(R.color.color_input, null));
        if(mOriention == LinearLayout.HORIZONTAL) {
            mDividerHeight = ta.getDimensionPixelSize(R.styleable.BaseTabView_dividerHeight, 0);
            mDividerWidth = ta.getDimensionPixelSize(R.styleable.BaseTabView_dividerWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            mDividerHeight = ta.getDimensionPixelSize(R.styleable.BaseTabView_dividerHeight, ViewGroup.LayoutParams.MATCH_PARENT);
            mDividerWidth = ta.getDimensionPixelSize(R.styleable.BaseTabView_dividerWidth, 0);
        }

        mDividerMarginBottom = ta.getDimensionPixelSize(R.styleable.BaseTabView_dividerMarginBottom, 0);

        mDividerMarginRight = ta.getDimensionPixelSize(R.styleable.BaseTabView_dividerMarginRight, 0);
        if(mOriention == LinearLayout.HORIZONTAL) {
            mDividerMarginTob = ta.getDimensionPixelSize(R.styleable.BaseTabView_dividerMarginTob, mSpaceTitleAndLine);
            mDividerMarginLeft = ta.getDimensionPixelSize(R.styleable.BaseTabView_dividerMarginLeft, 0);
        } else {
            mDividerMarginTob = ta.getDimensionPixelSize(R.styleable.BaseTabView_dividerMarginTob, 0);
            mDividerMarginLeft = ta.getDimensionPixelSize(R.styleable.BaseTabView_dividerMarginLeft, mSpaceTitleAndLine);
        }

        //获取title上下padding间距
        mTitlePaddingBottom = ta.getDimensionPixelSize(R.styleable.BaseTabView_tabPaddingBottom, 0);
        mTitlePaddingTob = ta.getDimensionPixelSize(R.styleable.BaseTabView_tabPaddingTob, 0);
        mTitlePaddingRight = ta.getDimensionPixelSize(R.styleable.BaseTabView_tabPaddingRight, 0);
        mTitlePaddingLeft = ta.getDimensionPixelSize(R.styleable.BaseTabView_tabPaddingLeft, 0);

        //获取title上下margin间距
        mTitleMarginBottom = ta.getDimensionPixelSize(R.styleable.BaseTabView_tabMarginBottom, 0);
        mTitleMarginTob = ta.getDimensionPixelSize(R.styleable.BaseTabView_tabMarginTob, 0);
        mTitleMarginRight = ta.getDimensionPixelSize(R.styleable.BaseTabView_tabMarginRight, 0);
        mTitleMarginLeft = ta.getDimensionPixelSize(R.styleable.BaseTabView_tabMarginLeft, 0);



    }

    private void initView(AttributeSet attrs) {
        parenLinearLayout.setOrientation(LinearLayout.VERTICAL);
        Log.d(TAG, "initView: mTitles:" + mTitles.size());

        //设置属性：
        linearLayout = new LinearLayout(getContext());

        initAttrs(attrs);

        //设置方向
        if(mOriention == LinearLayout.VERTICAL) {
            linearLayout.setOrientation(LinearLayout.VERTICAL);
        } else  {
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        }

        //设置linearlayout 大小
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(layoutParams);
        parenLinearLayout.addView(linearLayout);

        //分割线
        View dividerLine = new View(getContext());
        dividerLine.setBackgroundColor(mDividerColor);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(mDividerWidth, mDividerHeight);
        dividerParams.setMargins(mDividerMarginLeft,mDividerMarginTob,mDividerMarginRight,mDividerMarginBottom);

        dividerLine.setLayoutParams(dividerParams);

        parenLinearLayout.addView(dividerLine);
        addView(parenLinearLayout);
    }


    private void initTitleView() {

        mTitleView = new ArrayList<>();
        Log.d(TAG, "onLayout: mtitles:" + mTitles.size());


        for(int i=0; i<mTitles.size();i++) {
            final TextView textView = new TextView(getContext());
            textView.setText(mTitles.get(i));

            final int finalI = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: lastSelected:" + lastSelected  + ", view:" + v);
                    if(lastSelected != -1) {
                        TextView lastSelectedTextView = mTitleView.get(lastSelected);
                        if(lastSelectedTextView == v) {
                            Log.d(TAG, "onClick: xiangtong");
                            return;
                        }

                    }
					selectOne(finalI);
                }
            });

            initTextView(textView, true);
            textView.setTextSize(mTextUnit, mTextSize);
            linearLayout.addView(textView);
            mTitleView.add(textView);
        }
    }
	
	public void selectOne(int index) {
        if(lastSelected != -1 ) {
            TextView lastSelectedTextView = mTitleView.get(lastSelected);
            changeTextSizeWithAnimator(mChangeTextSizeWithAnim, lastSelectedTextView, mTextSizeAfterClick/mTextSize, 1.0f);
            initTextView(lastSelectedTextView, false);
        }
        TextView textView = mTitleView.get(index);
        changeTextSizeWithAnimator(mChangeTextSizeWithAnim, textView, 1.0f, mTextSizeAfterClick/mTextSize);
        textView.setTextColor(mSelectedTextColor);
        if(listener != null) {
            listener.onSelected(textView, index);
        }
        lastSelected = index;
    }

    private void initTextView(TextView textView, boolean init) {
        LinearLayout.LayoutParams params;
        if(init) {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            params = (LinearLayout.LayoutParams) textView.getLayoutParams();
        }
        params.setMargins(mTitleMarginLeft, mTitleMarginTob,mTitleMarginRight,mTitleMarginBottom);
        params.gravity = Gravity.CENTER;
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(mTextColor);
        textView.setPadding(mTitlePaddingLeft, mTitlePaddingTob, mTitlePaddingRight, mTitlePaddingBottom);
    }

    public void setTabTitles(List<String> titles) {
        mTitles = titles;
        initTitleView();
    }

    public void setTextSize(int unit, float textSize) {
        mTextUnit = unit;
        mTextSize = textSize;
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        this.listener = listener;
    }

    private void changeTextSizeWithAnimator(boolean isNeedAnim, TextView runView, float start, float end) {
        Log.d(TAG, "changeTextSizeWithAnimator: annim:" + isNeedAnim);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(runView, "scaleX", start, end);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(runView, "scaleY", start, end);

        AnimatorSet set = new AnimatorSet();
        set.play(animator1).with(animator2);
        if (isNeedAnim) {
            set.setDuration(100);
        } else {
            set.setDuration(0);
        }
        set.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: onMes");
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        for(TextView view: mTitleView) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            if(linearLayout.getOrientation() == LinearLayout.HORIZONTAL) {
                layoutParams.width = width / mTitles.size();
            } else {
                layoutParams.height = height / mTitles.size();
            }
            view.setLayoutParams(layoutParams);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed,l,t,r,b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    private int getScreenWidth() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    private int getScreenHeight() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }


    public interface OnSelectedListener {
        void onSelected(TextView v, int count);
    }

    public List<TextView> getTitleView() {
        return mTitleView;
    }

	public int getCurrentPosition() {
        return lastSelected;
    }
}
