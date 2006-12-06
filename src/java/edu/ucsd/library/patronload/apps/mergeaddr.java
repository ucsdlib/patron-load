package edu.ucsd.library.patronload.apps;

/*
 * mergeaddr.java
 *
 * This is the Java port of "mergeaddr.pl", the perl version
 *
 * Created on June 4, 2002, 10:15 AM
 */

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import edu.ucsd.library.util.TextUtils;

/**
 * @author Joseph Jesena
 */
public class mergeaddr {

    /** Creates new mergeaddr */
    public mergeaddr(String str) {
    }

    /**
     * Method to merge multiple lines of student addresses into one line
     * 
     * @param fileToRead
     *            The raw data from the database (tab-delimited) file
     * @param fileToWrite
     *            The file to write the merged results to
     */
    public static void mergeAddresses(String fileToRead, String fileToWrite) {

        BufferedReader in = null;
        PrintWriter pw = null;

        try {
            in = new BufferedReader(new FileReader(fileToRead));
            pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(
                    fileToWrite)));
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

        // Notes:
        // ---------------------------------------------------------
        // pm = permanent address
        // cm = current address
        // pt = permanent telephone
        // ct = current telephone

        // variable to denote if parsing is currently at top line
        boolean top = true;

        String lineIn = "?";
        String newid = "?";
        String lastid = "?";
        String name = "?";
        String ssn = "?";
        String regstat = "?";
        String lastenroll = "?";
        String level = "?";
        String dept = "?";
        String atype = "?";
        String pm = "?\t ";
        String cm = "?\t ";
        String pt = "?";
        String ct = "?";

        String startdate = "?";
        String stopdate = "?";
        String line1 = "?";
        String line2 = "?";
        String line3 = "?";
        String line4 = "?";
        String city = "?";
        String tel1 = "?";
        String tel2 = "?";
        String tel3 = "?";
        String tel4 = "?";
        String state = "?";
        String zip = "?";
        String country = "?";
        String email = "?";
        String barcode = "?";

        String activePM = "?";
        String activeCM = "?";

        boolean foundActiveCM = false;
        boolean foundActivePM = false;

        try {

            // keep going while there are still lines
            while (((lineIn = in.readLine()) != null)
                    && !(lineIn.trim().equals("?"))) {
                lineIn = lineIn.trim();

                // get the student ID of this record
                try {
                    newid = lineIn.substring(0, lineIn.indexOf("\t"));
                } catch (StringIndexOutOfBoundsException ex1) {
                    System.out.println("Error: expected input not received.");
                    System.exit(1);
                }

                // convert ID to lowercase
                newid = newid.trim().toLowerCase();

                if (!top && !lastid.equals(newid)) {

                    //--use addresses that have no stop dates (active) as the
                    // priority, otherwise pick
                    //the last CM and PM address

                    if (foundActiveCM) {
                        cm = activeCM;
                    }

                    if (foundActivePM) {
                        pm = activePM;
                    }

                    // write the record to stream
                    pw.write(lastid + "\t" + name + "\t" + ssn + "\t" + regstat
                            + "\t" + lastenroll + "\t" + level + "\t" + dept
                            + "\t" + pm + "\t" + cm + "\t" + email + "\t"
                            + barcode + "\n");

                    // clear the variables for the next record
                    pm = "?\t ";
                    cm = "?\t ";
                    pt = "?";
                    ct = "?";

                    activePM = "?";
                    activeCM = "?";
                    foundActiveCM = false;
                    foundActivePM = false;

                }

                top = false;

                // pad the tabs with a space so that the tokenizer
                // can work properly
                lineIn = TextUtils.strReplace(lineIn, "\t", "\t ");

                // split apart the new record (common fields will be
                // overwritten but should be identical)
                StringTokenizer st = new StringTokenizer(lineIn, "\t", false);

                //NOTE: Try using String.split()   -- or get rid of trim()
                
                
                
                
                // remove any initial or trailing white space from each
                // variable

                lastid = st.nextToken().trim().toLowerCase();
                name = st.nextToken().trim();
                ssn = st.nextToken().trim();
                regstat = st.nextToken().trim();
                lastenroll = st.nextToken().trim();
                level = st.nextToken().trim();
                dept = st.nextToken().trim();
                atype = st.nextToken().trim();
                startdate = st.nextToken().trim();
                stopdate = st.nextToken().trim();
                line1 = st.nextToken().trim();
                line2 = st.nextToken().trim();
                line3 = st.nextToken().trim();
                line4 = st.nextToken().trim();
                city = st.nextToken().trim();
                tel1 = st.nextToken().trim();
                tel2 = st.nextToken().trim();
                tel3 = st.nextToken().trim();
                tel4 = st.nextToken().trim();
                state = st.nextToken().trim();
                zip = st.nextToken().trim();
                country = st.nextToken().trim();
                email = st.nextToken().trim();
                barcode = st.nextToken().trim();

                // blank out country if it is US (default)
                if (country.equals("US")) {
                    country = "?";
                }

                // check the address type and save it
                if (atype.equals("PM")) {
                    pm = line1;

                    if (!line2.equals("?")) {
                        pm = pm + "$" + line2;
                    }
                    if (!line3.equals("?")) {
                        pm = pm + "$" + line3;
                    }
                    if (!line4.equals("?")) {
                        pm = pm + line4;
                    }

                    pt = tel1 + tel2 + tel3;
                    if (pt.equals("?"))
                        pt = " ";
                    pm = pm + "  $" + city + ", " + state + " " + zip + " " + country
                            + "\t" + pt;

                    if (stopdate.equals("?")) {
                        foundActivePM = true;
                        activePM = pm;
                    }
                }

                if (atype.equals("CM")) {
                    cm = line1;
                    if (!line2.equals("?")) {
                        cm = cm + "$" + line2;
                    }
                    if (!line3.equals("?")) {
                        cm = cm + "$" + line3;
                    }
                    if (!line4.equals("?")) {
                        cm = cm + line4;
                    }

                    ct = tel1 + tel2 + tel3;
                    if (ct.equals("?"))
                        ct = " ";
                    cm = cm + "  $" + city + ", " + state + " " + zip + " " + country
                            + "\t" + ct;

                    if (stopdate.equals("?")) {
                        foundActiveCM = true;
                        activeCM = cm;
                    }
                }

                // convert the level to its appropriate value
                if (level.equals("U"))
                    level = "3";
                if (level.equals("G"))
                    level = "2";
                if (level.equals("M"))
                    level = "2";
                if (level.equals("P"))
                    level = "2";
            }

            //--use addresses that have no stop dates (active) as the priority,
            // otherwise pick
            //the last CM and PM address

            if (foundActiveCM) {
                cm = activeCM;
            }

            if (foundActivePM) {
                pm = activePM;
            }

            // write the very last record from the input to the stream
            pw.write(lastid + "\t" + name + "\t" + ssn + "\t" + regstat + "\t"
                    + lastenroll + "\t" + level + "\t" + dept + "\t" + pm
                    + "\t" + cm + "\t" + email + "\t" + barcode + "\n");

        } catch (IOException e) {
            System.out.println(e);
        }

        if (pw != null)
            pw.close();
        if (in != null) {
            try {
                in.close();
            } catch (IOException ioe) {
            }
        }
    }

    /**
     * Main method of application
     * 
     * @param args
     *            the command line arguments args[0] = fileToRead args[1] =
     *            fileToWrite
     */
    public static void main(String args[]) {

        if ((args == null) || (args.length < 2)) {
            System.out
                    .println("\nSyntax: java mergeaddr [fileToRead] [fileToWrite]");
        } else {
            mergeAddresses(args[0], args[1]);
        }
    }
}