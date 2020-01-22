package EntityManager;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

@Entity
public class TransferOrderEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    private LineItemEntity lineItem;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateTransferred;
    @OneToOne
    private StorageBinEntity origin;
    @OneToOne
    private StorageBinEntity target;
    private String status;
    @ManyToOne
    private WarehouseEntity warehouse;
    private String submittedBy;
    private Boolean isDeleted;

    public TransferOrderEntity() {

    }

    public TransferOrderEntity(WarehouseEntity warehouse, LineItemEntity lineItem, StorageBinEntity origin, StorageBinEntity target) {
        this.warehouse = warehouse;
        this.lineItem = lineItem;
        this.origin = origin;
        this.target = target;
        this.dateCreated = Calendar.getInstance().getTime();
        this.dateTransferred = null;
        this.status = "Pending";
        submittedBy = "N.A.";
        this.isDeleted = false;
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        Calendar cal = Calendar.getInstance();
//        dateFormat.format(cal.getTime());
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LineItemEntity getLineItem() {
        return lineItem;
    }

    public void setLineItem(LineItemEntity lineItem) {
        this.lineItem = lineItem;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateTransferred() {
        return dateTransferred;
    }

    public void setDateTransferred(Date dateTransferred) {
        this.dateTransferred = dateTransferred;
    }

    public StorageBinEntity getOrigin() {
        return origin;
    }

    public void setOrigin(StorageBinEntity origin) {
        this.origin = origin;
    }

    public StorageBinEntity getTarget() {
        return target;
    }

    public void setTarget(StorageBinEntity target) {
        this.target = target;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
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
        if (!(object instanceof TransferOrderEntity)) {
            return false;
        }
        TransferOrderEntity other = (TransferOrderEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityManager.TransferOrderEntity[ id=" + id + " ]";
    }

    public WarehouseEntity getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseEntity warehouse) {
        this.warehouse = warehouse;
    }

    
}
