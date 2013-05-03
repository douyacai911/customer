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
			
			order = order + dishname + quantity+"��"+" ";
			total = total + subtotal;
		}
		if(TextUtils.isEmpty(remark)){
			result = "����"+username+"�Ķ������Ͳ�ʱ�䣺"+eattime+" �Ͳ�����"+numofpeo+" ������£�"+order+"��"+total+"Ԫ"+" ����ͨ��txd�ͻ��˲鿴������ϸ��Ϣ";
		}
		else{
			result = "����"+username+"�Ķ������Ͳ�ʱ�䣺"+eattime+" �Ͳ�����"+numofpeo+" ������£�"+order+"��"+total+"Ԫ"+" ��ע��"+remark+" ����ͨ��txd�ͻ��˲鿴������ϸ��Ϣ";
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
			
			order = order + dishname + quantity+"��"+" ";
			total = total + subtotal;
		}
		if(TextUtils.isEmpty(remark)){
			result = "����"+username+"�Ķ������Ͳ�ʱ�䣺"+eattime+" ������£�"+order+"��"+total+"Ԫ"+" ����ͨ��txd�ͻ��˲鿴������ϸ��Ϣ";
		}
		else{
			result = "����"+username+"�Ķ������Ͳ�ʱ�䣺"+eattime+" ������£�"+order+"��"+total+"Ԫ"+" ��ע��"+remark+" ����ͨ��txd�ͻ��˲鿴������ϸ��Ϣ";
		}
		return result;
		
	}
}
