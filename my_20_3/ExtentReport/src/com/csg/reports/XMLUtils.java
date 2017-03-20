package com.csg.reports;
import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.itextpdf.text.log.SysoCounter;

public class XMLUtils {

	static String xmlFile = Reporting.getPath()+"\\test-output\\testng-results.xml";
	HashMap<Object, Object> testResult;
	HashMap<Object, Object> testSuite;
	HashMap<Object, Object> testTest;
	HashMap<Object, Object> testClass;
	HashMap<Object, Object> testMethods;
	HashMap<Object, Object> testMethodsName;
	
	public XMLUtils()
	{  
		testResult = new HashMap<Object,Object>();
		testSuite = new HashMap<Object,Object>();
		testClass = new HashMap<Object,Object>();
		testMethods = new HashMap<Object,Object>();
		testTest = new HashMap<Object,Object>();
		testMethodsName = new HashMap<Object,Object>();
	}

	public void readXML()
	{
		try{
			int tempMethod = 1;
			System.out.println(xmlFile);
			File inputFile = new File(xmlFile);
			DocumentBuilderFactory dbFactory 
			= DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList0 = doc.getElementsByTagName("testng-results");
			System.out.println("------------RESULTS----------------");
			for (int tempRes = 0; tempRes < nList0.getLength(); tempRes++) {
				Node nNode = nList0.item(tempRes);

				System.out.println("\nCurrent Element :" 
						+ nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					testResult.put("total"+tempRes, eElement.getAttribute("total"));
					System.out.println("Total no : " 
							+testResult.get("total"+tempRes));
					testResult.put("passed"+tempRes, eElement.getAttribute("passed"));
					System.out.println("Passed :  " 
							+testResult.get("passed"+tempRes));
					testResult.put("failed"+tempRes, eElement.getAttribute("failed"));
					System.out.println("Failed : " 
							+testResult.get("failed"+tempRes));
					testResult.put("skipped"+tempRes, eElement.getAttribute("skipped"));
					System.out.println("Skipped : " 
							+testResult.get("skipped"+tempRes));
				}

				Node suiteType = doc.getElementsByTagName("testng-results").item(tempRes);
				NodeList nList1 = ((Element)suiteType).getElementsByTagName("suite");

				//NodeList nList1 = doc.getElementsByTagName("suite");
				System.out.println("---------------SUITE-------------");
				for (int tempSuite = 0; tempSuite < nList1.getLength(); tempSuite++) {
					nNode = nList1.item(tempSuite);
					System.out.println("\nCurrent Element :" 
							+ nNode.getNodeName());
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						testSuite.put("name"+tempSuite, eElement.getAttribute("name"));
						System.out.println("Suite Name : " 
								+testSuite.get("name"+tempSuite));
						testSuite.put("duration-ms"+tempSuite, eElement.getAttribute("duration-ms"));
						System.out.println("Duration : " 
								+testSuite.get("duration-ms"+tempSuite));
						testSuite.put("started-at"+tempSuite, eElement.getAttribute("started-at"));
						System.out.println("Started-at : " 
								+testSuite.get("started-at"+tempSuite));
						testSuite.put("finished-at"+tempSuite, eElement.getAttribute("finished-at"));
						System.out.println("Finished-at : " 
								+testSuite.get("finished-at"+tempSuite));
					}
					NodeList nList2 =null;
					try {
						Node testType = doc.getElementsByTagName("tests").item(tempRes);
						nList2 = ((Element)testType).getElementsByTagName("test");
					} catch (Exception e) {
						nList2 = doc.getElementsByTagName("test");
					}

					System.out.println("------------TEST : "+nList2.getLength()+"----------------");
					for (int tempTest = 0; tempTest < nList2.getLength(); tempTest++) {
						nNode = nList2.item(tempTest);
						System.out.println("\nCurrent Element :" 
								+ nNode.getNodeName());
						if (nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement = (Element) nNode;
							testTest.put("name"+tempTest, eElement.getAttribute("name"));
							System.out.println("Test Name : " 
									+testTest.get("name"+tempTest));
							testTest.put("duration-ms"+tempTest, eElement.getAttribute("duration-ms"));
							System.out.println("Duration : " 
									+testTest.get("duration-ms"+tempTest));
							testTest.put("started-at"+tempTest, eElement.getAttribute("started-at"));
							System.out.println("Started-at : " 
									+testTest.get("started-at"+tempTest));
							testTest.put("finished-at"+tempTest, eElement.getAttribute("finished-at"));
							System.out.println("Finished-at : " 
									+testTest.get("finished-at"+tempTest));

						}

						NodeList nList3 = null;
						Node classType = null;
						try {
							classType = doc.getElementsByTagName("classes").item(tempTest);
							nList3 = ((Element)classType).getElementsByTagName("class");
						} catch (Exception e) {
							nList3 = doc.getElementsByTagName("class");
						}


						//	NodeList nList3 = doc.getElementsByTagName("class");
						System.out.println("------------CLASS + "+nList3.getLength()+"----------------");
						for (int tempClass = 0; tempClass < nList3.getLength(); tempClass++) {
							try {
								classType = doc.getElementsByTagName("classes").item(tempTest);
								nList3 = ((Element)classType).getElementsByTagName("class");
							} catch (Exception e) {
								nList3 = doc.getElementsByTagName("class");
							}
							nNode = nList3.item(tempClass);
							System.out.println("\nCurrent Element :" 
									+ nNode.getNodeName());
							if (nNode.getNodeType() == Node.ELEMENT_NODE) {
								Element eElement = (Element) nNode;
								testClass.put("name"+tempClass, eElement.getAttribute("name"));
								System.out.println("Class Name : " 
										+ testClass.get("name"+tempClass));
							}


							Node portType = doc.getElementsByTagName("class").item(tempClass);
							NodeList nList4 = ((Element)portType).getElementsByTagName("test-method");
							//	NodeList nList4 = doc.getElementsByTagName("test-method");

							System.out.println("-------------TESTMETHOD---------------");
							System.out.println(nList4.getLength());
							for (int temp = 0; temp < nList4.getLength(); temp++) {
								nNode = nList4.item(temp);
								if (nNode.getNodeType() == Node.ELEMENT_NODE) {
									Element eElement = (Element) nNode;
									if(!eElement.hasAttribute("is-config")){
										testMethods.put("status"+tempMethod, eElement.getAttribute("status"));
										System.out.println("Status : "+ testMethods.get("status"+tempMethod));
										if(eElement.getAttribute("status").equalsIgnoreCase("Fail"))
										{
											//testMethods.put("full-stacktrace"+tempMethod, eElement.getElementsByTagName("full-stacktrace").item(0).getTextContent());
											testMethods.put("full-stacktrace"+tempMethod, eElement.getElementsByTagName("message").item(0).getTextContent());
											System.out.println("EXCEPTION : " + testMethods.get("full-stacktrace"+tempMethod));
										}
										testMethodsName.put("name"+tempMethod, eElement.getAttribute("name"));
										System.out.println("TestCase Name : " + testMethodsName.get("name"+tempMethod));
										testMethods.put("started-at"+tempMethod, eElement.getAttribute("started-at"));
										System.out.println("Started-at : " + testMethods.get("started-at"+tempMethod));
										testMethods.put("finished-at"+tempMethod, eElement.getAttribute("finished-at"));
										System.out.println("Finished-at : " + testMethods.get("finished-at"+tempMethod));
										testMethods.put("duration-ms"+tempMethod, eElement.getAttribute("duration-ms"));
										System.out.println("Duration : " + testMethods.get("duration-ms"+tempMethod));
										System.out.println("-------------TESTMETHOD "+testMethods.get("name"+tempMethod)+" : "+tempMethod+" ---------------");
										tempMethod ++;
									}
									
								}
							}
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		XMLUtils obj = new XMLUtils();
		obj.readXML();
		//System.out.println(obj.testMethods.get("status2"));
	}
}
