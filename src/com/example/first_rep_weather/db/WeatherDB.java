package com.example.first_rep_weather.db;

import java.util.ArrayList;
import java.util.List;

import com.example.first_rep_weather.model.City;
import com.example.first_rep_weather.model.County;
import com.example.first_rep_weather.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WeatherDB {
	/**
	 * database name
	 */
	public static final String DB_NAME = "weather";
	
	/**
	 * database version
	 */
	public static final int VERSION = 1;
	
	private static WeatherDB weatherDB;
	
	private SQLiteDatabase db;
	
	private WeatherDB(Context context){
		WeatherOpenHelper dbHelper = new WeatherOpenHelper(context,
				DB_NAME, null, VERSION, null);
		db = dbHelper.getWritableDatabase();
	}
	
	/**
	 * get WeatherDB instance
	 * @param context 
	 * @return WeatherDB
	 */
	public synchronized static WeatherDB getInstance(Context context){
		if(weatherDB != null){
			weatherDB = new WeatherDB(context);
		}
		return weatherDB;
	}
	
	/**
	 * save province to the weather database province table
	 * @param province
	 */
	public void saveProvince(Province province){
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getName());
			values.put("province_code", province.getCode());
			db.insert("Province", null, values);
		}
	}
	
	/**
	 * save city to the weather database city table
	 * @param city
	 */
	public void saveCity(City city){
		if(city != null){
			ContentValues values = new ContentValues();
			values.put("city_name", city.getName());
			values.put("city_code", city.getCode());
			values.put("id", city.getId());
			values.put("province_id", city.getProvince_id());
			db.insert("city", null, values);
		}
	}
	
	/**
	 * save county to the weather database county table
	 * @param county
	 */
	public void saveCounty(County county){
		if(county != null){
			ContentValues values = new ContentValues();
			values.put("id", county.getId());
			values.put("county_name", county.getName());
			values.put("county_code", county.getCode());
			values.put("city_id", county.getCode());
			db.insert("county", null, values);
		}
	}
	
	/**
	 * query the weather database,province table get the province list;
	 * @return province list
	 */
	public List<Province> loadProvince() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			} while(cursor.moveToNext());
		}
		return list;
	}
	
	/**
	 * query the weather database,city table get the city list;
	 * @return city list
	 */
	public List<City> loadCity(){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("city", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setProvince_id(cursor.getInt(cursor.getColumnIndex("Province_id")));
			} while(cursor.moveToNext());
		}
		return list;
	}
	
	/**
	 * query the weather database,county table get the county list;
	 * @return county list
	 */
	public List<County> loadCounty(){
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("county", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCity_id(cursor.getInt(cursor.getColumnIndex("city_id")));
				list.add(county);
			} while(cursor.moveToNext());
		}
		return list;
	}
	
}
