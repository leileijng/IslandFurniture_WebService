package StoreTransaction.RetailInventory;

import EntityManager.FurnitureEntity;
import EntityManager.ItemEntity;
import EntityManager.Item_CountryEntity;
import EntityManager.RawMaterialEntity;
import EntityManager.RetailProductEntity;
import EntityManager.StoreEntity;
import java.util.List;
import javax.ejb.Local;
import javax.jws.WebParam;

@Local
public interface RetailInventoryBeanLocal {

    public StoreEntity getStoreByID(Long storeID);
    public ItemEntity getItemBySKU(String SKU);
    public Item_CountryEntity getItemPricing(Long countryId, String SKU);
    public boolean checkSKUExists(String SKU);
    public RawMaterialEntity viewRawMaterial(String SKU);
    public List<RawMaterialEntity> listAllRawMaterials();

    public FurnitureEntity viewFurniture(String SKU);
    public List<FurnitureEntity> listAllFurniture();
    
    public RetailProductEntity viewRetailProduct(String SKU);
    public List<RetailProductEntity> listAllRetailProduct();
    
    public Boolean checkIfCustomerNeedToWaitForPicker(String receiptNo);
}
