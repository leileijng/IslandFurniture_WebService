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
import javax.persistence.OneToOne;

@Entity
public class RecipeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Lob
    private String name;
    @Lob
    private String description;
    @OneToOne
    private MenuItemEntity menuItem;
    private Integer broadLotSize;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.REFRESH})
    private List<LineItemEntity> listOfLineItems;

    public RecipeEntity() {
        listOfLineItems = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MenuItemEntity getMenuItem() {
        return menuItem;
    }

    public Integer getBroadLotSize() {
        return broadLotSize;
    }

    public void setBroadLotSize(Integer broadLotSize) {
        this.broadLotSize = broadLotSize;
    }

    public void setMenuItem(MenuItemEntity furniture) {
        this.menuItem = furniture;
    }

    public List<LineItemEntity> getListOfLineItems() {
        return listOfLineItems;
    }

    public void setListOfLineItems(List<LineItemEntity> listOfLineItems) {
        this.listOfLineItems = listOfLineItems;
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
        if (!(object instanceof RecipeEntity)) {
            return false;
        }
        RecipeEntity other = (RecipeEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entityManagerBean.Recipe[ id=" + id + " ]";
    }

}
