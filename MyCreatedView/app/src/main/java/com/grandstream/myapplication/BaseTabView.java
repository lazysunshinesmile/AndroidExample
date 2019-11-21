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

import com.grandstream.myapplication.R;
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
    private boolean mChangeTextSizeWithAnim;

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

        //设置TextView 字体(选择/未选择)颜色
        mTextColor = ta.getColor(R.styleable.BaseTabView_android_textColor, getResources().getColor(R.color.color_label, null));
        mSelectedTextColor = ta.getColor(R.styleable.BaseTabView_selectedColor, getResources().getColor(R.color.warn_text, null));
        Log.d(TAG, "initAttrs: mTextColor:" + mTextColor);

        //设置Divider参数
        mDividerColor = ta.getColor(R.styleable.BaseTabView_dividerColor, getResources().getColor(R.color.color_input, null));
        mDividerHeight = ta.getDimensionPixelSize(R.styleable.BaseTabView_dividerHeight, 20);
        mDividerWidth = ta.getDimensionPixelSize(R.styleable.BaseTabView_dividerWidth, getScreenWidth());

        //设置title上下间距
        mTitlePaddingBottom = ta.getDimensionPixelSize(R.styleable.BaseTabView_tabPaddingBottom, 16);
        mTitlePaddingTob = ta.getDimensionPixelSize(R.styleable.BaseTabView_tabPaddingTob, 16);
        mTitlePaddingRight = ta.getDimensionPixelSize(R.styleable.BaseTabView_tabPaddingRight, 16);
        mTitlePaddingLeft = ta.getDimensionPixelSize(R.styleable.BaseTabView_tabPaddingLeft, 16);

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
        LinearLayout sonLinearLayout = new LinearLayout(getContext());
        ViewGroup.LayoutParams parentParams = new ViewGroup.LayoutParams(mDividerWidth, mDividerHeight);
        sonLinearLayout.setLayoutParams(parentParams);
        sonLinearLayout.setBackgroundColor(mDividerColor);

        parenLinearLayout.addView(sonLinearLayout);
        addView(parenLinearLayout);
    }


    private void initTitleView() {

        mTitleView = new ArrayList<>();
        Log.d(TAG, "onLayout: mtitles:" + mTitles.size());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(linearLayout.getOrientation() == LinearLayout.HORIZONTAL) {
            params.width = getScreenWidth()/mTitles.size();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            params.height = getScreenHeight()/mTitles.size();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        for(int i=0; i<mTitles.size();i++) {
            final TextView textView = new TextView(getContext());
            textView.setText(mTitles.get(i));
            textView.setLayoutParams(params);

            if(linearLayout.getOrientation() == LinearLayout.HORIZONTAL) {
                //水平
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
            } else {
                //竖直
                textView.setGravity(Gravity.CENTER_VERTICAL);
            }
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
						changeTextSizeWithAnimator(mChangeTextSizeWithAnim, lastSelectedTextView, mTextSizeAfterClick/mTextSize, 1.0f);
                        initTextView(lastSelectedTextView);
                        
                    }
                    textView.setPadding(0,0,0,0);
					changeTextSizeWithAnimator(mChangeTextSizeWithAnim, textView, 1.0f, mTextSizeAfterClick/mTextSize);
                    textView.setTextColor(mSelectedTextColor);
                    
                    if(listener != null) {
                        listener.onSelected(textView, finalI);
                    }
                    lastSelected = finalI;
                }
            });

            initTextView(textView);
			textView.setTextSize(mTextUnit, mTextSize);
            linearLayout.addView(textView);
            mTitleView.add(textView);

        }
        Log.d(TAG, "onClick: mTitleView.size = :" + mTitleView.size());
        Log.d(TAG, "xiangsun onLayout: " + linearLayout.getChildCount());

    }

    private void initTextView(TextView textView) {
        
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
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        ViewGroup.LayoutParams layoutParams;
        for(TextView view: mTitleView) {
            if(linearLayout.getOrientation() == LinearLayout.HORIZONTAL) {
                layoutParams = new LinearLayout.LayoutParams(width /mTitles.size(), ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , height/mTitles.size());
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

    public List<TextView> getmTitleView() {
        return mTitleView;
    }
}
