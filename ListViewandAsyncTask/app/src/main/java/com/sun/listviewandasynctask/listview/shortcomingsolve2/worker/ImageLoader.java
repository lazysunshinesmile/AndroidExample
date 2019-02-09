package com.sun.listviewandasynctask.listview.shortcomingsolve2.worker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


import com.sun.listviewandasynctask.listview.shortcomingsolve2.listner.CacheListener;

import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoader extends AsyncTask<String, Double, Bitmap> {

    private CacheListener mCacheListener;
    private String url;


    public ImageLoader(CacheListener cacheListner) {
        this.mCacheListener = cacheListner;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap = downloadImage(strings[0]);
        return bitmap;
    }

    private Bitmap downloadImage(String imageUrl) {
        Bitmap bitmap = null;
        HttpURLConnection con = null;
        this.url = imageUrl;
        try {
            URL url = new URL(imageUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5 * 1000);
            con.setReadTimeout(10 * 1000);
            bitmap = BitmapFactory.decodeStream(con.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return bitmap;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        mCacheListener.setImageCache(url, bitmap);
    }
}
