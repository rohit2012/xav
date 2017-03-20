package com.csg.reports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class HTMLGenerate {

	private PrintWriter mOut;
	String desc = Reporting.getPath()+"//Reports//NewReport.html";

	public void generateHTMLReport()
	{
		try {
			mOut = new PrintWriter(new BufferedWriter(new FileWriter(new File(desc))));
		} catch (Exception e) {
			e.printStackTrace();
		}
		startHTML();
		endHTML();
		System.out.println("HTML REPORT GENERATE");
	}

	public void startHTML()
	{
		mOut.println("<!DOCTYPE html>");
		mOut.println("<html>");
		mOut.println("<head>");
		mOut.println("<title>TESTNG REPORT</title>");
		mOut.println("<style> table, th, td { border: 1px solid black; border-collapse: collapse; } </style>");
		mOut.println("</head>");
		mOut.println("<body style=background-color:pink;>");
		mOut.println("<h1 align=center> AUTOMATION REPORT </h1>");
		mOut.println("<table align=right style=width:50%>");
		mOut.println("<tr> <th>START EXECUTION TIME </th><td>11/24/2016 5:21 PM</td>  </tr>");
		mOut.println("<tr> <th>END EXECUTION TIME </th> <td>11/24/2016 6:01 PM</td></tr>");
		// Chart
		mOut.println("<script type=text/javascript>");
		mOut.println("</body>");
		mOut.println("</html>");
}

public void endHTML()
{
	mOut.println("");
	mOut.flush();
	mOut.close();
}

public static void main(String[] args) {
	HTMLGenerate obj = new HTMLGenerate();
	obj.generateHTMLReport();
}
}
