package A6_servlets;

import CorporateManagement.ItemManagement.ItemManagementBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BomManagement_AddLineItemBomServlet extends HttpServlet {

    @EJB
    private ItemManagementBeanLocal itemManagementBean;
    String result = "";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            System.out.println("coming in add line item servlet");            
            String SKU = request.getParameter("sku");
            Integer qty = Integer.parseInt(request.getParameter("qty"));
            Long bomId = Long.parseLong(request.getParameter("bomId"));
            System.out.println("SKU is " + SKU);
            System.out.println("Quantity is " + qty);
            System.out.println("BOM Id is " + bomId);
                     
            boolean canCreate = itemManagementBean.addLineItemToBOM(SKU, qty, bomId);

            if (!canCreate) {
                System.out.println("cannot add line item in servlet");
                result = "?errMsg=Error adding Line Item. Please try again.&id=" + bomId;
                response.sendRedirect("BomManagement_LineItemBomServlet" + result);
            } else {
                System.out.println("can add line item in servlet");
                result = "?goodMsg=Line Item added successfully.&id=" + bomId;
                response.sendRedirect("BomManagement_LineItemBomServlet" + result);
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
