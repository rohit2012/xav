package com.xavient.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Properties_Reader {

	/**
	 * Method is fetching values from config files.
	 * @param key
	 * @return String value based on key.
	 * @author NMakkar
	 */
	public static String readProperty(String key) {
		//Initialize.
		File file = new File(System.getProperty("user.dir")	+ "\\src\\test\\resources\\testData\\dataFile.properties");
		Properties prop = new Properties();
		FileInputStream inputFile = null;
		{
			try {
				inputFile = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				//Loading file
				prop.load(inputFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Returning value.
			return prop.getProperty(key);
		}
	}	
}