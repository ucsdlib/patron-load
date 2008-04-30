package edu.ucsd.library.patronload.apps;

/*
 * fullquery.java
 *
 * This is the Java port of "darwin.query", the perl version
 *
 * Created on June 18, 2002, 12:32 PM
 */

/**
 * 
 * @author Joseph Jesena
 */

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

import edu.ucsd.library.util.FileUtils;

public class fullquery {

    /** Creates new fullquery */
    public fullquery() {
    }

    /**
     * @param args
     *            the command line arguments args[0] = pathToProperties args[1] =
     *            fileToWrite
     */
    public static void main(String args[]) {

        if ((args == null) || (args.length < 2)) {
            System.out
                    .println("\nSyntax: java fullquery [pathToProperties] [fileToWrite]");
        } else {
            grabData(args[0], args[1]);
        }
    }

    /**
     * Write data out to the file
     * 
     * @param pathToProperties
     *            Path to the properties file
     * @param fileToWrite
     *            Path to the file to write results to
     */
    public static void grabData(String pathToProperties, String fileToWrite) {

        PrintWriter pw = null;

        //--support a collection of 300,000 students, max
        student_ids = new HashMap(300000);

        //--- Create the file stream here to output to file
        try {
            pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(
                    fileToWrite)));

            getRawData(pathToProperties, pw);
            getAcceptedGrads(pathToProperties, pw);

            if (pw != null)
                pw.close();

        } catch (IOException ioe) {
            System.out.println(ioe);
        } finally {
            try {
                pw.close();
            } catch (Exception e1) {
            }
        }
    }

    /**
     * Method to retreive data from database and output to raw file.
     * 
     * @param pathToProperties
     *            Path to the properties file
     * @param fileToWrite
     *            Path to the file to write results to
     */
    public static void getRawData(String pathToProperties, PrintWriter pw) {

        if (!pathToProperties.equals("")) {
            pathToProperties += File.separator;
        }

        // load the properties file to get the current quarter code
        Properties myProp = null;
        try {
            myProp = FileUtils.loadProperties(pathToProperties
                    + "patron_load.properties");
        } catch (IOException ioe) {
            System.out.println("Error loading properties file in fullquery.getRawData!");
            //System.exit(1);
            return;
        }

        String trm_term_code = "";
        String term = (String) myProp.get("quartercode");
        term = term.trim();

        /*
        String dbDriver = (String) myProp.get("dbdriver");
        dbDriver = dbDriver.trim();

        String dbUsername = (String) myProp.get("dbusername");
        dbUsername = dbUsername.trim();

        String dbPassword = (String) myProp.get("dbpassword");
        dbPassword = dbPassword.trim();

        String dbConnection = (String) myProp.get("dbconnection");
        dbConnection = dbConnection.trim();
        */
        
        trm_term_code = "T.trm_term_code = '" + term + "' and ";
        String year = term.substring(2, term.length());
        
        String db2Driver = (String) myProp.get("db2driver");
		String db2Username = (String) myProp.get("db2username");
		String db2Password = (String) myProp.get("db2password");
		String db2Connection = (String) myProp.get("db2connection");
		//System.out.println("db2Username:"+db2Username+ " db2Password"+db2Password);
		//System.out.println("db2Connection:"+db2Connection+ " db2Driver"+db2Driver);
		if (term.toUpperCase().startsWith("SU")) {
            trm_term_code = "(";
            trm_term_code += "(T.trm_term_code = 'S1" + year + "') or ";
            trm_term_code += "(T.trm_term_code = 'S2" + year + "') or ";
            trm_term_code += "(T.trm_term_code = 'S3" + year + "') or ";
            trm_term_code += "(T.trm_term_code = 'SU" + year + "')";
            trm_term_code += ") and ";
        }

        try {
        	Class.forName(db2Driver).newInstance();
            //Class.forName(dbDriver).newInstance();          
        } catch (Exception E) {
            System.err.println("Error: Unable to load driver: " + db2Driver);
            E.printStackTrace();
            System.exit(1);
        }

        Statement stmt = null;
        //Connection conn = null;
        ResultSet rs = null;
        Connection db2Conn = null;
        
        try {

			db2Conn = DriverManager.getConnection(
					db2Connection,
					db2Username,
					db2Password);
			
            // Used TLI to IP/Port Converter to get IP and port
            // http://www.outlands.demon.co.uk/utilities/tli2ip.html
            // TLI: \x000207e984efb4080000000000000000
            /*conn = DriverManager.getConnection(dbConnection, dbUsername,
                    dbPassword);*/
            stmt = db2Conn.createStatement();

            String query = "select S.stu_pid, S.stu_name,'' as ssn, " +
            		"T.stt_registration_status_code, '"+ term +"' as last_enrolled, " +
            		"substr(T.stt_academic_level,1,1) as academic_level, T.maj_major_code, " +
            		"A.adr_address_type, rtrim(substr(char(year(A.adr_start_date)),3,4)) " +
            		"concat rtrim(ltrim(char(month(A.adr_start_date)))) concat " +
            		"rtrim(ltrim(char(day(A.adr_start_date)))) as startdate, " +
            		"rtrim(substr(char(year(A.adr_end_date)),3,4)) concat " +
            		"rtrim(ltrim(char(month(A.adr_end_date)))) concat " +
            		"rtrim(ltrim(char(day(A.adr_end_date)))) as stopdate, " +
            		"A.adr_address_line_1, A.adr_address_line_2, A.adr_address_line_3, " +
            		"A.adr_address_line_4, A.adr_city, substr(A.adr_phone,1,3) " +
            		"as area_code, substr(A.adr_phone,5,3) as exchange, " +
            		"substr(A.adr_phone,9,4) as sqid, char(' ',4) as extension, " +
            		"A.adr_state, A.adr_zip, A.co_country_code, E.em_address_line, " +
            		"I.bar_code from student_db.s_student_term T, " +
            		"student_db.s_address A, " +
            		"(student_db.s_student S LEFT OUTER JOIN student_db.s_email E ON " +
            		"S.stu_pid = E.stu_pid) LEFT OUTER JOIN affiliates_dw.rosetta_stone I " +
            		"ON S.stu_pid = I.stu_pid where (S.stu_pid = T.stu_pid) and " +
            		trm_term_code + " T.stt_major_primary_flag = 'Y' and " +
            		"T.stu_pid = A.stu_pid and (adr_address_type = 'CM' or " +
            		"adr_address_type = 'PM') and stt_registration_status_code in " +
            		"('EN', 'RG') and E.em_address_type = 'EMC' and " +
            		"E.em_address_line like '%ucsd.edu%' and " +
            		"(E.em_end_date is null or E.em_end_date !< current date) " +
            		"order by S.stu_pid, A.adr_start_date, A.adr_end_date ";

            /*
            String query = "select "
                    + "S.stu_pid, "
                    + "S.stu_name, "
                    + "ssn='', "
                    + "T.stt_registration_status_code, "
                    + "last_enrolled = '"
                    + term
                    + "', "
                    + "academic_level = substring(T.stt_academic_level,1,1), "
                    + "T.maj_major_code, "
                    + "A.adr_address_type, "
                    + "startdate=convert(char(12),A.adr_start_date,12), "
                    + "stopdate=convert(char(6), A.adr_end_date,12), "
                    + "A.adr_address_line_1, "
                    + "A.adr_address_line_2, "
                    + "A.adr_address_line_3, "
                    + "A.adr_address_line_4, "
                    + "A.adr_city, "
                    + "area_code = substring(A.adr_phone,1,3), "
                    + "exchange = substring(A.adr_phone,5,3), "
                    + "sqid = substring(A.adr_phone,9,4), "
                    + "extension = convert(char(4), ' '), "
                    + "A.adr_state, A.adr_zip, "
                    + "A.co_country_code, "
                    + "E.em_address_line, "
                    + "I.barcode "

                    + "from "
                    + "s_student_term T, "
                    + "s_student S, "
                    + "s_address A, "
                    + "s_email E, "
                    + "proxy_db..idcard_v I "

                    + "where "
                    + "(S.stu_pid = T.stu_pid) and "
                    + trm_term_code
                    + "T.stt_major_primary_flag = 'Y' and "
                    + "T.stu_pid = A.stu_pid and "
                    + "(adr_address_type = 'CM' or adr_address_type = 'PM') and "
                    + "stt_registration_status_code in ('EN', 'RG') and "
                    + "E.em_address_type = 'EMC' and "
                    + "E.em_address_line like '%ucsd.edu' and "
                    + "(E.em_end_date = null or E.em_end_date !< getdate()) and "
                    + "(S.stu_pid *= E.stu_pid) and " + "(S.stu_pid *= I.pid) "

                    + "order by S.stu_pid, A.adr_start_date, A.adr_end_date ";
            */
            
            try {
                //String dir = pathToProperties + "marc_files" + File.separator;
                FileUtils.confirmDir(marcFilesDir);
                FileUtils.stringToFile(query, marcFilesDir
                        + "full_query_undergrad_student.sql");
            } catch (IOException ioe) {
            }

            //System.out.println(query);
            rs = stmt.executeQuery(query);

            ResultSetMetaData rsms = rs.getMetaData();
            int numcol = rsms.getColumnCount();
            while (rs.next()) {

                for (int i = 1; i <= numcol; i++) {
                    String tmpStr = rs.getString(i);

                    if ((tmpStr == null) || (tmpStr.trim().toLowerCase().equals(""))) {
                        // Put in question mark if its null or blank
                        tmpStr = "?";
                    }
                    
                    if (i == 1) {
                        //--get a list of all the student IDs
                        student_ids.put(tmpStr.toLowerCase().trim(), "");
                    }

                    pw.print(tmpStr);
                    pw.print("\t");
                }
                pw.print("\n");
            }

        } catch (SQLException ex) {
            System.err.println("Exception: " + ex);
            //ex.printStackTrace(System.out);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                /*if (conn != null)
                    conn.close();*/
                if (db2Conn != null)
					db2Conn.close();
            } catch (SQLException e) {
            }
        }
    }

    /**
     * Method to retreive accepted Grad studdent data from database and output
     * to raw file.
     * 
     * @param pathToProperties
     *            Path to the properties file
     * @param fileToWrite
     *            Path to the file to write results to
     */
    public static void getAcceptedGrads(String pathToProperties, PrintWriter pw) {

        if (!pathToProperties.equals("")) {
            pathToProperties += File.separator;
        }

        // load the properties file to get the current quarter code
        Properties myProp = null;
        try {
            myProp = FileUtils.loadProperties(pathToProperties
                    + "patron_load.properties");
        } catch (IOException ioe) {
            System.out.println("Error loading properties file in fullquery.getAcceptedGrads()!");
            //System.exit(1);
            return;
        }

        String term = (String) myProp.get("quartercode");
        term = term.trim();
        String year = term.substring(2);

        /*
        String dbDriver = (String) myProp.get("dbdriver");
        dbDriver = dbDriver.trim();

        String dbUsername = (String) myProp.get("dbusername");
        dbUsername = dbUsername.trim();

        String dbPassword = (String) myProp.get("dbpassword");
        dbPassword = dbPassword.trim();

        String dbConnection = (String) myProp.get("dbconnection2");
        dbConnection = dbConnection.trim();
        */
        
        String db2Driver = (String) myProp.get("db2driver");
		String db2Username = (String) myProp.get("db2username");
		String db2Password = (String) myProp.get("db2password");
		String db2Connection = (String) myProp.get("db2connection");
		
        String adr_info[] = new String[14];

        try {
        	Class.forName(db2Driver).newInstance();
            //Class.forName(dbDriver).newInstance();
        } catch (Exception E) {
            System.err.println("Error: Unable to load driver: " + db2Driver);
            E.printStackTrace();
            System.exit(1);
        }

        Statement stmt = null;
        //Connection conn = null;
        ResultSet rs = null;

        Statement stmt2 = null;
        ResultSet rs2 = null;

        String gradType = "";

        int iterations = 1;
        Connection db2Conn = null;
        
        try {
        	db2Conn = DriverManager.getConnection(
					db2Connection,
					db2Username,
					db2Password);
            //conn = DriverManager.getConnection(dbConnection, dbUsername,
            //        dbPassword);
            //conn2 = DriverManager.getConnection(dbConnection, dbUsername,
            // dbPassword);
            stmt = db2Conn.createStatement();

//           will be uncomment on august 16, and july 13
            if (term.toUpperCase().startsWith("SU")) {
                // iterations = 3; will be uncomment on august 16
                //iterations = 2;
                iterations = 3;
            }

            for (int k = 0; k < iterations; k++) {
                for (int t = 0; t < 3; t++) {
                    //--Do this 3 times: once for grads, another for pharmacy,
                    //--and a third for medical students.

                    switch (t) {
                    case 0:
                        gradType = "G";
                        break;
                    case 1:
                        gradType = "P";
                        break;
                    case 2:
                        gradType = "M";
                        break;
                    }

                    //trm_term_code = "and T.trm_term_code = '" + term + "' ";

                    //--If quarter code is in summer, also download it: S1, S2,
                    // S3
                    if (iterations > 1) {
                        //term = "FA" + year;
                        //term = "S" + (k + 1) + year; uncomment it on august 16
                    	term = "S" + (k + 1) + year;
                    	if(k == 1)
                    		term = "S" + (k + 2) + year;
                    	if(k == 2) {
                    		term = "FA" + year;
                    	}
                      //need to uncomment this on july 13 
                    }
                    //term = "S3" + year;
 
                    
                    String term_admn = "sqldse.ADMN" + term + gradType+"_V";
                    String term_stad = "sqldse.STAD" + term + gradType;
                    
                    String query = "select S.PID9, S.STUDENT_NAME, '' as ssn, '' as regStatusCode, " +
                    		"'" + term + "' as last_enrolled, char(S.STUDENT_LEV,1) as stu_lev, " +
                    		"S.MAJOR_CODE, A.ADDR_TYPE, rtrim(substr(char(year(A.start_date)),3,4)) " +
                    		"concat rtrim(ltrim(char(month(A.start_date)))) concat " +
                    		"rtrim(ltrim(char(day(A.start_date)))) as startdate, " +
                    		"rtrim(substr(char(year(A.end_date)),3,4)) concat " +
                    		"rtrim(ltrim(char(month(A.end_date)))) concat " +
                    		"rtrim(ltrim(char(day(A.end_date)))) as enddate, A.LINE_ADR1, " +
                    		"A.LINE_ADR2, A.LINE_ADR3, A.LINE_ADR4, A.CITY_NAME, A.AREA_CODE, " +
                    		"A.XCHNG_ID, A.SEQ_ID, char('    ',4) as extension, A.STATE_CO, " +
                    		"A.ZIP_CODE, A.CNTRY_CO, E.EM_EMAIL_LINE, I.bar_code from " +
                    		term_stad + " A, (" + term_admn + " S LEFT JOIN " +
                    		"sqldse.PRSNEMAD E ON S.PID9 = E.PID) LEFT JOIN " +
                    		"affiliates_dw.rosetta_stone I ON S.PID9 = I.stu_pid " +
                    		"where S.PID9 = A.PID9 and S.APCT_DECN='ACC' and " +
                    		"E.EM_EMAIL_TYPE='EMC' and E.EM_EMAIL_LINE like '%ucsd.edu%' " +
                    		"order by S.PID9, A.START_DATE, A.END_DATE";
                    // this query gets the student records with a UCSD email
                    // address
                    
                    /*
                    String query = "select " + "S.PID9, " + "S.STUDENT_NAME, "
                            + "ssn='', " + "regStatusCode= '', "
                            + "last_enrolled = '" + term + "', "
                            + "stu_lev=convert(char(1),S.STUDENT_LEV,1), "
                            + "S.MAJOR_CODE, " + "A.ADDR_TYPE, "
                            + "startdate=convert(char(12),A.START_DATE,12), "
                            + "enddate=convert(char(12),A.END_DATE,12), "
                            + "A.LINE_ADR1, " + "A.LINE_ADR2, "
                            + "A.LINE_ADR3, " + "A.LINE_ADR4, "
                            + "A.CITY_NAME, " + "A.AREA_CODE, "
                            + "A.XCHNG_ID, " + "A.SEQ_ID, "
                            + "extension = convert(char(4), '    '), "
                            + "A.STATE_CO, " + "A.ZIP_CODE, " + "A.CNTRY_CO, "
                            + "E.EM_EMAIL_LINE, " + "I.barcode "

                            + "from " + term_admn + " S, " + term_stad + " A, "
                            + "PRSNEMAD E, " + "proxy_db..idcard_v I "

                            + "where " + "S.PID9 = A.PID9 and "
                            + "S.APCT_DECN='ACC' and "
                            + "E.EM_EMAIL_TYPE='EMC' and "
                            + "E.EM_EMAIL_LINE like '%ucsd.edu' and "
                            + "(S.PID9 *= E.PID) and " + "(S.PID9 *= I.pid) "

                            + "order by S.PID9, A.START_DATE, A.END_DATE ";
                    */
                    //System.out.println("k: "+k);
                    //System.out.println("query: "+query);
                    
                    // Used TLI to IP/Port Converter to get IP and port
                    // http://www.outlands.demon.co.uk/utilities/tli2ip.html
                    // TLI: \x000207e984efb4080000000000000000

                    try {

                        try {
                            //String dir = pathToProperties + "marc_files"
                                    //+ File.separator;
                            FileUtils.confirmDir(marcFilesDir);
                            FileUtils.stringToFile(query, marcFilesDir
                                    + "full_query_grad_student_" + gradType + ".sql");
                        } catch (IOException ioe) {
                        }

                        rs = stmt.executeQuery(query);

                        ResultSetMetaData rsms = rs.getMetaData();
                        int numcol = rsms.getColumnCount();

                        while (rs.next()) {
                            
                            boolean isExist = false;                        

                            for (int i = 1; i <= numcol; i++) {

                                // WARNING: result of rs.getString() can be null!
                                String tmpStr = rs.getString(i);

                                if ((tmpStr == null) || (tmpStr.trim().toLowerCase().equals(""))) {
                                    // Make sure its not null or blank
                                    tmpStr = "?";
                                }

                                if ((i == 1) && student_ids.containsKey(tmpStr.trim().toLowerCase())) {
                                    // Don't allow patron if he's already in the list from undergrad
                                    isExist = true;
                                    break;
                                }

                                pw.print(tmpStr);
                                pw.print("\t");
                            }
                            
                            if (!isExist) {
                                pw.print("\n");
                            }
                        } //-- end while loop
                        
                        if (rs != null) {
                            rs.close();
                        }

                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                        //System.out.println("--Oh no! Table was probably
                        // not
                        // found.");
                    }
                } //end for loop
            } //end for loop

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

                if (rs2 != null)
                    rs2.close();
                if (stmt2 != null)
                    stmt2.close();

            } catch (SQLException e) {
            }
        }
    }

    public static void setMarcFilesDir(String mDir) {
    	marcFilesDir = mDir;
    }
    
    private static HashMap student_ids;
    private static String marcFilesDir;
}

/*******************************************************************************
 * Test query:
 * 
 * select
 * 
 * S.stu_pid, S.stu_name, ssn=convert(char(9),' '),
 * T.stt_registration_status_code, last_enrolled = 'SP04', academic_level =
 * substring(T.stt_academic_level,1,1), T.maj_major_code, A.adr_address_type,
 * startdate=convert(char(12),A.adr_start_date,12), stopdate=convert(char(6),
 * A.adr_end_date,12), A.adr_address_line_1, A.adr_address_line_2,
 * A.adr_address_line_3, A.adr_address_line_4, A.adr_city, area_code =
 * substring(A.adr_phone,1,3), exchange = substring(A.adr_phone,5,3), sqid =
 * substring(A.adr_phone,9,4), extension = convert(char(4), ' '), A.adr_state,
 * A.adr_zip, A.co_country_code, E.em_address_line, I.barcode
 * 
 * from s_student_term T, s_student S, s_address A, s_email E,
 * proxy_db..idcard_v I
 * 
 * where
 * 
 * (S.stu_pid = T.stu_pid) and T.trm_term_code = 'SP04' and
 * T.stt_major_primary_flag = 'Y' and T.stu_pid = A.stu_pid and
 * (adr_address_type = 'CM' or adr_address_type = 'PM') and
 * stt_registration_status_code in ('EN', 'RG') and S.stu_pid = E.stu_pid and
 * E.em_address_type = 'EMC' and (S.stu_pid *= pid) and (E.em_end_date = null or
 * E.em_end_date ! < getdate())
 * 
 * order by S.stu_pid, A.adr_start_date, A.adr_end_date
 */