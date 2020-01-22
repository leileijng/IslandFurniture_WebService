package B_servlets;

import CommonInfrastructure.AccountManagement.AccountManagementBeanLocal;
import CommonInfrastructure.SystemSecurity.SystemSecurityBeanLocal;
import HelperClasses.Member;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@WebServlet(name = "ECommerce_MemberEditProfileServlet", urlPatterns = {"/ECommerce_MemberEditProfileServlet"})
public class ECommerce_MemberEditProfileServlet extends HttpServlet {

    @EJB
    private AccountManagementBeanLocal accountManagementBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String result = "";
        Member memberToUpdate = new Member();

        Long id = Long.parseLong(request.getParameter("id"));
        memberToUpdate.setId(id);

        String name = request.getParameter("name");
        memberToUpdate.setName(name);

        String email = request.getParameter("email");
        memberToUpdate.setEmail(email);
        
        String phone = request.getParameter("phone");
        memberToUpdate.setPhone(phone);

        String country = request.getParameter("country");
        memberToUpdate.setCity(country);

        String address = request.getParameter("address");
        memberToUpdate.setAddress(address);

        int securityQuestion = Integer.parseInt(request.getParameter("securityQuestion"));
        memberToUpdate.setSecurityQuestion(securityQuestion);

        String securityAnswer = request.getParameter("securityAnswer");
        memberToUpdate.setSecurityAnswer(securityAnswer);

        int age = Integer.parseInt(request.getParameter("age"));
        memberToUpdate.setAge(age);

        int income = Integer.parseInt(request.getParameter("income"));
        memberToUpdate.setIncome(income);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.memberentity")
                .path("updateMember");

        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);

        Response editProfileSuccess = invocationBuilder.put(Entity.json(memberToUpdate));

        HttpSession s = request.getSession();

        boolean updated = true;

        if (editProfileSuccess.getStatus() == 200) {
            s.setAttribute("member", memberToUpdate);
            s.setAttribute("memberName",memberToUpdate.getName());
            result += "Personal Information updated successfully";
        } else {
            updated = false;
            result += "Personal Information update failed";
        }

        String newPassword = request.getParameter("password");

        //check for the password updating
        if (newPassword != null && !newPassword.equals("")) {
            target = client.target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.memberentity")
                    .path("updatePassword")
                    .queryParam("email", email)
                    .queryParam("password", newPassword);

            invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response updatePwdResponse = invocationBuilder.put(Entity.entity("", "application/json"));

            if (updatePwdResponse.getStatus() == 200) {
                result += " and Password changed successfully";
            } else {
                updated = false;
                result += " and Password update failed";
            }
        }

        if (updated) {
            response.sendRedirect("/IS3102_Project-war/B/SG/memberProfile.jsp?goodMsg=" + result);
        } else {
            response.sendRedirect("/IS3102_Project-war/B/SG/memberProfile.jsp?errMsg=" + result);
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
