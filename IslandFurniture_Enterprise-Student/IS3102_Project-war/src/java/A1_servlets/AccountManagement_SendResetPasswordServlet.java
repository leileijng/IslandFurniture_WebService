package A1_servlets;

import CommonInfrastructure.AccountManagement.AccountManagementBeanLocal;
import CommonInfrastructure.SystemSecurity.SystemSecurityBeanLocal;
import EntityManager.StaffEntity;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccountManagement_SendResetPasswordServlet extends HttpServlet {

    @EJB
    private SystemSecurityBeanLocal systemSecurityBean;
    private String result;
    @EJB
    private AccountManagementBeanLocal accountManagementBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            String email = request.getParameter("email");
            String securityAnswer = request.getParameter("securityAnswer");
            
            StaffEntity staff = accountManagementBean.getStaffByEmail(email);
            if (securityAnswer.equals(staff.getSecurityAnswer())) {
                systemSecurityBean.sendPasswordResetEmailForStaff(email);
                result = "?goodMsg=Password reset code sent. Check your email for the code to be filled in below.&email=" + email;
                response.sendRedirect("./A1/staffResetPasswordCode.jsp" + result);
            } else {
                result = "?errMsg=Security answer is not correct.";
                response.sendRedirect("./A1/staffForgetPasswordSecurity.jsp" + result);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            System.out.close();
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
