package com.customer.activity;

import com.customer.activity.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class BeginActivity extends Activity {
	
	private TextView textview;   //TODO test
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_begin);
		
		textview = (TextView) findViewById(R.id.textView1);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		int customerid = bundle.getInt("customerid");
		String id = String.valueOf(customerid);
		textview.setText(id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.begin, menu);
		return true;
	}

}
