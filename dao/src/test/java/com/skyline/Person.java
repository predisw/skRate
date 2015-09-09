package com.skyline;

public class Person {
	public static  String name;
	private int age;
	/**
	 * @return the name
	 */
	public   String getName() {
		System.out.println("person.name:"+name);
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public   void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the age
	 */
	public  int getAge() {
		System.out.println("person.age:"+age);
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}
	
}
