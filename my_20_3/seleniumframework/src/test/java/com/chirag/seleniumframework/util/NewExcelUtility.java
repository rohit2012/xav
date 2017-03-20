package com.chirag.seleniumframework.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class NewExcelUtility {

	static Workbook workbook = null;
	static Sheet sheet =null;
	static File excelFile =null;
	static FileInputStream inputstream =null;
	static ArrayList<Integer> rowN;
	
	//static int testcaseNo = 0;
	public static void initalizeObjects(String filePath,String fileName,String sheetName)
	{
		try {
			excelFile = new File(filePath);
			inputstream = new FileInputStream(excelFile);
			String fileExtention = fileName.substring(fileName.indexOf("."));
			if(fileExtention.equalsIgnoreCase(".xlsx"))
			{
				workbook = new XSSFWorkbook(inputstream);
			} else if(fileExtention.equalsIgnoreCase(".xls"))
			{
				workbook = new HSSFWorkbook(inputstream);
			}
			sheet = workbook.getSheet(sheetName);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static ArrayList<Object> readExcelByColumn(String filePath,String fileName,String sheetName,String columnName)
	{
		ArrayList<Object> data = new ArrayList<Object>();
		try {
			initalizeObjects(filePath, fileName,sheetName);
			int rowNum = sheet.getLastRowNum();
			int colNum =0;
			String colName = null;
			int k=0;
			for(int i=0;i<=rowNum-1;i++)
			{
				colNum = sheet.getRow(i).getLastCellNum();
				for(int j=0;j<=colNum-1;j++)
				{
					colName = sheet.getRow(i).getCell(j).getStringCellValue();
					if(columnName.equalsIgnoreCase(colName))
					{
						k=j;
						break;
					}
				}
				try {
					data.add(sheet.getRow(i+1).getCell(k).getStringCellValue());
				} catch (Exception e) {
					e.getMessage();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public static HashMap<Object,Object> newreadExcelByColumn(String filePath,String fileName,String sheetName,String columnName)
	{
		HashMap<Object,Object>  data = new HashMap<Object,Object> ();
		try {
			initalizeObjects(filePath, fileName,sheetName);
			int rowNum = sheet.getLastRowNum();
			int colNum =0;
			String colName = null;
			int k=0;
			for(int i=0;i<=rowNum-1;i++)
			{
				colNum = sheet.getRow(i).getLastCellNum();
				for(int j=0;j<=colNum-1;j++)
				{
					colName = sheet.getRow(i).getCell(j).getStringCellValue();
					if(columnName.equalsIgnoreCase(colName))
					{
						k=j;
						break;
					}
				}
				try {
					data.put(i,sheet.getRow(i+1).getCell(k).getStringCellValue());
				} catch (Exception e) {
					e.getMessage();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public static void readExcelAllData(String filePath,String fileName,String sheetName)
	{
		try {
			initalizeObjects(filePath, fileName,sheetName);
			int rowNum = sheet.getLastRowNum();
			int colNum=0;
			for(int i=1;i<=rowNum-1;i++)
			{
				colNum = sheet.getRow(i).getLastCellNum();
				for(int j=0;j<=colNum-1;j++)
				{
					try {
						System.out.println(" Test Data : "+ sheet.getRow(i).getCell(j).getStringCellValue());
					} catch (Exception e) {
						e.getMessage();
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static HashMap<Object,Object> readTestCases()
	{
		HashMap<Object,Object>  data = new HashMap<Object,Object>();
		
		try {
			initalizeObjects(ContantVariables.webSuite, "webApplicationSuite.xlsx",ContantVariables.webTestCaseSheetName);
			int rowNum = sheet.getLastRowNum();
			int colNum=0;
			int testcaseNo = 0;
			for(int i=1;i<=rowNum;i++)
			{
				colNum = sheet.getRow(i).getLastCellNum();
				for(int j=0;j<=colNum-1;j++)
				{
					try {
						if(sheet.getRow(i).getCell(2).getStringCellValue().equalsIgnoreCase("Y")){
							testcaseNo++;
							data.put("TestCaseName"+testcaseNo,sheet.getRow(i).getCell(1).getStringCellValue());
							data.put("TestDataIteration"+testcaseNo,new DataFormatter().formatCellValue(sheet.getRow(i).getCell(3)));
							break;
						}
					} catch (Exception e) {
						e.getMessage();
					}

				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public static ArrayList<Object> readKeywords()
	{
		HashMap<Object, Object> testCases = readTestCases();
		ArrayList<Object> datatestCases = new ArrayList<Object>();
		int testcaseNo=1;
		for(int i = 0;i<=(testCases.size()/2)-1;i++)
		{
			String fileName = testCases.get("TestCaseName"+testcaseNo).toString().trim()+".xlsx";
			String f = "D:\\Folder Strucure\\Projects\\Charter Communication\\workspace\\seleniumframework\\executioninfo\\testcases\\"+fileName;
			try {
				initalizeObjects(f, fileName,ContantVariables.webKeywordsSheetName);
				int rowNum = sheet.getLastRowNum();
				int colNum =0;
				String colName = null;
				int k=0;
				for(int l=0;l<=rowNum-1;l++)
				{
					colNum = sheet.getRow(l).getLastCellNum();
					for(int j=0;j<=colNum-1;j++)
					{
						colName = sheet.getRow(l).getCell(j).getStringCellValue();
						if("RunKeywords".equalsIgnoreCase(colName))
						{
							k=j;
							break;
						}
					}
					try {
						datatestCases.add(l,sheet.getRow(l+1).getCell(k).getStringCellValue());
					} catch (Exception e) {
						e.getMessage();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			testcaseNo++;
		}
		for(Object s : datatestCases)
		{
			//System.out.println("KEY : "+s.toString());
		}
		
		return datatestCases;
	}

	public static ArrayList<Object> readTestData(String s)
	{
		HashMap<Object, Object> testCases = readTestCases();
		ArrayList<Object> datatestCase = null;
		ArrayList<Object> testData = new ArrayList<Object>();
		int testcaseNo=1;
		for(int i = 0;i<=(testCases.size()/2)-1;i++)
		{
			String fileName = testCases.get("TestCaseName"+testcaseNo).toString().trim()+".xlsx";
			String f = "D:\\Folder Strucure\\Projects\\Charter Communication\\workspace\\seleniumframework\\executioninfo\\testcases\\"+fileName;
			datatestCase = readExcelByColumn(f, fileName,ContantVariables.webTestDataSheetName,s);
			int p = Integer.parseInt(testCases.get("TestDataIteration"+testcaseNo).toString());
			for(int m=0;m<=p-1;m++)
			{
				testData.add(datatestCase.get(m).toString());
			    //System.out.println(datatestCase.get(m).toString());
			}
			testcaseNo++;
		}
		return testData;
	}


	public static void main(String[] args) {
//		readTestCases();
	readKeywords();
//		readTestData("Browser");
//		readTestData("ExpectedTitle");
	}
}
