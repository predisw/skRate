package com.skyline.pojo;

// Generated 2015-8-13 14:52:03 by Hibernate Tools 4.3.1

import java.text.DecimalFormat;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import com.predisw.annotation.Description;

/**
 * Rate generated by hbm2java
 */
@Description(name="报价记录")
public class Rate extends BaseRate  implements Cloneable, java.io.Serializable {

	private Integer RId;
	
	private Integer PRid;

	public Rate() {
	}



	public Integer getRId() {
		return this.RId;
	}

	public void setRId(Integer RId) {
		this.RId = RId;
	}

	
	public Integer getPRid() {
		return this.PRid;
	}

	public void setPRid(Integer PRid) {
		this.PRid = PRid;
	}


	

	
	

}