package com.cardview.study;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animationstudy.R;

import java.util.Random;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(System.currentTimeMillis() + "");
        holder.name.setBackgroundColor(new Random().nextInt());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemRemoved(position);
                notifyItemInserted(0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ViewGroup group;
        public MyViewHolder(View view) {
            super(view);
            group = view.findViewById(R.id.group);
            name = view.findViewById(R.id.name);
        }
    }
}
