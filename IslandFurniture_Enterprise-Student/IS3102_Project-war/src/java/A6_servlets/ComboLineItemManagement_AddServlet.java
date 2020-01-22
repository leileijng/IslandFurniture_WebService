package A6_servlets;

import CorporateManagement.RestaurantManagement.RestaurantManagementBeanLocal;
import EntityManager.ComboLineItemEntity;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ComboLineItemManagement_AddServlet extends HttpServlet {

    @EJB
    private RestaurantManagementBeanLocal RestaurantManagementBean;
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String comboId = request.getParameter("id");
            String sku = request.getParameter("sku");

            ComboLineItemEntity comboLineItemEntity = RestaurantManagementBean.createComboLineItem(sku);
            boolean canUpdate = false;
            if (comboLineItemEntity != null) {
                canUpdate = RestaurantManagementBean.addLineItemToCombo(Long.parseLong(comboId), comboLineItemEntity.getId());
            }
            if (!canUpdate) {
                result = "?errMsg=Add combo item failed. Please check SKU.&id=" + comboId;
                response.sendRedirect("A6/comboManagement_AddLineItem.jsp" + result);
            } else {
                result = "?goodMsg=Line item added successfully.&id=" + comboId;
                response.sendRedirect("ComboLineItemManagement_Servlet" + result);
            }

        } catch (Exception ex) {
            out.println(ex);
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
