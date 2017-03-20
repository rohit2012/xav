package com.xavient.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excelfont {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/* Create Workbook and Worksheet */
		FileInputStream fis = new FileInputStream(new File("D:\\RTF_Output.xlsx"));
        XSSFWorkbook my_workbook = new XSSFWorkbook(fis);
        XSSFSheet my_sheet = my_workbook.getSheet("Rich Text Format Example");
        /* We define a rich Text String, that we split into three parts by using applyFont method */
        XSSFFont my_font_1=my_workbook.createFont();
        XSSFFont my_font_2=my_workbook.createFont();
        XSSFFont my_font_3=my_workbook.createFont();
        
        //font for FAIL String
        my_font_1.setColor(Font.COLOR_RED);
        my_font_1.setBoldweight(Font.BOLDWEIGHT_BOLD);
        my_font_1.setItalic(true);
        
        //font for Pass string
        my_font_2.setColor(Font.COLOR_NORMAL);
        Row myrow = my_sheet.getRow(0);
        Cell mycell = myrow.getCell(0);
        
        String mystring = mycell.getStringCellValue(); 
       
        XSSFRichTextString my_rich_text_string = new XSSFRichTextString(mystring );
       
        my_rich_text_string.applyFont(my_font_1);

        
        mycell.setCellValue(my_rich_text_string);
        /* Attach these links to cells */
       // Row row = my_sheet.createRow(0);                
       // Cell cell = row.createCell(0);
       // cell.setCellValue(my_rich_text_string);         
        
        /* Write changes to the workbook */
        FileOutputStream out = new FileOutputStream(new File("D:\\RTF_Output.xlsx"));
    
        my_workbook.write(out);
        out.close();

	}

}
