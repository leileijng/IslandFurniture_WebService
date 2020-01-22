package EntityManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

@Entity
public class ShippingOrderEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String status;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date shippedDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date expectedReceivedDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date receivedDate;
    @OneToMany(cascade = {CascadeType.ALL})
    private List<LineItemEntity> lineItems;
    @ManyToOne
    private WarehouseEntity origin;
    @ManyToOne
    private WarehouseEntity destination;
    private String submittedBy;

    public ShippingOrderEntity() {
        this.lineItems = new ArrayList<>();
        submittedBy = "N.A.";
    }

    public ShippingOrderEntity(Date expectedReceivedDate, WarehouseEntity origin, WarehouseEntity destination) {
        this.createdDate = new Date();
        this.expectedReceivedDate = expectedReceivedDate;
        this.lineItems = new ArrayList<>();
        this.origin = origin;
        this.destination = destination;
        this.status = "Pending";
        submittedBy = "N.A.";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    public Date getExpectedReceivedDate() {
        return expectedReceivedDate;
    }

    public void setExpectedReceivedDate(Date expectedReceivedDate) {
        this.expectedReceivedDate = expectedReceivedDate;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public List<LineItemEntity> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItemEntity> lineItems) {
        this.lineItems = lineItems;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public WarehouseEntity getOrigin() {
        return origin;
    }

    public void setOrigin(WarehouseEntity origin) {
        this.origin = origin;
    }

    public WarehouseEntity getDestination() {
        return destination;
    }

    public void setDestination(WarehouseEntity destination) {
        this.destination = destination;
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
        if (!(object instanceof ShippingOrderEntity)) {
            return false;
        }
        ShippingOrderEntity other = (ShippingOrderEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityManager.ShippingOrderEntity[ id=" + id + " ]";
    }

}
