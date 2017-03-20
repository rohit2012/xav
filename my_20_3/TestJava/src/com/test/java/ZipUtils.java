package com.test.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils
{

private List<String> fileList;
private List<String> fileList1;
private static final String OUTPUT_ZIP_FILE = "D:\\Folder Strucure\\Projects\\Charter Communication\\workspace\\TestJava\\File.zip";
private static final String SOURCE_FOLDER = "D:\\Folder Strucure\\Projects\\Charter Communication\\workspace\\TestJava\\File"; // SourceFolder path
private static final String SOURCE_FOLDER1 = "D:\\Folder Strucure\\Projects\\Charter Communication\\workspace\\TestJava\\Screenshot";

public ZipUtils()
{
   fileList = new ArrayList<String>();
   fileList1 = new ArrayList<String>();
}

public static void main(String[] args)
{
   ZipUtils appZip = new ZipUtils();
   appZip.generateFileList(new File(SOURCE_FOLDER));
   appZip.generateFileList1(new File(SOURCE_FOLDER1));
   appZip.zipIt(OUTPUT_ZIP_FILE);
}

public void zipIt(String zipFile)
{
   byte[] buffer = new byte[1024];
   String source = "";
   String source1 = "";
   FileOutputStream fos = null;
   ZipOutputStream zos = null;
   try
   {
      try
      {
         source = SOURCE_FOLDER.substring(SOURCE_FOLDER.lastIndexOf("\\") + 1, SOURCE_FOLDER.length());
         source1 = SOURCE_FOLDER1.substring(SOURCE_FOLDER1.lastIndexOf("\\") + 1, SOURCE_FOLDER1.length());
      }
     catch (Exception e)
     {
        source = SOURCE_FOLDER;
        source1 = SOURCE_FOLDER1;
     }
     fos = new FileOutputStream(zipFile);
     zos = new ZipOutputStream(fos);

     System.out.println("Output to Zip : " + zipFile);
     FileInputStream in = null;
     FileInputStream in1 = null;
     for (String file : this.fileList)
     {
        System.out.println("File Added : " + file);
        ZipEntry ze = new ZipEntry(source + File.separator + file);
        zos.putNextEntry(ze);
        try
        {
           in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
           int len;
           while ((len = in.read(buffer)) > 0)
           {
              zos.write(buffer, 0, len);
           }
        }
        finally
        {
           in.close();
        }
     }
     
     
     
     for (String file : this.fileList1)
     {
        System.out.println("File Added : " + file);
        ZipEntry ze = new ZipEntry(source + File.separator + file);
        zos.putNextEntry(ze);
        try
        {
           in1 = new FileInputStream(SOURCE_FOLDER1 + File.separator + file);
           int len1;
           while ((len1 = in1.read(buffer)) > 0)
           {
              zos.write(buffer, 0, len1);
           }
        }
        finally
        {
           in.close();
        }
     }

     zos.closeEntry();
     System.out.println("Folder successfully compressed");

  }
  catch (IOException ex)
  {
     ex.printStackTrace();
  }
  finally
  {
     try
     {
        zos.close();
     }
     catch (IOException e)
     {
        e.printStackTrace();
     }
  }
}

public void generateFileList(File node)
{

  // add file only
  if (node.isFile())
  {
     fileList.add(generateZipEntry(node.toString()));

  }

  if (node.isDirectory())
  {
     String[] subNote = node.list();
     for (String filename : subNote)
     {
        generateFileList(new File(node, filename));
     }
  }
}

private String generateZipEntry(String file)
{
   return file.substring(SOURCE_FOLDER.length() + 1, file.length());
}

public void generateFileList1(File node)
{

  // add file only
  if (node.isFile())
  {
     fileList1.add(generateZipEntry1(node.toString()));

  }

  if (node.isDirectory())
  {
     String[] subNote = node.list();
     for (String filename : subNote)
     {
    	 generateFileList1(new File(node, filename));
     }
  }
}

private String generateZipEntry1(String file)
{
   return file.substring(SOURCE_FOLDER1.length() + 1, file.length());
}
}    