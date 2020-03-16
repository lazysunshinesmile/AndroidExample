package com.grandstream.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.leakcanary.RefWatcher;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.text.Html.FROM_HTML_MODE_LEGACY;
import static android.text.Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button btn;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RefWatcher refWatche = MyApplication.getRefWatcher(this);
        refWatche.watch(this);
        setContentView(R.layout.activity_main);

        final Context context = this;
        TestLeak.getInstance(this);
        textView = findViewById(R.id.ttt);
        TestLeak.setTextView(textView);

//        imageView = findViewById(R.id.kkkk);

        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestLeak.setString(System.currentTimeMillis() + "");
                Intent intent = new Intent(context, SecondActivity.class);
                startActivity(intent);
            }
        });

        requestPermissions(new String[]{
                WRITE_EXTERNAL_STORAGE,
                READ_EXTERNAL_STORAGE
        }, 1000);


        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("sjdfjsdlkf")
                .setMessage(Html.fromHtml("nihao <li> dklfjaslkj</li><li>dfsdfs</li>sadfdsf", Html.FROM_HTML_MODE_COMPACT))
                .show();
    }
}
