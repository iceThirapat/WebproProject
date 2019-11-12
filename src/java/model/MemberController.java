/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 *
 * @author ICE
 */
public class MemberController {

    public final String REGISTERQUERY = "INSERT INTO MEMBERS (USERNO,USERNAME,PASSWORD,FNAME,LNAME,DEPARTMENTNO,SCHOOL) VALUES (?,?,?,?,?,?,?)";
    public final String FIND_MEMBERAMOUNT = "select * from MEMBERS";
    private final String FIND_LASTUSER = "SELECT MAX(USERNO) AS USERNO FROM MEMBERS";
    private final String FIND_DEPARTMENTNAME = "select departmentname from DEPARTMENT where departmentno='?'";
    private final String FIND_HISTORY = "select subjectno,score,historyno from EXAMHISTORY where userno = '?'";
    private final String FIND_SUBJECTNAME = "select subjectname from SUBJECTS where subjectno = '?'";

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
            Logger.getLogger(MemberController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(MemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getDepartmentName(String departmentno) {
        String name = "";
        Connection conn = DatabaseConnection.getConnection();
        try {
            PreparedStatement pstm = conn.prepareCall(FIND_DEPARTMENTNAME);
            pstm.setString(1, departmentno);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("departmentname");
                }
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return name;
    }

    public ArrayList<History> getHistory(int userNo) {
        ArrayList<History> history = new ArrayList();
 
        String subjectName = "";
        Connection conn = DatabaseConnection.getConnection();
        try {
            PreparedStatement pstm = conn.prepareStatement(FIND_HISTORY);
            pstm.setInt(1, userNo);
            try (ResultSet rsHistory = pstm.executeQuery()) {
                while (rsHistory.next()) {
                    PreparedStatement pstmSubject = conn.prepareStatement(FIND_SUBJECTNAME);
                    pstmSubject.setString(1,rsHistory.getString("subjectno"));
                    ResultSet rsSubject = pstmSubject.executeQuery();
                    if(rsSubject.next()){
                        subjectName = rsSubject.getString("subjectname");
                    }
                    History historyModel = new History(rsHistory.getInt("historyno"),subjectName,rsHistory.getInt("score"));
                    history.add(historyModel);
                }
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return history;
    }

}
