package com.fjz;

import com.example.servicedemo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartServiceActivity extends Activity implements OnClickListener{

	private Button btn_start, btn_stop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}
	
	@Override
	public void onClick(View v) {
		Intent it = new Intent(StartServiceActivity.this, MyService.class);
		switch (v.getId()) {
		case R.id.btn_start:
			startService(it);
			break;
		case R.id.btn_stop:
			stopService(it);
			break;
		}
	}
	
	private void init(){
		btn_start = (Button) findViewById(R.id.btn_start);
		btn_stop = (Button) findViewById(R.id.btn_stop);
		btn_start.setOnClickListener(this);
		btn_stop.setOnClickListener(this);
	}

	
}
