package com.credr.productsearch.utils;

import com.credr.productsearch.controller.CredrTestApplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Utils {

	public static boolean getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork && 
				(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)){
			return true;
		}
		return false;
	}

	public static String createProductURl(String productName) {
		String mUrl = Constants.URL+ Constants.URL_SEARCH + productName;
		mUrl.replaceAll("\\s", "%20");
		Log.e(CredrTestApplication.TAG, " mUrl = " + mUrl);
		return mUrl;
	}
	
	public static boolean isValidQuery(String searchQuery) {

		return searchQuery != null && !searchQuery.isEmpty();
	}
}
