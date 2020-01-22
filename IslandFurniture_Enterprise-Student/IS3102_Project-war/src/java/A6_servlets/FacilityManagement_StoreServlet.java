/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package A6_servlets;

import CorporateManagement.FacilityManagement.FacilityManagementBeanLocal;
import EntityManager.CountryEntity;
import EntityManager.RegionalOfficeEntity;
import EntityManager.StaffEntity;
import EntityManager.StoreEntity;
import HelperClasses.StoreHelper;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@MultipartConfig
public class FacilityManagement_StoreServlet extends HttpServlet {

    @EJB
    private FacilityManagementBeanLocal fmBean;
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String nextPage = "/A6/facilityManagement";
        ServletContext servletContext = getServletContext();
        RequestDispatcher dispatcher;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Long storeId;
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

            case "/storeManagement_index":
                List<StoreHelper> models = fmBean.getStoreHelperList();
                request.setAttribute("modelList", models);
                nextPage = "/A6/storeManagement";
                break;

            case "/createStore_GET":
                System.out.println("Create store in servlet");
                String submit_btn = request.getParameter("submit-btn");
                if (submit_btn.equals("Add Store")) {
                    List<RegionalOfficeEntity> regionalOfficeList = fmBean.viewListOfRegionalOffice();
                    request.setAttribute("regionalOfficeList", regionalOfficeList);
                    List<CountryEntity> countryList = fmBean.getListOfCountries();
                    request.setAttribute("countryList", countryList);
                    nextPage = "/A6/createStore";
                } else if (submit_btn.equals("Delete Store")) {
                    nextPage = "/FacilityManagement_StoreServlet/deleteStore";
                } else {
                    storeId = Long.parseLong(submit_btn);
                    request.setAttribute("storeId", storeId);
                    nextPage = "/FacilityManagement_StoreServlet/editStore_GET";
                }
                break;

            case "/createStore_POST":
                try {
                    String storeName = request.getParameter("storeName");
                    String address = request.getParameter("address");
                    String telephone = request.getParameter("telephone");
                    String email = request.getParameter("email");
                    String longitude = request.getParameter("longitude");
                    String latitude = request.getParameter("latitude");                    
                    Long regionalOfficeId = Long.parseLong(request.getParameter("regionalOfficeId"));
                    Long countryID = Long.parseLong(request.getParameter("countryID"));
                    String postalCode = request.getParameter("postalCode");
                    Part file = request.getPart("javafile");
                    String fileName = storeName + ".jpg";
                    String imageURL = "/IS3102_Project-war/img/storemaps/" + fileName;

                    if (fmBean.checkNameExistsOfStore(storeName)) {
                        // request.setAttribute("alertMessage", "Fail to create store due to duplicated store name.");
                        // request.setAttribute("regionalOffice", true);

                        result = "?errMsg=Fail to create store due to duplicated store name.";

                        nextPage = "/FacilityManagement_StoreServlet/storeManagement_index" + result;
                    } else {
                        System.out.println("Posting from create store :");
                        StoreEntity store = fmBean.createStore(currentLoggedInStaffID, storeName, address, telephone, email, countryID, postalCode, imageURL, latitude, longitude);
                        fmBean.addStoreToRegionalOffice(currentLoggedInStaffID, regionalOfficeId, store.getId());
                        if (store != null) {
                            result = "?goodMsg=A new store record has been saved.";
                            if (file != null) {
                                String s = file.getHeader("content-disposition");
                                InputStream fileInputStream = file.getInputStream();
                                OutputStream fileOutputStream = new FileOutputStream(request.getServletContext().getRealPath("/img/storemaps/") + "/" + fileName);

                                System.out.println("fileOutputStream  " + fileOutputStream);
                                int nextByte;
                                while ((nextByte = fileInputStream.read()) != -1) {
                                    fileOutputStream.write(nextByte);
                                }
                                fileOutputStream.close();
                                fileInputStream.close();
                            }
                        } else {
                            result = "?errMsg=Fail to create store due to duplicated store name.";
                        }
                        nextPage = "/FacilityManagement_StoreServlet/storeManagement_index" + result;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;

            case "/editStore_GET":
                storeId = (long) request.getAttribute("storeId");
                System.out.println("Store ID is " + storeId);
                StoreHelper storeHelper = fmBean.getStoreHelperClass(storeId);
                request.setAttribute("storeHelper", storeHelper);
                List<RegionalOfficeEntity> regionalOfficeList = fmBean.viewListOfRegionalOffice();
                request.setAttribute("regionalOfficeList", regionalOfficeList);
                List<CountryEntity> countryList = fmBean.getListOfCountries();
                request.setAttribute("countryList", countryList);
                nextPage = "/A6/editStore";
                break;

            case "/editStore_POST":
                String storeName = request.getParameter("storeName");
                String address = request.getParameter("address");
                String telephone = request.getParameter("telephone");
                String email = request.getParameter("email");
                String longitude = request.getParameter("longitude");
                String latitude = request.getParameter("latitude");  
                Long id = Long.parseLong(request.getParameter("storeId"));
                Long regionalOfficeId = Long.parseLong(request.getParameter("regionalOfficeId"));
                Long countryID = Long.parseLong(request.getParameter("countryID"));
                Part file = request.getPart("javafile");
                String fileName = storeName + ".jpg";
                String imageURL = "/IS3102_Project-war/img/storemaps/" + fileName;

                if (fmBean.editStore(currentLoggedInStaffID, id, storeName, address, telephone, email, countryID, imageURL, latitude, longitude)
                        && fmBean.updateStoreToRegionalOffice(currentLoggedInStaffID, regionalOfficeId, id)) {
                    if (file != null) {
                        String s = file.getHeader("content-disposition");
                        InputStream fileInputStream = file.getInputStream();
                        OutputStream fileOutputStream = new FileOutputStream(request.getServletContext().getRealPath("/img/storemaps/") + "/" + fileName);

                        System.out.println("fileOutputStream  " + fileOutputStream);
                        int nextByte;
                        while ((nextByte = fileInputStream.read()) != -1) {
                            fileOutputStream.write(nextByte);
                        }
                        fileOutputStream.close();
                        fileInputStream.close();
                    }
                    result = "?goodMsg=The store has been updated.";
                } else {
                    result = "?errMsg=Fail to edit store.";
                }
                nextPage = "/FacilityManagement_StoreServlet/storeManagement_index" + result;
                break;

            case "/deleteStore":
                String[] deletes = request.getParameterValues("delete");
                if (deletes != null) {
                    for (String storeString : deletes) {
                        String store_Id = storeString;
                        storeId = Long.parseLong(store_Id);
                        fmBean.removeStore(currentLoggedInStaffID, storeId);
                    }
                }
                result = "?goodMsg=Successfully removed: " + deletes.length + " record(s).";

                nextPage = "/FacilityManagement_StoreServlet/storeManagement_index" + result;
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
