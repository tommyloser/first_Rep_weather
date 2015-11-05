package com.example.first_rep_weather.activity;

import com.example.first_rep_weather.R;
import com.example.first_rep_weather.util.HttpCallbackListener;
import com.example.first_rep_weather.util.HttpUtil;
import com.example.first_rep_weather.util.Utility;

import android.app.Activity;
import android.app.DownloadManager.Query;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener{

	private LinearLayout weatherInfoLayout;
	/** ������ʾ������ */
	private TextView cityNameText;
	/** ������ʾ����ʱ�� */
	private TextView publishText;
	/** ������ʾ����������Ϣ */
	private TextView weatherDespText;
	/** ������ʾ����1 */
	private TextView temp1Text;
	/** ������ʾ����2 */
	private TextView temp2Text;
	/** ������ʾ��ǰ���� */
	private TextView currentDateText;
	/** �л����а�ť */
	private Button switchCity;
	/** ����������ť */
	private Button refreshWeather;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_layout);
		// ��ʼ�����ؼ�
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh);
		String countyCode = getIntent().getStringExtra("county_code");
		if (!TextUtils.isEmpty(countyCode)) {
			// ���سǴ���ʱȥ��ѯ����
			publishText.setText("ͬ���С�����");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		}else {
			// û���سǴ���ʱ��ֱ����ʾ��������
			showWeather();
		}
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
//		if (!TextUtils.isEmpty(countyCode)) {
//			// ���ؼ�����ʱ��ȥ��ѯ����
//			publishText.setText("ͬ����...");
//			weatherInfoLayout.setVisibility(View.INVISIBLE);
//			cityNameText.setVisibility(View.INVISIBLE);
//			queryWeatherCode(countyCode);
//			} else {
//			// û���ؼ�����ʱ��ֱ����ʾ��������
//			showWeather();
//			}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh:
			publishText.setText("ͬ���С�����");
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			String weatherCode = prefs.getString("weather_code", "");
			if (!TextUtils.isEmpty(weatherCode)) {
				queryWeatherInfo(weatherCode);
			}
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * ��ѯ������������Ӧ������
	 * @param weatherCode
	 */
	private void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/" +
				weatherCode + ".html";
		queryFromServer(address, "weatherCode");
	}
	
	/**
	 * ��ѯ�ش�������Ӧ����������
	 * @param countyCode
	 */
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city" +
				countyCode + ".xml";
		queryFromServer(address, "countyCode");
		
	}
	
	/**
	 * ���ݴ��˵ĵ�ַ������ȥ��������ѯ�������Ż�������Ϣ
	 * @param address
	 * @param string
	 */
	private void queryFromServer(String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onfinish(String response) {
				if ("countyCode".equals(type)) {
					if(!TextUtils.isEmpty(response)){
						//�ӷ��������ص������н�������������
						String [] array = response.split("\\|");
						if(array != null && array.length == 2) {
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				} else if ("weatherCode".equals(type)){
					//�����������������ص�������Ϣ
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							showWeather();
							
						}
					});
				}
				
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						publishText.setText("ͬ��ʧ��");
						
					}
				});
				
			}
		});
		
	}

	/**
	 * ��SharedPreferences��ȡ�洢��������Ϣ������ʾ�ڽ�����
	 */
	private void showWeather() {
		SharedPreferences spfrs = PreferenceManager
				.getDefaultSharedPreferences(this);
		cityNameText.setText(spfrs.getString("city_name", ""));
		temp1Text.setText(spfrs.getString("temp1", ""));
		temp2Text.setText(spfrs.getString("temp2", ""));
		weatherDespText.setText(spfrs.getString("weather_desp", ""));
		publishText.setText(spfrs.getString("current_date", "")+"����");
		currentDateText.setText(spfrs.getString("current_data", ""));//�����ʱ��
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		
	}
}
