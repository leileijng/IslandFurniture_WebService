package A6_servlets;

import CorporateManagement.RestaurantManagement.RestaurantManagementBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RecipeManagement_LinkRecipeServlet extends HttpServlet {

    @EJB
    private RestaurantManagementBeanLocal restaurantManagementBean;
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String id = request.getParameter("id");
            String menuItemId = request.getParameter("menuItemId" + id);
            if (menuItemId.equals("")) {
                result = "?errMsg=Please try again.";
                response.sendRedirect("RecipeManagement_RecipeServlet" + result);
            }
            System.out.println("RecipeId: " + id);
            System.out.println("menuItemId: " + menuItemId);

            boolean success = restaurantManagementBean.linkRecipeAndMenuItem(Long.parseLong(id), Long.parseLong(menuItemId));
            if (!success) {
                result = "?errMsg=Please try again.";
                response.sendRedirect("RecipeManagement_RecipeServlet" + result);
            } else {
                result = "?goodMsg=Recipe linked successfully.";
                response.sendRedirect("RecipeManagement_RecipeServlet" + result);
            }
        } catch (Exception ex) {
            out.println(ex);

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
