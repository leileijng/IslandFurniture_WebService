/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package A2_servlets;

import CommonInfrastructure.AccountManagement.AccountManagementBeanLocal;
import CorporateManagement.FacilityManagement.FacilityManagementBeanLocal;
import CorporateManagement.ItemManagement.ItemManagementBeanLocal;
import EntityManager.MonthScheduleEntity;
import EntityManager.ProductGroupEntity;
import EntityManager.ProductGroupLineItemEntity;
import EntityManager.RegionalOfficeEntity;
import EntityManager.SaleAndOperationPlanEntity;
import EntityManager.SaleForecastEntity;
import EntityManager.StaffEntity;
import EntityManager.StoreEntity;
import HelperClasses.StoreHelper;
import InventoryManagement.StoreAndKitchenInventoryManagement.StoreAndKitchenInventoryManagementBeanLocal;
import MRP.SalesAndOperationPlanning.SOP_Helper;
import MRP.SalesAndOperationPlanning.SalesAndOperationPlanningBeanLocal;
import MRP.SalesForecast.SalesForecastBeanLocal;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class SaleAndOperationPlanning_Servlet extends HttpServlet {

    @EJB
    private StoreAndKitchenInventoryManagementBeanLocal simBean;
    @EJB
    private SalesForecastBeanLocal sfBean;
    @EJB
    private AccountManagementBeanLocal amBean;
    @EJB
    private ItemManagementBeanLocal imBean;
    @EJB
    private FacilityManagementBeanLocal fmBean;
    @EJB
    private SalesAndOperationPlanningBeanLocal sopBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String nextPage = "/A2/sop_index";
        ServletContext servletContext = getServletContext();
        RequestDispatcher dispatcher;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        HttpSession session = request.getSession();
        List<MonthScheduleEntity> scheduleList;
        String target = request.getPathInfo();

        switch (target) {

            case "/sop_scheduleManagement_GET":
                scheduleList = sopBean.getScheduleList();
                request.setAttribute("scheduleList", scheduleList);
                nextPage = "/A2/sop_scheduleManagement";
                break;

            case "/sop_scheduleManagement_POST":
                String submit = request.getParameter("submit-btn");
                if (submit.equals("Add Schedule")) {
                    nextPage = "/SaleAndOperationPlanning_Servlet/addSchedule";
                } else if (submit.equals("Delete Schedule")) {
                    nextPage = "/SaleAndOperationPlanning_Servlet/deleteSchedule";
                }
                break;

            case "/addSchedule":
                Integer year = Integer.parseInt(request.getParameter("year"));
                Integer month = Integer.parseInt(request.getParameter("month"));
                Integer week1 = Integer.parseInt(request.getParameter("week1"));
                Integer week2 = Integer.parseInt(request.getParameter("week2"));
                Integer week3 = Integer.parseInt(request.getParameter("week3"));
                Integer week4 = Integer.parseInt(request.getParameter("week4"));
                Integer week5 = Integer.parseInt(request.getParameter("week5"));

                sopBean.createSchedule(year, month, week1, week2, week3, week4, week5);

                nextPage = "/SaleAndOperationPlanning_Servlet/sop_scheduleManagement_GET";
                break;

            case "/deleteSchedule":
                String[] deletes = request.getParameterValues("delete");
                if (deletes != null) {
                    for (String scheduleIdStr : deletes) {
                        Long scheduleId = Long.parseLong(scheduleIdStr);
                        sopBean.deleteSchedule(scheduleId);
                    }
                }
                nextPage = "/SaleAndOperationPlanning_Servlet/sop_scheduleManagement_GET";
                break;

            case "/sop_index_GET":
                List<RegionalOfficeEntity> regionalOfficeList = fmBean.viewListOfRegionalOffice();
                if (regionalOfficeList == null) {
                    regionalOfficeList = new ArrayList<>();
                }
                request.setAttribute("regionalOfficeList", regionalOfficeList);
                nextPage = "/A2/sop_index";
                break;

            case "/sop_index_Post":

                String storeName = request.getParameter("storeName");
                StoreEntity store = fmBean.getStoreByName(storeName);
                StaffEntity currentUser = (StaffEntity) session.getAttribute("staffEntity");
                if (amBean.canStaffAccessToTheStore(currentUser.getId(), store.getId())) {
                    session.setAttribute("sop_storeId", store.getId());
                    nextPage = "/SaleAndOperationPlanning_Servlet/sop_schedule_GET";
                } else {
                    request.setAttribute("alertMessage", "You are not allowed to access the store.");
                    nextPage = "/SaleAndOperationPlanning_Servlet/sop_index_GET";
                }
                break;

            case "/sop_schedule_GET":
                scheduleList = sopBean.getScheduleList();
                request.setAttribute("scheduleList", scheduleList);
                nextPage = "/A2/sop_schedule";
                break;

            case "/sop_schedule_POST":
                try {
                    Long schedulelId = Long.parseLong(request.getParameter("scheduleId"));
                    session.setAttribute("scheduleId", schedulelId);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                nextPage = "/SaleAndOperationPlanning_Servlet/sop_main_GET";
                break;

            case "/sop_main_GET":
                try {
                    Long storeId = (long) session.getAttribute("sop_storeId");
                    Long schedulelId = (long) session.getAttribute("scheduleId");
                    List<ProductGroupEntity> unplannedProductGroupList = sopBean.getUnplannedProductGroup(storeId, schedulelId);
                    List<SaleAndOperationPlanEntity> retailSopList = sopBean.getSaleAndOperationPlanList_RetailGood(storeId, schedulelId);
                    List<SOP_Helper> sopHelperList = sopBean.getSOPHelperList(storeId, schedulelId);
                    store = fmBean.viewStoreEntity(storeId);
                    MonthScheduleEntity schedule = sopBean.getScheduleById(schedulelId);
                    request.setAttribute("store", store);
                    request.setAttribute("schedule", schedule);
                    request.setAttribute("unplannedProductGroupList", unplannedProductGroupList);
                    request.setAttribute("sopHelperList", sopHelperList);
                    request.setAttribute("retailSopList", retailSopList);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                nextPage = "/A2/sop_main";
                break;

            case "/sop_main_POST":
                Long productGroupId = Long.parseLong(request.getParameter("productGroupId"));
                session.setAttribute("productGroupId", productGroupId);
                nextPage = "/SaleAndOperationPlanning_Servlet/sop_create_GET";
                break;

            case "/sop_create_GET":
                try {
                    Long storeId = (long) session.getAttribute("sop_storeId");
                    StoreHelper storeHelper = fmBean.getStoreHelperClass(storeId);
                    request.setAttribute("storeHelper", storeHelper);

                    productGroupId = (long) session.getAttribute("productGroupId");
                    ProductGroupEntity productGroup = imBean.getProductGroup(productGroupId);
                    request.setAttribute("productGroup", productGroup);

                    Long schedulelId = (long) session.getAttribute("scheduleId");
                    MonthScheduleEntity schedule = sopBean.getScheduleById(schedulelId);
                    request.setAttribute("schedule", schedule);

                    SaleForecastEntity salesForecast = sfBean.getSalesForecast(storeId, productGroupId, schedulelId);
                    request.setAttribute("saleForecast", salesForecast);

                    List<Integer> pastTargetInventoryLevel = sopBean.getPastTargetInventoryLevel(storeId, schedulelId, productGroupId);
                    request.setAttribute("pastTargetInventoryLevel", pastTargetInventoryLevel);

                    int currentInventoryLevel = 0;
                    for (ProductGroupLineItemEntity lineitem : productGroup.getLineItemList()) {
                        currentInventoryLevel += simBean.checkItemQty(storeHelper.store.getWarehouse().getId(), lineitem.getItem().getSKU());
                    }
                    request.setAttribute("currentInventoryLevel", currentInventoryLevel);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                nextPage = "/A2/sop_create";
                break;

            case "/sop_create_POST":

                Integer saleForecast = Integer.parseInt(request.getParameter("saleForecast"));
                Integer productionPlan = Integer.parseInt(request.getParameter("productionPlan"));
                Integer currentInventory = Integer.parseInt(request.getParameter("currentInventory"));
                Integer targetInventory = Integer.parseInt(request.getParameter("targetInventory"));

                if (session.getAttribute("sop_storeId") == null) {
                    System.err.println("session.getAttribute(\"sop_storeId\") == null");
                }
                if (session.getAttribute("productGroupId") == null) {
                    System.err.println("session.getAttribute(\"productGroupId\") == null");
                }
                if (session.getAttribute("scheduleId") == null) {
                    System.err.println("session.getAttribute(\"scheduleId\") == null");
                }

                Long storeId = (long) session.getAttribute("sop_storeId");
                productGroupId = (long) session.getAttribute("productGroupId");
                Long schedulelId = (long) session.getAttribute("scheduleId");

                SaleAndOperationPlanEntity sop = sopBean.createSOP(storeId, schedulelId, productGroupId, saleForecast, productionPlan, currentInventory, targetInventory);

                nextPage = "/SaleAndOperationPlanning_Servlet/sop_main_GET";
                break;

            case "/sopManagement":
                String submit_btn = request.getParameter("submit-btn");
                System.out.println("submit_btn: " + submit_btn);
                if (submit_btn.equals("Delete Sales And Operations Plan")) {
                    System.out.println(" redirect to deleteSOP");
                    nextPage = "/SaleAndOperationPlanning_Servlet/deleteSOP";
                } else if (submit_btn.equals("Purchase Order")) {
                    storeId = (long) session.getAttribute("sop_storeId");
                    schedulelId = (long) session.getAttribute("scheduleId");
                    if (sopBean.generatePurchaseOrdersForRetailProduct(storeId, schedulelId)) {
                        request.setAttribute("alertMessage", "Purchase Order has been generated.");
                    } else {
                        request.setAttribute("alertMessage", "Fail to place purchase order.");
                    }
                    nextPage = "/SaleAndOperationPlanning_Servlet/sop_main_GET";
                } else {
                    String sopIdStr = request.getParameter("submit-btn");
                    request.setAttribute("sopIdStr", sopIdStr);
                    System.out.println("sopIdStr: " + sopIdStr);
                    nextPage = "/SaleAndOperationPlanning_Servlet/sop_edit_GET";
                }
                break;

            case "/sop_edit_GET":

                storeId = (long) session.getAttribute("sop_storeId");
                StoreHelper storeHelper = fmBean.getStoreHelperClass(storeId);
                request.setAttribute("storeHelper", storeHelper);

                schedulelId = (long) session.getAttribute("scheduleId");
                MonthScheduleEntity schedule = sopBean.getScheduleById(schedulelId);
                request.setAttribute("schedule", schedule);

                Long sopId = Long.parseLong((String) request.getAttribute("sopIdStr"));
                System.out.println("sopId: " + sopId);
                SaleAndOperationPlanEntity sopEntity = sopBean.getSOPbyId(sopId);
                request.setAttribute("sopEntity", sopEntity);

                ProductGroupEntity productGroup = sopBean.getProductGroupBySOP(sopId);
                request.setAttribute("productGroup", productGroup);

                List<Integer> pastTargetInventoryLevel = sopBean.getPastTargetInventoryLevel(storeId, schedulelId, productGroup.getId());
                request.setAttribute("pastTargetInventoryLevel", pastTargetInventoryLevel);

                nextPage = "/A2/sop_edit";
                break;

            case "/sop_edit_POST":
                productionPlan = Integer.parseInt(request.getParameter("productionPlan"));
                currentInventory = Integer.parseInt(request.getParameter("currentInventory"));
                targetInventory = Integer.parseInt(request.getParameter("targetInventory"));
                sopId = Long.parseLong(request.getParameter("sopId"));

                sopBean.editSOP(sopId, productionPlan, currentInventory, targetInventory);

                nextPage = "/SaleAndOperationPlanning_Servlet/sop_main_GET";
                break;

            case "/deleteSOP":
                try {
                    deletes = request.getParameterValues("delete");
                    if (deletes != null && deletes.length != 0) {
                        for (String sopString : deletes) {
                            sopId = Long.parseLong(sopString);
                            sopBean.deleteSOP(sopId);
                        }
                    }
                } catch (Exception ex) {
                }
                nextPage = "/SaleAndOperationPlanning_Servlet/sop_main_GET";
                break;
            case "/deleteSOP1":
                try {
                    deletes = request.getParameterValues("delete1");
                    if (deletes != null && deletes.length != 0) {
                        for (String sopString : deletes) {
                            sopId = Long.parseLong(sopString);
                            sopBean.deleteSOP(sopId);
                        }
                    }
                } catch (Exception ex) {
                }
                nextPage = "/SaleAndOperationPlanning_Servlet/sop_main_GET";
                break;
        }
        dispatcher = servletContext.getRequestDispatcher(nextPage);
        dispatcher.forward(request, response);
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
