package com.example.first_rep_weather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	
	/**
	 * send http request by "get" method
	 * @param address  需要访问的地址
	 * @param listener callback interface 回调接口
	 */
	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener){
		new Thread(new Runnable(){

			@Override
			public void run() {
				HttpURLConnection connection = null;
				try{
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader
							(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while((line=reader.readLine())!= null){
						response.append(line);
					}
					if(listener != null){
						listener.onfinish(response.toString());
					}
				}catch (Exception e){
					if( listener != null){
						listener.onError(e);
					}
				}finally {
					if(connection != null){
						connection.disconnect();
					}
				}
				
			}
			
		}).start();
	}
	
}
