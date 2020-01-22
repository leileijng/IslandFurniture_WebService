/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package A5_servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import CommonInfrastructure.SystemSecurity.SystemSecurityBeanLocal;
import javax.ejb.EJB;

public class Analytical_ValueAnalysisSendLoyaltyRPServlet extends HttpServlet {

    @EJB
    SystemSecurityBeanLocal systemSecurityBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session = request.getSession();

            String[] deleteArr = request.getParameterValues("delete");
            Integer loyaltyPoints = 0;
            if (request.getParameter("loyaltyPoints") != null && request.getParameter("loyaltyPoints") != "") {
                loyaltyPoints = Integer.valueOf(request.getParameter("loyaltyPoints"));
            }
            if (deleteArr != null && loyaltyPoints != 0) {
                for (int i = 0; i < deleteArr.length; i++) {
                    systemSecurityBean.discountMemberLoyaltyPoints(deleteArr[i], loyaltyPoints);
                }
                response.sendRedirect("A5/rfmRetailProduct.jsp?goodMsg=Successfully sent loyalty points : " + deleteArr.length + " record(s).");
            } else {
                response.sendRedirect("A5/rfmRetailProduct.jsp?errMsg=Nothing is selected or no loyalty points enter.");
            }
        } catch (Exception ex) {
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
