/*
 * Created on Sep 22, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.ucsd.library.patronload.apps;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

/**
 * @author jjesena
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class QuickQuery {

    public static void main(String[] args) {

        String dbDriver = "com.sybase.jdbc2.jdbc.SybDriver";
        String username = "libgpf";
        String password = "donald";
        String dbUri1 = "jdbc:sybase:Tds:132.239.180.8:2025/student_db";
        String dbUri2 = "jdbc:sybase:Tds:132.239.180.8:2025/sqldse";
        String dbUri3 = "jdbc:sybase:Tds:132.239.180.3:2025/employee";

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(
                    "c:\\result.txt")));

        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }

        try {
            Class.forName(dbDriver).newInstance();
        } catch (Exception E) {
            System.err.println("Error: Unable to load driver: " + dbDriver);
            E.printStackTrace();
            System.exit(1);
        }

        Statement stmt = null;
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(dbUri2, username, password);
            stmt = conn.createStatement();

            // grad 'G' more than 1 barcode - TOTAL
            //String query = "select S.PID9, S.STUDENT_NAME, I.barcode from ADMNFA04G S, proxy_db..idcard_v I  where  S.APCT_DECN='ACC' and  (S.PID9 *= I.pid) and S.PID9 in (select X.PID9 from ADMNFA04G X, proxy_db..idcard_v Y  where  X.APCT_DECN='ACC' and (X.PID9 *= Y.pid) group by X.PID9 having COUNT(Y.barcode) > 1)";
            
            
            // Grads 'G' no barcodes - TOTAL 
            String query = "select S.PID9, S.STUDENT_NAME, I.barcode from ADMNFA04G S, proxy_db..idcard_v I  where  S.APCT_DECN='ACC' and  (S.PID9 *= I.pid) and S.PID9 in (select X.PID9 from ADMNFA04G X, proxy_db..idcard_v Y  where  X.APCT_DECN='ACC' and (X.PID9 *= Y.pid) group by X.PID9 having COUNT(Y.barcode) = 0)";
            
            
            // grad 'M' query
            //String query = "select S.PID9, S.STUDENT_NAME, I.barcode from ADMNFA04M S, proxy_db..idcard_v I  where  S.APCT_DECN='ACC' and  (S.PID9 *= I.pid) and S.PID9 in (select X.PID9 from ADMNFA04M X, proxy_db..idcard_v Y  where  X.APCT_DECN='ACC' and (X.PID9 *= Y.pid) group by X.PID9 having COUNT(Y.barcode) > 1)";

            // grad 'P' query
            //String query = "select S.PID9, S.STUDENT_NAME, I.barcode from ADMNFA04P S, proxy_db..idcard_v I  where  S.APCT_DECN='ACC' and  (S.PID9 *= I.pid) and S.PID9 in (select X.PID9 from ADMNFA04P X, proxy_db..idcard_v Y  where  X.APCT_DECN='ACC' and (X.PID9 *= Y.pid) group by X.PID9 having COUNT(Y.barcode) > 1)";
            
            
            //undergrad query (2 or more barcodes total)
            //String query = "select S.stu_pid, S.stu_name, I.barcode from s_student S, proxy_db..idcard_v I where (S.stu_pid *= I.pid) and S.stu_pid in (select X.stu_pid from s_student X, proxy_db..idcard_v Y where (X.stu_pid *= Y.pid) group by X.stu_pid having COUNT(Y.barcode) > 1)";

            //undergrad query (2 or more barcodes) - FA04   --DOES NOT WORK
            //String query = "select S.stu_pid, S.stu_name, I.barcode from s_student_term T, s_student S, proxy_db..idcard_v I, s_address A where (S.stu_pid = T.stu_pid) and T.trm_term_code = 'FA04' and T.stt_major_primary_flag = 'Y' and (adr_address_type = 'CM' or adr_address_type = 'PM') and stt_registration_status_code in ('EN', 'RG') and (S.stu_pid *= I.pid) and S.stu_pid in ";
            //query += "(select distinct(S2.stu_pid) from s_student S2, proxy_db..idcard_v I2 where (S2.stu_pid *= I2.pid) group by S2.stu_pid having COUNT(I2.barcode) > 1)";

            
            
            
            
            //undergrad query (no barcodes) - total
            //String query = "select S.stu_pid, S.stu_name, I.barcode from s_student S, proxy_db..idcard_v I where (S.stu_pid *= I.pid) and S.stu_pid in (select X.stu_pid from s_student X, proxy_db..idcard_v Y where (X.stu_pid *= Y.pid) group by X.stu_pid having COUNT(Y.barcode) = 0)";

            //undergrad query (no barcodes) - FA04
            //String query = "select distinct(S.stu_pid), S.stu_name from s_student_term T, s_student S, s_address A, s_email E where (S.stu_pid = T.stu_pid) and T.trm_term_code = 'FA04' and T.stt_major_primary_flag = 'Y' and T.stu_pid = A.stu_pid and (adr_address_type = 'CM' or adr_address_type = 'PM') and stt_registration_status_code in ('EN', 'RG') and E.em_address_type = 'EMC' and (E.em_end_date = null or E.em_end_date !< getdate()) and S.stu_pid in ";
            //query += "(select S2.stu_pid from s_student S2, proxy_db..idcard_v I2 where (S2.stu_pid *= I2.pid) group by S2.stu_pid having COUNT(I2.barcode) = 0)";
            
            
            rs = stmt.executeQuery(query);

            ResultSetMetaData rsms = rs.getMetaData();
            int numcol = rsms.getColumnCount();
            while (rs.next()) {

                for (int i = 1; i <= numcol; i++) {
                    String tmpStr = rs.getString(i);

                    if ((tmpStr == null)
                            || (tmpStr.trim().toLowerCase().equals(""))) {
                        // Put in question mark if its null or blank
                        tmpStr = "?";
                    }

                    pw.print(tmpStr);
                    pw.print("\t");
                }
                pw.print("\n");
            }

            pw.flush();
            pw.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (Exception e) {
            }
        }

    }

}