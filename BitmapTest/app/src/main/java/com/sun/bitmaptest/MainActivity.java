package com.sun.bitmaptest;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    private Button bigBtn, smallBtn;
    private ImageView imageView;

    private BitmapFactory.Options options;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bigBtn = findViewById(R.id.big_pic);
        smallBtn = findViewById(R.id.small);
        imageView = findViewById(R.id.img);

        options = new BitmapFactory.Options();
        resources = getResources();
        bigBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options.inSampleSize = 2;
                Log.d(TAG, "onClick: options+" + options);
                Bitmap bitmap = BitmapFactory.decodeResource(resources, R.mipmap.big, options);
                options.inBitmap = bitmap;
                //onClick: bitmap:android.graphics.Bitmap@e554e9a options.inbitmap:android.graphics.Bitmap@e554e9a
                Log.d(TAG, "onClick: bitmap:" + bitmap + " options.inbitmap:" + options.inBitmap);
                Bitmap bitmap2 = BitmapFactory.decodeResource(resources, R.mipmap.small, options);
                //onClick: bitmap:android.graphics.Bitmap@e554e9a options.inbitmap:android.graphics.Bitmap@e554e9a
                Log.d(TAG, "onClick: bitmap2:" + bitmap2 + " options.inbitmap:" + options.inBitmap);

                //显示的是大图
                imageView.setImageBitmap(bitmap);
                //显示的是大图
                imageView.setImageBitmap(options.inBitmap);

                Log.d(TAG, "onClick: inBitmap.size"+ options.inBitmap.getDensity());
                bitmap.toString();
            }
        });
        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                options.inJustDecodeBounds = true; //加了这个，下面仅仅会解析图片大小，不会解析图片。
                Bitmap bitmap = BitmapFactory.decodeResource(resources, R.mipmap.small, options);
                imageView.setImageBitmap(bitmap);
                options.inBitmap = bitmap;
                Log.d(TAG, "onClick: inBitmap.size"+ options.inBitmap.getDensity());
            }
        });
    }
}
