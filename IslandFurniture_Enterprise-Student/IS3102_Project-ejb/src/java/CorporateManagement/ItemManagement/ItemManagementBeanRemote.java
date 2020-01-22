package CorporateManagement.ItemManagement;

import EntityManager.FurnitureEntity;
import EntityManager.ItemEntity;
import EntityManager.BillOfMaterialEntity;
import EntityManager.CountryEntity;
import EntityManager.Item_CountryEntity;
import EntityManager.RawMaterialEntity;
import EntityManager.ProductGroupEntity;
import EntityManager.ProductGroupLineItemEntity;
import EntityManager.RetailProductEntity;
import EntityManager.Supplier_ItemEntity;
import HelperClasses.ReturnHelper;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Remove;

@Remote
public interface ItemManagementBeanRemote {

    public ItemEntity getItemBySKU(String SKU);
    public boolean checkSKUExists(String SKU);
    public boolean addRawMaterial(String SKU, String name, String category, String description, Integer _length, Integer width, Integer height);
    public boolean editRawMaterial(String id, String SKU, String name, String category, String description);
    public boolean removeRawMaterial(String SKU);
    public RawMaterialEntity viewRawMaterial(String SKU);
    public List<RawMaterialEntity> listAllRawMaterials();

    public boolean addFurniture(String SKU, String name, String category, String description, String imageURL, Integer _length, Integer width, Integer height);
    public boolean editFurniture(String id, String SKU, String name, String category, String description, String imageURL);
    public boolean removeFurniture(String SKU);
    public FurnitureEntity viewFurniture(String SKU);
    public List<FurnitureEntity> listAllFurniture();
    
    public boolean addRetailProduct(String SKU, String name, String category, String description, String imageURL, Integer _length, Integer width, Integer height);
    public boolean editRetailProduct(String id, String SKU, String name, String category, String description, String imageURL);
    public boolean removeRetailProduct(String SKU);
    public RetailProductEntity viewRetailProduct(String SKU);
    public List<RetailProductEntity> listAllRetailProduct();
   
    public boolean createBOM(String name, String description, Integer workHour);
    public boolean editBOM(Long BOMId, String name, String description);
    public boolean deleteBOM(Long BOMId);
    public BillOfMaterialEntity viewSingleBOM(Long BOMId);
    public List<BillOfMaterialEntity> listAllBOM();
    
    public boolean addLineItemToBOM(String SKU, Integer qty, Long BOMId);
    public boolean deleteLineItemFromBOM(Long lineItemId, Long BOMId);
    public boolean linkBOMAndFurniture(Long BOMId, Long FurnitureId);
    public List<FurnitureEntity> listAllFurnitureWithoutBOM();
    /*
     public boolean addItem(String name, String materialID, String description, String imageURL);
     public boolean editItem(String name, String materialID, String description, String imageURL);
     public boolean removeItem(String itemName);
     public ItemEntity viewItem(String itemName);
     */
    
    public ProductGroupEntity createProductGroup(String name, Integer workhours, Integer lotSize);
    public Boolean editProductGroup(Long productGroupID, String name, Integer workhours, Integer lotSize);
    public ProductGroupEntity getProductGroup(Long id);
    public List<ProductGroupEntity> getAllProductGroup();    
    public ProductGroupLineItemEntity createProductGroupLineItem(String furnitureSKU, double percent);    
    public Boolean editProductGroupLineItem(Long productGroupID, Long productGroupLineItemID, String SKU, double percent);
    public Boolean addLineItemToProductGroup(Long productGroupId, Long lineItemId);
    public Boolean removeLineItemFromProductGroup(Long productGroupId, Long lineItemId);
    public boolean removeProductGroup(Long productGroupID);
    public Boolean checkIfSKUIsFurniture(String SKU);
    
    public ReturnHelper addCountryItemPricing(Long countryId, String SKU, double retailPrice);
    public ReturnHelper removeCountryItemPricing(Long countryItemId);
    public ReturnHelper editCountryItemPricing(Long countryItemId, double retailPrice);
    public Item_CountryEntity getCountryItemPricing(Long countryItemId);
    public List<Item_CountryEntity> listAllCountryItemPricing();
    public List<CountryEntity> listAllCountry();
    public List<String> listAllItemsSKU();
    public List<String> listAllItemsSKUForSupplier();
    
    public List<Item_CountryEntity> listAllItemsOfCountry(Long countryId);
    public Item_CountryEntity getItemPricing(Long countryId, String SKU);
    
    
    public ReturnHelper addSupplierItemInfo(String SKU, Long supplierId, Double costPrice, Integer lotSize, Integer leadTime);
    public ReturnHelper removeSupplierItemInfo(Long supplierItemId);
    public ReturnHelper editSupplierItemInfo(Long supplierItemId, Double costPrice, Integer lotSize, Integer leadTime);
    public Supplier_ItemEntity getSupplierItemInfo(Long supplierItemId);
    public List<Supplier_ItemEntity> listAllSupplierItemInfo();

    @Remove
    public void remove();
}
