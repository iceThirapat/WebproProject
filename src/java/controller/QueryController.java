/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.History;
import model.User;

/**
 *
 * @author ICE
 */
public class QueryController {

    public final String REGISTERQUERY = "INSERT INTO MEMBERS (USERNO,USERNAME,PASSWORD,FNAME,LNAME,DEPARTMENTNO,SCHOOL) VALUES (?,?,?,?,?,?,?)";
    public final String FIND_MEMBERAMOUNT = "select * from MEMBERS";
    private final String FIND_LASTUSER = "SELECT MAX(USERNO) AS USERNO FROM MEMBERS";
    private final String FIND_LASTHISTORY = "SELECT MAX(HISTORYNO) AS historyno FROM examhistory";
    private final String SETTING = "UPDATE members set username=?,password=?,fname=?,lname=?,departmentno=?,school=? where userno = ?";
    private final String SAVE_HISTORY = "insert into examhistory (historyno,userno,subjectno,score) values (?,?,?,?)";

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
            Logger.getLogger(QueryController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(QueryController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(QueryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int findLastHistoryNo() {
        int i = 0;
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstm = conn.prepareStatement(FIND_LASTHISTORY);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                i = rs.getInt("historyno");
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(QueryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    public void saveHistory(String userNo,History history, int maxScore) {
        try {
            try (Connection conn = DatabaseConnection.getConnection()) {
                PreparedStatement pstm = conn.prepareStatement(SAVE_HISTORY);
                pstm.setInt(1, findLastHistoryNo() + 1);
                pstm.setString(2, userNo);
                pstm.setString(3, history.getSubjectNo());
                if (maxScore==0) {
                    pstm.setString(4,"cancel");
                }else if(maxScore==-1){
                    pstm.setString(4,"timeout");
                } 
                else {
                    pstm.setString(4, history.getScore() + '/' + String.valueOf(maxScore));
                }
                pstm.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(QueryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
