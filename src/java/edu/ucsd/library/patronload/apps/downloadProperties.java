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
import edu.ucsd.library.util.FileUtils;

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
	 BufferedReader is = new BufferedReader(new FileReader(marcFilesDir + fileName));
	 is.readLine();
	 is.readLine();
	 is.readLine();
	 if(fileName.equals("emp_affiliations.properties"))
		 is.readLine();
	 String lineIn;
	 ServletOutputStream os = response.getOutputStream();  	 
	 Map<String,String> sortedMap = null;	 
     if(fileName.equals("patron_load.properties"))
     {
    	 	Map<String, String> propPatronMap = new HashMap<String, String>();
    	 	Properties props = null;
        try {
            	props = FileUtils.loadProperties(marcFilesDir + fileName);
        } catch (IOException ioe) {
        		System.err.println("IOException:"+ioe);
        } 
    	 	Enumeration e = props.propertyNames();
	    for (; e.hasMoreElements(); ) {
	        String propName = (String)e.nextElement();
	        String propValue = (String)props.get(propName);
	        if(propValue.length() < 5 && !propName.contains("quartercode"))
	        		propPatronMap.put(propName, propValue);
	    }    
	    sortedMap = new TreeMap(propPatronMap);
     } else {
    	 	Map<Integer, String> propMap = new HashMap<Integer, String>();
		while((lineIn = is.readLine()) != null)
		{
			String[] temp = lineIn.split("=");
			if(temp.length == 2)
				propMap.put(Integer.parseInt(temp[0]),temp[1]);
			else
				propMap.put(Integer.parseInt(temp[0]), "");
		 }
		 sortedMap = new TreeMap(propMap);
		 if(fileName.equals("emp_affiliations.properties"))
			 os.write("##Employee Download - Affiliation code\n#[department code] = [library code]\n".getBytes());
		 else
			 os.write("##Staff Group\n".getBytes());
     }
		 
	 Iterator it = sortedMap.entrySet().iterator();
	 while(it.hasNext())
	 {
		 Map.Entry pairs = (Map.Entry)it.next();
		 if(pairs.getKey().toString().length() > 0) {
			String temp2 = pairs.getKey().toString() + "=" + pairs.getValue() + "\n";
		 	os.write(temp2.getBytes());
		 }
	 }
	 
	 os.flush();  
	 os.close(); 
	}  
	

}  


