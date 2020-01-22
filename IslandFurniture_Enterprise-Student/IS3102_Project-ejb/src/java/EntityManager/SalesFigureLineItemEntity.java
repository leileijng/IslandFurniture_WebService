/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntityManager;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Baoyu
 */
@Entity
public class SalesFigureLineItemEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer saleAmount;
    private String SKU;
    @ManyToOne
    private SalesFigureEntity saleFigure;
    
    public SalesFigureLineItemEntity(){}

    public Integer getQuantity() {
        return saleAmount;
    }

    public void setQuantity(Integer quantity) {
        this.saleAmount = quantity;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public SalesFigureEntity getSaleFigure() {
        return saleFigure;
    }

    public void setSaleFigure(SalesFigureEntity saleFigure) {
        this.saleFigure = saleFigure;
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
        if (!(object instanceof SalesFigureLineItemEntity)) {
            return false;
        }
        SalesFigureLineItemEntity other = (SalesFigureLineItemEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityManager.SalesFigureLineItemEntity[ id=" + id + " ]";
    }
    
}
