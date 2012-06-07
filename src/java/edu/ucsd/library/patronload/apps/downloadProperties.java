package edu.ucsd.library.patronload.apps;
   
import java.io.IOException;  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.*;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import java.io.FileNotFoundException;  
import java.io.*;  
   
   
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
public void doGet(HttpServletRequest request, HttpServletResponse response)  
throws ServletException, IOException, FileNotFoundException  {  
 String fileName = request.getParameter("fileName");
 response.setContentType("text/plain"); 
 response.setHeader("Content-Disposition",
         "attachment;filename=" + fileName);
 ServletContext ctx = getServletContext();
 String marcFilesDir = ctx.getInitParameter("marcFilePath");
 InputStream is = new FileInputStream(marcFilesDir + fileName);
 int read =0;  
 byte[] bytes = new byte[1024];  
 OutputStream os = response.getOutputStream();  
 while((read = is.read(bytes)) != -1)  
 {  
 os.write(bytes, 0, read);  
 }  
 os.flush();  
 os.close();  
}  
}  