package EntityManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement
public class StoreEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Lob
    private String name;
    @Lob
    private String address;
    @Lob
    private String postalCode;
    @Lob
    private String telephone;
    @Lob
    private String email;
    private String latitude;
    private String longitude;
    @OneToOne
    private WarehouseEntity warehouse;
    @OneToMany(cascade={CascadeType.REMOVE}, mappedBy="store")
    private List<SaleForecastEntity> saleForcastList;    
    @OneToMany(cascade={CascadeType.REMOVE}, mappedBy="store")
    private List<SalesFigureEntity> salesFigureList;    
    @OneToMany(cascade={CascadeType.REMOVE}, mappedBy="store")
    private List<SaleAndOperationPlanEntity> saleAndOperationPlanList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "store")
    private List<Store_ItemEntity> items;   
    @ManyToMany(mappedBy="storeList")
    @JoinTable(name="store_manufacturingFacility")
    private List<ManufacturingFacilityEntity> manufacturingFacilityList;
    @ManyToOne
    private RegionalOfficeEntity regionalOffice;
    @ManyToOne(cascade = {CascadeType.REMOVE})
    private CountryEntity country;
    @OneToMany(mappedBy="store")
    private List<SalesRecordEntity> salesRecords;
    @OneToMany(mappedBy="store")
    private List<PickRequestEntity> pickRequest;
    private String storeMapImageURL;
    private Boolean isDeleted;
    
    public StoreEntity() {}
    public StoreEntity(String name, String address, String telephone, String email, CountryEntity country, String postalCode, String imageURL, String latitude, String longitude){
        this.manufacturingFacilityList = new ArrayList<>();
        this.saleForcastList = new ArrayList<>();
        this.saleAndOperationPlanList = new ArrayList<>();
        this.isDeleted=false;
        this.salesRecords = new ArrayList<>();
        this.setName(name);
        this.setAddress(address);
        this.setTelephone(telephone);
        this.setStoreMapImageURL(imageURL);
        this.setEmail(email);
        this.country = country;
        this.postalCode = postalCode;
        this.pickRequest = new ArrayList<>();
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    @XmlTransient
    public List<SalesRecordEntity> getSalesRecords() {
        return salesRecords;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
            
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public List<Store_ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<Store_ItemEntity> items) {
        this.items = items;
    }

    public List<PickRequestEntity> getPickRequest() {
        return pickRequest;
    }

    public void setPickRequest(List<PickRequestEntity> pickRequest) {
        this.pickRequest = pickRequest;
    }

    public void setSalesRecords(List<SalesRecordEntity> salesRecords) {
        this.salesRecords = salesRecords;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public void setCountry(CountryEntity countryEntity) {
        this.country = countryEntity;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    @XmlTransient
    public List<SaleAndOperationPlanEntity> getSaleAndOperationPlanList() {
        return saleAndOperationPlanList;
    }

    public void setSaleAndOperationPlanList(List<SaleAndOperationPlanEntity> saleAndOperationPlanList) {
        this.saleAndOperationPlanList = saleAndOperationPlanList;
    }        

    public RegionalOfficeEntity getRegionalOffice() {
        return regionalOffice;
    }

    public void setRegionalOffice(RegionalOfficeEntity regionalOffice) {
        this.regionalOffice = regionalOffice;
    }        

    @XmlTransient
    public List<SaleForecastEntity> getSaleForcastList() {
        return saleForcastList;
    }

    public void setSaleForcastList(List<SaleForecastEntity> saleForcastList) {
        this.saleForcastList = saleForcastList;
    }        
    
    @XmlTransient
    public List<ManufacturingFacilityEntity> getManufacturingFacilityList() {
        return manufacturingFacilityList;
    }

    public void setManufacturingFacilityList(List<ManufacturingFacilityEntity> manufacturingFacilityList) {
        this.manufacturingFacilityList = manufacturingFacilityList;
    }        
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public WarehouseEntity getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseEntity warehouse) {
        this.warehouse = warehouse;
    }

    @XmlTransient
    public List<SalesFigureEntity> getSalesFigureList() {
        return salesFigureList;
    }
   
    public void setSalesFigureList(List<SalesFigureEntity> salesFigureList) {
        this.salesFigureList = salesFigureList;
    }

    public String getStoreMapImageURL() {
        return storeMapImageURL;
    }

    public void setStoreMapImageURL(String storeMapImageURL) {
        this.storeMapImageURL = storeMapImageURL;
    }
    
    
}
