package A4_servlets;

import EntityManager.PickRequestEntity;
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

public class PickerCompleteJob_Servlet extends HttpServlet {

    @EJB
    CustomerServiceBeanLocal customerServiceBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session;
            session = request.getSession();
            StaffEntity picker = (StaffEntity) (session.getAttribute("picker"));
            if (picker == null) {
                String result = "Login fail. Please try again.";
                response.sendRedirect("A4/pickerLogin.jsp?errMsg=" + result);
            } else {
                String pickRequestId = request.getParameter("pickRequestId");
                PickRequestEntity pickRequestEntity = customerServiceBean.completePickRequest(Long.parseLong(pickRequestId));
                session.setAttribute("pickRequest", pickRequestEntity);
                response.sendRedirect("A4/pickerDisplayQueueNo.jsp?queueNo="+pickRequestEntity.getQueueNo());
            }

        } catch (Exception ex) {
            out.println(ex);
            response.sendRedirect("A4/pickerLogin_waiting.jsp");
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
