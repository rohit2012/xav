package com.xavient.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFConditionalFormattingRule;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFFontFormatting;
import org.apache.poi.xssf.usermodel.XSSFPatternFormatting;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSheetConditionalFormatting;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColorScale;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfType;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType;

public class ConditionalFomatting {
	
	
	

	
	
	
		public static void main(String[] args) throws Exception{
        
			
			/* Create Workbook and Worksheet XLSX Format */
			 FileInputStream input_document = new FileInputStream(new File("C:\\Users\\fansari\\Documents\\inputfile.xlsx"));    
	            XSSFWorkbook my_workbook = new XSSFWorkbook(input_document); 
	            XSSFSheet my_sheet = my_workbook.getSheetAt(0); 
	            
           
            /* We define a rich Text String, that we split into three parts by using applyFont method */
            XSSFFont my_font_1=my_workbook.createFont();
            XSSFFont my_font_2=my_workbook.createFont();
            XSSFFont my_font_3=my_workbook.createFont();
            XSSFFont my_font_4=my_workbook.createFont();
            XSSFFont my_font_5=my_workbook.createFont();
            my_font_1.setColor(Font.COLOR_RED);
            my_font_2.setBoldweight(Font.BOLDWEIGHT_BOLD);
            my_font_3.setItalic(true);
            
            XSSFRichTextString my_rich_text_string = new XSSFRichTextString( "RichTextFormat" );
            my_rich_text_string.applyFont( 0, 4, my_font_1 );
            my_rich_text_string.applyFont( 5, 8, my_font_2);
            my_rich_text_string.applyFont( 8, 13, my_font_3 );
            
            /* Attach these links to cells */
            Row row = my_sheet.createRow(0);                
            Cell cell = row.createCell(0);
            cell.setCellValue(my_rich_text_string); 
            
            /* Write changes to the workbook */
            FileOutputStream out = new FileOutputStream(new File("C:\\Users\\fansari\\Documents\\inputfile.xlsx"));
            my_workbook.write(out);
            out.close();
			
			
			
			
		}
		
}
