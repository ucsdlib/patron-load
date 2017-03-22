package edu.ucsd.library.patronload.apps;

/*
 * 
 * dofull_employee.java
 *
 * Created on October 22, 2003, 2:02 PM
 */

/**
 *
 * @author  Joseph Jesena
 */

import java.io.File;
import java.text.Format;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class create_employee_file {

	/** Creates new create_employee_file */
	public create_employee_file() {
	}

	/**
	 * Method to generate the filenames of the resulting CSV file
	 * @return String - The filename
	 */
	public static String createCsvFileName() {

		Format formatter;
		formatter = new SimpleDateFormat("yyyy-MMM-dd");
		String dateString = formatter.format(new java.util.Date());
		return ("inc-employees-" + dateString + ".csv");
	}

	/**
	 * Method to generate the filenames of the resulting CSV file
	 * @return String - The filename
	 */
	public static String createFullCsvFileName() {

		Format formatter;
		formatter = new SimpleDateFormat("yyyy-MMM-dd");
		String dateString = formatter.format(new java.util.Date());
		return ("full-employees-" + dateString + ".csv");
	}
	
	/**
	* @param args the command line arguments
	* args[0] = destination directory
	* args[1] = properties directory
	*/
	public static void main(String args[]) {

		if ((args == null) || (args.length < 2)) {
			System.out.println(
				"\nSyntax: java create_employee_file [destination directory] [properties dir]");
		} else {
			
			String prop_affiliations = args[1] + File.separator + "emp_affiliations.properties";
			String prop_employee_type = args[1] + File.separator + "employee_types.properties";
			String prop_original = args[1] + File.separator + "patron_load.properties";
			try {
				File tmp = new File(args[0] + "all_active_employee.txt");
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		        String modifiedDatePart = null;
		        if(tmp.exists())
		        	modifiedDatePart = formatter.format(tmp.lastModified());
		        
		        Calendar c =  Calendar.getInstance();
		        String todayDatePart = formatter.format(c.getTime());
				/*if(modifiedDatePart != null && todayDatePart.equals(modifiedDatePart)) {
					System.out.println("Difference file already exists");
				} else {*/
					//if there is already an old file that was renamed, delete it
					tmp = new File(args[0] + "all_active_employee_old.txt");
					if (tmp.exists())
						tmp.delete();
					
					//now rename the 'new' old file (if it exists, which it should)
					tmp = new File(args[0] + "all_active_employee.txt");
					if (tmp.exists()) {
						tmp.renameTo(new File(args[0] + "all_active_employee_old.txt"));
					}
					all_employee.grabData(prop_affiliations, prop_employee_type, prop_original, args[0] + File.separator + "all_active_employee.txt");
					makecsv_employee.makeCsv(
							args[1],
							args[0] + File.separator + "all_active_employee.txt",
							args[0] + File.separator + createFullCsvFileName());
					if ((new File(args[0] + "all_active_employee_old.txt")).exists()) {
						
						String[] tmp_args = new String[3];
						tmp_args[0] = args[0] + "all_active_employee_old.txt";
						tmp_args[1] = args[0] + "all_active_employee.txt";
						tmp_args[2] = args[0] + "inc_all_employee.txt";
						inc_all_employee.main(tmp_args);
						
						System.out.println("---Difference all employee file has been created!");
								
						makecsv_employee.makeCsv(
							args[1],
							args[0] + File.separator + "inc_all_employee.txt",
							args[0] + File.separator + createCsvFileName());
						System.out.println("csv file for inc_all_employee.txt has been created");
							
					} else {
					    System.out.println("ERROR: '" + args[0] + "all_active_employee_old.txt' doex not exist!");
					}
				//}
			} catch (Exception e) {
			}					
		}
		System.out.println("end");
	}

}
