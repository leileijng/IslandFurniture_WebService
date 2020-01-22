package A4_servlets;

import CommonInfrastructure.AccountManagement.AccountManagementBeanLocal;
import EntityManager.AccessRightEntity;
import EntityManager.PickRequestEntity;
import EntityManager.StaffEntity;
import OperationalCRM.CustomerService.CustomerServiceBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ReceptionistLastCalled_Servlet extends HttpServlet {

    @EJB
    CustomerServiceBeanLocal customerServiceBean;
    @EJB
    AccountManagementBeanLocal accountManagementBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            HttpSession session;
            session = request.getSession();
            StaffEntity staff = (StaffEntity) session.getAttribute("receptionist");
            if (staff != null) {
                if (accountManagementBean.checkIfStaffIsStoreManager(staff.getId()) || accountManagementBean.checkIfStaffIsReceptionist(staff.getId())) {
                    AccessRightEntity accessRightEntity = accountManagementBean.isAccessRightExist(staff.getId(), 4L);
                    if (accessRightEntity == null) {
                        accessRightEntity = accountManagementBean.isAccessRightExist(staff.getId(), 10L);
                    }
                    Long storeID = accessRightEntity.getStore().getId();
                    List<PickRequestEntity> pickRequests = customerServiceBean.getLastCalledPickRequestInStoreForReceptionist(storeID);
                    session.setAttribute("pickRequests", pickRequests);
                    response.sendRedirect("A4/receptionistLastCalledQueueNo.jsp");
                }
            } else {//no store manager or receptionist role
                String result = "Account does not have store manager or receptionist role.";
                response.sendRedirect("A4/receptionistLastCalledLogin.jsp?errMsg=" + result);
            }
        } catch (Exception ex) {
            String result = "An error has occured, please try again.";
            response.sendRedirect("A4/receptionistLastCalledLogin.jsp?errMsg=" + result);
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
