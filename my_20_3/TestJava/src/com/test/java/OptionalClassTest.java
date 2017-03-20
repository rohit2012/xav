package com.test.java;

import java.util.Optional;

public class OptionalClassTest {

	// isPresent
	public static void isPresentTest()
	{
		Optional<String> s = Optional.of("o");
		if(s.isPresent())
		{
			System.out.println(s.get());
		}
	}
	
	
	
	public static void main(String[] args) {
		isPresentTest();
	
	}
}
