/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement
public class RegionalOfficeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Lob
    private String name;
    @Lob
    private String address;
    @Lob
    private String telephone;
    @Lob
    private String email;
    @OneToMany(cascade = {CascadeType.REMOVE}, mappedBy = "regionalOffice")
    private List<WarehouseEntity> warehouseList;
    @OneToMany(cascade = {CascadeType.REMOVE}, mappedBy = "regionalOffice")
    private List<StoreEntity> storeList;
    @OneToMany(cascade = {CascadeType.REMOVE}, mappedBy = "regionalOffice")
    private List<ManufacturingFacilityEntity> manufacturingFacilityList;
    @OneToMany(cascade = {CascadeType.REMOVE}, mappedBy = "regionalOffice")
    private List<SupplierEntity> suppliers;
    private Boolean isDeleted;

    public RegionalOfficeEntity() {
        this.storeList = new ArrayList<>();
        this.manufacturingFacilityList = new ArrayList<>();
        this.warehouseList = new ArrayList<>();
        this.isDeleted = false;
    }

    public void create(String name, String address, String telephone, String email) {
        this.setName(name);
        this.setAddress(address);
        this.setTelephone(telephone);
        this.setEmail(email);
        this.isDeleted = false;
    }

    @XmlTransient
    public List<SupplierEntity> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<SupplierEntity> suppliers) {
        this.suppliers = suppliers;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @XmlTransient
    public List<WarehouseEntity> getWarehouseList() {
        return warehouseList;
    }

    public void setWarehouseList(List<WarehouseEntity> warehouseList) {
        this.warehouseList = warehouseList;
    }

    @XmlTransient
    public List<StoreEntity> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<StoreEntity> storeList) {
        this.storeList = storeList;
    }

    @XmlTransient
    public List<ManufacturingFacilityEntity> getManufacturingFacilityList() {
        return manufacturingFacilityList;
    }

    public void setManufacturingFacilityList(List<ManufacturingFacilityEntity> manufacturingFacilityList) {
        this.manufacturingFacilityList = manufacturingFacilityList;
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
        if (!(object instanceof RegionalOfficeEntity)) {
            return false;
        }
        RegionalOfficeEntity other = (RegionalOfficeEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityManager.RegionalOfficeEntity[ id=" + id + " ]";
    }

}
