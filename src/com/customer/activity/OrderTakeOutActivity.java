package com.customer.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.customer.util.CreateSMS;
import com.customer.util.HttpUtil;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class OrderTakeOutActivity extends Activity {
	private TextView time;
	private String eattime, eattimestring,cusaddress;
	private EditText ecusaddress, eremark;
	private ProgressBar progressbar;
	private TheApplication app;
	private Handler mainHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (TheApplication) getApplication();
		setContentView(R.layout.activity_order_take_out);
		time = (TextView) findViewById(R.id.textView5);
		time.setTextColor(Color.RED);
		eremark = (EditText) findViewById(R.id.editText2);
		ecusaddress = (EditText) findViewById(R.id.textView2);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			eattime = bundle.getString("datetime");
			time.setText(eattime);
			time.setTextColor(Color.BLACK);
		}

		time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(OrderTakeOutActivity.this,
						EatTimeActivity.class);
				startActivity(intent);

			}
		});
		
		findViewById(R.id.button2).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						eattimestring = time.getText().toString();
						cusaddress = ecusaddress.getText().toString();
						boolean cancel = false;
						View focusView = null;
						if (TextUtils.isEmpty(cusaddress)) {
							ecusaddress.setError("请填入送餐地址");
							focusView = ecusaddress;
							cancel = true;
						} else {
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd hh:mm:ss");
							try {
								sdf.parse(eattimestring);
							} catch (Exception e) {
								time.setError("请添加就餐时间以便为您准备");
								// Toast.makeText(GoToRestActivity.this,"请添加就餐时间以便为您准备",
								// Toast.LENGTH_SHORT).show();
								focusView = time;
								cancel = true;
							}

						}

						if (cancel) {
							focusView.requestFocus();
						} else {
							final AlertDialog.Builder builder = new Builder(
									OrderTakeOutActivity.this);
							builder.setTitle("订单提交")
									.setMessage("订单将以短信形式发送到店家手机，确认无误后请点击“确定”")
									.setPositiveButton(
											"确定",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialoginterface,
														int i) {
													// 按钮事件
													Toast.makeText(
															OrderTakeOutActivity.this,
															"请稍候",
															Toast.LENGTH_SHORT)
															.show();
													progressbar = (ProgressBar) findViewById(R.id.progressBar1);
													progressbar
															.setVisibility(0);
													CreateSMS smsCreater = new CreateSMS();
													try {
														Toast.makeText(
																OrderTakeOutActivity.this,
																app.getRestTel(),
																Toast.LENGTH_LONG)
																.show();

//														sendSmsMessage(
//																app.getRestTel(),
//																smsCreater
//																		.createSMS(
//																				app.getUsername(),
//							发短信													eattimestring,
//																				eremark.getText()
//																						.toString(),
//																				app.getOrderJsonArray()));

														new Thread(
																progressThread)
																.start();
														// Toast.makeText(GoToRestActivity.this,
														// "SMS Sent",
														// Toast.LENGTH_LONG).show();
													} catch (Exception e) {
														Toast.makeText(
																OrderTakeOutActivity.this,
																"Failed to send SMS",
																Toast.LENGTH_LONG)
																.show();
														e.printStackTrace();
													}

												}
											})
									.setNegativeButton(
											"返回",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.dismiss();
												}
											}).show();
						}
					}
				});
		findViewById(R.id.button3).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						finish();

					}
				});
		this.mainHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				findViewById(R.id.progressBar1).setVisibility(View.GONE);
				switch (msg.what) {
				case 0:
					Toast.makeText(OrderTakeOutActivity.this, "对不起，请稍后再试",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(OrderTakeOutActivity.this, "下单成功",
							Toast.LENGTH_SHORT).show();
//					LoginActivity.instance.finish(); // 关闭LoginActivity
//					Intent intent = new Intent().setClass(
//							RegisterActivity.this, BeginActivity.class);
//					Bundle bundle = new Bundle();
//					bundle.putInt("customerid", msg.what);
//					intent.putExtras(bundle);
//					startActivity(intent);
//					finish();

					break;
				}
				super.handleMessage(msg);
			}

		};

	}

	Runnable progressThread = new Runnable() {
		@Override
		public void run() {
			Message msg = new Message();
			new Thread();
			String result = createOrder();
			int flag = Integer.parseInt(result);
			msg.what = flag;
			
			mainHandler.sendMessage(msg);
		}
	};
	

		
	private String createOrder(){
		int customerid = app.getCustomerid();
		int restid = app.getRestid();
		boolean delivery = app.getDelivery();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");       
		String maketime = sDateFormat.format(new java.util.Date());  
		double total = app.getTotal();
		String remark = eremark.getText().toString();
		String customeraddress = ecusaddress.getText().toString();
		// 查询参数
		JSONObject json = new JSONObject();
		String param = null;
		try {
			json.put("total", total);
			json.put("customerid", customerid);
			json.put("restid", restid);
			json.put("delivery", delivery);
			json.put("maketime", maketime);
			json.put("eattime", eattimestring);
			json.put("cusaddress", customeraddress);
//			json.put("numofpeo", numofpeo);
			if(remark!=null){
				json.put("remark", remark);
			}
			json.put("orderjsonarray", app.getOrderJsonArray());
			param = json.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			param = URLEncoder.encode(param, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String createString = "param="+param;
		// URL
		String url = HttpUtil.BASE_URL+"CreateOrderServlet?"+createString;
		// 查询返回结果
		return HttpUtil.queryStringForPost(url);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.go_to_rest, menu);
		return true;
	}

	private void sendSmsMessage(String phoneNumber, String message) {
		// 可以通过静态方法getDefault()方法来获得 一个SmsManager对象
		SmsManager sms = SmsManager.getDefault();
		PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,
				GoToRestActivity.class), 0);
		// if message's length more than 70 ,
		// then call divideMessage to dive message into several part ,and call
		// sendTextMessage()
		// else direct call sendTextMessage()
		if (message.length() > 70) {
			ArrayList<String> msgs = sms.divideMessage(message);
			for (String msg : msgs) {
				sms.sendTextMessage(phoneNumber, null, msg, pi, null);
			}
		} else {
			sms.sendTextMessage(phoneNumber, null, message, pi, null);
		}
		Toast.makeText(OrderTakeOutActivity.this, "短信发送完成", Toast.LENGTH_LONG)
				.show();
	}

}