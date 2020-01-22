package HelperClasses;

public class ItemCountryHelper {

    private Long id;
    private Double retailPrice;
    private String itemSKU;
    private String itemName;
    private Long countryID;
    private String countryName;

    public ItemCountryHelper() {
    }

    public ItemCountryHelper(Long id, Double retailPrice, String itemSKU, String itemName, Long countryID, String countryName) {
        this.id = id;
        this.retailPrice = retailPrice;
        this.itemSKU = itemSKU;
        this.itemName = itemName;
        this.countryID = countryID;
        this.countryName = countryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getItemSKU() {
        return itemSKU;
    }

    public void setItemSKU(String itemSKU) {
        this.itemSKU = itemSKU;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getCountryID() {
        return countryID;
    }

    public void setCountryID(Long countryID) {
        this.countryID = countryID;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

}
