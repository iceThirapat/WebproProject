<%-- 
    Document   : Account
    Created on : 11 พ.ย. 2562, 19:50:22
    Author     : ICE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Account</title>
    </head>
    <body>
        <h1>Account</h1><div align="right"><a href="History?type=link">exam history</a></div>
        <table border="1 solid">
            <tr>
                <th>username</th><td>${user.getUsername()}</td>
            </tr>
            <tr>
                <th>first name</th><td>${user.getFname()}</td>
            </tr>
            <tr>
                <th>last name</th><td>${user.getLname()}</td>
            </tr>
            <tr>
                <th>department</th><td>${user.getDepartmentno().getDepartmentname()}</td>
            </tr>
            <tr>
                <th>school</th><td>${user.getSchool()}</td>
            </tr>
        </table><br><br>
        <a href="Account?type=back">back</a><p>                </p><a href="AccountServlet">setting</a>
    </body>
</html>
