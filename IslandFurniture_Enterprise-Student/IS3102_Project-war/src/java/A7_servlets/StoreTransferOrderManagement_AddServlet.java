package A7_servlets;

import EntityManager.WarehouseEntity;
import InventoryManagement.StoreAndKitchenInventoryManagement.StoreAndKitchenInventoryManagementBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class StoreTransferOrderManagement_AddServlet extends HttpServlet {

    @EJB
    private StoreAndKitchenInventoryManagementBeanLocal simbl;
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session;
            session = request.getSession();
            WarehouseEntity warehouseEntity = (WarehouseEntity) (session.getAttribute("warehouseEntity"));
            String origin = request.getParameter("origin");
            String target = request.getParameter("target");

            if (origin.equals(target)) {
                result = "?errMsg=Invalid movement, Origin and Target are the same.";
                response.sendRedirect("A7/transferOrderManagement_Add.jsp" + result);
            } else {
                boolean canUpdate = simbl.createTransferOrder(warehouseEntity.getId(), Long.parseLong(origin), Long.parseLong(target), null);
                if (!canUpdate) {
                    result = "?errMsg=Ops error, please try again.";
                    response.sendRedirect("A7/transferOrderManagement_Add.jsp" + result);
                } else {
                    result = "?goodMsg=Transfer Order created successfully.&id=" + warehouseEntity.getWarehouseName();
                    response.sendRedirect("StoreTransferOrderManagement_Servlet" + result);
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
