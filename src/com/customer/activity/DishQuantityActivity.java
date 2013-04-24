package com.customer.activity;

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
	private int customerid = 0;
	private String dishname;
	private double price,subtotal;
	private TextView tdishname,tprice,tsubtotal;
	private Spinner spinner;  
	private int MaxQuantity = 30; 
    private String[] array = new String[MaxQuantity];
    private ArrayAdapter<String> adapter; 
    private Bundle theBundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dish_quantity);

		setTitle("��ȷ�Ϸ���");
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle!=null){
			theBundle = bundle;
		}
		foodid = bundle.getInt("foodid");
		dishname = bundle.getString("dishname");
		price = bundle.getDouble("price");
		customerid = bundle.getInt("customerid");
		
		tdishname = (TextView) findViewById(R.id.textView1);
		tprice = (TextView) findViewById(R.id.textView5);
		tsubtotal = (TextView) findViewById(R.id.textView6);
		
		tdishname.setText(dishname);
		tprice.setText(String.valueOf(price));
		tsubtotal.setText(String.valueOf(price));
		for(int i=0;i<array.length;i++){
			array[i]=Integer.toString(i+1);
		}
		
		spinner = (Spinner) findViewById(R.id.spinner1);  
        //����ѡ������ArrayAdapter��������  
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,array);  
          
        //���������б�ķ��  
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
          
        //��adapter ��ӵ�spinner��  
        spinner.setAdapter(adapter);  
          
        spinner.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        	quantity = position+1;
                        	subtotal = price*quantity;
                        	tsubtotal.setText(String.valueOf(subtotal));
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        
                    }
                }); 
        
        findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Toast.makeText(DishQuantityActivity.this,String.valueOf(subtotal), Toast.LENGTH_SHORT).show();
//						Intent intent = new Intent(DishQuantityActivity.this,DishQuantityActivity.class);
//						startActivity(intent);
//						Bundle bundle = new Bundle();
//						bundle.putInt("foodid", foodid);
//						bundle.putString("dishname", dishname);
//						bundle.putInt("customerid", customerid);
//		    			startActivity(intent);
						//��ת��ע��ҳ��
						
					}
				});
        findViewById(R.id.button2).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						
						Intent intent = new Intent(DishQuantityActivity.this,DishDetailActivity.class);
						startActivity(intent);
						finish();
//						Bundle bundle = new Bundle();
//						bundle.putInt("foodid", foodid);
//						bundle.putString("dishname", dishname);
//						bundle.putInt("customerid", customerid);
//		    			startActivity(intent);
						//��ת��ע��ҳ��
						
					}
				});
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dish_quantity, menu);
		return true;
	}

//	@Override
//	public void onBackPressed() {
//		Intent intent = new Intent(DishQuantityActivity.this,DishDetailActivity.class);
//		startActivity(intent);
//		bundle
//			super.onBackPressed();
//	}

}
