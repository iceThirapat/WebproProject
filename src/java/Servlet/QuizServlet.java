/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import controller.QuizController;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Subjects;

/**
 *
 * @author ICE
 */
public class QuizServlet extends HttpServlet {

    @PersistenceUnit(unitName = "WebProProjectAAAPU")
    EntityManagerFactory emf;

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
        String type = request.getParameter("type");
        request.setAttribute("subject", getSubjectDetail(type));
        getServletContext().getRequestDispatcher("/WEB-INF/QuizDetail.jsp").forward(request, response);
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
        HttpSession session = request.getSession(false);
        String type = request.getParameter("type");
        if (type.equals("next")) {
            QuizController quizControl = (QuizController) session.getAttribute("quiz");
            quizControl.goNextPage();
            getServletContext().getRequestDispatcher("/WEB-INF/Quiz.jsp").forward(request, response);
        } else if (type.equals("back")) {
            QuizController quizControl = (QuizController) session.getAttribute("quiz");
            quizControl.goPreviosPage();
            getServletContext().getRequestDispatcher("/WEB-INF/Quiz.jsp").forward(request, response);
        }
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

    protected Subjects getSubjectDetail(String subjectNo) {
        EntityManager em = emf.createEntityManager();
        Subjects subject = em.find(Subjects.class, subjectNo);
        return subject;
    }

}
