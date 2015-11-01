package com.example.first_rep_weather.util;

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
}
