package com.test.java;

import java.util.regex.Pattern;

import org.testng.annotations.Test;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class TestJava {

	private static void testReturn(int a,int b)
	{
		a =15;
		if(a>=b)
		{
			System.out.println(a+" : "+b);
			return;
		} else
		{
			System.out.println(a+" : "+b);
			return;
		}
	}
	
	public static void testSwitch(String i)
	{
		switch(i)
		{
		case "Jan":
			System.out.println(i);
			break;
		case "Feb":
			System.out.println(i);
			break;
		default :
			System.out.println("No value "+i);
			break;
		}
	}
	
	enum test{Chirag,singh};

	public static void displayEnum()
	{
		System.out.println(test.Chirag);
		String s = "Ch";
		System.out.println(s.matches(".h"));
		System.out.println(Pattern.matches(".h", s));
		Predicate<String> ff = Predicates.notNull();
		Predicate<Integer> isZero =  Predicates.equalTo(0);
		System.out.println( ff + ": "+isZero);
		Predicate<Integer> isNullOrZero = Predicates.or(isZero, Predicates.isNull());
		System.out.println(isNullOrZero);
	}
	
	//public static void main(String[] args) {

	@Test
	public void myRest()
	{
		int a =15;
		testReturn(a, 4);
		testSwitch("Jan");
		displayEnum();
	}
}
