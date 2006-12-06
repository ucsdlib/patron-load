/*
 * Created on Sep 25, 2003
 */
package edu.ucsd.library.patronload.apps;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Vector;

import edu.ucsd.library.util.datastructure.BigHashMap;

/**
 * @author jjesena
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class incquery_employee {

	/**
	 * Generates a new file based on the changes from inFile1 and inFile2
	 * and sent to outFile
	 * @param inFile1 - The old 'master' file 
	 * @param inFile2 - The new 'master' file
	 * @param outFile String - File to send difference to
	 */
	private static void getChanges(String inFile1, //old 
	String inFile2, //new 
	String outFile) {

		BigHashMap bigMapOld = new BigHashMap(3000);
		BigHashMap bigMapNew = new BigHashMap(3000);
        
        //this map keeps track of employeeIDs that have already been marked
        //download - so we know not to download the same employee ID again
        HashMap changes      = new HashMap(100000);

		BufferedReader breader; //reader for the template files
		String line; //line from file

		try {
			//--read in all lines from the OLD full file and insert in the HashMap
			breader = new BufferedReader(new FileReader(inFile1));
			while ((line = breader.readLine()) != null) {

				//insert the employee ID into the hashmap
				bigMapOld.put(fullquery_employee.parseRecord(line, 0), line);
			}
			breader.close();
		} catch (IOException ioe) {
		}

		try {
			//--read in all lines from the NEW full file and insert in the HashMap
			breader = new BufferedReader(new FileReader(inFile2));
			while ((line = breader.readLine()) != null) {

				//insert the employee ID into the hashmap
				bigMapNew.put(fullquery_employee.parseRecord(line, 0), line);
			}
			breader.close();
		} catch (IOException ioe) {
		}

		//--Use a Vector, I don't anticipate having updates taking more than 1MB
		//so there should not be any memory issues.
		Vector changed = new Vector();


        //--Add all the DELETED records here
        try {
            //--go through each line in the OLD full file 
            breader = new BufferedReader(new FileReader(inFile1));
            while ((line = breader.readLine()) != null) {

                String employeeId = fullquery_employee.parseRecord(line, 0);

                if (!bigMapNew.containsKey(employeeId) && !changes.containsKey(employeeId)) {
                    //--tag as being separated, because they have been deleted from the campus DB
                    //changed.addElement(markAsDeleted(line.trim()));
                    changes.put(employeeId, "");
                }
            }
            breader.close();
        } catch (IOException ioe) {
        }


		//--Add all the NEW and DIFFERENT records here
		try {
			//--go through each line in the NEW full file 
			breader = new BufferedReader(new FileReader(inFile2));
			while ((line = breader.readLine()) != null) {

				String employeeId = fullquery_employee.parseRecord(line, 0);
				String oldLine = null;
				Object result = null;

				if (bigMapOld.containsKey(employeeId)) {
					result = bigMapOld.get(employeeId);
					if (result != null)
						oldLine = (String) result;
				}

				if ((result == null || !line.trim().equals(oldLine.trim())) && !changes.containsKey(employeeId) ) {
					//add it to the changed list if line has changed or
					//if line doesn't exist in old list
					changed.addElement(line.trim());
                    changes.put(employeeId, "");
				}
			}
			breader.close();
		} catch (IOException ioe) {
		}


		BufferedWriter bwriter; //writer to the file

		try {
			bwriter =
				new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(outFile)));

			for (int x = 0; x < changed.size(); x++) {
				bwriter.write((String) changed.elementAt(x) + "\n");
			}

			bwriter.flush();
			bwriter.close();
		} catch (IOException ioe) {
		}

		bigMapOld.clean();
		bigMapNew.clean();
	}


	/**
	 * Converts Patron status code to be "X" (I made this up)
	 * These patrons should now be marked as separated (blocked) becuase they have been 
	 * deleted from the campus database.
	 * @param line The patron record
	 * @return The patron record with status code converted
	 */
	private static String markAsDeleted(String line) {

		//Use 'K' or 'I' because then it would also get rid of 
        //deleted Emeritus staff
        String separated = "K";
		
		String[] tokens = line.split("\t");

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < 12; i++) {

			if (i == 2) {
				sb.append(separated + "\t");
			} else {
				sb.append(tokens[i] + "\t");
			}
		}
		return sb.toString().trim();
	}

	/**
	 * Main method for application
	 * @param args args[0]=old 'master' file, args[1]=new 'master' file
	 * args[2]=output file
	 */
	public static void main(String[] args) {
		getChanges(args[0], args[1], args[2]);
	}
}
