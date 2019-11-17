/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import controller.QueryController;
import controller.QuizController;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Answer;
import model.Department;
import model.History;
import model.Subjects;
import model.User;

/**
 *
 * @author ICE
 */
public class QuizServlet extends HttpServlet {

    @PersistenceUnit(unitName = "WebProProjectAAAPU")
    EntityManagerFactory emf;
    private final String PATH_QUIZ = "/WEB-INF/Quiz.jsp";
    private final String PATH_RESULT = "/WEB-INF/Result.jsp";

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
        String type = null;
        if (request.getParameter("type") != null) {
            type = request.getParameter("type");
        } else {
            request.getServletContext().getRequestDispatcher("/WEB-INF/Quiz.jsp").forward(request, response);
        }
        if ("cancel".equals(type)) {
            HttpSession session = request.getSession(false);
            QuizController quizControl = (QuizController) session.getAttribute("quiz");
            cancelQuiz(request, response, quizControl);
        } else {
            HttpSession session = request.getSession(false);
            EntityManager em = emf.createEntityManager();
            User user = (User) session.getAttribute("user");
            Department userDepartment = em.find(Department.class, user.getDepartmentNo());
            Subjects subject = em.find(Subjects.class, type);
            Collection<Department> departmentOfSubject = subject.getDepartmentCollection();
            Iterator<Department> iterator = departmentOfSubject.iterator();
            while (iterator.hasNext()) {
                Department department = iterator.next();
                if (department.equals(userDepartment)) {
                    request.setAttribute("subject", getSubjectDetail(type));
                    getServletContext().getRequestDispatcher("/WEB-INF/QuizDetail.jsp").forward(request, response);
                    return;
                }
            }
            request.setAttribute("message", "you can't see this subject detail!!");
            getServletContext().getRequestDispatcher("/WEB-INF/MainMenu.jsp").forward(request, response);
        }
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
        if (request.getParameter("type") == null) {
            request.getServletContext().getRequestDispatcher("/WEB-INF/Quiz.jsp").forward(request, response);
        }
        String type = request.getParameter("type");
        QuizController quizControl = (QuizController) session.getAttribute("quiz");
        switch (type) {
            case "next":
                nextQuestion(request, response, quizControl);
                break;
            case "back":
                previousQuestion(request, response, quizControl);
                break;
            case "finish":
                checkAnswer(request, response, quizControl, session);
                break;
            default:
                goToPage(request, response, quizControl);
                break;
        }
    }

    protected void nextQuestion(HttpServletRequest request, HttpServletResponse response, QuizController quizControl) throws ServletException, IOException {
        addAnswer(request, response, quizControl);
        quizControl.goNextPage();
        getServletContext().getRequestDispatcher(PATH_QUIZ).forward(request, response);
    }

    protected void previousQuestion(HttpServletRequest request, HttpServletResponse response, QuizController quizControl) throws ServletException, IOException {
        addAnswer(request, response, quizControl);
        if (quizControl.getPageNo() > 1) {
            quizControl.goPreviosPage();
        } else {
            request.setAttribute("message", "can't go back anymore!!");
        }
        request.getServletContext().getRequestDispatcher(PATH_QUIZ).forward(request, response);
    }

    protected void goToPage(HttpServletRequest request, HttpServletResponse response, QuizController quizControl) throws ServletException, IOException {
        addAnswer(request, response, quizControl);
        int specificPage = Integer.valueOf(request.getParameter("type"));
        quizControl.goSpecificPage(specificPage);
        getServletContext().getRequestDispatcher("/WEB-INF/Quiz.jsp").forward(request, response);
    }

    protected void checkAnswer(HttpServletRequest request, HttpServletResponse response, QuizController quizControl, HttpSession session) throws ServletException, IOException {
        addAnswer(request, response, quizControl);
        String[] allAnswer = quizControl.getAllAnswer();
        for (String element : allAnswer) {
            if (element == null) {
                request.setAttribute("message", "please answer all the question!!");
                request.getServletContext().getRequestDispatcher(PATH_QUIZ).forward(request, response);
                return;
            }
        }
        int score = getScore(allAnswer);
        request.setAttribute("score", score);
        QueryController controller = new QueryController();
        History history = new History();
        history.setSubjectNo(quizControl.getSubject().getSubjectno());
        history.setScore(String.valueOf(score));
        controller.saveHistory(String.valueOf(quizControl.getUserNo()), history, quizControl.getAllQuestion().size());
        session.removeAttribute("quiz");
        getServletContext().getRequestDispatcher(PATH_RESULT).forward(request, response);
    }

    protected void cancelQuiz(HttpServletRequest request, HttpServletResponse response, QuizController quizControl) throws ServletException, IOException {
        QueryController controller = new QueryController();
        History history = new History();
        history.setSubjectNo(quizControl.getSubject().getSubjectno());
        controller.saveHistory(String.valueOf(quizControl.getUserNo()), history, 0);
        request.setAttribute("score", "\"CANCEL\"");
        HttpSession session = request.getSession(false);
        session.removeAttribute("quiz");
        getServletContext().getRequestDispatcher(PATH_RESULT).forward(request, response);
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

    protected void addAnswer(HttpServletRequest request, HttpServletResponse response, QuizController quizControl) throws ServletException, IOException {
        if (request.getParameter("answer") != null) {
            String answerNo = request.getParameter("answer");
            quizControl.addAnswer(answerNo);
        }
    }

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
