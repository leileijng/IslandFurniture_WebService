/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package A6_servlets;

import CorporateManagement.FacilityManagement.FacilityManagementBeanLocal;
import EntityManager.ManufacturingFacilityEntity;
import EntityManager.StaffEntity;
import EntityManager.StoreEntity;
import EntityManager.WarehouseEntity;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Administrator
 */
public class FacilityManagement_Servlet extends HttpServlet {

    @EJB
    private FacilityManagementBeanLocal fmBean;
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            String nextPage = "/A6/facilityManagement";
            ServletContext servletContext = getServletContext();
            RequestDispatcher dispatcher;
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Long warehouseId;
            String target = request.getPathInfo();

            HttpSession session = request.getSession();
            StaffEntity currentLoggedInStaffEntity = (StaffEntity) session.getAttribute("staffEntity");
            String currentLoggedInStaffID;
            if (currentLoggedInStaffEntity == null) {
                currentLoggedInStaffID = "System";
            } else {
                currentLoggedInStaffID = currentLoggedInStaffEntity.getId().toString();
            }

            switch (target) {

                case "/warehouseManagement_index":
                    List<WarehouseEntity> warehouseList = fmBean.getWarehouseList();
                    request.setAttribute("warehouseList", warehouseList);
                    nextPage = "/A6/warehouseManagement";
                    break;

                case "/createWarehouse_GET":
                    String submit_btn = request.getParameter("submit-btn");
                    if (submit_btn.equals("Add Warehouse")) {
                        List<StoreEntity> storeList = fmBean.viewListOfStore();
                        List<ManufacturingFacilityEntity> mfList = fmBean.viewListOfManufacturingFacility();
                        request.setAttribute("storeList", storeList);
                        request.setAttribute("mfList", mfList);
                        nextPage = "/A6/createWarehouse";
                    } else if (submit_btn.equals("Delete Warehouse")) {
                        nextPage = "/FacilityManagement_Servlet/deleteWarehouse";
                    } else {
                        warehouseId = Long.parseLong(submit_btn);
                        request.setAttribute("warehouseId", warehouseId);
                        nextPage = "/FacilityManagement_Servlet/editWarehouse_GET";
                    }
                    break;

                case "/createWarehouse_POST":
                    try {
                        String warehouseName = request.getParameter("warehouseName");
                        String address = request.getParameter("address");
                        String telephone = request.getParameter("telephone");
                        String email = request.getParameter("email");
                        String StoreIdString = request.getParameter("StoreId");
                        System.out.print("StoreIdString: " + StoreIdString);
                        String mfIdString = request.getParameter("mfId");
                        Long StoreId = (long) -1;
                        Long mfId = (long) -1;
                        if (!StoreIdString.equals("")) {
                            StoreId = Long.parseLong(StoreIdString);
                        }
                        if (!mfIdString.equals("")) {
                            mfId = Long.parseLong(mfIdString);
                        }
                        if (fmBean.checkNameExistsOfWarehouse(warehouseName)) {
                            result = "?errMsg=Fail to create warehouse due to duplicated warehouse name.";
                        } else {
                            WarehouseEntity warehouse = fmBean.createWarehouse(currentLoggedInStaffID, warehouseName, address, telephone, email, StoreId, mfId);
                            if (warehouse != null) {
                                result = "?goodMsg=A new warehouse record has been saved.";
                            } else {
                                result = "?errMsg=Fail to create warehouse due to duplicated warehouse name.";
                            }
                        }
                        nextPage = "/FacilityManagement_Servlet/warehouseManagement_index" + result;

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;

                case "/editWarehouse_GET":
                    warehouseId = (long) request.getAttribute("warehouseId");
                    WarehouseEntity warehouse = fmBean.getWarehouseById(warehouseId);
                    request.setAttribute("warehouse", warehouse);

                    List<StoreEntity> storeList = fmBean.viewListOfStore();
                    List<ManufacturingFacilityEntity> mfList = fmBean.viewListOfManufacturingFacility();
                    request.setAttribute("storeList", storeList);
                    request.setAttribute("mfList", mfList);

                    nextPage = "/A6/editWarehouse";
                    break;

                case "/editWarehouse_POST":
                    String warehouseName = request.getParameter("warehouseName");
                    String address = request.getParameter("address");
                    String telephone = request.getParameter("telephone");
                    String email = request.getParameter("email");
                    Long id = Long.parseLong(request.getParameter("warehouseId"));

                    if (fmBean.editWarehouse(currentLoggedInStaffID, id, warehouseName, address, telephone, email)) {
                        result = "?goodMsg=The warehouse has been updated.";

                    } else {
                        result = "?errMsg=Fail to edit warehouse.";
                    }
                    nextPage = "/FacilityManagement_Servlet/warehouseManagement_index" + result;
                    break;

                case "/deleteWarehouse":
                    String[] deletes = request.getParameterValues("delete");
                    Boolean msg = true;

                    if (deletes != null) {
                        for (String warehouseString : deletes) {
                            Long warehouse_Id = Long.parseLong(warehouseString);
                            if (!fmBean.checkIfWarehouseContainsItem(warehouse_Id)) {
                                fmBean.deleteWarehouse(currentLoggedInStaffID, warehouse_Id);
                            } else {
                                msg = false;
                                result = "?errMsg=Fail to delete one or more warehouses as the warehouse contains storage bins.";
                            }
                        }
                        if (msg) {
                            result = "?goodMsg=Successfully removed: " + deletes.length + " record(s).";
                        }
                    }
                    nextPage = "/FacilityManagement_Servlet/warehouseManagement_index" + result;
                    break;

            }
            dispatcher = servletContext.getRequestDispatcher(nextPage);
            dispatcher.forward(request, response);
        } catch (Exception ex) {
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
