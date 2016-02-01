package com.fjz;

import com.example.servicedemo.R;
import com.fjz.MyService.MyBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BindServiceActivity extends Activity implements OnClickListener{

	private String TAG = "testLog";
	private boolean flag;
	private Button btn_bind, btn_unbind, btn_next;
	
	private ServiceConnection serviceConnectin = new ServiceConnection() {
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Log.i(TAG, "onServiceConnected()");  
            MyBinder myBinder = (MyBinder) iBinder;
            MyService myService = myBinder.getMyService();
            myService.setContext(getApplicationContext());
            flag = true;
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected()");  
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_service);
		init();
	}
	
	private void bindService(){  
        Intent intent = new Intent(BindServiceActivity.this,MyService.class);  
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_bind:
			bindService();
			break;
		case R.id.btn_unbind:
			unBindService();
			break;
		case R.id.btn_next:
			Intent intent = new Intent(BindServiceActivity.this, StartServiceActivity.class);
			startActivity(intent);
			finish();
			break;
		}
	}
	
	private void init(){
		btn_bind = (Button) findViewById(R.id.btn_bind);
		btn_unbind = (Button) findViewById(R.id.btn_unbind);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_bind.setOnClickListener(this);
		btn_unbind.setOnClickListener(this);
		btn_next.setOnClickListener(this);
	}
	
}
