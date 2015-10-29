package com.skyline.pojo;

import com.predisw.annotation.Description;

@Description(name="供应商报价")
public class RRate extends BaseRate implements Cloneable, java.io.Serializable {

	public String forLog(){
		return "vosId:"+this.getVosId()+"-code: "+this.getCode();
	}
	


}
