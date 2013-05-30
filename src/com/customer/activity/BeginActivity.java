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
		setTitle("餐厅一览");
		instance = this; //指定关闭用
		
		list = (ListView) findViewById(R.id.ListView01);
		customerid = app.getCustomerid();
		
		LocationManager loctionManager;
		String contextService=Context.LOCATION_SERVICE;
		//通过系统服务，取得LocationManager对象
		loctionManager=(LocationManager) getSystemService(contextService);
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
		criteria.setAltitudeRequired(false);//不要求海拔
		criteria.setBearingRequired(false);//不要求方位
		criteria.setCostAllowed(true);//允许有花费
		criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗
		//从可用的位置提供器中，匹配以上标准的最佳提供器
		String provider = loctionManager.getBestProvider(criteria, true);
		//获得最后一次变化的位置
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

	         
		// 添加点击
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
					Toast.makeText(BeginActivity.this, "尚未有餐厅注册",Toast.LENGTH_SHORT).show();
				}else{
				listItemAdapter = new SimpleAdapter(BeginActivity.this,
						(ArrayList<HashMap<String, Object>>) msg.obj,// 数据源
						R.layout.rest_list_layout,// ListItem的XML实现
						// 动态数组与ImageItem对应的子项
						new String[] { "restname", "distance" },//TODO
						// ImageItem的XML文件里面的一个ImageView,两个TextView ID
						new int[] { R.id.RestName, R.id.Distance }

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
		// 查询参数

		String registerString = "customerid=" + customerid + "&lon=" + lon + "&lat=" + lat;
		// URL
		String url = HttpUtil.BASE_URL + "RestListServlet?" + registerString;
		// 查询返回结果
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
				map.put("distance", "相距 "+String.valueOf(distance)+" 米");
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
        switch(item.getItemId())//得到被点击的item的itemId
        {
        case R.id.action_settings://这里的Id就是布局文件中定义的Id，在用R.id.XXX的方法获取出来
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
			builder.setTitle("确认退出")
					.setMessage("确定要退出程序吗？")
					.setPositiveButton(
							"确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface,
										int i) {
									// 按钮事件
									  Intent exit = new Intent(Intent.ACTION_MAIN);
					                    exit.addCategory(Intent.CATEGORY_HOME);
					                    exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					                    startActivity(exit);
					                    System.exit(0);

								}
							})
					.setNegativeButton(
							"取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show(); //弹出确定退出对话框
          
            //不需要执行父类的点击事件，直接return
            return true;
        }
        //继续执行父类的其他点击事件
        return super.onKeyDown(keyCode, event);
    }
}
