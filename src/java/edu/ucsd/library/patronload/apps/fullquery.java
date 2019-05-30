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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.httpclient.methods.GetMethod;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;

import edu.ucsd.library.shared.Http;
import edu.ucsd.library.util.FileUtils;
import org.apache.commons.httpclient.methods.GetMethod;

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
            getToken(pathToProperties);
            getPreferredName(pathToProperties);
            //getRawData(pathToProperties, pw);
            //getGradStudentData(pathToProperties, pw);

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

    public static void getPreferredName(String filePath) {
      PrintWriter printWriter = null;
      try {
          GetMethod rdfGet = null;
          String body = null;
          FileReader reader = new FileReader(filePath+"access_token.txt");
          JSONParser jsonParser = new JSONParser();
          JSONObject jsonObject = (JSONObject)jsonParser.parse(reader);
          String token = jsonObject.get("access_token").toString();
          rdfGet = new GetMethod("https://api.ucsd.edu:8243/display_name_info/v1/students/preferred_names");          
          rdfGet.setRequestHeader("Accept", "application/json");
          rdfGet.setRequestHeader("Authorization", "Bearer "+token);
          body = Http.execute( rdfGet );
          if(body != null) {
              printWriter = new PrintWriter(new BufferedOutputStream(new FileOutputStream(filePath+"students_preferred_name.txt")));            
              printWriter.print(body);
          }          
      } catch (Exception e) {
          e.printStackTrace();
      } finally {
          try {
              printWriter.close();
          } catch (Exception e) {}
      }
    }
  
    public static void getToken(String pathToProperties) {
        Process process = null;
        PrintWriter printWriter = null;
        try {
            process = Runtime.getRuntime().exec(pathToProperties+"getAccessToken.sh");
            InputStream inputStream = process.getInputStream();
            printWriter = new PrintWriter(new BufferedOutputStream(new FileOutputStream(
                pathToProperties+"access_token.txt")));
            printWriter.print(convert(inputStream));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                process.destroy();
                printWriter.close();
            } catch (Exception e) {}
        }
    }

    public static String convert(InputStream inputStream) throws IOException{
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int length;
      while ((length = inputStream.read(buffer)) != -1) {
          result.write(buffer, 0, length);
      }
      return result.toString("UTF-8");
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

            String query = 	"select S.stu_pid, S.stu_name, '' as ssn, " +
            				"T.stt_registration_status_code, T.trm_term_code as last_enrolled, " +
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
            				"I.student_barcode, E.em_address_type, SI.id from student_db.s_student S " +
            				"inner join student_db.s_student_term T on S.stu_pid = T.stu_pid and " +
            				trm_term_code + " T.stt_major_primary_flag = 'Y' and " +
            				"stt_registration_status_code in ('EN', 'RG') and T.stt_academic_level in ('UN') " +
            				"inner join student_db.s_address A on T.stu_pid = A.stu_pid and " +
            				"(adr_address_type = 'CM' or adr_address_type = 'PM') left outer join student_db.s_email E " +
            				"ON S.stu_pid = E.stu_pid and (E.em_address_type = 'EMC' or E.em_address_type = 'EMH' or E.em_address_type is null) " +
            				"and (E.em_end_date is null or E.em_end_date !< current date) LEFT OUTER JOIN " +
            				"affiliates_dw.rosetta_stone_barcode_v I ON S.stu_pid = I.stu_pid " +
            				"LEFT JOIN affiliates_dw.affiliates_safe_attributes SA ON S.stu_pid = SA.pid " +
            				"LEFT JOIN affiliates_dw.system SI ON SA.aid = SI.aid and SI.system_id = 41 order by S.stu_pid, A.adr_start_date, A.adr_end_date";

           /*
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
            		"I.student_barcode, E.em_address_type, SI.id from student_db.s_student_term T, " +
            		"student_db.s_address A, " +
            		"(student_db.s_student S LEFT OUTER JOIN student_db.s_email E ON " +
            		"S.stu_pid = E.stu_pid) LEFT OUTER JOIN affiliates_dw.rosetta_stone_barcode_v I " +
            		"ON S.stu_pid = I.stu_pid "+
            		"LEFT JOIN affiliates_dw.affiliates_safe_attributes SA ON S.stu_pid = SA.pid " +
            		"LEFT JOIN affiliates_dw.system SI ON SA.aid = SI.aid and SI.system_id = 41 " +
            		"where (S.stu_pid = T.stu_pid) and " +
            		trm_term_code + " T.stt_major_primary_flag = 'Y' and " +
            		"T.stu_pid = A.stu_pid and (adr_address_type = 'CM' or " +
            		"adr_address_type = 'PM') and stt_registration_status_code in " +
            		"('EN', 'RG') and T.stt_academic_level in ('UN') and (E.em_address_type = 'EMC' or E.em_address_type = 'EMH' or E.em_address_type is null)and " +
            		//"E.em_address_line like '%ucsd.edu%' and " +
            		"(E.em_end_date is null or E.em_end_date !< current date) " +
            		"order by S.stu_pid, A.adr_start_date, A.adr_end_date ";
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
     * Method to retreive Grad Students data from database and output to raw file.
     * 
     * @param pathToProperties
     *            Path to the properties file
     * @param fileToWrite
     *            Path to the file to write results to
     */
    public static void getGradStudentData(String pathToProperties, PrintWriter pw) {

        if (!pathToProperties.equals("")) {
            pathToProperties += File.separator;
        }

        // load the properties file to get the current quarter code
        Properties myProp = null;
        try {
            myProp = FileUtils.loadProperties(pathToProperties
                    + "patron_load.properties");
        } catch (IOException ioe) {
            System.out.println("Error loading properties file in fullquery.getGradStudentData!");
            //System.exit(1);
            return;
        }

        String trm_term_code = "";
        String term = (String) myProp.get("quartercode");
        term = term.trim();

        
        trm_term_code = "T.trm_term_code = '" + term + "' and ";
        String year = term.substring(2, term.length());
        
        String db2Driver = (String) myProp.get("db2driver");
		String db2Username = (String) myProp.get("db2username");
		String db2Password = (String) myProp.get("db2password");
		String db2Connection = (String) myProp.get("db2connection");
		
		if (term.toUpperCase().startsWith("SU")) {
			trm_term_code = "(";
            trm_term_code += "(T.trm_term_code = 'S1" + year + "') or ";
            trm_term_code += "(T.trm_term_code = 'S2" + year + "') or ";
            trm_term_code += "(T.trm_term_code = 'S3" + year + "') or ";
            trm_term_code += "(T.trm_term_code = 'FA" + year + "') or ";
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
			
            stmt = db2Conn.createStatement();

            String query = "select S.stu_pid, S.stu_name, '' as ssn, " +
            		"T.stt_registration_status_code, T.trm_term_code as last_enrolled, " +
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
            		"I.student_barcode, E.em_address_type, SI.id from student_db.s_student S " +
            		"inner join student_db.s_student_term T on S.stu_pid = T.stu_pid and " +
            		trm_term_code+" T.stt_major_primary_flag = 'Y' and stt_registration_status_code in ('EN', 'RG') and " +
            		"T.stt_academic_level in ('GR','MD','PH') inner join student_db.s_address A on " +
            		"T.stu_pid = A.stu_pid and (adr_address_type = 'CM' or adr_address_type = 'PM') " +
            		"left outer join student_db.s_email E ON S.stu_pid = E.stu_pid and (E.em_address_type = 'EMC' " +
            		"or E.em_address_type = 'EMH' or E.em_address_type is null) and (E.em_end_date is null or " +
            		"E.em_end_date !< current date) LEFT OUTER JOIN affiliates_dw.rosetta_stone_barcode_v I " +
            		"ON S.stu_pid = I.stu_pid LEFT JOIN affiliates_dw.affiliates_safe_attributes SA " +
            		"ON S.stu_pid = SA.pid LEFT JOIN affiliates_dw.system SI ON SA.aid = SI.aid and SI.system_id = 41 " +
            		"order by S.stu_pid, A.adr_start_date, A.adr_end_date";
            
            /*
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
            		"I.student_barcode, E.em_address_type, SI.id from student_db.s_student_term T, " +
            		"student_db.s_address A, " +
            		"(student_db.s_student S LEFT OUTER JOIN student_db.s_email E ON " +
            		"S.stu_pid = E.stu_pid) LEFT OUTER JOIN affiliates_dw.rosetta_stone_barcode_v I " +
            		"ON S.stu_pid = I.stu_pid "+
            		"LEFT JOIN affiliates_dw.affiliates_safe_attributes SA ON S.stu_pid = SA.pid " +
            		"LEFT JOIN affiliates_dw.system SI ON SA.aid = SI.aid and SI.system_id = 41 " +
            		"where (S.stu_pid = T.stu_pid) and " +
            		trm_term_code + " T.stt_major_primary_flag = 'Y' and " +
            		"T.stu_pid = A.stu_pid and (adr_address_type = 'CM' or " +
            		"adr_address_type = 'PM') and stt_registration_status_code in " +
            		"('EN', 'RG') and T.stt_academic_level in ('GR','MD','PH') and "+
            		"(E.em_address_type = 'EMC' or E.em_address_type = 'EMH' or E.em_address_type is null) and " +
            		"(E.em_end_date is null or E.em_end_date < current date) " +
            		"order by S.stu_pid, A.adr_start_date, A.adr_end_date ";

            /* query befor new db2 upgrade 
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
    		"I.student_barcode, E.em_address_type, SI.id from student_db.s_student_term T, " +
    		"student_db.s_address A, " +
    		"(student_db.s_student S LEFT OUTER JOIN student_db.s_email E ON " +
    		"S.stu_pid = E.stu_pid) LEFT OUTER JOIN affiliates_dw.rosetta_stone_barcode_v I " +
    		"ON S.stu_pid = I.stu_pid "+
    		"LEFT JOIN affiliates_dw.affiliates_safe_attributes SA ON S.stu_pid = SA.pid " +
    		"LEFT JOIN affiliates_dw.system SI ON SA.aid = SI.aid and SI.system_id = 41 " +
    		"where (S.stu_pid = T.stu_pid) and " +
    		trm_term_code + " T.stt_major_primary_flag = 'Y' and " +
    		"T.stu_pid = A.stu_pid and (adr_address_type = 'CM' or " +
    		"adr_address_type = 'PM') and stt_registration_status_code in " +
    		"('EN', 'RG') and T.stt_academic_level in ('GR','MD','PH') and "+
    		"(E.em_address_type = 'EMC' or E.em_address_type = 'EMH' or E.em_address_type is null) and " +
    		"(E.em_end_date is null or E.em_end_date < current date) " +
    		"order by S.stu_pid, A.adr_start_date, A.adr_end_date ";         
            */
            try {
               
                FileUtils.confirmDir(marcFilesDir);
                FileUtils.stringToFile(query, marcFilesDir
                        + "full_query_all_grad_students.sql");
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