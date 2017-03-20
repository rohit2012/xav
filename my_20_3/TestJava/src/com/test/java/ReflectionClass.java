/**
 * 
 */
package com.test.java;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

import com.itextpdf.text.log.SysoCounter;

/**
 * @author csingh5
 *
 */
public class ReflectionClass {

	/**
	 * @param args
	 */
	public void testClass()
	{
		System.out.println("Class Method is called");
	}

	public void testmethod(String s)
	{
		System.out.println("Method String : "+s); 

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// getClass method
		ReflectionClass obj = new ReflectionClass();
		Class<?> c= obj.getClass();
		System.out.println("GET Class : "+c);

		// .class syntax (boolean is a primitive type and cannot be dereferenced. 
		//The .class syntax returns the Class corresponding to the type boolean.)
		Class<?> c1 = boolean.class;
		System.out.println(".class : "+c1);

		// Class.forName()
		try {
			Class<?> c2 = Class.forName("com.test.java.ReflectionClass");
			try {
				ReflectionClass obj1 = (ReflectionClass) c2.newInstance();
				obj1.testClass();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("Class.forName() "+c2);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Methods are calling Class
		Class<?>[] arr = Character.class.getClasses();
		for(Object b : arr)
		{
			System.out.println(" : "+b.toString());
		}

		Class<?> clas = ReflectionClass.class;
		Method[] method = clas.getMethods();
		int i=1;
		for(Method m : method)
		{
			System.out.println(i+". Method Name : "+m.getName());
			if(m.getName().equals("testmethod"))
			{
				try {
					m.invoke(new ReflectionClass(),"CHIRAG");
					break;
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			i++;
		}
	}

}
