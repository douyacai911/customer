package com.customer.activity;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class EatTimeActivity extends Activity {
	private DatePicker datepicker;
	private TimePicker timepicker;
	private boolean flag = false;
	private String date;
	private String time;
	private int mHour, mMinute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eat_time);
		setTitle("请确认日期和时间");
		
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		flag = bundle.getBoolean("delivery");
		datepicker = (DatePicker) findViewById(R.id.datePicker1);
		datepicker.setCalendarViewShown(false);

		timepicker = (TimePicker) findViewById(R.id.timePicker1);
		timepicker.setIs24HourView(true);
		mHour = timepicker.getCurrentHour();
		mMinute = timepicker.getCurrentMinute();

		timepicker
				.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
					@Override
					public void onTimeChanged(TimePicker view, int hourOfDay,
							int minute) {
						mHour = hourOfDay;
						mMinute = minute;

						/* 处理业务逻辑 */
					}
				});

		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						int month = datepicker.getMonth() + 1;
						int day = datepicker.getDayOfMonth();
						String monthString, dayString,minuteString,hourString;
						if (month < 10) {
							DecimalFormat df = new DecimalFormat("00");
							monthString = df.format(month);
						} else {
							monthString = String.valueOf(month);
						}
						if (day < 10) {
							DecimalFormat df = new DecimalFormat("00");
							dayString = df.format(day);
						} else {
							dayString = String.valueOf(day);
						}
						if (mHour < 10) {
							DecimalFormat df = new DecimalFormat("00");
							hourString = df.format(mHour);
						} else {
							hourString = String.valueOf(mHour);
						}
						if (mMinute < 10) {
							DecimalFormat df = new DecimalFormat("00");
							minuteString = df.format(mMinute);
						} else {
							minuteString = String.valueOf(mMinute);
						}
						date = datepicker.getYear() + "-" + monthString + "-"
								+ dayString;
						time = hourString + ":" + minuteString + ":00";
						String datetime = date + " " + time;
						Intent intent;
						if(!flag){
							intent = new Intent().setClass(EatTimeActivity.this, GoToRestActivity.class);
						}else{
							intent = new Intent().setClass(EatTimeActivity.this, OrderTakeOutActivity.class);
						}
								
						Bundle bundle = new Bundle();
						bundle.putString("datetime", datetime);
						intent.putExtras(bundle);
						startActivity(intent);
						finish();

					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.eat_time, menu);
		return true;
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        
        if(keyCode==KeyEvent.KEYCODE_BACK){

			Intent intent;
        	if(!flag){
				intent = new Intent().setClass(EatTimeActivity.this, GoToRestActivity.class);
			}else{
				intent = new Intent().setClass(EatTimeActivity.this, OrderTakeOutActivity.class);
			}
			startActivity(intent);
			finish();
            return true;
        }
        //继续执行父类的其他点击事件
        return super.onKeyDown(keyCode, event);
    }
}
