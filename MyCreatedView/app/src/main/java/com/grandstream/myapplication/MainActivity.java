package com.grandstream.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    private int content =0;
    private int lastClick = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BaseTabView baseTabView = findViewById(R.id.basetabview);
//        final TextView textView = findViewById(R.id.text);
//        textView.setTextSize(120);
        List<String> titles = new ArrayList<>();
        titles.add("nihao1asfdddddddddddddddddddddddddddddddddddddd");
        titles.add("nihao2");
        titles.add("nihao3");
        baseTabView.setTabTitles(titles);
        int child = baseTabView.getLinearLayout().getChildCount();
        Log.d(TAG, "xiangsun onCreate: child:" + child);

        baseTabView.setOnSelectedListener(new BaseTabView.OnSelectedListener() {
            @Override
            public void onSelected(TextView v, int count) {
                Log.d(TAG, "onSelected: select count:" + count);
//                textView.setText("第"+count+"个标题："+v.getText());

                v.setShadowLayer(27,0,0, Color.parseColor("#cc2b88f6"));
                if(lastClick != -1) {
                    baseTabView.getTitleView().get(lastClick).setShadowLayer(0, 0, 0, Color.parseColor("#4c000000"));
                }

                lastClick = count;
            }

        });
    }
}
