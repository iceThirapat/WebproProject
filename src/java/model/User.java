/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ICE
 */
public class User {

    public String username;
    public String password;
    public String firstName;
    public String lastName;
    public String departmentNo;
    public String school;
    public int userNo;

    public User(String username, String password, String firstName, String lastName, String departmentNo, String school) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.departmentNo = departmentNo;
        this.school = school;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDepartmentNo() {
        return departmentNo;
    }

    public void setDepartmentNo(String departmentNo) {
        this.departmentNo = departmentNo;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public String getDepartmentName() {
        String name = "no";
        Connection conn = DatabaseConnection.getConnection();
        try {
            PreparedStatement pstm = conn.prepareStatement("select departmentname from DEPARTMENT where departmentno = ?");
            pstm.setString(1, this.getDepartmentNo());
            ResultSet rs = pstm.executeQuery();
            rs.next();
            name = rs.getString("departmentname");
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return name;
    }

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", password=" + password + ", firstName=" + firstName + ", lastName=" + lastName + ", departmentNo=" + departmentNo + ", school=" + school + '}';
    }

}
