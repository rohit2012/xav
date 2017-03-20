package com.xavient.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author nkumar9
 * This class contains the methods for reading the expected data from excel.
 */

public class ExcelCache {

	//public static Logger logger=Logger.getLogger(ExcelCache.class);
	static HashMap<String, HashMap<String, String>> map = new LinkedHashMap<String, HashMap<String, String>>();
	/**
	 * @author nkumar9
	 * This block is loading the excel file and storing the data into the map.
	 */	
	static {

		FileInputStream file = null;
		try {
			file = new FileInputStream(new File(System.getProperty("user.dir")
					+ Properties_Reader.readProperty("XLS_file")));
					
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		HSSFWorkbook workbook = null;
		try {
			workbook = new HSSFWorkbook(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int sheetcount = workbook.getNumberOfSheets();
		for (int i = 0; i < sheetcount; i++) {
			HSSFSheet sheet = workbook.getSheetAt(i);
			String sheetName=workbook.getSheetName(i);
			
			Iterator<Row> rowIterator = sheet.rowIterator();
			rowIterator.next();
			while (rowIterator.hasNext()) {
				HashMap<String, String> innerMap = new LinkedHashMap<String, String>();
				Row row = rowIterator.next();
				innerMap.put(row.getCell(1,Row.CREATE_NULL_AS_BLANK).getStringCellValue(), row
						.getCell(2,Row.CREATE_NULL_AS_BLANK).getStringCellValue());
				map.put(sheetName+row.getCell(0,Row.CREATE_NULL_AS_BLANK).getStringCellValue(), innerMap);
			}
		}

	}
	/**
	 * @pageName this should be as test case class name
	 * @logicalName this is the object logical name
	 */
	public static List<String> getExpectedListData(String pageName,
			String logicalName)  {
		ArrayList<String> expectData = new ArrayList<String>();
		Set<String> objectNames = map.keySet();
		for (String object : objectNames) {
			if (object.equalsIgnoreCase(pageName+logicalName)) {
				HashMap<String, String> map2 = new LinkedHashMap<String, String>();
				map2 = (HashMap<String, String>) map.get(object);
				Set<String> objectTypeSet = map2.keySet();
				for (String objectType : objectTypeSet) {
					if (objectType.equalsIgnoreCase("list")) {
						String listValue = (String) map2.get(objectType);
						String[] listValues = listValue.split("\\$\\$");
						for (int i = 0; i < listValues.length; i++) {

							expectData.add(listValues[i].trim());
						}
					}

				}

			}
			//else
			//	logger.info("ObjectName or Sheet Name does not exist in the Excel");
		}
		return expectData;

	}
	/**
	 * @pageName this should be as test case class name
	 * @logicalName this is the object logical name
	 */
	
	public static String getExpectedData(String pageName, String logicalName)
			 {
		Set<String> objectNames = map.keySet();
		String expectedValue = null;
		for (String object : objectNames) {
			if (object.equalsIgnoreCase(pageName+logicalName)) {
				HashMap<String, String> map2 = new LinkedHashMap<String, String>();
				map2 = (HashMap<String, String>) map.get(object);
				Set<String> objectTypeSet = map2.keySet();
				for (String objectType : objectTypeSet) {
					if (objectType.equalsIgnoreCase("label")) {
						expectedValue = (String) map2.get(objectType);
					}

				}

			}
			//else
				//logger.info("ObjectName or Sheet Name does not exist in the Excel");
		}
		return expectedValue;

	}

	

}
