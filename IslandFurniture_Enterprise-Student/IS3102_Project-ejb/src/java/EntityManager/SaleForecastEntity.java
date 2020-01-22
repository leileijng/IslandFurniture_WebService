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
 * @author Administrator
 */
@Entity
public class SaleForecastEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;            
    @ManyToOne
    private StoreEntity store;
    @ManyToOne
    private ProductGroupEntity productGroup;
    @ManyToOne
    private MonthScheduleEntity schedule;
    @ManyToOne
    private MenuItemEntity menuItem;
    private String method;
    private Integer quantity;
    
    public SaleForecastEntity(){}    

    public SaleForecastEntity(StoreEntity store, ProductGroupEntity productGroup, MonthScheduleEntity schedule, Integer quantity) {
        this.store = store;
        this.productGroup = productGroup;
        this.schedule = schedule;
        this.quantity = quantity;
    }            
    
    public SaleForecastEntity(StoreEntity store, MenuItemEntity menuItem, MonthScheduleEntity schedule, Integer quantity) {
        this.store = store;
        this.menuItem = menuItem;
        this.schedule = schedule;
        this.quantity = quantity;
    }

    public MonthScheduleEntity getSchedule() {
        return schedule;
    }

    public void setSchedule(MonthScheduleEntity schedule) {
        this.schedule = schedule;
    }    

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public StoreEntity getStore() {
        return store;
    }

    public MenuItemEntity getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItemEntity menuItem) {
        this.menuItem = menuItem;
    }       

    public void setStore(StoreEntity store) {
        this.store = store;
    }

    public ProductGroupEntity getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroupEntity productGroup) {
        this.productGroup = productGroup;
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
        if (!(object instanceof SaleForecastEntity)) {
            return false;
        }
        SaleForecastEntity other = (SaleForecastEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityManager.SaleForcastEntity[ id=" + id + " ]";
    }
    
}
