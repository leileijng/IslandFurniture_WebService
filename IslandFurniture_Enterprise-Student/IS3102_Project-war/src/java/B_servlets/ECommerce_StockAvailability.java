package B_servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ECommerce_StockAvailability extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String storeIDstring = request.getParameter("storeID");
            String SKU = request.getParameter("sku");
            String type = request.getParameter("type");

            //<editor-fold defaultstate="collapsed" desc="check storeID and SKU validity">
            if ((storeIDstring == null || storeIDstring.equals("")) && (SKU == null || SKU.equals(""))) {
                response.sendRedirect("/IS3102_Project-war/B/SG/index.jsp");
            } else if (storeIDstring == null || storeIDstring.equals("")) {
                if (type.equals("Furniture")) {
                    response.sendRedirect("/IS3102_Project-war/B/SG/furnitureProductDetails.jsp?sku=" + SKU);
                } else if (type.equals("Retail Product")) {
                    response.sendRedirect("/IS3102_Project-war/B/SG/retailProductDetails.jsp?sku=" + SKU);
                }
            }
            //</editor-fold>

            Long storeID = Long.parseLong(storeIDstring);
            int itemQty = getQuantity(storeID, SKU);

            if (type.equals("Furniture")) {
                response.sendRedirect("/IS3102_Project-war/B/SG/furnitureProductDetails.jsp?sku=" + SKU + "&itemQty=" + itemQty + "&storeID=" + storeID);
            } else if (type.equals("Retail Product")) {
                response.sendRedirect("/IS3102_Project-war/B/SG/retailProductDetails.jsp?sku=" + SKU + "&itemQty=" + itemQty + "&storeID=" + storeID);
            }

        } catch (Exception ex) {
            out.println("\n\n " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public int getQuantity(Long storeID, String SKU) {
        try {
            System.out.println("getQuantity() SKU: " + SKU);
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.storeentity")
                    .path("getQuantity")
                    .queryParam("storeID", storeID)
                    .queryParam("SKU", SKU);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            System.out.println("status: " + response.getStatus());
            if (response.getStatus() != 200) {
                return 0;
            }
            String result = (String) response.readEntity(String.class);
            System.out.println("Result returned from ws: " + result);
            return Integer.parseInt(result);

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
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
