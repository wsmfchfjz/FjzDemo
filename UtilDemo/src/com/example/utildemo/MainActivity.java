package com.example.utildemo;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new Thread(){
			public void run() {
				HttpService.getInstance().getCaptcha();
			};
		}.start();
		Log.d("a", "d");
		Log.w("a", "w");
		Log.e("a", "e");
		Log.i("a", "i");
		Log.v("a", "v");
	}

}
