package EntityManager;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class LoyaltyTierEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String tier;
    private Double amtOfSpendingRequired;
    private Boolean isDeleted;

    public LoyaltyTierEntity() {
    }

    public LoyaltyTierEntity(String tier, Double amtRequiredPerAnnum) {
        this.tier = tier;
        this.amtOfSpendingRequired = amtRequiredPerAnnum;
        this.setIsDeleted(false);
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public Double getAmtOfSpendingRequired() {
        return amtOfSpendingRequired;
    }

    public void setAmtOfSpendingRequired(Double amtOfSpendingRequired) {
        this.amtOfSpendingRequired = amtOfSpendingRequired;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setLoyalty(String tier) {
        this.tier = tier;
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
        if (!(object instanceof LoyaltyTierEntity)) {
            return false;
        }
        LoyaltyTierEntity other = (LoyaltyTierEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "em.LoyaltyTier[ id=" + id + " ]";
    }

}
