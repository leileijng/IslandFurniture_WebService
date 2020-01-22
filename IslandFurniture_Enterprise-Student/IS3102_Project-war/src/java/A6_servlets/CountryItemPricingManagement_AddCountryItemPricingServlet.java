package A6_servlets;

import CorporateManagement.ItemManagement.ItemManagementBeanLocal;
import HelperClasses.ReturnHelper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CountryItemPricingManagement_AddCountryItemPricingServlet extends HttpServlet {

    @EJB
    private ItemManagementBeanLocal itemManagementBeanLocal;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            String country = request.getParameter("country");
            String sku = request.getParameter("sku");
            String price = request.getParameter("price");
            ReturnHelper helper;
            
            if (itemManagementBeanLocal.checkSKUExists(sku)){
            helper = itemManagementBeanLocal.addCountryItemPricing(Long.parseLong(country), sku, Double.parseDouble(price));
            
            if (!helper.getIsSuccess()) {
                response.sendRedirect("CountryItemPricingManagement_Servlet?errMsg=" + helper.getMessage());
            } else {
                response.sendRedirect("CountryItemPricingManagement_Servlet?errMsg=" + helper.getMessage());
            }
            }
            else {
                response.sendRedirect("CountryItemPricingManagement_Servlet?errMsg=SKU does not exist. Please try again");
            }
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
