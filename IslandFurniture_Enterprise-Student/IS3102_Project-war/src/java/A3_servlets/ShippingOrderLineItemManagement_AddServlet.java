package A3_servlets;

import SCM.InboundAndOutboundLogistics.InboundAndOutboundLogisticsBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShippingOrderLineItemManagement_AddServlet extends HttpServlet {

    @EJB
    private InboundAndOutboundLogisticsBeanLocal inboundAndOutboundLogisticsBeanLocal;
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String shippingOrderId = request.getParameter("id");
            String sku = request.getParameter("sku");
            String quantity = request.getParameter("quantity");

            if (!inboundAndOutboundLogisticsBeanLocal.checkSKUExists(sku)) {
                result = "?errMsg=SKU not found.&id=" + shippingOrderId;
                response.sendRedirect("A3/shippingOrderManagement_AddLineItem.jsp" + result);
            } else {
                boolean canUpdate = inboundAndOutboundLogisticsBeanLocal.addLineItemToShippingOrder(Long.parseLong(shippingOrderId), sku, Integer.parseInt(quantity));
                if (!canUpdate) {
                    result = "?errMsg=Shipping Order not found.&id=" + shippingOrderId;
                    response.sendRedirect("A3/shippingOrderManagement_AddLineItem.jsp" + result);
                } else {
                    result = "?goodMsg=Line item added successfully.&id=" + shippingOrderId;
                    response.sendRedirect("ShippingOrderLineItemManagement_Servlet" + result);
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
