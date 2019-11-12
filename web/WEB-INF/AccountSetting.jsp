<%-- 
    Document   : AccountSetting
    Created on : 12 พ.ย. 2562, 21:21:39
    Author     : ICE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Account Setting</title>
    </head>
    <body>
        <h1>Account Setting</h1><br>
        ${message}<br>
        <form action="AccountSetting" method="post">
            username <input type="text" required name="username" value="${user.getUsername()}">*<br>
            firstname <input type="text" required name="firstname" value="${user.getFirstName()}">*<br>
            lastname <input type="text" required name="lastname" value="${user.getLastName()}">*<br>
            department <select>
            <option selected>${user.getDepartmentName()}</option>
            </select>
            school<input type="text" required name="school" value="${user.getSchool()}">*<br>
            new password<input type="password" name="newPassword" placeholder="optional"><br>
            confirm new password<input type="password" name="confirmNewPassword" placeholder="optional"><br>
            INSERT PASSWORD TO CONFIRM<input type="password" required name="confirmPassword"><br>
            <input type="submit" value="change"><a href="Account?type=link">back</a>
        </form>
    </body>
</html>
