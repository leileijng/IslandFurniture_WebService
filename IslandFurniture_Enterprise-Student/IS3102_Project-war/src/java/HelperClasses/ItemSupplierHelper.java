package HelperClasses;

public class ItemSupplierHelper {
    private Long itemID;
    private String SKU;
    private String itemName;
    private Long supplierID;
    private String itemType;
    private Double price;

    public Long getItemID() {
        return itemID;
    }

    public void setItemID(Long itemID) {
        this.itemID = itemID;
    }
    
    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Long supplierID) {
        this.supplierID = supplierID;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    
    public Double getItemPrice() {
        return price;
    }

    public void setItemPrice(Double price) {
        this.price = price;
    }
        
}
