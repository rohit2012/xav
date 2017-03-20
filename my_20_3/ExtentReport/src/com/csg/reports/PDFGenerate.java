package com.csg.reports;


import java.awt.Color;
import java.awt.Graphics2D;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

import com.itextpdf.awt.DefaultFontMapper;
import java.awt.geom.Rectangle2D;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

public class PDFGenerate{

	static final String dest = Reporting.getPath()+"//Reports//NewPDF.pdf";
	static Document document = null;
	static PdfWriter writer = null;
	static int passCount = 0,failCount =0,skipCount=0,totalCount=0;

	public PDFGenerate() {
		//reportData = new CustomReports();
	}

	public static Document pdfDocument (String des) throws FileNotFoundException, DocumentException
	{
		passCount = (int) CustomReports.testPassed.get("passedno");
		failCount = (int) CustomReports.testFailed.get("failedno");
		skipCount = (int) CustomReports.testSkiped.get("skipedno");
		totalCount = passCount+failCount+skipCount;
		document = new Document();
		writer = PdfWriter.getInstance(document, new FileOutputStream(des));
		document.open();
		return document;
	}


	public static void createPDF(String des) throws DocumentException, MalformedURLException, IOException
	{
		int temp=1,temp1=1,tempS=1;
		Document document = pdfDocument(des);

		// Add Header Text
		Font green = new Font(FontFamily.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK);
		Chunk greenText = new Chunk("LAST EXECUTION TEST REPORT ", green);
		greenText.setUnderline(0.5f, -1.5f);
		Paragraph p1 = new Paragraph(greenText);
		p1.setAlignment(Element.ALIGN_CENTER);
		p1.setSpacingAfter(20f);

		// Add Chart Time
		Font chart = new Font(FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK);
		Chunk chart1 = new Chunk("EXECUTION SUMMARY CHART ", chart);
		chart1.setUnderline(0.5f, -1.5f);
		Paragraph pchart = new Paragraph(chart1);
		pchart.setAlignment(Element.HEADER);
		pchart.setSpacingAfter(-25f);

		// Create Pie Chart for Pass and Fail test method

		long width = 500;
		long height = 250;
		PdfContentByte contentByte = writer.getDirectContent();
		PdfTemplate template = contentByte.createTemplate(width, height);
		Graphics2D graphics2d = template.createGraphics(width, height, new DefaultFontMapper());
		Rectangle2D rectangle2d = new Rectangle2D.Double(0,0,width,height);
		generatePieChart().draw(graphics2d, rectangle2d);
		graphics2d.dispose();
		Image chartImage = Image.getInstance(template);

		// Add Title for Timimg
		Font time = new Font(FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK);
		Chunk time1 = new Chunk("EXECUTION TIME ", time);
		time1.setUnderline(0.5f, -1.5f);
		Paragraph ptime = new Paragraph(time1);
		ptime.setAlignment(Element.HEADER);

		// Add Execution Details
		PdfPTable tableTimeExe = new PdfPTable(2);
		tableTimeExe.setWidthPercentage(90f);
		tableTimeExe.setSpacingBefore(10f);
		tableTimeExe.setSpacingAfter(10f);
		PdfPCell timestartExe = new PdfPCell(new Phrase("START EXECUTION TIME "));
		timestartExe.setColspan(1);
		timestartExe.setBackgroundColor(BaseColor.LIGHT_GRAY);
		tableTimeExe.addCell(timestartExe);
		PdfPCell timestartExeT = new PdfPCell(new Phrase(CustomReports.testTest.get("testStart").toString()));
		timestartExeT.setColspan(1);
		timestartExeT.setBackgroundColor(BaseColor.WHITE);
		tableTimeExe.addCell(timestartExeT);
		PdfPCell timesEndExe = new PdfPCell(new Phrase("END EXECUTION TIME "));
		timesEndExe.setColspan(1);
		timesEndExe.setBackgroundColor(BaseColor.LIGHT_GRAY);
		tableTimeExe.addCell(timesEndExe);
		PdfPCell timeendExeT = new PdfPCell(new Phrase(CustomReports.testTest.get("testEnd").toString()));
		timeendExeT.setColspan(1);
		timeendExeT.setBackgroundColor(BaseColor.WHITE);
		tableTimeExe.addCell(timeendExeT);

		// Add Title for SUMMARY
		Font summ = new Font(FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK);
		Chunk summary = new Chunk("SUMMARY RESULT ", summ);
		summary.setUnderline(0.5f, -1.5f);
		Paragraph psum = new Paragraph(summary);
		psum.setAlignment(Element.HEADER);


		// Add TEST CASE Execution Details

		PdfPTable tableExe = new PdfPTable(2);
		tableExe.setWidthPercentage(90f);
		tableExe.setSpacingBefore(10f);
		tableExe.setSpacingAfter(10f);
		PdfPCell nameExe = new PdfPCell(new Phrase("PASSED TEST CASES "));
		nameExe.setColspan(1);
		nameExe.setBackgroundColor(BaseColor.LIGHT_GRAY);
		tableExe.addCell(nameExe);
		PdfPCell pasCount = new PdfPCell(new Phrase(String.valueOf(passCount)));
		pasCount.setColspan(1);
		pasCount.setBackgroundColor(BaseColor.WHITE);
		tableExe.addCell(pasCount);
		PdfPCell failExe = new PdfPCell(new Phrase("FAILED TEST CASES "));
		failExe.setColspan(1);
		failExe.setBackgroundColor(BaseColor.LIGHT_GRAY);
		tableExe.addCell(failExe);
		PdfPCell failCont = new PdfPCell(new Phrase(String.valueOf(failCount)));
		pasCount.setColspan(1);
		pasCount.setBackgroundColor(BaseColor.WHITE);
		tableExe.addCell(failCont);
		PdfPCell skipExe = new PdfPCell(new Phrase("SKIPPED TEST CASES "));
		skipExe.setColspan(1);
		skipExe.setBackgroundColor(BaseColor.LIGHT_GRAY);
		tableExe.addCell(skipExe);
		PdfPCell skpCont = new PdfPCell(new Phrase(String.valueOf(skipCount)));
		skpCont.setColspan(1);
		skpCont.setBackgroundColor(BaseColor.WHITE);
		tableExe.addCell(skpCont);
		PdfPCell totalExe = new PdfPCell(new Phrase("TOTAL TEST CASES "));
		totalExe.setColspan(1);
		totalExe.setBackgroundColor(BaseColor.LIGHT_GRAY);
		tableExe.addCell(totalExe);
		PdfPCell tCont = new PdfPCell(new Phrase(String.valueOf(totalCount)));
		tCont.setColspan(1);
		tCont.setBackgroundColor(BaseColor.WHITE);
		tableExe.addCell(tCont);

		// Add Title for Pass Details
		Font det = new Font(FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK);
		Chunk details = new Chunk("DETAIL PASS RESULT ", det);
		details.setUnderline(0.5f, -1.5f);
		Paragraph pdet = new Paragraph(details);
		pdet.setAlignment(Element.HEADER);

		// Add Title for Fail Details
		Font detf = new Font(FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK);
		Chunk detailsf = new Chunk("DETAIL FAIL RESULT ", detf);
		detailsf.setUnderline(0.5f, -1.5f);
		Paragraph pdetf = new Paragraph(detailsf);
		pdetf.setAlignment(Element.HEADER);

		// Add Title for Skip Details
		Font dets = new Font(FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK);
		Chunk detailss = new Chunk("DETAIL SKIP RESULT ", dets);
		detailss.setUnderline(0.5f, -1.5f);
		Paragraph pdets = new Paragraph(detailss);
		pdets.setAlignment(Element.HEADER);

		// Create Tables for Pass, Skip and Fail tests method
		PdfPTable table =null;
		PdfPCell name = null;
		PdfPTable table1 =null;
		PdfPCell name1 = null;
		PdfPTable tableS =null;
		PdfPCell nameS = null;

		passCount = (int) CustomReports.testPassed.get("passedno");
		if(passCount>=1)
		{
			for(int i=1;i<=passCount;i++)
			{
				if(temp==1)
				{	
					float[] columnWidths = {7f, 7f,};
					table = new PdfPTable(columnWidths);
					table.setWidthPercentage(90f);
					table.setSpacingBefore(10f);
					table.setSpacingAfter(10f);
					name = new PdfPCell(new Phrase("TEST NAME"));
					name.setColspan(1);
					name.setBackgroundColor(BaseColor.GRAY);
					table.addCell(name);
					PdfPCell status = new PdfPCell(new Phrase("STATUS"));
					status.setColspan(1);
					status.setBackgroundColor(BaseColor.GRAY);
					table.addCell(status);
					temp++;
				}

				PdfPCell f2 = new PdfPCell(new Phrase(CustomReports.testPassed.get("passedname"+i).toString()));
				f2.setBackgroundColor(BaseColor.WHITE);
				table.addCell(f2);

				PdfPCell f3 = new PdfPCell(new Phrase("PASS"));
				f3.setBackgroundColor(BaseColor.GREEN);
				table.addCell(f3);
			} 
		}

		failCount = (int) CustomReports.testFailed.get("failedno");
		if(failCount>=1)
		{
			for(int i=1;i<=failCount;i++)
			{
				if(temp1==1)
				{
					float[] columnWidths = {3f, 1.5f, 9f};
					table1 = new PdfPTable(columnWidths);
					table1.setWidthPercentage(90f);
					table1.setSpacingBefore(10f);
					table1.setSpacingAfter(10f);
					name1 = new PdfPCell(new Phrase("TEST NAME")); 
					name1.setColspan(1);
					name1.setBackgroundColor(BaseColor.GRAY);
					table1.addCell(name1);
					PdfPCell status1 = new PdfPCell(new Phrase("STATUS"));
					status1.setColspan(1);
					status1.setBackgroundColor(BaseColor.GRAY);
					table1.addCell(status1);
					PdfPCell status2 = new PdfPCell(new Phrase("EXCEPTION MESSAGE"));
					status2.setColspan(1);
					status2.setBackgroundColor(BaseColor.GRAY);
					status2.setHorizontalAlignment(Element.ALIGN_CENTER);
					table1.addCell(status2);
					temp1++;
				}

				PdfPCell f4 = new PdfPCell(new Phrase(CustomReports.testFailed.get("failedname"+i).toString()));
				f4.setBackgroundColor(BaseColor.WHITE);
				f4.setColspan(1);
				f4.setBorder(20);
				f4.setBottom(7f);
				table1.addCell(f4);

				PdfPCell f5 = new PdfPCell(new Phrase("FAIL"));
				f5.setBackgroundColor(BaseColor.RED);
				f5.setColspan(1);
				f5.setBorder(20);
				f5.setBottom(7f);
				table1.addCell(f5);

				PdfPCell f6 = new PdfPCell(new Phrase(CustomReports.testFailed.get("failederror"+i).toString()));
				f6.setColspan(1);
				f6.setBackgroundColor(BaseColor.LIGHT_GRAY);
				f6.setBorder(20);
				f6.setBottom(7f);
				table1.addCell(f6);

				String IMG = Reporting.getPath()+"\\Reports\\Screenshot\\"+CustomReports.testFailed.get("failedname"+i).toString()+".jpg";
				Image img = Image.getInstance(IMG);
				PdfPCell f7 = new PdfPCell(img, true);
				f7.setBorder(20);
				f7.setBorderWidth(2f);
				f7.setBottom(7f);
				f7.setColspan(3);
				table1.addCell(f7);
			}	
		}			

		skipCount = (int) CustomReports.testSkiped.get("skipedno");
		if(skipCount>=1)
		{
			for(int i=1;i<=skipCount;i++)
			{
				if(tempS==1)
				{	
					float[] columnWidths = {3f, 1.5f, 9f};
					tableS = new PdfPTable(columnWidths);
					tableS.setWidthPercentage(90f);
					tableS.setSpacingBefore(10f);
					tableS.setSpacingAfter(10f);
					nameS = new PdfPCell(new Phrase("TEST NAME"));
					nameS.setColspan(1);
					nameS.setBackgroundColor(BaseColor.GRAY);
					tableS.addCell(nameS);
					PdfPCell status = new PdfPCell(new Phrase("STATUS"));
					status.setColspan(1);
					status.setBackgroundColor(BaseColor.GRAY);
					tableS.addCell(status);
					PdfPCell status2 = new PdfPCell(new Phrase("SKIP MESSAGE"));
					status2.setColspan(1);
					status2.setBackgroundColor(BaseColor.GRAY);
					status2.setHorizontalAlignment(Element.ALIGN_CENTER);
					tableS.addCell(status2);
					temp++;
				}

				PdfPCell f2 = new PdfPCell(new Phrase(CustomReports.testSkiped.get("skippedname"+i).toString()));
				f2.setBackgroundColor(BaseColor.WHITE);
				tableS.addCell(f2);

				PdfPCell f3 = new PdfPCell(new Phrase("SKIP"));
				f3.setBackgroundColor(BaseColor.YELLOW);
				tableS.addCell(f3);
				PdfPCell f6 = new PdfPCell(new Phrase(CustomReports.testSkiped.get("skipError"+i).toString()));
				f6.setColspan(1);
				f6.setBackgroundColor(BaseColor.LIGHT_GRAY);
				tableS.addCell(f6);
			} 
		}
		// Add Header
		document.add(p1);

		// Add Execution time Text and Table
		document.add(ptime);
		document.add(tableTimeExe);

		// Add Chart Text and Pie Chart
		document.add(pchart);
		document.add(chartImage);

		// Add Summary Text and Table
		document.add(psum);
		document.add(tableExe);

		// Add details result table 
		if(table!=null)
		{
			document.add(pdet);
			document.add(table);
		} 
		if(tableS!=null)
		{
			document.add(pdets);
			document.add(tableS);
		} 
		if(table1!=null)
		{
			document.add(pdetf);
			document.add(table1);
		} 
		document.close();
	}

	public static JFreeChart generatePieChart()
	{
		DefaultPieDataset dataSet = new DefaultPieDataset();
		if(passCount>=1)
		{
			dataSet.setValue("Passed : "+passCount, passCount);
		}
		if(failCount>=1)
		{
			dataSet.setValue("Failed : "+failCount, failCount);
		}
		if(skipCount>=1)
		{
			dataSet.setValue("Skiped : "+skipCount, skipCount);
		}

		JFreeChart chart = ChartFactory.createPieChart(" ", dataSet,true,true,false);
		PiePlot ColorConfigurator = (PiePlot)chart.getPlot();                
		ColorConfigurator.setSectionPaint("Passed : "+passCount, Color.GREEN);
		ColorConfigurator.setSectionPaint("Failed : "+failCount, Color.RED);
		ColorConfigurator.setSectionPaint("Skiped : "+skipCount, Color.YELLOW);
		return chart;
	}



	public static void main(String[] args) throws DocumentException, MalformedURLException, IOException {
		PDFGenerate obj = new PDFGenerate();
		obj.createPDF(dest);

	}



}
