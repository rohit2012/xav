package com.test.java;

import java.util.ArrayList;
import java.util.Arrays;

public class LambdasTest {

	
	public static void methodOne()
	{
		
		Arrays.asList(new ArrayList<String>()).forEach(e -> System.out.println(e));
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		methodOne();
	}

}
