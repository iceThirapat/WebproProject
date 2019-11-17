<%-- 
    Document   : Quiz
    Created on : 13 พ.ย. 2562, 22:14:56
    Author     : ICE
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quiz Page</title>
    </head>
    <body>${message}<br>
        <h1>${quiz.getSubject().getSubjectname()}</h1><br>
        ${quiz.getPageNo()}) ${quiz.getAllQuestion().get(quiz.getPageNo()).getQuestion()}<br><br>
        <form action="Quiz" method="post">
            <c:forEach items="${quiz.getAllQuestion().get(quiz.getPageNo()).getAnswerCollection()}" var="answer">
                <input type="radio" name="answer" value="${answer.getAnswerno()}" 
                       <c:if test="${quiz.getAnswerNo(quiz.getPageNo()-1)==answer.getAnswerno()}">
                           checked                      
                       </c:if>
                       >${answer.getAnswer()}<br>
            </c:forEach>
            <input type="submit" name="type" value="back"/><p>                              
            </p>
            <c:choose>
                <c:when test="${quiz.getPageNo()<quiz.getAllQuestion().size()}">
                    <input type="submit" name="type" value="next"/>
                </c:when>
                <c:otherwise>
                   <input type="submit" name="type" value="finish"/>
                </c:otherwise>
            </c:choose><br><br><br>
                <c:forEach items="${quiz.getAllQuestion()}" varStatus="vs">
                    <input type="submit" name="type" value="${vs.count}"/>   
                </c:forEach><br><br>
                <a href="Quiz?type=cancel">cancel quiz</a>
        </form>
        <a
    </body>
</html>
