/*
 * Created on Sep 11, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package edu.ucsd.library.patronload.apps;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import edu.ucsd.library.util.FileUtils;

/**
 * @author jjesena
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class fullquery_employee {

	/**
	 * @param args the command line arguments
	 * args[0] = path to affiliations properties file
	 * args[1] = path to patron type codes properties file
	 * args[2] = path to original properties file
	 * args[3] = file to write output to
	 * args[4] = boolean value; true=get everything; false=get only active empl.
	 */
	public static void main(String args[]) {

		if ((args == null) || (args.length < 5)) {
			System.out.println(
				"\nSyntax: java fullquery [affiliations properties file] [patron type codes properties file] [original properties file] [fileToWrite] [true=get all; false=get only active empl]");
		} else {
			grabData(args[0], args[1], args[2], args[3], args[4].equals("true") ? true : false);
		}
	}
	
	private static void writeFile(StringBuffer in, String file) {
		if (in.toString().length() > 0) {
			try {
				FileUtils.stringToFile(in.toString(), outputLocation + file);
			} catch (IOException ioe) {
			}
		}
	}

	/**
	 * Write data out to the file
	 * @param props1 Path to affilications properties file
	 * @param props2 Path to type codes properties file
	 * @param props3 Path to original properties file
	 * @param fileToWrite Path to the file to write results to
	 * @param total Boolean value: true=get everything; false=get active empl. only
	 */
	public static void grabData(
		String props1,
		String props2,
		String props3,
		String fileToWrite,
		boolean total) {

		outputLocation = fileToWrite.substring(0, fileToWrite.lastIndexOf(File.separator) + 1);
		
		PrintWriter pw = null;

		//--- Create the file stream here to output to file
		try {
			pw =
				new PrintWriter(
					new BufferedOutputStream(
						new FileOutputStream(fileToWrite)));

			getRawData(props1, props2, props3, pw, total);

			if (pw != null)
				pw.close();

			//go ahead and write the debug files
			writeFile(filter3a, "remove-step3a.txt");
			writeFile(filter4a, "remove-step4a.txt");
			writeFile(filter4b, "remove-step4b.txt");
			writeFile(filter4c, "remove-step4c.txt");
			writeFile(filter4d, "remove-step4d.txt");
			writeFile(typeCodeNotFound, "type_code_not_found.txt");
			writeFile(titleCodeNotFound, "title_code_not_found.txt");
			writeFile(affiliationCodeNotFound, "affiliation_code_not_found.txt");
			writeFile(moreDuplicates, "more_dups.txt");
			writeFile(invalidRecords, "remove-invalid.txt");

		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

	/**
	 * Removes leading zeros from the String
	 * @param str The String to remove leading zeros from
	 * @return The String without any leading zeros
	 */
	private static String removeLeadingZeros(String str) {
		if (str == null) {
			return null;
		}
		char[] chars = str.toCharArray();
		int index = 0;
		for (; index < str.length(); index++) {
			if (chars[index] != '0') {
				break;
			}
		}
		return (index == 0) ? str : str.substring(index);
	}

	/**
	 * Parses a record String 'in' - it returns the String field at position 'num'
	 * Note: fields are separated by a tab character
	 * @param in The String to parse
	 * @param num The field position to return 
	 * @return String - The field at position 'num'
	 */
	public static String parseRecord(String in, int num) {
		String[] array = in.split("\t");

		  //remove leading zeros except in mailcode case
		  array[num] = removeLeadingZeros(array[num]);

		return array[num];
	}

  public static String parseRecord(String in, int num, boolean removeZero) {
		String[] array = in.split("\t");

    if(removeZero)
		  //remove leading zeros except in mailcode case
		  array[num] = removeLeadingZeros(array[num]);

		return array[num];
	}
	/**
	 * Checks to see if the provided titleCode is an Emeritus title code
	 * @param titleCode The title code to test
	 * @return boolean true if titleCode is an Emeritus title code; else false
	 */
	public static boolean isEmeritusTitleCode(String titleCode) {
		titleCode = titleCode.trim();
		if (EMERITUS_TITLE_CODES.indexOf("'" + titleCode + "'") > -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks to see if the provided status code is a 'separated' status code
	 * @param status The status code to test
	 * @return boolean true if statusCode is a 'separated' statusCode; else false
	 */
	public static boolean isSeparatedStatus(String status) {
		status = status.trim();
		if (SEPARATED_CODES.indexOf("'" + status + "'") > -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Tests if a patron record is separated
	 * @param line The patron record to test
	 * @return boolean true if patron is separated; else false
	 */
	public static boolean isPatronSeparated(String line) {
		String statusCode = parseRecord(line, 2).trim();
		return isSeparatedStatus(statusCode);
	}
	
	/**
	 * Tests if a patron is regarded as an Emeritus
	 * @param line The patron record to test
	 * @return boolean true if patron is granted emeritus status; else false
	 */
	public static boolean isPatronEmeritus(String line) {
		String titleCode = parseRecord(line, 4).trim();
		return isEmeritusTitleCode(titleCode);
	}

	/**
	 * Returns the staff title codes as a string surrounded by quotes; these
	 * codes are then separated by a comma.
	 * @param props The Properties file to get the codes from 
	 * @return String - The staff title codes as a String
	 */
	private static String getStaffTitleCodes(Properties props) {

		StringBuffer result = new StringBuffer("(");
		for (Enumeration e = props.propertyNames(); e.hasMoreElements();) {

			String value = (String) e.nextElement();
			if (props.get(value).equals("17")) {
				result.append("'" + value + "',");
			}
		}

		result.append("'')");
		return result.toString();
	}

	/**
	 * Returns the faculty title codes as a string surrounded by quotes; these
	 * codes are then separated by a comma.
	 * @param props The Properties file to get the codes from 
	 * @return String - The faculty title codes as a String
	 */
	private static String getFacultyTitleCodes(Properties props) {

		StringBuffer result = new StringBuffer("(");
		for (Enumeration e = props.propertyNames(); e.hasMoreElements();) {

			String value = (String) e.nextElement();
			if (props.get(value).equals("1")) {
				result.append("'" + value + "',");
			}
		}

		result.append("'')");
		return result.toString();
	}

	/**
	 * Returns the student title codes as a string surrounded by quotes; these
	 * codes are then separated by a comma.
	 * @param props The Properties file to get the codes from 
	 * @return String - The student title codes as a String
	 */
	private static String getStudentTitleCodes(Properties props) {

		StringBuffer result = new StringBuffer("(");
		for (Enumeration e = props.propertyNames(); e.hasMoreElements();) {

			String value = (String) e.nextElement();
			if (props.get(value).equals("12")) {
				result.append("'" + value + "',");
			}
		}

		result.append("'')");
		return result.toString();
	}

	/**
	 * Returns the post-doc title codes as a string surrounded by quotes; these
	 * codes are then separated by a comma.
	 * @param props The Properties file to get the codes from 
	 * @return String - The post-doc title codes as a String
	 */
	private static String getPostDocTitleCodes(Properties props) {

		StringBuffer result = new StringBuffer("(");
		for (Enumeration e = props.propertyNames(); e.hasMoreElements();) {

			String value = (String) e.nextElement();
			if (props.get(value).equals("40")) {
				result.append("'" + value + "',");
			}
		}

		result.append("'')");
		return result.toString();
	}

	/**
	 * Performs step-3 filtering: "Software removes all listing for Code 3 employees
	 * with non-staff job titles"
	 * @param recordBuffer The Vector that potentially holds duplicates; what we are trying to clean
	 * @param typeCodes The Properties file to get the employee codes from
	 * @return Vector - The result of applying step-3 filtering
	 */
	private static Vector filter3a(Vector recordBuffer, Properties typeCodes) {
		int index = 0;
		
		while (index < recordBuffer.size()) {
			String tmpCurrent = (String) recordBuffer.elementAt(index);

			//check if this is a code 3 employee
			if (parseRecord(tmpCurrent, 8).equals("3")) {
				if (getStaffTitleCodes(typeCodes)
					.indexOf("'" + parseRecord(tmpCurrent, 4) + "'")
					== -1) {
					//if it's not part of the staff title codes, remove it
					
					filter3a.append(tmpCurrent);

					recordBuffer.removeElementAt(index);
				} else {
					index++;
				}
			} else {
				index++;
			}
		}
		
		return recordBuffer;
	}

	/**
	 * Performs step-4a filtering: "If any listing has department code=256, then retain this listing 
	 * and delete all other listings for that employee"
	 * @param recordBuffer The Vector that potentially holds duplicates; what we are trying to clean
	 * @return Vector - The result of applying step-4a filtering
	 */
	private static Vector filter4a(Vector recordBuffer) {
		boolean isAfter = false;
		boolean isAfterTmp = false;		
		Date recordEndDate = null;
		Date recordEndDateTmp = null;		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = new Date();
		//String debugTempId = null;
		
		try {
			for (int i = 0; i < recordBuffer.size(); i++) {
				String current = (String) recordBuffer.elementAt(i);

				/*recordEndDate = format.parse(parseRecord(current, 13));
				//debugTempId = parseRecord(current, 0);
				
				isAfter = recordEndDate.after(currentDate);
				if(debugTempId.equals("146094"))
					System.out.println("outside the 2nd loop: "+current);
				
				if(isAfter) {
					int index = 0;
					while ((recordBuffer.size() > 1)
						&& (index < recordBuffer.size())) {
						String tmpCurrent = (String) recordBuffer.elementAt(index);

						recordEndDateTmp = format.parse(parseRecord(tmpCurrent, 13));

						isAfterTmp = recordEndDateTmp.after(currentDate);
						
						if(debugTempId.equals("146094"))
							System.out.println("insde the 2nd loop: "+tmpCurrent);
							
						if (index == 0 && isAfterTmp) {
							index++;
							continue;
						}				
						filter4a.append(tmpCurrent);
						
						recordBuffer.removeElementAt(index);
					}					      		
				} else*/ if (parseRecord(current, 6).equals("265")) {
	
					int index = 0;
					while ((recordBuffer.size() > 1)
						&& (index < recordBuffer.size())) {
						String tmpCurrent = (String) recordBuffer.elementAt(index);
						recordEndDateTmp = format.parse(parseRecord(tmpCurrent, 13));

						isAfterTmp = recordEndDateTmp.after(currentDate);						
						if (index == 0
							&& parseRecord(tmpCurrent, 6).equals("265") && isAfterTmp) {
							index++;
							continue;
						}				
						filter4a.append(tmpCurrent);
						
						recordBuffer.removeElementAt(index);
					}
				} 
			}
		} catch (Exception e) {
			System.out.println("DateFormatException in fullquery_employee.filter4a(): "+e);
		}
		
		return recordBuffer;
	}

	/**
	 * Performs step-4b filtering: "If all listings are from the same group type, 
	 * then retain the listing with the lowest title value and delete all other 
	 * listings for that employee"
	 * @param recordBuffer The Vector that potentially holds duplicates; what we are trying to clean
	 * @param typeCodes The Properties file to get the employee codes from
	 * @return Vector - The result of applying step-4b filtering
	 */
	private static Vector filter4b(Vector recordBuffer, Properties typeCodes) {
		int staffCount = 0, facultyCount = 0, postDocCount = 0;
		
		boolean isAfterTmp = false;		
		Date recordEndDateTmp = null;		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = new Date();
		
		try {
			//---begin filtering: part4(b)
			if (recordBuffer.size() > 1) {
				for (int i = 0; i < recordBuffer.size(); i++) {
					String current = (String) recordBuffer.elementAt(i);
	
					if (getStaffTitleCodes(typeCodes)
						.indexOf("'" + parseRecord(current, 4) + "'")
						> -1) {
						staffCount++;
					}
	
					if (getFacultyTitleCodes(typeCodes)
						.indexOf("'" + parseRecord(current, 4) + "'")
						> -1) {
						facultyCount++;
					}
	
					if (getPostDocTitleCodes(typeCodes)
						.indexOf("'" + parseRecord(current, 4) + "'")
						> -1) {
						postDocCount++;
					}
				}
	
				if (staffCount == recordBuffer.size()
					|| facultyCount == recordBuffer.size()
					|| postDocCount == recordBuffer.size()) {
					int smallestTitleCode = 99999999;
	
					//get the lowest title code value
					for (int j = 0; j < recordBuffer.size(); j++) {
						String tmpCurrent = (String) recordBuffer.elementAt(j);
	
						if (Integer.parseInt(parseRecord(tmpCurrent, 4))
							< smallestTitleCode) {
							smallestTitleCode =
								Integer.parseInt(parseRecord(tmpCurrent, 4));
							//smallestEntry = new String(tmpCurrent);
						}
					}
	
					int index = 0;
					while ((recordBuffer.size() > 1)
						&& (index < recordBuffer.size())) {
						String tmpCurrent = (String) recordBuffer.elementAt(index);
						recordEndDateTmp = format.parse(parseRecord(tmpCurrent, 13));
	
						isAfterTmp = recordEndDateTmp.after(currentDate);
						
						if (index == 0
							&& parseRecord(tmpCurrent, 4).equals(
								Integer.toString(smallestTitleCode)) && isAfterTmp) {
							index++;
	
							continue;
						}
						
						filter4b.append(tmpCurrent);
						recordBuffer.removeElementAt(index);
					}
				}
			}
		
	    }
		catch (Exception e) {
			System.out.println("DateFormatException in fullquery_employee.filter4b(): "+e);
		}
		//---end filtering: part 4(b)
		return recordBuffer;
	}

	/**
	 * Performs step-4c filtering: "If the listings are from different group 
	 * types and a Faculty title exists, then retain the listing with the lowest 
	 * Faculty title value and delete all other listings for that employee"
	 * @param recordBuffer The Vector that potentially holds duplicates; what we are trying to clean
	 * @param typeCodes The Properties file to get the employee codes from
	 * @return Vector - The result of applying step-4c filtering
	 */
	private static Vector filter4c(Vector recordBuffer, Properties typeCodes) {
		int staffCount = 0, facultyCount = 0, postDocCount = 0;
		boolean isAfterTmp = false;		
		Date recordEndDateTmp = null;		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = new Date();
		
		try {
			for (int i = 0; i < recordBuffer.size(); i++) {
				String current = (String) recordBuffer.elementAt(i);
	
				if (getStaffTitleCodes(typeCodes)
					.indexOf("'" + parseRecord(current, 4) + "'")
					> -1) {
					staffCount++;
				}
	
				if (getFacultyTitleCodes(typeCodes)
					.indexOf("'" + parseRecord(current, 4) + "'")
					> -1) {
					facultyCount++;
				}
	
				if (getPostDocTitleCodes(typeCodes)
					.indexOf("'" + parseRecord(current, 4) + "'")
					> -1) {
					postDocCount++;
				}
			}
	
			if ((facultyCount > 0) && ((staffCount > 0) || (postDocCount > 0))) {
				int smallestTitleCode = 99999999;
				for (int j = 0; j < recordBuffer.size(); j++) {
					String tmpCurrent = (String) recordBuffer.elementAt(j);
	
					if (getFacultyTitleCodes(typeCodes)
						.indexOf("'" + parseRecord(tmpCurrent, 4) + "'")
						== -1) {
						//if title code is not in Faculty group, skip it 
						continue;
					}
	
					if (Integer.parseInt(parseRecord(tmpCurrent, 4))
						< smallestTitleCode) {
						smallestTitleCode =
							Integer.parseInt(parseRecord(tmpCurrent, 4));
					}
				}
	
				//note: smallestTitleCode should now contain the lowest
				//Faculty title value
	
				int index = 0;
				while ((recordBuffer.size() > 1)
					&& (index < recordBuffer.size())) {
					String tmpCurrent = (String) recordBuffer.elementAt(index);
					recordEndDateTmp = format.parse(parseRecord(tmpCurrent, 13));
					
					isAfterTmp = recordEndDateTmp.after(currentDate);
										
					if (index == 0
						&& parseRecord(tmpCurrent, 4).equals(
							Integer.toString(smallestTitleCode)) && isAfterTmp) {
						index++;
						continue;
					}
					
					filter4c.append(tmpCurrent);
					recordBuffer.removeElementAt(index);
				}
			}
	    } catch (Exception e) {
			System.out.println("DateFormatException in fullquery_employee.filter4c(): "+e);
		}
		return recordBuffer;
	}

	/**
	 * Performs step-4d filtering: "If the listings are from the Post Doc and 
	 * Staff groups, then retain the listing with the lowest Post Doc title 
	 * value and delete all other listings for that employee"
	 * @param recordBuffer The Vector that potentially holds duplicates; what we are trying to clean
	 * @param typeCodes The Properties file to get the employee codes from
	 * @return Vector - The result of applying step-4d filtering
	 */
	private static Vector filter4d(Vector recordBuffer, Properties typeCodes) {
		int staffCount = 0, facultyCount = 0, postDocCount = 0;
		boolean isAfterTmp = false;		
		Date recordEndDateTmp = null;		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = new Date();
		
		try {
			for (int i = 0; i < recordBuffer.size(); i++) {
				String current = (String) recordBuffer.elementAt(i);
	
				if (getStaffTitleCodes(typeCodes)
					.indexOf("'" + parseRecord(current, 4) + "'")
					> -1) {
					staffCount++;
				}
	
				if (getFacultyTitleCodes(typeCodes)
					.indexOf("'" + parseRecord(current, 4) + "'")
					> -1) {
					facultyCount++;
				}
	
				if (getPostDocTitleCodes(typeCodes)
					.indexOf("'" + parseRecord(current, 4) + "'")
					> -1) {
					postDocCount++;
				}
			}
	
			if ((facultyCount == 0) && (staffCount > 0) && (postDocCount > 0)) {
				int smallestTitleCode = 99999999;
				for (int j = 0; j < recordBuffer.size(); j++) {
					String tmpCurrent = (String) recordBuffer.elementAt(j);
	
					if (getPostDocTitleCodes(typeCodes)
						.indexOf("'" + parseRecord(tmpCurrent, 4) + "'")
						== -1) {
						//if title code is not in Post-doc group, skip it 
						continue;
					}
	
					if (Integer.parseInt(parseRecord(tmpCurrent, 4))
						< smallestTitleCode) {
						smallestTitleCode =
							Integer.parseInt(parseRecord(tmpCurrent, 4));
					}
				}
	
				//note: smallestTitleCode should now contain the lowest
				//Post-doc title value
	
				int index = 0;
				while ((recordBuffer.size() > 1)
					&& (index < recordBuffer.size())) {
					String tmpCurrent = (String) recordBuffer.elementAt(index);
					recordEndDateTmp = format.parse(parseRecord(tmpCurrent, 13));
					
					isAfterTmp = recordEndDateTmp.after(currentDate);
										
					if (index == 0
						&& parseRecord(tmpCurrent, 4).equals(
							Integer.toString(smallestTitleCode)) && isAfterTmp) {
						index++;
						continue;
					}
					
					filter4d.append(tmpCurrent);
					recordBuffer.removeElementAt(index);
				}
			}
		} catch (Exception e) {
			System.out.println("DateFormatException in fullquery_employee.filter4d(): "+e);
		}
		return recordBuffer;
	}

	private static Vector removeStudentTitles(Vector recordBuffer, Properties typeCodes) {
		int index = 0;
		while ((recordBuffer.size() > 1)
			&& (index < recordBuffer.size())) {
			String current = (String) recordBuffer.elementAt(index);

			if (getStudentTitleCodes(typeCodes).indexOf("'" + parseRecord(current, 4) + "'") > -1) {
				//System.out.println("Removed: " + parseRecord(current, 1));				
				recordBuffer.removeElementAt(index);
				continue;
			} else {
				index++;
			}
		}
		return recordBuffer;
	}

	/**
	 * Checks to see if the Patron record contains a student title-code
	 * @param typeCodes The properties taht define the title-codes
	 * @param in The patron record to check
	 * @return boolean true if patron has student title-code; else false
	 */
	private static boolean isStudentTitleCode(Properties typeCodes, String in) {

		if (getStudentTitleCodes(typeCodes)
			.indexOf("'" + parseRecord(in, 4) + "'")
			> -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check wether or not a title code is valid. A title code is valid if 
	 * the code is in the Properties file referenced by typeCodes
	 * @param typeCodes
	 * @param in
	 * @return
	 */
	private static boolean isValidCode(Properties typeCodes, String in) {
		boolean isValid = false;
		if (getStaffTitleCodes(typeCodes)
			.indexOf("'" + parseRecord(in, 4) + "'")
			> -1) {
			isValid = true;
		}
		if (getFacultyTitleCodes(typeCodes)
			.indexOf("'" + parseRecord(in, 4) + "'")
			> -1) {
			isValid = true;
		}
		if (getPostDocTitleCodes(typeCodes)
			.indexOf("'" + parseRecord(in, 4) + "'")
			> -1) {
			isValid = true;
		}
		if (getStudentTitleCodes(typeCodes)
			.indexOf("'" + parseRecord(in, 4) + "'")
			> -1) {
			isValid = false;
		}

		return isValid;
	}

	/**
	 * Method to retreive data from database and output to raw file.
	 * @param props1 Path to affiliation properties file
	 * @param props2 Path to type codes properties file
	 * @param props3 Path to original properties file
	 * @param fileToWrite Path to the file to write results to
	 * @param total Boolean value: true=does everything; false=only active empl.
	 */
	public static void getRawData(
		String props1,
		String props2,
		String props3,
		PrintWriter pw,
		boolean total) {

		if (!props3.equals("")) {
			props3 += File.separator;
		}

		// load the properties file to get the current quarter code
		Properties typeCodes = null;
		Properties original = null;
		Properties affiliationCodes = null;
		try {
			original = FileUtils.loadProperties(props3);
			typeCodes = FileUtils.loadProperties(props2);
			affiliationCodes = FileUtils.loadProperties(props1);
		} catch (IOException ioe) {
			System.out.println("Error loading properties file!");
			//System.exit(1);
			return;
		}
/*
		String dbDriver = (String) original.get("dbdriver");
		dbDriver = dbDriver.trim();

		String dbUsername = (String) original.get("dbusername");
		dbUsername = dbUsername.trim();

		String dbPassword = (String) original.get("dbpassword");
		dbPassword = dbPassword.trim();

		String dbConnection = (String) original.get("dbconnection3");
		dbConnection = dbConnection.trim();

		try {
			Class.forName(dbDriver).newInstance();
		} catch (Exception E) {
			System.err.println("Error: Unable to load driver: " + dbDriver);
			E.printStackTrace();
			System.exit(1);
		}
*/
		
		String db2Driver = (String) original.get("db2driver");
		String db2Username = (String) original.get("db2username");
		String db2Password = (String) original.get("db2password");
		String db2Connection = (String) original.get("db2connection");
		
		
		try {
			Class.forName(db2Driver).newInstance();
		} catch (Exception E) {
			System.err.println("Error: Unable to load db2 driver: " + db2Driver);
			E.printStackTrace();
			System.exit(1);
		}
		
		Statement stmt = null;
		//Connection conn = null;
		Connection db2Conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtPhone = null;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		String currentDate = format.format(new java.util.Date());
		ResultSet emailRS = null;
		ResultSet phoneRS = null;
		ResultSet barcodeRS = null;
		Map employeeMap = null;
		Map employeePhone = null;
		Map employeeBarcode = null;

		try {

			db2Conn = DriverManager.getConnection(
					db2Connection,
					db2Username,
					db2Password);
/*			
			String queryTest = "SELECT emb_employee_name " +
					"FROM employee.p_employee_fin_view " +
					"WHERE emb_employee_name like 'CHU, V%'";
			
			pstmt = db2Conn.prepareStatement(queryTest);
			System.out.println("start");
			emailRS = pstmt.executeQuery();		
			
			while (emailRS.next()) {	
				System.out.println((String)emailRS.getString(1));			
			}
			
			db2Conn.close();
			pstmt = null;
			emailRS = null;*/
			
			// Used TLI to IP/Port Converter to get IP and port
			// http://www.outlands.demon.co.uk/utilities/tli2ip.html
			// TLI: \x000207e984efb4080000000000000000
			/*conn =
				DriverManager.getConnection(
					dbConnection,
					dbUsername,
					dbPassword);*/
			stmt = db2Conn.createStatement();
			//stmt = conn.createStatement();

			String query =
				"SELECT DISTINCT "
					+ "p.emb_person_id EMPID, "
					+ "p.emb_employee_name NAME, "
					+ "p.emp_employment_status_code, "
					+ "p.emp_student_status_code, "
					+ "p1.app_title_code TITLE_CODE, "
					+ "p1.app_title_name, "
					+ "p1.app_department_code DEPT_CODE, "
					+ "p1.app_department_name, "
					+ "p.emp_student_status_code, "
					+ "ph.employee_mail_code MAIL_CODE, " 
					+ "ph.employee_office_phone PHONE, " 
					+ "ph.employee_email EMAIL, " 
					//+ "v.barcode BARCODE, " 
					+ "p.emb_employee_id, "
					+ "p2.dis_end_date " 
					+ " FROM " 
					+ " employee.p_employee_fin_view p LEFT OUTER JOIN phone.employee ph "
					+ " ON ph.emb_employee_number = p.emb_person_id , "
					//+ " LEFT JOIN idcard_db.dbo.idcard_v v "
					//+ " ON p.emb_employee_id = v.employee_id, "
					+ " employee.p_appointment p1, "  
					+ " employee.p_distribution p2 "
					+ " WHERE " 
					+ " ("
					+ "((p.emp_student_status_code IN (" + EMP_STUDENT_STATUS_CODE + ")) OR "
                    + "((p.emp_student_status_code = '4') AND (p1.app_title_code IN (" + STUDENT_STAFF_TITLE_CODES + ")) )) "

                    // if title code=4011, download only if status code = 1
                    + "AND ((p1.app_title_code <> '4011') OR ((p1.app_title_code = '4011') AND (p.emp_student_status_code = '1'))) "
					
                    + "AND (p1.emb_person_id = p.emb_person_id) "
					+ "AND (p2.emb_person_id = p.emb_person_id) "
					+ "AND (p2.app_appointment_number = p1.app_appointment_number) "
					
					+ (total==false ?
						
						 "AND ((p.emp_employment_status_code NOT IN (" + BLOCKED_STATUS_CODES + ")) "
							
						 + "AND ((p.emp_employment_status_code NOT IN (" + SEPARATED_CODES + ")) OR " +
						//--if Emeritus, download record if separated						
						 "(p1.app_title_code IN (" + EMERITUS_TITLE_CODES + "))" + 

						")) " : "")
					
					+ ") ORDER BY EMPID, p.emb_employee_name ";

			
			
			/*
			String query =
				"SELECT DISTINCT "
					+ "p.emb_person_id EMPID, "
					+ "p.emb_employee_name NAME, "
					+ "p.emp_employment_status_code, "
					+ "p.emp_student_status_code, "
					+ "p1.app_title_code TITLE_CODE, "
					+ "p1.app_title_name, "
					+ "p1.app_department_code DEPT_CODE, "
					+ "p1.app_department_name, "
					+ "p.emp_student_status_code, "
					//+ "p.emp_mailcode MAIL_CODE," 
					+ "ph.employee_mail_code MAIL_CODE," 
					+ " ph.employee_office_phone PHONE" 
					+ ", ph.employee_email EMAIL " 
					+ ", v.barcode BARCODE " 
					+ ", p2.dis_end_date " 
					+ " FROM " 
					+ "(dbo.p_employee p LEFT JOIN phone.dbo.employee ph "
					+ " ON ph.emb_employee_number = p.emb_person_id) "
					+ " LEFT JOIN idcard_db.dbo.idcard_v v "
					+ " ON p.emb_employee_id = v.employee_id, "
					+ " dbo.p_appointment p1, "  
					+ " dbo.p_distribution p2 "
					+ " WHERE " 
					+ " ("
					+ "((p.emp_student_status_code IN (" + EMP_STUDENT_STATUS_CODE + ")) OR "
                    + "((p.emp_student_status_code = '4') AND (p1.app_title_code IN (" + STUDENT_STAFF_TITLE_CODES + ")) )) "

                    // if title code=4011, download only if status code = 1
                    + "AND ((p1.app_title_code <> '4011') OR ((p1.app_title_code = '4011') AND (p.emp_student_status_code = '1'))) "
					
                    + "AND (p1.emb_id = p.emb_id) "
					+ "AND (p2.emb_id = p.emb_id) "
					+ "AND (p2.app_appointment_number = p1.app_appointment_number) "
					
					+ (total==false ?
						
						 "AND ((p.emp_employment_status_code NOT IN (" + BLOCKED_STATUS_CODES + ")) "
							
						 + "AND ((p.emp_employment_status_code NOT IN (" + SEPARATED_CODES + ")) OR " +
						//--if Emeritus, download record if separated						
						 "(p1.app_title_code IN (" + EMERITUS_TITLE_CODES + "))" + 

						")) " : "")
					
					+ ") ORDER BY EMPID, p.emb_employee_name "; */
			
			//System.out.println("query" + query);
			
			//try {
			//	FileUtils.stringToFile(query, "c:\\full_query_employee.txt");
			//} catch (IOException ioe) {
			//}
			/*String emailQuery = "select distinct b.value as ID, a.value as EMAIL " +
					"from affiliates_db.dbo.safe_attributes a, " +
					"affiliates_db.dbo.safe_attributes b where a.aid=b.aid and b.name='emp_id' and " +
					"b.value=? and a.name='email'";*/
			/*
			String emailQuery = "select distinct a.value, b.value from affiliates_db.dbo.safe_attributes a, " +
					"affiliates_db.dbo.safe_attributes b " +
					"where a.aid=b.aid and a.name='emp_id' and b.name='official_email'";
			*/

			String emailQuery = "select distinct emb_person_id, official_email from " +
					"affiliates_dw.affiliates_safe_attributes where emb_person_id != 0 " +
					"and official_email != ''";

			employeeMap = new HashMap();
			
			//pstmt = conn.prepareStatement(emailQuery);
			pstmt = db2Conn.prepareStatement(emailQuery);
			
			emailRS = pstmt.executeQuery();		
			
			while (emailRS.next()) {	
				employeeMap.put(String.valueOf(emailRS.getInt(1)), (String)emailRS.getString(2));	
				
				//System.out.println("---"+String.valueOf(emailRS.getInt(1)) + (String)emailRS.getString(2));
			}
			emailRS = null;
			pstmt = null;
			
			/*String phoneQuery = "select distinct a.value, b.value from affiliates_db.dbo.safe_attributes a, " +
			"affiliates_db.dbo.safe_attributes b " +
			"where a.aid=b.aid and a.name='emp_id' and b.name='phone'";*/
			
			String phoneQuery = "select distinct emb_person_id, phone from " +
					"affiliates_dw.affiliates_safe_attributes where " +
					"emb_person_id != 0 and phone != ''";
			
			employeePhone = new HashMap();
			
			//pstmtPhone = conn.prepareStatement(phoneQuery);
			pstmtPhone = db2Conn.prepareStatement(phoneQuery);
			
			phoneRS = pstmtPhone.executeQuery();		
			
			while (phoneRS.next()) {	
				employeePhone.put(String.valueOf(phoneRS.getInt(1)), (String)phoneRS.getString(2));	
		
			}
			phoneRS = null;
			pstmtPhone = null;
			
			//String barcodeQuery = "select distinct v.employee_id, " +
				//	"v.barcode from idcard_db.dbo.idcard_v v";
			
			String barcodeQuery = "select distinct emb_employee_id, bar_code " +
					"from affiliates_dw.rosetta_stone where emb_person_id != 0 " +
					"and bar_code != 0";
			
			employeeBarcode = new HashMap();
			
			pstmt = db2Conn.prepareStatement(barcodeQuery);
			
			barcodeRS = pstmt.executeQuery();		
			String barcodeVal = null;
			String keyVal = null;
			while (barcodeRS.next()) {	
				keyVal = (String)barcodeRS.getString(1);
				barcodeVal = (String)barcodeRS.getString(2);


		        //System.out.println("barcodeVal:"+barcodeVal+"----"+barcodeVal.length());
				
		        if(keyVal != null && barcodeVal != null)
		        	employeeBarcode.put(keyVal.trim(), barcodeVal.trim());	
		
			}
			barcodeRS = null;
			pstmt = null;
			
			//String key;
			
			/*
			for (Iterator it = employeeBarcode.entrySet().iterator(); it.hasNext();) {
				Map.Entry entry = (Map.Entry) it.next();
				//key = (String)entry.getKey();
				
				System.out.println(entry.getKey() + " value : "+entry.getValue());
				
			}*/
			
			rs = stmt.executeQuery(query);
			
			ResultSetMetaData rsms = rs.getMetaData();
			int numcol = rsms.getColumnCount();
			String tmpStr = "";
			String newId = "";
			String oldId = "";

			recordBuffer = new Vector();
			
			int counter = 1; 
			String empId = null;
			//Date dateCol = null;
			//boolean isBefore;
			String employeeId = null;
			while (rs.next()) {
				newId = rs.getString(1);
	
				
				if (!newId.equals(oldId)) {	
					//ok, now we've come to a new ID, so write
					//the old ID if it was unique; otherwise, 
					//process multiple until we only have one

					//remove student title codes here
					
					//recordBuffer = removeStudentTitles(recordBuffer, typeCodes);
					
					//perform filter operations here
					
					recordBuffer = filter3a(recordBuffer, typeCodes);					
					recordBuffer = filter4a(recordBuffer);					
					recordBuffer = filter4b(recordBuffer, typeCodes);					
					recordBuffer = filter4c(recordBuffer, typeCodes);					
					recordBuffer = filter4d(recordBuffer, typeCodes);
					//recordBuffer = filter4a(recordBuffer);	
					
					if (recordBuffer.size() < 2) {
						if (recordBuffer.size() > 0) {
						
							//if we only have 1 record (removed all duplicates), do stuff here
							
							String tmp = (String) recordBuffer.elementAt(0);

							StringBuffer writeOut = new StringBuffer();
							empId = (String)parseRecord(tmp, 0);

							//write out the employee ID
							writeOut.append(parseRecord(tmp, 0) + "\t");
							
							//write out the employee name
							writeOut.append(parseRecord(tmp, 1) + "\t");
							
							//write out the employment status code:
							//--if emeritus, write out "A" for active
							if (isEmeritusTitleCode(parseRecord(tmp, 4))) {
								writeOut.append("A\t");
							} else {
								writeOut.append(parseRecord(tmp, 2) + "\t");	
							}
							
							//write out the student status code
							writeOut.append(parseRecord(tmp, 3) + "\t");

							//write out the patron-type code (mapped from title codes)
							if (typeCodes.get(parseRecord(tmp, 4)) != null) {
								
								//exeption: if department code=265 (affiliation code=29), 
								//then assign patron type=16
								if (parseRecord(tmp, 6).equals("265")) {
								    	writeOut.append("16\t");
								} else {
									//if department_code != 265, then do this:
									
									if (typeCodes.get(parseRecord(tmp, 4)) == null) {
										typeCodeNotFound.append(tmp);
										writeOut.append("0\t");
									} else {
										writeOut.append(
											typeCodes.get(parseRecord(tmp, 4)) + "\t");
									}
								}
							} else {
								//write a "0" if title code is not found in the mappings.
								writeOut.append("0" + "\t");
								titleCodeNotFound.append(tmp);
							}

							//write out the title name
							writeOut.append(parseRecord(tmp, 5) + "\t");

							//write the affiliation code (mapped from department-code)
							if (affiliationCodes.get(parseRecord(tmp, 6))
								!= null) {
								writeOut.append(
									affiliationCodes.get(parseRecord(tmp, 6))
										+ "\t");
							} else {
								writeOut.append("0" + "\t");
								affiliationCodeNotFound.append(tmp);
							}

							//write out the department name
							writeOut.append(parseRecord(tmp, 7)+"\t");
							
							//write out the mailcode
							if(parseRecord(tmp, 9) != null && parseRecord(tmp, 9).length() > 0)
								writeOut.append(parseRecord(tmp, 9, false)+"\t");
							else
								writeOut.append("none\t");

							//write out the phone
/*							if(parseRecord(tmp, 10) != null && parseRecord(tmp, 10).length() > 0)
								writeOut.append(parseRecord(tmp, 10)+"\t");
							else
								writeOut.append("none\t");*/
							
							if(empId != null && employeePhone.containsKey(empId) && employeePhone.get(empId) != null
									&& ((String)employeePhone.get(empId)).length() > 0) {
								writeOut.append((String)employeePhone.get(empId)+"\t");	
							} else {
								writeOut.append("none\t");
							}								
							//write out the email
/*							if(parseRecord(tmp, 11) != null && parseRecord(tmp, 11).length() > 0)
								writeOut.append(parseRecord(tmp, 11)+"\t");
							else
								writeOut.append("none\t");*/

							//write out the email from affiliates

							if(empId != null && employeeMap.containsKey(empId) && employeeMap.get(empId) != null
									&& ((String)employeeMap.get(empId)).length() > 0) {
								writeOut.append((String)employeeMap.get(empId)+"\t");	
							} else {
								writeOut.append("none\t");
							}		
							
							//write out the barcode
							/*
							if(parseRecord(tmp, 12) != null && parseRecord(tmp, 12).length() > 0)
								writeOut.append(parseRecord(tmp, 12));
							else
								writeOut.append("none");
							*/

							employeeId = (String)parseRecord(tmp, 12, false);
							
							//System.out.println("employeeId:"+employeeId + " -- "+empId + 
								//	"..."+parseRecord(tmp, 12, false));
							
							if(employeeId != null && employeeBarcode.containsKey(employeeId) && 
									employeeBarcode.get(employeeId) != null
									&& ((String)employeeBarcode.get(employeeId)).length() > 0) {
								//System.out.println("hey: "+(String)employeeBarcode.get(employeeId));
								writeOut.append((String)employeeBarcode.get(employeeId));	
							} else {
								writeOut.append("none");
								//System.out.println("this one has no barcode:"+employeeId);
							}
				
							
							pw.write(writeOut.toString() + "\n");
							empId = null;		
							employeeId = null;
						}
						
					} else {
						//if this is still a duplicate, dump it into another file
						for (int i = 0; i < recordBuffer.size(); i++) {
							moreDuplicates.append((String) recordBuffer.elementAt(0));
						}
					}

					//clear the Vector for use with new records
					recordBuffer = new Vector();
				}
				
				
				
				oldId = new String(newId);

				StringBuffer bufferEntry = new StringBuffer("");

				bufferEntry.append(newId);
				bufferEntry.append("\t");

				for (int i = 2; i <= numcol; i++) {
					//tmpStr = rs.getString(i).trim();
					tmpStr = rs.getString(i);
					if (tmpStr != null) {
						bufferEntry.append(tmpStr.trim());
					}
					/*else
						bufferEntry.append("N/A");*/
					bufferEntry.append("\t");
					
				}
				bufferEntry.append("\n");
				String record = bufferEntry.toString();

				//--check if title code is valid
				boolean isValid = isValidCode(typeCodes, record);

				//--filter out records that are not staff, faculty, post-doc, 
				//or student-title codes (these get filtered out later)
				if (isValid) {
					recordBuffer.addElement(record);
				} else {
					//if it's invalid, write to file
					//a record is invalid if title code does not fall in either:
					//the staff, faculty, or post-doc groups. It is also invalid
					//if code falls in the Student Position list of title codes.
					
					//if it's a studentTitleCode, don't write it to file
					if (!isStudentTitleCode(typeCodes, record)) invalidRecords.append(record);
				}
			}
			
		} catch (SQLException ex) {
			System.err.println("Exception: " + ex);
	/*		System.out.println("Exception in fullquery_employee.java : " + ex);
	   		StringWriter sw = new StringWriter();
	 	    ex.printStackTrace(new PrintWriter(sw));
	 	    System.out.println("Stack: "+ sw.toString());*/
			//ex.printStackTrace(System.out);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (emailRS != null)
					emailRS.close();		
				if (phoneRS != null)
					phoneRS.close();	
				if (stmt != null)
					stmt.close();		
				//if (conn != null)
					//conn.close();
				if (pstmt != null)
					pstmt.close();
				if (pstmtPhone != null)
					pstmtPhone.close();
				if (db2Conn != null)
					db2Conn.close();
				employeeMap = null;
				employeePhone = null;
				employeeBarcode = null;
			} catch (SQLException e) {
			}
		}
	}

	private static Vector recordBuffer;
	private static String outputLocation;
	
	private static StringBuffer filter3a = new StringBuffer();
	private static StringBuffer filter4a = new StringBuffer();	
	private static StringBuffer filter4b = new StringBuffer();	
	private static StringBuffer filter4c = new StringBuffer();
	private static StringBuffer filter4d = new StringBuffer();
	private static StringBuffer titleCodeNotFound = new StringBuffer();
	private static StringBuffer typeCodeNotFound = new StringBuffer();
	private static StringBuffer affiliationCodeNotFound = new StringBuffer();
	private static StringBuffer moreDuplicates = new StringBuffer();
	private static StringBuffer invalidRecords = new StringBuffer();
	
	public static final String STUDENT_STAFF_TITLE_CODES = "'771', '772', '843', '1506', '1630', '1631', '1728', '2077', '2220', '2221', '2709', '2723', '2724', '2725', '2728', '2738', '3220', '3394', '3240', '3296'";
    
    public static final String EMERITUS_TITLE_CODES = "'1132', '1620', '1621', '3249', '3800'";
	public static final String EMP_STUDENT_STATUS_CODE = "'1', '3'";
	public static final String SEPARATED_CODES = "'S', 'K'";
	public static final String BLOCKED_STATUS_CODES = "'K'";
	
}


/*
This is a sample query that can be used for debugging purposes:

----------------FULL--------------------
SELECT DISTINCT 
p.emb_person_id EMPID, 
p.emb_employee_name NAME, 
p.emp_employment_status_code, 
p.emp_student_status_code, 
p1.app_title_code TITLE_CODE, 
p1.app_title_name, 
p1.app_department_code DEPT_CODE, 
p1.app_department_name, 
p.emp_student_status_code, 
ph.employee_mail_code MAIL_CODE,
ph.employee_office_phone PHONE,
ph.employee_email EMAIL 
FROM 
dbo.p_employee p, 
dbo.p_appointment p1, 
dbo.p_distribution p2,
phone.dbo.employee ph 
WHERE 
ph.emb_employee_number = p.emb_person_id AND (
((p.emp_student_status_code = '1') or 
(p.emp_student_status_code = '3')) AND 
(p1.emb_id = p.emb_id) AND 
(p2.emb_id = p.emb_id) AND 
(p2.app_appointment_number = p1.app_appointment_number)  
)
ORDER BY 
EMPID, p.emb_employee_name


--------FULL ACTIVE---------------
SELECT DISTINCT 
p.emb_person_id EMPID, 
p.emb_employee_name NAME, 
p.emp_employment_status_code, 
p.emp_student_status_code, 
p1.app_title_code TITLE_CODE, 
p1.app_title_name, 
p1.app_department_code DEPT_CODE, 
p1.app_department_name, 
p.emp_student_status_code 
FROM 
dbo.p_employee p, 
dbo.p_appointment p1, 
dbo.p_distribution p2 
WHERE 

((p.emp_student_status_code IN ('1', '3')) OR
((p.emp_student_status_code = '4') AND (p1.app_title_code IN ('771', '772', '843', '1506', '1630', '1631', '1728', '2077', '2220', '2221', '2709', '2723', '2724', '2725', '2728', '2738', '3220', '3394', '3240', '3296')) ))


AND ((p1.app_title_code <> '4011') OR ((p1.app_title_code = '4011') AND (p.emp_student_status_code = '1')))


AND (p1.emb_id = p.emb_id)
AND (p2.emb_id = p.emb_id)
AND (p2.app_appointment_number = p1.app_appointment_number)

AND ((p.emp_employment_status_code NOT IN ('S', 'I', 'K')) OR 
(p1.app_title_code IN ('1132', '1620', '1621', '3249', '3800')))

and p.emb_person_id=370707

ORDER BY 
EMPID, p.emb_employee_name 

----
select * from dbo.p_employee where emb_employee_name like 'RIGOLI%'
*/







