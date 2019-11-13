<%-- 
    Document   : QuizDetail
    Created on : 13 พ.ย. 2562, 22:56:31
    Author     : ICE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quiz Detail</title>
    </head>
    <body>
        <h1>${subject.getSubjectname()}</h1><br>
        <p>Detail
        ${subject.getSubjectdetail()}
        </p><br>
        <a href="Account?type=back">back</a><br>
        <a href="StartQuiz?subject=${subject.getSubjectno()}">start quiz</a><br>
    </body>
</html>
