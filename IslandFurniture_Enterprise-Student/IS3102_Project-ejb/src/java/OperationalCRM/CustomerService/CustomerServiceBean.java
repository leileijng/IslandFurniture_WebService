package OperationalCRM.CustomerService;

import CommonInfrastructure.AccountManagement.AccountManagementBeanLocal;
import Config.Config;
import CorporateManagement.ItemManagement.ItemManagementBeanLocal;
import EntityManager.AccessRightEntity;
import EntityManager.CountryEntity;
import EntityManager.FeedbackEntity;
import EntityManager.LineItemEntity;
import EntityManager.MemberEntity;
import EntityManager.PickRequestEntity;
import EntityManager.RoleEntity;
import EntityManager.SalesRecordEntity;
import EntityManager.StaffEntity;
import EntityManager.StoreEntity;
import InventoryManagement.StoreAndKitchenInventoryManagement.StoreAndKitchenInventoryManagementBeanLocal;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class CustomerServiceBean implements CustomerServiceBeanLocal {

    @EJB
    AccountManagementBeanLocal ambl;
    @EJB
    StoreAndKitchenInventoryManagementBeanLocal simbl;
    @EJB
    ItemManagementBeanLocal imbl;

    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;

    @Override
    public List<SalesRecordEntity> viewSalesRecord(Long storeId) {
        System.out.println("View sales record is called()" + storeId);
        try {
            Query q = em.createQuery("select sr from SalesRecordEntity sr where sr.store.id = ?1").setParameter(1, storeId);
            List<SalesRecordEntity> salesRecords = q.getResultList();
            return salesRecords;
        } catch (Exception ex) {
            System.out.println("\nServer failed to retrieve sales record:\n" + ex);
            return null;
        }
    }

    @Override
    public List<FeedbackEntity> viewFeedback() {
        System.out.println("View feedback is called()");
        try {
            Query q = em.createQuery("select f from FeedbackEntity f");
            List<FeedbackEntity> feedbacks = q.getResultList();
            return feedbacks;
        } catch (Exception ex) {
            System.out.println("\nServer failed to retrieve feedback:\n" + ex);
            return null;
        }
    }

    @Override
    public StaffEntity pickerLoginStaff(String email, String password) {
        System.out.println("pickerLoginStaff() called");
        Long staffID = null;
        try {
            StaffEntity staffEntity = ambl.loginStaff(email, password);
            if (staffEntity == null) {
                return null;
            }
            staffID = staffEntity.getId();
            // Check roles, only store manager or picker role can login
            if (ambl.checkIfStaffIsStoreManager(staffID) || ambl.checkIfStaffIsPicker(staffID)) {//Store manager or pFicker role
                staffID = staffEntity.getId();
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
                out.println(new Date().toString() + ";" + staffID + ";pickerLoginStaff();" + staffID + ";");
                out.close();
                return staffEntity;
            }

            return null;
        } catch (Exception ex) {
            System.out.println("pickerLoginStaff(): error");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public PickRequestEntity getNextPickRequest(Long staffID) {
        System.out.println("acceptPickRequest() called");
        try {
            StaffEntity staffEntity = em.getReference(StaffEntity.class, staffID);
            //Get the store which the picker belong to
            AccessRightEntity accessRightEntity = ambl.isAccessRightExist(staffID, 12L);//Picker role
            Long storeID = accessRightEntity.getStore().getId();
            //Get pick requests which are not picked yet
            Query q = em.createQuery("SELECT p from PickRequestEntity p where p.store.id=:storeID AND p.pickStatus=1 ORDER BY p.dateSubmitted ASC");
            q.setParameter("storeID", storeID);
            List<PickRequestEntity> pickRequestEntities = (List<PickRequestEntity>) q.getResultList();
            //Get the oldestt one
            PickRequestEntity pickRequestEntity = pickRequestEntities.get(0);
            pickRequestEntity.setPickStatus(2);//Update pick status to 2.In-progress
            pickRequestEntity.setCollectionStatus(1);//Update collection status to 1.Picking
            pickRequestEntity.setPicker(staffEntity);
            em.merge(pickRequestEntity);
            return pickRequestEntity;
        } catch (Exception ex) {
            System.out.println("acceptPickRequest(): error");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public PickRequestEntity getPickRequest(Long pickRequestID) {
        System.out.println("getPickRequest() called");
        try {
            PickRequestEntity pickRequestEntity = em.getReference(PickRequestEntity.class, pickRequestID);
            return pickRequestEntity;
        } catch (Exception ex) {
            System.out.println("getPickRequest(): error");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public PickRequestEntity completePickRequest(Long pickRequestID) {
        System.out.println("completePickRequest() called");
        try {
            PickRequestEntity pickRequestEntity = em.getReference(PickRequestEntity.class, pickRequestID);
            //Update status
            pickRequestEntity.setDateCompleted(new Date());
            pickRequestEntity.setPickStatus(3);//3.Completed
            pickRequestEntity.setCollectionStatus(2);//2.Ready for Collection
            em.merge(pickRequestEntity);

            //Update store inventory
            Long storeID = pickRequestEntity.getStore().getId();
            List<LineItemEntity> itemsInPickRequest = pickRequestEntity.getItems();
            for (int itemsToRemove = itemsInPickRequest.size(); itemsToRemove > 0; itemsToRemove--) {
                String currentItemType = itemsInPickRequest.get(itemsToRemove - 1).getItem().getType();
                switch (currentItemType) { //only remove if is one of the following items type
                    case "Furniture":
                    case "Retail Product":
                    case "Raw Material":
                        simbl.removeItemsFromInventory(storeID, itemsInPickRequest.get(itemsToRemove - 1).getItem().getSKU(), itemsInPickRequest.get(itemsToRemove - 1).getQuantity(), true);
                }
            }
            System.out.println("completePickRequest(): success");
            return pickRequestEntity;
        } catch (Exception ex) {
            System.out.println("completePickRequest(): error");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean addPickRequest(Long salesRecordID) {
        System.out.println("addPickRequest() called");
        try {
            SalesRecordEntity salesRecordEntity = em.getReference(SalesRecordEntity.class, salesRecordID);

            //Find the store
            StoreEntity storeEntity = em.getReference(StoreEntity.class, salesRecordEntity.getStore().getId());

            //Create the items to be picked
            List<LineItemEntity> itemsToBePicked = new ArrayList<LineItemEntity>();
            for (LineItemEntity curr : salesRecordEntity.getItemsPurchased()) {
                if (curr.getItem().getVolume() > Config.minVolumeForCollectionAreaItems) {
                    itemsToBePicked.add(curr);
                }
            }
            //Create the PickRequest (only if got items to be picked)
            String receiptNo = salesRecordEntity.getReceiptNo();
            String queueNo = receiptNo.substring(receiptNo.length() - 4);
            PickRequestEntity pickRequestEntity = new PickRequestEntity(storeEntity, salesRecordEntity, itemsToBePicked, queueNo);
            if (itemsToBePicked.size() > 0) {
                em.persist(pickRequestEntity);
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            System.out.println("addPickRequest(): error");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<PickRequestEntity> getAllPickRequestInStore(Long storeID) {
        System.out.println("getAllPickRequestInStore() called");
        try {
            Query q = em.createQuery("SELECT p from PickRequestEntity p WHERE p.store.id=:storeID ORDER BY p.pickStatus ASC,p.dateSubmitted DESC");
            q.setParameter("storeID", storeID);
            return q.getResultList();
        } catch (Exception ex) {
            System.out.println("getAllPickRequestInStore(): error");
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<PickRequestEntity> getPickRequestInStoreForReceptionist(Long storeID) {
        System.out.println("getPickRequestInStoreForReceptionist() called");
        try {
            Query q = em.createQuery("SELECT p from PickRequestEntity p WHERE p.store.id=:storeID AND p.collectionStatus!=6 ORDER BY p.pickStatus DESC,p.collectionStatus DESC,p.dateSubmitted ASC");
            q.setParameter("storeID", storeID);
            return q.getResultList();
        } catch (Exception ex) {
            System.out.println("getPickRequestInStoreForReceptionist(): error");
            ex.printStackTrace();
            return new ArrayList();
        }
    }

    @Override
    public List<PickRequestEntity> getAllUncollectedPickRequestInStore(Long storeID) {
        System.out.println("getAllUncollectedPickRequestInStore() called");
        try {
            Query q = em.createQuery("SELECT p from PickRequestEntity p WHERE p.store.id=:storeID AND p.collectionStatus=3 AND ORDER BY p.dateSubmitted ASC");
            q.setParameter("storeID", storeID);
            return q.getResultList();
        } catch (Exception ex) {
            System.out.println("getAllUncollectedPickRequestInStore(): error");
            ex.printStackTrace();
            return new ArrayList();
        }
    }

    @Override
    public Boolean markPickRequestAsCollected(Long pickRequestID) {
        System.out.println("markPickRequestAsCollected() called");
        try {
            Query q = em.createQuery("SELECT p from PickRequestEntity p WHERE p.id=:pickRequestID");
            q.setParameter("pickRequestID", pickRequestID);
            PickRequestEntity pickRequestEntity = (PickRequestEntity) q.getSingleResult();
            pickRequestEntity.setCollectionStatus(6);//Collected
            em.merge(pickRequestEntity);
            return true;
        } catch (Exception ex) {
            System.out.println("markPickRequestAsCollected(): error");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean markPickRequestAsUnCollected(Long pickRequestID) {
        System.out.println("markPickRequestAsUnCollected() called");
        try {
            Query q = em.createQuery("SELECT p from PickRequestEntity p WHERE p.id=:pickRequestID");
            q.setParameter("pickRequestID", pickRequestID);
            PickRequestEntity pickRequestEntity = (PickRequestEntity) q.getSingleResult();
            pickRequestEntity.setCollectionStatus(5);//Uncollected
            em.merge(pickRequestEntity);
            return true;
        } catch (Exception ex) {
            System.out.println("markPickRequestAsUnCollected(): error");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean markPickRequestForCollection(String receiptNo) {
        System.out.println("markPickRequestForCollection() called");
        try {
            Query q = em.createQuery("SELECT p from PickRequestEntity p WHERE p.salesRecord.receiptNo=:receiptNo");
            q.setParameter("receiptNo", receiptNo);
            PickRequestEntity pickRequestEntity = (PickRequestEntity) q.getSingleResult();
            pickRequestEntity.setCollectionStatus(4);//Collecting
            em.merge(pickRequestEntity);
            return true;
        } catch (Exception ex) {
            System.out.println("markPickRequestForCollection(): error");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean callCustomer(Long pickRequestID) {
        System.out.println("callCustomer(): called");
        try {
            PickRequestEntity pickRequestEntity = em.getReference(PickRequestEntity.class, pickRequestID);
            pickRequestEntity.setCollectionStatus(3);//Called
            pickRequestEntity.setDateCalled(new Date());
            //SMS to member if sales record is tied to member & member has HP number in profile
            MemberEntity memberEntity = pickRequestEntity.getSalesRecord().getMember();
            if (memberEntity!= null && memberEntity.getCity()!=null && memberEntity.getCity()!="" && memberEntity.getPhone()!=null && memberEntity.getPhone()!="") {
                String country = memberEntity.getCity();
                Query q = em.createQuery("SELECT c from CountryEntity c where c.name=:country");
                q.setParameter("country", country);
                CountryEntity countryEntity = (CountryEntity) q.getSingleResult();
                String countryCode = "00"+countryEntity.getCountryCode();
                String phoneNo = memberEntity.getPhone()+"";
                String telNo = countryCode + phoneNo;
                String smsMessage = "[Island Furniture] Your furniture(s) purchased is ready for collection. Q["+pickRequestEntity.getQueueNo()+"]";
            System.out.println("Sending SMS: " + telNo + ": " +  URLEncoder.encode(smsMessage));

            String requestURL = "http://smsc.vianett.no/v3/send.ashx?";
            requestURL += ("username=" + "lee_yuan_guang@hotmail.com");
            requestURL += ("&SenderAddress="+"Island");//11char max
            requestURL += ("&SenderAddressType="+"5");
            requestURL += ("&password=" + "r0b16");
            requestURL += ("&tel=" + telNo);
            requestURL += ("&msg=" + URLEncoder.encode(smsMessage));

            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("charset", "utf-8");
            connection.connect();
            connection.getInputStream();
            connection.disconnect();
            }
            em.merge(pickRequestEntity);
            return true;
        } catch (Exception ex) {
            System.out.println("callCustomer(): Error");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public StaffEntity receptionistLoginStaff(String email, String password) {
        System.out.println("receptionistLoginStaff() called");
        Long staffID = null;
        try {
            StaffEntity staffEntity = ambl.loginStaff(email, password);
            if (staffEntity == null) {
                return null;
            }
            staffID = staffEntity.getId();
            // Check roles, only store manager or receptionist role can login
            if (ambl.checkIfStaffIsStoreManager(staffID) || ambl.checkIfStaffIsReceptionist(staffID)) {//Store manager or receptionist role
                staffID = staffEntity.getId();
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
                out.println(new Date().toString() + ";" + staffID + ";receptionistLoginStaff();" + staffID + ";");
                out.close();
                return staffEntity;
            }
            return null;
        } catch (Exception ex) {
            System.out.println("receptionistLoginStaff(): error");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PickRequestEntity> getLastCalledPickRequestInStoreForReceptionist(Long storeID) {
        System.out.println("getLastCalledPickRequestInStoreForReceptionist() called");
        try {
            Query q = em.createQuery("SELECT p from PickRequestEntity p WHERE p.store.id=:storeID AND (p.collectionStatus=3 OR p.collectionStatus=4 OR p.collectionStatus=5 OR p.collectionStatus=6) ORDER BY p.collectionStatus ASC");
            q.setParameter("storeID", storeID);
            return q.getResultList();
        } catch (Exception ex) {
            System.out.println("getLastCalledPickRequestInStoreForReceptionist(): error");
            ex.printStackTrace();
            return new ArrayList();
        }
    }
}
