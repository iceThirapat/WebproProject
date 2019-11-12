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
        <h1>History</h1>
        <table border="1 solid">
            <tr>
                <th>No.</th><th>subject</th><th>score</th>
            </tr>
            <c:forEach items="${history}" var="h" varStatus='vs'>
            <tr>
                <td>${vs.count}</td>${h.getSubjectName()}<td>${h.getScore()}</td>
            </tr>
            </c:forEach>
        </table><br><br>
        <a href="History?type=back">back</a>
    </body>
</html>
