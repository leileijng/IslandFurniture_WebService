package A1_servlets;

import CommonInfrastructure.Workspace.WorkspaceBeanLocal;
import EntityManager.StaffEntity;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class WorkspaceMessage_RemoveServlet extends HttpServlet {

    @EJB
    private WorkspaceBeanLocal workspaceBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        try {
            HttpSession session;
            session = request.getSession();
            StaffEntity staffEntity = (StaffEntity) session.getAttribute("staffEntity");
            if (staffEntity == null) {
                response.sendRedirect("A1/staffLogin.jsp");
            }

            String deleteType = request.getParameter("deleteType");//single or many
            String[] deleteArr = request.getParameterValues("delete");
            String deleteMessageType = request.getParameter("deleteMessageType");//inbox or sentMessages
            String deleteSingleMessageID = request.getParameter("messageID");
            String view = (String) session.getAttribute("view");
            if (deleteType!=null && deleteType.equals("many")) {
                if (deleteArr != null) {
                    for (int i = 0; i < deleteArr.length; i++) {
                        if (deleteMessageType.equals("inbox")) {
                            workspaceBean.deleteSingleInboxMessage(staffEntity.getId(), Long.parseLong(deleteArr[i]));
                        } else {
                            workspaceBean.deleteSingleOutboxMessage(staffEntity.getId(), Long.parseLong(deleteArr[i]));
                        }
                    }
                    if (view.equals("sentMessages")) {
                        response.sendRedirect("WorkspaceMessage_Servlet?view=sentMessages&goodMsg=Successfully deleted: " + deleteArr.length + " messages(s).");
                    } else {
                        response.sendRedirect("WorkspaceMessage_Servlet?view=inbox&goodMsg=Successfully deleted: " + deleteArr.length + " messages(s).");
                    }
                } else {
                    if (view.equals("sentMessages")) {
                        response.sendRedirect("WorkspaceMessage_Servlet?view=sentMessages&errMsg=No messages selected for deletion.");
                    } else {
                        response.sendRedirect("WorkspaceMessage_Servlet?view=inbox&errMsg=No messages selected for deletion.");
                    }
                }
            } else if (deleteSingleMessageID != null && deleteSingleMessageID != "") {
                System.out.println("Servlet deleting single message.");
                if (deleteMessageType.equals("inbox")) {
                    workspaceBean.deleteSingleInboxMessage(staffEntity.getId(), Long.parseLong(deleteSingleMessageID));
                    response.sendRedirect("WorkspaceMessage_Servlet?goodMsg=Successfully deleted message.");
                } else { //outbox
                    workspaceBean.deleteSingleOutboxMessage(staffEntity.getId(), Long.parseLong(deleteSingleMessageID));
                    response.sendRedirect("WorkspaceMessage_Servlet?view=sentMessages&goodMsg=Successfully deleted message.");
                }
            } else {
                System.out.println("asd");
                response.sendRedirect("WorkspaceMessage_Servlet?view=inbox&errMsg=No messages selected for deletion.");
            }
        } catch (Exception ex) {
            out.println(ex);
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
