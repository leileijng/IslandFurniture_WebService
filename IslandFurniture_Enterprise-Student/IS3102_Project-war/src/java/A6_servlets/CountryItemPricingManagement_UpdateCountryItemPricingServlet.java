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

public class CountryItemPricingManagement_UpdateCountryItemPricingServlet extends HttpServlet {

    @EJB
    private ItemManagementBeanLocal itemManagementBean;
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String id = request.getParameter("id");
            String price = request.getParameter("setPrice");
            if (Double.parseDouble(price)>99999 || Double.parseDouble(price)<0){
              response.sendRedirect("CountryItemPricingManagement_Servlet?errMsg=Price should be between 0 and 99999, please try again.");
            }
            else {
                //make sure 1 decimal place                
                price = String.valueOf(Math.round(Double.parseDouble(price)*10)/10.0);
            ReturnHelper helper = itemManagementBean.editCountryItemPricing(Long.parseLong(id), Double.parseDouble(price));

            response.sendRedirect("CountryItemPricingManagement_Servlet?errMsg=" + helper.getMessage());
            }
        } catch (Exception ex) {
            out.println(ex);
            response.sendRedirect("CountryItemPricingManagement_Servlet?errMsg=Failed to update, please try again.");
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