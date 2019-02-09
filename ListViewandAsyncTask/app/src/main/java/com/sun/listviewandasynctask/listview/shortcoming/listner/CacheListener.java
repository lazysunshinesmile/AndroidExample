package com.sun.listviewandasynctask.listview.shortcoming.listner;

import android.graphics.Bitmap;

public interface CacheListener {
    void setImageCache(String url, Bitmap bitmap);
}
