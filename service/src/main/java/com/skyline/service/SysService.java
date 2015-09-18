package com.skyline.service;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public interface SysService {

	String getPerformance(Path path, int arrLength) throws IOException, JSONException, ParseException;

	String getCurrentPerformance();

	
}
