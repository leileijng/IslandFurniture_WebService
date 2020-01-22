/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jason
 */
@Entity
@Table(name = "item_countryentity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemCountryentity.findAll", query = "SELECT i FROM ItemCountryentity i"),
    @NamedQuery(name = "ItemCountryentity.findById", query = "SELECT i FROM ItemCountryentity i WHERE i.id = :id"),
    @NamedQuery(name = "ItemCountryentity.findByIsdeleted", query = "SELECT i FROM ItemCountryentity i WHERE i.isdeleted = :isdeleted"),
    @NamedQuery(name = "ItemCountryentity.findByRetailprice", query = "SELECT i FROM ItemCountryentity i WHERE i.retailprice = :retailprice")})
public class ItemCountryentity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Column(name = "ISDELETED")
    private Boolean isdeleted;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "RETAILPRICE")
    private Double retailprice;
    @JoinColumn(name = "COUNTRY_ID", referencedColumnName = "ID")
    @ManyToOne
    private Countryentity countryId;
    @JoinColumn(name = "ITEM_ID", referencedColumnName = "ID")
    @ManyToOne
    private Itementity itemId;

    public ItemCountryentity() {
    }

    public ItemCountryentity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public Double getRetailprice() {
        return retailprice;
    }

    public void setRetailprice(Double retailprice) {
        this.retailprice = retailprice;
    }

    public Countryentity getCountryId() {
        return countryId;
    }

    public void setCountryId(Countryentity countryId) {
        this.countryId = countryId;
    }

    public Itementity getItemId() {
        return itemId;
    }

    public void setItemId(Itementity itemId) {
        this.itemId = itemId;
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
        if (!(object instanceof ItemCountryentity)) {
            return false;
        }
        ItemCountryentity other = (ItemCountryentity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.ItemCountryentity[ id=" + id + " ]";
    }
    
}
