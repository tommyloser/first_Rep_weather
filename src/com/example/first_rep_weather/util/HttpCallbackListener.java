package com.example.first_rep_weather.util;

public interface HttpCallbackListener {
	
	void onfinish(String response);
	
	void onError(Exception e);
}
