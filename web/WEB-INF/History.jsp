<%-- 
    Document   : History
    Created on : 11 พ.ย. 2562, 21:54:02
    Author     : ICE
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>History</title>
    </head>
    <body>
        <h1>History</h1><div align="right"><a href="History?type=back">back</a></div>
        <table border="1 solid">
            <tr>
                <th>No.</th><th>subject</th><th>score</th>
            </tr>
            <c:forEach items="${history}" var="h" varStatus='vs'>
            <tr>
                <td>${vs.count}</td><td>${h.getSubjectName()}</td><td>${h.getScore()}</td>
            </tr>
            </c:forEach>
        </table><br>${message}<br>
    </body>
</html>
