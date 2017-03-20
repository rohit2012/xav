package com.test.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ComparableTest implements Comparable<Object>{

	 ArrayList<Integer> i = new ArrayList<Integer>();
	
	public void addListNumber()
	{
		i.add(1);
		i.add(5);
		i.add(2);
		i.add(10);
	}
	
	public  void printList()
	{
		addListNumber();
		
		for(Integer h : i)
		{
			System.out.println(h);
		}
		
//	System.out.println(compareTo(i));
		Collections.sort(i);
		System.out.println("===================");
		for(Integer h : i)
		{
			System.out.println(h);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Object o) {
		ArrayList<Integer> n = (ArrayList<Integer>)o;
		return(n.get(0)>n.get(1)?1:(n.get(0)<n.get(1)?-1:0));
	}
	
	public static void main(String[] args) {
		ComparableTest obj = new ComparableTest();
		obj.printList();
		
	}

	
}
