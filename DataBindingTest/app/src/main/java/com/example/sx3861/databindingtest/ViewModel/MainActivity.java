package com.example.sx3861.databindingtest.ViewModel;

import android.arch.lifecycle.ViewModelProvider;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sx3861.databindingtest.Model.User;
import com.example.sx3861.databindingtest.R;
import com.example.sx3861.databindingtest.databinding.DataBindingTest;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private DataBindingTest mDataBindingTest;

    private User user;
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBindingTest = DataBindingUtil.setContentView(this, R.layout.activity_main);
        user = new User("sunxiang", 27);
        mDataBindingTest.setUser(user);
//        button = findViewById(R.id.button);
        mDataBindingTest.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count == 0 ) {
                    Log.d(TAG, "onClick: count = 0");
                    user.getName().set("caoqiushaung");
                    count++;
                }else {
                    user.getAge().set(26);
                }
            }
        });
    }
}
