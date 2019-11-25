<%-- 
    Document   : Register
    Created on : 11 พ.ย. 2562, 14:41:57
    Author     : ICE
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register</title>
    </head>
    <body>
        ${message}<br>
        <h1>Register</h1><br>
        <form method="post" action="Register">
            username <input type="text" required name="username" title="Must contain at least 8 and less than 12 characters"><br>
            password <input type="password" required name="password" ><br>
            confirm password <input type="password" required name="confirmPassword" ><br>
            firstname <input type="text" required name="firstname"><br>
            lastname <input type="text" required name="lastname"><br>
            department <select name="department">
                <c:forEach items="${department}" var="depart">
                    <option value="${depart.getDepartmentno()}">${depart.getDepartmentname()}</option>
                </c:forEach>
            </select>
            school<input type="text" required name="school"><br>
            <input type="submit" value="submit">
        </form><br><a href="Login">go to Login</a><br>
        <a href="Index.jsp">back</a>
    </body>
</html>
