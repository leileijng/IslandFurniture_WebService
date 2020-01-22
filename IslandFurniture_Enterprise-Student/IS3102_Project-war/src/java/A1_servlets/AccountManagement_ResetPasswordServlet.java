/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package A1_servlets;

import CommonInfrastructure.AccountManagement.AccountManagementBeanLocal;
import CommonInfrastructure.SystemSecurity.SystemSecurityBeanLocal;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccountManagement_ResetPasswordServlet extends HttpServlet {

    @EJB
    private AccountManagementBeanLocal accountManagementBean;
    @EJB
    private SystemSecurityBeanLocal systemSecurityBean;
    
    private String result;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            HttpSession session;
            session = request.getSession();

            String email = request.getParameter("email");
            String resetCode = request.getParameter("resetCode");
            String password = request.getParameter("password");
            
            if (systemSecurityBean.validatePasswordResetForStaff(email, resetCode)) {
                accountManagementBean.resetStaffPassword(email, password);
                result = "?goodMsg=Reset Password Successful. Please login with your new password.";
                response.sendRedirect("./A1/staffLogin.jsp" + result);
            } else {
                result = "?errMsg=Reset Password Unsuccessful. Please key in the correct reset code.";
                response.sendRedirect("./A1/staffResetPasswordCode.jsp" + result);
            }

        } catch (Exception ex) {
            response.sendRedirect("A1/error.jsp?errMsg="+ex);
            ex.printStackTrace();
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
        processRequest(request, response);
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
