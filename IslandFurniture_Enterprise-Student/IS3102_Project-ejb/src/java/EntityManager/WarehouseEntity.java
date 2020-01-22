package EntityManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@XmlRootElement
public class WarehouseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Lob
    private String warehouseName;
    @Lob
    private String address;
    @Lob
    private String email;
    @Lob
    private String telephone;
    @OneToMany(mappedBy = "warehouse")
    private List<StorageBinEntity> storageBins;
    @OneToOne(mappedBy="warehouse")
    private StoreEntity store;
    @OneToOne(mappedBy="warehouse")
    private ManufacturingFacilityEntity manufaturingFacility;
    @OneToMany(mappedBy="destination")
    private List<PurchaseOrderEntity> purchaseOrders;
    @OneToMany(mappedBy="warehouse")
    private List<TransferOrderEntity> transferOrders;
    @OneToMany(mappedBy="origin")
    private List<ShippingOrderEntity> outbound;
    @OneToMany(mappedBy="destination")
    private List<ShippingOrderEntity> inbound;
    @ManyToOne(cascade = {CascadeType.REMOVE})
    private CountryEntity country;
    @ManyToOne
    private RegionalOfficeEntity regionalOffice;
    private Boolean isDeleted;
    
    public WarehouseEntity(){
        this.storageBins = new ArrayList<>();
        this.purchaseOrders = new ArrayList<>();
        this.isDeleted=false;
    }    

    public WarehouseEntity(String warehouseName, String address, String telephone, String email) {
        this.warehouseName = warehouseName;
        this.address = address;
        this.email = email;
        this.telephone = telephone;
        this.storageBins = new ArrayList<>();
        this.purchaseOrders = new ArrayList<>();
        this.isDeleted = false;
    }                

    @XmlTransient
    public List<ShippingOrderEntity> getOutbound() {
        return outbound;
    }

    public void setOutbound(List<ShippingOrderEntity> outbound) {
        this.outbound = outbound;
    }

    @XmlTransient
    public List<ShippingOrderEntity> getInbound() {
        return inbound;
    }

    public void setInbound(List<ShippingOrderEntity> inbound) {
        this.inbound = inbound;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public String getWarehouseName() {
        return warehouseName;
    }

    public RegionalOfficeEntity getRegionalOffice() {
        return regionalOffice;
    }

    public void setRegionalOffice(RegionalOfficeEntity regionalOffice) {
        this.regionalOffice = regionalOffice;
    }    
    
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }        

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @XmlTransient
    public List<StorageBinEntity> getStorageBins() {
        return storageBins;
    }

    public void setStorageBins(List<StorageBinEntity> storageBins) {
        this.storageBins = storageBins;
    }            
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StoreEntity getStore() {
        return store;
    }

    public void setStore(StoreEntity store) {
        this.store = store;
    }

    public ManufacturingFacilityEntity getManufaturingFacility() {
        return manufaturingFacility;
    }

    public void setManufaturingFacility(ManufacturingFacilityEntity manufaturingFacility) {
        this.manufaturingFacility = manufaturingFacility;
    }        
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WarehouseEntity)) {
            return false;
        }
        WarehouseEntity other = (WarehouseEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityManager.WarehouseEntity[ id=" + id + " ]";
    }

    public CountryEntity getCountry() {
        return country;
    }

    public void setCountry(CountryEntity country) {
        this.country = country;
    }

    @XmlTransient
    public List<PurchaseOrderEntity> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(List<PurchaseOrderEntity> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    @XmlTransient
    public List<TransferOrderEntity> getTransferOrders() {
        return transferOrders;
    }

    public void setTransferOrders(List<TransferOrderEntity> transferOrders) {
        this.transferOrders = transferOrders;
    }


    
}
