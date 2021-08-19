package com.example.recyclerviewtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

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
        AdapterWrapper adapterWrapper = new AdapterWrapper(mAdapter);
        adapterWrapper.setmMenuListener(new AdapterWrapper.OnMenuListener() {
            @Override
            public void onMenuClick(int pos) {
                Toast.makeText(getApplicationContext(), "点击了第 " + pos + "个munu", Toast.LENGTH_SHORT).show();
                startDrawRect();
            }
        });
        mData = new ArrayList<>();
        for(int i=0; i< 100; i++) {
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
        adapterWrapper.setItemTouchHelper(itemTouchHelper);
        mAdapter.setItemTouchHelper(itemTouchHelper);
        mItemTouchCallback.initAnim(itemTouchHelper);
    }

    private void startDrawRect() {
        Path path = new Path();
        path.addRect(10, 10, 100, 100, Path.Direction.CCW);
        PathMeasure pathMeasure = new PathMeasure(path, false);
        Canvas canvas = new Canvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawPath(path, paint);
    }
}