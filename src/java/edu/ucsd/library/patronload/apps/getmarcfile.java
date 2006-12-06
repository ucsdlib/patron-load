package edu.ucsd.library.patronload.apps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ucsd.library.util.FileUtils;

public class getmarcfile extends HttpServlet {
	private String marcFilesDir;
	
	public void init(ServletConfig conf) throws ServletException {		
		ServletContext ctx = conf.getServletContext();
		marcFilesDir = ctx.getInitParameter("marcFilePath");
		super.init(conf);		
	}

	public void service(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		//res.setContentType("text/html");
		ServletOutputStream out = res.getOutputStream();
		HttpSession mySession = req.getSession(true);

		String contextDir =
			getServletConfig().getServletContext().getRealPath("")
				+ File.separator;

		String fileToGet = req.getParameter("file");

		if (fileToGet != null) {
			if (mySession.getAttribute("username") != null) {

/*				String pathToFile =
					contextDir
						+ "WEB-INF"
						+ File.separator
						+ "marc_files"
						+ File.separator
						+ fileToGet;*/

				String pathToFile = marcFilesDir + fileToGet;
				
				if ((new File(pathToFile)).exists()) {

					res.setContentType("application/marc");
					res.setHeader(
						"Content-disposition",
						"attachment; filename=" + fileToGet);

					BufferedReader br = new BufferedReader(new FileReader(pathToFile));
					
					int charRead = 0;
					
					while ((charRead = br.read()) != -1) {
						out.print((char)charRead);	
					}
					//String contents = FileUtils.fileToString(pathToFile);
					//out.print(contents);

				} else {
					out.print("Error: filename not found!");
				}
			} else {
				out.print("Error: username is not found in the session!");
			}
		} else {
			out.print("Error: parameter \"file\" not found in request!");
		}
	}
}
