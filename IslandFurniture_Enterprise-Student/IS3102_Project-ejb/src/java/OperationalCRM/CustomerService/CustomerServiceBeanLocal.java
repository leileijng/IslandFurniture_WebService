package OperationalCRM.CustomerService;

import EntityManager.FeedbackEntity;
import EntityManager.PickRequestEntity;
import EntityManager.SalesRecordEntity;
import EntityManager.StaffEntity;
import java.util.List;
import javax.ejb.Local;

@Local
public interface CustomerServiceBeanLocal {

    public List<SalesRecordEntity> viewSalesRecord(Long storeId);

    public List<FeedbackEntity> viewFeedback();

    public StaffEntity pickerLoginStaff(String email, String password);

    public PickRequestEntity getNextPickRequest(Long staffID);

    public PickRequestEntity getPickRequest(Long pickRequestID);

    public PickRequestEntity completePickRequest(Long pickRequestID);

    //public Boolean pickerLogoff(Long staffID);
    //Following methods are called by checkout process
    public Boolean addPickRequest(Long salesRecordID);

    //public Boolean reassignPickRequest(Long pickRequestID);
    //Following methods are for the main console showing status
    public List<PickRequestEntity> getAllPickRequestInStore(Long storeID);

    //=======Receiptionist    
    //Following methods are for the receptionist
    public Boolean callCustomer(Long pickRequestID);

    public List<PickRequestEntity> getPickRequestInStoreForReceptionist(Long storeID);

    public List<PickRequestEntity> getAllUncollectedPickRequestInStore(Long storeID);

    public Boolean markPickRequestAsCollected(Long pickRequestID);

    public Boolean markPickRequestAsUnCollected(Long pickRequestID);

    public Boolean markPickRequestForCollection(String receiptNo);

    public StaffEntity receptionistLoginStaff(String email, String password);

    public List<PickRequestEntity> getLastCalledPickRequestInStoreForReceptionist(Long storeID);
}
