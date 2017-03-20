package com.test.java;

import java.lang.reflect.Field;

public class ReflectionChangeValue {

	public int i =0;
	public final long b = 1;
	
	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
		ReflectionChangeValue obj = new ReflectionChangeValue();
		
		// Normal Change value
		Class<?> c = ReflectionChangeValue.class;
		Field f = c.getDeclaredField("i");
		System.out.println("Before : "+ obj.i);
		f.setInt(obj, 10);
		System.out.println("After : "+ obj.i);
		
		ReflectionChangeValue obj1 = new ReflectionChangeValue();
		// Final Variable change value
		//Class<?> c1 = Class.forName("com.test.java.ReflectionChangeValue");
		Class<?> c1 = obj1.getClass();
		Field f1 = c1.getDeclaredField("b");
		System.out.println("Before : "+obj1.b);
		
		f1.setAccessible(true);
		
		f1.setLong(obj1, 2);
		System.out.println("After : "+obj1.b);
		
	}

}
