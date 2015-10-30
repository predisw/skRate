package com.skyline.comparatorImple;

import java.util.Comparator;

import com.skyline.pojo.BaseRate;

public class BaseRateEfTimeComparator implements Comparator<BaseRate> {

	@Override
	public int compare(BaseRate rate1, BaseRate rate2) {
		// TODO Auto-generated method stub
		return rate1.getEffectiveTime().compareTo(rate2.getEffectiveTime());
	}

}