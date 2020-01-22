package EntityManager;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class SupplierEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Lob
    private String supplierName;
    @Lob
    private String contactNo;
    @Lob
    private String email;
    @Lob
    private String address;
    private Boolean isDeleted;
    @ManyToOne(cascade = {CascadeType.REMOVE})
    private CountryEntity country;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy="supplier")
    private List<Supplier_ItemEntity> supplyingItems;
   
    @OneToMany(mappedBy="supplier")
    private List<PurchaseOrderEntity> purchaseOrders;
    
    @ManyToOne
    private RegionalOfficeEntity regionalOffice;
            
    public SupplierEntity() {

    }

    public SupplierEntity(String supplierName, String contactNo, String email, String address, RegionalOfficeEntity regionalOffice) {
        this.supplierName = supplierName;
        this.contactNo = contactNo;
        this.email = email;
        this.address = address;
        this.isDeleted = false;
        this.regionalOffice = regionalOffice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegionalOfficeEntity getRegionalOffice() {
        return regionalOffice;
    }

    public void setRegionalOffice(RegionalOfficeEntity regionalOffice) {
        this.regionalOffice = regionalOffice;
    }

    public List<PurchaseOrderEntity> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(List<PurchaseOrderEntity> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }


    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }



    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public void setCountry(CountryEntity country) {
        this.country = country;
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
        if (!(object instanceof SupplierEntity)) {
            return false;
        }
        SupplierEntity other = (SupplierEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public List<Supplier_ItemEntity> getSupplyingItems() {
        return supplyingItems;
    }

    public void setSupplyingItems(List<Supplier_ItemEntity> supplyingItems) {
        this.supplyingItems = supplyingItems;
    }

    @Override
    public String toString() {
        return "EntityManager.SupplierEntity[ id=" + id + " ]";
    }
}
