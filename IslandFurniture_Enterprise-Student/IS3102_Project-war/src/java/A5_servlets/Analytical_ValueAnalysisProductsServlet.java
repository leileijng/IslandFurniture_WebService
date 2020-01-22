package A5_servlets;

import AnalyticalCRM.ValueAnalysis.CustomerValueAnalysisBeanLocal;
import EntityManager.FurnitureEntity;
import EntityManager.ItemEntity;
import EntityManager.LineItemEntity;
import EntityManager.MenuItemEntity;
import EntityManager.RetailProductEntity;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Analytical_ValueAnalysisProductsServlet extends HttpServlet {

    @EJB
    CustomerValueAnalysisBeanLocal customerValueAnalysisBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session = request.getSession();
            List<LineItemEntity> sortBestSellingFurniture = customerValueAnalysisBean.sortBestSellingFurniture();
            List<LineItemEntity> sortBestSellingFurniture1Year = customerValueAnalysisBean.sortBestSellingFurniture1Year();
            session.setAttribute("sortBestSellingFurniture", sortBestSellingFurniture);
            session.setAttribute("sortBestSellingFurniture1Year",sortBestSellingFurniture1Year);

            List<Date> dateOfLastPurchaseFurniture = new ArrayList();

            for (LineItemEntity item : sortBestSellingFurniture) {

                Date itemPurchasedDate = customerValueAnalysisBean.getItemLastPurchase(item.getItem().getId());
                dateOfLastPurchaseFurniture.add(itemPurchasedDate);
            }
            
            session.setAttribute("dateOfLastPurchaseFurniture", dateOfLastPurchaseFurniture);

            List<LineItemEntity> listOfSecondProduct = new ArrayList();
            for (LineItemEntity item : sortBestSellingFurniture) {
                if (item.getItem().getName() != null) {
                    LineItemEntity secondBestItem = customerValueAnalysisBean.getSecondProductFromFirst(item.getItem().getName());
                    if (secondBestItem == null) {
                        LineItemEntity lineItem = new LineItemEntity();
                        ItemEntity newItem = new FurnitureEntity();
                        newItem.setName("");
                        lineItem.setQuantity(0);
                        lineItem.setItem(newItem);
                        listOfSecondProduct.add(lineItem);
                    } else {
                        listOfSecondProduct.add(secondBestItem);
                    }
                } else {
                    LineItemEntity lineItem = new LineItemEntity();
                    ItemEntity newItem = new FurnitureEntity();
                    newItem.setName("");
                    lineItem.setQuantity(0);
                    lineItem.setItem(newItem);
                    listOfSecondProduct.add(lineItem);
                }
            }

            session.setAttribute("listOfSecondProduct", listOfSecondProduct);

            List<LineItemEntity> sortBestSellingRetailProducts = customerValueAnalysisBean.sortBestSellingRetailProducts();
            List<LineItemEntity> sortBestSellingRetailProduct1Year = customerValueAnalysisBean.sortBestSellingRetailProduct1Year();
            session.setAttribute("sortBestSellingRetailProducts", sortBestSellingRetailProducts);
            session.setAttribute("sortBestSellingRetailProduct1Year",sortBestSellingRetailProduct1Year);
            
            List<Date> dateOfLastPurchaseRetailProduct = new ArrayList();

            for (LineItemEntity item : sortBestSellingRetailProducts) {

                Date itemPurchasedDate = customerValueAnalysisBean.getItemLastPurchase(item.getItem().getId());
                dateOfLastPurchaseRetailProduct.add(itemPurchasedDate);
            }
            
            session.setAttribute("dateOfLastPurchaseRetailProduct", dateOfLastPurchaseRetailProduct);

            List<LineItemEntity> listOfSecondProductRP = new ArrayList();
            for (LineItemEntity item : sortBestSellingRetailProducts) {
                if (item.getItem().getName() != null) {
                    LineItemEntity secondBestItem = customerValueAnalysisBean.getSecondProductFromFirstRP(item.getItem().getName());
                    if (secondBestItem == null) {
                        LineItemEntity lineItem = new LineItemEntity();
                        ItemEntity newItem = new RetailProductEntity();
                        newItem.setName("");
                        lineItem.setQuantity(0);
                        lineItem.setItem(newItem);
                        listOfSecondProductRP.add(lineItem);
                    } else {
                        listOfSecondProductRP.add(secondBestItem);
                    }
                } else {
                    LineItemEntity lineItem = new LineItemEntity();
                    ItemEntity newItem = new RetailProductEntity();
                    newItem.setName("");
                    lineItem.setQuantity(0);
                    lineItem.setItem(newItem);
                    listOfSecondProductRP.add(lineItem);
                }
            }

            session.setAttribute("listOfSecondProductRP", listOfSecondProductRP);

            List<LineItemEntity> sortBestSellingMenuItem = customerValueAnalysisBean.sortBestSellingMenuItem();
            List<LineItemEntity> sortBestSellingMenuItem1Year = customerValueAnalysisBean.sortBestSellingMenuItem1Year();
            session.setAttribute("sortBestSellingMenuItem", sortBestSellingMenuItem);
            session.setAttribute("sortBestSellingMenuItem1Year", sortBestSellingMenuItem1Year);
            
            List<Date> dateOfLastPurchaseMenuItem = new ArrayList();

            for (LineItemEntity item : sortBestSellingMenuItem) {

                Date itemPurchasedDate = customerValueAnalysisBean.getItemLastPurchase(item.getItem().getId());
                dateOfLastPurchaseMenuItem.add(itemPurchasedDate);
            }
            
            session.setAttribute("dateOfLastPurchaseMenuItem", dateOfLastPurchaseMenuItem);

            List<LineItemEntity> listOfSecondProductMenuItem = new ArrayList();
            for (LineItemEntity item : sortBestSellingMenuItem) {
                if (item.getItem().getName() != null) {
                    LineItemEntity secondBestItem = customerValueAnalysisBean.getSecondProductFromFirstMenuItem(item.getItem().getName());
                    if (secondBestItem == null) {
                        LineItemEntity lineItem = new LineItemEntity();
                        ItemEntity newItem = new MenuItemEntity();
                        newItem.setName("");
                        lineItem.setQuantity(0);
                        lineItem.setItem(newItem);
                        listOfSecondProductMenuItem.add(lineItem);
                    } else {
                        listOfSecondProductMenuItem.add(secondBestItem);
                    }
                } else {
                    LineItemEntity lineItem = new LineItemEntity();
                    ItemEntity newItem = new MenuItemEntity();
                    newItem.setName("");
                    lineItem.setQuantity(0);
                    lineItem.setItem(newItem);
                    listOfSecondProductMenuItem.add(lineItem);
                }
            }

            session.setAttribute("listOfSecondProductMenuItem", listOfSecondProductMenuItem);

            Integer totalNumberOfFurnitureInID = customerValueAnalysisBean.getTotalFurnitureSoldInCountry("indonesia");
            session.setAttribute("totalNumberOfFurnitureInID", totalNumberOfFurnitureInID);

            Integer totalNumberOfFurnitureInSG = customerValueAnalysisBean.getTotalFurnitureSoldInCountry("singapore");
            session.setAttribute("totalNumberOfFurnitureInSG", totalNumberOfFurnitureInSG);

            Integer totalNumberOfFurnitureInMS = customerValueAnalysisBean.getTotalFurnitureSoldInCountry("malaysia");
            session.setAttribute("totalNumberOfFurnitureInMS", totalNumberOfFurnitureInMS);

            Integer getTotalRetailProductsSoldInID = customerValueAnalysisBean.getTotalRetailProductsSoldInCountry("indonesia");
            session.setAttribute("getTotalRetailProductsSoldInID", getTotalRetailProductsSoldInID);

            Integer getTotalRetailProductsSoldInSG = customerValueAnalysisBean.getTotalRetailProductsSoldInCountry("singapore");
            session.setAttribute("getTotalRetailProductsSoldInSG", getTotalRetailProductsSoldInSG);

            Integer getTotalRetailProductsSoldInMS = customerValueAnalysisBean.getTotalRetailProductsSoldInCountry("malaysia");
            session.setAttribute("getTotalRetailProductsSoldInMS", getTotalRetailProductsSoldInMS);

            response.sendRedirect("A5/products.jsp");
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
