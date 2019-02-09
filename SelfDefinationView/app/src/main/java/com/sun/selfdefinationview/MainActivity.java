package com.sun.selfdefinationview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private PieChart mPieChart;
    private Button mBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPieChart = findViewById(R.id.piechart);
        mBtn = findViewById(R.id.change_data);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Float> data = new HashMap<>();
                data.put("name1", 12f);
                data.put("name2", 36f);
                data.put("name3", 90f);
                data.put("name4", 180f);
                mPieChart.setData(data);
            }
        });




    }

}
