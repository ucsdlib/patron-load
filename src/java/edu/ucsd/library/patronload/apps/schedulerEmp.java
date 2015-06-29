package edu.ucsd.library.patronload.apps;

/*
 * schedulerEmp.java
 *
 * Created on June 25, 2002, 1:44 PM;
 */

/**
 *
 * @author  Joseph Jesena
 */

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.naming.InitialContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ucsd.library.util.FileUtils;

public class schedulerEmp extends HttpServlet {

	class RemindTask extends TimerTask {

		private String contextDir = null;

		public RemindTask(String contextDir) {
			this.contextDir = contextDir;
		}

		public void run() {
			String webinf = contextDir + "WEB-INF" + File.separator;
			//String marcDir = webinf + "marc_files" + File.separator;

			//make sure the marc directory exists
			try {
				FileUtils.confirmDir(marcFilesDir);
			} catch (Exception e) {
				System.out.println("Exception in scheduler.run(): "+e);
			}

			//String appsDir = webinf + File.separator;

			String[] tmp = new String[2];
			tmp[0] = marcFilesDir;
			//tmp[1] = appsDir;
			tmp[1] = marcFilesDir;
			incquery.setMarcFilesDir(marcFilesDir);
		    fullquery.setMarcFilesDir(marcFilesDir);	
		    System.out.println(
			"***Patronload Scheduler Status: employee marc file - running Date" + new java.util.Date());
			//edu.ucsd.library.patronload.apps.doinc.main(tmp);
			// generate the incremental employee marc file 
			edu.ucsd.library.patronload.apps.doinc_employee.main(tmp);
			System.out.println("***Patronload Scheduler Status: Done Date" + new java.util.Date());
		}

	}

	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
		ServletContext ctx = conf.getServletContext();
		String contextDir =
			conf.getServletContext().getRealPath("") + File.separator;
		/*
		String sharedPath = "";
		try
		{
			InitialContext jndi = new InitialContext();
			sharedPath = (String)jndi.lookup("java:comp/env/clusterSharedPath");
		}
		catch ( Exception ex )
		{
			sharedPath = "/pub/data1/import/htdocs";
			ex.printStackTrace();
		}
		marcFilesDir = sharedPath + ctx.getInitParameter("marcFilePath");
		*/
		marcFilesDir = ctx.getInitParameter("marcFilePath");
		System.out.println("filePath:"+marcFilesDir);
		incquery.setMarcFilesDir(marcFilesDir);
	    fullquery.setMarcFilesDir(marcFilesDir);	
		System.out.println(
			"****Patronload Scheduler running. version date: 2002-07-09");
		runme(contextDir);
		System.out.println("****Patronload Scheduler Status: Done");
	}

	public void runme(String contextDir) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 1);
		calendar.set(Calendar.MINUTE, 01);
		calendar.set(Calendar.SECOND, 0);
		Date time = calendar.getTime();

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new RemindTask(contextDir), time, 86400000);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		service(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		service(req, res);
	}

	public void service(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {

		String contextDir =
			getServletContext().getRealPath("") + File.separator;
		
		
		System.out.println(
			"This is the PatronLoad Scheduler! Date: " + new java.util.Date());
	}
    
	private String marcFilesDir;
}
