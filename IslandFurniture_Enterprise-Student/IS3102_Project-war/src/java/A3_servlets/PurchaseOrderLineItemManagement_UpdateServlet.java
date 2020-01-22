package A3_servlets;

import EntityManager.PurchaseOrderEntity;
import EntityManager.StaffEntity;
import SCM.ManufacturingInventoryControl.ManufacturingInventoryControlBeanLocal;
import SCM.ManufacturingWarehouseManagement.ManufacturingWarehouseManagementBeanLocal;
import SCM.RetailProductsAndRawMaterialsPurchasing.RetailProductsAndRawMaterialsPurchasingBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PurchaseOrderLineItemManagement_UpdateServlet extends HttpServlet {

    @EJB
    private RetailProductsAndRawMaterialsPurchasingBeanLocal retailProductsAndRawMaterialsPurchasingBean;

    @EJB
    private ManufacturingInventoryControlBeanLocal manufacturingInventoryControlBean;

    @EJB
    private ManufacturingWarehouseManagementBeanLocal manufacturingWarehouseManagementBean;

    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String purchaseOrderId = request.getParameter("id");
            String lineItemId = request.getParameter("lineitemId");
            String sku = request.getParameter("sku");
            String quantity = request.getParameter("quantity");
            String status = request.getParameter("status");
            String destinationWarehouseID = request.getParameter("destinationWarehouseID");
            PurchaseOrderEntity purchaseOrderEntity = retailProductsAndRawMaterialsPurchasingBean.getPurchaseOrderById(Long.parseLong(purchaseOrderId));
            
            HttpSession session = request.getSession();
            StaffEntity staff = ((StaffEntity) session.getAttribute("staffEntity"));
            String submittedBy =  staff.getName();
            if (status != null) {
                if (purchaseOrderEntity.getStatus().equals("Pending")) {
                    //get purchase order

                    List<PurchaseOrderEntity> purchaseOrders = (List<PurchaseOrderEntity>) (session.getAttribute("purchaseOrders"));
                    PurchaseOrderEntity purchaseOrder = new PurchaseOrderEntity();
                    for (int i = 0; i < purchaseOrders.size(); i++) {
                        if (purchaseOrders.get(i).getId() == Integer.parseInt(purchaseOrderId)) {
                            purchaseOrder = purchaseOrders.get(i);
                        }
                    }

                    if (purchaseOrder.getLineItems() == null || purchaseOrder.getLineItems().isEmpty()) {
                        result = "?errMsg=Empty purchase order cannot be submitted.&id=" + purchaseOrderId;
                        response.sendRedirect("A3/purchaseOrderManagement_Update.jsp" + result);
                    } else {

                        boolean canUpdate = retailProductsAndRawMaterialsPurchasingBean.updatePurchaseOrderStatus(Long.parseLong(purchaseOrderId), "Submitted", submittedBy);
                        if (!canUpdate) {
                            result = "?errMsg=Failed to submit Purchase Order.&id=" + purchaseOrderId;
                            response.sendRedirect("A3/purchaseOrderManagement_Update.jsp" + result);
                        } else {
                            result = "?goodMsg=Purchase Order submitted successfully.&id=" + purchaseOrderId;
                            response.sendRedirect("PurchaseOrderLineItemManagement_Servlet" + result);
                        }
                    }
                } else if (status.equals("Completed")) {
                    System.out.println("<<<<<<<<<Completed<<<<<<<<<<<");
                    if (manufacturingWarehouseManagementBean.getInboundStorageBin(Long.parseLong(destinationWarehouseID)) == null) {
                        result = "?errMsg=Destination warehouse does not have an inbound storage bin.<br/>Please create one first before marking this order as completed.&id=" + purchaseOrderId;
                        response.sendRedirect("PurchaseOrderLineItemManagement_Servlet" + result);
                    } else {
                        manufacturingInventoryControlBean.moveInboundPurchaseOrderItemsToReceivingBin(Long.parseLong(purchaseOrderId));
                        result = "?goodMsg=Purchase Order updated successfully.&id=" + purchaseOrderId;
                        response.sendRedirect("PurchaseOrderLineItemManagement_Servlet" + result);
                    }
                } else {
                    retailProductsAndRawMaterialsPurchasingBean.updatePurchaseOrderStatus(Long.parseLong(purchaseOrderId), status, submittedBy);
                    result = "?goodMsg=Purchase Order updated successfully.&id=" + purchaseOrderId;
                    response.sendRedirect("PurchaseOrderLineItemManagement_Servlet" + result);
                }
            } else {
                if (!retailProductsAndRawMaterialsPurchasingBean.checkSKUExists(sku)) {
                    result = "?errMsg=SKU not found.&id=" + purchaseOrderId + "&lineItemId=" + lineItemId;
                    response.sendRedirect("A3/purchaseOrderManagement_UpdateLineItem.jsp" + result);
                } else {
                    boolean canUpdate = retailProductsAndRawMaterialsPurchasingBean.updateLineItemFromPurchaseOrder(Long.parseLong(purchaseOrderId), Long.parseLong(lineItemId), sku, Integer.parseInt(quantity));
                    if (!canUpdate) {
                        result = "?errMsg=Purchase Order not found.&id=" + purchaseOrderId + "&lineItemId=" + lineItemId;
                        response.sendRedirect("A3/purchaseOrderManagement_UpdateLineItem.jsp" + result);
                    } else {
                        result = "?goodMsg=Line Item updated successfully.&id=" + purchaseOrderId;
                        response.sendRedirect("PurchaseOrderLineItemManagement_Servlet" + result);
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
