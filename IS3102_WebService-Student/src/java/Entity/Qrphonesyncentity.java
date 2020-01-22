/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jason
 */
@Entity
@Table(name = "qrphonesyncentity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Qrphonesyncentity.findAll", query = "SELECT q FROM Qrphonesyncentity q"),
    @NamedQuery(name = "Qrphonesyncentity.findById", query = "SELECT q FROM Qrphonesyncentity q WHERE q.id = :id"),
    @NamedQuery(name = "Qrphonesyncentity.findByMemberemail", query = "SELECT q FROM Qrphonesyncentity q WHERE q.memberemail = :memberemail"),
    @NamedQuery(name = "Qrphonesyncentity.findByQrcode", query = "SELECT q FROM Qrphonesyncentity q WHERE q.qrcode = :qrcode")})
public class Qrphonesyncentity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Size(max = 255)
    @Column(name = "MEMBEREMAIL")
    private String memberemail;
    @Size(max = 255)
    @Column(name = "QRCODE")
    private String qrcode;

    public Qrphonesyncentity() {
    }

    public Qrphonesyncentity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberemail() {
        return memberemail;
    }

    public void setMemberemail(String memberemail) {
        this.memberemail = memberemail;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
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
        if (!(object instanceof Qrphonesyncentity)) {
            return false;
        }
        Qrphonesyncentity other = (Qrphonesyncentity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Qrphonesyncentity[ id=" + id + " ]";
    }
    
}
