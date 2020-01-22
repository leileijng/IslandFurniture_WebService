package A3_servlets;

import CorporateManagement.FacilityManagement.FacilityManagementBeanLocal;
import EntityManager.TransferOrderEntity;
import EntityManager.WarehouseEntity;
import SCM.ManufacturingInventoryControl.ManufacturingInventoryControlBeanLocal;
import SCM.ManufacturingWarehouseManagement.ManufacturingWarehouseManagementBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ManufacturingWarehouseManagement_Servlet extends HttpServlet {

    @EJB
    private FacilityManagementBeanLocal facilityManagementBeanLocal;
    @EJB
    private ManufacturingInventoryControlBeanLocal micbl;
    @EJB
    private ManufacturingWarehouseManagementBeanLocal mwmbl;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session;
            session = request.getSession();
            String errMsg = request.getParameter("errMsg");
            String destination = request.getParameter("destination");
            String warehouseId = request.getParameter("id");

            if (destination != null && warehouseId != null) {

                double[] warehousesCapacity = new double[4];
                double totalPallet = micbl.getTotalVolumeOfPalletStorageBin(Long.parseLong(warehouseId));
                double freePallet = micbl.getTotalFreeVolumeOfPalletStorageBin(Long.parseLong(warehouseId));
                double totalShelf = micbl.getTotalVolumeOfShelfStorageBin(Long.parseLong(warehouseId));
                double freeShelf = micbl.getTotalFreeVolumeOfShelfStorageBin(Long.parseLong(warehouseId));
                double totalInbound = micbl.getTotalVolumeOfInboundStorageBin(Long.parseLong(warehouseId));
                double freeInbound = micbl.getTotalFreeVolumeOfInboundStorageBin(Long.parseLong(warehouseId));
                double totalOutbound = micbl.getTotalVolumeOfOutboundStorageBin(Long.parseLong(warehouseId));
                double freeOutbound = micbl.getTotalFreeVolumeOfOutboundStorageBin(Long.parseLong(warehouseId));
                List<TransferOrderEntity> latestTransferOrders = mwmbl.viewLatestCompletedTransferOrders(Long.parseLong(warehouseId));
                session.setAttribute("latestTransferOrders", latestTransferOrders);

                System.out.println("Checking total at start");
                System.out.println("Total Pallet: "+totalPallet + " , Total Shelf: " + totalShelf + " , Total Inbound: " + totalInbound + " , Total Outbound: " + totalOutbound);

                System.out.println("Checking free at start");
                System.out.println("Free Pallet: " + freePallet + " , Free Shelf: " + freeShelf + " , Free Inbound: " + freeInbound + " , Free Outbound: " + freeOutbound);

                if (totalPallet == 0) {
                    warehousesCapacity[0] = 0;
                } else {
                    warehousesCapacity[0] = ((totalPallet - freePallet) * 100) / totalPallet;
                    warehousesCapacity[0] = Math.round(warehousesCapacity[0] * 100);
                    warehousesCapacity[0] = warehousesCapacity[0] / 100;
                }
                if (totalShelf == 0) {
                    warehousesCapacity[1] = 0;
                } else {
                    warehousesCapacity[1] = ((totalShelf - freeShelf) * 100) / totalShelf;
                    warehousesCapacity[1] = Math.round(warehousesCapacity[1] * 100);
                    warehousesCapacity[1] = warehousesCapacity[1] / 100;
                }
                if (totalInbound == 0) {
                    warehousesCapacity[2] = 0;
                } else {
                    warehousesCapacity[2] = ((totalInbound - freeInbound) * 100) / totalInbound;
                    warehousesCapacity[2] = Math.round(warehousesCapacity[2] * 100);
                    warehousesCapacity[2] = warehousesCapacity[2] / 100;
                }
                if (totalOutbound == 0) {
                    warehousesCapacity[3] = 0;
                } else {
                    warehousesCapacity[3] = ((totalOutbound - freeOutbound) * 100) / totalOutbound;
                    warehousesCapacity[3] = Math.round(warehousesCapacity[3] * 100);
                    warehousesCapacity[3] = warehousesCapacity[3] / 100;
                }

                System.out.println("final free space in %");
                System.out.println("Warehouse Capactiy[0]: " + warehousesCapacity[0] + " , Warehouse Capactiy[1]: " + warehousesCapacity[1] + " , Warehouse Capactiy[2]: " + warehousesCapacity[2] + " , Warehouse Capactiy[3]: " + warehousesCapacity[3]);

                session.setAttribute("warehousesCapacity", warehousesCapacity);

                if (destination.equals("manufacturingWarehouseManagement.jsp")) {
                    WarehouseEntity warehouseEntity = facilityManagementBeanLocal.getWarehouseById(Long.parseLong(warehouseId));
                    session.setAttribute("warehouseEntity", warehouseEntity);
                    response.sendRedirect("A3/manufacturingWarehouseManagement.jsp");
                }
            } else {
                List<WarehouseEntity> warehouses = facilityManagementBeanLocal.getMFWarehouseList();
                session.setAttribute("warehouses", warehouses);

                if (errMsg == null || errMsg.equals("")) {
                    response.sendRedirect("A3/manufacturingWarehouseManagement_view.jsp");
                } else {
                    response.sendRedirect("A3/manufacturingWarehouseManagement_view.jsp?errMsg=" + errMsg);
                }
            }
        } catch (Exception ex) {
            out.println("\n\n " + ex.getMessage());
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
