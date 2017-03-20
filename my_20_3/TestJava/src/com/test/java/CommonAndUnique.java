package com.test.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommonAndUnique {

	public static <T> Set<T> getCommonElement(Collection<? extends Collection<T>> collection)
	{
		Set<T> common = new HashSet<T>();
		if(!collection.isEmpty())
		{
			Iterator<? extends Collection<T>> itr= collection.iterator();
			common.addAll(itr.next());
			while(itr.hasNext())
			{
				common.retainAll(itr.next());
			}
		}
		return common;
	}
	
	public static <T> List<Set<T>> getUnquieElement(Collection<? extends Collection<T>> collection)
	{
		List<Set<T>> allUniqueSets = new ArrayList<Set<T>>();
		for (Collection<T> collections : collection) {
			Set<T> unique = new LinkedHashSet<T>(collections);
			allUniqueSets.add(unique);
			for (Collection<T> allCollection : collection) {
				if(collections!=allCollection)
				{
					unique.removeAll(allCollection);
				}
			}
		}
		return allUniqueSets;
	}
	
	public static void main(String[] args) {
		List<String> l = Arrays.asList("Chirag","Singh","test","Singh");
		List<String> l1 = Arrays.asList("Vicky","Singh","test");
		List<List<String>> arr = new ArrayList<List<String>>();
		arr.add(l);
//		arr.add(l1);
		List<String> arr1 = new ArrayList<String>();
		arr1.add("Vicky");
		arr1.add("Singh");
		arr1.add("test");
		//arr.add(arr1);
		/*System.out.println("COMMON : "+getCommonElement(arr));
		System.out.println("UNIQUE : "+getUnquieElement(arr));*/
		
		
		/*System.out.println(arr1.retainAll(l1));
		System.out.println(arr1);*/
		
		Map<String, String> m = new HashMap<String,String>();
		m.put("1", "Chirag");
		m.put("2", "Singh");
		
		/*Set<String> s = m.keySet();
		for( String d : s)
		{
			System.out.println(d+" : "+m.get(d));
		}*/
		
	
		Iterator<String> i = m.keySet().iterator();
		while(i.hasNext())
		{
		
			String k = i.next();
			System.out.println(k + " : "+m.get(k));
		}
		
	}

}
