package com.zensar.myfirstproject.beans;

import java.util.Comparator;

public class NameComparator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		Employee e1=(Employee)o1;
		Employee e2=(Employee)o2;
		
		return e1.getEmployeeName().compareTo(e2.getEmployeeName());
	}

}
