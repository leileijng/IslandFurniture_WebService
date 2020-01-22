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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "itementity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Itementity.findAll", query = "SELECT i FROM Itementity i"),
    @NamedQuery(name = "Itementity.findById", query = "SELECT i FROM Itementity i WHERE i.id = :id"),
    @NamedQuery(name = "Itementity.findByDtype", query = "SELECT i FROM Itementity i WHERE i.dtype = :dtype"),
    @NamedQuery(name = "Itementity.findByLength", query = "SELECT i FROM Itementity i WHERE i.length = :length"),
    @NamedQuery(name = "Itementity.findByCategory", query = "SELECT i FROM Itementity i WHERE i.category = :category"),
    @NamedQuery(name = "Itementity.findByDescription", query = "SELECT i FROM Itementity i WHERE i.description = :description"),
    @NamedQuery(name = "Itementity.findByHeight", query = "SELECT i FROM Itementity i WHERE i.height = :height"),
    @NamedQuery(name = "Itementity.findByIsdeleted", query = "SELECT i FROM Itementity i WHERE i.isdeleted = :isdeleted"),
    @NamedQuery(name = "Itementity.findByVolume", query = "SELECT i FROM Itementity i WHERE i.volume = :volume"),
    @NamedQuery(name = "Itementity.findByWidth", query = "SELECT i FROM Itementity i WHERE i.width = :width")})
public class Itementity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Size(max = 31)
    @Column(name = "DTYPE")
    private String dtype;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "SKU")
    private String sku;
    @Column(name = "_LENGTH")
    private Integer length;
    @Size(max = 255)
    @Column(name = "CATEGORY")
    private String category;
    @Size(max = 255)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "HEIGHT")
    private Integer height;
    @Column(name = "ISDELETED")
    private Boolean isdeleted;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "NAME")
    private String name;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "TYPE")
    private String type;
    @Column(name = "VOLUME")
    private Integer volume;
    @Column(name = "WIDTH")
    private Integer width;
    @ManyToMany(mappedBy = "itementityList")
    private List<Wishlistentity> wishlistentityList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "itementity")
    private Retailproductentity retailproductentity;
    @JoinColumn(name = "WAREHOUSES_ID", referencedColumnName = "ID")
    @ManyToOne
    private Warehouseentity warehousesId;
    @OneToMany(mappedBy = "itemId")
    private List<Lineitementity> lineitementityList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "itementity")
    private Furnitureentity furnitureentity;
    @OneToMany(mappedBy = "itemId")
    private List<ItemCountryentity> itemCountryentityList;

    public Itementity() {
    }

    public Itementity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @XmlTransient
    public List<Wishlistentity> getWishlistentityList() {
        return wishlistentityList;
    }

    public void setWishlistentityList(List<Wishlistentity> wishlistentityList) {
        this.wishlistentityList = wishlistentityList;
    }

    public Retailproductentity getRetailproductentity() {
        return retailproductentity;
    }

    public void setRetailproductentity(Retailproductentity retailproductentity) {
        this.retailproductentity = retailproductentity;
    }

    public Warehouseentity getWarehousesId() {
        return warehousesId;
    }

    public void setWarehousesId(Warehouseentity warehousesId) {
        this.warehousesId = warehousesId;
    }

    @XmlTransient
    public List<Lineitementity> getLineitementityList() {
        return lineitementityList;
    }

    public void setLineitementityList(List<Lineitementity> lineitementityList) {
        this.lineitementityList = lineitementityList;
    }

    public Furnitureentity getFurnitureentity() {
        return furnitureentity;
    }

    public void setFurnitureentity(Furnitureentity furnitureentity) {
        this.furnitureentity = furnitureentity;
    }

    @XmlTransient
    public List<ItemCountryentity> getItemCountryentityList() {
        return itemCountryentityList;
    }

    public void setItemCountryentityList(List<ItemCountryentity> itemCountryentityList) {
        this.itemCountryentityList = itemCountryentityList;
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
        if (!(object instanceof Itementity)) {
            return false;
        }
        Itementity other = (Itementity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Itementity[ id=" + id + " ]";
    }
    
}
