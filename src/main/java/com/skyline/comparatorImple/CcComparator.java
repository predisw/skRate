package com.skyline.comparatorImple;

import java.util.Comparator;

import com.skyline.pojo.CountryCode;

public class CcComparator implements Comparator<CountryCode> {

	@Override
	public int compare(CountryCode cc1, CountryCode cc2) {
		// TODO Auto-generated method stub
		return cc1.getCountry().compareToIgnoreCase(cc2.getCountry());
	}

}
