package com.customer.util;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.customer.activity.TheApplication;

public class CreateSMS {

	private TheApplication app;
	public String createSMS(String username,String eattime,String numofpeo,String remark,JSONArray orderjsonarray) throws JSONException{
		String order = "";
		String result ="";
		double total = 0.0;
		for (int i = 0; i < orderjsonarray.length(); i++) {
			JSONObject dish = (JSONObject) orderjsonarray.get(i);
			String dishname = dish.getString("dishname");
			double subtotal = dish.getDouble("subtotal");
			int quantity = dish.getInt("quantity");
			int restid = dish.getInt("restid");
			
			order = order + dishname + quantity+"份"+" ";
			total = total + subtotal;
		}
		if(TextUtils.isEmpty(remark)){
			result = "来自"+username+"的订单：就餐时间："+eattime+" 就餐人数"+numofpeo+" 点菜如下："+order+"共"+total+"元"+" 您可通过txd客户端查看订单详细信息";
		}
		else{
			result = "来自"+username+"的订单：就餐时间："+eattime+" 就餐人数"+numofpeo+" 点菜如下："+order+"共"+total+"元"+" 备注："+remark+" 您可通过txd客户端查看订单详细信息";
		}
		return result;
		
	}
	public String createSMS(String username,String eattime,String remark,JSONArray orderjsonarray) throws JSONException{
		String order = "";
		String result ="";
		double total = 0.0;
		for (int i = 0; i < orderjsonarray.length(); i++) {
			JSONObject dish = (JSONObject) orderjsonarray.get(i);
			String dishname = dish.getString("dishname");
			double subtotal = dish.getDouble("subtotal");
			int quantity = dish.getInt("quantity");
			int restid = dish.getInt("restid");
			
			order = order + dishname + quantity+"份"+" ";
			total = total + subtotal;
		}
		if(TextUtils.isEmpty(remark)){
			result = "来自"+username+"的订单：就餐时间："+eattime+" 点菜如下："+order+"共"+total+"元"+" 您可通过txd客户端查看订单详细信息";
		}
		else{
			result = "来自"+username+"的订单：就餐时间："+eattime+" 点菜如下："+order+"共"+total+"元"+" 备注："+remark+" 您可通过txd客户端查看订单详细信息";
		}
		return result;
		
	}
}
