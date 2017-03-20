package com.test.java;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionTest {

	// URL : https://www.javacodegeeks.com/2012/11/java-regular-expression-tutorial-with-examples.html


	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("\\D");
		String[] s = pattern.split("D12:34");
		for(String s1 : s)
		{
			if(!s1.equals(""))
			{
				System.out.println(s1);
			}
			
		}
		pattern = Pattern.compile("\\W");
		Matcher matcher = pattern.matcher("Abr$98@");
		while(matcher.find())
		{
			System.out.println("Start : "+matcher.start()+" : "+"End : "+matcher.end() + " : "+matcher.group());
		}
		System.out.println(matcher.replaceAll(""));
	}

}
