package B_servlets;

import HelperClasses.Furniture;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ECommerce_FurnitureCategoryServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session = request.getSession();
            String category = URLDecoder.decode(request.getParameter("cat"));
            Long countryID = (Long) session.getAttribute("countryID");

            List<Furniture> furniture = retrieveFurnitureByCategoryRESTful(countryID, category);
            session.setAttribute("furnitures", furniture);
            String categoryEncoded = URLEncoder.encode(category);

            if (furniture == null) {
                response.sendRedirect("/IS3102_Project-war/B/SG/furnitureCategory.jsp?cat=" + categoryEncoded + "&errorMsg=No furniture to be displayed.");
                return;
            }
            response.sendRedirect("/IS3102_Project-war/B/SG/furnitureCategory.jsp?cat=" + categoryEncoded);
        } catch (Exception ex) {
            out.println("\n\n " + ex.getMessage());
        }
    }

    public List<Furniture> retrieveFurnitureByCategoryRESTful(Long countryID, String category) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client
                .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.furnitureentity").path("getFurnitureListByCategory")
                .queryParam("countryID", countryID)
                .queryParam("category", category);
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        invocationBuilder.header("some-header", "true");
        Response response = invocationBuilder.get();
        System.out.println("status: " + response.getStatus());

        if (response.getStatus() != 200) {
            return null;
        }

        List<Furniture> list = response.readEntity(new GenericType<List<Furniture>>() {
        });
        System.out.println("list size: " + list.size());
        return list;
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
