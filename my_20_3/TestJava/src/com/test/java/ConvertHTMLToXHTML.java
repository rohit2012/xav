package com.test.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.BufferUnderflowException;
import org.w3c.tidy.Tidy;

import com.itextpdf.text.log.SysoCounter;

public class ConvertHTMLToXHTML {

	public static void  getXHTMLFromHTML(String inputFile,
			String outputFile) throws Exception {

		File file = new File(inputFile);
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			fos = new FileOutputStream(outputFile);
			is = new FileInputStream(file);
			Tidy tidy = new Tidy(); 
			tidy.setXHTML(true); 
			tidy.parse(is, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					fos = null;
				}
				fos = null;
			}
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					is = null;
				}
				is = null;
			}
		}
	} 

	public static void main(String[] args) throws Exception {
		getXHTMLFromHTML("D:\\Folder Strucure\\Projects\\Charter Communication\\workspace\\TestJava\\File\\ExtentReport.html", "D:\\Folder Strucure\\Projects\\Charter Communication\\workspace\\TestJava\\File\\Test4.html");

	}
}
