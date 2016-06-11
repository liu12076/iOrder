package com.eeccs.jimmy.iorderserve.tool;

/**
 * Created by User on 2016/6/11.
 */
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleyRequestManager {
    private static VolleyRequestManager mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private ImageLoader.ImageCache mImageCache = new BitmapLruCache();
    private static Context mContext;

    private VolleyRequestManager(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
        mImageCache = getImageCache();
        mImageLoader = new ImageLoader(mRequestQueue, mImageCache);
    }

    public static synchronized VolleyRequestManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyRequestManager(context);
        }
        return mInstance;
    }

    private ImageLoader.ImageCache getImageCache() {
        if (mImageCache == null) {
            mImageCache = new BitmapLruCache();
        }
        return mImageCache;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
