package com.example.first_rep_weather.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherOpenHelper extends SQLiteOpenHelper{
	
	/**
	 * province table create sql
	 */
	public static final String CREATE_PROVINCE = "create table Province("
			+"id integer primary key autoincrement,"
			+"province_name text,"
			+"province_code text)";
	
	/**
	 * city table create sql
	 */
	public static final String CREATE_CITY = "create table City("
			+"id integer primary key autoincrement,"
			+"city_name text,"
			+"city_code text,"
			+"province_id integer)";
	
	/**
	 * county table create sql
	 */
	public static final String CREATE_CONUTY = "create table County("
			+"id integer primary key autoincrement,"
			+"county_name text,"
			+"county_code text,"
			+ "city_id integer)";

	public WeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_CONUTY);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
