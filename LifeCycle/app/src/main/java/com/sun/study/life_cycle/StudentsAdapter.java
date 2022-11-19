package com.sun.study.life_cycle;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.sun.study.life_cycle.entity.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.StudentsViewHolder> {

    private static String TAG = StudentsAdapter.class.getName();
    List<Student>  mData = new ArrayList<>();
    private MyFragment myFragment;
    ClassViewModel viewModel;
    public StudentsAdapter(MyFragment myFragment) {
        this.myFragment = myFragment;
        //与MyFragment里面的ViewModel 是一个对象。
        viewModel = new ViewModelProvider(myFragment).get(ClassViewModel.class);
        Log.d(TAG, "StudentsAdapter: sunxiang viewMode = " + viewModel);
    }

    @NonNull
    @Override
    public StudentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.student, parent, false);
        return new StudentsAdapter.StudentsViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsViewHolder holder, int position) {
        Student student = mData.get(position);
        holder.id.setText(String.valueOf(student.id));
        holder.name.setText(student.name);
        holder.name.setOnClickListener(v -> {
            Intent intent = new Intent(myFragment.getContext(), MyActivity.class);
            myFragment.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Student> students) {
        Log.d(TAG, "setData: sunxiang ");
        mData.clear();
        mData.addAll(students);
        notifyDataSetChanged();
    }

    public class StudentsViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView name;
        ConstraintLayout student;

        public StudentsViewHolder(@NonNull View itemView) {
            super(itemView);
            student = itemView.findViewById(R.id.student);
            id = itemView.findViewById(R.id.student_id);
            name = itemView.findViewById(R.id.student_name);
        }

    }
}
