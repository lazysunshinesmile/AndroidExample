package com.sun.rxjava2examples.module.rxjava2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sun.rxjava2examples.R;
import com.sun.rxjava2examples.base.BaseFragment;
import com.sun.rxjava2examples.model.OperatorModel;
import com.sun.rxjava2examples.module.rxjava2.operators.OperatorsAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Author: sun
 */

public abstract class CategoryBaseFragment extends BaseFragment implements OnRefreshListener {

    protected List<OperatorModel> data;

    @BindView(R.id.operators_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.operators_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_operators;
    }

    protected abstract void fillData();

    protected abstract void itemClick(int position);

    @Override
    protected void init() {
        fillData();
        OperatorsAdapter adapter = new OperatorsAdapter(data) {
            @Override
            public void onItemClick(int position) {
                itemClick(position);
            }
        };

//        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

        mRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mRefreshLayout.setOnRefreshListener(this);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        mRecyclerView.setAdapter(adapter);

    }


    @Override
    public void onRefresh() {
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        });
    }
}