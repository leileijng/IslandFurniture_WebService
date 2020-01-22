package A3_servlets;

import EntityManager.LineItemEntity;
import EntityManager.PurchaseOrderEntity;
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

public class PurchaseOrderLineItemManagement_Servlet extends HttpServlet {

    @EJB
    private RetailProductsAndRawMaterialsPurchasingBeanLocal retailProductsAndRawMaterialsPurchasingBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session;
            session = request.getSession();
            String purchaseOrderId = request.getParameter("id");
            String errMsg = request.getParameter("errMsg");
            String goodMsg = request.getParameter("goodMsg");

            List<PurchaseOrderEntity> purchaseOrders = retailProductsAndRawMaterialsPurchasingBean.getPurchaseOrderList();
            session.setAttribute("purchaseOrders", purchaseOrders);
            if (purchaseOrderId != null) {
                if (errMsg != null) {
                    response.sendRedirect("A3/purchaseOrderManagement_Update.jsp?id=" + purchaseOrderId + "&errMsg=" + errMsg);
                } else if (goodMsg != null) {
                    response.sendRedirect("A3/purchaseOrderManagement_Update.jsp?id=" + purchaseOrderId + "&goodMsg=" + goodMsg);
                } else {
                    response.sendRedirect("A3/purchaseOrderManagement_Update.jsp?id=" + purchaseOrderId);
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
