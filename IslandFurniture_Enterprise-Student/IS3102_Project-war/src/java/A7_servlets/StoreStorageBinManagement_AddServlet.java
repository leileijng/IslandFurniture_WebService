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

public class StoreStorageBinManagement_AddServlet extends HttpServlet {

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
            String name = request.getParameter("name");
            String type = request.getParameter("type");
            String length = request.getParameter("length");
            String width = request.getParameter("width");
            String height = request.getParameter("height");

            boolean canUpdate = simbl.createStorageBin(warehouseEntity.getId(), name, type, Integer.parseInt(length), Integer.parseInt(width), Integer.parseInt(height));
            if (!canUpdate) {
                result = "?errMsg=The selected storage bin type already exist. Only one inbound and outbound storage bin can exist per warehouse. If the size of the bin was changed, update there the bin details accordingly instead of creating a new one. Alternatively, delete the bin first before trying to create one.";
                response.sendRedirect("A7/storageBinManagement_Add.jsp" + result);
            } else {
                result = "?errMsg=Storage Bin added successfully.&id=" + warehouseEntity.getWarehouseName();
                response.sendRedirect("StoreStorageBinManagement_Servlet" + result);
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
