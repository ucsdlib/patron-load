<html>
  <head>
    <style type="text/css">
      body { font-family: sans-serif; text-align: center; padding: 40px; background:#4e71b9 url(../images/body_gradient.gif) top left repeat-x !important; }
      h1 { font-size: x-large; color:#f0f0f0; }
      p { color:#f0f0f0 }
      form { border: solid gray 3px; background: #f0f0f0; padding: 10px;
             margin-left: 25%; margin-right: 25%; padding: 40px; }
    </style>
    <script type="text/javascript">
      function init() {
        document.f1.j_username.focus();
      }
    </script>
  </head>
  <body onLoad="init();">
    <h1>Patronload</h1>
    <p>Please enter your Active Directory username and password.</p>

    <form name="f1" method="POST" action="j_security_check" >
      <table align="center">
        <tr>
          <th>Username:</th>
          <td><input type="text" name="j_username"></td>
        </tr>
        <tr>
          <th>Password:</th>
          <td><input type="password" name="j_password"></td>
        </tr>

        <tr>
          <td colspan="2" style="text-align: center">
            <input type="submit" value="Login" name="j_security_check">
          </td>
        </tr>
      </table>
    </form>

  </body>
</html>

