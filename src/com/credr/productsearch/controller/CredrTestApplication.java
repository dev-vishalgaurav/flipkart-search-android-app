package com.credr.productsearch.controller;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.credr.productsearch.utils.LruBitmapCache;

public class CredrTestApplication extends Application {

	public static final String TAG = CredrTestApplication.class.getSimpleName();
	private static CredrTestApplication mInstance;
	RequestQueue reqQueue = null;
	ImageLoader imgLoader = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mInstance = this;
	}

	public static synchronized CredrTestApplication getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (reqQueue == null) {
			reqQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return reqQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (imgLoader == null) {
			imgLoader = new ImageLoader(this.reqQueue, new LruBitmapCache());
		}
		return this.imgLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (reqQueue != null) {
			reqQueue.cancelAll(tag);
		}
	}
}
