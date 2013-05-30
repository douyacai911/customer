package com.customer.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.customer.activity.R;
import com.customer.util.CreateSMS;
import com.customer.util.HttpUtil;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BeginActivity extends Activity {

	public static BeginActivity instance = null;
	private int customerid = 0;
	private Handler mainHandler;
	private SimpleAdapter listItemAdapter;
	private ListView list;
	private double mylon = 0;
	private double mylat = 0;
	private TheApplication app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_begin);
		app = (TheApplication) getApplication();
		setTitle("����һ��");
		instance = this; //ָ���ر���
		
		list = (ListView) findViewById(R.id.ListView01);
		customerid = app.getCustomerid();
		
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
		
		findViewById(R.id.button2).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent().setClass(BeginActivity.this, MyOrderActivity.class);
						startActivity(intent);
					}
				});

	         
		// ��ӵ��
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				HashMap<String, Object> thisrest = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
				int thisrestid = (Integer) thisrest.get("restid");
				boolean isdelivery = (Boolean) thisrest.get("delivery");
				String restname = (String) thisrest.get("restname");
				String restaddress = (String) thisrest.get("address");
				String restlocation = (String) thisrest.get("location");
				String resttel = (String) thisrest.get("tel");
				app.setRestid(thisrestid);
				app.setDelivery(isdelivery);
				app.setRestname(restname);
				app.setRestAddress(restaddress);
				app.setRestLocation(restlocation);
				app.setRestTel(resttel);
				Intent intent = new Intent().setClass(BeginActivity.this, MenuActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putInt("restid", thisrestid);
//				bundle.putBoolean("delivery",isdelivery);
//				bundle.putString("restname", restname);
//				bundle.putInt("customerid", customerid);
//				intent.putExtras(bundle);
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
				String address = rest.getString("address");
				String location = rest.getString("location");
				String tel = rest.getString("tel");
				map.put("distance", "��� "+String.valueOf(distance)+" ��");
				map.put("restname", restname);
				map.put("delivery", delivery);
				map.put("restid", restid);
				map.put("address", address);
				map.put("location", location);
				map.put("tel", tel);
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
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId())//�õ��������item��itemId
        {
        case R.id.action_settings://�����Id���ǲ����ļ��ж����Id������R.id.XXX�ķ�����ȡ����
        	Intent intent = new Intent().setClass(BeginActivity.this, EditInfoActivity.class);
			startActivity(intent);
            break;
       
        }
        return true;
    }
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        
        if(keyCode==KeyEvent.KEYCODE_BACK){
        	final AlertDialog.Builder builder = new Builder(
					BeginActivity.this);
			builder.setTitle("ȷ���˳�")
					.setMessage("ȷ��Ҫ�˳�������")
					.setPositiveButton(
							"ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface,
										int i) {
									// ��ť�¼�
									  Intent exit = new Intent(Intent.ACTION_MAIN);
					                    exit.addCategory(Intent.CATEGORY_HOME);
					                    exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					                    startActivity(exit);
					                    System.exit(0);

								}
							})
					.setNegativeButton(
							"ȡ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show(); //����ȷ���˳��Ի���
          
            //����Ҫִ�и���ĵ���¼���ֱ��return
            return true;
        }
        //����ִ�и������������¼�
        return super.onKeyDown(keyCode, event);
    }
}
