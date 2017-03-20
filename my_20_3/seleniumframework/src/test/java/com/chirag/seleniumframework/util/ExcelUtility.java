package com.chirag.seleniumframework.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility {

	static Workbook workbook = null;
	static Sheet sheet =null;
	static File excelFile =null;
	static FileInputStream inputstream =null;
	static ArrayList<Integer> rowN;

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

	public static ArrayList<Object> readTestCases()
	{
		ArrayList<Object> data = new ArrayList<Object>();
		try {
			initalizeObjects(ContantVariables.webSuite, "webApplicationSuite.xlsx",ContantVariables.webTestCaseSheetName);
			int rowNum = sheet.getLastRowNum();
			int colNum=0;
			for(int i=1;i<=rowNum;i++)
			{
				colNum = sheet.getRow(i).getLastCellNum();
				for(int j=0;j<=colNum-1;j++)
				{
					try {
						if(sheet.getRow(i).getCell(j).getStringCellValue().equalsIgnoreCase("Y")){
							System.out.println(sheet.getRow(i).getCell(0).getStringCellValue());
							data.add(sheet.getRow(i).getCell(0).getStringCellValue());
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
		ArrayList<Object> data = new ArrayList<Object>();
		try {
			ArrayList<Object> datatestCase = readTestCases();
			initalizeObjects(ContantVariables.webSuite, "webApplicationSuite.xlsx",ContantVariables.webKeywordsSheetName);
			int rowNum = sheet.getLastRowNum();
			int colNum=0;
			for(int i=1;i<=rowNum;i++)
			{
				colNum = sheet.getRow(i).getLastCellNum();
				for(int j=0;j<=colNum-1;j++)
				{
					try {
						for(Object testCase : datatestCase)
						{
							if(sheet.getRow(i).getCell(j).getStringCellValue().equalsIgnoreCase(testCase.toString())){
								data.add(sheet.getRow(i).getCell(2).getStringCellValue());
								System.out.println(sheet.getRow(i).getCell(2).getStringCellValue());
							}

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

	public static HashMap<Object,Object> readTestData()
	{
		HashMap<Object, Object> data = new HashMap<Object,Object>();
		try {
			ArrayList<Object> datatestCase = readTestCases();
			initalizeObjects(ContantVariables.webSuite, "webApplicationSuite.xlsx",ContantVariables.webTestDataSheetName);
			int rowNum = sheet.getLastRowNum();
			int colNum=0;
			boolean flag =false;

			for(int l=0;l<=datatestCase.size()-1;l++)
			{
				for(int i=l;i<=rowNum-1;i++)
				{
					colNum = sheet.getRow(i).getLastCellNum();
					for(int j=0;j<=colNum-1;j++)
					{
						try {
							for(int k=i;k<=rowNum-1;k++)
							{
								if(sheet.getRow(i).getCell(j).getStringCellValue().equalsIgnoreCase(datatestCase.get(l).toString())){
									int m=0;
									try {
										while(sheet.getRow(i+2+m).getCell(1).getCellType()!=Cell.CELL_TYPE_BLANK)
										{
											if(sheet.getRow(i+2+m).getCell(1).getStringCellValue().equalsIgnoreCase("Y"))
											{
												System.out.println(sheet.getRow(i+2+m).getCell(1).getStringCellValue());
												System.out.println(sheet.getRow(i+2+m).getCell(2).getStringCellValue());
												try {
													System.out.println(sheet.getRow(i+2+m).getCell(3).getStringCellValue());
													System.out.println(sheet.getRow(i+2+m).getCell(4).getStringCellValue());
												} catch (Exception e) {
													// TODO: handle exception
												} 
											}
											m++;
										}
									} catch (Exception e) {
										// TODO: handle exception
									}
									flag=true;
									break;
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						if(flag)
						{
							break;
						}
					}
					if(flag)
					{
						break;
					}
				}
				flag =false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//readExcelAllData(ContantVariables.webSuite,ContantVariables.webExcelFileName,ContantVariables.webTestCaseSheetName)		readTestCases();
		readKeywords();
//		readTestData();
	}

}
