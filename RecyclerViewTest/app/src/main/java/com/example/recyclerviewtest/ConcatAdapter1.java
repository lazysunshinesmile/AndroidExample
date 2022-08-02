package com.example.recyclerviewtest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ConcatAdapter1 extends RecyclerView.Adapter<ConcatAdapter1.CreatedViewHolder> {
    @NonNull
    @Override
    public ConcatAdapter1.CreatedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.created_layout, viewGroup, false);
        return new CreatedViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ConcatAdapter1.CreatedViewHolder createdViewHolder, int i) {
        createdViewHolder.tv.setText("第 " + i + " 个未完成，");
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class CreatedViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        public CreatedViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.textview);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 10;
    }
}
