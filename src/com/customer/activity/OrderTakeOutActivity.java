package com.customer.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class OrderTakeOutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_take_out);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order_take_out, menu);
		return true;
	}

}
