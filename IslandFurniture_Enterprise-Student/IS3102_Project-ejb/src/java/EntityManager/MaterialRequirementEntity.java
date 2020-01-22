/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntityManager;

import java.io.Serializable;
import javax.persistence.CascadeType;
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
public class MaterialRequirementEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private ManufacturingFacilityEntity mf;
    @ManyToOne
    private StoreEntity store;
    @ManyToOne
    private RawMaterialEntity rawMaterial;    
    @ManyToOne
    private RawIngredientEntity rawIngredient;    
    private Integer quantity;
    @ManyToOne
    private MonthScheduleEntity schedule;
    @ManyToOne
    private MasterProductionScheduleEntity mps;
    private Integer day;
    
    public MaterialRequirementEntity(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RawIngredientEntity getRawIngredient() {
        return rawIngredient;
    }

    public void setRawIngredient(RawIngredientEntity rawIngredient) {
        this.rawIngredient = rawIngredient;
    }

    public StoreEntity getStore() {
        return store;
    }

    public void setStore(StoreEntity store) {
        this.store = store;
    }

    public ManufacturingFacilityEntity getMf() {
        return mf;
    }

    public MasterProductionScheduleEntity getMps() {
        return mps;
    }

    public void setMps(MasterProductionScheduleEntity mps) {
        this.mps = mps;
    }        

    public void setMf(ManufacturingFacilityEntity mf) {
        this.mf = mf;
    }

    public RawMaterialEntity getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(RawMaterialEntity rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public MonthScheduleEntity getSchedule() {
        return schedule;
    }

    public void setSchedule(MonthScheduleEntity schedule) {
        this.schedule = schedule;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
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
        if (!(object instanceof MaterialRequirementEntity)) {
            return false;
        }
        MaterialRequirementEntity other = (MaterialRequirementEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityManager.MaterialRequirementEntity[ id=" + id + " ]";
    }
    
}
