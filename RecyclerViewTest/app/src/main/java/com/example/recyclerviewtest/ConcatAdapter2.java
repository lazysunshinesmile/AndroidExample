package com.example.recyclerviewtest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ConcatAdapter2 extends RecyclerView.Adapter<ConcatAdapter2.FinishedViewHolder> {
    @NonNull
    @Override
    public FinishedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.finished_layout, viewGroup, false);
        return new FinishedViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FinishedViewHolder finishedViewHolder, int i) {
        finishedViewHolder.tv.setText("第 " + i + " 个已完成，" + System.currentTimeMillis());
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class FinishedViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public FinishedViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.textview);
        }
    }
}
