package A8_servlets;

import CommonInfrastructure.AccountManagement.AccountManagementBeanLocal;
import CorporateManagement.FacilityManagement.FacilityManagementBeanLocal;
import CorporateManagement.ItemManagement.ItemManagementBeanLocal;
import CorporateManagement.RestaurantManagement.RestaurantManagementBeanLocal;
import EntityManager.ItemEntity;
import EntityManager.ManufacturingFacilityEntity;
import EntityManager.MasterProductionScheduleEntity;
import EntityManager.MaterialRequirementEntity;
import EntityManager.MenuItemEntity;
import EntityManager.MonthScheduleEntity;
import EntityManager.RegionalOfficeEntity;
import EntityManager.SaleForecastEntity;
import EntityManager.SalesFigureEntity;
import EntityManager.StaffEntity;
import EntityManager.StoreEntity;
import KitchenManagement.FoodDemandForecastingAndPlanning.FoodDemandForecastingAndPlanningBeanLocal;
import MRP.SalesAndOperationPlanning.SalesAndOperationPlanningBeanLocal;
import java.io.IOException;
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

public class KitchenManagement_servlet extends HttpServlet {

    @EJB
    private RestaurantManagementBeanLocal restaurantBean;
    @EJB
    private FoodDemandForecastingAndPlanningBeanLocal fdfpBean;
    @EJB
    private FacilityManagementBeanLocal fmBean;
    @EJB
    private AccountManagementBeanLocal amBean;
    @EJB
    private SalesAndOperationPlanningBeanLocal sopBean;
    @EJB
    private ItemManagementBeanLocal imBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        System.out.println("KitchenManagement_Servlet is called");

        String nextPage = "/KitchenManagement_servlet/KitchenSaleForecast_index_GET";
        ServletContext servletContext = getServletContext();
        RequestDispatcher dispatcher;
        HttpSession session = request.getSession();
        List<MonthScheduleEntity> scheduleList;
        String target = request.getPathInfo();

        switch (target) {

            case "/KitchenSaleForecast_index_GET":
                List<RegionalOfficeEntity> regionalOfficeList = fmBean.viewListOfRegionalOffice();
                if (regionalOfficeList == null) {
                    regionalOfficeList = new ArrayList<>();
                }
                request.setAttribute("regionalOfficeList", regionalOfficeList);
                nextPage = "/A8/KitchenSaleForecast_index";
                break;

            case "/KitchenSaleForecast_index_POST":
                String storeName = request.getParameter("storeName");
                StoreEntity store = fmBean.getStoreByName(storeName);
                StaffEntity currentUser = (StaffEntity) session.getAttribute("staffEntity");
                if (amBean.canStaffAccessToTheStore(currentUser.getId(), store.getId())) {
                    session.setAttribute("s_storeId", store.getId());
                    nextPage = "/KitchenManagement_servlet/KitchenSaleForecast_schedule_GET";
                } else {
                    request.setAttribute("alertMessage", "You are not allowed to access the store.");
                    nextPage = "/KitchenManagement_servlet/KitchenSaleForecast_index_GET";
                }
                break;

            case "/KitchenSaleForecast_schedule_GET":
                scheduleList = sopBean.getScheduleList();
                request.setAttribute("scheduleList", scheduleList);
                nextPage = "/A8/KitchenSaleForecast_schedule";
                break;

            case "/KitchenSaleForecast_schedule_POST":
                try {
                    Long schedulelId = Long.parseLong(request.getParameter("scheduleId"));
                    session.setAttribute("scheduleId", schedulelId);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                nextPage = "/KitchenManagement_servlet/KitchenSaleForecast_main_GET";
                break;

            case "/KitchenSaleForecast_main_GET":
                try {
                    Long storeId = (long) session.getAttribute("s_storeId");
                    Long schedulelId = (long) session.getAttribute("scheduleId");

                    List<MenuItemEntity> menuItemList = restaurantBean.listAllMenuItem();
                    System.out.println("menuItemList.size(): " + menuItemList.size());
                    List<SaleForecastEntity> saleForecastList = new ArrayList<>();
                    for (MenuItemEntity menuItem : menuItemList) {
                        SaleForecastEntity saleForecast = fdfpBean.getSalesForecast(storeId, menuItem.getId(), schedulelId);
                        saleForecastList.add(saleForecast);
                    }
                    System.out.println("saleForecastList.size(): " + saleForecastList.size());
                    store = fmBean.viewStoreEntity(storeId);
                    MonthScheduleEntity schedule = sopBean.getScheduleById(schedulelId);

                    request.setAttribute("store", store);
                    request.setAttribute("schedule", schedule);
                    request.setAttribute("saleForecastList", saleForecastList);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                nextPage = "/A8/KitchenSaleForecast_main";
                break;

            case "/Kitchen_editSaleForecast":
                System.out.println("Kitchen_editSaleForecast is called.");
                Long saleForecastId = Long.parseLong(request.getParameter("saleForecastId"));
                Integer quantity = Integer.parseInt(request.getParameter("quantity"));
                fdfpBean.editSalesForecast(saleForecastId, quantity);
                nextPage = "/KitchenManagement_servlet/KitchenSaleForecast_main_GET";
                break;

            case "/ViewSaleFigure_GET":
                System.out.println("ViewSaleFigure_GET is called.");
                String menuItemSKU = request.getParameter("menuItemSKU");
                Long storeId = (long) session.getAttribute("s_storeId");
                Long schedulelId = (long) session.getAttribute("scheduleId");

                ItemEntity item = restaurantBean.getItemBySKU(menuItemSKU);
                store = fmBean.viewStoreEntity(storeId);
                MonthScheduleEntity schedule = sopBean.getScheduleById(schedulelId);

                List<SalesFigureEntity> list1;
                List<SalesFigureEntity> list2;
                List<SalesFigureEntity> list3;
                if (schedule.getMonth() != 1) {
                    list1 = fdfpBean.getYearlySalesFigureList(storeId, menuItemSKU, schedule.getYear() - 2);
                    list2 = fdfpBean.getYearlySalesFigureList(storeId, menuItemSKU, schedule.getYear() - 1);
                    list3 = fdfpBean.getYearlySalesFigureList(storeId, menuItemSKU, schedule.getYear());
                } else {
                    list1 = fdfpBean.getYearlySalesFigureList(storeId, menuItemSKU, schedule.getYear() - 3);
                    list2 = fdfpBean.getYearlySalesFigureList(storeId, menuItemSKU, schedule.getYear() - 2);
                    list3 = fdfpBean.getYearlySalesFigureList(storeId, menuItemSKU, schedule.getYear() - 1);
                }
                request.setAttribute("menuItem", (MenuItemEntity) item);
                request.setAttribute("store", store);
                request.setAttribute("schedule", schedule);
                request.setAttribute("saleDate1", list1);
                request.setAttribute("saleDate2", list2);
                request.setAttribute("saleDate3", list3);
                nextPage = "/A8/HistoricalData";
                break;

            case "/KitchenDemandManagement_GET":
                try {
                    System.out.println("KitchenDemandManagement_GET is called");
                    storeId = (long) session.getAttribute("s_storeId");

                    List<MasterProductionScheduleEntity> mpsList = new ArrayList<>();
                    if (fdfpBean.generateMasterProductionSchedules(storeId)) {
                        System.out.println("Master Production Schedule is generated.");
                        mpsList = fdfpBean.getMasterProductionSchedules(storeId);
                        System.out.println("mpsList.size(): " + mpsList.size());
                    }
                    request.setAttribute("mpsList", mpsList);
                    System.out.println("mpsList.size: " + mpsList.size());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                nextPage = "/A8/KitchenDemandManagement";
                break;

            case "/KitchenMaterialRequirement_GET":
                System.out.println("KitchenMaterialRequirement_GET is called");
                storeId = (long) session.getAttribute("s_storeId");
                List<MaterialRequirementEntity> mrList = new ArrayList<>();
                if (fdfpBean.generateMaterialRequirementPlan(storeId)) {
                    System.out.println("Material Requirement Plan is generated.");
                    mrList = fdfpBean.getMaterialRequirementEntityList(storeId);
                }
                request.setAttribute("mrList", mrList);
                nextPage = "/A8/KitchenMaterialRequirement";
                break;

            case "/Kitchen_purchaseOrder":
                storeId = (long) session.getAttribute("s_storeId");
                if (fdfpBean.generatePurchaseOrderFromMaterialRequirement(storeId)) {
                    request.setAttribute("alertMessage", "Purchase Orders have been generated.");
                } else {
                    request.setAttribute("alertMessage", "Failed To generate Purchase Orders");
                }
                nextPage = "/KitchenManagement_servlet/KitchenMaterialRequirement_GET";
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
