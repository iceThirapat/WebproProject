/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import controller.Department;
import controller.Members;
import controller.Subjects;
import model.User;

/**
 *
 * @author ICE
 */
public class LoginServlet extends HttpServlet {

    @PersistenceUnit(unitName = "WebProProjectAAAPU")
    EntityManagerFactory emf;
    String path = "/WEB-INF/Login.jsp";

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
        String userName = request.getParameter("username");
        String passWord = request.getParameter("password");
        if (userName.trim().isEmpty() || passWord.trim().isEmpty()) {
            request.setAttribute("message", "Wrong Password");
            getServletContext().getRequestDispatcher(path).forward(request, response);
        }
        EntityManager em = emf.createEntityManager();
        Query qry = em.createNamedQuery("Members.findByUsername");
        qry.setParameter("username", userName);
        List<Members> list = qry.getResultList();
        Iterator<Members> it = list.iterator();
        while (it.hasNext()) {
            Members member = it.next();
            em.refresh(member);
            if (userName.equals(member.getUsername())) {
                String passwordCheck = member.getPassword();
                if (passwordCheck.equals(passWord)) {
                    HttpSession session = request.getSession(); 
                    User user = new User(userName, passWord,member.getFname(),member.getLname(),member.getDepartmentno().getDepartmentno(),member.getSchool());
                    user.setUserNo(member.getUserno());
                    session.setAttribute("user", user);
                    Department department = em.find(Department.class,member.getDepartmentno().getDepartmentno());
                    Collection<Subjects> subject = department.getSubjectsCollection();
                    session.setAttribute("subject",subject);
                    getServletContext().getRequestDispatcher("/WEB-INF/MainMenu.jsp").forward(request, response);
                }
            }
        }
        request.setAttribute("message", "wrong password!");
        getServletContext().getRequestDispatcher(path).forward(request, response);

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
        getServletContext().getRequestDispatcher(path).forward(request, response);
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
