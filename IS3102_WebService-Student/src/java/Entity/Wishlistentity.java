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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jason
 */
@Entity
@Table(name = "wishlistentity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Wishlistentity.findAll", query = "SELECT w FROM Wishlistentity w"),
    @NamedQuery(name = "Wishlistentity.findById", query = "SELECT w FROM Wishlistentity w WHERE w.id = :id")})
public class Wishlistentity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @JoinTable(name = "wishlistentity_itementity", joinColumns = {
        @JoinColumn(name = "WishListEntity_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "items_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<Itementity> itementityList;
    @OneToMany(mappedBy = "wishlistId")
    private List<Memberentity> memberentityList;

    public Wishlistentity() {
    }

    public Wishlistentity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlTransient
    public List<Itementity> getItementityList() {
        return itementityList;
    }

    public void setItementityList(List<Itementity> itementityList) {
        this.itementityList = itementityList;
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
        if (!(object instanceof Wishlistentity)) {
            return false;
        }
        Wishlistentity other = (Wishlistentity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Wishlistentity[ id=" + id + " ]";
    }
    
}
