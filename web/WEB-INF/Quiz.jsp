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
        <script>
            //cookies[(pair[0] + '').trim()] = unescape(pair.slice(1).join('='));

            function getTimeCookie() {
                let setCookies = document.cookie.split(";");
                let timeCookie;
                for (let i = 0; i < setCookies.length; i++) {
                    let pair = setCookies[i].split("=");
                    if (pair[0] === "deadLineTime") {
                        if (pair[1] === "") {
                            return null;
                        } else {
                            timeCookie = parseInt(pair[1]);
                            return parseInt(timeCookie);
                        }
                    }
                }
                return null;
            }

            function createTimeCookie(usedTime) {
                let deadLineTime = getTimeCookie();
                if (deadLineTime != null) {
                    return;
                }
                parseInt(usedTime);
                if (usedTime > 1440) {
                    usedTime = 1440;
                } else if (usedTime < 1) {
                    usedTime = 1;
                }
                let deadLine = new Date();
                let now = new Date();
                deadLine.setTime(now.getTime());
                deadLine.setMinutes(now.getMinutes() + usedTime);
                document.cookie = "deadLineTime=" + deadLine.getTime().toString();
            }

            function deleteTimeCookie() {
                document.cookie = "deadLineTime=";
            }

            function timer() {
                let deadLine = getTimeCookie();
                let now = new Date();
                checkRemainingTime(now, deadLine);
                let inter = setInterval(function () {
                    now = new Date();
                    checkRemainingTime(now, deadLine);
                    if ((deadLine - now.getTime()) < 0) {
                        document.getElementById('hour').innerHTML = 0;
                        document.getElementById('minute').innerHTML = 0;
                        document.getElementById('second').innerHTML = 0;
                        clearInterval(inter);
                        alert('time out');
                        deleteTimeCookie();
                        document.location.href = "Quiz?type=timeout";
                    }
                }, 1000);
            }

            function checkRemainingTime(now, deadLine) {
                let timeLeft = deadLine - now.getTime();
                let hours = Math.floor((timeLeft % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                let minutes = Math.floor((timeLeft % (1000 * 60 * 60)) / (1000 * 60));
                let seconds = Math.floor((timeLeft % (1000 * 60)) / 1000);
                document.getElementById('hour').innerHTML = hours;
                document.getElementById('minute').innerHTML = minutes;
                document.getElementById('second').innerHTML = seconds;
            }

            function sure(page) {
                if (document.getElementById(page).style.backgroundColor !== 'green') {
                    document.getElementById(page).style.backgroundColor = 'green';
                    document.getElementById(page).style.color = 'white';
                } else {
                    document.getElementById(page).style.backgroundColor = '';
                    document.getElementById(page).style.color = 'black';
                }
            }

            function openSure() {
                document.getElementById("sureStatus").disabled = false;
            }
           

        </script>
    </head>
    <body onload="createTimeCookie(${quiz.getAllQuestion().size()} * 3), timer()">${message}<br>
        <span>time remaining : </span><span id="hour"></span>:<span id="minute"></span>:<span id="second"></span>
        <h1>${quiz.getSubject().getSubjectname()}</h1><br>
        ${quiz.getPageNo()}) ${quiz.getAllQuestion()[quiz.getPageNo()-1].getQuestion()}<br><br>
        <form action="Quiz" method="post" id="formQuestion">
            <c:forEach items="${quiz.getAllQuestion()[quiz.getPageNo()-1].getAnswerCollection()}" var="answer">
                <input type="radio" name="answer" value="${answer.getAnswerno()}" onchange="openSure()"
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
                    <input type="submit" name="type"  id="finish" value="finish" onclick="submit()"/>
                </c:otherwise>
            </c:choose>
            <input type="checkbox" name="status" value="sure" onchange="sure(${quiz.getPageNo()})" id="sureStatus"
                   <c:if test='${quiz.getAnswerNo(quiz.getPageNo()-1)==null}'>
                       disabled
                   </c:if>
                   <c:if test="${quiz.getStatus()[quiz.getPageNo()-1]==true}">
                       checked                      
                   </c:if>
                   /> sure?<br><br><br>
            <c:forEach items="${quiz.getAllQuestion()}" varStatus="vs">            
                <input type="submit" name="type" value="${vs.count}" id="${vs.count}"
                       <c:if test="${quiz.getStatus()[vs.count-1]==true}">
                           style="background-color:green;color:white"
                       </c:if>
                       />   
            </c:forEach><br><br>
            <a href="Quiz?type=cancel">cancel quiz</a>
        </form>
    </body>
</html>
