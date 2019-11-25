/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import controller.QuizController;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Department;
import model.Question;
import model.Subjects;
import model.User;

/**
 *
 * @author ICE
 */
public class StartQuizServlet extends HttpServlet {

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
        HttpSession session = request.getSession(false);
        if (session.getAttribute("quiz") != null) {
            request.getServletContext().getRequestDispatcher("/WEB-INF/Quiz.jsp").forward(request, response);
        } else {
            EntityManager em = emf.createEntityManager();
            User user = (User) session.getAttribute("user");
            Department userDepartment = em.find(Department.class, user.getDepartmentNo());
            String subjectNo = request.getParameter("subject");
            Subjects subject = em.find(Subjects.class, subjectNo);
            Collection<Department> departmentOfSubject = subject.getDepartmentCollection();
            Iterator<Department> iterator = departmentOfSubject.iterator();
            while (iterator.hasNext()) {
                Department department = iterator.next();
                if (department.equals(userDepartment)) {
                    int userNo = user.getUserNo();
                    Collection<Question> allQuestion = subject.getQuestionCollection();
                    QuizController quizControl = new QuizController(userNo, subject, allQuestion.size());
                    Iterator<Question> iteratorQuestion = allQuestion.iterator();
                    int count = 0;
                    while (iteratorQuestion.hasNext()) {
                        Question question = iteratorQuestion.next();
                        quizControl.getAllQuestion().put(++count, question);
                    }
                    Cookie cookie = new Cookie("deadLineTime", "");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    session.setAttribute("quiz", quizControl);
                    getServletContext().getRequestDispatcher("/WEB-INF/Quiz.jsp").forward(request, response);
                    return;
                }
            }
            request.setAttribute("message", "you can't quiz this subject!!");
            getServletContext().getRequestDispatcher("/WEB-INF/MainMenu.jsp").forward(request, response);
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
        if (request.getParameter("type") != null) {
            String subjectNo = request.getParameter("type");
            HttpSession session = request.getSession(false);
            EntityManager em = emf.createEntityManager();
            User user = (User) session.getAttribute("user");
            Department userDepartment = em.find(Department.class, user.getDepartmentNo());
            Subjects subject = em.find(Subjects.class, subjectNo);
            Collection<Department> departmentOfSubject = subject.getDepartmentCollection();
            Iterator<Department> iterator = departmentOfSubject.iterator();
            while (iterator.hasNext()) {
                Department department = iterator.next();
                if (department.equals(userDepartment)) {
                    request.setAttribute("subject", getSubjectDetail(subjectNo));
                    getServletContext().getRequestDispatcher("/WEB-INF/QuizDetail.jsp").forward(request, response);
                    return;
                }
            }
            request.setAttribute("message", "you can't see this subject detail!!");
            getServletContext().getRequestDispatcher("/WEB-INF/MainMenu.jsp").forward(request, response);
        } else {
            processRequest(request, response);
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

    protected Subjects getSubjectDetail(String subjectNo) {
        EntityManager em = emf.createEntityManager();
        Subjects subject = em.find(Subjects.class, subjectNo);
        return subject;
    }

}
