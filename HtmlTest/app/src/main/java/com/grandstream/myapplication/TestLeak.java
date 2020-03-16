package com.grandstream.myapplication;

import android.content.Context;
import android.widget.TextView;

public class TestLeak {
    private static TestLeak instance;
    private Context context;
    private static TextView textView;

    private TestLeak(Context context) {
        this.context = context;
    }

    public static TestLeak getInstance(Context context) {
        if(instance == null) {
            instance = new TestLeak(context);
        }
        return instance;
    }

    public static void setTextView(TextView tv) {
        textView = tv;
    }

    public static void setString(String aa) {
        textView.setText(aa);
    }


}
