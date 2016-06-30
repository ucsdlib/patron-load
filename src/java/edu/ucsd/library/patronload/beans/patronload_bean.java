package edu.ucsd.library.patronload.beans;
/*
 * patronload_bean.java
 *
 * Created on June 26, 2002, 9:10 AM
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Vector;
import java.util.HashMap; 
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import java.util.SortedMap;


import javax.servlet.http.HttpServletRequest;

import edu.ucsd.library.patronload.apps.fullquery;
import edu.ucsd.library.patronload.apps.incquery;
import edu.ucsd.library.util.FileUtils;
import edu.ucsd.library.util.TextUtils;

/**
 *
 * @author  Joseph Jesena
 */
public class patronload_bean {
    
    /** Creates new patronload_bean */
    public patronload_bean() {
    	//incquery.setMarcFilesDir(marcFilesDir);
    }
    
    /** Method to retreive the hash value of a password. Use this to generate
     * the initial password of the first user
     * @param args  */
    public static void main(String[] args) {
        patronload_bean my = new patronload_bean();
        System.out.println("Hash: " + my.getSecureHash(args[0]));
    }
    
    /**
     * Method to get the hash value of an input String
     * @param pass The String to compute the hash from
     * @return String - The resulting hash value
     */
    public String getSecureHash(String pass) {
        byte[] buf= new byte[pass.length()];
        buf = pass.getBytes();
        
        MessageDigest algorithm = null;
        try {
            algorithm = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        algorithm.reset();
        algorithm.update(buf);
        byte[] digest1 = algorithm.digest();
        
        char[] myChar =
        edu.ucsd.library.util.math.HexDumper.formatBytes(digest1);
        
        String myStr = new String(myChar);
        myStr = TextUtils.strReplace(myStr, " ", "");
        
        return myStr;
    }

    
/*	public static String getQuarterCodeLetter(String in) {
		
			String quarter = in.substring(0, 2);
			String year = in.substring(2,4);
			
			boolean isEvenYear = (((Integer.parseInt(year)) % 2) == 0);

			String quarterCodeLetter = "";
			if (quarter.equals("FA") &&  isEvenYear) quarterCodeLetter = "a";
			if (quarter.equals("WI") && !isEvenYear) quarterCodeLetter = "b";
			if (quarter.equals("SP") && !isEvenYear) quarterCodeLetter = "c";
			if (quarter.equals("SU") && !isEvenYear) quarterCodeLetter = "d";
			if (quarter.equals("FA") && !isEvenYear) quarterCodeLetter = "e";
			if (quarter.equals("WI") &&  isEvenYear) quarterCodeLetter = "f";
			if (quarter.equals("SP") &&  isEvenYear) quarterCodeLetter = "g";
			if (quarter.equals("SU") &&  isEvenYear) quarterCodeLetter = "h";
		
			return 	quarterCodeLetter;
		}*/
    
    public static String getQuarterCodeLetter(String in) {

		String quarterCodeLetter = "";
		if (in.equals("FA04")) quarterCodeLetter = "a";
		if (in.equals("WI05")) quarterCodeLetter = "b";
		if (in.equals("SP05")) quarterCodeLetter = "c";
		if (in.equals("SU05")) quarterCodeLetter = "d";
		if (in.equals("FA05")) quarterCodeLetter = "e";
		if (in.equals("WI06")) quarterCodeLetter = "f";
		if (in.equals("SP06")) quarterCodeLetter = "g";
		if (in.equals("SU06")) quarterCodeLetter = "h";
		if (in.equals("FA06")) quarterCodeLetter = "i";
		if (in.equals("WI07")) quarterCodeLetter = "j";
		if (in.equals("SP07")) quarterCodeLetter = "k";
		if (in.equals("SU07")) quarterCodeLetter = "l";
		if (in.equals("FA07")) quarterCodeLetter = "m";
		if (in.equals("WI08")) quarterCodeLetter = "n";
		if (in.equals("SP08")) quarterCodeLetter = "o";
		if (in.equals("SU08")) quarterCodeLetter = "p";
		if (in.equals("FA08")) quarterCodeLetter = "q";
		if (in.equals("WI09")) quarterCodeLetter = "r";
		if (in.equals("SP09")) quarterCodeLetter = "s";
		if (in.equals("SU09")) quarterCodeLetter = "t";		
		if (in.equals("FA09")) quarterCodeLetter = "u";
		if (in.equals("SP10")) quarterCodeLetter = "w";
		if (in.equals("WI17")) quarterCodeLetter = "x";
		if (in.equals("SP17")) quarterCodeLetter = "y";
		if (in.equals("SU17")) quarterCodeLetter = "z";		
		if (in.equals("FA17")) quarterCodeLetter = "a";
		if (in.equals("WI18")) quarterCodeLetter = "b";
		if (in.equals("SP18")) quarterCodeLetter = "c";
		if (in.equals("WI12")) quarterCodeLetter = "d";
		if (in.equals("SP12")) quarterCodeLetter = "e";
		if (in.equals("SU12")) quarterCodeLetter = "f";
		if (in.equals("FA12")) quarterCodeLetter = "g";
		if (in.equals("WI13")) quarterCodeLetter = "h";
		if (in.equals("SP13")) quarterCodeLetter = "i";
		if (in.equals("SU13")) quarterCodeLetter = "j";
		if (in.equals("FA13")) quarterCodeLetter = "k";
		if (in.equals("WI14")) quarterCodeLetter = "l";
		if (in.equals("SP14")) quarterCodeLetter = "m";
		if (in.equals("SU14")) quarterCodeLetter = "n";
		if (in.equals("FA14")) quarterCodeLetter = "o";
		if (in.equals("WI15")) quarterCodeLetter = "p";		
		if (in.equals("SP15")) quarterCodeLetter = "q";
		if (in.equals("SU15")) quarterCodeLetter = "r";
		if (in.equals("FA15")) quarterCodeLetter = "s";
		if (in.equals("WI16")) quarterCodeLetter = "t";
		if (in.equals("SP16")) quarterCodeLetter = "u";
		if (in.equals("SU16")) quarterCodeLetter = "v";
		if (in.equals("FA16")) quarterCodeLetter = "w";
		
		return 	quarterCodeLetter;
	}
    
    /**
     * Method to generate the HTML code for a dropdown box that
     * contains all available users
     * @return String - the HTML code
     */
    public String getUserSelectBox() {
        
        Vector myVec = null;
        try {
            myVec = FileUtils.readFileToVector(pathToACLFile);
        } catch (IOException ioe) {
        }
        
        StringBuffer myST = new StringBuffer("<select name=\"username\">\n");
        
        for (int i=0; i < myVec.size(); i++) {
            String tmp = (String)myVec.elementAt(i);
            tmp = tmp.substring(0, tmp.indexOf(":"));
            
            myST.append("<option value=\"" + tmp + "\">" + tmp + "\n");
        }
        
        myST.append("\n</select>\n");
        return myST.toString();
    }
    
    /**
     * Method to save the current user's permissions in the bean
     * from the HTTP request object
     * @param request The HttpServletRequest to geth te permissions from
     */
    public void saveUserPermissions(HttpServletRequest request) {
        
        if (request.getParameter("createfullfile") != null) {
            canCreateFullFile = true;
        } else {
            canCreateFullFile = false;
        }
        
        if (request.getParameter("authorizedusers") != null) {
            canAuthorizeUsers = true;
        } else {
            canAuthorizeUsers = false;
        }
        
        if (request.getParameter("incrementalfiles") != null) {
            canAccessIncFiles = true;
        } else {
            canAccessIncFiles = false;
        }
        
        if (request.getParameter("fulldatabasefiles") != null) {
            canAccessFullFiles = true;
        } else {
            canAccessFullFiles = false;
        }
        
        if (request.getParameter("changesettings") != null) {
            canChangeSettings = true;
        } else {
            canChangeSettings = false;
        }
    }
    
    /**
     * Method to load a user's permissions from the ACL file
     * @param user The user to load permissions from
     */
    public void loadUserPermissions(String user) {
        
        currentUserInfo = new user_bean();
        
        currentUserInfo.setPathToACLFile(pathToACLFile);
        currentUserInfo.loadUser(user);
        
        canCreateFullFile =  currentUserInfo.canCreateFullFile();
        canAuthorizeUsers =  currentUserInfo.canAuthorizeUsers();
        canAccessIncFiles =  currentUserInfo.canAccessIncFiles();
        canAccessFullFiles =  currentUserInfo.canAccessFullFiles();
        canChangeSettings =  currentUserInfo.canChangeSettings();
    }
    
    /**
     * @return  */
    public boolean canCreateFullFile() {
        return canCreateFullFile;
    }
    
    /**
     * @return  */
    public boolean canAuthorizeUsers() {
        return canAuthorizeUsers;
    }
    
    /**
     * @return  */
    public boolean canAccessIncFiles() {
        return canAccessIncFiles;
    }
    
    /**
     * @return  */
    public boolean canAccessFullFiles() {
        return canAccessFullFiles;
    }
    
    /**
     * @return  */
    public boolean canChangeSettings() {
        return canChangeSettings;
    }
    
    /**
     * Authenticates the username and password for a user
     * @param user The user's username
     * @param pass The user's password
     * @return boolean - is this user allowed with the given username/password?
     */
    public boolean checkLogon(String user, String pass) {
        
        StringBuffer str = new StringBuffer(user);
        str.append(":" + getSecureHash(pass) );
        
        String acl = null;
        try {
            acl = FileUtils.fileToString(pathToACLFile);
        } catch (IOException ioe) {
        }
        
        return (acl.indexOf(str.toString()) > -1 );
    }
    
    /**
     * Change the current user's password
     * @param oldPass The user's old password
     * @param newPass1 The user's new password
     * @param newPass2 Make sure user's new password matches
     * @return int - 0=ok, 1=new passwords don't match, 2=old password wrong
     */
    public int changePassword(String oldPass, String newPass1,
    String newPass2) {
        
        if (checkLogon(currentUser, oldPass)) {
            
            if (newPass1.equals(newPass2)) {
                
                editUser(   currentUser,
                newPass1,
                canCreateFullFile,
                canAuthorizeUsers,
                canAccessIncFiles,
                canAccessFullFiles,
                canChangeSettings
                );
                return 0;
                
            } else {
                return 1;
            }
            
        } else {
            return 2;
        }
    }
    
    /**
     * Method to change a user's information
     * @param request The request object
     * @return int - 0=ok,1=passwords are not equal,2=user could not be changed
     */
    public int editUserFromRequest(HttpServletRequest request) {
        
        String pass = request.getParameter("password");
        String user = request.getParameter("username");
        
        if (pass.equals("<keep_same_password>")) {
            user_bean myUser = new user_bean();
            myUser.setPathToACLFile(pathToACLFile);
            myUser.loadUser(user);
            pass = myUser.getPassword();
            request.setAttribute("password", "&" + pass);
        }
        
        removeUser(user);
        return (addUserFromRequest(request));
    }
    
    /**
     * Method to edit user with these parameters
     * @param user The user name
     * @param pass The user's password
     * @param p1 Permission1
     * @param p2 Permission2
     * @param p3 Permission3
     * @param p4 Permission4
     * @param p5 Permission5
     * @return boolean - Was it successful?
     */
    public boolean editUser(String user, String pass, boolean p1,
    boolean p2, boolean p3, boolean p4, boolean p5) {
        
        removeUser(user);
        addUser(user, pass, p1, p2, p3, p4, p5);
        
        if (user.equals(currentUser)) {
            loadUserPermissions(user);
        }
        
        return true;
    }
    
    /**
     * Method to add user
     * @param request The request object
     * @return int - 0=ok, 1=passwords are not equal, 2=user could not be added
     */
    public int addUserFromRequest(HttpServletRequest request) {
        
        String username = request.getParameter("username");
        
        if (username.trim().equals("")) {
            return 2;
        }
        
        String password;
        if (request.getAttribute("password") == null) {
            password = request.getParameter("password");
            
            String verifypassword = request.getParameter("verifypassword");
            
            if (!password.equals(verifypassword)) {
                //passwords are not equal
                return 1;
            }
            
        } else {
            password = (String) request.getAttribute("password");
        }
        
        boolean createfullfile      = false;
        boolean authorizedusers     = false;
        boolean incrementalfiles    = false;
        boolean fulldatabasefiles   = false;
        boolean changesettings      = false;
        
        if (request.getParameter("createfullfile") != null) {
            createfullfile = true;
        }
        
        if (request.getParameter("authorizedusers") != null) {
            authorizedusers = true;
        }
        
        if (request.getParameter("incrementalfiles") != null) {
            incrementalfiles = true;
        }
        
        if (request.getParameter("fulldatabasefiles") != null) {
            fulldatabasefiles = true;
        }
        
        if (request.getParameter("changesettings") != null) {
            changesettings = true;
        }
        
        if (!addUser(username, password, createfullfile, authorizedusers,
        incrementalfiles, fulldatabasefiles, changesettings)) {
            return 2;
        }
        
        return 0;
    }
    
    /**
     * Method to add user
     */
    public boolean addUser(String user, String pass, boolean p1,
    boolean p2, boolean p3, boolean p4, boolean p5) {
        
        String aclData = null;
        try {
            aclData = FileUtils.fileToString( pathToACLFile );
        } catch (IOException ioe) {
        }
        
        if ( aclData.indexOf("\n" + user + ":") > -1) {
            return false;
        }
        
        if (pass.startsWith("&")) {
            pass = pass.substring(1, pass.length());
        } else {
            pass = getSecureHash(pass);
        }
        
        StringBuffer str = new StringBuffer(user);
        
        str.append(":" + pass );
        str.append(":" + (p1 ? "1" : "0") );
        str.append(":" + (p2 ? "1" : "0") );
        str.append(":" + (p3 ? "1" : "0") );
        str.append(":" + (p4 ? "1" : "0") );
        str.append(":" + (p5 ? "1" : "0") );
        str.append("\n");
        
        try {
            FileUtils.appendToFile(str.toString(), pathToACLFile);
        } catch (IOException ioe) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Method to remove user
     * @param user The user to remove
     * @return boolean - Was user removed?
     */
    public boolean removeUser(String user) {
        
        Vector myVec = null;
        try {
            myVec = FileUtils.readFileToVector(pathToACLFile);
        } catch (IOException ioe) {
        }
        
        String dataToWrite = "";
        for (int i=0; i < myVec.size(); i++) {
            String userInfo = (String)myVec.elementAt(i);
            if (!userInfo.startsWith(user + ":")) {
                //save all the accounts except the one currently editing
                dataToWrite +=userInfo + "\n";
            }
        }
        
        try {
            FileUtils.stringToFile(dataToWrite, pathToACLFile);
        } catch (IOException ioe) {
            return false;
        }
        
        return true;
    }
    
    /**
     * @return  */
    public String getContextDir() {
        return contextDir;
    }
    
    /**
     * @param str  */
    public void setContextDir(String str) {
        contextDir = str;
    }
    
    /**
     * @return  */
    public String getContextUrl() {
        return contextUrl;
    }
    
    /**
     * @param str  */
    public void setContextUrl(String str) {
        contextUrl = str;
    }
    
    /**
     * @return  */
    public String getMarcFilesDir() {
        return marcFilesDir;
    }
    
    /**
     * @param str  */
    public void setMarcFilesDir(String str) {
        marcFilesDir = str;
        incquery.setMarcFilesDir(marcFilesDir);
        fullquery.setMarcFilesDir(marcFilesDir);
    }
    
    /**
     * Delete the given MARC file
     * @param filen The file to delete
     * @return boolean - Was the file deleted?
     */
    public boolean deleteMarcFile(String filen) {
        //String marcFilesDir =  contextDir + "WEB-INF" + File.separator +
        //"marc_files" + File.separator;
        File myF = new File( marcFilesDir + filen);
        return (myF.delete());
    }
    
    
    /**
     * Method to upudate settings
     * @param quarter The quarter from which to get data
     * @param exp1 Expiration date 1
     * @param exp2 Expiration date 2
     * @return boolean - Was update successful?
     */
    public boolean updateSettings(String quarter, String exp1, String exp2,
    String employee1_exp, String employee2_exp) {
        
        String webinfDir = contextDir + "WEB-INF" + File.separator;
        //String marcFileUrl = contextUrl + "marc_files/";
        
        Properties myProp = null;
        try {
            myProp = FileUtils.loadProperties(pathToProperties);
        } catch (IOException ioe) {
        }
        
        myProp.setProperty("expiredate_undergrad", exp1);
        myProp.setProperty("expiredate_graduate", exp2);
        
        myProp.setProperty("expiredate_employee1", employee1_exp);
		myProp.setProperty("expiredate_employee2", employee2_exp);
        
        myProp.setProperty("quartercode", quarter);
        
        try {
            FileOutputStream fos = new FileOutputStream(pathToProperties);
            myProp.store(fos, "");
            fos.close();
        } catch (FileNotFoundException fnfe) {
            return false;
        } catch (IOException ioe) {
            return false;
        }
        
        //update the current variables;
        quarterCode = quarter;
        this.quarterCodeLetter = quarterCodeLetter; 
        expiredate_undergrad = exp1;
        expiredate_graduate = exp2;
        
        expiredate_employee1 = employee1_exp;
		expiredate_employee2 = employee2_exp;
        
        return true;
    }
    
    /**
     * Method to get MARC file names in HTML
     * @param type The type of marc file (full or inc?)
     * @return String - the list of MARC files in HTML
     */
    public String getMarcFileNames(String type) {
        //String webinfDir = contextDir + "WEB-INF" + File.separator;
        //String marcFilesDir =  contextDir + "WEB-INF" + File.separator +
        //"marc_files" + File.separator;
        
        try {
            FileUtils.confirmDir(marcFilesDir);
        } catch (IOException ioe) {
            //just ignore it for now....
        }
        
        
        String marcFileUrl = contextUrl.substring(0, contextUrl.length()-4) + "getmarcfile";
        
        File myF = new File( marcFilesDir);
        String[] myFiles = myF.list();
        
        StringBuffer retValue = new StringBuffer("");
        
        // format time like: July 04 10:48:39 PDT 2002
        Format formatter;
        formatter = new SimpleDateFormat("MMM dd kk:mm:ss yyyy");
        
        for (int i=0; i < myFiles.length; i++) {
            File testF = new File(marcFilesDir + myFiles[i]);
            
            if ((testF.isFile()) && (testF.getName().startsWith(type)) &&
            (testF.getName().endsWith(".marc")) ) {
                retValue.append("<tr>");
                
                java.util.Date date = new java.util.Date(testF.lastModified());
                String dateString = formatter.format(date);
                
                retValue.append("<td>" + "<a href=\"" + marcFileUrl + "?file=" + myFiles[i] + "\"> " + myFiles[i] + "</a></td>");
                retValue.append("<td>" + dateString + "</td>");
                retValue.append("<td>" + (testF.length() / 1024) + "k</td>");
                retValue.append("<td> <input type=\"checkbox\" name=\"remove_" + myFiles[i] + "\"> </td>");
                retValue.append("</tr>");
            }
        }
        
        return retValue.toString();
    }
    
    /**
     * @return  */
    public String getCurrentUser() {
        return currentUser;
    }
    
    /**
     * @return  */
    public String getPathToProperties() {
        return pathToProperties;
    }
    
    /**
     * @param in  */
    public void setPathToProperties(String in) {
        pathToProperties = in;
    }
    
    /**
     * Method to load settings from properties file
     */
    public void getSettings() {
        
        String webinfDir = contextDir + "WEB-INF" + File.separator;
        
        Properties myProp = null;
        try {
            myProp = FileUtils.loadProperties(pathToProperties);
        } catch (IOException ioe) {
        }
        
        quarterCode = myProp.getProperty("quartercode");
        expiredate_undergrad = myProp.getProperty("expiredate_undergrad");
        expiredate_graduate = myProp.getProperty("expiredate_graduate");
        
        expiredate_employee1 = myProp.getProperty("expiredate_employee1");
		expiredate_employee2 = myProp.getProperty("expiredate_employee2");
    }
    
    /**
     * @param usr  */
    public void setCurrentUser(String usr) {
        currentUser = usr;
        loadUserPermissions(usr);
        getSettings();
    }
    
    /**
     * @return  */
    public String getPathToACLFile() {
        return pathToACLFile;
    }
    
    /**
     * @param str  */
    public void setPathToACLFile(String str) {
        pathToACLFile = str;
    }
    
    /**
     * @return  */
    public String getQuarterCode() {
        return quarterCode;
    }
    
    /**
     * @param in  */
    public void setQuarterCode(String in) {
        quarterCode = in;
    }
    
    /**
     * @return  */
    public String getExpiredate_undergrad() {
        return expiredate_undergrad;
    }
    
    /**
     * @param in  */
    public void setExpiredate_undergrad(String in) {
        expiredate_undergrad = in;
    }
    
    /**
     * @return  */
    public String getExpiredate_graduate() {
        return expiredate_graduate;
    }
    
    /**
     * @param in  */
    public void setExpiredate_graduate(String in) {
        expiredate_graduate = in;
    }
    
	/**
	 * @return
	 */
	public String getExpiredate_employee1() {
		return expiredate_employee1;
	}

	/**
	 * @return
	 */
	public String getExpiredate_employee2() {
		return expiredate_employee2;
	}

	/**
	 * @param string
	 */
	public void setExpiredate_employee1(String string) {
		expiredate_employee1 = string;
	}

	/**
	 * @param string
	 */
	public void setExpiredate_employee2(String string) {
		expiredate_employee2 = string;
	}

    /**
     * Method to do a full database query for students
     */
    public void doFull() {
        String[] tmp = new String[2];
        tmp[0] = marcFilesDir;
        //tmp[1] = pathToProperties.substring(0, pathToProperties.lastIndexOf(File.separator)+1);
        tmp[1] = marcFilesDir;
        edu.ucsd.library.patronload.apps.dofull.main(tmp);
    }
    
	/**
	 * Method to do a full database query for employees
	 */
	public void doFullEmployee() {
		String[] tmp = new String[3];
		
		String propsDir = pathToProperties.substring(0, pathToProperties.lastIndexOf(File.separator)+1);
		tmp[0] = marcFilesDir;
		//tmp[1] = propsDir;
		tmp[1] = marcFilesDir;
		tmp[2] = "true";
		edu.ucsd.library.patronload.apps.dofull_employee.main(tmp);
	}

	/**
	 * Method to do an incremental database query for employees
	 */
	public void doIncEmployee() {
		String[] tmp = new String[2];
		
		String propsDir = pathToProperties.substring(0, pathToProperties.lastIndexOf(File.separator)+1);
		tmp[0] = marcFilesDir;
		//tmp[1] = propsDir;
		tmp[1] = marcFilesDir;
		edu.ucsd.library.patronload.apps.doinc_employee.main(tmp);
	}
	
	/**
	 * Method to do a full pull of only active employees
	 */
	public void doFullActiveEmployee() {
		String[] tmp = new String[3];
		
		String propsDir = pathToProperties.substring(0, pathToProperties.lastIndexOf(File.separator)+1);
		tmp[0] = marcFilesDir;
		//tmp[1] = propsDir;
		tmp[1] = marcFilesDir;
		tmp[2] = "false";
		edu.ucsd.library.patronload.apps.dofull_employee.main(tmp);
	}

	
	/**
	 * Method to change the properties file
	 */
	public int setPropertiesFile(String file, String name, String newValue) throws IOException
	{
		 Properties myProp = null;
		 Object o = null;
		 String link = "";
	        try {
	        	link = pathToProperties.substring(0,pathToProperties.length()-22);
	            myProp = FileUtils.loadProperties(link + file);
	        } catch (IOException ioe) {
	        	
	        }  
	        o = myProp.setProperty(name, newValue);
	        String cmt;
	        
	        if(file.equals("emp_affiliations.properties"))
	        	cmt = "#Employee Download - Affiliation code\n#[department code] = [library code]\n";
	       	else if(file.equals("employee_types.properties"))
	       		cmt ="#Staff Group\n";
	       	else 
	       		cmt = "#\n#Wed Apr 09 15:47:59 PDT 2008\n";
	        
	        try {
	            FileOutputStream fos = new FileOutputStream(link + file);
	            myProp.store(fos, cmt);
	            fos.close();
	        } catch (FileNotFoundException fnfe) {
	            return 2;
	        } catch (IOException ioe) {
	            return 3;
	        }
        if(o == null)
        	return 0;
        else
        	return 1;
	}
	
	public int delProperties(String file, String name) throws IOException
	{
		Properties myProp = null;
		 Object o = null;
		 String link = "";
	        try {
	        	link = pathToProperties.substring(0,pathToProperties.length()-22);
	            myProp = FileUtils.loadProperties(link + file);
	        } catch (IOException ioe) {
	        	
	        }  
	        o = myProp.remove(name);
	        String cmt;
	        
	        if(file.equals("emp_affiliations.properties"))
	        	cmt = "#Employee Download - Affiliation code\n#[department code] = [library code]\n";
	       	else if(file.equals("employee_types.properties"))
	       		cmt ="#Staff Group\n";
	       	else 
	       		cmt = "#\n#Wed Apr 09 15:47:59 PDT 2008\n";
	        
	        try {
	            FileOutputStream fos = new FileOutputStream(link + file);
	            myProp.store(fos, cmt);
	            fos.close();
	        } catch (FileNotFoundException fnfe) {
	            return 2;
	        } catch (IOException ioe) {
	            return 3;
	        }
       if(o == null)
       	return 0;
       else
       	return 1;
	}
	
	
	/**
	 * Method to get the whole set from the properties file
	 */
	public Map getPropertiesSet(String file)  throws IOException
	{
		 
		Properties props = null;
		Object o = null;
		String link = "";
        try {
        	link = pathToProperties.substring(0,pathToProperties.length()-22);
        	props = FileUtils.loadProperties(link + file);
        } catch (IOException ioe) {
        	
        } 
        Map<String,String> sortedMap = null;
        if(file.equals("patron_load.properties"))
        {
	        Map<String, String> propMap = new HashMap<String, String>();
	        Enumeration e = props.propertyNames();
	        for (; e.hasMoreElements(); ) {
	            String propName = (String)e.nextElement();
	            String propValue = (String)props.get(propName);
	            if(propValue.length() < 5 && !propName.contains("quartercode"))
	            		propMap.put(propName, propValue);
	        }    
	        sortedMap = new TreeMap(propMap);
        }
        else
        {
        	Map<Integer, String> propMap = new HashMap<Integer, String>();
  	        Enumeration e = props.propertyNames();
  	        for (; e.hasMoreElements(); ) {
  	            String propName = (String)e.nextElement();
  	            int propKey = Integer.parseInt(propName);
  	            String propValue = (String)props.get(propName);
  	            if(propValue.length() < 5)
  	            	propMap.put(propKey, propValue);
  	        }    
  	        sortedMap = new TreeMap(propMap);
        }
       return sortedMap;
	}
	
	/**
	 * Method to search for the existed key
	 */
	public boolean hasPropertiesKey(String file, String key)throws IOException
	{
		Properties props = null;
		Object o = null;
		String link = "";
        try {
        	link = pathToProperties.substring(0,pathToProperties.length()-22);
        	props = FileUtils.loadProperties(link + file);
        } catch (IOException ioe) {
        	
        } 
        if(props.getProperty(key) == null)
        	return false;
        return true;
	}
	

    /**
     * Method to upudate settings
     * @param quarter The quarter from which to get data
     * @param exp1 Expiration date 1
     * @param exp2 Expiration date 2
     * @return boolean - Was update successful?
     */
	
/*    public Map loadMajorCode() {
        
        String webinfDir = contextDir + "WEB-INF" + File.separator;
        String marcFileUrl = contextUrl + "marc_files/";
        
        Properties myProp = null;
        try {
            myProp = FileUtils.loadProperties(pathToProperties);
        } catch (IOException ioe) {
        }
        
        myProp.setProperty("expiredate_undergrad", exp1);
        myProp.setProperty("expiredate_graduate", exp2);
        
        myProp.setProperty("expiredate_employee1", employee1_exp);
		myProp.setProperty("expiredate_employee2", employee2_exp);
        
        myProp.setProperty("quartercode", quarter);
        
        try {
            FileOutputStream fos = new FileOutputStream(pathToProperties);
            myProp.store(fos, "");
            fos.close();
        } catch (FileNotFoundException fnfe) {
            return false;
        } catch (IOException ioe) {
            return false;
        }
    }   
        //update the current variables;
        quarterCode = quarter;
        this.quarterCodeLetter = quarterCodeLetter; 
        expiredate_undergrad = exp1;
        expiredate_graduate = exp2;
        
        expiredate_employee1 = employee1_exp;
		expiredate_employee2 = employee2_exp;
        
        return true;
    }	*/
	
	
    private String contextDir;
    private String contextUrl;
    private String marcFilesDir;
    private String currentUser;
    private String pathToACLFile;
    private String quarterCode;
    private String quarterCodeLetter;
    private String expiredate_undergrad;
    private String expiredate_graduate;
    private String expiredate_employee1;
	private String expiredate_employee2;
    private String pathToProperties;
    private boolean canCreateFullFile;
    private boolean canAuthorizeUsers;
    private boolean canAccessIncFiles;
    private boolean canAccessFullFiles;
    private boolean canChangeSettings;
    
    private user_bean currentUserInfo;
}
