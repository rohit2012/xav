/**
 * 
 */
package com.test.java;

/**
 * @author csingh5
 *
 */
public interface Login {

	int i =10;
	void loginWeb();
	static void myTest()
	{
		System.out.println();
	}

	default boolean isPresent()
	{
	
		return true;
	}
	
}
