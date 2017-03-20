package com.chirag.seleniumframework.testscripts;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.testng.annotations.Test;

import com.chirag.seleniumframework.util.NewExcelUtility;

public class RunTest {

	@Test
	public void runTestScript() throws ClassNotFoundException
	{
		Class<?> c = DriverClass.class;
		Method[] method = c.getMethods();
		ArrayList<Object> keywords = NewExcelUtility.readKeywords();

		for(int j=0;j<=method.length-1;j++)
		{
			for(Method m : method)
			{
				try {
					if(m.getName().equals(keywords.get(j).toString()))
					{
						try {
							//System.out.println("method : "+m.getName());
							m.invoke(new DriverClass());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}
}
