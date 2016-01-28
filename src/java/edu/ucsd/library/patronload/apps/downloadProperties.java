package edu.ucsd.library.patronload.apps;
   
import java.io.IOException;  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.*;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import java.io.FileNotFoundException;  
import java.io.*;  
import java.util.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.SortedMap;

public class downloadProperties extends HttpServlet {  
   
/** 
 * The doGet method of the servlet. <br> 
 * 
 * This method is called when a form has its tag value method equals to get. 
 *  
 * @param request the request send by the client to the server 
 * @param response the response send by the server to the client 
 * @throws ServletException if an error occurred 
 * @throws IOException if an error occurred 
 */  
public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FileNotFoundException  {  
	 String fileName = request.getParameter("fileName");
	 response.setContentType("text/plain"); 
	 response.setHeader("Content-Disposition","attachment;filename=" + fileName);
	 ServletContext ctx = getServletContext();
	 String marcFilesDir = ctx.getInitParameter("marcFilePath");
	 boolean readPatronFile = false;
	 
	 BufferedReader is = new BufferedReader(new FileReader(marcFilesDir + fileName));
	 is.readLine();
	 is.readLine();
	 is.readLine();
	 if(fileName.equals("patron_load.properties")) {
		 readPatronFile = true;
		 is.readLine();
	 }
	 if(fileName.equals("emp_affiliations.properties"))
		 is.readLine();
	 String lineIn;
	 ServletOutputStream os = response.getOutputStream();  
	 Map<Integer, String> propMap = new HashMap<Integer, String>();
	 Map<String, String> patronMap = new HashMap<String, String>();
	 
	 if (readPatronFile) {
		 String inTmp = "";
		 while((lineIn = is.readLine()) != null)
		 {
			 inTmp = lineIn + "\n";
			 if(!lineIn.contains("db") && !lineIn.contains("expire"))
				 os.write(inTmp.getBytes());
		 }		 
	 } else {
		 while((lineIn = is.readLine()) != null)
		 {
			 String[] temp = lineIn.split("=");
			 if(temp.length == 2)
				 propMap.put(Integer.parseInt(temp[0]),temp[1]);
			 else
				 propMap.put(Integer.parseInt(temp[0]), "");
		 }
		 if(fileName.equals("emp_affiliations.properties"))
			 os.write("##Employee Download - Affiliation code\n#[department code] = [library code]\n".getBytes());
		 else
			 os.write("##Staff Group\n".getBytes());
		 Map<Integer, String> sortedMap = new TreeMap(propMap);
		 Iterator it = sortedMap.entrySet().iterator();
		 while(it.hasNext())
		 {
			 Map.Entry pairs = (Map.Entry)it.next();
			 String temp2 = pairs.getKey().toString() + "=" + pairs.getValue() + "\n";
			 os.write(temp2.getBytes());
		 }
	 }
	 os.flush();  
	 os.close(); 
	}  
	

}  


