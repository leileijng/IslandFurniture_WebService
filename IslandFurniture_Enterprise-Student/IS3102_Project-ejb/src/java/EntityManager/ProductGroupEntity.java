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
import javax.persistence.Lob;
import javax.persistence.OneToMany;

@Entity
public class ProductGroupEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Lob
    private String productGroupName;   
    private Integer workHours;
    private Integer lotSize;
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="productGroup")
    private List<ProductGroupLineItemEntity> lineItemList;
    @OneToMany(cascade={CascadeType.REMOVE}, mappedBy="productGroup")
    private List<SaleForecastEntity> saleForcastEntityList;
    @OneToMany(cascade={CascadeType.REMOVE}, mappedBy="productGroup")
    private List<SaleAndOperationPlanEntity> sopList;
    private String type;
    private Boolean isDeleted;
    
    public ProductGroupEntity(){
        this.lineItemList = new ArrayList<>();
        this.saleForcastEntityList = new ArrayList<>();
        this.sopList = new ArrayList<>();
        this.isDeleted=false;
    } 

    public ProductGroupEntity(String productGroupName, Integer workHours, Integer lotSize) {
        this.workHours = workHours;
        this.productGroupName = productGroupName;
        this.lotSize = lotSize;
        this.lineItemList = new ArrayList<>();
        this.saleForcastEntityList = new ArrayList<>();
        this.sopList = new ArrayList<>();
        this.isDeleted = false;
    }        

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }        
    
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public void create(String name) {
        setName(name);
    }

    public List<SaleAndOperationPlanEntity> getSopList() {
        return sopList;
    }

    public void setSopList(List<SaleAndOperationPlanEntity> sopList) {
        this.sopList = sopList;
    }        
    
    public Integer getWorkHours() {
        return workHours;
    }

    public void setWorkHours(Integer workHours) {
        this.workHours = workHours;
    }
    
    public String getProductGroupName() {
        return productGroupName;
    }

    public void setProductGroupName(String productGroupName) {
        this.productGroupName = productGroupName;
    }

    public List<ProductGroupLineItemEntity> getLineItemList() {
        return lineItemList;
    }

    public void setLineItemList(List<ProductGroupLineItemEntity> lineItemList) {
        this.lineItemList = lineItemList;
    }

    public List<SaleForecastEntity> getSaleForcastEntityList() {
        return saleForcastEntityList;
    }

    public void setSaleForcastEntityList(List<SaleForecastEntity> saleForcastEntityList) {
        this.saleForcastEntityList = saleForcastEntityList;
    }
    
    public boolean checkLineItemParameter(){
        try{
            double amount = 0;
            for(ProductGroupLineItemEntity lineItem: this.lineItemList){
                amount += lineItem.getPercent();
            }
            if(amount == 1.0)
                return true;            
        }catch(Exception ex){
            ex.printStackTrace();            
        }                
        return false;
   }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return productGroupName;
    }

    public void setName(String name) {
        this.productGroupName = name;
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
        if (!(object instanceof ProductGroupEntity)) {
            return false;
        }
        ProductGroupEntity other = (ProductGroupEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityManager.ProductionGroupEntity[ id=" + id + " ]";
    }

    public Integer getLotSize() {
        return lotSize;
    }

    public void setLotSize(Integer lotSize) {
        this.lotSize = lotSize;
    }
    
}
