package edu.ucsd.library.patronload.apps;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import edu.ucsd.library.util.datastructure.BigHashMap;

/**
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class inc_all_employee {

	/**
	 * Generates a new file based on the changes from inFile1 and inFile2
	 * and sent to outFile
	 * @param inFile1 - The old 'master' file 
	 * @param inFile2 - The new 'master' file
	 * @param outFile String - File to send difference to
	 */
	private static void getChanges(String firstFile, //old 
	String secondFile, //new 
	String outFile) throws Exception {
	    Scanner x = new Scanner(new File(firstFile));
	    List<String> list1 = getScannerList(x);
	    x = new Scanner(new File(secondFile));
	    List<String> list2 = getScannerList(x);
	    x.close();
	    printLnList(listExtras(list1, new ArrayList<String>(list2)),outFile);
	}

	private static List<String> listExtras(List<String> list1,
	        List<String> list2) throws Exception {      
	    list2.removeAll(list1);
	    return list2;
	}

	private static List<String> getScannerList(Scanner sc) throws Exception {

	    List<String> scannerList = new ArrayList<String>();

	    while (sc.hasNext())
	        scannerList.add(sc.nextLine());

	    return scannerList;
	}

	private static void printLnList(List<String> list, String outFile) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
		for (String string : list) {
        //System.out.println(string);
			bw.write(string+"\n");
		}
		bw.close();
	}	
	/**
	 * Main method for application
	 * @param args args[0]=old 'master' file, args[1]=new 'master' file
	 * args[2]=output file
	 */
	public static void main(String[] args) {
		try {
			getChanges(args[0], args[1], args[2]);
		} catch (Exception e) {}
	}
}
