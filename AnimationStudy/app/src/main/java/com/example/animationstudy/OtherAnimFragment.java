package com.example.animationstudy;

import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class OtherAnimFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.other_anim, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UnderLineTextView underLineSpan = view.findViewById(R.id.underline_span);
        String content = underLineSpan.getText().toString();
        SpannableString ss = new SpannableString(content);

        underLineSpan.setOnClickListener(v -> {
            underLineSpan.startUnderlineAnimation();
        });

        TodoStatusView todoStatusView = view.findViewById(R.id.circle);
        todoStatusView.setOnClickListener(v -> {
            todoStatusView.changeState();
        });
    }
}
