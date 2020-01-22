package A4_servlets;

import EntityManager.StaffEntity;
import OperationalCRM.CustomerService.CustomerServiceBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ReceptionistCallQueue_Servlet extends HttpServlet {

    @EJB
    CustomerServiceBeanLocal customerServiceBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session;
            session = request.getSession();
            StaffEntity staff = (StaffEntity) session.getAttribute("receptionist");

            String requestType = request.getParameter("requestType");
            String pickRequestID = request.getParameter("pickRequestID");

            if (staff != null && pickRequestID != null && requestType != null) {

                if (requestType.equals("1")) {
                    System.out.println("customerServiceBean.callCustomer");
                    customerServiceBean.callCustomer(Long.parseLong(pickRequestID));
                } else if (requestType.equals("2")) {
                    customerServiceBean.markPickRequestAsUnCollected(Long.parseLong(pickRequestID));
                } else if (requestType.equals("3")) {
                    String barcode = request.getParameter("barcode");
                    customerServiceBean.markPickRequestForCollection(barcode);
                }
                response.sendRedirect("ReceptionistJobList_Servlet");
            } else {
                String result = "Session Expired.";
                response.sendRedirect("A4/receptionistLogin.jsp?errMsg=" + result);
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
