package B_servlets;

import EntityManager.MemberEntity;
import HelperClasses.Member;
import HelperClasses.ShoppingCartLineItem;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
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
import HelperClasses.StoreHelper;
import java.util.Enumeration;

/**
 *
 * @author Vanesssa Jiang Lei
 */
@WebServlet(name = "ECommerce_PaymentServlet", urlPatterns = {"/ECommerce_PaymentServlet"})
public class ECommerce_PaymentServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession s = request.getSession();

        String failMsg;

        //validate card number
        String cardNumber = request.getParameter("cardno");
        boolean isCardValid = verifyCard(cardNumber);
        if (!isCardValid) {
            failMsg = " Sorry, the checkout failed due to the invalid card number.";
            response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + failMsg);
        } else {
            //get currency depending on country
            Long countryID = (Long) s.getAttribute("countryID");
            String currency = getCurrency(countryID);

            //insert 1 sales record
            //get memberId
            Long memberId = (Long) s.getAttribute("memberId");
            System.out.println("member is " + memberId);
            List<ShoppingCartLineItem> shoppingCart = (List<ShoppingCartLineItem>) request.getSession().getAttribute("myCart");
            //get amount
            double amountPaid = 0.0;
            for (ShoppingCartLineItem item : shoppingCart) {
                amountPaid += item.getPrice() * item.getQuantity();
            }
            Long soID = insertSalesOrder(memberId, currency, amountPaid);
            System.out.println(soID);
            if (soID != Long.valueOf(0)) {
                boolean allSuccess = true;
                for (ShoppingCartLineItem item : shoppingCart) {
                    int itemId = Integer.parseInt(item.getId());
                    System.out.println("item ID" + itemId);
                    int itemQty = item.getQuantity();
                    boolean result = createLineItem(itemId, itemQty);
                    if (!result) {
                        allSuccess = false;
                        break;
                    }
                }

                if (!allSuccess) {
                    System.out.print("error here 1");
                    failMsg = " There was an error in processing your order. Please try again.";
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + failMsg);
                } else {
                    //cleart the shopping cart
                    shoppingCart.clear();
                    //set myCart attribute to session
                    s.setAttribute("myCart", shoppingCart);

                    //The pick-up store
                    String pickUpStore = getPickUpStore();
                    String successResult = "You have checkout successfully! Pick up point: " + pickUpStore;
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + successResult);
                }

            } else {
                System.out.print("error here 2");
                failMsg = " There was an error in processing your order. Please try again.";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + failMsg);
            }
        }

    }

    public boolean verifyCard(String cardNumber) {
        System.out.println(cardNumber.length());
        int[] creditcardInt = new int[cardNumber.length()];
        for (int i = 0; i < cardNumber.length(); i++) {
            creditcardInt[i] = Integer.parseInt(cardNumber.substring(i, i + 1));
        }

        for (int i = creditcardInt.length - 2; i >= 0; i = i - 2) {
            int j = creditcardInt[i];
            j = j * 2;
            if (j > 9) {
                j = j % 10 + 1;
            }
            creditcardInt[i] = j;
        }
        int sum = 0;
        for (int i = 0; i < creditcardInt.length; i++) {
            sum += creditcardInt[i];
        }
        if (sum % 10 == 0) {
            System.out.println(cardNumber + " is a valid credit card number");
            return true;
        } else {
            System.out.println(cardNumber + " is an invalid credit card number");
            return false;
        }

    }

    public String getCurrency(Long countryCode) {
        try {
            System.out.println("Country shopping in: " + countryCode);
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.countryentity")
                    .path("getCountry")
                    .queryParam("countrycode", countryCode);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            System.out.println("get currency status: " + response.getStatus());
            if (response.getStatus() != 200) {
                return null;
            }
            String currency = (String) response.readEntity(String.class);
            System.out.println("Currency returned from ws: " + currency);
            return currency;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Long insertSalesOrder(Long memberId, String currency, double amountPaid) {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/commerce")
                    .path("createEcommerceTransactionRecord")
                    .queryParam("memberID", memberId)
                    .queryParam("currency", currency)
                    .queryParam("amountPaid", amountPaid);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response insertSOResponse = invocationBuilder.post(Entity.entity("", "application/json"));

            if (insertSOResponse.getStatus() == 201) {
                //get the Sales Order ID
                Long salesOrderID;
                salesOrderID = Long.valueOf(Integer.parseInt(insertSOResponse.getHeaderString("getSOnumber")));
                System.out.println("SO: " + salesOrderID);
                return salesOrderID;
            } else {
                return Long.valueOf(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Long.valueOf(0);
        }
    }

    public boolean createLineItem(int itemId, int itemQty) {
        Client newClient = ClientBuilder.newClient();
        WebTarget targetNew = newClient
                .target("http://localhost:8080/IS3102_WebService-Student/webresources/commerce")
                .path("createEcommerceLineItemRecord")
                .queryParam("itemEntityID", itemId)
                .queryParam("quantity", itemQty);
        Invocation.Builder invocationBuilderNew = targetNew.request(MediaType.APPLICATION_JSON);
        Response myResponseNew = invocationBuilderNew.post(Entity.entity("", "application/json"));
        if (myResponseNew.getStatus() != 201) {
            return false;
        } else {
            return true;
        }
    }

    public String getPickUpStore() {
        try {
            System.out.println("Get store info method called");
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.storeentity")
                    .path("getStoreInfo")
                    .queryParam("storeID", 59);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            System.out.println("get currency status: " + response.getStatus());
            if (response.getStatus() != 200) {
                return null;
            }

            String pickUpStore = response.readEntity(String.class);
            System.out.println("Currency returned from ws: " + pickUpStore);
            return pickUpStore;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    
    
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
