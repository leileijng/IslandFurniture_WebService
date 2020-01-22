package EntityManager;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;

@Entity
public class AnnouncementEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Lob
    private String sender;
    @Lob
    private String title;
    @Lob
    private String message;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date broadcastedDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date expiryDate;

    public AnnouncementEntity() {
    }

    public AnnouncementEntity(String sender, String title, String message, Date expiryDate) {
        this.sender = sender;
        this.title = title;
        this.message = message;
        this.broadcastedDate = new Date();
        this.expiryDate = expiryDate;
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
        if (!(object instanceof AnnouncementEntity)) {
            return false;
        }
        AnnouncementEntity other = (AnnouncementEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityManager.AnnouncementEntity[ id=" + id + " ]";
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getBroadcastedDate() {
        return broadcastedDate;
    }

    public void setBroadcastedDate(Date broadcastedDate) {
        this.broadcastedDate = broadcastedDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    
}
