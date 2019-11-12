/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Department;
import model.MemberController;
import model.User;

/**
 *
 * @author ICE
 */
public class RegisterServlet extends HttpServlet {
@PersistenceUnit(unitName="WebProProjectAAAPU")
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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegisterServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        this.getAllDepartment(request,response);
        getServletContext().getRequestDispatcher("/WEB-INF/Register.jsp").forward(request, response);
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
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String department = request.getParameter("department");
        String school = request.getParameter("school");
        if(userName.trim().length()<8||userName.trim().length()>12){
            request.setAttribute("message","username must be at least 8 and less than 13 characters!!");
            this.getAllDepartment(request,response);
            getServletContext().getRequestDispatcher("/WEB-INF/Register.jsp").forward(request, response);
        }else if(password.trim().length()<9||password.trim().length()>15){
            request.setAttribute("message","password must be at least 9 and less than 16!!");
            this.getAllDepartment(request,response);
            getServletContext().getRequestDispatcher("/WEB-INF/Register.jsp").forward(request, response);
        }else if(!password.equals(confirmPassword)){
            request.setAttribute("message","password and confirm password must be matched!!");
            this.getAllDepartment(request,response);
            getServletContext().getRequestDispatcher("/WEB-INF/Register.jsp").forward(request, response);
        }else{
        User newUser = new User(userName, password, firstName, lastName, department, school);
        MemberController memberQRY = new MemberController();
        memberQRY.register(newUser);
        getServletContext().getRequestDispatcher("/WEB-INF/Login.jsp").forward(request, response);
        }
    }

    protected void getAllDepartment(HttpServletRequest request, HttpServletResponse response){
        EntityManager em = emf.createEntityManager();
        Query qry = em.createNamedQuery("Department.findAll");
        List<Department> department = qry.getResultList();
        request.setAttribute("department",department);
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
