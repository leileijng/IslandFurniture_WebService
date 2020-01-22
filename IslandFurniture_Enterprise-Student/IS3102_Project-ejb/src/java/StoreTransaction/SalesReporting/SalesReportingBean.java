package StoreTransaction.SalesReporting;

import StoreTransaction.RetailInventory.RetailInventoryBeanLocal;
import StoreTransaction.SalesRecordingWebService_Service;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.ws.WebServiceRef;

@Stateless
public class SalesReportingBean implements SalesReportingBeanLocal {
    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/SalesRecordingWebService/SalesRecordingWebService.wsdl")
    private SalesRecordingWebService_Service service;

    @EJB
    RetailInventoryBeanLocal inventoryBean;

    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;

    @Override
    public Boolean submitSalesRecord(String staffEmail, String staffPassword, Long storeID, String posName, List<String> itemsPurchasedSKU, List<Integer> itemsPurchasedQty, Double amountDue, Double amountPaid, Double amountPaidUsingPoints, Integer loyaltyPointsDeducted, String memberEmail, String receiptNo) {
        System.out.println("submitSalesRecord() called;");
        // Caching/threading method should go here (if got time to do)
        // Rough check for any missing info before submitting it to the HQ
        if (staffEmail == null || staffPassword == null || storeID == null || posName == null) {
            System.out.println("Sales record has missing authorization or information, ignoring request.");
            return false;
        }
        return createSalesRecord(staffEmail, staffPassword, storeID, posName, itemsPurchasedSKU, itemsPurchasedQty, amountDue, amountPaid, amountPaidUsingPoints, loyaltyPointsDeducted, memberEmail,receiptNo);
    }
    
    //consume hq web service

    private Boolean createSalesRecord(java.lang.String staffEmail, java.lang.String password, java.lang.Long storeID, java.lang.String posName, java.util.List<java.lang.String> itemsPurchasedSKU, java.util.List<java.lang.Integer> itemsPurchasedQty, java.lang.Double amountDue, java.lang.Double amountPaid, java.lang.Double amountPaidUsingPoints, java.lang.Integer loyaltyPointsDeducted, java.lang.String memberEmail, java.lang.String receiptNo) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        StoreTransaction.SalesRecordingWebService port = service.getSalesRecordingWebServicePort();
        return port.createSalesRecord(staffEmail, password, storeID, posName, itemsPurchasedSKU, itemsPurchasedQty, amountDue, amountPaid, amountPaidUsingPoints, loyaltyPointsDeducted, memberEmail, receiptNo);
    }
}
