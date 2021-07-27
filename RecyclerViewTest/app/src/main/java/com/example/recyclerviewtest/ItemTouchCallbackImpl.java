package com.example.recyclerviewtest;

import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemTouchCallbackImpl extends ItemTouchHelper.Callback {

    private final String TAG = ItemTouchCallbackImpl.class.getSimpleName();
    private RecyclerAdapter mAdapter;

    public ItemTouchCallbackImpl(RecyclerAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlag;
        int swipeFlags = 0;
        //如果是表格布局，则可以上下左右的拖动，但是不能滑动
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            dragFlag = ItemTouchHelper.UP |
                    ItemTouchHelper.DOWN |
                    ItemTouchHelper.LEFT |
                    ItemTouchHelper.RIGHT;
            swipeFlags = 0;
        } else {
            //如果是线性布局，那么只能上下拖动，只能左右滑动
            dragFlag = ItemTouchHelper.UP |
                    ItemTouchHelper.DOWN;
//            swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        }

        //通过makeMovementFlags生成最终结果
        return makeMovementFlags(dragFlag, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //被拖动的item位置
        int fromPosition = viewHolder.getLayoutPosition();
        //他的目标位置
        int targetPosition = target.getLayoutPosition();
        //为了降低耦合，使用接口让Adapter去实现交换功能

        mAdapter.switchData(viewHolder.getAbsoluteAdapterPosition(), target.getAbsoluteAdapterPosition());

        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.d(TAG, "onSwiped: direction:"+ direction);
        if(direction == ItemTouchHelper.LEFT) {
            mAdapter.remove(viewHolder.getAbsoluteAdapterPosition());
        } else {
            mAdapter.add();
            Log.d(TAG, "onSwiped: cannot remove");
        }
        
    }

    @Override
    public boolean isLongPressDragEnabled() {
        super.isLongPressDragEnabled();
        return true;
}
}
