package edu.ucsd.library.patronload.apps;

/*
 * dofull.java
 *
 * Created on June 21, 2002, 1:44 PM
 */

/**
 *
 * @author  Joseph Jesena
 */

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class dofull {

	/** Creates new dofull */
	public dofull() {
	}

	/**
	 * Method to generate the filenames of the resulting MARC file
	 * @return String - The filename
	 */
	public static String createFileName() {

		Calendar calendar = Calendar.getInstance();
		Format formatter;
		formatter = new SimpleDateFormat("yyyy-MMM-dd");
		String dateString = formatter.format(new java.util.Date());
		return ("full-" + dateString + ".marc");
	}

	/**
	* @param args the command line arguments
	* args[0] = destination directory
	* args[1] = properties directory
	*/
	public static void main(String args[]) {

		if ((args == null) || (args.length < 2)) {
			System.out.println(
				"\nSyntax: java dofull [destination directory] [properties dir]");
		} else {
			fullquery.grabData(args[1], args[0] + "full_raw_file.txt");
			mergeaddr.mergeAddresses(
				args[0] + "full_raw_file.txt",
				args[0] + "full_merged_file.txt");
			makemarc.makeMarc(
				args[1],
				args[0] + "full_merged_file.txt",
				args[0] + createFileName());
		}
	}

}
