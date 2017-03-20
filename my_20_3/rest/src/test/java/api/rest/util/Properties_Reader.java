package api.rest.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Properties_Reader {

	public static String readProperty(String key) {
		//Initialize.
		File file = new File(System.getProperty("user.dir")	+ "\\Resources\\dataFile.properties");
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
