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

public class schedulerCsv extends HttpServlet {

	class RemindTask extends TimerTask {

		private String contextDir = null;

		public RemindTask(String contextDir) {
			this.contextDir = contextDir;
		}

		public void run() {
			String webinf = contextDir + "WEB-INF" + File.separator;
			try {
				FileUtils.confirmDir(marcFilesDir);
			} catch (Exception e) {
				System.out.println("Exception in scheduler.run(): "+e);
			}

			//String appsDir = webinf + File.separator;

			String[] tmp = new String[2];
			tmp[0] = marcFilesDir;
			tmp[1] = marcFilesDir;
			
		    System.out.println(
			"***Patronload Scheduler Status: employee csv file - running Date" + new java.util.Date());
			edu.ucsd.library.patronload.apps.create_employee_file.main(tmp);
			System.out.println("***Patronload Scheduler CSV Status: Done Date" + new java.util.Date());
		}

	}

	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
		ServletContext ctx = conf.getServletContext();
		String contextDir =
			conf.getServletContext().getRealPath("") + File.separator;
		
		marcFilesDir = ctx.getInitParameter("marcFilePath");
		
		System.out.println(
			"****Patronload Scheduler CSV running");
		runme(contextDir);
		System.out.println("****Patronload Scheduler Status: Done");
	}

	public void runme(String contextDir) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 1);
		calendar.set(Calendar.MINUTE, 30);
		calendar.set(Calendar.SECOND, 0);
		Date time = calendar.getTime();

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new RemindTask(contextDir), time, 604800000);
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
