/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EntityManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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

/**
 *
 * @author Loi Liang Yang
 */
@Entity
@XmlRootElement
public class ManufacturingFacilityEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Lob
    private String name;
    private Integer capacity;
    @Lob
    private String address;
    @Lob
    private String telephone;
    private String latitude;
    private String longitude;
    @Lob
    private String email;
    @OneToOne
    private WarehouseEntity warehouse;
    @ManyToMany
    @JoinTable(name="store_manufacturingFacility")
    private List<StoreEntity> storeList;
    @ManyToOne
    private RegionalOfficeEntity regionalOffice;
    @OneToMany(mappedBy="manufacturingFacility")
    private List<SaleAndOperationPlanEntity> SaleAndOperationPlanList;
    private Boolean isDeleted;
    
    public ManufacturingFacilityEntity() {
        this.storeList = new ArrayList<>();
        this.SaleAndOperationPlanList = new ArrayList<>();
    }
    
    public void create(String name, String address, String telephone, String email, Integer capacity, String latitude, String longitude) {
        this.setName(name);
        this.setAddress(address);
        this.setTelephone(telephone);
        this.setEmail(email);
        this.setCapacity(capacity);
        this.isDeleted=false;
        this.latitude = latitude;                                
        this.longitude = longitude;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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
    
    public List<SaleAndOperationPlanEntity> getSaleAndOperationPlanList() {
        return SaleAndOperationPlanList;
    }

    public void setSaleAndOperationPlanList(List<SaleAndOperationPlanEntity> SaleAndOperationPlanList) {
        this.SaleAndOperationPlanList = SaleAndOperationPlanList;
    }        
    
    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }        

    public RegionalOfficeEntity getRegionalOffice() {
        return regionalOffice;
    }

    public void setRegionalOffice(RegionalOfficeEntity regionalOffice) {
        this.regionalOffice = regionalOffice;
    }        
    
    @XmlTransient
    public List<StoreEntity> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<StoreEntity> storeList) {
        this.storeList = storeList;
    }        
    
    public WarehouseEntity getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseEntity warehouse) {
        this.warehouse = warehouse;
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
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof ManufacturingFacilityEntity)) {
            return false;
        }
        ManufacturingFacilityEntity other = (ManufacturingFacilityEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entityManagerBean.Factory[ id=" + id + " ]";
    }
    
}
