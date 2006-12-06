package edu.ucsd.library.patronload.apps;

/*
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
import java.text.SimpleDateFormat;

public class dofull_employee {

	/** Creates new dofull */
	public dofull_employee() {
	}

	/**
	 * Method to generate the filenames of the resulting MARC file
	 * @return String - The filename
	 */
	public static String createFullFileName() {

		Format formatter;
		formatter = new SimpleDateFormat("yyyy-MMM-dd");
		String dateString = formatter.format(new java.util.Date());
		return ("full-te-" + dateString + ".marc");
	}

	/**
	 * Method to generate the filenames of the resulting MARC file
	 * @return String - The filename
	 */
	public static String createActiveFileName() {

		Format formatter;
		formatter = new SimpleDateFormat("yyyy-MMM-dd");
		String dateString = formatter.format(new java.util.Date());
		return ("full-ae-" + dateString + ".marc");
	}

	/**
	* @param args the command line arguments
	* args[0] = destination directory
	* args[1] = properties directory
	* args[2] = true=full pull; false=only full active pull (remove unactive) 
	*/
	public static void main(String args[]) {

		if ((args == null) || (args.length < 3)) {
			System.out.println(
				"\nSyntax: java dofull_employee [destination directory] [properties dir] [boolean; do a total full pull?]");
		} else {
			
			String prop_affiliations = args[1] + File.separator + "emp_affiliations.properties";
			String prop_employee_type = args[1] + File.separator + "employee_types.properties";
			String prop_original = args[1] + File.separator + "patron_load.properties";
			
			if (args[2].equals("true")) {
				
				fullquery_employee.grabData(prop_affiliations, prop_employee_type, prop_original, args[0] + File.separator + "full_raw_employee.txt", true);
				
				makemarc_employee.makeMarc(
					args[1],
					args[0] + File.separator + "full_raw_employee.txt",
					args[0] + File.separator + createFullFileName());
			} else {
				
				fullquery_employee.grabData(prop_affiliations, prop_employee_type, prop_original, args[0] + File.separator + "full_raw_active_employee.txt", false);
				
				makemarc_employee.makeMarc(
					args[1],
					args[0] + File.separator + "full_raw_active_employee.txt",
					args[0] + File.separator + createActiveFileName());
			}
			
		}
	}

}
