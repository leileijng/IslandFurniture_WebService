package A3_servlets;

import EntityManager.ShippingOrderEntity;
import SCM.InboundAndOutboundLogistics.InboundAndOutboundLogisticsBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ShippingOrderManagement_UpdateServlet extends HttpServlet {

    @EJB
    private InboundAndOutboundLogisticsBeanLocal inboundAndOutboundLogisticsBeanLocal;
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String shippingOrderId = request.getParameter("id");
            String sourceId = request.getParameter("origin");
            String destinationId = request.getParameter("destination");
            String expectedDate = request.getParameter("expectedDate");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(expectedDate);
            if (sourceId != null && destinationId != null) {
                boolean canUpdate = inboundAndOutboundLogisticsBeanLocal.updateShippingOrder(Long.parseLong(shippingOrderId), Long.parseLong(sourceId), Long.parseLong(destinationId), date);
                if (!canUpdate) {
                    result = "?errMsg=One of the selected warehouse no longer exist.&id=" + shippingOrderId;
                    response.sendRedirect("A3/shippingOrderManagement_Update.jsp" + result);
                } else {
                    HttpSession session;
                    session = request.getSession();

                    List<ShippingOrderEntity> shippingOrders = inboundAndOutboundLogisticsBeanLocal.getShippingOrderList();
                    session.setAttribute("shippingOrders", shippingOrders);

                    result = "?goodMsg=Shipping Order updated successfully&id=" + shippingOrderId;
                    response.sendRedirect("A3/shippingOrderManagement_Update.jsp" + result);
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
