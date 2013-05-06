package com.customer.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.customer.util.HttpUtil;

public class MyOrderActivity extends Activity {
	private int customerid = 0;
	private TextView textview;   //TODO test
	private Handler mainHandler;
	private SimpleAdapter listItemAdapter;
	private ListView list;
	private TheApplication app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order);
		app = (TheApplication) getApplication();
		list = (ListView) findViewById(R.id.listView1);

		setTitle("订单一览");
		customerid = app.getCustomerid();

		new Thread(progressThread).start();
		
		// 添加点击
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				HashMap<String, Object> thisorder = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
				int thisorderid = (Integer) thisorder.get("orderid");
				boolean flag = (Boolean) thisorder.get("completeflag");
				Intent intent = new Intent().setClass(MyOrderActivity.this, MyOrderDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("orderid", thisorderid);
				bundle.putBoolean("completeflag", flag);
				intent.putExtras(bundle);
				startActivity(intent);
			}
				});
		
		this.mainHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// findViewById(R.id.progressBar1).setVisibility(View.GONE);
				if(msg.what==-123){
					Toast.makeText(MyOrderActivity.this, "现无订单",Toast.LENGTH_SHORT).show();
				}else{
				listItemAdapter = new SimpleAdapter(MyOrderActivity.this,
						(ArrayList<HashMap<String, Object>>) msg.obj,// 数据源
						R.layout.my_order_list_layout,// ListItem的XML实现
						// 动态数组与ImageItem对应的子项
						new String[] { "restname", "maketime","total","flagString" },
						// ImageItem的XML文件里面的一个ImageView,两个TextView ID
						new int[] { R.id.textView7, R.id.textView2, R.id.textView4,R.id.textView6}

				);

				// 添加并且显示
				list.setAdapter(listItemAdapter);
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
			Object flag = (Object)jsonToOrder(customerid);
			
			if(flag==null){
				msg.what = -123;
			}else{
				msg.obj = flag;
			}
			mainHandler.sendMessage(msg);
			
		}
	};
	
	private String goSearchOrder(int customerid) {
		// 查询参数

		String registerString = "customerid=" + customerid;
		// URL
		String url = HttpUtil.BASE_URL + "MyOrderServlet?" + registerString;
		// 查询返回结果
		return HttpUtil.queryStringForPost(url);
	}

	private ArrayList<HashMap<String, Object>> jsonToOrder(int customerid) {
		String jsonString = goSearchOrder(customerid);
		if(jsonString.equals("-1")){
			return null;
		}

		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		try {
			JSONObject json = new JSONObject(jsonString);
			JSONArray jsonArray = json.getJSONArray("order");

			for (int i = 0; i < jsonArray.length(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				JSONObject rest = (JSONObject) jsonArray.get(i);
				int orderid = rest.getInt("orderid");
				String restname = rest.getString("restname");
				String maketime = rest.getString("maketime");
				boolean delivery = rest.getBoolean("delivery");
				boolean completeflag = rest.getBoolean("completeflag");
				String flagString = "";
				if(completeflag){
					flagString = "已完成";
				}else{
					flagString = " ";
				}
				double total = rest.getDouble("total");
				map.put("restname", restname);
				map.put("maketime", maketime);
				map.put("delivery", delivery);
				map.put("completeflag", completeflag);
				map.put("flagString", flagString);
				map.put("orderid", orderid);
				map.put("total", total);
				map.put("index", i);
				
				
				listItem.add(map);
			}
			return listItem;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(jsonString, "????");
			return null;
		}

	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_order, menu);
		return true;
	}

}
