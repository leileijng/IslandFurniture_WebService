package StoreTransaction.RetailInventory;

import Config.Config;
import javax.ejb.Stateless;
import EntityManager.ItemEntity;
import EntityManager.RawMaterialEntity;
import EntityManager.RetailProductEntity;
import EntityManager.FurnitureEntity;
import EntityManager.Item_CountryEntity;
import EntityManager.LineItemEntity;
import EntityManager.SalesRecordEntity;
import EntityManager.StoreEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class RetailInventoryBean implements RetailInventoryBeanLocal {

    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;

    @Override
    public StoreEntity getStoreByID(Long storeID) {
        System.out.println("getStoreByID() called.");
        try {
            Query q = em.createQuery("select s from StoreEntity s where s.isDeleted=false and s.id = ?1").setParameter(1, storeID);
            System.out.println("getStoreByID(): Store returned.");
            return (StoreEntity) q.getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("getStoreByID(): No such store found.");
            return null;
        } catch (Exception ex) {
            System.out.println("getStoreByID(): Failed to getStoreByID()");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public RawMaterialEntity viewRawMaterial(String SKU) {
        System.out.println("viewRawMaterial() called with SKU:" + SKU);
        try {
            Query q = em.createQuery("SELECT t FROM RawMaterialEntity t");

            for (Object o : q.getResultList()) {
                RawMaterialEntity i = (RawMaterialEntity) o;
                if (i.getSKU().equalsIgnoreCase(SKU) && i.getIsDeleted() == false) {
                    System.out.println("\nServer returns raw material:\n" + SKU);
                    return i;
                }
            }
            return null; //Could not find
        } catch (Exception ex) {
            System.out.println("\nServer failed to view raw material:\n" + ex);
            return null;
        }
    }

    @Override
    public FurnitureEntity viewFurniture(String SKU) {
        System.out.println("viewRawMaterial() called with SKU:" + SKU);
        try {
            Query q = em.createQuery("SELECT t FROM FurnitureEntity t where t.isDeleted=false");

            for (Object o : q.getResultList()) {
                FurnitureEntity i = (FurnitureEntity) o;
                if (i.getSKU().equalsIgnoreCase(SKU) && i.getIsDeleted() == false) {
                    System.out.println("\nServer returns furniture:\n" + SKU);
                    return i;
                }
            }
            return null; //Could not find
        } catch (Exception ex) {
            System.out.println("\nServer failed to view furniture:\n" + ex);
            return null;
        }
    }

    @Override
    public RetailProductEntity viewRetailProduct(String SKU) {
        System.out.println("viewRetailProduct() called with SKU:" + SKU);
        try {
            Query q = em.createQuery("SELECT t FROM RetailProductEntity t where t.isDeleted=false");

            for (Object o : q.getResultList()) {
                RetailProductEntity i = (RetailProductEntity) o;
                if (i.getSKU().equalsIgnoreCase(SKU)) {
                    System.out.println("\nServer returns retail product:\n" + SKU);
                    return i;
                }
            }
            return null; //Could not find
        } catch (Exception ex) {
            System.out.println("\nServer failed to view retail product:\n" + ex);
            return null;
        }
    }

    @Override
    public List<RawMaterialEntity> listAllRawMaterials() {
        System.out.println("listAllRawMaterials() called.");
        try {
            Query q = em.createQuery("SELECT t FROM RawMaterialEntity t where t.isDeleted=false");
            List<RawMaterialEntity> rawMaterialEntity = q.getResultList();
            return rawMaterialEntity;
        } catch (Exception ex) {
            System.out.println("\nServer failed to list all raw materials:\n" + ex);
            return null;
        }
    }

    @Override
    public List<FurnitureEntity> listAllFurniture() {
        System.out.println("listAllFurniture() called.");
        try {
            Query q = em.createQuery("SELECT t FROM FurnitureEntity t where t.isDeleted=false");
            List<FurnitureEntity> furnitureEntity = q.getResultList();
            return furnitureEntity;
        } catch (Exception ex) {
            System.out.println("\nServer failed to list all furniture:\n" + ex);
            return null;
        }
    }

    @Override
    public List<RetailProductEntity> listAllRetailProduct() {
        System.out.println("listAllRetailProduct() called.");
        try {
            Query q = em.createQuery("SELECT t FROM RetailProductEntity t where t.isDeleted=false");
            List<RetailProductEntity> retailProductEntity = q.getResultList();
            return retailProductEntity;
        } catch (Exception ex) {
            System.out.println("\nServer failed to list all retail product:\n" + ex);
            return null;
        }
    }

    @Override
    public ItemEntity getItemBySKU(String SKU) {
        System.out.println("getItemBySKU() called with SKU: " + SKU);
        try {
            Query q = em.createQuery("Select i from ItemEntity i where i.SKU=:SKU and i.isDeleted=false");
            q.setParameter("SKU", SKU);
            ItemEntity item = (ItemEntity) q.getSingleResult();
            System.out.println("getItemBySKU() is successful.");
            return item;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getItemBySKU():\n" + ex);
            return null;
        }
    }

    @Override
    public Item_CountryEntity getItemPricing(Long countryId, String SKU) {
        System.out.println("getItemPricing() called.");
        try {
            Query q = em.createQuery("Select i from Item_CountryEntity i where i.isDeleted=false and i.country.id=:countryId and i.item.SKU=:SKU");
            q.setParameter("countryId", countryId);
            q.setParameter("SKU", SKU);
            Item_CountryEntity itemCountry = (Item_CountryEntity) q.getSingleResult();
            System.out.println("getItemPricing(): Successful.");
            return itemCountry;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("getItemPricing(): Failed to retrieve item pricing.");
            return null;
        }
    }

    @Override
    public boolean checkSKUExists(String SKU) {
        try {
            Query q = em.createQuery("Select i from ItemEntity i where i.SKU=:SKU and i.isDeleted=false");
            q.setParameter("SKU", SKU);
            q.getSingleResult();
            return true;
        } catch (NoResultException n) {
            System.out.println("\nServer return no result:\n" + n);
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer failed to perform checkSKUExists:\n" + ex);
            return false;
        }
    }

    @Override
    public Boolean checkIfCustomerNeedToWaitForPicker(String receiptNo) {
        System.out.println("checkIfCustomerNeedToWaitForPicker() called");
        try {
            Query q = em.createQuery("Select i from SalesRecordEntity i where i.receiptNo=:receiptNo");
            q.setParameter("receiptNo", receiptNo);
            SalesRecordEntity salesRecordEntity = (SalesRecordEntity) q.getSingleResult();
            List<LineItemEntity> lineItemEntities = salesRecordEntity.getItemsPurchased();
            for (LineItemEntity curr:lineItemEntities) {
                if (curr.getItem().getVolume()>Config.minVolumeForCollectionAreaItems) {
                    System.out.println("checkIfCustomerNeedToWaitForPicker(): Yes");
                    return true;
                }
            }
            System.out.println("checkIfCustomerNeedToWaitForPicker(): No");
            return false;
        } catch (Exception ex) {
            System.out.println("checkIfCustomerNeedToWaitForPicker(): Error");
            ex.printStackTrace();
            return null;
        }
    }
}
