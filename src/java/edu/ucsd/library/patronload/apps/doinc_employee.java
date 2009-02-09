package edu.ucsd.library.patronload.apps;

/*
 * doinc_employee.java
 *
 * Created on October 22, 2003, 3:24 PM
 */

/**
 *
 * @author  Joseph Jesena
 */

import java.io.File;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class doinc_employee {

	/** Creates new dofull */
	public doinc_employee() {
	}

	/**
	 * Method to generate the filenames of the resulting MARC file
	 * @return String - The filename
	 */
	public static String createFileNameInc() {

		Format formatter;
		formatter = new SimpleDateFormat("yyyy-MMM-dd");
		String dateString = formatter.format(new java.util.Date());
		return ("inc-e-" + dateString + ".marc");
	}

	public static String createFileNameFull() {

		Format formatter;
		formatter = new SimpleDateFormat("yyyy-MMM-dd");
		String dateString = formatter.format(new java.util.Date());
		return ("full-ae-" + dateString + ".marc");
	}

	
	
	/**
	* @param args the command line arguments
	* args[0] = destination directory
	* args[1] = properties directory
	*/
	public static void main(String args[]) {

		if ((args == null) || (args.length < 2)) {
			System.out.println(
				"\nSyntax: java doinc_employee [destination directory] [properties dir]");
		} else {

			String prop_affiliations = args[1] + "emp_affiliations.properties";
			String prop_employee_type = args[1] + "employee_types.properties";
			String prop_original = args[1] + "patron_load.properties";

			try {
				File tmp = new File(args[0] + "full_raw_employee.txt");
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		        String modifiedDatePart = null;
		        if(tmp.exists())
		        	modifiedDatePart = formatter.format(tmp.lastModified());
		        
		        Calendar c =  Calendar.getInstance();
		        String todayDatePart = formatter.format(c.getTime());
		        
				if(modifiedDatePart != null && todayDatePart.equals(modifiedDatePart)) {
					System.out.println("Difference file already exists");
				} else {
					//if there is already an old file that was renamed, delete it
					tmp = new File(args[0] + "full_raw_employee_old.txt");
					if (tmp.exists())
						tmp.delete();
	
					//now rename the 'new' old file (if it exists, which it should)
					tmp = new File(args[0] + "full_raw_employee.txt");
					if (tmp.exists()) {
						tmp.renameTo(new File(args[0] + "full_raw_employee_old.txt"));
						//System.out.println("rename full_raw_employee.txt to full_raw_employee_old.txt");
					}
	
					//System.out.println("tmp file:"+tmp);
					//generate a new raw file
					fullquery_employee.grabData(
						prop_affiliations,
						prop_employee_type,
						prop_original,
						args[0] + "full_raw_employee.txt",
						false);
					//System.out.println("create the marc file for this new raw file");
					//create the marc file for this new raw file
					makemarc_employee.makeMarc(
							args[1],
							args[0] + "full_raw_employee.txt",
							args[0] + createFileNameFull());
					//System.out.println("now generate a new file based on the difference");
					//now generate a new file based on the difference (if the old file exists)
					if ((new File(args[0] + "full_raw_employee_old.txt")).exists()) {
						
						String[] tmp_args = new String[3];
						tmp_args[0] = args[0] + "full_raw_employee_old.txt";
						tmp_args[1] = args[0] + "full_raw_employee.txt";
						tmp_args[2] = args[0] + "inc_raw_employee.txt";
						incquery_employee.main(tmp_args);
						
						System.out.println("---Difference file has been created!");
						
		
						//now create the marc file
						makemarc_employee.makeMarc(
							args[1],
							args[0] + File.separator + "inc_raw_employee.txt",
							args[0] + File.separator + createFileNameInc());
						System.out.println("marc file inc_raw_employee has been created");
							
					} else {
					    System.out.println("ERROR: '" + args[0] + "full_raw_employee_old.txt' doex not exist!");
					}
				}
			} catch (Exception e) {
			}
		}
	}

}
