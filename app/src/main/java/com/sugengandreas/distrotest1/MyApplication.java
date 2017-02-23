package com.sugengandreas.distrotest1;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
	private static MyApplication sInstance = null;

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
	}

	public static MyApplication getInstance() {
		return sInstance;
	}

	public static Context getAppContext() {
		return sInstance.getApplicationContext();
	}
}
