package OperationalCRM.SalesRecording;

import EntityManager.LineItemEntity;
import HelperClasses.ReturnHelper;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ws.rs.Path;

@WebService(serviceName = "SalesRecordingWebService")
@Stateless
public class SalesRecordingWebService {

    @EJB
    SalesRecordingBeanLocal SalesRecordingBeanLocal;

    @WebMethod
    public Boolean createSalesRecord(@WebParam(name = "staffEmail") String staffEmail, @WebParam(name = "password") String staffPasword, @WebParam(name = "storeID") Long storeID, @WebParam(name = "posName") String posName, @WebParam(name = "itemsPurchasedSKU") List<String> itemsPurchasedSKU, @WebParam(name = "itemsPurchasedQty") List<Integer> itemsPurchasedQty, @WebParam(name = "amountDue") Double amountDue, @WebParam(name = "amountPaid") Double amountPaid, @WebParam(name = "amountPaidUsingPoints") Double amountPaidUsingPoints, @WebParam(name = "loyaltyPointsDeducted") Integer loyaltyPointsDeducted, @WebParam(name = "memberEmail") String memberEmail,@WebParam(name = "receiptNo") String receiptNo) {
        return SalesRecordingBeanLocal.createSalesRecord(staffEmail, staffPasword, storeID, posName, itemsPurchasedSKU, itemsPurchasedQty, amountDue, amountPaid, amountPaidUsingPoints, loyaltyPointsDeducted, memberEmail, receiptNo);
    }
}
