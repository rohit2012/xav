package com.test.java;

public class Encapsulation_Test {

	private String name;
	private int roll_num;
	
	public Encapsulation_Test(String name,int roll_num) {
		this.name = name;
		this.roll_num = roll_num;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getRollNum()
	{
		return roll_num;
	}
	
	public void setRollNum(int roll_num)
	{
		this.roll_num = roll_num;
	}
}
