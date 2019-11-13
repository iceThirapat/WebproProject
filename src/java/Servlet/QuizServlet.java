/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import controller.QueryController;
import controller.QuizController;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Answer;
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
        QuizController quizControl = (QuizController) session.getAttribute("quiz");
        if (request.getParameter("answer") != null) {
            String answerNo = request.getParameter("answer");
            quizControl.addAnswer(answerNo);
        }
        if (type.equals("next")) {
            quizControl.goNextPage();
            getServletContext().getRequestDispatcher("/WEB-INF/Quiz.jsp").forward(request, response);
        } else if (type.equals("back")) {
            if (quizControl.getPageNo() > 1) {
                quizControl.goPreviosPage();
            } else {
                request.setAttribute("message", "can't go back anymore!!");
            }
        } else if (type.equals("finish")) {
            String[] allAnswer = quizControl.getAllAnswer();
            int score = getScore(allAnswer);
            request.setAttribute("score", score);
            QueryController controller = new QueryController();          
            controller.saveHistory(String.valueOf(quizControl.getUserNo()),quizControl.getSubject().getSubjectno(),score,quizControl.getAllQuestion().size());
            getServletContext().getRequestDispatcher("/WEB-INF/Result.jsp").forward(request, response);
        }
        getServletContext().getRequestDispatcher("/WEB-INF/Quiz.jsp").forward(request, response);

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

    protected int getScore(String[] allAnswer) {
        EntityManager em = emf.createEntityManager();
        int score = 0;
        int index = 0;
        while (index < allAnswer.length) {
            String answerNo = allAnswer[index];
            Answer answer = em.find(Answer.class, answerNo);
            if (answer.getIsright().equals('y')) {
                score++;
            }
            index++;
        }
        return score;
    }

}
