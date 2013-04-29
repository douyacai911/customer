package com.customer.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.customer.util.HttpUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.NavUtils;

public class MenuActivity extends Activity {
	private TextView textview; // TODO test
	private Handler mainHandler;
	private SimpleAdapter listItemAdapter;
	private ListView list;
	private int restid = 0;
	private int customerid = 0;
	private boolean delivery = false;
	private String restname;
	private TheApplication app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		app = (TheApplication) getApplication();
		JSONArray jsonarray = app.getOrderJsonArray();
		if(jsonarray.length()==0){
			findViewById(R.id.Button1).setVisibility(View.GONE);
		}
		list = (ListView) findViewById(R.id.ListView01);
		textview = (TextView) findViewById(R.id.test1);
		restid = app.getRestid();
		restname = app.getRestname();
		delivery = app.getDelivery();
		customerid = app.getCustomerid();

		setTitle(restname);
		if (delivery) {
			textview.setText("此店提供外送服务");
		} else {
			textview.setText("此店不提供外送服务");
		}

		new Thread(progressThread).start();
		

	         
		

		// 添加点击
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				HashMap<String, Object> thisfood = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
				int thisfoodid = (Integer) thisfood.get("foodid");
				String dishname = (String) thisfood.get("dishname");
				Intent intent = new Intent().setClass(MenuActivity.this,DishDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("foodid", thisfoodid);
				bundle.putString("dishname", dishname);
				bundle.putInt("customerid", customerid);
				bundle.putInt("restid", restid);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	
		this.mainHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// findViewById(R.id.progressBar1).setVisibility(View.GONE);
				if (msg.what == -123) {
					Toast.makeText(MenuActivity.this, "现无菜品",
							Toast.LENGTH_SHORT).show();
				} else {
					listItemAdapter = new SimpleAdapter(MenuActivity.this,
							(ArrayList<HashMap<String, Object>>) msg.obj,// 数据源
							R.layout.menu_list_layout,// ListItem的XML实现
							// 动态数组与ImageItem对应的子项
							new String[] { "dishname", "price" },
							// ImageItem的XML文件里面的一个ImageView,两个TextView ID
							new int[] { R.id.DishName, R.id.DishPrice }

					);

					// 添加并且显示
					list.setAdapter(listItemAdapter);
					
				}

				super.handleMessage(msg);
			}

		};
		
		findViewById(R.id.Button1).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(MenuActivity.this,OrderListActivity.class);
						startActivity(intent);
						finish();
						//跳转到订单页面
						
					}
				});
	}

	Runnable progressThread = new Runnable() {
		@Override
		public void run() {
			Message msg = new Message();
			new Thread();
			Object flag = jsonToMenu(restid);
			if (flag == null) {
				msg.what = -123;
			} else {
				msg.obj = flag;
			}
			mainHandler.sendMessage(msg);
		}
	};

	private String goRegister(int id) {
		// 查询参数

		String registerString = "restid=" + id;
		// URL
		String url = HttpUtil.BASE_URL + "MenuServlet?" + registerString;
		// 查询返回结果
		return HttpUtil.queryStringForPost(url);
	}

	private ArrayList<HashMap<String, Object>> jsonToMenu(int id) {
		String jsonString = goRegister(id);
		if (jsonString.equals("-1")) {
			return null;
		}
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		try {
			JSONObject json = new JSONObject(jsonString);
			JSONArray jsonArray = json.getJSONArray("menu");

			for (int i = 0; i < jsonArray.length(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				// map.put("ItemTitle", "Level "+i);
				// map.put("ItemText", "Finished in 1 Min 54 Secs, 70 Moves! ");
				// listItem.add(map);
				JSONObject dish = (JSONObject) jsonArray.get(i);
				int foodid = dish.getInt("foodid");
				String dishname = dish.getString("dishname");
				double price = dish.getDouble("price");
				int categoryid = dish.getInt("categoryid");
				map.put("foodid", foodid);
				map.put("dishname", dishname);
				map.put("price", price);
				map.put("categoryid", categoryid);
				map.put("index", i);
				listItem.add(map);
			}
			return listItem;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}
