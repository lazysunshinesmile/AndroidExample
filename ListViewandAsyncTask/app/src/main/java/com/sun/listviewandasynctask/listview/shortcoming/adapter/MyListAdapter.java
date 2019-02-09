package com.sun.listviewandasynctask.listview.shortcoming.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.sun.listviewandasynctask.R;
import com.sun.listviewandasynctask.listview.shortcoming.listner.CacheListener;
import com.sun.listviewandasynctask.listview.shortcoming.worker.ImageLoader;

public class MyListAdapter extends ArrayAdapter<String> {

    private final String TAG = "MyListAdapter xiang";
    private LruCache<String, Bitmap> mCache;
    private String[] mImages;
    private ImageView imageView;

    private CacheListener mListener = new CacheListener() {
        @Override
        public void setImageCache(String url, Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            mCache.put(url, bitmap);
            notifyDataSetChanged();
        }
    };

    public MyListAdapter(@NonNull Context context, int resource, String[] images) {
        super(context, resource);
        mCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() /8)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        mImages = images;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String url = getItem(position);
        View view;
        if(convertView == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.image_item, null);
        else
            view = convertView;

        imageView = view.findViewById(R.id.imageitem);

        setImage(imageView, url);
        Log.d(TAG, "getView: here");
        return view;
    }

    private void setImage(ImageView imageView, String url) {
        if(mCache.get(url) == null) {
            loadImage(imageView, url);
        }else {
            imageView.setImageBitmap(mCache.get(url));
        }

    }

    private void loadImage(ImageView imageView, String url) {
        ImageLoader imageLoader = new ImageLoader(mListener);
        imageLoader.execute(url);
    }


    @Nullable
    @Override
    public String getItem(int position) {
        return mImages[position];
    }

    @Override
    public int getCount() {
        return mImages.length;
    }
}
