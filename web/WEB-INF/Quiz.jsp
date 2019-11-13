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
    <body>
        <h1>${quiz.getSubject().getSubjectname()}</h1><br>
        ${quiz.getAllQuestion().get(Integer.valueOf(String.valueOf(quiz.getPageNo()))).getQuestion()}<br><br>
        <form action="Quiz" method="post">
            <c:forEach items="${quiz.getAllQuestion().get(Integer.valueOf(String.valueOf(quiz.getPageNo()))).getAnswerCollection()}" var="answer">
                <input type="radio" name="answer" value="${answer.getAnswerno()}" 
                       <c:if test="${quiz.getAnswerNo(quiz.getPageNo()-1)==answer.getAnswerno()}">
                           checked                      
                       </c:if>
                       >${answer.getAnswer()}<br>
            </c:forEach>
            <input type="submit" name="type" value="back"/><p>                              </p><input type="submit" name="type" value="next"/>
        </form>
    </body>
</html>
