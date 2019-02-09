package com.sun.listviewandasynctask.listview.shortcomingsolve1.adapter;

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
import android.widget.ListView;

import com.sun.listviewandasynctask.R;
import com.sun.listviewandasynctask.listview.shortcomingsolve1.listner.CacheListener;
import com.sun.listviewandasynctask.listview.shortcomingsolve1.worker.ImageLoader;

public class MyListAdapter extends ArrayAdapter<String> {

    private final String TAG = "MyListAdapter xiang";
    private LruCache<String, Bitmap> mCache;
    private String[] mImages;
//    private ImageView imageView;

    private ListView mListView;
    private String mUrl;
    private ViewHolder mViewHolder;

    private CacheListener mListener = new CacheListener() {
        @Override
        public void setImageCache(String url, Bitmap bitmap) {
//            mViewHolder.imageView = mListView.findViewWithTag(mUrl);
            if (mViewHolder.imageView != null && mViewHolder.imageView.getTag().equals(url))
                mViewHolder.imageView.setImageBitmap(bitmap);
            mCache.put(url, bitmap);
            notifyDataSetChanged();
        }
    };

    public MyListAdapter(@NonNull Context context, int resource, String[] images) {
        super(context, resource);
        mCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 8)) {
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
        if (mListView == null)
            mListView = (ListView) parent;
        String url = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_item, null);
            mViewHolder = new ViewHolder((ImageView) convertView.findViewById(R.id.imageitem));
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder = (ViewHolder)convertView.getTag();
        }

/**
 * 这里是比较重要的地方，对每一个imageView 设置了Tag，被复用的时候，Tag就会被覆盖。但这样还是下载完成之后，所以会比较浪费资源
 *
 */
        mUrl = url;
        mViewHolder.imageView.setTag(url);


        setImage(mViewHolder.imageView, url);
        Log.d(TAG, "getView: here");
        return convertView;
    }

    private void setImage(ImageView imageView, String url) {
        if (mCache.get(url) == null) {
            Log.d(TAG, "setImage: 网络");
            loadImage(imageView, url);
        } else {
            /**
             * 这个地方也要注意！！！！！！
             */
            Log.d(TAG, "setImage: 内存");
            if(url.equals(imageView.getTag()))
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

    class ViewHolder {
        ImageView imageView;

        public ViewHolder(ImageView imageView) {
            this.imageView = imageView;
        }
    }
}
