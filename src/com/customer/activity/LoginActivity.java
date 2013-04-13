package com.customer.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.customer.activity.R;
import com.customer.util.HttpUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity{
	public static LoginActivity instance = null;
	private EditText userText,pwdText;
	private Handler mainHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//实例化EditText
		userText = (EditText)findViewById(R.id.editText1);
		pwdText = (EditText)findViewById(R.id.editText2);
		
		instance = this; //指定关闭用
		 
		
		
		
		findViewById(R.id.loginBtn).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						String usertext = userText.getText().toString();
						String pwdtext = pwdText.getText().toString();
						boolean cancel = false;
						View focusView = null;
						if (TextUtils.isEmpty(usertext)) { //用户名为空
							userText.setError(getString(R.string.error_username_required));
							focusView  = userText;
							cancel = true;
						}else if (TextUtils.isEmpty(pwdtext)){ //密码为空
							pwdText.setError(getString(R.string.error_field_required));
							focusView = pwdText;
							cancel = true;
						}
						if (cancel) {
							focusView.requestFocus();
						} else {

							findViewById(R.id.waitingBar).setVisibility(0);
							new Thread(progressThread).start();
						}
					}
				});
		
		findViewById(R.id.registerBtn).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
						startActivity(intent);
						
						//跳转到注册页面
						
					}
				});
		
		this.mainHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				findViewById(R.id.waitingBar).setVisibility(View.GONE);
				switch(msg.what){
				case -3:
					Toast.makeText(LoginActivity.this, "网络错误",Toast.LENGTH_SHORT).show();
					break;
				case -2:
					Toast.makeText(LoginActivity.this, "密码错误",Toast.LENGTH_SHORT).show();
					break;
				case -1:
					Toast.makeText(LoginActivity.this, "无此账户",Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(LoginActivity.this, "登陆成功",Toast.LENGTH_SHORT).show();
					Intent intent = new Intent().setClass(LoginActivity.this, BeginActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("customerid", msg.what);
					intent.putExtras(bundle);
	    			startActivity(intent);
	    			finish();
					break;
				}
				super.handleMessage(msg);
			}
			
		};
	}




	//登录方法
	private int login(){
//		
		// 获得用户名称
		String username = userText.getText().toString();
		// 获得密码
		String password = pwdText.getText().toString();
		// 获得登录结果
		String result=query(username,password);
		String[] msgs = result.split(";");
		String[] flag = msgs[0].split("=");
		
		
		if (flag[0] != null && flag[0].equals("customerid")) {
			int customerid = Integer.parseInt(flag[1]);
			return customerid;
		} else if (result != null && result.equals("0")) {
			return -1;
		} else if (result != null && result.equals("1")) {
			return -2;
		} else
			return -3;
	}
	private String query(String username,String password){
		// 查询参数
		JSONObject json = new JSONObject();
		String param = null;
		String encoderParam = null;
		try {
			json.put("username", username);
			json.put("password", password);
			param = json.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			encoderParam = URLEncoder.encode(param, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String queryString = "param="+encoderParam;
		
		// URL
		String url = HttpUtil.BASE_URL+"LoginServlet?"+queryString;
		
		// 查询返回结果
		return HttpUtil.queryStringForPost(url);
	}

	Runnable progressThread = new Runnable(){
		@Override
		public void run() {
			Message msg = new Message();
				new Thread();
				 
					msg.what=login();
			
				mainHandler.sendMessage(msg);
			}
		};
		// 将用户信息保存到配置文件 //以后用bundle
//		private void saveUserMsg(String msg){
//			// 用户编号
//			String customerid = "";
//			// 用户名称
//			String username = "";
//			// 获得信息数组
//			String[] msgs = msg.split(";");
//			int idx = msgs[0].indexOf("=");
//			customerid = msgs[0].substring(idx+1);
//			idx = msgs[1].indexOf("=");
//			username = msgs[1].substring(idx+1);
//			// 共享信息
//			SharedPreferences pre = getSharedPreferences("user_msg", MODE_WORLD_WRITEABLE);
//			SharedPreferences.Editor editor = pre.edit();
//			editor.putString("customerid", customerid);
//			editor.putString("username", username);
//			editor.commit();
//		}
}

