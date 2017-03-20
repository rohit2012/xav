package com.test.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CompratorTest implements Comparator<CompratorNameTest>{
	
	
	
	public static void main(String[] args) {
		ArrayList<CompratorNameTest> i = new ArrayList<CompratorNameTest>();
		i.add(new CompratorNameTest(1, "Abhay"));
		i.add(new CompratorNameTest(5, "Chirag"));
		i.add(new CompratorNameTest(2, "Bobby"));
		
		for(CompratorNameTest l : i)
		{
			System.out.println(l.roll+" : "+l.name);
		}
		System.out.println("===================");
		
		Collections.sort(i,new CompratorTest());
		
		for(CompratorNameTest l : i)
		{
			System.out.println(l.roll+" : "+l.name);
		}
		
		//System.out.println((1>5)?0:1);
	}

	@Override
	public int compare(CompratorNameTest o1, CompratorNameTest o2) {
		//return o1.name.compareTo(o2.name);
		return(o1.roll>o2.roll)? -1:(o1.roll<o2.roll)?1:0;
	}

	
}
