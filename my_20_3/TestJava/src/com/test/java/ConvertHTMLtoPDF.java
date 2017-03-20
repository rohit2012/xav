package com.test.java;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

public class ConvertHTMLtoPDF {
	
	public static final String HTML = "D:\\Folder Strucure\\Projects\\Charter Communication\\workspace\\TestJava\\File\\Testxhtml.html";
	public static final String DEST = "D:\\Folder Strucure\\Projects\\Charter Communication\\workspace\\TestJava\\File\\TestPDF1.pdf";

//	public static final String HTML = "D:\\Folder Strucure\\Projects\\Charter Communication\\workspace\\TestJava\\File\\ExtentReport.html";
//	public static final String DEST = "D:\\Folder Strucure\\Projects\\Charter Communication\\workspace\\TestJava\\File\\ExtentReportPDF.pdf";

	public void createPdf(String file) throws IOException, DocumentException {
		// step 1
		Document document = new Document();
		// step 2
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		
		// step 3
		document.open();
		// step 4
		XMLWorkerHelper.getInstance().parseXHtml(writer, document,
				new FileInputStream(HTML));


		// step 5
		document.close();
	}
	public static void main(String[] args) throws IOException, DocumentException {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		new ConvertHTMLtoPDF().createPdf(DEST);
		
	}
}

