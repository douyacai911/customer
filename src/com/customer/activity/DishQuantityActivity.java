package com.customer.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.support.v4.app.NavUtils;

public class DishQuantityActivity extends Activity {
	private int foodid = 0;
	private int quantity = 0;
	private int restid = 0;
	private String dishname;
	private double price, subtotal;
	private TextView tdishname, tprice, tsubtotal;
	private Spinner spinner;
	private int MaxQuantity = 30;
	private String[] array = new String[MaxQuantity];
	private ArrayAdapter<String> adapter;
	private TheApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dish_quantity);

		app = (TheApplication) getApplication();

		setTitle("��ȷ�Ϸ���");
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		foodid = bundle.getInt("foodid");
		dishname = bundle.getString("dishname");
		price = bundle.getDouble("price");
		restid = app.getRestid();

		tdishname = (TextView) findViewById(R.id.textView1);
		tprice = (TextView) findViewById(R.id.textView5);
		tsubtotal = (TextView) findViewById(R.id.textView6);

		tdishname.setText(dishname);
		tprice.setText(String.valueOf(price));
		tsubtotal.setText(String.valueOf(price));
		for (int i = 0; i < array.length; i++) {
			array[i] = Integer.toString(i + 1);
		}

		spinner = (Spinner) findViewById(R.id.spinner1);
		// ����ѡ������ArrayAdapter��������
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, array);

		// ���������б�ķ��
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// ��adapter ��ӵ�spinner��
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				quantity = position + 1;
				subtotal = price * quantity;
				tsubtotal.setText(String.valueOf(subtotal));
			}

			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						JSONArray jsonarray = app.getOrderJsonArray();
						int pastid = 0;
						if (!jsonarray.isNull(0)) {
							try {
								pastid = jsonarray.getJSONObject(0).getInt(
										"restid");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (pastid != restid) {
								Toast.makeText(DishQuantityActivity.this,
										"�ڱ�Ҳ�������ǰ�����Ƚ��붩��ҳ������ն���",
										Toast.LENGTH_SHORT).show();
								

							}
							else {
								Toast.makeText(DishQuantityActivity.this,
										String.valueOf(subtotal),
										Toast.LENGTH_SHORT).show();

								JSONObject json = new JSONObject();
								try {
									json.put("foodid", foodid);
									json.put("dishname", dishname);
									json.put("quantity", quantity);
									json.put("subtotal", subtotal);
									json.put("restid", restid);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								app.addToOrderJsonArray(json);
								Intent intent = new Intent(
										DishQuantityActivity.this,
										MenuActivity.class);
								startActivity(intent);

								DishDetailActivity.instance.finish();
								finish();
							}
						} 
						else {
							Toast.makeText(DishQuantityActivity.this,
									String.valueOf(subtotal),
									Toast.LENGTH_SHORT).show();

							JSONObject json = new JSONObject();
							try {
								json.put("foodid", foodid);
								json.put("dishname", dishname);
								json.put("quantity", quantity);
								json.put("subtotal", subtotal);
								json.put("restid", restid);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							app.addToOrderJsonArray(json);
							Intent intent = new Intent(
									DishQuantityActivity.this,
									MenuActivity.class);
							startActivity(intent);

							DishDetailActivity.instance.finish();
							finish();
						}
						// Intent intent = new
						// Intent(DishQuantityActivity.this,DishQuantityActivity.class);
						// startActivity(intent);
						// Bundle bundle = new Bundle();
						// bundle.putInt("foodid", foodid);
						// bundle.putString("dishname", dishname);
						// bundle.putInt("customerid", customerid);
						// startActivity(intent);
						// ��ת��ע��ҳ��

					}
				});
		findViewById(R.id.button2).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						finish();
						// Bundle bundle = new Bundle();
						// bundle.putInt("foodid", foodid);
						// bundle.putString("dishname", dishname);
						// bundle.putInt("customerid", customerid);
						// startActivity(intent);
						// ��ת��ע��ҳ��

					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dish_quantity, menu);
		return true;
	}

	// @Override
	// public void onBackPressed() {
	// Intent intent = new
	// Intent(DishQuantityActivity.this,DishDetailActivity.class);
	// startActivity(intent);
	// bundle
	// super.onBackPressed();
	// }

}
