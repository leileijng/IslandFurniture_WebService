package A3_servlets;

import EntityManager.ShippingOrderEntity;
import EntityManager.StaffEntity;
import SCM.InboundAndOutboundLogistics.InboundAndOutboundLogisticsBeanLocal;
import SCM.ManufacturingInventoryControl.ManufacturingInventoryControlBeanLocal;
import SCM.ManufacturingWarehouseManagement.ManufacturingWarehouseManagementBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ShippingOrderLineItemManagement_UpdateServlet extends HttpServlet {

    @EJB
    private InboundAndOutboundLogisticsBeanLocal inboundAndOutboundLogisticsBean;

    @EJB
    private ManufacturingInventoryControlBeanLocal manufacturingInventoryControlBean;

    @EJB
    private ManufacturingWarehouseManagementBeanLocal manufacturingWarehouseManagementBean;
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String shippingOrderId = request.getParameter("id");
            String lineItemId = request.getParameter("lineitemId");
            String sku = request.getParameter("sku");
            String quantity = request.getParameter("quantity");
            String status = request.getParameter("status");
            ShippingOrderEntity shippingOrderEntity = inboundAndOutboundLogisticsBean.getShippingOrderById(Long.parseLong(shippingOrderId));
           
            HttpSession session = request.getSession();
            StaffEntity staff = ((StaffEntity) session.getAttribute("staffEntity"));
            String submittedBy =  staff.getName();
            if (status != null) {
                if (shippingOrderEntity.getStatus().equals("Pending")) {
                    //get shippingOrder

                    List<ShippingOrderEntity> shippingOrders = (List<ShippingOrderEntity>) (session.getAttribute("shippingOrders"));
                    ShippingOrderEntity shippingOrder = new ShippingOrderEntity();
                    for (int i = 0; i < shippingOrders.size(); i++) {
                        if (shippingOrders.get(i).getId() == Integer.parseInt(shippingOrderId)) {
                            shippingOrder = shippingOrders.get(i);
                        }
                    }

                    if (shippingOrder.getLineItems() == null || shippingOrder.getLineItems().isEmpty()) {
                        result = "?errMsg=Empty shipping order cannot be submitted.&id=" + shippingOrderId;
                        response.sendRedirect("A3/shippingOrderManagement_Update.jsp" + result);
                    } else {
                        boolean canUpdate = inboundAndOutboundLogisticsBean.updateShippingOrderStatus(Long.parseLong(shippingOrderId), "Submitted", submittedBy);
                        if (!canUpdate) {
                            result = "?source=isSubmit&errMsg=Failed to submit Shipping Order.&id=" + shippingOrderId;
                            response.sendRedirect("A3/shippingOrderManagement_UpdateLineItem.jsp" + result);
                        } else {
                            result = "?goodMsg=Shipping Order submitted successfully.&id=" + shippingOrderId;
                            response.sendRedirect("ShippingOrderLineItemManagement_Servlet" + result);
                        }
                    }
                } else if (status.equals("Completed")) {
                    if (manufacturingWarehouseManagementBean.getInboundStorageBin(shippingOrderEntity.getDestination().getId()) == null) {
                        result = "?errMsg=Destination warehouse does not have an inbound storage bin.<br/>Please create one first before marking this order as completed.&id=" + shippingOrderId;
                        response.sendRedirect("ShippingOrderLineItemManagement_Servlet" + result);
                    } else {
                        manufacturingInventoryControlBean.moveInboundShippingOrderItemsToReceivingBin(Long.parseLong(shippingOrderId));
                        result = "?goodMsg=Shipping Order updated successfully.&id=" + shippingOrderId;
                        response.sendRedirect("ShippingOrderLineItemManagement_Servlet" + result);
                    }
                } else {
                    boolean canUpdate = inboundAndOutboundLogisticsBean.updateShippingOrderStatus(Long.parseLong(shippingOrderId), status, submittedBy);
                    if (canUpdate) {
                        result = "?goodMsg=Shipping Order updated successfully.&id=" + shippingOrderId;
                        response.sendRedirect("ShippingOrderLineItemManagement_Servlet" + result);
                    } else {
                        result = "?errMsg=Failed to update shipping order to shipped. Outbound bin not found or no items.&id=" + shippingOrderId;
                        response.sendRedirect("ShippingOrderLineItemManagement_Servlet" + result);
                    }
                }

            } else {
                //no need change
                if (!inboundAndOutboundLogisticsBean.checkSKUExists(sku)) {
                    result = "?errMsg=SKU not found.&id=" + shippingOrderId + "&lineItemId=" + lineItemId;
                    response.sendRedirect("A3/shippingOrderManagement_UpdateLineItem.jsp" + result);
                } else {
                    boolean canUpdate = inboundAndOutboundLogisticsBean.updateLineItemFromShippingOrder(Long.parseLong(shippingOrderId), Long.parseLong(lineItemId), sku, Integer.parseInt(quantity));
                    if (!canUpdate) {
                        result = "?errMsg=Shipping Order not found.&id=" + shippingOrderId + "&lineItemId=" + lineItemId;
                        response.sendRedirect("A3/shippingOrderManagement_UpdateLineItem.jsp" + result);
                    } else {
                        result = "?goodMsg=Line Item updated successfully.&id=" + shippingOrderId;
                        response.sendRedirect("ShippingOrderLineItemManagement_Servlet" + result);
                    }
                }
            }
        } catch (Exception ex) {
            out.println("\n\n " + ex.getMessage());
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
