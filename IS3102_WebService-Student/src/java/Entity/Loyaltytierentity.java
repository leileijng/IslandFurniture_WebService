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
@Table(name = "loyaltytierentity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Loyaltytierentity.findAll", query = "SELECT l FROM Loyaltytierentity l"),
    @NamedQuery(name = "Loyaltytierentity.findById", query = "SELECT l FROM Loyaltytierentity l WHERE l.id = :id"),
    @NamedQuery(name = "Loyaltytierentity.findByAmtofspendingrequired", query = "SELECT l FROM Loyaltytierentity l WHERE l.amtofspendingrequired = :amtofspendingrequired"),
    @NamedQuery(name = "Loyaltytierentity.findByIsdeleted", query = "SELECT l FROM Loyaltytierentity l WHERE l.isdeleted = :isdeleted"),
    @NamedQuery(name = "Loyaltytierentity.findByTier", query = "SELECT l FROM Loyaltytierentity l WHERE l.tier = :tier")})
public class Loyaltytierentity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "AMTOFSPENDINGREQUIRED")
    private Double amtofspendingrequired;
    @Column(name = "ISDELETED")
    private Boolean isdeleted;
    @Size(max = 255)
    @Column(name = "TIER")
    private String tier;
    @OneToMany(mappedBy = "loyaltytierId")
    private List<Memberentity> memberentityList;

    public Loyaltytierentity() {
    }

    public Loyaltytierentity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmtofspendingrequired() {
        return amtofspendingrequired;
    }

    public void setAmtofspendingrequired(Double amtofspendingrequired) {
        this.amtofspendingrequired = amtofspendingrequired;
    }

    public Boolean getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    @XmlTransient
    public List<Memberentity> getMemberentityList() {
        return memberentityList;
    }

    public void setMemberentityList(List<Memberentity> memberentityList) {
        this.memberentityList = memberentityList;
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
        if (!(object instanceof Loyaltytierentity)) {
            return false;
        }
        Loyaltytierentity other = (Loyaltytierentity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Loyaltytierentity[ id=" + id + " ]";
    }
    
}
