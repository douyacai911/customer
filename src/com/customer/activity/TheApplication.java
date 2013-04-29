package com.customer.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Application;

public class TheApplication extends Application{
	private int customerid,restid;
	private boolean delivery;
	private String username,restname,restaddress,restlocation,resttel,cusaddress,cuslocation;
	private JSONArray orderJsonArray;
	private double total;
	
	
	@Override
    public void onCreate() {
            super.onCreate();
            setCustomerid(CUSTOMERID); //初始化全局变量
            setUsername(USERNAME);
            setRestid(RESTID);
            setDelivery(DELIVERY);
            setRestname(RESTNAME);
            setRestAddress(RESTADDRESS);
            setRestLocation(RESTLOCATION);
            setRestTel(RESTTEL);
            setCusaddress(CUSADDRESS);
            setCuslocation(CUSLOCATION);
            setTotal(TOTAL);
            setJsonArrayNull();
    }
	
	public int getCustomerid(){
		return customerid;
	}
	public void setCustomerid(int customerid){
		this.customerid = customerid;
	}
	public String getUsername(){
		return username;
	}
	public void setUsername(String username){
		this.username = username;
	}
	public String getCusaddress(){
		return cusaddress;
	}
	public void setCusaddress(String cusaddress){
		this.cusaddress = cusaddress;
	}
	public String getCuslocation(){
		return cuslocation;
	}
	public void setCuslocation(String cuslocation){
		this.cuslocation = cuslocation;
	}
	public int getRestid(){
		return restid;
	}
	public void setRestid(int restid){
		this.restid = restid;
	}
	public boolean getDelivery(){
		return delivery;
	}
	public void setDelivery(boolean delivery){
		this.delivery = delivery;
	}
	public String getRestname(){
		return restname;
	}
	public void setRestname(String restname){
		this.restname = restname;
	}
	public String getRestAddress(){
		return restaddress;
	}
	public void setRestAddress(String restaddress){
		this.restaddress = restaddress;
	}
	public String getRestLocation(){
		return restlocation;
	}
	public void setRestLocation(String restlocation){
		this.restlocation = restlocation;
	}
	public String getRestTel(){
		return resttel;
	}
	public void setRestTel(String resttel){
		this.resttel = resttel;
	}
	public double getTotal(){
		return total;
	}
	public void setTotal(double total){
		this.total = total;
	}
	
	public JSONArray getOrderJsonArray(){
		return orderJsonArray;
	}
	public void addToOrderJsonArray(JSONObject json){
		this.orderJsonArray.put(json);
	}
	public void setOrderJsonArray(JSONArray orderJsonArray){
		this.orderJsonArray = orderJsonArray;
	}
	public void setJsonArrayNull(){
		orderJsonArray = new JSONArray();
	}
	private static final int CUSTOMERID = 0;
	private static final int RESTID = 0;
	private static final JSONArray ORDERJSONARRAY = null;
	private static final boolean DELIVERY = false;
	private static final String RESTNAME = "";
	private static final String RESTADDRESS = "";
	private static final String RESTLOCATION = "";
	private static final String RESTTEL = "";
	private static final String USERNAME = "";
	private static final String CUSADDRESS = "";
	private static final String CUSLOCATION = "";
	private static final double TOTAL = 0.0;
}
