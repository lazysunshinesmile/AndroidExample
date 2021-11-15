package com.example.animationstudy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ThirdFragment extends Fragment {
    //SurfaceView的学习

    Button mStartBtn;
    MySurfaceView mSurfaceView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mStartBtn = view.findViewById(R.id.start);
        mSurfaceView = view.findViewById(R.id.surface);
        mStartBtn.setOnClickListener(v -> {
            mSurfaceView.start = true;
            new Thread(mSurfaceView).start();
        });


    }


}
