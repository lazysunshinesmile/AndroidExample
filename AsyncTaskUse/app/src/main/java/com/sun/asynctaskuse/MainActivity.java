package com.sun.asynctaskuse;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final Integer OK = 1;
    private String TAG = "MainActivity";

    private Button start, cancel;
    private TextView processText;
    private AsyncTask<String, Double, Integer> asyncTask;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = findViewById(R.id.start);
        cancel = findViewById(R.id.cancel);
        processText = findViewById(R.id.process_text);


        //做泛型的类型不能是基本类型
        asyncTask = new AsyncTask<String, Double, Integer>() {
            //参数类型由上面的AsyncTask中的第一个泛型决定
            @Override
            protected Integer doInBackground(String... strings) {
                Log.d(TAG, "doInBackground: " + strings[0]);
                for(int i = 0; i< 101; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    publishProgress((double) i);
                    Log.d(TAG, "doInBackground: 线程名："+Thread.currentThread().getName()+" 当前进度是：" + i + "%");
                }
                return OK;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d(TAG, "onPreExecute: 线程名："+Thread.currentThread().getName());
            }

            //参数类型由上面的AsyncTask中的第二个泛型决定
            @Override
            protected void onProgressUpdate(Double... values) {
                Log.d(TAG, "onProgressUpdate: 线程名："+Thread.currentThread().getName()+", " + values + "%");
                processText.setText("当前进度：" + values[0] + "%");
            }

            //参数类型由上面的AsyncTask中的第三个泛型决定
            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                if(integer.intValue() == OK) {
                    Log.d(TAG, "onPostExecute: 执行成功,线程名："+Thread.currentThread().getName());
                }
            }
        };
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncTask.execute("nihao");
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncTask.cancel(true);
            }
        });


    }
}
