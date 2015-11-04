package com.skyline.pojo;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;






import lombok.Setter;
import lombok.ToString;

import com.predisw.annotation.Description;

// Generated 2015-9-9 9:11:18 by Hibernate Tools 4.3.1

/**
 * Customer generated by hbm2java
 */
@Description(name="客户")
public class Customer implements java.io.Serializable {

	private Integer CId;
	private Integer version;
	private String vosId;
	private String email;
	private String bccEmail;
	private String ccEmail="";
	private String CName;
	@Enumerated(EnumType.STRING)
	private Partner CType;
	private Integer DId;
	private String ENum;
	private Integer count;
	private Boolean isOften;
	private String rateLevel;

	public Customer() {
	}

	public Customer(String vosId, String email, String bccEmail,
			String ccEmail, String CName, Partner CType, Integer DId,
			String ENum, Integer count, Boolean isOften, String rateLevel) {
		this.vosId = vosId;
		this.email = email;
		this.bccEmail = bccEmail;
		this.ccEmail = ccEmail;
		this.CName = CName;
		this.CType = CType;
		this.DId = DId;
		this.ENum = ENum;
		this.count = count;
		this.isOften = isOften;
		this.rateLevel = rateLevel;
	}

	public Integer getCId() {
		return this.CId;
	}

	public void setCId(Integer CId) {
		this.CId = CId;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getVosId() {
		return this.vosId;
	}

	public void setVosId(String vosId) {
		this.vosId = vosId;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBccEmail() {
		return this.bccEmail;
	}

	public void setBccEmail(String bccEmail) {
		this.bccEmail = bccEmail;
	}

	public String getCcEmail() {
		return this.ccEmail;
	}

	public void setCcEmail(String ccEmail) {
		this.ccEmail = ccEmail;
	}

	public String getCName() {
		return this.CName;
	}

	public void setCName(String CName) {
		this.CName = CName;
	}

	public Partner getCType() {
		return this.CType;
	}

	public void setCType(Partner CType) {
		this.CType = CType;
	}

	public Integer getDId() {
		return this.DId;
	}

	public void setDId(Integer DId) {
		this.DId = DId;
	}

	public String getENum() {
		return this.ENum;
	}

	public void setENum(String ENum) {
		this.ENum = ENum;
	}

	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Boolean getIsOften() {
		return this.isOften;
	}

	public void setIsOften(Boolean isOften) {
		this.isOften = isOften;
	}

	public String getRateLevel() {
		return this.rateLevel;
	}

	public void setRateLevel(String rateLevel) {
		this.rateLevel = rateLevel;
	}

	@Override
	public String toString() {
		StringBuffer content = new StringBuffer();
		content.append("name:"+CName);
		content.append(",type:"+CType);
		content.append(",vosId:"+vosId);
		content.append(",email:"+email);
		content.append(",ccEmail:"+ccEmail);
		content.append(",bccEmail:"+bccEmail);
		return content.toString();
	}
	
	public String forLog(){
		return "name:"+this.getCName()+"-vosid:"+this.getVosId();
	}
	
	
	
}
