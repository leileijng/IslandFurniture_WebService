package A1_servlets;

import CommonInfrastructure.AccountManagement.AccountManagementBeanLocal;
import CommonInfrastructure.SystemSecurity.SystemSecurityBeanLocal;
import EntityManager.StaffEntity;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

public class StaffManagement_AddStaffServlet extends HttpServlet {

    @EJB
    private AccountManagementBeanLocal accountManagementBean;
    private String result;

    @EJB
    private SystemSecurityBeanLocal systemSecurityBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session = request.getSession();
            StaffEntity currentLoggedInStaffEntity = (StaffEntity) session.getAttribute("staffEntity");
            String currentLoggedInStaffID;
            if (currentLoggedInStaffEntity == null) {
                currentLoggedInStaffID = "System";
            } else {
                currentLoggedInStaffID = currentLoggedInStaffEntity.getId().toString();
            }
            String identificationNo = request.getParameter("identificationNo");
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String source = request.getParameter("source");

            boolean ifExist = accountManagementBean.checkStaffEmailExists(email);
            if (ifExist) {
                result = "?errMsg=Registration fail. Staff email already registered.";
                response.sendRedirect(source + result);
            } else {
                if (source.equals("A1/staffManagement_add.jsp")) {
                    accountManagementBean.registerStaff(currentLoggedInStaffID, identificationNo, name, phone, email, address, password);
                    systemSecurityBean.sendActivationEmailForStaff(email);
                    result = "?goodMsg=Staff added successfully.";
                    response.sendRedirect("StaffManagement_StaffServlet" + result);
                } else {
                    String remoteAddr = request.getRemoteAddr();
                    ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
                    reCaptcha.setPrivateKey("6LdjyvoSAAAAAHnUl50AJU-edkUqFtPQi9gCqDai");

                    String challenge = request.getParameter("recaptcha_challenge_field");
                    String uresponse = request.getParameter("recaptcha_response_field");
                    ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);

                    if (reCaptchaResponse.isValid()) {
                        accountManagementBean.registerStaff(currentLoggedInStaffID, identificationNo, name, phone, email, address, password);
                        systemSecurityBean.sendActivationEmailForStaff(email);
                        result = "?goodMsg=Staff added successfully.";
                        response.sendRedirect(source + result);
                    } else {
                        result = "?errMsg=You have entered an wrong Captcha code.";
                        response.sendRedirect("A1/staffRegister.jsp" + result);
                    }

                }
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
