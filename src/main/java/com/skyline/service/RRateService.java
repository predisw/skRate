package com.skyline.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.hibernate.HibernateException;
import org.json.JSONArray;

import com.skyline.pojo.RRate;

public interface RRateService {
	
	public void SaveJsonToDb(JSONArray jsonArr) throws Exception;
}
