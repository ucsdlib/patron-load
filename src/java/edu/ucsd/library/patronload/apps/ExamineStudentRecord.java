/*
 * Created on Apr 30, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package edu.ucsd.library.patronload.apps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author jjesena
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExamineStudentRecord {

	private static void parseRecord(String line) {
		
		String[] tokens = line.split("\t");
		
		System.out.println("[0] Student ID               : " + tokens[0]);
		System.out.println("[1] Name                     : " + tokens[1]);
		System.out.println("[2] Social Security Number   : " + tokens[2]);
		System.out.println("[3] Registration Status Code : " + tokens[3]);
		System.out.println("[4] Quarter Last Enrolled    : " + tokens[4]);
		System.out.println("[5] Academic Level           : " + tokens[5]);
		System.out.println("[6] Major Code               : " + tokens[6]);
		System.out.println("[7] Address Type             : " + tokens[7]);
		System.out.println("[8] Start Date               : " + tokens[8]);
		System.out.println("[9] Stop  Date               : " + tokens[9]);
		System.out.println("[10] Address Line1           : " + tokens[10]);
		System.out.println("[11] Address Line2           : " + tokens[11]);
		System.out.println("[12] Address Line3           : " + tokens[12]);
		System.out.println("[13] Address Line4           : " + tokens[13]);
		System.out.println("[14] City                    : " + tokens[14]);
		System.out.println("[15] Area Code               : " + tokens[15]);
		System.out.println("[16] Exchange Code           : " + tokens[16]);
		System.out.println("[17] SQID                    : " + tokens[17]);
		System.out.println("[18] Extension               : " + tokens[18]);
		System.out.println("[19] State                   : " + tokens[19]);
		System.out.println("[20] Zip Code                : " + tokens[20]);
		System.out.println("[21] Country Code            : " + tokens[21]);
		System.out.println("[22] Email                   : " + tokens[22]);		
		System.out.println("[23] Barcode                 : " + tokens[23]);		
	}
	
	private static void printInvalidRecords(String line) {
		String[] tokens = line.split("\t");

		if (tokens.length != 24) {
		    parseRecord(line);
		}
	}
	
	
	
	public static void main(String[] args) {
		
		File file = new File(args[0]);
		String pid = args[1];
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String line = "";
			while ((line = br.readLine()) != null) {
				if (!line.startsWith(pid)) continue;
				
				parseRecord(line);
				printInvalidRecords(line);
			}
			
		} catch (FileNotFoundException ex1) {
		} catch (IOException ex2) {
		}
		
		
		
	}


}
