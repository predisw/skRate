package com.skyline.comparatorImple;

import java.util.Comparator;

import com.skyline.pojo.BaseRate;

public class BaseRateComparator  implements Comparator<BaseRate>{

	@Override
	public int compare(BaseRate rate1, BaseRate rate2) {
		// TODO Auto-generated method stub
		
		
		return (rate1.getCountry()+rate1.getOperator()+rate1.getCode()).compareToIgnoreCase(rate2.getCountry()+rate2.getOperator()+rate1.getCode());
	}

}
