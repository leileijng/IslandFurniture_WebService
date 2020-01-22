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
import javax.persistence.TemporalType;

@Entity
public class PurchaseOrderEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private SupplierEntity supplier;
    @OneToMany(cascade = {CascadeType.ALL})
    private List<LineItemEntity> lineItems;
    @ManyToOne
    private WarehouseEntity destination;
    @Temporal(value = TemporalType.DATE)
    private Date expectedReceivedDate;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdDate;

    private String status;
    private String submittedBy;

    public PurchaseOrderEntity() {
        submittedBy = "N.A.";
    }

    public PurchaseOrderEntity(SupplierEntity supplier, WarehouseEntity destination, Date expectedReceivedDate) {
        this.createdDate = new Date();
        this.supplier = supplier;
        this.destination = destination;
        this.expectedReceivedDate = expectedReceivedDate;
        this.setStatus("Pending");
        this.lineItems = new ArrayList<>();
        submittedBy = "N.A.";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SupplierEntity getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierEntity origin) {
        this.supplier = origin;
    }

    public List<LineItemEntity> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItemEntity> lineItems) {
        this.lineItems = lineItems;
    }

    public Date getExpectedReceivedDate() {
        return expectedReceivedDate;
    }

    public void setExpectedReceivedDate(Date expectedReceivedDate) {
        this.expectedReceivedDate = expectedReceivedDate;
    }

    public WarehouseEntity getDestination() {
        return destination;
    }

    public void setDestination(WarehouseEntity destination) {
        this.destination = destination;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

}
