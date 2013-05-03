package com.customer.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class OrderListActivity extends Activity {
	private SimpleAdapter listItemAdapter;
	private ListView list;
	private TextView ttotal;
	private boolean delivery = false;
	private RadioGroup radiogroup;
	private RadioButton radio1, radio2;
	private TheApplication app;
	private JSONArray jsonarray;

	private double total = 0.0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_list);
		app = (TheApplication) getApplication();
		
		jsonarray = new JSONArray();
		list = (ListView) findViewById(R.id.listView1);
		ttotal = (TextView) findViewById(R.id.textView1);
		radiogroup = (RadioGroup) findViewById(R.id.radiogroup1);
		radio1 = (RadioButton) findViewById(R.id.radioButton1);
		radio2 = (RadioButton) findViewById(R.id.radioButton2);
		
		if (app.getDelivery()) {

			radiogroup.setVisibility(0);
			// �����Ƿ�����ѡ��
			radiogroup
					.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							if (checkedId == radio2.getId()) {
								delivery = true;
							} else {
								delivery = false;
							}
						}
					});
		}
		jsonarray = app.getOrderJsonArray();
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < jsonarray.length(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			try {
				JSONObject dish = (JSONObject) jsonarray.get(i);
				int foodid = dish.getInt("foodid");
				String dishname = dish.getString("dishname");
				double subtotal = dish.getDouble("subtotal");
				int quantity = dish.getInt("quantity");
				int restid = dish.getInt("restid");
				map.put("foodid", foodid);
				map.put("dishname", dishname);
				map.put("quantity", quantity);
				map.put("subtotal", subtotal);
				map.put("restid", restid);
				map.put("index", i);
				listItem.add(map);
				total = total + subtotal;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			app.setTotal(total);
		}
		
		listItemAdapter = new SimpleAdapter(OrderListActivity.this,
				(ArrayList<HashMap<String, Object>>) listItem,// ����Դ
				R.layout.order_list_layout,// ListItem��XMLʵ��
				// ��̬������ImageItem��Ӧ������
				new String[] { "dishname", "quantity","subtotal"},
				// ImageItem��XML�ļ������һ��ImageView,����TextView ID
				new int[] { R.id.textView1, R.id.textView4,R.id.textView3 }

		);
		

		// ��Ӳ�����ʾ
		list.setAdapter(listItemAdapter);
		
		ttotal.setText(String.valueOf(total));
		
		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent;
						if(!delivery){
							intent = new Intent(OrderListActivity.this,GoToRestActivity.class);
						}
						else
						{
							 intent = new Intent(OrderListActivity.this,OrderTakeOutActivity.class);
						}
						startActivity(intent);
						finish();
						
						
						
					}
				});
		
		findViewById(R.id.button2).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						app.setJsonArrayNull();
						Intent intent = new Intent(OrderListActivity.this,MenuActivity.class);
						startActivity(intent);
						finish();
						
						//��ն���������menu
						
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order_list, menu);
		return true;
	}

}
