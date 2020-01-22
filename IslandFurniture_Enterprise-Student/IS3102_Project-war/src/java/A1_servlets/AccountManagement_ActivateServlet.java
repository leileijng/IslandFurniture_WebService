package A1_servlets;

import CommonInfrastructure.SystemSecurity.SystemSecurityBeanLocal;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccountManagement_ActivateServlet extends HttpServlet {

    @EJB
    private SystemSecurityBeanLocal systemSecurityBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            HttpSession session;
            session = request.getSession();

            String email = request.getParameter("email");
            String activationCode = request.getParameter("activationCode");
            String accountType = request.getParameter("accountType");
            if (email == null || activationCode == null || accountType == null) {
                if (accountType != null && accountType.equals("member")) {
                    response.sendRedirect("A1/memberActivateAccount.jsp");
                } else if (accountType != null && accountType.equals("staff")) {
                    response.sendRedirect("A1/staffActivateAccount.jsp");
                } else {
                    response.sendRedirect("A1/error.jsp");
                }
            } else {
                if (accountType.equals("staff")) {
                    if (systemSecurityBean.activateStaffAccount(email, activationCode)) {
                        response.sendRedirect("A1/staffLogin.jsp?goodMsg=Account activated successfully. Please login with your username and activation code.");
                    } else {
                        response.sendRedirect("A1/staffActivateAccount.jsp?errMsg=Activation Failed. Email or activation code is invalid.");
                    }
                } else if (accountType.equals("member")) {
                    if (systemSecurityBean.activateMemberAccount(email, activationCode)) {
                        response.sendRedirect("A1/memberActivateAccount.jsp?goodMsg=Account activated successfully.");
                    } else {
                        response.sendRedirect("A1/memberActivateAccount.jsp?errMsg=Activation Failed. Email or activation code is invalid.");
                    }
                }
            }

        } catch (Exception ex) {
            response.sendRedirect("A1/error.jsp?errMsg=" + ex);
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
