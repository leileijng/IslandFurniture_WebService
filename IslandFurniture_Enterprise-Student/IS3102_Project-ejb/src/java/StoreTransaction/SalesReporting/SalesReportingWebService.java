package StoreTransaction.SalesReporting;

import EntityManager.ItemEntity;
import EntityManager.LineItemEntity;
import HelperClasses.ReturnHelper;
import StoreTransaction.RetailInventory.RetailInventoryBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "SalesReportingWebService")
@Stateless
public class SalesReportingWebService {
    
    @EJB
    SalesReportingBeanLocal salesReportingBean;

    @WebMethod
    public Boolean submitSalesRecord(@WebParam(name = "staffEmail") String staffEmail, @WebParam(name = "password") String staffPassword, @WebParam(name = "storeID") Long storeID, @WebParam(name = "posName") String posName, @WebParam(name = "itemsPurchasedSKU") List<String> itemsPurchasedSKU, @WebParam(name = "itemsPurchasedQty") List<Integer> itemsPurchasedQty, @WebParam(name = "amountDue") Double amountDue,@WebParam(name = "amountPaid") Double amountPaid,@WebParam(name = "amountPaidUsingPoints") Double amountPaidUsingPoints,@WebParam(name = "loyaltyPointsDeducted") Integer loyaltyPointsDeducted, @WebParam(name = "memberEmail") String memberEmail, @WebParam(name = "receiptNo") String receiptNo) {
        return salesReportingBean.submitSalesRecord(staffEmail, staffPassword, storeID, posName, itemsPurchasedSKU, itemsPurchasedQty, amountDue,amountPaid,amountPaidUsingPoints,loyaltyPointsDeducted, memberEmail,receiptNo);
    }    
}
