package com.example.sunxiang.faceapptest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

  private String TAG = "sunxiang";

  Button button;
  ThreadPoolExecutor threadPoolExecutor;

  String imagePath = Environment.getExternalStorageDirectory().getPath() + "/";
  String fileName = "timg.jpeg";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    button = findViewById(R.id.start);

    int corePoolSize = Runtime.getRuntime().availableProcessors();
    threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, 2 * corePoolSize, 1, TimeUnit.SECONDS,
        new LinkedBlockingDeque<Runnable>());

    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        threadPoolExecutor.execute(new Runnable() {
          @Override
          public void run() {
            try {
              //Detect API
//              startRequest();
              //compare API
              startRequest1();

            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });
      }
    });

  }

  public void startRequest() throws IOException {
//    https://console.faceplusplus.com/service/face/intro

//    apikeys:DyZwG6X4tD5TrAmhIP9Ckort5U60f5EX
//    apiseceret:PvAPhYZJH-4nqOSJd8SKdc9NrySnXVFC

    String apiKey = "DyZwG6X4tD5TrAmhIP9Ckort5U60f5EX";
    String apiSecret = "PvAPhYZJH-4nqOSJd8SKdc9NrySnXVFC";
    File file = new File(imagePath + fileName);

    if (!file.exists()) {
      Log.d(TAG, "startRequest: file is not exit");
      return;
    }


    String contentType = file.toURL().openConnection().getContentType();
    RequestBody fileBody = RequestBody.create(MediaType.parse(contentType), file);

    RequestBody requestBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("api_key", apiKey)
        .addFormDataPart("api_secret", apiSecret)
        .addFormDataPart("return_landmark", "2")
        .addFormDataPart("return_attributes", "gender,age")
        .addFormDataPart("image_file", "profile.png",
            fileBody)
        .build();


    //1.创建OkHttpClient对象
    OkHttpClient okHttpClient = new OkHttpClient();
    //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
    Request request = new Request.Builder()
        .url("https://api-us.faceplusplus.com/facepp/v3/detect")
        .post(requestBody)
        .build();
    //3.创建一个call对象,参数就是Request请求对象
    Call call = okHttpClient.newCall(request);
    //4.请求加入调度，重写回调方法
    call.enqueue(new Callback() {
      //请求失败执行的方法
      @Override
      public void onFailure(Call call, IOException e) {
        e.printStackTrace();
      }

      //请求成功执行的方法
      @Override
      public void onResponse(Call call, Response response) throws IOException {
        Log.d(TAG, "onResponse: " + response.body().string());
      }
    });
  }

  public void startRequest1() throws IOException {
//    apikeys:DyZwG6X4tD5TrAmhIP9Ckort5U60f5EX
//    apiseceret:PvAPhYZJH-4nqOSJd8SKdc9NrySnXVFC

    String apiKey = "DyZwG6X4tD5TrAmhIP9Ckort5U60f5EX";
    String apiSecret = "PvAPhYZJH-4nqOSJd8SKdc9NrySnXVFC";
    File file = new File(imagePath + fileName);

    if (!file.exists()) {
      Log.d(TAG, "startRequest: file is not exit");
      return;
    }


    String contentType = file.toURL().openConnection().getContentType();
    RequestBody fileBody = RequestBody.create(MediaType.parse(contentType), file);

    RequestBody requestBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("api_key", apiKey)
        .addFormDataPart("api_secret", apiSecret)
        .addFormDataPart("return_landmark", "2")
        .addFormDataPart("return_attributes", "gender,age")
        .addFormDataPart("image_file1", "profile.png", fileBody)
        .addFormDataPart("image_file2", "profile2.png", fileBody)
        .build();


    //1.创建OkHttpClient对象
    OkHttpClient okHttpClient = new OkHttpClient();
    //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
    Request request = new Request.Builder()
        .url("https://api-us.faceplusplus.com/facepp/v3/compare")
        .post(requestBody)
        .build();
    //3.创建一个call对象,参数就是Request请求对象
    Call call = okHttpClient.newCall(request);
    //4.请求加入调度，重写回调方法
    call.enqueue(new Callback() {
      //请求失败执行的方法
      @Override
      public void onFailure(Call call, IOException e) {
        e.printStackTrace();
      }

      //请求成功执行的方法
      @Override
      public void onResponse(Call call, Response response) throws IOException {
        Log.d(TAG, "onResponse: " + response.body().string());
      }
    });
  }

}
