<%-- 
    Document   : Result
    Created on : 14 พ.ย. 2562, 1:00:15
    Author     : ICE
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Result</title>
    </head>
    <body>
        <h1>Your Score</h1><br>
        your score is ${score}<br>
        <table border="1 solid">
            <tr>
                <th>no.</th><th>your answer</th><th>IS CORRECT</th>
            </tr>
            <c:forEach items="${userAnswer}" var="ans" varStatus="vs">
                <tr>
                    <td>${vs.count}</td>
                    <td>${ans.getAnswer()}</td>
                    <c:choose>
                        <c:when test="${ans.getIsright()=='t'}">
                            <td style="color:green"> correct </td>
                        </c:when>
                        <c:otherwise>
                            <td style="color:red"> wrong</td>
                        </c:otherwise>
                    </c:choose>
                </tr>
            </c:forEach>
        </table>
        <a href="Account?type=back">back</a>${s}
    </body>
</html>
