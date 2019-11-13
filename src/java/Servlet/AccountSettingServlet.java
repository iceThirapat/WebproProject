/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import controller.QueryController;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author ICE
 */
public class AccountSettingServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        String userPassword = user.getPassword();
        String userName = request.getParameter("username");
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String school = request.getParameter("school");
        String newPassword = request.getParameter("newPassword");
        String confirmNewPassword = request.getParameter("confirmNewPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        String message = "";
        if (userName.trim().length() < 8 || userName.trim().length() > 12) {
            message = "username must be at least 8 and less than 13 characters!!";
            sendMessage(message, request, response);
        }
        if (firstName.trim().length() > 20) {
            message = "firstname must less than 21 characters!!";
            sendMessage(message, request, response);
        }
        if (lastName.trim().length() > 20) {
            message = "lastname must less than 21 characters!!";
            sendMessage(message, request, response);
        }
        if (school.trim().length() > 20) {
            message = "school must less than 21 characters!!";
            sendMessage(message, request, response);
        }
        if (newPassword.trim().length() > 0) {
            if (newPassword.trim().length() < 9 || newPassword.trim().length() > 15) {
                message = "new password must be at least 9 and less than 16!!";
                sendMessage(message, request, response);
            }
        }
        if (!(newPassword.equals(confirmNewPassword))) {
            message = "new password and new confirm password must be matched!!";
            sendMessage(message, request, response);
        }
        if (!confirmPassword.equals(userPassword)) {
            message = "confirm password is wrong!!";
            sendMessage(message, request, response);
        }
        if (newPassword.equals(userPassword)) {
            message = "new password is same as old password!!";
            sendMessage(message, request, response);
        } if(message==""){
           
            if (newPassword.trim().length()==0) {
                newPassword = user.getPassword();
                user.setUsername(userName);
                user.setPassword(newPassword);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setSchool(school);
                QueryController memberControl = new QueryController();
                memberControl.settingAccount(user);
                getServletContext().getRequestDispatcher("/WEB-INF/Account.jsp").forward(request, response);
            } else {
                user.setUsername(userName);
                user.setPassword(newPassword);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setSchool(school);
                QueryController memberControl = new QueryController();
                memberControl.settingAccount(user);
                getServletContext().getRequestDispatcher("/WEB-INF/Account.jsp").forward(request, response);
            }
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void sendMessage(String message, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("message", message);
        getServletContext().getRequestDispatcher("/WEB-INF/AccountSetting.jsp").forward(request, response);
        return;
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
