package A6_servlets;

import CorporateManagement.ItemManagement.ItemManagementBeanLocal;
import EntityManager.ProductGroupEntity;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ProductGroupLineItemManagement_UpdateServlet extends HttpServlet {

    @EJB
    private ItemManagementBeanLocal ItemManagementBean;
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session;
            session = request.getSession();
            String productGroupId = request.getParameter("id");
            String lineitemId = request.getParameter("lineitemId");
            String sku = request.getParameter("sku");

            //    result = "A6/productGroupManagement_UpdateLineItem.jsp?errMsg=Item successfully updated.&id=" + productGroupId + "&lineItemId=" + lineitemId;
            //out.println("<h1>" + result + "</h1>");
            if (!ItemManagementBean.checkSKUExists(sku)) {
                result = "?errMsg=SKU not found.&id=" + productGroupId + "&lineItemId=" + lineitemId;
                response.sendRedirect("A6/productGroupManagement_UpdateLineItem.jsp" + result);
            } else if (!ItemManagementBean.checkIfSKUIsFurniture(sku)) {
                result = "?errMsg=SKU is not a funriture.&id=" + productGroupId + "&lineItemId=" + lineitemId;
                response.sendRedirect("A6/productGroupManagement_UpdateLineItem.jsp" + result);
            } else {
                boolean canUpdate = ItemManagementBean.editProductGroupLineItem(Long.parseLong(productGroupId), Long.parseLong(lineitemId), sku, 0.01);
                if (!canUpdate) {
                    result = "?errMsg=Unable to update product group: Total percentage exceeds 100.&id=" + productGroupId + "&lineItemId=" + lineitemId;
                    response.sendRedirect("A6/productGroupManagement_UpdateLineItem.jsp" + result);
                } else {
                    List<ProductGroupEntity> productGroups = ItemManagementBean.getAllProductGroup();
                    session.setAttribute("productGroups", productGroups);
                    result = "?goodMsg=Item successfully updated.&id=" + productGroupId + "&lineItemId=" + lineitemId;
                    response.sendRedirect("A6/productGroupManagement_UpdateLineItem.jsp" + result);
                }
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
