/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "lineitementity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lineitementity.findAll", query = "SELECT l FROM Lineitementity l"),
    @NamedQuery(name = "Lineitementity.findById", query = "SELECT l FROM Lineitementity l WHERE l.id = :id"),
    @NamedQuery(name = "Lineitementity.findByPacktype", query = "SELECT l FROM Lineitementity l WHERE l.packtype = :packtype"),
    @NamedQuery(name = "Lineitementity.findByQuantity", query = "SELECT l FROM Lineitementity l WHERE l.quantity = :quantity")})
public class Lineitementity implements Serializable {
    @ManyToMany(mappedBy = "lineitementityList")
    private List<Memberentity> memberentityList;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @Size(max = 255)
    @Column(name = "PACKTYPE")
    private String packtype;
    @Column(name = "QUANTITY")
    private Integer quantity;
    @JoinColumn(name = "ITEM_ID", referencedColumnName = "ID")
    @ManyToOne
    private Itementity itemId;

    public Lineitementity() {
    }

    public Lineitementity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPacktype() {
        return packtype;
    }

    public void setPacktype(String packtype) {
        this.packtype = packtype;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
        if (!(object instanceof Lineitementity)) {
            return false;
        }
        Lineitementity other = (Lineitementity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Lineitementity[ id=" + id + " ]";
    }


    @XmlTransient
    public List<Memberentity> getMemberentityList() {
        return memberentityList;
    }

    public void setMemberentityList(List<Memberentity> memberentityList) {
        this.memberentityList = memberentityList;
    }

}
