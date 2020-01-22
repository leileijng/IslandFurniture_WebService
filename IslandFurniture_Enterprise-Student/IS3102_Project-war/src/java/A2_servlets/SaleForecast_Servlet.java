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
import EntityManager.RegionalOfficeEntity;
import EntityManager.SaleForecastEntity;
import EntityManager.SalesFigureEntity;
import EntityManager.StaffEntity;
import EntityManager.StoreEntity;
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
public class SaleForecast_Servlet extends HttpServlet {

    @EJB
    private AccountManagementBeanLocal amBean;
    @EJB
    private SalesForecastBeanLocal sfBean;
    @EJB
    private SalesAndOperationPlanningBeanLocal sopBean;
    @EJB
    private FacilityManagementBeanLocal fmBean;
    @EJB
    private ItemManagementBeanLocal imBean;

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

            case "/SaleForecast_index_GET":
                List<RegionalOfficeEntity> regionalOfficeList = fmBean.viewListOfRegionalOffice();
                if (regionalOfficeList == null) {
                    regionalOfficeList = new ArrayList<>();
                }
                request.setAttribute("regionalOfficeList", regionalOfficeList);
                nextPage = "/A2/SaleForecast_index";
                break;

            case "/SaleForecast_index_POST":
                String storeName = request.getParameter("storeName");
                StoreEntity store = fmBean.getStoreByName(storeName);
                StaffEntity currentUser = (StaffEntity) session.getAttribute("staffEntity");
                if (amBean.canStaffAccessToTheStore(currentUser.getId(), store.getId())) {
                    session.setAttribute("sf_storeId", store.getId());
                    nextPage = "/SaleForecast_Servlet/SaleForecast_schedule_GET";
                } else {
                    request.setAttribute("alertMessage", "You are not allowed to access the store.");
                    nextPage = "/SaleForecast_Servlet/SaleForecast_index_GET";
                }
                break;

            case "/SaleForecast_schedule_GET":
                scheduleList = sopBean.getScheduleList();
                request.setAttribute("scheduleList", scheduleList);
                nextPage = "/A2/SaleForecast_schedule";
                break;

            case "/SaleForecast_schedule_POST":
                try {
                    Long schedulelId = Long.parseLong(request.getParameter("scheduleId"));
                    session.setAttribute("scheduleId", schedulelId);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                nextPage = "/SaleForecast_Servlet/SaleForecast_main_GET";
                break;

            case "/SaleForecast_main_GET":
                try {
                    Long storeId = (long) session.getAttribute("sf_storeId");
                    Long schedulelId = (long) session.getAttribute("scheduleId");
                    List<ProductGroupEntity> productGroupList = imBean.getAllProductGroup();
                    List<SaleForecastEntity> saleForecastList = new ArrayList<>();
                    for (ProductGroupEntity productGroup : productGroupList) {
                        SaleForecastEntity saleForecast = sfBean.getSalesForecast(storeId, productGroup.getId(), schedulelId);
                        System.out.println("saleForecast.getId(): " + saleForecast.getId());
                        saleForecastList.add(saleForecast);
                    }

                    store = fmBean.viewStoreEntity(storeId);
                    MonthScheduleEntity schedule = sopBean.getScheduleById(schedulelId);

                    System.out.println("store: " + store.getName());
                    System.out.println("schedule: " + schedule.getYear() + schedule.getMonth());
                    System.out.println("saleForecastList: " + saleForecastList.size());
                    try {
                        for (SaleForecastEntity s : saleForecastList) {
                            System.out.println("s.getId(): " + s.getId());
                            System.out.println("s.getMethod(): " + s.getMethod());
                            System.out.println("s.getProductGroup().getName(): " + s.getProductGroup().getName());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    request.setAttribute("store", store);
                    request.setAttribute("schedule", schedule);
                    request.setAttribute("saleForecastList", saleForecastList);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                nextPage = "/A2/SaleForecast_main";
                break;

            case "/SaleForecast_main_POST":
                Long productGroupId = Long.parseLong(request.getParameter("productGroupId"));
                session.setAttribute("productGroupId", productGroupId);
                nextPage = "/SaleForecast_Servlet/ViewSaleFigure_GET";
                break;

            case "/editSaleForecast":
                System.out.println("editSaleForecast is called.");
                Long saleForecastId = Long.parseLong(request.getParameter("saleForecastId"));
                Integer quantity = Integer.parseInt(request.getParameter("quantity"));
                System.out.print("saleForecastId: " + saleForecastId);
                System.out.print("quantity: " + quantity);
                sfBean.editSaleForecast(saleForecastId, quantity);
                nextPage = "/SaleForecast_Servlet/SaleForecast_main_GET";
                break;

            case "/ViewSaleFigure_GET":
                productGroupId = (long) session.getAttribute("productGroupId");
                Long storeId = (long) session.getAttribute("sf_storeId");
                Long schedulelId = (long) session.getAttribute("scheduleId");

                ProductGroupEntity productGroup = imBean.getProductGroup(productGroupId);
                store = fmBean.viewStoreEntity(storeId);
                MonthScheduleEntity schedule = sopBean.getScheduleById(schedulelId);

                List<SalesFigureEntity> list1;
                List<SalesFigureEntity> list2;
                List<SalesFigureEntity> list3;
                if (schedule.getMonth() != 1) {
                    list1 = sfBean.getYearlySalesFigureList(storeId, productGroupId, schedule.getYear() - 2);
                    list2 = sfBean.getYearlySalesFigureList(storeId, productGroupId, schedule.getYear() - 1);
                    list3 = sfBean.getYearlySalesFigureList(storeId, productGroupId, schedule.getYear());
                } else {
                    list1 = sfBean.getYearlySalesFigureList(storeId, productGroupId, schedule.getYear() - 3);
                    list2 = sfBean.getYearlySalesFigureList(storeId, productGroupId, schedule.getYear() - 2);
                    list3 = sfBean.getYearlySalesFigureList(storeId, productGroupId, schedule.getYear() - 1);
                }
                System.out.println("list1.size(): " + list1.size());
                System.out.println("list2.size(): " + list2.size());
                System.out.println("list3.size(): " + list3.size());

                request.setAttribute("productGroup", productGroup);
                request.setAttribute("store", store);
                request.setAttribute("schedule", schedule);
                request.setAttribute("saleDate1", list1);
                request.setAttribute("saleDate2", list2);
                request.setAttribute("saleDate3", list3);
                nextPage = "/A2/ViewSaleFigure";
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
