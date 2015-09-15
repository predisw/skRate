package com.skyline.service;

import java.io.IOException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public interface SysService {

	String getPerformance(String pathFileName, int arrLength) throws IOException, JSONException, ParseException;

	String getCurrentPerformance();

	
}
