package edu.ucsd.library.patronload.apps;

/*
 * makemarc.java
 *
 * This is the Java port of makemarc.pl, the perl version
 *
 * Created on June 7, 2002, 12:50 PM
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import edu.ucsd.library.patronload.beans.patronload_bean;
import edu.ucsd.library.util.FileUtils;
import edu.ucsd.library.util.TextUtils;

/**
 *
 * @author  Joseph Jesena
 * @version 1.0 June 17, 2002
 */
public class makemarc {

	/** Creates new makemarc */
	public makemarc(String dataIn) {
		makemarc.dataIn = dataIn;
	}

	/**
	 * Method to generate a MARC file from merged student file
	 * @param pathToProperties The path to the properties file
	 * @param fileToRead The file to read the input from
	 * @param fileToWrite The file to write the output to
	 */
	public static void makeMarc(
		String pathToProperties,
		String fileToRead,
		String fileToWrite) {

		BufferedReader in = null;
		PrintWriter pw = null;

		try {
			in = new BufferedReader(new FileReader(fileToRead));
			pw =
				new PrintWriter(
					new BufferedOutputStream(
						new FileOutputStream(fileToWrite)));

			patronLoadProperties = getPatronLoadProperties(pathToProperties);

			String lineIn = "";
			String id = "";
			String ssn = "";
			String regstat = "";
			String lastenroll = "";
			String level = "";
			String dept = "";
			String pm = "";
			String cm = "";
			String ct = "";
			String name = "";
			String pt = "";
			String email = "";
			String barcode = "";
			String systemId = "";
			debugData = new StringBuffer();

			// keep going while there are still lines

			while (((lineIn = in.readLine()) != null)
				&& !(lineIn.trim().equals(""))) {
				lineIn = lineIn.trim();
				
				//remove the question marks
				lineIn = TextUtils.strReplace(lineIn, "?", "");
				

				// pad the tabs with a space so that the tokenizer
				// can work properly
				lineIn = TextUtils.strReplace(lineIn, "\t", "\t ");

				// split apart the new record (common fields will be
				// overwritten but should be identical)
				StringTokenizer st = new StringTokenizer(lineIn, "\t", false);

				// remove any initial or trailing white space from each
				// variable

				if (st.hasMoreTokens()) {
					id = st.nextToken().trim();
				} else {
					id = "";
				}

				if (st.hasMoreTokens()) {
					name = st.nextToken().trim();
				} else {
					name = "";
				}

				if (st.hasMoreTokens()) {
					ssn = st.nextToken().trim();
				} else {
					ssn = "";
				}

				if (st.hasMoreTokens()) {
					regstat = st.nextToken().trim();
				} else {
					regstat = "";
				}

				if (st.hasMoreTokens()) {
					lastenroll = st.nextToken().trim();
				} else {
					lastenroll = "";
				}

				if (st.hasMoreTokens()) {
					level = st.nextToken().trim();
				} else {
					level = "";
				}

				if (st.hasMoreTokens()) {
					dept = st.nextToken().trim();
				} else {
					dept = "";
				}

				if (st.hasMoreTokens()) {
					pm = st.nextToken().trim();
				} else {
					pm = "";
				}

				if (st.hasMoreTokens()) {
					pt = st.nextToken().trim();
				} else {
					pt = "";
				}

				if (st.hasMoreTokens()) {
					cm = st.nextToken().trim();
				} else {
					cm = "";
				}

				if (st.hasMoreTokens()) {
					ct = st.nextToken().trim();
				} else {
					ct = "";
				}

				if (st.hasMoreTokens()) {
					email = st.nextToken().trim();
					if(email.equals("none"))
						email = "";
				} else {
					email = "";
				}
				
				if (st.hasMoreTokens()) {
					barcode = st.nextToken().trim();
					if(barcode.equals("0"))
						barcode = "";
				} else {
					barcode = "";
				}

				if (st.hasMoreTokens()) {
					systemId = st.nextToken().trim();
				} else {
					systemId = "";
				}
				
				String deptnum = "";
				try {
					deptnum = ((String) patronLoadProperties.get(dept)).trim();
				} catch (NullPointerException npe) {
				}

				//get the expiration date from the properties file
				String expiredate = "";

				if (level.equals("3")) {
					// undergraduate is level 3
					expiredate =
						(String) patronLoadProperties.get(
							"expiredate_undergrad");
				}

				if (level.equals("2")) {
					// graduate, medical, and PH is level 2
					expiredate =
						(String) patronLoadProperties.get(
							"expiredate_graduate");
				}

				expiredate = expiredate.trim();
				
				
				String quarterCodeLetter = patronload_bean.getQuarterCodeLetter((String) patronLoadProperties.get("quartercode"));

				if (id.equals(""))
					continue;

				//makeFieldEntry("010", ssn);
				makeFieldEntry("020", id);
				makeFieldEntry("030", barcode);
				makeFieldEntry("080", expiredate);
				makeFieldEntry("081", quarterCodeLetter);				
				makeFieldEntry("083", deptnum);
				makeFieldEntry("084", level);
				makeFieldEntry("100", name);
				makeFieldEntry("220", cm);
				makeFieldEntry("225", ct);
				makeFieldEntry("230", pm);
				makeFieldEntry("235", pt);
				makeFieldEntry("550", email);
				if(systemId != "")
					makeFieldEntry("400", systemId);
				if (debug) {
				    debugData.append("\n");
				}
				
				// calculate the size of the total record and the base offset
				// total record is 24 byte leader + entire directory (12*n) +
				// directory field terminator + all fields (ie $curoffset),
				// plus record terminator)

				String leader =
					pad(
						(new Integer(24 + dirsize + 1 + curoffset + 1))
							.toString(),
						'0',
						5)
						+ "       "
						+ pad((new Integer(24 + dirsize + 1)).toString(), '0', 5)
						+ "       ";

//				if (debug) {
//					System.out.println("Leader:");
//				}

				//retString.append(leader);
				pw.write(leader);

//				if (debug) {
//					System.out.println("\nDirectory:");
//				}

				for (int i = 0; i < directory.size(); i++) {
					//retString.append(directory.elementAt(i));
					pw.write((String) directory.elementAt(i));
				}

				// output the directory field terminator
				//retString.append((char)0x1e);
				pw.write((char) 0x1e);

				// output the fields, each already includes its own terminator
				for (int i = 0; i < fields.size(); i++) {
					//retString.append(fields.elementAt(i));
					pw.write((String) fields.elementAt(i));
				}

				// output the record terminator
				//retString.append((char)0x1d);
				pw.write((char) 0x1d);

				// reset the variables
				curoffset = 0;
				dirsize = 0;
				curfieldnum = 1;
				fields = new Vector();
				directory = new Vector();

			}

			pw.close();
		} catch (IOException ioe) {
		}

		if (pw != null)
			pw.close();
		if (in != null) {
			try {
				in.close();
			} catch (IOException ioe) {
			}
		}
		
		
		/*if (debug) {
		    try {
		        FileUtils.stringToFile(debugData.toString(), pathToProperties + "/marc_files/marc_output.txt");
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}*/
		
		
	}

	public static String pad(String in, char ch, int len) {
		while (in.length() < len) {
			in = ch + in;
		}
		return in;
	}

	public static void makeFieldEntry(String ftag, String fdata) {

		if (!fdata.equals("")) {
			if (debug) {
			    debugData.append(ftag + ": " + fdata + "\n");
			}

			String thisfield =
				"  " + ((char) 0x1f) + "a" + fdata + ((char) 0x1e);

//			if (debug) {
//			    debugData.append("   field:" + thisfield);
//			}

			String thisdirentry =
				ftag
					+ pad((new Integer(fdata.length() + 5)).toString(), '0', 4)
					+ pad((new Integer(curoffset)).toString(), '0', 5);

//			if (debug) {
//			    debugData.append("dirent:" + thisdirentry + "\n");
//			}

			dirsize += thisdirentry.length();

			fields.add(thisfield);
			directory.add(thisdirentry);

			curoffset += fdata.length() + 5;
			curfieldnum++;

		} else {
			if (debug) {
			    debugData.append(ftag + ": [empty]\n");
			}
		}

//		if (debug) {
//			System.out.println(
//				"off:"
//					+ curoffset
//					+ ",size:"
//					+ dirsize
//					+ ",curfieldnum:"
//					+ curfieldnum);
//		}
	}

	public static Properties getPatronLoadProperties(String pathToProperties) {
		Properties prop = new Properties();

		BufferedInputStream bis = null;

		if (!pathToProperties.equals("")) {
			pathToProperties += File.separator;
		}

		try {
			try {
				bis =
					new BufferedInputStream(
						new FileInputStream(
							pathToProperties + "patron_load.properties"));
			} catch (FileNotFoundException fne) {
				if (bis != null)
					bis.close();
				System.out.println(
					"Error: patron_load.properties file not found!");
				System.exit(1);
			}

			try {
				prop.load(bis);
			} catch (IOException ioe) {
				if (bis != null)
					bis.close();
				System.out.println("Error: cannot load properties file!");
				System.exit(1);
			}

			if (bis != null)
				bis.close();
		} catch (IOException ioe) {
		}

		return prop;
	}

	/**
	* @param args the command line arguments
	* args[0] = path to properties
	* args[1] = fileToRead
	* args[2] = fileToWrite
	*/
	public static void main(String args[]) {

		if ((args == null) || (args.length < 3)) {
			System.out.println(
				"\nSyntax: java makemarc [pathToProperties] [fileToRead] [fileToWrite]");
		} else {
			makeMarc(args[0], args[1], args[2]);
		}
	}

	public static boolean debug = false;
	public static StringBuffer debugData;
	public static Properties patronLoadProperties;
	public static int curoffset = 0;
	public static int dirsize = 0;
	public static int curfieldnum = 1;
	public static Vector fields = new Vector();
	public static Vector directory = new Vector();

	private static Reader systemIn = null;
	public static String dataIn;
}