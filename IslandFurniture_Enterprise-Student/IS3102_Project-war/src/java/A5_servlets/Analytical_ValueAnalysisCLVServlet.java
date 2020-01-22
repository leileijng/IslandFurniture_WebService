package A5_servlets;

import CommonInfrastructure.AccountManagement.AccountManagementBeanLocal;
import AnalyticalCRM.ValueAnalysis.CustomerValueAnalysisBeanLocal;
import EntityManager.MemberEntity;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Analytical_ValueAnalysisCLVServlet extends HttpServlet {

    @EJB
    private CustomerValueAnalysisBeanLocal customerValueAnalysisBean;
    @EJB
    private AccountManagementBeanLocal accountManagementBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session;
            session = request.getSession();
            String errMsg = request.getParameter("errMsg");
            String goodMsg = request.getParameter("goodMsg");
            System.out.println("Analytical_ValueAnalysisCLVServlet");

            List<MemberEntity> members = accountManagementBean.listAllMember();
            session.setAttribute("members",members);
            Double customerRetentionRate = customerValueAnalysisBean.getCustomerRetentionRate();
            session.setAttribute("customerRetentionRate", customerRetentionRate);

            Double averageOrdersPerAcquiredYear = customerValueAnalysisBean.averageOrdersPerAcquiredYear();
            session.setAttribute("averageOrdersPerAcquiredYear", averageOrdersPerAcquiredYear);
            
            Double averageOrderPriceInAcquiredYear = customerValueAnalysisBean.averageOrderPriceInAcquiredYear();
            session.setAttribute("averageOrderPriceInAcquiredYear",averageOrderPriceInAcquiredYear);
            
            List<MemberEntity> retainedMembers = customerValueAnalysisBean.getRetainedMembers();
            Double getRetainedCustomerRetentionRate = customerValueAnalysisBean.getRetainedCustomerRetentionRate(retainedMembers);
            session.setAttribute("getRetainedCustomerRetentionRate",getRetainedCustomerRetentionRate);

            Double averageOrdersPerRetainedMember = customerValueAnalysisBean.averageOrdersPerRetainedMember();
            session.setAttribute("averageOrdersPerRetainedMember",averageOrdersPerRetainedMember);
            
            Double averageOrderPriceForRetainedMembers = customerValueAnalysisBean.averageOrderPriceForRetainedMembers();
            session.setAttribute("averageOrderPriceForRetainedMembers",averageOrderPriceForRetainedMembers);
            
            Double getEstimatedCustomerLife = customerValueAnalysisBean.getEstimatedCustomerLife();
            session.setAttribute("getEstimatedCustomerLife", getEstimatedCustomerLife);
            
            Integer avgMonetaryValue = customerValueAnalysisBean.getAverageCustomerMonetaryValue();
            session.setAttribute("avgMonetaryValue",avgMonetaryValue);
            
            response.sendRedirect("A5/clv.jsp");

        } catch (Exception ex) {
            response.sendRedirect("A5/clv.jsp");
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
