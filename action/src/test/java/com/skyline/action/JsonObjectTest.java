package com.skyline.action;

import java.util.Iterator;

import org.json.JSONObject;
import org.junit.Test;

public class JsonObjectTest {

	@Test
	public void test(){
		JSONObject obj = new JSONObject();
		obj.put("001", "a");
		obj.put("009", "c");
		
		Iterator<String>   ite =obj.keys();
		while(ite.hasNext()){
			System.out.println(obj.get(ite.next()));
			
		}
		
	}
}
