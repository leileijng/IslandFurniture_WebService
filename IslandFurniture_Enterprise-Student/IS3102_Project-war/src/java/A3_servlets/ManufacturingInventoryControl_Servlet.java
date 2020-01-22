package A3_servlets;

import EntityManager.WarehouseEntity;
import HelperClasses.ItemStorageBinHelper;
import SCM.ManufacturingInventoryControl.ManufacturingInventoryControlBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ManufacturingInventoryControl_Servlet extends HttpServlet {

    @EJB
    private ManufacturingInventoryControlBeanLocal manufacturingInventoryControlBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session;
            session = request.getSession();
            String errMsg = request.getParameter("errMsg");
            WarehouseEntity warehouseEntity = (WarehouseEntity) (session.getAttribute("warehouseEntity"));
            if (warehouseEntity == null) {
                response.sendRedirect("ManufacturingWarehouseManagement_Servlet");
            } else {
                List<ItemStorageBinHelper> itemStorageBinHelpers = manufacturingInventoryControlBean.getItemList(warehouseEntity.getId());
                System.out.println("Retrieving itemStorageBinHelpers list...");
                System.out.println("Size of itemStorageBinHelpers: " + itemStorageBinHelpers.size());
                session.setAttribute("itemStorageBinHelpers", itemStorageBinHelpers);
                if (errMsg == null || errMsg.equals("")) {
                    response.sendRedirect("A3/manufacturingInventoryControlManagement.jsp");
                } else {
                    response.sendRedirect("A3/manufacturingInventoryControlManagement.jsp?errMsg=" + errMsg);
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
