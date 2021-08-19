package com.example.recyclerviewtest;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterWrapper extends RecyclerView.Adapter<AdapterWrapper.ViewHolderWrapper> {
    private BaseAdapter mAdapter;
    private OnMenuListener mMenuListener;

    public AdapterWrapper(BaseAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }


    @NonNull
    @Override
    public ViewHolderWrapper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wrapper, parent, false);
        ViewHolderWrapper vhw = new ViewHolderWrapper(itemView);
        RecyclerView.ViewHolder vh = mAdapter.onCreateViewHolder(parent, viewType);
        vhw.viewHolder = vh;
        vhw.adapterViewFL.addView(vh.itemView);
        return vhw;
    }

    public void setmMenuListener(OnMenuListener mMenuListener) {
        this.mMenuListener = mMenuListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderWrapper holder, int position) {
        holder.rightMenuIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuListener.onMenuClick(0);
            }
        });

       /* holder.adapterViewFL.setOnTouchListener(new View.OnTouchListener() {
            float x;
            float startX;
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("sunxiang", "onTouch: event action:" + event.getAction());

                if(event.getAction() == MotionEvent.ACTION_MOVE) {
                    float x2 = event.getX();
                    Log.d("sunxiang", "onTouch: x:" + x + ", x2:" + x2);
//                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(holder., "translationX", 0, x - x2);
//                    objectAnimator.setDuration(200);
//                    objectAnimator.start();
//                    holder.viewHolder.itemView.scrollby(x-x2, 0);
                    //>0 向左滑
                    int left = (int) (x - x2);
                    if(startX - x2 > holder.rightMenuIV.getWidth()) {
                        return false;
                    }
                    holder.adapterViewFL.scrollBy((int) (x - x2), 0);
                    x = x2;
                    return true;
                } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d("sunxiang", "onTouch: x:" + x);
                    x = event.getX();
                    startX = x;
                    return true;
                } else {
                    float x2 = event.getX();
                    Log.d("sunxiang", "onTouch: startX -x:" + (startX -x2));
                    if(startX - x2 < holder.rightMenuIV.getWidth()) {
                        holder.adapterViewFL.scrollTo(0, 0);
                    }
                }

                return false;
            }
        });*/

       mAdapter.onBindViewHolder(holder.viewHolder, position);
    }



    @Override
    public int getItemCount() {
        return mAdapter.getItemCount();
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
    }

    public void switchData(int absoluteAdapterPosition, int absoluteAdapterPosition1) {
        mAdapter.notifyItemSwap(absoluteAdapterPosition, absoluteAdapterPosition1);
    }

    public void remove(int absoluteAdapterPosition) {
        notifyItemRemoved(absoluteAdapterPosition);
        mAdapter.notifyItemRemove(absoluteAdapterPosition);
    }

    class ViewHolderWrapper extends RecyclerView.ViewHolder {
        RecyclerView.ViewHolder viewHolder;
        FrameLayout adapterViewFL;
        ImageView rightMenuIV;

        public ViewHolderWrapper(@NonNull View itemView) {
            super(itemView);
            adapterViewFL = itemView.findViewById(R.id.adapter_view_fl);
            rightMenuIV = itemView.findViewById(R.id.right_menu_iv);
        }


    }

    public interface OnMenuListener {
        void onMenuClick(int pos);
    }

    static abstract class BaseAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
        abstract List getData();
        abstract void notifyItemSwap(int i, int j);
        abstract void notifyItemRemove(int j);

    }
}
