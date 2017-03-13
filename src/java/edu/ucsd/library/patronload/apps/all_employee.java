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
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class all_employee {

	/**
	 * @param args the command line arguments
	 * args[0] = path to affiliations properties file
	 * args[1] = path to patron type codes properties file
	 * args[2] = path to original properties file
	 * args[3] = file to write output to
	 */
	public static void main(String args[]) {

		if ((args == null) || (args.length < 5)) {
			System.out.println(
				"\nSyntax: java all_employee [affiliations properties file] [patron type codes properties file] [original properties file] [fileToWrite]");
		} else {
			grabData(args[0], args[1], args[2], args[3]);
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
	 */
	public static void grabData(
		String props1,
		String props2,
		String props3,
		String fileToWrite) {

		outputLocation = fileToWrite.substring(0, fileToWrite.lastIndexOf(File.separator) + 1);
		
		PrintWriter pw = null;

		//--- Create the file stream here to output to file
		try {
			
			pw =
				new PrintWriter(
					new BufferedOutputStream(
						new FileOutputStream(fileToWrite)));

			getRawData(props1, props2, props3, pw);

			if (pw != null)
				pw.close();


		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}



	/**
	 * Method to retreive data from database and output to raw file.
	 * @param props1 Path to affiliation properties file
	 * @param props2 Path to type codes properties file
	 * @param props3 Path to original properties file
	 * @param fileToWrite Path to the file to write results to
	 */
	public static void getRawData(
		String props1,
		String props2,
		String props3,
		PrintWriter pw) {

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
			getAllTypeCodes(typeCodes);
			
		} catch (IOException ioe) {
			System.out.println("Error loading properties file!");
			return;
		}

		
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
		
		//Statement stmt = null;
		PreparedStatement stmt = null;
		Connection db2Conn = null;
		ResultSet rs = null;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		String currentDate = format.format(new java.util.Date());
	    try {

			db2Conn = DriverManager.getConnection(
					db2Connection,
					db2Username,
					db2Password);

			//stmt = db2Conn.createStatement();

			String query =
				"SELECT DISTINCT "
					+ "p.emb_person_id EMPID, "
					+ "p.emp_last_name, "
					+ "p.emp_first_name, "
					+ "p1.app_title_name, "
					+ "ph.employee_office_phone PHONE, " 
					+ "ph.employee_email EMAIL, "
					+ "p1.app_department_name, "
					+ "ph.employee_mail_code MAIL_CODE"
					+ " FROM " 
					+ " employee.p_employee_fin_view p LEFT OUTER JOIN phone.employee ph "
					+ " ON ph.emb_employee_number = p.emb_person_id , "
					+ " employee.p_appointment p1, "  
					+ " employee.p_distribution p2 "
					+ " WHERE " 
					+ " ("
					+ "((p.emp_student_status_code IN (" + EMP_STUDENT_STATUS_CODE + ")) OR "
                    + "((p.emp_student_status_code = '4') AND (p1.app_title_code IN (" + NEW_STUDENT_STAFF_TITLE_CODES + ")) )) "

                    + "AND ((p1.app_title_code <> '4011') OR ((p1.app_title_code = '4011') AND((p.emp_student_status_code = '1') OR (p.emp_student_status_code = '3')))) "
					
                    + "AND (p1.emb_person_id = p.emb_person_id) "
					+ "AND (p2.emb_person_id = p.emb_person_id) "
					+ "AND (p2.app_appointment_number = p1.app_appointment_number) "
					
					+ "AND ((p.emp_employment_status_code NOT IN (" + BLOCKED_STATUS_CODES + ")) "
							
						 + "AND ((p.emp_employment_status_code NOT IN (" + SEPARATED_CODES + ")) OR " +
						 "(p1.app_title_code IN (" + EMERITUS_TITLE_CODES + "))" + 

						")) " 
					
					+ ") ORDER BY EMPID, p.emp_last_name";
			
            //System.out.println ("all employee query:"+query+"$$$$$");	
			stmt = db2Conn.prepareStatement(query);
			rs = stmt.executeQuery();			
			StringBuffer writeOut = new StringBuffer();
			String id="", firstName = "", lastName = "", title = "";
			String phone = "", email = "", deptName = "", mailCode = ""; 
			while (rs.next()) {
				id = parseField(rs.getString(1));
				firstName = parseField(rs.getString(2));
				lastName = parseField(rs.getString(3));
				title = parseField(rs.getString(4));
				phone = parseField(rs.getString(5));
				email = parseField(rs.getString(6));
				deptName = parseField(rs.getString(7));
				mailCode = parseField(rs.getString(8));
				
				writeOut.append(id+"\t"+firstName+"\t"+lastName+"\t");
				writeOut.append(title+"\t"+phone+"\t"+email+"\t");
				writeOut.append(deptName+"\t"+mailCode+"\n");											
			}
			pw.write(writeOut.toString());
			
		} catch (SQLException ex) {
			System.err.println("Exception: " + ex);
	
		} finally {
			try {
				if (rs != null)
					rs.close();				
				if (stmt != null)
					stmt.close();			
				if (db2Conn != null)
					db2Conn.close();
			} catch (SQLException e) {
				System.out.println("Close Connection Failure : " + e);
			}
		}
	}

	private static String parseField(String fieldValue) {
		if(fieldValue == "" || fieldValue == null)
			fieldValue = "(null)";			
		return fieldValue.trim();
	}
	
	private static void getAllTypeCodes(Properties props) {
		StringBuffer tmpStringBuffer = new StringBuffer();
		
		for (Enumeration e = props.propertyNames(); e.hasMoreElements();) {
			String value = (String) e.nextElement();
			if(value != null)
				tmpStringBuffer.append("'"+value+"',");
		}
		NEW_STUDENT_STAFF_TITLE_CODES = tmpStringBuffer.toString();
		NEW_STUDENT_STAFF_TITLE_CODES = NEW_STUDENT_STAFF_TITLE_CODES.substring(0, NEW_STUDENT_STAFF_TITLE_CODES.length()-1);
	}
	private static Vector recordBuffer;
	private static String outputLocation;
	
	public static final String STUDENT_STAFF_TITLE_CODES = "'771', '772', '843', '1506', '1630', '1631', '1728', '2077', '2220', '2221', '2709', '2723', '2724', '2725', '2728','2729', '2738', '3220', '3394', '3240', '3296'";
    
    public static final String EMERITUS_TITLE_CODES = "'1132', '1620', '1621', '3249', '3800'";
	public static final String EMP_STUDENT_STATUS_CODE = "'1', '3'";
	public static final String SEPARATED_CODES = "'S', 'K'";
	public static final String BLOCKED_STATUS_CODES = "'K'";
	public static String NEW_STUDENT_STAFF_TITLE_CODES = "";
}







