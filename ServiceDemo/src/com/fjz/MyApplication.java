package com.fjz;

import com.fjz.MyService.MyBinder;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class MyApplication extends Application{
	
	private String TAG = "testLog";
	private boolean flag;
	MyService myService;

	private ServiceConnection serviceConnectin = new ServiceConnection() {
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Log.i(TAG, "onServiceConnected()");  
            MyBinder myBinder = (MyBinder) iBinder;
            myService = myBinder.getMyService();
            myService.setContext(getApplicationContext());
            flag = true;
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected()");  
		}
		
	};
	
	private void bindService(){  
        Intent intent = new Intent(this,MyService.class);  
        Log.i(TAG, "bindService()");  
        bindService(intent, serviceConnectin, Context.BIND_AUTO_CREATE);  
    }  
      
    private void unBindService(){  
        Log.i(TAG, "unBindService() start....");  
        if(flag){  
            Log.i(TAG, "unBindService()");  
            unbindService(serviceConnectin);  
            flag = false;  
        }  
    } 
	
	@Override
	public void onCreate() {
		super.onCreate();
		bindService();
	}
	
	public MyService getMyService() {
		return myService;
	}

	public void setMyService(MyService myService) {
		this.myService = myService;
	}
	
}
