package A6_servlets;

import CorporateManagement.RestaurantManagement.RestaurantManagementBeanLocal;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig
public class ComboManagement_UpdateServlet extends HttpServlet {

    @EJB
    private RestaurantManagementBeanLocal RestaurantManagementBean;
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String sku = request.getParameter("sku");
            String description = request.getParameter("description");
            Part file = request.getPart("javafile");
            String fileName = sku + ".jpg";
            String imageURL = "/IS3102_Project-war/img/products/" + fileName;

            boolean canUpdate = RestaurantManagementBean.editCombo(Long.parseLong(id), sku, name, description, imageURL);

            if (!canUpdate) {
                result = "?errMsg=Combo SKU already exists or Combo ID not found.&id=" + id;
                response.sendRedirect("A6/comboManagement_Update.jsp" + result);
            } else {
                result = "?goodMsg=Combo updated successfully.&id=" + id;
                if (file != null) {
                    String s = file.getHeader("content-disposition");
                    InputStream fileInputStream = file.getInputStream();
                    OutputStream fileOutputStream = new FileOutputStream(request.getServletContext().getRealPath("/img/products/") + "/" + fileName);

                    System.out.println("fileOutputStream  " + fileOutputStream);
                    int nextByte;
                    while ((nextByte = fileInputStream.read()) != -1) {
                        fileOutputStream.write(nextByte);
                    }
                    fileOutputStream.close();
                    fileInputStream.close();
                }
                response.sendRedirect("ComboLineItemManagement_Servlet" + result);
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
