package com.fjz;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service{
	
	private String TAG = "testLog";
	private Context ctx;
	private MyBinder myBinder = new MyBinder();
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.i(TAG, "return myBinder");
		return myBinder;
	}
	
	public class MyBinder extends Binder{
		public MyService getMyService(){
			Log.i(TAG, "getMyService");
			return MyService.this;
		}
	}
	
	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(TAG, "onStart");
		super.onStart(intent, startId);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}
	
	public void setContext(Context ctx){  
		this.ctx = ctx;
        Log.i(TAG, "MyService-->setContext()");  
    }  

}
