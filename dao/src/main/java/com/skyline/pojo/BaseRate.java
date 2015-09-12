package com.skyline.pojo;

import java.text.DecimalFormat;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import com.predisw.annotation.Description;
@Description(name="baseRate")
public class BaseRate {
	private Integer id;
	private String name;
	private String country;
	private String operator;
	private String code;
	@NumberFormat
	private Double rate;
	@NumberFormat
	private Double newRate;
	private String RSymbol;
	private String isChange;
	private String billingType;
	private String level;
	private String levelPrefix;
	@DateTimeFormat(pattern="yyyy-MM-dd") /* <input type="date" name="sendTime">*/
	private Date sendTime;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date effectiveTime;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date expireTime;
	private String ENum;
	private String vosId;
	private String bakVosId;
	private Integer DId;
	private Boolean isSuccess;
	private Boolean isAvailable;
	private Boolean isCorrect;
	private int CId;
	private String remark;
	private Integer Pid;

	public BaseRate() {
	}

	public BaseRate(String name, String country, String operator, String code,
			Double rate, Double newRate, String RSymbol, String isChange,
			String billingType, String level, String levelPrefix,
			Date sendTime, Date effectiveTime, Date expireTime, String ENum,
			String vosId, String bakVosId, Integer DId, Boolean isSuccess,
			Boolean isAvailable, Boolean isCorrect, int CId, String remark,
			Integer Pid) {
		this.name = name;
		this.country = country;
		this.operator = operator;
		this.code = code;
		this.rate = rate;
		this.newRate = newRate;
		this.RSymbol = RSymbol;
		this.isChange = isChange;
		this.billingType = billingType;
		this.level = level;
		this.levelPrefix = levelPrefix;
		this.sendTime = sendTime;
		this.effectiveTime = effectiveTime;
		this.expireTime = expireTime;
		this.ENum = ENum;
		this.vosId = vosId;
		this.bakVosId = bakVosId;
		this.DId = DId;
		this.isSuccess = isSuccess;
		this.isAvailable = isAvailable;
		this.isCorrect = isCorrect;
		this.CId = CId;
		this.remark = remark;
		this.Pid = Pid;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer Id) {
		this.id = Id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getRate() {
		return this.rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getNewRate() {
		return this.newRate;
	}

	public void setNewRate(Double newRate) {
		this.newRate = newRate;
	}

	public String getRSymbol() {
		return this.RSymbol;
	}

	public void setRSymbol(String RSymbol) {
		this.RSymbol = RSymbol;
	}

	public String getIsChange() {
		return this.isChange;
	}

	public void setIsChange(String isChange) {
		this.isChange = isChange;
	}

	public String getBillingType() {
		return this.billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLevelPrefix() {
		return this.levelPrefix;
	}

	public void setLevelPrefix(String levelPrefix) {
		this.levelPrefix = levelPrefix;
	}

	public Date getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getEffectiveTime() {
		return this.effectiveTime;
	}

	public void setEffectiveTime(Date effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public Date getExpireTime() {
		return this.expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public String getENum() {
		return this.ENum;
	}

	public void setENum(String ENum) {
		this.ENum = ENum;
	}

	public String getVosId() {
		return this.vosId;
	}

	public void setVosId(String vosId) {
		this.vosId = vosId;
	}

	public String getBakVosId() {
		return this.bakVosId;
	}

	public void setBakVosId(String bakVosId) {
		this.bakVosId = bakVosId;
	}

	public Integer getDId() {
		return this.DId;
	}

	public void setDId(Integer DId) {
		this.DId = DId;
	}

	public Boolean getIsSuccess() {
		return this.isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Boolean getIsAvailable() {
		return this.isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Boolean getIsCorrect() {
		return this.isCorrect;
	}

	public void setIsCorrect(Boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public int getCId() {
		return this.CId;
	}

	public void setCId(int CId) {
		this.CId = CId;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getPid() {
		return this.Pid;
	}

	public void setPid(Integer Pid) {
		this.Pid = Pid;
	}

	public String doubleF(Double d){
		DecimalFormat df = new DecimalFormat("##0.########");
		return df.format(d);
	}
	
	public String forLog(){
		return "vosId:"+this.getVosId()+"-code: "+this.getCode();
	}
}
