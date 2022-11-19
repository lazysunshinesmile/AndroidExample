package com.sun.study.life_cycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sun.study.life_cycle.entity.Student;

import java.util.List;

public class MyFragment extends Fragment {


    private static final String TAG= MyFragment.class.getName();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.my_fragment, container, false);

        RecyclerView rv = inflate.findViewById(R.id.students_rv);

        //observer 是观察者, classLiveData 是被观察者
        ClassViewModel myViewModel = new ViewModelProvider(this).get(ClassViewModel.class);
        Log.d(TAG, "onCreate: sunxiang myViewModel= " + myViewModel);
        StudentsAdapter adapter = new StudentsAdapter(this);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);


        LiveData<List<Student>> classLiveData = myViewModel.getAllStudents("class1");
        Observer<List<Student>> observer = adapter::setData;
        /* 上面这行等价于下面这个
        Observer<List<Student>> observer = new Observer<List<Student>>() {
            @Override
            public void onChanged(List<Student> students) {
                adapter.setData(students);
            }
        };*/
        classLiveData.observe(getViewLifecycleOwner(), observer);
        return inflate;
    }
}
