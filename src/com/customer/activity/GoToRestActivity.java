package com.customer.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.PendingIntent;
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

public class GoToRestActivity extends Activity {
	private DatePicker datepicker;
	private TextView time,trestaddress;
	private String eattime, numofpeo, eattimestring,restaddress;
	private EditText num, eremark;
	private ProgressBar progressbar;
	private TheApplication app;
	private Handler mainHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (TheApplication) getApplication();
		setContentView(R.layout.activity_go_to_rest);
		num = (EditText) findViewById(R.id.editText1);
		time = (TextView) findViewById(R.id.textView5);
		time.setTextColor(Color.RED);
		eremark = (EditText) findViewById(R.id.editText2);
		trestaddress = (TextView) findViewById(R.id.textView2);
		trestaddress.setText(app.getRestAddress());

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
				
				
				Intent intent = new Intent().setClass(GoToRestActivity.this,
						EatTimeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putBoolean("delivery", false);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();

			}
		});
		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						String location = app.getRestLocation();
						String[] restCoord = location.split(",");
						double restlon = Double.parseDouble(restCoord[0]);
						double restlat = Double.parseDouble(restCoord[1]);
						Uri uri = Uri.parse("geo:0,0?q=" + restlat + ","
								+ restlon + "(" + app.getRestname()
								+ ")&hl=zh&z=15");
						String url = "http://ditu.google.cn/maps?hl=zh&mrt=loc&q="
								+ restlat + "," + restlon;
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);// LabelName�Ǳ�ǩ����
																			// lat
																			// long
																			// �Ǿ�γ��
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								& Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
						startActivity(intent);

					}
				});
		findViewById(R.id.button2).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						numofpeo = num.getText().toString();
						eattimestring = time.getText().toString();
						boolean cancel = false;
						View focusView = null;
						if (TextUtils.isEmpty(numofpeo)) {
							num.setError("�������ŵľͲ������Ա�Ϊ��׼��");
							focusView = num;
							cancel = true;
						} else {
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd hh:mm:ss");
							try {
								sdf.parse(eattimestring);
							} catch (Exception e) {
								time.setError("����ӾͲ�ʱ���Ա�Ϊ��׼��");
								// Toast.makeText(GoToRestActivity.this,"����ӾͲ�ʱ���Ա�Ϊ��׼��",
								// Toast.LENGTH_SHORT).show();
								focusView = time;
								cancel = true;
							}

						}

						if (cancel) {
							focusView.requestFocus();
						} else {
							final AlertDialog.Builder builder = new Builder(
									GoToRestActivity.this);
							builder.setTitle("�����ύ")
									.setMessage("�������Զ�����ʽ���͵�����ֻ���ȷ�������������ȷ����")
									.setPositiveButton(
											"ȷ��",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialoginterface,
														int i) {
													// ��ť�¼�
													Toast.makeText(
															GoToRestActivity.this,
															"���Ժ�",
															Toast.LENGTH_SHORT)
															.show();
													progressbar = (ProgressBar) findViewById(R.id.progressBar1);
													progressbar
															.setVisibility(0);
													CreateSMS smsCreater = new CreateSMS();
													try {
														Toast.makeText(
																GoToRestActivity.this,
																app.getRestTel(),
																Toast.LENGTH_LONG)
																.show();

//														sendSmsMessage(
//																app.getRestTel(),
//																smsCreater
//																		.createSMS(
//																				app.getUsername(),
//							������													eattimestring,
//																				numofpeo,
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
																GoToRestActivity.this,
																"Failed to send SMS",
																Toast.LENGTH_LONG)
																.show();
														e.printStackTrace();
													}

												}
											})
									.setNegativeButton(
											"����",
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
					Toast.makeText(GoToRestActivity.this, "�Բ������Ժ�����",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(GoToRestActivity.this, "�µ��ɹ�",
							Toast.LENGTH_SHORT).show();
//					LoginActivity.instance.finish(); // �ر�LoginActivity
//					Intent intent = new Intent().setClass(
//							RegisterActivity.this, BeginActivity.class);
//					Bundle bundle = new Bundle();
//					bundle.putInt("customerid", msg.what);
//					intent.putExtras(bundle);
//					startActivity(intent);
					finish();

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
//		boolean delivery = app.getDelivery();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");       
		String maketime = sDateFormat.format(new java.util.Date());  
		double total = app.getTotal();
		String remark = eremark.getText().toString();
		
		// ��ѯ����
		JSONObject json = new JSONObject();
		String param = null;
		try {
			json.put("total", total);
			json.put("customerid", customerid);
			json.put("restid", restid);
			json.put("delivery", false);
			json.put("maketime", maketime);
			json.put("eattime", eattimestring);
			json.put("numofpeo", numofpeo);
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
		// ��ѯ���ؽ��
		return HttpUtil.queryStringForPost(url);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.go_to_rest, menu);
		return true;
	}

	private void sendSmsMessage(String phoneNumber, String message) {
		// ����ͨ����̬����getDefault()��������� һ��SmsManager����
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
		Toast.makeText(GoToRestActivity.this, "���ŷ������", Toast.LENGTH_LONG)
				.show();
	}

}
