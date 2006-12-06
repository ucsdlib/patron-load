/*
 * Created on Oct 1, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package edu.ucsd.library.patronload.apps;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Vector;

/**
 * @author jjesena
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class makemarc_employee {

	public static void makeMarc(String propsFolder, String fileToRead, String fileToWrite) {

		BufferedReader in = null;
		PrintWriter pw = null;

		Properties patronLoadProperties = makemarc.getPatronLoadProperties(propsFolder);

		try {
			in = new BufferedReader(new FileReader(fileToRead));
			pw =
				new PrintWriter(
					new BufferedOutputStream(
						new FileOutputStream(fileToWrite)));

			String lineIn = "";

			while (((lineIn = in.readLine()) != null)
				&& !(lineIn.trim().equals(""))) {
				lineIn = lineIn.trim();

				String empId = edu.ucsd.library.patronload.apps.fullquery_employee.parseRecord(lineIn, 0);
				String name = fullquery_employee.parseRecord(lineIn, 1);
				String titleCode = fullquery_employee.parseRecord(lineIn, 4);
				String affilCode = fullquery_employee.parseRecord(lineIn, 6);
				
				//String employmentStatusCode = fullquery_employee.parseRecord(lineIn, 2);
				
				String employeeMailCode = fullquery_employee.parseRecord(lineIn, 8, false);
				String employeePhone = fullquery_employee.parseRecord(lineIn, 9);
				String employeeEmail = fullquery_employee.parseRecord(lineIn, 10);
				//String testEmail = fullquery_employee.parseRecord(lineIn, 12);
				String employeeBarcode = fullquery_employee.parseRecord(lineIn, 11);
				
/*				System.out.println("name:"+name+" id:"+empId+" mailcode:"+employeeMailCode);
				System.out.println("phone:"+employeePhone);
				System.out.println("email:"+employeeEmail);
				System.out.println("barcode:"+employeeBarcode);*/
				
				String expiredate;
				if (titleCode.equals("1") || titleCode.equals("16") || titleCode.equals("40")) {
					expiredate = (String) patronLoadProperties.get("expiredate_employee1");
				} else {
					expiredate = (String) patronLoadProperties.get("expiredate_employee2");
				}
				
				makeFieldEntry("024", empId);
				makeFieldEntry("100", name);
				
				//note: title code was converted to 17, 1, 40, or 16
				makeFieldEntry("084", titleCode);
				
				makeFieldEntry("083", affilCode);
				
				//use the expiration date from the properties file
				//if employee is not separated
				makeFieldEntry("080", expiredate);

				if(!employeeMailCode.equals("none"))
					makeFieldEntry("220", employeeMailCode);
				
				if(!employeePhone.equals("none"))
					makeFieldEntry("225", employeePhone);
				
				if(!employeeEmail.equals("none"))
					makeFieldEntry("550", employeeEmail);
				
				if(!employeeBarcode.equals("none"))
					makeFieldEntry("030", employeeBarcode);
				
				/*System.out.println("id:"+empId + " name:"+name + " titleCode:"+titleCode 
						+ " " + affilCode + " " + employeeMailCode + " " + employeePhone 
						+ " "+employeeEmail + " " + employeeBarcode);*/
				
/*				if(!testEmail.equals("none"))
					makeFieldEntry("032", testEmail);*/
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

				if (debug) {
					System.out.println("Leader:");
				}

				//retString.append(leader);
				pw.write(leader);

				if (debug) {
					System.out.println("\nDirectory:");
				}

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
			System.out.println(ioe);
		} finally {
			try {
				pw.close();
			} catch (Exception e) {
			}
		}

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
				System.out.println("got:" + ftag + ":" + fdata);
			}

			String thisfield =
				"  " + ((char) 0x1f) + "a" + fdata + ((char) 0x1e);

			if (debug) {
				System.out.println("field:" + thisfield);
			}

			String thisdirentry =
				ftag
					+ pad((new Integer(fdata.length() + 5)).toString(), '0', 4)
					+ pad((new Integer(curoffset)).toString(), '0', 5);

			if (debug) {
				System.out.println("dirent:" + thisdirentry);
			}

			dirsize += thisdirentry.length();

			fields.add(thisfield);
			directory.add(thisdirentry);

			curoffset += fdata.length() + 5;
			curfieldnum++;

		} else {
			if (debug) {
				System.out.println("Empty field:");
			}
		}

		if (debug) {
			System.out.println(
				"off:"
					+ curoffset
					+ ",size:"
					+ dirsize
					+ ",curfieldnum:"
					+ curfieldnum);
		}
	}

	/**
	* @param args the command line arguments
	* args[0] = folder of patronload properties file
	* args[1] = fileToRead
	* args[2] = fileToWrite
	*/
	public static void main(String args[]) {

		if ((args == null) || (args.length < 3)) {
			System.out.println(
				"\nSyntax: java makemarc_employee [properties folder] [fileToRead] [fileToWrite]");
		} else {
			makeMarc(args[0], args[1], args[2]);
		}
	}

	public static boolean debug = false;
	public static int curoffset = 0;
	public static int dirsize = 0;
	public static int curfieldnum = 1;
	public static Vector fields = new Vector();
	public static Vector directory = new Vector();

	public static String dataIn;

}
