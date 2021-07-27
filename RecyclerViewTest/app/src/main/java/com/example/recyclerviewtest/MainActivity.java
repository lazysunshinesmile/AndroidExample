package com.example.recyclerviewtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private List<Item> mData;
    private ItemTouchCallbackImpl mItemTouchCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recycle);
        mAdapter = new RecyclerAdapter();
        mData = new ArrayList<>();
        for(int i=0; i< 1000; i++) {
            Item item = new Item(String.valueOf(i));
            mData.add(item);
        }
        mAdapter.setData(mData);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL/**分布的方向**/));
        mRecyclerView.setAdapter(mAdapter);
        mItemTouchCallback = new ItemTouchCallbackImpl(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mAdapter.setItemTouchHelper(itemTouchHelper);
    }
}