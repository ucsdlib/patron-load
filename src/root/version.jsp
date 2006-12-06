 <%
    String version = application.getInitParameter("version-number");
    if ( version == null || version.trim().equals("") )
    {
            version = "0.0.0";
    }
    String build = application.getInitParameter("build-date");
    if ( build == null || build.trim().equals("") )
    {
            build = "unknown";
    }
%><html>
  <head>
    <title>PatronLoad: Version <%=version%></title>
  </head>
  <body>
    <p>PatronLoad, Version <%=version%>, Build <%=build%></p>
  </body>
</html>
