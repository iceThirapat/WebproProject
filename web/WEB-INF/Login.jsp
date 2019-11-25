<%-- 
    Document   : Login
    Created on : 10 พ.ย. 2562, 17:29:00
    Author     : ICE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>${user}
        <h1>Login</h1>
        <br>${message}<br>
        <form method="post" action="Login">
            <p>Username : 
                <input type="text" required name="username"><br>    </p>
            </input>
            <p>Password : 
                <input type="password" required name="password"><br>    </P>
            </input>
            <input type="submit" value="OK"><br><br>if you don't have account register here=><a href="Register?type=first">REGISTER</a>
        </form><br>
        <a href="Index.jsp">back</a>
    </body>
</html>
