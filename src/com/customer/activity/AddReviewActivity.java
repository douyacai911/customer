package com.customer.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.customer.util.HttpUtil;

public class AddReviewActivity extends Activity {
	private int foodid = 0;
	private String dishname = "";
	private String review = "";
	private TextView tdishname;
	private EditText ereview;
	private RatingBar ratingbar;
	private float star = 0;
	private TheApplication app;

	private Handler mainHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_review);
		app = (TheApplication) getApplication(); 
		setTitle("添加评价");
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		foodid = bundle.getInt("foodid");
		dishname = bundle.getString("dishname");
		tdishname = (TextView) findViewById(R.id.textView1);
		tdishname.setText(dishname);
		ereview = (EditText) findViewById(R.id.editText1);
		ratingbar = (RatingBar) findViewById(R.id.ratingBar1);
		ratingbar.setStepSize(1);
		
		ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){

			   public void onRatingChanged(RatingBar ratingBar, float rating,
			     boolean fromUser) {
			    star = rating;
			   }});
		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						review = ereview.getText().toString();
						boolean cancel = false;
						View focusView = null;
						if (TextUtils.isEmpty(review)) { 
							ereview.setError("请添加评论内容");
							focusView  = ereview;
							cancel = true;
						} if (cancel) {
							focusView.requestFocus();
						} else {

							new Thread(progressThread).start();
						}
				}});
		
		findViewById(R.id.button2).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						finish();
					}
				});
		
		this.mainHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					Toast.makeText(AddReviewActivity.this, "评价添加成功",
							Toast.LENGTH_SHORT).show();
					finish();
					break;
				case 2:
					Toast.makeText(AddReviewActivity.this, "对不起，您曾对此菜品做出过评价，无法再次添加评价",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					
					Toast.makeText(AddReviewActivity.this, "对不起，请稍后再试",
							Toast.LENGTH_SHORT).show();

					break;
				}
				super.handleMessage(msg);
			}

		};
	}

	
	
	Runnable progressThread = new Runnable(){
		@Override
		public void run() {
			Message msg = new Message();
				new Thread();
				 
				String result = addreview();
				if(Integer.parseInt(result)==0){
					msg.what=0;
				}else if (Integer.parseInt(result)==2){
					msg.what=2;
				}else{
					msg.what=1;
				}
			
				mainHandler.sendMessage(msg);
			}
		};
		
		private String addreview(){
			// 查询参数
			JSONObject json = new JSONObject();
			String param = null;
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");       
			String maketime = sDateFormat.format(new java.util.Date()); 
			double thestar = star;
			try {
				if(star!=0){
					json.put("star", thestar);
				}
				json.put("review", ereview.getText().toString());
				json.put("foodid", foodid);
				json.put("customerid", app.getCustomerid());
				json.put("time", maketime);
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
			String registerString = "param="+param;
			// URL
			String url = HttpUtil.BASE_URL+"AddReviewServlet?"+registerString;
			// 查询返回结果
			return HttpUtil.queryStringForPost(url);
		}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_review, menu);
		return true;
	}

}
