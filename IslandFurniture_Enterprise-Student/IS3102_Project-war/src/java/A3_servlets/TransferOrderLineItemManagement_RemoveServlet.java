package A3_servlets;

import EntityManager.TransferOrderEntity;
import EntityManager.WarehouseEntity;
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

public class TransferOrderLineItemManagement_RemoveServlet extends HttpServlet {

    @EJB
    private ManufacturingWarehouseManagementBeanLocal manufacturingWarehouseManagementBean;
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session;
            session = request.getSession();
            WarehouseEntity warehouseEntity = (WarehouseEntity) (session.getAttribute("warehouseEntity"));
            String transferOrderId = request.getParameter("id");
            boolean canUpdate = manufacturingWarehouseManagementBean.removeLineItemFromTransferOrder(Long.parseLong(transferOrderId));
            if (!canUpdate) {
                result = "?errMsg=Item not found. Please try again.&id="+transferOrderId;
                response.sendRedirect("A3/transferOrderLineItemManagement.jsp" + result);
            } else {
                List<TransferOrderEntity> transferOrders = manufacturingWarehouseManagementBean.viewAllTransferOrderByWarehouseId(warehouseEntity.getId());
                session.setAttribute("transferOrders", transferOrders);
                result = "?goodMsg=Item removed successfully.&id="+transferOrderId;
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
