/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntityManager;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Administrator
 */
@Entity
public class SaleAndOperationPlanEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;    
    @OneToOne
    private SaleForecastEntity saleForcast;
    @ManyToOne
    private StoreEntity store;
    @ManyToOne
    private ManufacturingFacilityEntity manufacturingFacility;
    @ManyToOne
    private ProductGroupEntity productGroup;
    @ManyToOne
    private MonthScheduleEntity schedule;
            
    private int year;
    private int month;
    private Integer saleForcastdata;
    private Integer productionPlan;
    private Integer currentInventoryLevel;
    private Integer targetInventoryLevel;
    
    public SaleAndOperationPlanEntity(){}

    public SaleAndOperationPlanEntity(SaleForecastEntity saleForcast, StoreEntity store, Integer productionPlan, Integer currentInventoryLevel, Integer targetInventoryLevel, MonthScheduleEntity schedule) {
        this.saleForcast = saleForcast;
        this.schedule = schedule;
        this.store = store;        
        this.year = schedule.getYear();
        this.month = schedule.getMonth();
        this.productionPlan = productionPlan;
        this.currentInventoryLevel = currentInventoryLevel;
        this.targetInventoryLevel = targetInventoryLevel;
    }        

    public SaleAndOperationPlanEntity(StoreEntity store, ProductGroupEntity productGroup, MonthScheduleEntity schedule, Integer saleForcastdata, Integer productionPlan, Integer currentInventoryLevel, Integer targetInventoryLevel) {
        this.store = store;
        this.productGroup = productGroup;
        this.schedule = schedule;
        this.year = schedule.getYear();
        this.month = schedule.getMonth();
        this.saleForcastdata = saleForcastdata;
        this.productionPlan = productionPlan;
        this.currentInventoryLevel = currentInventoryLevel;
        this.targetInventoryLevel = targetInventoryLevel;
    }       

    public Integer getSaleForcastdata() {
        return saleForcastdata;
    }

    public void setSaleForcastdata(Integer saleForcastdata) {
        this.saleForcastdata = saleForcastdata;
    }    
    
    public MonthScheduleEntity getSchedule() {
        return schedule;
    }

    public void setSchedule(MonthScheduleEntity schedule) {
        this.schedule = schedule;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
        
    public ProductGroupEntity getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroupEntity productGroup) {
        this.productGroup = productGroup;
    }        
    
    public ManufacturingFacilityEntity getManufacturingFacility() {
        return manufacturingFacility;
    }

    public void setManufacturingFacility(ManufacturingFacilityEntity manufacturingFacility) {
        this.manufacturingFacility = manufacturingFacility;
    }        
    
    public StoreEntity getStore() {
        return store;
    }

    public void setStore(StoreEntity store) {
        this.store = store;
    }        
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public int getYear(){
        return this.year;
    }

    public SaleForecastEntity getSaleForcast() {
        return saleForcast;
    }

    public void setSaleForcast(SaleForecastEntity saleForcast) {
        this.saleForcast = saleForcast;
    }

    public Integer getProductionPlan() {
        return productionPlan;
    }

    public void setProductionPlan(Integer productionPlan) {
        this.productionPlan = productionPlan;
    }

    public Integer getCurrentInventoryLevel() {
        return currentInventoryLevel;
    }

    public void setCurrentInventoryLevel(Integer currentInventoryLevel) {
        this.currentInventoryLevel = currentInventoryLevel;
    }

    public Integer getTargetInventoryLevel() {
        return targetInventoryLevel;
    }

    public void setTargetInventoryLevel(Integer targetInventoryLevel) {
        this.targetInventoryLevel = targetInventoryLevel;
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
        if (!(object instanceof SaleAndOperationPlanEntity)) {
            return false;
        }
        SaleAndOperationPlanEntity other = (SaleAndOperationPlanEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityManager.SaleAndOperationPlanEntity[ id=" + id + " ]";
    }
    
}
