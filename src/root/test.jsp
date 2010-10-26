<%@page import="java.io.*,java.sql.*,java.util.*,javax.naming.*,edu.ucsd.library.util.*"%>
<%
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	try
	{
		// write files
		InitialContext jndi = new InitialContext();
		String sharedPath = "";
		try
		{
			sharedPath = (String)jndi.lookup("java:comp/env/clusterSharedPath");
		}
		catch ( Exception ex )
		{
			sharedPath = "/pub/data1/import/htdocs";
		}
		File outputDir = new File(
			sharedPath + application.getInitParameter("marcFilePath")
		);
		if ( !outputDir.exists() || !outputDir.isDirectory() || !outputDir.canWrite() )
		{
			throw new IOException("Output directory (" + outputDir.getAbsolutePath() + ") doesn't exist or isn't writable!");
		}
	
		// read properties files from webinf
		String propsFile = request.getRealPath("") + File.separator + "WEB-INF/patron_load.properties";
		Properties props = FileUtils.loadProperties(propsFile);
		String url = props.getProperty( "dbconnection3" ).trim();
		String user = props.getProperty( "dbusername" ).trim();
		String pass = props.getProperty( "dbpassword" ).trim();
		String driv = props.getProperty( "dbdriver" ).trim();
		String sql = "select count(*) from p_employee where emb_employee_name like 'SPIG%'";
	
		// db query
		Class.forName( driv );
		con = DriverManager.getConnection( url, user, pass );
		stmt = con.createStatement();
		rs = stmt.executeQuery( sql );
		if ( rs.next() )
		{
			int count = rs.getInt(1);
		}

		// success
		out.println("SUCCESS");
	}
	catch ( Exception ex )
	{
		out.print("FAIL: " + ex.toString() );
	}
	finally
	{
		try { rs.close(); } catch ( Exception ex2 ) { ex2.printStackTrace(); }
		try { stmt.close(); } catch ( Exception ex2 ) { ex2.printStackTrace(); }
		try { con.close(); } catch ( Exception ex2 ) { ex2.printStackTrace(); }
	}
%>
