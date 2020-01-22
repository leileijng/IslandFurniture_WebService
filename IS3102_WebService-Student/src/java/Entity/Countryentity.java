/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jason
 */
@Entity
@Table(name = "countryentity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Countryentity.findAll", query = "SELECT c FROM Countryentity c"),
    @NamedQuery(name = "Countryentity.findById", query = "SELECT c FROM Countryentity c WHERE c.id = :id"),
    @NamedQuery(name = "Countryentity.findByCountrycode", query = "SELECT c FROM Countryentity c WHERE c.countrycode = :countrycode"),
    @NamedQuery(name = "Countryentity.findByExchangerate", query = "SELECT c FROM Countryentity c WHERE c.exchangerate = :exchangerate")})
public class Countryentity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Column(name = "COUNTRYCODE")
    private Integer countrycode;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "CURRENCY")
    private String currency;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "EXCHANGERATE")
    private Double exchangerate;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "NAME")
    private String name;
    @OneToMany(mappedBy = "countryId")
    private List<Storeentity> storeentityList;
    @OneToMany(mappedBy = "countryId")
    private List<Memberentity> memberentityList;
    @OneToMany(mappedBy = "countryId")
    private List<ItemCountryentity> itemCountryentityList;
    @OneToMany(mappedBy = "countryId")
    private List<Warehouseentity> warehouseentityList;

    public Countryentity() {
    }

    public Countryentity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(Integer countrycode) {
        this.countrycode = countrycode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getExchangerate() {
        return exchangerate;
    }

    public void setExchangerate(Double exchangerate) {
        this.exchangerate = exchangerate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<Storeentity> getStoreentityList() {
        return storeentityList;
    }

    public void setStoreentityList(List<Storeentity> storeentityList) {
        this.storeentityList = storeentityList;
    }

    @XmlTransient
    public List<Memberentity> getMemberentityList() {
        return memberentityList;
    }

    public void setMemberentityList(List<Memberentity> memberentityList) {
        this.memberentityList = memberentityList;
    }

    @XmlTransient
    public List<ItemCountryentity> getItemCountryentityList() {
        return itemCountryentityList;
    }

    public void setItemCountryentityList(List<ItemCountryentity> itemCountryentityList) {
        this.itemCountryentityList = itemCountryentityList;
    }

    @XmlTransient
    public List<Warehouseentity> getWarehouseentityList() {
        return warehouseentityList;
    }

    public void setWarehouseentityList(List<Warehouseentity> warehouseentityList) {
        this.warehouseentityList = warehouseentityList;
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
        if (!(object instanceof Countryentity)) {
            return false;
        }
        Countryentity other = (Countryentity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Countryentity[ id=" + id + " ]";
    }
    
}
