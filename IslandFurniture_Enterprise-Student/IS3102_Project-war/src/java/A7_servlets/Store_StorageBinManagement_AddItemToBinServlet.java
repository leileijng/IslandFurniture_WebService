package A7_servlets;

import SCM.ManufacturingInventoryControl.ManufacturingInventoryControlBeanLocal;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Store_StorageBinManagement_AddItemToBinServlet extends HttpServlet {

    @EJB
    private ManufacturingInventoryControlBeanLocal manufacturingInventoryControlBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String msg = "";
        try {
            Long storageBinID = Long.parseLong(request.getParameter("storageBinID"));
            String SKU = request.getParameter("SKU");
            Integer qty = Integer.parseInt(request.getParameter("qty"));
            Boolean result = (manufacturingInventoryControlBean.checkIfStorageBinIsOfAppropriateItemType(storageBinID, SKU) && manufacturingInventoryControlBean.addItemIntoBin(storageBinID, SKU, qty));
            if (result) {
                msg = "Item added to bin successfully!";
                response.sendRedirect("StoreStorageBinManagement_Servlet?goodMsg=" + msg);
            } else {
                msg = "Failed to add item to bin. Please try again!";
                response.sendRedirect("A7/storageBinManagement.jsp?errMsg=" + msg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            msg = "Error adding item to bin, please try again!";
            response.sendRedirect("A7/storageBinManagement.jsp?errMsg=" + msg);
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
