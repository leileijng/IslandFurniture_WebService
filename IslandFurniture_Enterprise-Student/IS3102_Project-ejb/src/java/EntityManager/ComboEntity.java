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
public class ComboEntity extends ItemEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Lob
    private String imageURL;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "combo")
    private List<ComboLineItemEntity> lineItemList;

    public ComboEntity() {
        this.lineItemList = new ArrayList<>();               
    }
    
    public ComboEntity(String SKU, String name, String description, String imageURL){
        super(SKU, 1, 1, 1);
        super.setName(name);
        super.setDescription(description);
        this.imageURL = imageURL;
        super.setType("Combo");
        super.setIsDeleted(false);
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public List<ComboLineItemEntity> getLineItemList() {
        return lineItemList;
    }

    public void setLineItemList(List<ComboLineItemEntity> lineItemList) {
        this.lineItemList = lineItemList;
    }        

}
