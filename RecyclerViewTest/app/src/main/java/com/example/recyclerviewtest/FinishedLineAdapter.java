package com.example.recyclerviewtest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FinishedLineAdapter extends RecyclerView.Adapter<FinishedLineAdapter.FinishedLineViewHolder> {
    @NonNull
    @Override
    public FinishedLineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.finished_line, viewGroup, false);
        return new FinishedLineViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FinishedLineViewHolder finishedLineViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class FinishedLineViewHolder extends RecyclerView.ViewHolder {
        public FinishedLineViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
