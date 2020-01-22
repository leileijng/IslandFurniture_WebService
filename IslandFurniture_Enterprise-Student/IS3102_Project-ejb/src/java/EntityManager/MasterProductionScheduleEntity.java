/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntityManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Administrator
 */
@Entity
public class MasterProductionScheduleEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private MonthScheduleEntity schedule;
    @ManyToOne
    private ManufacturingFacilityEntity mf;
    @ManyToOne
    private StoreEntity store;    
    @OneToMany(mappedBy="mps")
    private List<MaterialRequirementEntity> materialRequirementList;
    @OneToOne
    private FurnitureEntity furniture;
    @ManyToOne 
    MenuItemEntity menuItem;
    private Integer amount_month;
    private Integer amount_week1;
    private Integer amount_week2;
    private Integer amount_week3;
    private Integer amount_week4;
    private Integer amount_week5;
    
    public MasterProductionScheduleEntity(){
        this.amount_month = 0;
        this.amount_week1 = 0;
        this.amount_week2 = 0;
        this.amount_week3 = 0;
        this.amount_week4 = 0;
        this.amount_week5 = 0;
        this.materialRequirementList = new ArrayList<>();
    }
        
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StoreEntity getStore() {
        return store;
    }

    public void setStore(StoreEntity store) {
        this.store = store;
    }

    public MenuItemEntity getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItemEntity menuItem) {
        this.menuItem = menuItem;
    }
            
    public List<MaterialRequirementEntity> getMaterialRequirementList() {
        return materialRequirementList;
    }

    public void setMaterialRequirementList(List<MaterialRequirementEntity> materialRequirementList) {
        this.materialRequirementList = materialRequirementList;
    }     

    public Integer getAmount_month() {
        return amount_month;
    }

    public void setAmount_month(Integer amount_month) {
        this.amount_month = amount_month;
    }
        
    public MonthScheduleEntity getSchedule() {
        return schedule;
    }

    public void setSchedule(MonthScheduleEntity schedule) {
        this.schedule = schedule;
    }

    public ManufacturingFacilityEntity getMf() {
        return mf;
    }

    public void setMf(ManufacturingFacilityEntity mf) {
        this.mf = mf;
    }

    public FurnitureEntity getFurniture() {
        return furniture;
    }

    public void setFurniture(FurnitureEntity furniture) {
        this.furniture = furniture;
    }

    public Integer getAmount_week1() {
        return amount_week1;
    }

    public void setAmount_week1(Integer amount_week1) {
        this.amount_week1 = amount_week1;
    }

    public Integer getAmount_week2() {
        return amount_week2;
    }

    public void setAmount_week2(Integer amount_week2) {
        this.amount_week2 = amount_week2;
    }

    public Integer getAmount_week3() {
        return amount_week3;
    }

    public void setAmount_week3(Integer amount_week3) {
        this.amount_week3 = amount_week3;
    }

    public Integer getAmount_week4() {
        return amount_week4;
    }

    public void setAmount_week4(Integer amount_week4) {
        this.amount_week4 = amount_week4;
    }

    public Integer getAmount_week5() {
        return amount_week5;
    }

    public void setAmount_week5(Integer amount_week5) {
        this.amount_week5 = amount_week5;
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
        if (!(object instanceof MasterProductionScheduleEntity)) {
            return false;
        }
        MasterProductionScheduleEntity other = (MasterProductionScheduleEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityManager.PlannedIndependentRequirement[ id=" + id + " ]";
    }
    
}
