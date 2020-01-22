package OperationalCRM.SalesRecording;

import java.util.List;
import javax.ejb.Local;

@Local
public interface SalesRecordingBeanLocal {
    public Boolean createSalesRecord(String staffEmail, String staffPassword, Long storeID, String posName,List<String> itemsPurchasedSKU, List<Integer> itemsPurchasedQty, Double amountDue, Double amountPaid, Double amountPaidUsingPoints, Integer loyaltyPointsDeducted, String memberEmail, String receiptNo);
}
