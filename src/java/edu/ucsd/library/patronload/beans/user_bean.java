package edu.ucsd.library.patronload.beans;

/*
 * user_bean.java
 * 
 * Created on June 28, 2002, 1:36 PM
 */

import java.io.*;
import java.util.*;
import edu.ucsd.library.util.*;

/**
 *
 * @author  Joseph Jesena
 * @version 1.0 June 28, 2002
 */
public class user_bean {

    /** Creates new user_bean */
    public user_bean() {
    }
    
    public void loadUser(String user) {
        Vector myVec = null;
        try {
            myVec = FileUtils.readFileToVector(pathToACLFile);
        } catch (IOException ioe) {
        }

	String userInfo = "";
	for (int i=0; i < myVec.size(); i++) {
            userInfo = (String)myVec.elementAt(i);
            if (userInfo.startsWith(user + ":")) {
		break;
            }
        }

	StringTokenizer st = new StringTokenizer(userInfo, ":");

	username = st.nextToken();
	password = st.nextToken();

        canCreateFullFile =  (st.nextToken().equals("1")) ? true : false;
        canAuthorizeUsers =  (st.nextToken().equals("1")) ? true : false;
        canAccessIncFiles =  (st.nextToken().equals("1")) ? true : false;
        canAccessFullFiles = (st.nextToken().equals("1")) ? true : false;
        canChangeSettings =  (st.nextToken().equals("1")) ? true : false;
    }
    
    public String getPathToACLFile() {
        return pathToACLFile;
    }
    
    public void setPathToACLFile(String in) {
        pathToACLFile = in;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public boolean canCreateFullFile() {
        return canCreateFullFile;
    }
    
    public boolean canAuthorizeUsers() {
        return canAuthorizeUsers;
    }

    public boolean canAccessIncFiles() {
        return canAccessIncFiles;
    }

    public boolean canAccessFullFiles() {
        return canAccessFullFiles;
    }

    public boolean canChangeSettings() {
        return canChangeSettings;
    }
    
    private String username;
    private String password;
    private String pathToACLFile;
    private boolean canCreateFullFile;
    private boolean canAuthorizeUsers;
    private boolean canAccessIncFiles;
    private boolean canAccessFullFiles;
    private boolean canChangeSettings;

}