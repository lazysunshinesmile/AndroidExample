package com.example.recyclerviewtest;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerAdapter extends AdapterWrapper.BaseAdapter<RecyclerAdapter.MyViewHolder> {

    private static final String TAG = RecyclerAdapter.class.getSimpleName();
    private List<Item> mData = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    float lastX = 0;
    @Override
    public void onBindViewHolder(RecyclerAdapter.MyViewHolder holder, int position) {
        holder.tv.setText(mData.get(position).tag);
        /*holder.tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    lastX = event.getX();
                } else
                if(event.getAction() == MotionEvent.ACTION_MOVE) {
                    float moveX = event.getX() - lastX;
                    holder.tv.setTranslationX(moveX);
                }

                return false;
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Item> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.mItemTouchHelper = itemTouchHelper;
    }

    public ItemTouchHelper getItemTouchHelper() {
        return mItemTouchHelper;
    }

    public void switchData(int s, int t) {
        notifyItemMoved(s, t);
        Collections.swap(mData, s, t);
    }

    public void remove(int s) {
        notifyItemRemoved(s);
        Item item = mData.remove(s);
        notifyItemInserted(1);
        mData.add(1, item);
    }

    public void add() {
        mData.add(1, new Item("新增"));
        notifyItemInserted(1);
    }

    @Override
    List getData() {
        return mData;
    }

    @Override
    void notifyItemSwap(int f, int j) {
        Collections.swap(mData, f, j);
        notifyItemMoved(f, j);
    }

    @Override
    void notifyItemRemove(int p) {
        mData.remove(p);
//        notifyItemRemoved(p);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        Item item;
        TextView tv;

        public void change(MyViewHolder t) {
            Item tmp = t.item;
            t.item = this.item;
            this.item = tmp;

            this.tv.setText(this.item.tag);
            t.tv.setText(t.item.tag);
        }

        public Item getItem() {
            return item;
        }

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    if(mItemTouchHelper != null) {
//                        mItemTouchHelper.startDrag(MyViewHolder.this);
//                    }
//                    return false;
//                }
//            });
        }




    }


}
