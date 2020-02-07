/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.ShoppingCartLineItem;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ziyun
 */
@WebServlet(name = "ECommerce_RemoveItemFromListServlet", urlPatterns = {"/ECommerce_RemoveItemFromListServlet"})
public class ECommerce_RemoveItemFromListServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
           
             HttpSession s = request.getSession();
             
        try {
            String[] SKUs = request.getParameterValues("delete");
            
             List<ShoppingCartLineItem> cart = (List<ShoppingCartLineItem>) request.getSession().getAttribute("myCart");
             
             int cartSize = cart.size();
             
             int numberOfRecordDeleted = 0;
             
             for(int u = cartSize - 1; u >= 0; u--){
                 for(int i = 0; i <SKUs.length; i++){
                     if(cart.get(u).getSKU().equals(SKUs[i])){
                         numberOfRecordDeleted++;
                         cart.remove(cart.get(u));
                         break;
                     }
                 }
             }
             if (cart == null){
                 cart = new ArrayList<>();
                 request.getSession().setAttribute("myCart", cart);
             }
            String result = "Successfully removed: " + numberOfRecordDeleted + " record(s).";
            response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + result);
             
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
