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
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.customer.util.HttpUtil;

public class MyOrderDetailActivity extends Activity {
	private int customerid = 0;
	private int orderid = 0;
	private TextView textview,hit,lastinfo,lastinfo2,ttotal,teattime,tmaketime,tremark,tremark2,ttel;
	private Handler mainHandler,mainHandler2;
	private SimpleAdapter listItemAdapter;
	private ListView list;
	private TheApplication app;
	private boolean completeflag = false;
	private Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order_detail);
		app = (TheApplication) getApplication(); 
		customerid = app.getCustomerid();
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		orderid = bundle.getInt("orderid");
		completeflag = bundle.getBoolean("completeflag");
		hit = (TextView) findViewById(R.id.textView1);
		
		list = (ListView) findViewById(R.id.listView1);
		lastinfo = (TextView) findViewById(R.id.textView5);
		lastinfo2 = (TextView) findViewById(R.id.textView12); //就餐人数或送餐地址
		ttotal = (TextView) findViewById(R.id.textView8);
		teattime = (TextView) findViewById(R.id.textView11);
		tmaketime = (TextView) findViewById(R.id.textView10);
		tremark = (TextView) findViewById(R.id.textView7);
		tremark2 = (TextView) findViewById(R.id.textView14);
		ttel = (TextView)findViewById(R.id.textView13);
		setTitle(" ");
		
		new Thread(progressThread).start();
		
		if(completeflag){

			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					HashMap<String, Object> thisfood = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
					int thisfoodid = (Integer) thisfood.get("foodid");
					String dishname = (String) thisfood.get("dishname");
					Intent intent = new Intent().setClass(MyOrderDetailActivity.this,AddReviewActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("foodid", thisfoodid);
					bundle.putString("dishname", dishname);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
		}else{
			hit.setText("该订单尚未被店家标记完成，待您就餐后，店家将标记完成，届时您可以为此订单添加评价");
		}
		
		this.mainHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// findViewById(R.id.progressBar1).setVisibility(View.GONE);
				if(msg.what==-123){
					Toast.makeText(MyOrderDetailActivity.this, "对不起，请稍后再试",Toast.LENGTH_SHORT).show();
				}else{
				ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
				JSONObject json = new JSONObject();
				json = (JSONObject) msg.obj;
				try {
					String address,numofpeo;
					boolean delivery = json.getBoolean("delivery");
					String eattime = json.getString("eattime");
					String maketime = json.getString("maketime");
					String total = json.getString("total");
					String restname = json.getString("restname");
					String resttel = json.getString("resttel");
					setTitle("发往："+restname+" 的订单");
					findViewById(R.id.textView12).setVisibility(0);
					ttel.setText(resttel);
					ttel.setVisibility(0);
					if(json.has("remark")){
						tremark2.setText(json.getString("remark"));
						tremark.setVisibility(0);
						tremark2.setVisibility(0);
					}
					ttotal.setText(total);
					teattime.setText(eattime);
					tmaketime.setText(maketime);
					if(delivery){
						lastinfo.setText("送餐地址：");
						address = json.getString("address");
						lastinfo2.setText(address);
						lastinfo.setVisibility(0);
						lastinfo2.setVisibility(0);
						
					}else{
						lastinfo.setText("就餐人数：");
						numofpeo = json.getString("numofpeo");
						lastinfo2.setText(numofpeo);
						lastinfo.setVisibility(0);
						lastinfo2.setVisibility(0);
					}
					JSONArray jsonArray = json.getJSONArray("orderdetailarray");
					for (int i = 0; i < jsonArray.length(); i++) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						JSONObject orderdetail = (JSONObject) jsonArray.get(i);
						int foodid = orderdetail.getInt("foodid");
						String dishname = orderdetail.getString("dishname");
						String quantity = orderdetail.getString("quantity");
						String subtotal = orderdetail.getString("subtotal");
						
						map.put("foodid", foodid);
						map.put("dishname", dishname);
						map.put("quantity", quantity);
						map.put("subtotal", subtotal);
						map.put("index", i);
						listItem.add(map);
				}} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				listItemAdapter = new SimpleAdapter(MyOrderDetailActivity.this,listItem,// 数据源
						R.layout.order_list_layout,// ListItem的XML实现
						// 动态数组与ImageItem对应的子项
						new String[] { "dishname", "quantity","subtotal" },
						// ImageItem的XML文件里面的一个ImageView,两个TextView ID
						new int[] { R.id.textView1, R.id.textView4, R.id.textView3}

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
			JSONObject flag = getOrderDetailJson(orderid);
			if(flag==null){
				msg.what = -123;
			}else{
				msg.obj = flag;
			}
			mainHandler.sendMessage(msg);
		}
	};
	
	private String goSearch(int id) {
		// 查询参数

		String registerString = "orderid=" + id;
		// URL
		String url = HttpUtil.BASE_URL + "MyOrderDetailServlet?" + registerString;
		// 查询返回结果
		return HttpUtil.queryStringForPost(url);
	}
	private JSONObject getOrderDetailJson(int id) {
		String jsonString = goSearch(id);
		if(jsonString.equals("-1")){
			return null;
		}
		try {
			JSONObject json = new JSONObject(jsonString);
			return json;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_order_detail, menu);
		return true;
	}

}
