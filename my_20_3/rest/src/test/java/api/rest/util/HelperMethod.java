package api.rest.util;

import static com.jayway.restassured.RestAssured.given;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import org.muthu.Verify;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

@Listeners({org.muthu.TestMethodListener.class})
public class HelperMethod {

	public static SoftAssert softAssert = new SoftAssert();
	String baseurl = Properties_Reader.readProperty("URL");
	String contentType= Properties_Reader.readProperty("ContentType");

	public String readRequest(String jasonname)
	{
		String h= null;
		try {
			Reader reader = new FileReader(System.getProperty("user.dir")+"\\Requests\\"+jasonname+".json");
			BufferedReader bufferReader = new BufferedReader(reader);
			int num=0;
			char ch = 0;
			String s=null;
			StringBuffer sb = new StringBuffer();
			while((num=bufferReader.read()) != -1)
			{	
				ch=(char)num;
				s=String.valueOf(ch);
				sb.append(s);
			}
			h= sb.toString();
			bufferReader.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return h;
	}

	public Response getResponse(String req, String url,String command)
	{
		RestAssured.baseURI = baseurl+url;
		Response response = null;
		if(command.equalsIgnoreCase("post"))
		{
			response=  given().
					contentType(contentType).
					body(req)
					.post();
		} else if(command.equalsIgnoreCase("get"))
		{
			response=  given().
					contentType(contentType).
					body(req)
					.get();
		}
		System.out.println(response.asString());
		return response;
	}

	public Object getjsonData(String fileName,String url,Response response)
	{
		//String req = readRequest(fileName);
		//String res = getResponse(req,url,command).asString();
		String res = response.asString();
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(res);
		return document;
	}

	public int getStatus(String url,String fileName,String command)
	{
		String req = readRequest(fileName);
		return getResponse(req,url,command).getStatusCode();
	}

	public Object getrequestData(String fileName,String url,Response res,String xpath)
	{
		return JsonPath.read(getjsonData(fileName,url,res), xpath).toString();
	}

	public int getrequestDataLength(String fileName,String url,Response res,String xpath)
	{
		return Integer.parseInt(JsonPath.read(getjsonData(fileName,url,res), xpath+".length()").toString().replace("[", "").replace("]", ""));
	}


	public void assertSingleString(String url,String fileName,Response res,String xpath,String value)
	{
		String s = JsonPath.read(getjsonData(fileName,url,res), xpath).toString().replace("[\"", "").replace("\"]", "");
		boolean f = false;
		if(s.equals("N\\/A"))
		{
			value = "N\\/A";
		}
		if(s.equals(value))
		{
			System.out.println(s+" is equals to "+value);
			f=true;
		}else
		{
			f=false;
		}
		Assert.assertEquals(f,true,value+" is not matching");
	}


	public void assertListWithKey(String url,String fileName,Response res,String xpath,String key,String value)
	{
		String s = getrequestData(fileName,url,res, xpath).toString().replace("[{", "").replace("}]","").replace("[", "").replace("]", "");
		String[] sp = s.split(",");
		HashMap<Object, Object> dataMap = new HashMap<Object, Object>();
		ArrayList<Object> arr = new ArrayList<Object>();
		boolean f = false;
		for(int i =0;i<=sp.length-1;i++)
		{
			try {
				arr.add(sp[i]);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		for(Object obj : arr)
		{
			sp=obj.toString().split(":");
			dataMap.put(sp[0].replace("\"", "").replace("{", "").replace("}", ""), sp[1].replace("\"", "").replace("{", "").replace("}", ""));
		}
		for(Object sw : dataMap.keySet())
		{
			if(sw.equals(key))
			{
				if(dataMap.get(sw).equals("N\\/A"))
				{
					value="N\\/A";
				}
				if(dataMap.get(sw).equals(value.trim()))
				{
					f=true;
				} else
				{
					f=false;
				}
			}
		}
		Assert.assertEquals(f,true,key+" is not matching with "+value);
	}


	public void assertList(String url,String fileName,Response res,String xpath,Boolean keyPrsent,String key,String value)
	{
		value = value.toLowerCase().trim();
		if(!keyPrsent)
		{
			String s = getrequestData(fileName,url,res,xpath).toString().replace("[\"", "").replace("\"]", "").toLowerCase();
			boolean f = false;
			if(s.equals("N\\/A"))
			{
				value = "N\\/A";
			}
			if(s.equals(value))
			{
				System.out.println(s+" is equals to "+value);
				f=true;
			}else
			{
				f=false;
			}
			//softAssert.assertEquals(f,true,value+" is not matching");
			Verify.verifyEquals(f,true,value+" is not matching");
		} else if(keyPrsent)
		{
			String s= "";
			s = getrequestData(fileName,url,res, xpath).toString().replace("[{", "").replace("}]","").replace("[", "").replace("]", "").replace("{", "").replace("}", "");
			String[] sp = null;
			try {
				sp = s.split(",");
			} catch (Exception e) {
			}
			ListMultimap<String, String> dataMap = ArrayListMultimap.create();
			ArrayList<Object> arr = new ArrayList<Object>();
			boolean f = false;
			for(int i =0;i<=sp.length-1;i++)
			{
				try {
					arr.add(sp[i].toString());
				} catch (Exception e) {
				}
			}
			for(int l=0;l<=arr.size()-1;l++)
			{
				try {
					sp=arr.get(l).toString().split(":");
					// Convert response value into lower case
					dataMap.put(sp[0].replace("\"", "").replace("{", "").replace("}", ""), sp[1].toLowerCase().replace("\"", "").replace("{", "").replace("}", ""));
				} catch (Exception e) {
				}
			}
			if(value.equals("N/A"))
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				value = "N\\/A";
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for(Object sw : dataMap.keySet())
			{
				if(sw.equals(key))
				{
					value = value.replaceAll("/", "\\\\/");
						
					//System.out.println("ACTUAL : "+dataMap.get(sw.toString())+" : EXPECTED : "+value);
					
					f = dataMap.get(sw.toString()).contains(value);
					break;
				}

			}
			Verify.verifyEquals(f,true,value+" is not matching");
		}
	}
	
	public void assertListWitIndex(String url,String fileName,Response res,String xpath,Boolean keyPrsent,String pos,String key,String value)
	{
		String s= "";
		s = getrequestData(fileName,url,res, xpath).toString().replace("[{", "").replace("}]","").replace("[", "").replace("]", "").replace("{", "").replace("}", "");
		if(s.length()==0)
		{
			for(int m=1;m<=1;m++)
			{
				s = getrequestData(fileName,url,res, xpath).toString().replace("[{", "").replace("}]","").replace("[", "").replace("]", "").replace("{", "").replace("}", "");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(s.length()!=0)
				{
					break;
				}
			}
			if(s.length()==0)
			{
				System.out.println("Response is not present after 3 attempt : "+s);
			}
		}
		String[] sp = null;
		try {
			sp = s.split(",");
		} catch (Exception e) {
			// TODO: handle exception
		}
		HashMap<Object, Object> dataMap = new HashMap<Object, Object>();
		ArrayList<Object> arr = new ArrayList<Object>();
		boolean f = false;
		for(int i =0;i<=sp.length-1;i++)
		{
			try {
				arr.add(sp[i].toString());
			} catch (Exception e) {
			}
		}
		for(int l=0;l<=arr.size()-1;l++)
		{
			try {
				sp=arr.get(l).toString().split(":");
				dataMap.put(sp[0].replace("\"", "").replace("{", "").replace("}", "")+"$"+l, sp[1].replace("\"", "").replace("{", "").replace("}", "")+"$"+l);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		String val = "";
		int k=1;
		Object keyV;
		for(Object sw : dataMap.keySet())
		{
			if(pos.equalsIgnoreCase("Not"))
			{
				keyV = sw;
				sw = sw.toString().split("\\$")[0];
				if(sw.equals(key))
				{
					sw = keyV;
					val = dataMap.get(sw).toString().split("\\$")[0].trim();
					if(val.equals("N\\/A"))
					{
						value="N\\/A";
					}
					if(val.equals(value.trim()))
					{
						System.out.println(val+" is equals to "+value);
						f=true;
					} else
					{
						f=false;
					}
				}
			}else if(!pos.equals(""))
			{
				if(k==1)
				{
					key = key+"$"+pos;
					k++;
				}
				if(sw.equals(key))
				{
					val = dataMap.get(sw).toString().split("\\$")[0].trim();
					if(val.equals("N\\/A"))
					{
						value="N\\/A";
					}
					if(val.equals(value.trim()))
					{
						System.out.println(val+" is equals to "+value);
						f=true;
					} else
					{
						f=false;
					}
				}
			}
		}
		softAssert.assertEquals(f,true,key+" is not matching with "+value);
	}
}



