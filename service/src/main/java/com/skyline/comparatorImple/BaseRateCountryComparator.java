package com.skyline.comparatorImple;

import java.util.Comparator;

import com.skyline.pojo.BaseRate;

public class BaseRateCountryComparator implements Comparator<BaseRate>{

	@Override
	public int compare(BaseRate rate1, BaseRate rate2) {
		// TODO Auto-generated method stub
		
		return rate1.getCountry().compareToIgnoreCase(rate2.getCountry());
		
		//return (rate1.getCode()+rate1.getCountry()+rate1.getOperator()).compareToIgnoreCase(rate2.getCode()+rate2.getCountry()+rate2.getOperator());
	//	return (rate1.getCountry()+rate1.getOperator()+rate1.getCode()).compareToIgnoreCase(rate2.getCountry()+rate2.getOperator()+rate1.getCode());
	}

}
