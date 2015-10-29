package com.skyline.pojo;

import java.io.Serializable;

public enum Partner implements Serializable{
	
	PROVIDER("PROVIDER"),CUSTOMER("CUSTOMER");
	
	private String name;
	private Partner(String name){
		this.name=name.toUpperCase();
	}
	
	@Override
	public String toString(){
		return this.name;
	}
}
