/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jason
 */
@Entity
@Table(name = "memberentity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Memberentity.findAll", query = "SELECT m FROM Memberentity m"),
    @NamedQuery(name = "Memberentity.findById", query = "SELECT m FROM Memberentity m WHERE m.id = :id"),
    @NamedQuery(name = "Memberentity.findByDob", query = "SELECT m FROM Memberentity m WHERE m.dob = :dob"),
    @NamedQuery(name = "Memberentity.findByAccountactivationstatus", query = "SELECT m FROM Memberentity m WHERE m.accountactivationstatus = :accountactivationstatus"),
    @NamedQuery(name = "Memberentity.findByAccountlockstatus", query = "SELECT m FROM Memberentity m WHERE m.accountlockstatus = :accountlockstatus"),
    @NamedQuery(name = "Memberentity.findByActivationcode", query = "SELECT m FROM Memberentity m WHERE m.activationcode = :activationcode"),
    @NamedQuery(name = "Memberentity.findByCumulativespending", query = "SELECT m FROM Memberentity m WHERE m.cumulativespending = :cumulativespending"),
    @NamedQuery(name = "Memberentity.findByIsdeleted", query = "SELECT m FROM Memberentity m WHERE m.isdeleted = :isdeleted"),
    @NamedQuery(name = "Memberentity.findByLoyaltycardid", query = "SELECT m FROM Memberentity m WHERE m.loyaltycardid = :loyaltycardid"),
    @NamedQuery(name = "Memberentity.findByLoyaltypoints", query = "SELECT m FROM Memberentity m WHERE m.loyaltypoints = :loyaltypoints"),
    @NamedQuery(name = "Memberentity.findByPasswordhash", query = "SELECT m FROM Memberentity m WHERE m.passwordhash = :passwordhash"),
    @NamedQuery(name = "Memberentity.findByPasswordreset", query = "SELECT m FROM Memberentity m WHERE m.passwordreset = :passwordreset"),
    @NamedQuery(name = "Memberentity.findByPasswordsalt", query = "SELECT m FROM Memberentity m WHERE m.passwordsalt = :passwordsalt"),
    @NamedQuery(name = "Memberentity.findBySecurityquestion", query = "SELECT m FROM Memberentity m WHERE m.securityquestion = :securityquestion"),
    @NamedQuery(name = "Memberentity.findByUnlockcode", query = "SELECT m FROM Memberentity m WHERE m.unlockcode = :unlockcode")})
public class Memberentity implements Serializable {
    @Column(name = "AGE")
    private Integer age;
    @Column(name = "INCOME")
    private Integer income;
    @Column(name = "JOINDATE")
    @Temporal(TemporalType.DATE)
    private Date joindate;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "OCCUPATION")
    private String occupation;
    @Column(name = "SERVICELEVELAGREEMENT")
    private Boolean servicelevelagreement;
    @JoinTable(name = "memberentity_lineitementity", joinColumns = {
        @JoinColumn(name = "MemberEntity_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "shoppingList_ID", referencedColumnName = "ID")})
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Lineitementity> lineitementityList;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Column(name = "DOB")
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Column(name = "ACCOUNTACTIVATIONSTATUS")
    private Boolean accountactivationstatus;
    @Column(name = "ACCOUNTLOCKSTATUS")
    private Boolean accountlockstatus;
    @Size(max = 255)
    @Column(name = "ACTIVATIONCODE")
    private String activationcode;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "ADDRESS")
    private String address;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "CITY")
    private String city;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CUMULATIVESPENDING")
    private Double cumulativespending;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Lob
    @Size(max = 2147483647)
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "ISDELETED")
    private Boolean isdeleted;
    @Size(max = 255)
    @Column(name = "LOYALTYCARDID")
    private String loyaltycardid;
    @Column(name = "LOYALTYPOINTS")
    private Integer loyaltypoints;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "NAME")
    private String name;
    @Size(max = 255)
    @Column(name = "PASSWORDHASH")
    private String passwordhash;
    @Size(max = 255)
    @Column(name = "PASSWORDRESET")
    private String passwordreset;
    @Size(max = 255)
    @Column(name = "PASSWORDSALT")
    private String passwordsalt;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Lob
    @Size(max = 2147483647)
    @Column(name = "PHONE")
    private String phone;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "SECURITYANSWER")
    private String securityanswer;
    @Column(name = "SECURITYQUESTION")
    private Integer securityquestion;
    @Size(max = 255)
    @Column(name = "UNLOCKCODE")
    private String unlockcode;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "ZIPCODE")
    private String zipcode;
    @JoinColumn(name = "COUNTRY_ID", referencedColumnName = "ID")
    @ManyToOne
    private Countryentity countryId;
    @JoinColumn(name = "LOYALTYTIER_ID", referencedColumnName = "ID")
    @ManyToOne
    private Loyaltytierentity loyaltytierId;

    @JoinColumn(name = "WISHLIST_ID", referencedColumnName = "ID")
    @ManyToOne
    private Wishlistentity wishlistId;

    public Memberentity() {
    }

    public Memberentity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Boolean getAccountactivationstatus() {
        return accountactivationstatus;
    }

    public void setAccountactivationstatus(Boolean accountactivationstatus) {
        this.accountactivationstatus = accountactivationstatus;
    }

    public Boolean getAccountlockstatus() {
        return accountlockstatus;
    }

    public void setAccountlockstatus(Boolean accountlockstatus) {
        this.accountlockstatus = accountlockstatus;
    }

    public String getActivationcode() {
        return activationcode;
    }

    public void setActivationcode(String activationcode) {
        this.activationcode = activationcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getCumulativespending() {
        return cumulativespending;
    }

    public void setCumulativespending(Double cumulativespending) {
        this.cumulativespending = cumulativespending;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public String getLoyaltycardid() {
        return loyaltycardid;
    }

    public void setLoyaltycardid(String loyaltycardid) {
        this.loyaltycardid = loyaltycardid;
    }

    public Integer getLoyaltypoints() {
        return loyaltypoints;
    }

    public void setLoyaltypoints(Integer loyaltypoints) {
        this.loyaltypoints = loyaltypoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordhash() {
        return passwordhash;
    }

    public void setPasswordhash(String passwordhash) {
        this.passwordhash = passwordhash;
    }

    public String getPasswordreset() {
        return passwordreset;
    }

    public void setPasswordreset(String passwordreset) {
        this.passwordreset = passwordreset;
    }

    public String getPasswordsalt() {
        return passwordsalt;
    }

    public void setPasswordsalt(String passwordsalt) {
        this.passwordsalt = passwordsalt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSecurityanswer() {
        return securityanswer;
    }

    public void setSecurityanswer(String securityanswer) {
        this.securityanswer = securityanswer;
    }

    public Integer getSecurityquestion() {
        return securityquestion;
    }

    public void setSecurityquestion(Integer securityquestion) {
        this.securityquestion = securityquestion;
    }

    public String getUnlockcode() {
        return unlockcode;
    }

    public void setUnlockcode(String unlockcode) {
        this.unlockcode = unlockcode;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Countryentity getCountryId() {
        return countryId;
    }

    public void setCountryId(Countryentity countryId) {
        this.countryId = countryId;
    }

    public Loyaltytierentity getLoyaltytierId() {
        return loyaltytierId;
    }

    public void setLoyaltytierId(Loyaltytierentity loyaltytierId) {
        this.loyaltytierId = loyaltytierId;
    }
    public Wishlistentity getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Wishlistentity wishlistId) {
        this.wishlistId = wishlistId;
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
        if (!(object instanceof Memberentity)) {
            return false;
        }
        Memberentity other = (Memberentity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Memberentity[ id=" + id + " ]";
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getIncome() {
        return income;
    }

    public void setIncome(Integer income) {
        this.income = income;
    }

    public Date getJoindate() {
        return joindate;
    }

    public void setJoindate(Date joindate) {
        this.joindate = joindate;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Boolean getServicelevelagreement() {
        return servicelevelagreement;
    }

    public void setServicelevelagreement(Boolean servicelevelagreement) {
        this.servicelevelagreement = servicelevelagreement;
    }

    @XmlTransient
    public List<Lineitementity> getLineitementityList() {
        return lineitementityList;
    }

    public void setLineitementityList(List<Lineitementity> lineitementityList) {
        this.lineitementityList = lineitementityList;
    }
    
}
