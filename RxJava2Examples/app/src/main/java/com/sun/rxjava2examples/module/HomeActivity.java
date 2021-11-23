package com.sun.rxjava2examples.module;

import android.os.Build;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sun.rxjava2examples.R;
import com.sun.rxjava2examples.base.BaseActivity;
import com.sun.rxjava2examples.base.BaseViewPagerAdapter;
import com.sun.rxjava2examples.constant.GlobalConfig;
import com.sun.rxjava2examples.module.rxjava2.operators.OperatorsFragment;
import com.sun.rxjava2examples.module.rxjava2.usecases.UseCasesFragment;
import com.sun.rxjava2examples.module.web.WebViewActivity;
import com.sun.rxjava2examples.util.ScreenUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    Toolbar mToolbar;
    TabLayout mTabLayout;
    AppBarLayout mAppbar;
    ViewPager mViewPager;
    TextView mToolbarTitle;
    FloatingActionButton mFab;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
//        StatusBarUtil.setTranslucent(this);
        mToolbar = findViewById(R.id.home_toolbar);
        mTabLayout = findViewById(R.id.home_tabLayout);
        mAppbar = findViewById(R.id.home_appbar);
        mViewPager = findViewById(R.id.home_viewPager);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mFab = findViewById(R.id.fab);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 4.4 以上版本
            // 设置 Toolbar 高度为 80dp，适配状态栏
            ViewGroup.LayoutParams layoutParams = mToolbarTitle.getLayoutParams();
//            layoutParams.height = ScreenUtil.dip2px(this,ScreenUtil.getStatusBarHeight(this));
            layoutParams.height = ScreenUtil.dip2px(this,80);
            mToolbarTitle.setLayoutParams(layoutParams);
        }

        initToolBar(mToolbar, false, "");
        String []titles = {
                GlobalConfig.CATEGORY_NAME_OPERATORS,
                GlobalConfig.CATEGORY_NAME_EXAMPLES
        };

        BaseViewPagerAdapter pagerAdapter = new BaseViewPagerAdapter(getSupportFragmentManager(),titles);
        pagerAdapter.addFragment(new OperatorsFragment());
        pagerAdapter.addFragment(new UseCasesFragment());

        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    /** 初始化 Toolbar */
    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }


    @OnClick(R.id.fab)
    public void onViewClicked() {
        WebViewActivity.start(this,"https://github.com/sun2251","我的GitHub,欢迎Star");
    }

    @Override
    protected boolean translucentStatusBar() {
        return true;
    }
}
