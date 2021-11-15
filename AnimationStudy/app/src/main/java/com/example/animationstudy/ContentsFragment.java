package com.example.animationstudy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class ContentsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_layout, container, false);
        root.findViewById(R.id.first_fragment).setOnClickListener(v -> {
            NavHostFragment.findNavController(ContentsFragment.this).navigate(R.id.action_to_FirstFragment);
        });
        root.findViewById(R.id.second_fragment).setOnClickListener(v -> {
            NavHostFragment.findNavController(ContentsFragment.this).navigate(R.id.action_to_SecondFragment);
        });
        root.findViewById(R.id.thrid_fragment).setOnClickListener(v -> {
            NavHostFragment.findNavController(ContentsFragment.this).navigate(R.id.action_to_ThirdFragment);
        });
        root.findViewById(R.id.other_fragment).setOnClickListener(v -> {
            NavHostFragment.findNavController(ContentsFragment.this).navigate(R.id.action_to_OtherAnimFragment);
        });





        return root;
    }
}
