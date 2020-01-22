package A4_servlets;

import CorporateManagement.ItemManagement.ItemManagementBeanLocal;
import EntityManager.CountryEntity;
import EntityManager.ItemEntity;
import EntityManager.PromotionEntity;
import OperationalCRM.PromotionalSales.PromotionalSalesBeanLocal;
import SCM.InboundAndOutboundLogistics.InboundAndOutboundLogisticsBeanLocal;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@MultipartConfig
public class PromotionalSalesManagement_UpdateServlet extends HttpServlet {

    @EJB
    private PromotionalSalesBeanLocal promotionalSalesBeanLocal;
    @EJB
    private InboundAndOutboundLogisticsBeanLocal inboundAndOutboundLogisticsBeanLocal;
    @EJB
    private ItemManagementBeanLocal itemManagementBeanLocal;
    String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {       
            String id = request.getParameter("id");
            String sku = request.getParameter("sku");
            String countryId = request.getParameter("country");
            String source = request.getParameter("source");
            String discountRate = request.getParameter("discountRate");
            String startDateS = request.getParameter("startDate");
            String endDateS = request.getParameter("endDate");
            String description = request.getParameter("description");
            Part file = request.getPart("javafile");            
           
            
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = formatter.parse(startDateS);
            Date endDate = formatter.parse(endDateS);
            String datestring = formatter.format(endDate);

            if (endDate.before(startDate)) {
                result = "?errMsg=Failed to update promotion. End date is earlier than start date.";
                response.sendRedirect("PromotionalSalesManagement_Servlet" + result);
            }

            else if (inboundAndOutboundLogisticsBeanLocal.checkSKUExists(sku)) {
                String fileName = sku + datestring + ".jpg";
                String imageURL = "/IS3102_Project-war/img/promotions/" + fileName;

                if (promotionalSalesBeanLocal.checkIfPromotionCreated(Long.parseLong(id), sku, Long.parseLong(countryId), startDate, endDate)) {
                    ItemEntity item = itemManagementBeanLocal.getItemBySKU(sku);
                    CountryEntity country = promotionalSalesBeanLocal.getCountryByCountryId(Long.parseLong(countryId));
                    promotionalSalesBeanLocal.editPromotion(Long.parseLong(id), item, country, Double.parseDouble(discountRate), startDate, endDate, imageURL, description);
                    result = "?goodMsg=Promotion has been edited successfully.";

                    if (file != null) {
                        String s = file.getHeader("content-disposition");
                        InputStream fileInputStream = file.getInputStream();
                        OutputStream fileOutputStream = new FileOutputStream(request.getServletContext().getRealPath("/img/promotions/") + "/" + fileName);

                        System.out.println("fileOutputStream  " + fileOutputStream);
                        int nextByte;
                        while ((nextByte = fileInputStream.read()) != -1) {
                            fileOutputStream.write(nextByte);
                        }
                        fileOutputStream.close();
                        fileInputStream.close();
                    }

                    response.sendRedirect("PromotionalSalesManagement_Servlet" + result);
                } else {
                    result = "?errMsg=Failed to update promotion. There is an overlapping promotion occuring within the dates selected";
                    response.sendRedirect("PromotionalSalesManagement_Servlet" + result);
                }
                
            } else {
                result = "?errMsg=Failed to update promotion, SKU: " + sku + " does not exist.";
                response.sendRedirect("PromotionalSalesManagement_Servlet" + result);
            }
        } catch (Exception ex) {
            out.println(ex);
        } finally {
            out.close();
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
