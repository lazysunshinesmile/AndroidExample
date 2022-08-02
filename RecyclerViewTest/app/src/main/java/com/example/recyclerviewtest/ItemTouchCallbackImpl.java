package com.example.recyclerviewtest;

import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.logging.LogRecord;

public class ItemTouchCallbackImpl extends ItemTouchHelper.Callback {

    private final String TAG = ItemTouchCallbackImpl.class.getSimpleName();
    private RecyclerAdapter mAdapter;
    private SimulatedWaterAnim mSimulatedWaterAnim;
    private RecyclerView mRecyclerView;

    public ItemTouchCallbackImpl(RecyclerAdapter adapter, RecyclerView mRecyclerView) {
        this.mAdapter = adapter;
        this.mRecyclerView = mRecyclerView;
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
            swipeFlags = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
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
//        if(direction == ItemTouchHelper.RIGHT) {
            mAdapter.remove(viewHolder.getAbsoluteAdapterPosition());
        /*} else {
//            mAdapter.add();
            Log.d(TAG, "onSwiped: cannot remove");
        }*/
        
    }

    /**
     * 移动时触发，一般用于自定义主View交换动画效果。
     * @param c Canvas 画布
     * @param recyclerView RecyclerView RecyclerView
     * @param viewHolder ViewHolder 主View
     * @param dX Float 主View位移距离
     * @param dY Float 主View位移距离
     * @param actionState Int 拖动 or 横滑状态
     * @param isCurrentlyActive Boolean 是否由手控制位移
     */
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //进入我们的动画类
        mSimulatedWaterAnim.onDraw(c, recyclerView, viewHolder.itemView, dX, dY,
                actionState, isCurrentlyActive);
    }

    /**
     * 从静止状态变为拖拽或者滑动的时候会回调该方法
     * @param viewHolder ViewHolder? 主View
     * @param actionState Int 状态： 0=停止 1=横滑 2=拖动
     */
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        //进入我们的动画类
        mSimulatedWaterAnim.onSelected(viewHolder, actionState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: sunxiang error");
                mRecyclerView.requestDisallowInterceptTouchEvent(true);
            }
        }, 300);
    }

    /**
     * 当用户操作完毕某个item并且其动画也结束后会调用该方法，一般我们在该方法内恢复ItemView的初始状态
     * @param recyclerView RecyclerView RecyclerView本身
     * @param viewHolder ViewHolder 主View
     */
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //进入我们的动画类
        mSimulatedWaterAnim.clearView(viewHolder.itemView);
        //TODO-清空为主Item增加的标记，使用前文的id。
        viewHolder.itemView.setTag(R.string.app_name, 0);
    }


    @Override
    public boolean isLongPressDragEnabled() {
        super.isLongPressDragEnabled();
        return true;
}

    public void initAnim(ItemTouchHelper itemTouchHelper) {
        mSimulatedWaterAnim = new SimulatedWaterAnim(itemTouchHelper);
    }
}
