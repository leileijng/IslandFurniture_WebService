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
@Table(name = "regionalofficeentity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Regionalofficeentity.findAll", query = "SELECT r FROM Regionalofficeentity r"),
    @NamedQuery(name = "Regionalofficeentity.findById", query = "SELECT r FROM Regionalofficeentity r WHERE r.id = :id"),
    @NamedQuery(name = "Regionalofficeentity.findByIsdeleted", query = "SELECT r FROM Regionalofficeentity r WHERE r.isdeleted = :isdeleted")})
public class Regionalofficeentity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "ADDRESS")
    private String address;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Lob
    @Size(max = 2147483647)
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "ISDELETED")
    private Boolean isdeleted;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "NAME")
    private String name;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "TELEPHONE")
    private String telephone;
    @OneToMany(mappedBy = "regionalofficeId")
    private List<Storeentity> storeentityList;
    @OneToMany(mappedBy = "regionalofficeId")
    private List<Warehouseentity> warehouseentityList;

    public Regionalofficeentity() {
    }

    public Regionalofficeentity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @XmlTransient
    public List<Storeentity> getStoreentityList() {
        return storeentityList;
    }

    public void setStoreentityList(List<Storeentity> storeentityList) {
        this.storeentityList = storeentityList;
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
        if (!(object instanceof Regionalofficeentity)) {
            return false;
        }
        Regionalofficeentity other = (Regionalofficeentity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Regionalofficeentity[ id=" + id + " ]";
    }
    
}
