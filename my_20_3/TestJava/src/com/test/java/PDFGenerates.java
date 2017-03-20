package com.test.java;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFGenerates {

	static String file = "D:\\Folder Strucure\\Projects\\Charter Communication\\workspace\\TestJava\\File\\Test4.pdf";

	public static void addImagePDF()
	{
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
			Image image1 = Image.getInstance("D:\\Folder Strucure\\Projects\\Charter Communication\\workspace\\TestJava\\File\\test.png");
			document.add(image1);
			document.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void addLink()
	{
		String file = "D:/Folder Strucure/Projects/Charter Communication/workspace/TestJava/File/Test4.pdf";
		String imagePath = "D:\\Folder Strucure\\Projects\\Charter Communication\\workspace\\TestJava\\File\\test.png";
		System.out.println(imagePath);
		Document document = new Document();

		try {
			PdfWriter.getInstance(document,
					new FileOutputStream(file));
			document.open();
			Anchor anchor =
					new Anchor("Jump down to next paragraph");
			anchor.setReference("#linkTarget");
			Paragraph paragraph = new Paragraph();
			paragraph.add(anchor);
			document.add(paragraph);

			Anchor anchorTarget =
					new Anchor("This is the target of the link above");
			anchor.setName("linkTarget");
			Paragraph targetParagraph = new Paragraph();
			targetParagraph.setSpacingBefore(5000);

			targetParagraph.add(anchorTarget);
			document.add(targetParagraph);

			Anchor anchorTarget1 =
					new Anchor("This is the target of the link above");
			anchor.setName("linkTarget");
			Paragraph targetParagraph1 = new Paragraph();
			targetParagraph1.setSpacingBefore(5000);

			targetParagraph1.add(anchorTarget1);
			document.add(targetParagraph1);
			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		//addImagePDF();
		addLink();
	}
}
