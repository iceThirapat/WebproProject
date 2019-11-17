<%-- 
    Document   : MainMenu
    Created on : 10 พ.ย. 2562, 18:26:54
    Author     : ICE
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Main Menu</title>
        <style>
        </style>
    </head>
    <body>${message}<br>
        <p>${user.getFirstName()}    ${user.getLastName()}</p><div align="right">
            <a href="Account?type=link" >ACCOUNT</a>
        </div><br>
        <c:forEach items='${subject}' var="s">
                <p>
                    <a href="Quiz?type=${s.getSubjectno()}">${s.getSubjectname()}</a>
                </p>
        </c:forEach>
        <br><br>
        <a href="Logout">Logout</a>
    </body>
</html>
