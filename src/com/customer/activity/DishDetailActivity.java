package com.customer.activity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.customer.util.HttpUtil;
import com.customer.util.RatingbarAdapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.text.method.DateTimeKeyListener;

public class DishDetailActivity extends Activity {
	private Handler mainHandler;
	private SimpleAdapter listItemAdapter;
	private ListView list;
	private int customerid = 0;
	private int foodid = 0;
	private int theCategory = 0;
	private TextView dishname,category,price,description;
	private String theDishname,thePrice,theDescription;
	private static final String[] m={"����","����","��ʳ","��ˮ","����"}; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dish_detail);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		foodid = bundle.getInt("foodid");
		customerid = bundle.getInt("customerid");
		setTitle(bundle.getString("dishname"));
		// Show the Up button in the action bar.
				setupActionBar();
		

		list = (ListView) findViewById(R.id.listView1);
		dishname = (TextView) findViewById(R.id.textView5);
		category = (TextView) findViewById(R.id.textView6);
		price = (TextView) findViewById(R.id.textView7);
		description = (TextView) findViewById(R.id.textView8);
		
		
		new Thread(progressThread).start();
		
		this.mainHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// findViewById(R.id.progressBar1).setVisibility(View.GONE);
				dishname.setText(theDishname);
				price.setText(thePrice);
				description.setText(theDescription);
				category.setText(m[theCategory]);
				if (msg.what == -321) {
					
					listItemAdapter = new RatingbarAdapter(DishDetailActivity.this,
							(ArrayList<HashMap<String, Object>>) msg.obj,// ����Դ
							R.layout.comment_list_layout,// ListItem��XMLʵ��
							// ��̬������ImageItem��Ӧ������
							new String[] { "customername", "time" ,"detail","stars"},
							// ImageItem��XML�ļ������һ��ImageView,����TextView ID
							new int[] { R.id.textView1, R.id.time, R.id.textView4,R.id.rating}

					);

					// ���Ӳ�����ʾ
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
			String result = getResult(foodid);
			Object flag = null;
			try {
				JSONObject json = new JSONObject(result);
				theDishname = json.getString("dishname");
				theCategory = json.getInt("category");
				thePrice = String.valueOf(json.getDouble("price"));
				theDescription = json.getString("description");
				msg.what = -123;
				if(json.getBoolean("havecomment")){
					msg.what = -321;
					ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
					JSONArray jsonArray = json.getJSONArray("comment");
					for (int i = 0; i < jsonArray.length(); i++) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						// map.put("ItemTitle", "Level "+i);
						// map.put("ItemText", "Finished in 1 Min 54 Secs, 70 Moves! ");
						// listItem.add(map);
						JSONObject comment = (JSONObject) jsonArray.get(i);
						String customername = comment.getString("customername");
						String time = comment.getString("time");
						String detail = comment.getString("detail");
						double stars = comment.getDouble("stars");
						float star = (float) stars;
						map.put("customername", customername);
						map.put("detail", detail);
						map.put("stars", star);
						map.put("time", time);
						map.put("index", i);
						listItem.add(map);
					}
					flag = listItem;
				}
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (msg.what==-321) {
				
				msg.obj = flag;
			}
			mainHandler.sendMessage(msg);
		}
	};
	
	private String getResult(int id) {
		// ��ѯ����

		String registerString = "foodid=" + id;
		// URL
		String url = HttpUtil.BASE_URL + "GetDishServlet?" + registerString;
		// ��ѯ���ؽ��
		String result = HttpUtil.queryStringForPost(url);
		
		return result;
	}
	
	
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dish_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

}