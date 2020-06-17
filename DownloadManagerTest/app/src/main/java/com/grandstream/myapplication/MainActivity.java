package com.grandstream.myapplication;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private DownloadManager mDownloadManager;
    private Uri uri;
    private long id;

    private boolean isDown = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        int isHas = checkSelfPermission("android.permission.INTERNET");
        isHas = checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        isHas = checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE");
        Log.d("TAG", "onCreate: ishas:" + isHas);
        if(isHas == PackageManager.PERMISSION_DENIED) {
            Log.d("TAG", "onCreate: quanxian jujue");
        } else if(isHas == PackageManager.PERMISSION_GRANTED){
            Log.d("TAG", "onCreate: quanxian yihuoqu");
        }
        requestPermissions(new String[]{"android.permission.INTERNET", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, 2000);
        final FloatingActionButton fab = findViewById(R.id.fab);
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        uri = Uri.parse("https://storage.evozi.com/apk/dl/16/09/05/us.zoom.videomeetings.apk?h=8Z_Xor-9uUFKjy_q5A2IkQ&t=1589268110&vc=46011");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isDown) {
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE| DownloadManager.Request.NETWORK_WIFI);

                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setVisibleInDownloadsUi(true);
                    id = mDownloadManager.enqueue(request);
                    isDown = true;
                    Toast.makeText(view.getContext(), "开始下载", Toast.LENGTH_SHORT).show();
                } else {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(id);
                    Cursor cursor = mDownloadManager.query(query);
                    if(cursor!= null && cursor.moveToFirst()){
                        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        if(status == DownloadManager.STATUS_FAILED) {
                            int reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
                            isDown = false;
                            Log.d("TAG", "onClick: reason:" + reason);
                        } else if(status == DownloadManager.STATUS_RUNNING) {
                            isDown =false;
                            mDownloadManager.remove(id);
                            long has = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR ));
                            long total = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                            Log.d("TAG", "onClick: has:" + has);
                            Log.d("TAG", "onClick: total:" + total);
                            Log.d("TAG", "onClick: " + (has * 100/total) + "%");
                            Toast.makeText(view.getContext(), "移除下载", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "onClick: 移除下载");,
                        } else  if(status == DownloadManager.STATUS_SUCCESSFUL) {
                            Toast.makeText(view.getContext(), "xiazai chenggong", Toast.LENGTH_SHORT).show();
                            isDown = false;
                            DownloadManager.Request request = new DownloadManager.Request(uri);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setMimeType("bin");
                            request.setVisibleInDownloadsUi(true);
                            id = mDownloadManager.enqueue(request);
                            Toast.makeText(view.getContext(), "开始下载", Toast.LENGTH_SHORT).show();
                        }
                    }


                }


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}