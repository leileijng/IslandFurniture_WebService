package EntityManager;

import java.io.Serializable;
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
public class SalesRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String receiptNo;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdDate;
    private Double amountDue;
    private Double amountPaid;
    private Double amountPaidUsingPoints;
    private Integer loyaltyPointsDeducted;
    private String currency;
    private String servedByStaff;
    private String posName;
    @OneToMany(cascade = {CascadeType.ALL})
    private List<LineItemEntity> itemsPurchased;
    @ManyToOne
    private MemberEntity member;
    @ManyToOne
    private StoreEntity store;

    public SalesRecordEntity() {
    }

    public SalesRecordEntity(MemberEntity member, Double amountDue, Double amountPaid, Double amountPaidUsingPoints, Integer loyaltyPointsDeducted, String currency, String posName, String staffEntity, List<LineItemEntity> itemsPurchased, String receiptNo) {
        this.receiptNo = receiptNo;
        this.createdDate = new Date();
        this.member = member;
        this.amountDue = amountDue;
        this.amountPaid = amountPaid;
        this.amountPaidUsingPoints = amountPaidUsingPoints;
        this.loyaltyPointsDeducted = loyaltyPointsDeducted;
        this.currency = currency;
        this.posName = posName;
        this.servedByStaff = staffEntity;
        this.itemsPurchased = itemsPurchased;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public Integer getLoyaltyPointsDeducted() {
        return loyaltyPointsDeducted;
    }

    public void setLoyaltyPointsDeducted(Integer loyaltyPointsDeducted) {
        this.loyaltyPointsDeducted = loyaltyPointsDeducted;
    }

    public String getServedByStaff() {
        return servedByStaff;
    }

    public void setServedByStaff(String servedByStaff) {
        this.servedByStaff = servedByStaff;
    }

    public StoreEntity getStore() {
        return store;
    }

    public void setStore(StoreEntity store) {
        this.store = store;
    }

    public Double getAmountPaidUsingPoints() {
        return amountPaidUsingPoints;
    }

    public void setAmountPaidUsingPoints(Double amountPaidUsingPoints) {
        this.amountPaidUsingPoints = amountPaidUsingPoints;
    }

    public MemberEntity getMember() {
        return member;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public List<LineItemEntity> getItemsPurchased() {
        return itemsPurchased;
    }

    public void setItemsPurchased(List<LineItemEntity> itemsPurchased) {
        this.itemsPurchased = itemsPurchased;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(Double amountDue) {
        this.amountDue = amountDue;
    }

}
