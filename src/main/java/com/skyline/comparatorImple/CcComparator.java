package com.skyline.comparatorImple;

import java.util.Comparator;

import com.skyline.pojo.CountryCode;

public class CcComparator implements Comparator<CountryCode> {

	@Override
	public int compare(CountryCode cc1, CountryCode cc2) {
		int	cpr= cc1.getCountry().compareToIgnoreCase(cc2.getCountry());
		 if(cpr==0){
			cpr=cc1.getOperator().compareToIgnoreCase(cc2.getOperator());
			if(cpr==0){
				cpr=cc1.getCode().compareTo(cc2.getCode());
			}
		 }
		 
		 return cpr;
	}



}