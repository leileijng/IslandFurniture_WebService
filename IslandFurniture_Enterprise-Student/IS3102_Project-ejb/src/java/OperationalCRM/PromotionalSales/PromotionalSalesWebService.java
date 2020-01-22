package OperationalCRM.PromotionalSales;

import EntityManager.PromotionEntity;
import EntityManager.StoreEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@WebService(serviceName = "PromotionalSalesWebService")
@Stateless()
public class PromotionalSalesWebService {

    @EJB
    PromotionalSalesBeanLocal psbl;

    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;

    @WebMethod
    public Double getPromotionRate(@WebParam(name = "SKU") String sku, @WebParam(name = "storeID") Long storeID, @WebParam(name = "date") Date date) {
        System.out.println("getPromotionRate(): Called");
        try {
            StoreEntity storeEntity = em.getReference(StoreEntity.class, storeID);
            Long countryId = storeEntity.getCountry().getId();
            return psbl.getPromotionRate(sku, countryId, date);
        } catch (Exception ex) {
            System.out.println("getPromotionRate(): Error");
            ex.printStackTrace();
            return 0.0;
        }
    }
    
    @WebMethod
    public List<String> getActivePromotionInCountry(@WebParam(name="storeID") Long storeID) {
        System.out.println("getActivePromotionInCountry() called");
        try {
            StoreEntity storeEntity = em.getReference(StoreEntity.class, storeID);
            List<PromotionEntity> promotions = psbl.getAllActivePromotionsInCountry(storeEntity.getCountry().getId());
            List<String> promotionsImageURL = new ArrayList<>();
            for(PromotionEntity curr:promotions){
                promotionsImageURL.add(curr.getImageURL());
            }
            return promotionsImageURL;
        } catch (Exception ex) {
            System.out.println("getActivePromotionInCountry(): error");
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
}
