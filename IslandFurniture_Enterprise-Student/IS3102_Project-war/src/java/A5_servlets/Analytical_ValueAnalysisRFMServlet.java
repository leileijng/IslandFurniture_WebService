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

public class Analytical_ValueAnalysisRFMServlet extends HttpServlet {

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
            System.out.println("Analytical_ValueAnalysisRFMServlet");
            
            List<MemberEntity> members = accountManagementBean.listAllMember();
            List<Integer> memberRecencyValue = new ArrayList();
            for (MemberEntity member : members) {
                memberRecencyValue.add(customerValueAnalysisBean.getCustomerRecency(member.getId()));
            }
            List<Integer> memberFrequencyValue = new ArrayList();
            for (MemberEntity member : members) {
                memberFrequencyValue.add(customerValueAnalysisBean.getCustomerFrequency(member.getId()));
            }
            List<Integer> memberMonetaryValue = new ArrayList();
            for (MemberEntity member : members) {
                memberMonetaryValue.add(customerValueAnalysisBean.getCustomerMonetaryValue(member.getId()));
            }
            
            session.setAttribute("members", members);
            session.setAttribute("memberRecencyValue", memberRecencyValue);
            session.setAttribute("memberFrequencyValue", memberFrequencyValue);
            session.setAttribute("memberMonetaryValue", memberMonetaryValue);

            Integer averageMemberRecency = customerValueAnalysisBean.getAverageCustomerRecency();
            session.setAttribute("averageMemberRecency", averageMemberRecency);

            Integer averageMemberFrequency = customerValueAnalysisBean.getAverageCustomerFrequency();
            session.setAttribute("averageMemberFrequency", averageMemberFrequency);

            Integer averageMemberMonetaryValue = customerValueAnalysisBean.getAverageCustomerMonetaryValue();
            session.setAttribute("averageMemberMonetaryValue", averageMemberMonetaryValue);

            response.sendRedirect("A5/rfm.jsp");

        } catch (Exception ex) {
            out.println("\n\n " + ex.getMessage());
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
