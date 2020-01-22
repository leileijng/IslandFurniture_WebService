package A6_servlets;

import CorporateManagement.RestaurantManagement.RestaurantManagementBeanLocal;
import EntityManager.LineItemEntity;
import EntityManager.RawIngredientEntity;
import EntityManager.RecipeEntity;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RecipeManagement_LineItemRecipeServlet extends HttpServlet {

    @EJB
    private RestaurantManagementBeanLocal restaurantManagementBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String errMsg = request.getParameter("errMsg");
            String goodMsg = request.getParameter("goodMsg");

            Long recipeId = Long.parseLong(request.getParameter("id"));
            RecipeEntity recipe = restaurantManagementBean.viewSingleRecipe(recipeId);
            List<LineItemEntity> recipeListLineOfItems = recipe.getListOfLineItems();
            List<RawIngredientEntity> rawIngredients = restaurantManagementBean.listAllRawIngredients();

            HttpSession session = request.getSession();
            session.setAttribute("recipeListLineOfItems", recipeListLineOfItems);
            session.setAttribute("recipeId", recipeId);
            session.setAttribute("rawIngredients", rawIngredients);
            
             if (errMsg == null && goodMsg == null) {
                response.sendRedirect("A6/recipeManagement_lineItemManagement.jsp?recipeName=" + recipe.getName());
            } else if ((errMsg != null) && (goodMsg == null)) {
                if (!errMsg.equals("")) {
                response.sendRedirect("A6/recipeManagement_lineItemManagement.jsp?errMsg=" + errMsg + "&recipeName=" + recipe.getName());
                }
            } else if ((errMsg == null && goodMsg != null)) {
                if (!goodMsg.equals("")) {
                response.sendRedirect("A6/recipeManagement_lineItemManagement.jsp?goodMsg=" + goodMsg + "&recipeName=" + recipe.getName());
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
