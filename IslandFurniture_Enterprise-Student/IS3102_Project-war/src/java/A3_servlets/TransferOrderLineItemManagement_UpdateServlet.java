package A3_servlets;

import EntityManager.StaffEntity;
import EntityManager.TransferOrderEntity;
import EntityManager.WarehouseEntity;
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

public class TransferOrderLineItemManagement_UpdateServlet extends HttpServlet {

    @EJB
    private ManufacturingWarehouseManagementBeanLocal mwmb;
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session;
            session = request.getSession();
            WarehouseEntity warehouseEntity = (WarehouseEntity) (session.getAttribute("warehouseEntity"));
            String transferOrderId = request.getParameter("id");
            String status = request.getParameter("status");
            StaffEntity staff = (StaffEntity) session.getAttribute("staffEntity");

            result = "?goodMsg=Line item added successfully.&id=" + transferOrderId;

            boolean canUpdate = false;
            if (status.equals("Completed")) {
                canUpdate = mwmb.markTransferOrderAsCompleted(Long.parseLong(transferOrderId), staff.getName());
                result = "?goodMsg=Transfer order status updated successfully.&id=" + transferOrderId;
                //response.sendRedirect("A3/transferOrderLineItemManagement.jsp" + result);
            } else if (status.equals("Unfulfillable")) {
                result = "?goodMsg=Transfer order status updated successfully.&id=" + transferOrderId;
                canUpdate = mwmb.markTransferOrderAsUnfulfilled(Long.parseLong(transferOrderId));
                //response.sendRedirect("A3/transferOrderLineItemManagement.jsp" + result);
            } else if (status.equals("Pending")) {
                result = "?errMsg=Status not selected.";
                response.sendRedirect("A3/transferOrderLineItemManagement.jsp" + result);
            }
            if (!canUpdate) {

                result = "?errMsg=Invalid request. Items not found or destination bin cannot contain the item (full or wrong bin type).&id=" + transferOrderId;
                response.sendRedirect("A3/transferOrderLineItemManagement.jsp" + result);
            } else {
                List<TransferOrderEntity> transferOrders = mwmb.viewAllTransferOrderByWarehouseId(warehouseEntity.getId());
                session.setAttribute("transferOrders", transferOrders);
                response.sendRedirect("A3/transferOrderLineItemManagement.jsp" + result);
            }

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
