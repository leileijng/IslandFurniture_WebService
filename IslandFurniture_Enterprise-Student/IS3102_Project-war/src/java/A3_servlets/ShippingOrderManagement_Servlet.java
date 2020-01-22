package A3_servlets;

import CommonInfrastructure.AccountManagement.AccountManagementBeanLocal;
import CorporateManagement.FacilityManagement.FacilityManagementBeanLocal;
import EntityManager.ShippingOrderEntity;
import EntityManager.StaffEntity;
import EntityManager.WarehouseEntity;
import SCM.InboundAndOutboundLogistics.InboundAndOutboundLogisticsBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ShippingOrderManagement_Servlet extends HttpServlet {

    @EJB
    private InboundAndOutboundLogisticsBeanLocal inboundAndOutboundLogisticsBeanLocal;

    @EJB
    private FacilityManagementBeanLocal facilityManagementBeanLocal;

    @EJB
    private AccountManagementBeanLocal accountManagementBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session;
            session = request.getSession();
            String errMsg = request.getParameter("errMsg");
            String goodMsg = request.getParameter("goodMsg");
            List<ShippingOrderEntity> shippingOrders = inboundAndOutboundLogisticsBeanLocal.getShippingOrderList();
            session.setAttribute("shippingOrders", shippingOrders);

            List<WarehouseEntity> warehouses = new ArrayList();
            List<WarehouseEntity> warehouse1 = new ArrayList();
            System.out.println("1");
            StaffEntity staffEntity = (StaffEntity) session.getAttribute("staffEntity");
            Long id = staffEntity.getId();

            if (accountManagementBean.checkIfStaffIsAdministrator(staffEntity.getId()) || accountManagementBean.checkIfStaffIsGlobalManager(staffEntity.getId())) {
                warehouses = facilityManagementBeanLocal.getWarehouseList();
                warehouse1 = warehouses;
                System.out.println("2");
                session.setAttribute("warehouse1", warehouse1);
            
            } else if (accountManagementBean.checkIfStaffIsRegionalManager(staffEntity.getId()) || accountManagementBean.checkIfStaffIsPurchasingManager(staffEntity.getId())) {
                Long roID = accountManagementBean.getRegionalOfficeIdBasedOnStaffRole(staffEntity.getId());
                if (roID != null) {
                    warehouses = facilityManagementBeanLocal.getWarehouseListByRegionalOffice(roID);
                    session.setAttribute("warehouse1", warehouses);
                    System.out.println("3");
                }
            } else if ((accountManagementBean.checkIfStaffIsStoreManager(staffEntity.getId())) || (accountManagementBean.checkIfStaffIsManufacturingFacilityManager(staffEntity.getId())) || (accountManagementBean.checkIfStaffIsManufacturingFacilityWarehouseManager(staffEntity.getId()))) {
                System.out.println("2");
                WarehouseEntity wh = facilityManagementBeanLocal.getWarehouseEntityBasedOnStaffRole(id);

                if (wh != null) {
                    System.out.println("3");
                    warehouses.add(wh);
                    System.out.println("4");
                }

                Long roID = accountManagementBean.getRegionalOfficeIdBasedOnStaffRole(staffEntity.getId());
                System.out.println("6");

                if (roID != null) {
                    warehouse1 = facilityManagementBeanLocal.getWarehouseListByRegionalOffice(roID);

                    session.setAttribute("warehouse1", warehouse1);
                }
            }
            session.setAttribute("warehouses", warehouses);

            if (errMsg == null && goodMsg == null) {
                response.sendRedirect("A3/shippingOrderManagement.jsp");
            } else if ((errMsg != null) && (goodMsg == null)) {
                if (!errMsg.equals("")) {
                    response.sendRedirect("A3/shippingOrderManagement.jsp?errMsg=" + errMsg);
                }
            } else if ((errMsg == null && goodMsg != null)) {
                if (!goodMsg.equals("")) {
                    response.sendRedirect("A3/shippingOrderManagement.jsp?goodMsg=" + goodMsg);
                }
            }
        } catch (Exception ex) {
            out.println("\n\n " + ex.getMessage());
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
