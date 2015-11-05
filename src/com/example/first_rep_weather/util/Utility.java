package com.example.first_rep_weather.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.first_rep_weather.db.WeatherDB;
import com.example.first_rep_weather.model.City;
import com.example.first_rep_weather.model.County;
import com.example.first_rep_weather.model.Province;

public class Utility {

	/**
	 * ����province����
	 * @param coolWeatherDB  ���ݿ������  ���ڱ�����������ݽ����ݿ�
	 * @param response   ��Ҫ����������
	 * @return
	 */
	public synchronized static boolean handleProvincesResponce(WeatherDB
			weatherDB , String response){
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length > 0){
				for(String p:allProvinces){
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setCode(array[0]);
					province.setName(array[1]);
					
					weatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ����City����
	 * @param weatherDB ���ݿ������ ���ڱ������������
	 * @param response Ҫ����������
	 * @param provinceId ��������ʡ���
	 * @return
	 */
	public synchronized static boolean handleCityResponce(WeatherDB
			weatherDB , String response ,int provinceId){
		if( !TextUtils.isEmpty(response)){
			String[] allCitys = response.split(",");
			if(allCitys != null && allCitys.length > 0){
				for(String c: allCitys){
					String[] array = c.split("\\|");
					if( array != null&& array.length==2){
						City city = new City();
						city.setCode(array[0]);
						city.setName(array[1]);
						city.setProvince_id(provinceId);
						weatherDB.saveCity(city);
					}
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * ����county����
	 * @param weatherDB ���ݿ������ ���ڱ������������
	 * @param response Ҫ����������
	 * @param provinceId �س������б��
	 * @return
	 */
	public synchronized static boolean handlerCountyResponce(WeatherDB
			weatherDB, String response, int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allcounties = response.split(",");
			if(allcounties!= null && allcounties.length>0){
				for(String c : allcounties){
					String[] array = c.split("\\|");
					if(array!= null&& array.length==2){
						County county = new County();
						county.setCode(array[0]);
						county.setName(array[1]);
						county.setCity_id(cityId);
						weatherDB.saveCounty(county);
					}
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �������������ص�JSON���ݣ��������������ݴ��浽������
	 * @param context
	 * @param response
	 */
	public static void handleWeatherResponse(Context context, String response){
		try{
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			saveWeatherInfo(context , cityName ,weatherCode, temp1,temp2 ,
					weatherDesp, publishTime);
		}catch (JSONException e ){
			e.printStackTrace();
		}
	}

	private static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d��", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true)
				.putString("city_name", cityName)
				.putString("weather_code", weatherCode)
				.putString("temp1", temp1)
				.putString("temp2", temp2)
				.putString("weather_desp", weatherDesp)
				.putString("publish_time", publishTime)
				.putString("current_date", sdf.format(new Date()))
				.commit();
				;
	}
}
