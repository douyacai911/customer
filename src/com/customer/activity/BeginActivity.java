package com.customer.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.customer.activity.R;
import com.customer.util.HttpUtil;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BeginActivity extends Activity {
	private int customerid = 0;
	private TextView textview;   //TODO test
	private Handler mainHandler;
	private SimpleAdapter listItemAdapter;
	private ListView list;
	private double mylon = 0;
	private double mylat = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_begin);
		list = (ListView) findViewById(R.id.ListView01);
		textview = (TextView) findViewById(R.id.test1);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		customerid = bundle.getInt("customerid");
		String id = String.valueOf(customerid);
		textview.setText(id);
		
		LocationManager loctionManager;
		String contextService=Context.LOCATION_SERVICE;
		//ͨ��ϵͳ����ȡ��LocationManager����
		loctionManager=(LocationManager) getSystemService(contextService);
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);//�߾���
		criteria.setAltitudeRequired(false);//��Ҫ�󺣰�
		criteria.setBearingRequired(false);//��Ҫ��λ
		criteria.setCostAllowed(true);//�����л���
		criteria.setPowerRequirement(Criteria.POWER_LOW);//�͹���
		//�ӿ��õ�λ���ṩ���У�ƥ�����ϱ�׼������ṩ��
		String provider = loctionManager.getBestProvider(criteria, true);
		//������һ�α仯��λ��
		Location location = loctionManager.getLastKnownLocation(provider);
		mylon = location.getLongitude();
		mylat = location.getLatitude();
		
		
		
		
		new Thread(progressThread).start();
		// ��ӵ��
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				HashMap<String, Object> thisrest = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
				int thisrestid = (Integer) thisrest.get("restid");
				boolean isdelivery = (Boolean) thisrest.get("delivery");
				String restname = (String) thisrest.get("restname");
				Intent intent = new Intent().setClass(BeginActivity.this, MenuActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("restid", thisrestid);
				bundle.putBoolean("delivery",isdelivery);
				bundle.putString("restname", restname);
				bundle.putInt("customerid", customerid);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		this.mainHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// findViewById(R.id.progressBar1).setVisibility(View.GONE);
				if(msg.what==-123){
					Toast.makeText(BeginActivity.this, "��δ�в���ע��",Toast.LENGTH_SHORT).show();
				}else{
				listItemAdapter = new SimpleAdapter(BeginActivity.this,
						(ArrayList<HashMap<String, Object>>) msg.obj,// ����Դ
						R.layout.rest_list_layout,// ListItem��XMLʵ��
						// ��̬������ImageItem��Ӧ������
						new String[] { "restname", "distance" },//TODO
						// ImageItem��XML�ļ������һ��ImageView,����TextView ID
						new int[] { R.id.RestName, R.id.Distance }

				);

				// ��Ӳ�����ʾ
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
			Object flag = (Object)jsonToRest(mylon,mylat,customerid);
			
			if(flag==null){
				msg.what = -123;
			}else{
				msg.obj = flag;
			}
			mainHandler.sendMessage(msg);
		}
	};

	private String goSearchRest(double lon, double lat, int customerid) {
		// ��ѯ����

		String registerString = "customerid=" + customerid + "&lon=" + lon + "&lat=" + lat;
		// URL
		String url = HttpUtil.BASE_URL + "RestListServlet?" + registerString;
		// ��ѯ���ؽ��
		return HttpUtil.queryStringForPost(url);
	}

	private ArrayList<HashMap<String, Object>> jsonToRest(double lon,double lat,int customerid) {
		String jsonString = goSearchRest(lon,lat,customerid);
		if(jsonString.equals("-1")){
			return null;
		}

		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		try {
			JSONObject json = new JSONObject(jsonString);
			JSONArray jsonArray = json.getJSONArray("restlist");

			for (int i = 0; i < jsonArray.length(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				JSONObject rest = (JSONObject) jsonArray.get(i);
				double distance = rest.getDouble("distance");
				String restname = rest.getString("restname");
				int restid = rest.getInt("restid");
				boolean delivery = rest.getBoolean("delivery");
				map.put("distance", "��� "+String.valueOf(distance)+" ��");
				map.put("restname", restname);
				map.put("delivery", delivery);
				map.put("restid", restid);
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
		getMenuInflater().inflate(R.menu.begin, menu);
		return true;
	}

}
