package com.customer.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
import android.widget.Toast;

import com.customer.util.HttpUtil;

public class RegisterActivity extends Activity {
	
	private EditText userText,pwdText,pwdText2,emailText,etel;
	private Handler mainHandler;
	private TheApplication app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setTitle("ע��");
		app = (TheApplication) getApplication(); 
		userText = (EditText)findViewById(R.id.userText);
		pwdText = (EditText)findViewById(R.id.pwdText);
		pwdText2 = (EditText)findViewById(R.id.pwdText2);
		emailText = (EditText)findViewById(R.id.emailText);
		etel = (EditText)findViewById(R.id.editText1);
		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {		//���ע�ᰴť
					@Override
					public void onClick(View view) {
						String pwdtext = pwdText.getText().toString();
						String pwdtext2 = pwdText2.getText().toString();
						String usertext = userText.getText().toString();
						String emailtext = emailText.getText().toString();
						String tel = etel.getText().toString();
						boolean cancel = false;
						View focusView = null;
						if (TextUtils.isEmpty(usertext)) {// �û���Ϊ��
							userText.setError(getString(R.string.error_username_required));
							focusView = userText;
							cancel = true;
						}
						else if (TextUtils.isEmpty(pwdtext)) { //����Ϊ��
							pwdText.setError(getString(R.string.error_field_required));
							focusView  = pwdText;
							cancel = true;
						} else if (pwdtext.length() < 4) { //����С��4λ
							pwdText.setError(getString(R.string.error_invalid_password));
							focusView = pwdText;
							cancel = true;
						} else if (!pwdtext.equals(pwdtext2)){ //���벻һ��
							pwdText2.setError(getString(R.string.error_not_same_password));
							focusView = pwdText2;
							cancel = true;
						} else if (TextUtils.isEmpty(emailtext)) { //emailΪ��
							emailText.setError(getString(R.string.error_email_required));
							focusView = emailText;
							cancel = true;
						} else if (!emailtext.contains("@")) { //email��ʽ����ȷ
							emailText.setError(getString(R.string.error_invalid_email));
							focusView = emailText;
							cancel = true;
						} else if (TextUtils.isEmpty(tel)) { //emailΪ��
							etel.setError("�����������ֻ�����");
							focusView = etel;
							cancel = true;
						}

						if (cancel) {
							focusView.requestFocus();
						} else {
							findViewById(R.id.progressBar1).setVisibility(0);

							new Thread(progressThread).start();
						}
					}
				});
		this.mainHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				findViewById(R.id.progressBar1).setVisibility(View.GONE);
				switch(msg.what){
				case -1:
					Toast.makeText(RegisterActivity.this, "�Բ��𣬴��û���������ע��",Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(RegisterActivity.this, "ע��ɹ�",Toast.LENGTH_SHORT).show();
					LoginActivity.instance.finish();	//�ر�LoginActivity
					Intent intent = new Intent().setClass(RegisterActivity.this, BeginActivity.class);
					app.setCustomerid(msg.what);
	    			startActivity(intent);
	    			finish();
	    			
					break;
				}
				super.handleMessage(msg);
			}
			
		};
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	
	Runnable progressThread = new Runnable(){
		@Override
		public void run() {
			Message msg = new Message();
				new Thread();
				 
					msg.what=register();
			
				mainHandler.sendMessage(msg);
			}
		};
		
		private int register(){
//			
			// ����û�����
			String username = userText.getText().toString();
			// �������
			String password = pwdText.getText().toString();
			String email = emailText.getText().toString();
			String tel = etel.getText().toString();
			String result = goRegister(username, password,email,tel);
			if (result != null && result.equals("-1")) {
				return -1;// �û����ظ�
			} 
				return Integer.parseInt(result);
		}
			
		private String goRegister(String username,String password,String email,String tel){
			// ��ѯ����
			JSONObject json = new JSONObject();
			String param = null;
			try {
				json.put("username", username);
				json.put("password", password);
				json.put("email", email);
				json.put("tel", tel);
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
			String url = HttpUtil.BASE_URL+"RegisterServlet?"+registerString;
			// ��ѯ���ؽ��
			return HttpUtil.queryStringForPost(url);
		}
}
