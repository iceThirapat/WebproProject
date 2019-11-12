/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.DatabaseConnection;
import model.Department;
import model.User;

/**
 *
 * @author ICE
 */
public class MemberQuery {

    public final String REGISTERQUERY = "INSERT INTO MEMBERS (USERNO,USERNAME,PASSWORD,FNAME,LNAME,DEPARTMENTNO,SCHOOL) VALUES (?,?,?,?,?,?,?)";
    public final String FIND_MEMBERAMOUNT = "select * from MEMBERS";
    private final String FIND_LASTUSER = "SELECT MAX(USERNO) AS USERNO FROM MEMBERS";
    private final String SETTING = "UPDATE members set username=?,password=?,fname=?,lname=?,departmentno=?,school=? where userno = ?";

    public int findLastUserId() {
        int i = 0;
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstm = conn.prepareStatement(FIND_LASTUSER);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                i = rs.getInt("USERNO");
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(MemberQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    public void register(User newUser) {
        try {
            try (Connection conn = DatabaseConnection.getConnection()) {
                PreparedStatement pstm = conn.prepareStatement(REGISTERQUERY);
                pstm.setInt(1, findLastUserId() + 1);
                pstm.setString(2, newUser.getUsername());
                pstm.setString(3, newUser.getPassword());
                pstm.setString(4, newUser.getFirstName());
                pstm.setString(5, newUser.getLastName());
                pstm.setString(6, newUser.getDepartmentNo());
                pstm.setString(7, newUser.getSchool());
                pstm.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(MemberQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void settingAccount(User newUser) {
        try {
            try (Connection conn = DatabaseConnection.getConnection()) {
                PreparedStatement pstm = conn.prepareStatement(SETTING);
                pstm.setString(1, newUser.getUsername());
                pstm.setString(2, newUser.getPassword());
                pstm.setString(3, newUser.getFirstName());
                pstm.setString(4, newUser.getLastName());
                pstm.setString(5, newUser.getDepartmentNo());
                pstm.setString(6, newUser.getSchool());
                pstm.setString(7, String.valueOf(newUser.getUserNo()));
                pstm.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(MemberQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   

    
}
