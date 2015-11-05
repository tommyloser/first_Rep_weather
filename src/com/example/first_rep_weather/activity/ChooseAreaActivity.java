package com.example.first_rep_weather.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.first_rep_weather.R;
import com.example.first_rep_weather.db.WeatherDB;
import com.example.first_rep_weather.model.City;
import com.example.first_rep_weather.model.County;
import com.example.first_rep_weather.model.Province;
import com.example.first_rep_weather.util.HttpCallbackListener;
import com.example.first_rep_weather.util.HttpUtil;
import com.example.first_rep_weather.util.Utility;

public class ChooseAreaActivity extends Activity{
	public static final int LEVEL_PROVINCE=0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNT = 2;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private WeatherDB weatherDB;
	private List<String> dataList = new ArrayList<String>();
	/**
	 * ʡ�б�
	 */
	private List<Province> provinceList ;
	/**
	 * ���б�
	 */
	private List<City> cityList;
	/**
	 * ���б�
	 */
	private List<County> countylist;
	/**
	 * ѡ�е�ʡ��
	 */
	private Province selectedProvince;
	/**
	 * ѡ�еĳ���
	 */
	private City selectedCity;
	/**
	 * ��ǰѡ�еļ���
	 */
	private int currentLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//����Ѿ�ѡ����Ͳ�����ѡ���Զ���������activity
		SharedPreferences spfrs = PreferenceManager.getDefaultSharedPreferences(this);
		if (spfrs.getBoolean("city_selected", false)) {
			Intent intent = new Intent(this,WeatherActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this, android.R.layout
				.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		weatherDB= WeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(currentLevel == LEVEL_PROVINCE){
					selectedProvince = provinceList.get(position);
					queryCities();
				}else if(currentLevel == LEVEL_CITY){
					selectedCity = cityList.get(position);
					queryCounties();
				}else if(currentLevel == LEVEL_COUNT){
					Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
					intent.putExtra("county_code", countylist.get(position).getCode());
					startActivity(intent);
					finish();
				}
			}

		});
		queryProvinces();
	}

	/**
	 * ��ѯȫ�����е�ʡ�����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ��
	 */
	private void queryProvinces() {
		provinceList = weatherDB.loadProvince();
		if(provinceList.size()>0){
			dataList.clear();
			for(Province p : provinceList){
				dataList.add(p.getName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			currentLevel = LEVEL_PROVINCE;
		}else{
			queryFromServe(null,"province");
		}
		
	}
	
	/**
	 * ��ѯʡ�������У��ȴ����ݿ��ѯ�����û�в�ѯ����ӷ������ϲ�ѯ
	 */
	protected void queryCities() {
		cityList = weatherDB.loadCity();
		if(cityList.size() > 0){
			dataList.clear();
			for (City c: cityList){
				dataList.add(c.getName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			currentLevel = LEVEL_CITY;
		}else{
			queryFromServe( selectedProvince.getCode(),"city");
		}
		
	}
	
	/**
	 * ��ѯ�����������������ȴ����ݿ��ѯ�����û�в�ѯ����ӷ������ϲ�ѯ
	 */
	protected void queryCounties() {
		countylist = weatherDB.loadCounty();
		if(countylist.size()>0){
			dataList.clear();
			for(County c: countylist){
				dataList.add(c.getName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			currentLevel = LEVEL_COUNT;
		}else{
			queryFromServe(selectedCity.getCode(), "county");
		}
		
	}


	private void queryFromServe(final String code, final String type) {
		String address;
		if(!TextUtils.isEmpty(code)){
			address = "http://www.weather.com.cn/data/list3/city" + code +
					".xml";
		}else{
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onfinish(String response) {
				boolean result = false;
				if("province".equals(type)){
					result = Utility.handleProvincesResponce(weatherDB, response);
				}else if( "city".equals(type)){
					result = Utility.handleCityResponce(weatherDB, response, selectedProvince.getId());
				}else if( "county".equals(type)){
					result = Utility.handlerCountyResponce(weatherDB, response, selectedCity.getId());
				}
				if(result){
					//ͨ��runonUithread()�����ص����̴߳����߼�
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							closeProgressDialog();
							if("province".equals(type)){
								queryProvinces();
							}else if ("city".equals(type)){
								queryCities();
							}else if ("county".equals(type)){
								queryCounties();
							}
						}
					});
				}
				
			}
			
			@Override
			public void onError(Exception e) {
				// ͨ����������ص����̴߳����߼�
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}


	/**
	 * �رս��ȶԻ���
	 */
	private void closeProgressDialog(){
		if(progressDialog != null){
			progressDialog.dismiss();
		}
	}
	
	/**
	 * ��ʾ���ȶԻ���
	 */
	private void showProgressDialog() {
		if(progressDialog == null){
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ��ء�����");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
		
	}
	
	/**
	 * ���ݵ�ǰ�������ж� ��ʡ�б����б������˳�
	 */
	@Override
	public void onBackPressed() {
		if(LEVEL_PROVINCE == currentLevel){
			super.onBackPressed();
		}else if(LEVEL_CITY == currentLevel){
			queryProvinces();
		}else if(LEVEL_COUNT == currentLevel){
			queryCities();
		}
	}
	
	
}
