package A5_servlets;

import CommonInfrastructure.AccountManagement.AccountManagementBeanLocal;
import AnalyticalCRM.ValueAnalysis.CustomerValueAnalysisBeanLocal;
import EntityManager.LineItemEntity;
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

public class Analytical_ValueAnalysisServlet extends HttpServlet {

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
            System.out.println("Analytical_ValueAnalysisServlet");
            Double totalCustomerRevenue = customerValueAnalysisBean.totalMemberRevenue();
            Double totalNonCustomerRevenue = customerValueAnalysisBean.totalNonMemberRevenue();

            session.setAttribute("totalCustomerRevenue", totalCustomerRevenue);
            session.setAttribute("totalNonCustomerRevenue", totalNonCustomerRevenue);

            Integer numOfMembers = accountManagementBean.listAllMember().size();
            session.setAttribute("numOfMembers", numOfMembers);

            Integer numOfTransactions = customerValueAnalysisBean.getTotalNumberOfSalesRecord();
            session.setAttribute("numOfTransactions", numOfTransactions);

            List<LineItemEntity> sortBestSellingFurniture = customerValueAnalysisBean.sortBestSellingFurniture();
            Integer totalFurnitureSold = 0;
            for (LineItemEntity lineItem : sortBestSellingFurniture) {
                totalFurnitureSold += lineItem.getQuantity();
            }
            session.setAttribute("totalFurnitureSold", totalFurnitureSold);
            List<LineItemEntity> sortBestSellingRetailProducts = customerValueAnalysisBean.sortBestSellingRetailProducts();
            Integer retailProductSold = 0;
            for (LineItemEntity lineItem : sortBestSellingRetailProducts) {
                retailProductSold += lineItem.getQuantity();
            }
            session.setAttribute("retailProductSold", retailProductSold);

            List<LineItemEntity> sortBestSellingMenuItems = customerValueAnalysisBean.sortBestSellingMenuItem();
            Integer menuItemSold = 0;
            if (!sortBestSellingMenuItems.isEmpty()) {
                for (LineItemEntity lineItem : sortBestSellingMenuItems) {
                    menuItemSold += lineItem.getQuantity();
                }
            }
            session.setAttribute("menuItemSold", menuItemSold);
            
            Integer averageMemberMonetaryValue = customerValueAnalysisBean.getAverageCustomerMonetaryValue();
            session.setAttribute("averageMemberMonetaryValue", averageMemberMonetaryValue);
            response.sendRedirect("A5/valueAnalysis.jsp");

        } catch (Exception ex) {
            out.println("\n\n " + ex.getMessage());
            response.sendRedirect("A5/valueAnalysis.jsp");
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
