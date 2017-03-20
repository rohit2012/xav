package com.test.java;

public class Encapsulation_Test1 {

	public static void main(String[] args) {
		Encapsulation_Test obj = new Encapsulation_Test("Chirag",15);
		System.out.println(obj.getName()+" : "+obj.getRollNum());
		obj.setName("Vicky");
		System.out.println(obj.getName()+" : "+obj.getRollNum());
		Object s = 5;
		if(s instanceof Integer)
		{
			System.out.println("S is Integer");
		}else if(s instanceof String)
		{
			System.out.println("S is String");
		}else if(s instanceof Long)
		{
			System.out.println("S is Long");
		}else if(s instanceof Float)
		{
			System.out.println("S is Float");
		}
		
	}
	
}
