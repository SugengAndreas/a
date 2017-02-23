package com.sugengandreas.distrotest1.network;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.sugengandreas.distrotest1.MyApplication;

public class VolleySingleton {
	public static VolleySingleton sInstance = null;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private VolleySingleton() {
		mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
		mImageLoader = new ImageLoader(mRequestQueue,
				new ImageLoader.ImageCache() {

					private LruCache<String, Bitmap> mImageCache = new LruCache<>(
							(int) (Runtime.getRuntime().maxMemory() / 1024 / 8));

					@Override
					public Bitmap getBitmap(String url) {
						return mImageCache.get(url);
					}

					@Override
					public void putBitmap(String url, Bitmap bitmap) {
						mImageCache.put(url, bitmap);
					}
				});

	}

	public static VolleySingleton getInstance() {
		if (sInstance == null) {
			sInstance = new VolleySingleton();
		}
		return sInstance;
	}

	public RequestQueue getRequestQueue() {
		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

}
