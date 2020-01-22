package CorporateManagement.RestaurantManagement;

import EntityManager.ComboEntity;
import EntityManager.ComboLineItemEntity;
import EntityManager.ItemEntity;
import EntityManager.LineItemEntity;
import EntityManager.MenuItemEntity;
import EntityManager.RawIngredientEntity;
import EntityManager.RecipeEntity;
import EntityManager.SupplierEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class RestaurantManagementBean implements RestaurantManagementBeanLocal {

    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;

    @Override
    public boolean addRawIngredients(String SKU, String name, String category, String description, Integer _length, Integer width, Integer height) {
        System.out.println("addRawIngredient() called with SKU:" + SKU);
        try {
            RawIngredientEntity rawIngredient = new RawIngredientEntity(SKU, name, category, description, _length, width, height);
            em.persist(rawIngredient);
            em.flush();
            em.merge(rawIngredient);
            System.out.println("Raw Ingredient name \"" + name + "\" added successfully.");
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to add new raw ingredient:\n" + ex);
            return false;
        }
    }

    @Override
    public boolean editRawIngredients(String id, String SKU, String name, String category, String description) {
        System.out.println("editRawIngredient() called with SKU:" + SKU);
        try {
            RawIngredientEntity i = em.find(RawIngredientEntity.class, Long.valueOf(id));
            i.setName(name);
            i.setCategory(category);
            i.setDescription(description);
            em.flush();
            System.out.println("\nServer updated raw ingredient:\n" + name);
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to update raw ingredient:\n" + ex);
            return false;
        }
    }

    @Override
    public boolean removeRawIngredients(String id) {
        System.out.println("removeRawIngredient() called with SKU:" + id);
        try {
            RawIngredientEntity rawIngredientEntity = em.getReference(RawIngredientEntity.class, Long.valueOf(id));
            rawIngredientEntity.setIsDeleted(true);
            em.merge(rawIngredientEntity);
            em.flush();
            System.out.println("removeRawIngredient(): Raw ingridient removed succesfully");
            List<RecipeEntity> recipes = listAllRecipe();
            for (int i = 0; i < recipes.size(); i++) {
                List<LineItemEntity> lineItems = recipes.get(i).getListOfLineItems();
                for (LineItemEntity lineItem : lineItems) {
                    if (Objects.equals(lineItem.getItem().getId(), Long.valueOf(id))) {
                        deleteLineItemFromRecipe(lineItem.getId(), recipes.get(i).getId());
                    }
                    break;
                }
            }
            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("removeRawIngredient(): Failed to find SKU");
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer failed to remove raw ingredient:\n" + ex);
            return false;
        }
    }

    @Override
    public RawIngredientEntity viewRawIngredients(String SKU) {
        System.out.println("viewRawIngredient() called with SKU:" + SKU);
        try {
            Query q = em.createQuery("SELECT t FROM RawIngredientEntity t");

            for (Object o : q.getResultList()) {
                RawIngredientEntity i = (RawIngredientEntity) o;
                if (i.getSKU().equalsIgnoreCase(SKU) && i.getIsDeleted() == false) {
                    System.out.println("\nServer returns raw ingredient:\n" + SKU);
                    return i;
                }
            }
            return null; //Could not find
        } catch (Exception ex) {
            System.out.println("\nServer failed to view raw ingredient:\n" + ex);
            return null;
        }
    }

    @Override
    public boolean addMenuItem(String SKU, String name, String category, String description, String imageURL, Integer _length, Integer width, Integer height) {
        System.out.println("addMenuItem() called with SKU:" + SKU);
        try {
            MenuItemEntity menuItemEntity = new MenuItemEntity(SKU, name, category, description, imageURL, _length, width, height);
            em.persist(menuItemEntity);
            System.out.println("MenuItem name \"" + name + "\" added successfully.");
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to add new menuItem:\n" + ex);
            return false;
        }
    }

    @Override
    public boolean editMenuItem(String id, String SKU, String name, String category, String description, String imageURL) {
        System.out.println("editMenuItem() called with SKU:" + SKU + " id : " + id);
        try {
            MenuItemEntity i = em.find(MenuItemEntity.class, Long.valueOf(id));
            System.out.println("i name is " + i.getName() + " image url is : " + imageURL);
            i.setName(name);
            i.setCategory(category);
            i.setDescription(description);
            i.setImageURL(imageURL);
            em.merge(i);
            em.flush();
            System.out.println("\nServer updated menuItem:\n" + name);
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to update menuItem:\n" + ex);
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeMenuItem(String SKU) {
        System.out.println("removeMenuItem() called with SKU:" + SKU);
        try {
            MenuItemEntity menuItemEntity = em.getReference(MenuItemEntity.class, Long.valueOf(SKU));
            menuItemEntity.setIsDeleted(true);
            RecipeEntity recipeEntity = menuItemEntity.getRecipe();
            recipeEntity.setMenuItem(null);
            em.merge(recipeEntity);
            em.merge(menuItemEntity);
            em.flush();
            System.out.println("MenuItem removed succesfully");
            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("removeMenuItem(): Could not find menuItem");
            return false;
        } catch (Exception ex) {
            System.out.println("removeMenuItem(): Failed to remove menuItem:\n" + ex);
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public MenuItemEntity viewMenuItem(String SKU) {
        System.out.println("viewMenuItem() called with SKU:" + SKU);
        try {
            Query q = em.createQuery("SELECT t FROM MenuItemEntity t where t.isDeleted=false");

            for (Object o : q.getResultList()) {
                MenuItemEntity i = (MenuItemEntity) o;
                if (i.getSKU().equalsIgnoreCase(SKU) && i.getIsDeleted() == false) {
                    System.out.println("\nServer returns menuItem:\n" + SKU);
                    return i;
                }
            }
            return null; //Could not find
        } catch (Exception ex) {
            System.out.println("\nServer failed to view menuItem:\n" + ex);
            return null;
        }
    }

    @Override
    public List<RawIngredientEntity> listAllRawIngredients() {
        System.out.println("listAllRawIngredients() called.");
        try {
            Query q = em.createQuery("SELECT t FROM RawIngredientEntity t where t.isDeleted=false");
            List<RawIngredientEntity> rawIngredientEntity = q.getResultList();
            return rawIngredientEntity;
        } catch (Exception ex) {
            System.out.println("\nServer failed to list all raw ingredients:\n" + ex);
            return null;
        }
    }

    @Override
    public List<MenuItemEntity> listAllMenuItem() {
        System.out.println("listAllMenuItem() called.");
        try {
            Query q = em.createQuery("SELECT t FROM MenuItemEntity t where t.isDeleted=false");
            List<MenuItemEntity> menuItemEntity = q.getResultList();
            return menuItemEntity;
        } catch (Exception ex) {
            System.out.println("\nServer failed to list all menuItem:\n" + ex);
            return null;
        }
    }

    @Override
    public List<MenuItemEntity> listAllMenuItemWithoutRecipe() {
        System.out.println("listAllMenuItem() called.");
        try {
            Query q = em.createQuery("Select fu from MenuItemEntity fu where fu.isDeleted=false and fu.id not in (Select f.id from MenuItemEntity f, RecipeEntity b where f.id=b.menuItem.id)");
            List<MenuItemEntity> menuItemEntity = q.getResultList();
            return menuItemEntity;
        } catch (Exception ex) {
            System.out.println("\nServer failed to list all menuItem without recipe:\n" + ex);
            return null;
        }
    }

    @Override
    public boolean createRecipe(String name, String description, Integer lotSize) {//
        System.out.println("createRecipe() called with name:" + name);
        try {
            RecipeEntity recipeEntity = new RecipeEntity();
            recipeEntity.setDescription(description);
            recipeEntity.setName(name);
            recipeEntity.setBroadLotSize(lotSize);
            em.persist(recipeEntity);

            System.out.println("Recipie Name \"" + name + "\" registered successfully as id:" + recipeEntity.getId());
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to createRecipe():\n" + ex);
            return false;
        }
    }

    @Override
    public boolean editRecipe(Long recipeId, String name, String description, Integer lotSize) {//
        System.out.println("editRecipe() called with name:" + name + "and description: " + description);
        try {
            RecipeEntity recipeEntity = em.find(RecipeEntity.class, recipeId);
            recipeEntity.setName(name);
            recipeEntity.setDescription(description);
            recipeEntity.setBroadLotSize(lotSize);
            em.persist(recipeEntity);
            System.out.println("editRecipe() is successful.");
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to editRecipe():\n" + ex);
            return false;
        }
    }

    @Override
    public boolean deleteRecipe(Long recipeId) {
        System.out.println("deleteRecipe() called with bomName:" + recipeId);
        try {
            RecipeEntity BOM = em.find(RecipeEntity.class, recipeId);
            em.remove(BOM);
            System.out.println("deleteRecipe() is successful.");
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to remove recipe:\n" + ex);
            return false;
        }
    }

    @Override
    public RecipeEntity viewSingleRecipe(Long recipeId) {
        System.out.println("viewRecipe() called with id:" + recipeId);
        try {
            RecipeEntity BOM = em.find(RecipeEntity.class, recipeId);
            System.out.println("viewSingleBOM() is successful.");
            return BOM;
        } catch (Exception ex) {
            System.out.println("\nServer failed to view recipe:\n" + ex);
            return null;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean addLineItemToRecipe(String SKU, Integer qty, Long recipeId) {
        System.out.println("addLineItemToBOM() called with id:" + recipeId);
        try {
            //if sku is not a raw ingredient throw exception
            ItemEntity item = getItemBySKU(SKU);
            if (item.getType().equals("Raw Ingredient")) {
                LineItemEntity lineItem = new LineItemEntity(item, qty, "");
                RecipeEntity recipeEntity = em.find(RecipeEntity.class, recipeId);
                recipeEntity.getListOfLineItems().add(lineItem);
                System.out.println("addLineItemToBOM() is successful.");
                return true;
            } else {
                throw new Exception();
            }
        } catch (Exception ex) {
            System.out.println("\nServer failed to addLineItemToBOM():\n" + ex);
            return false;
        }
    }

    @Override
    public boolean deleteLineItemFromRecipe(Long lineItemId, Long recipeId) {
        System.out.println("deleteLineItemFromRecipe() called with id:" + recipeId);
        try {
            LineItemEntity lineItem = em.find(LineItemEntity.class, lineItemId);
            RecipeEntity recipeEntity = em.find(RecipeEntity.class, recipeId);
            recipeEntity.getListOfLineItems().remove(lineItem);
            System.out.println("Line item id:" + lineItemId + " is removed from recipe");
            em.remove(lineItem);
            System.out.println("Line item id:" + lineItemId + " is removed from Line Item");
            System.out.println("deleteLineItemFromRecipe() is successful.");
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to deleteLineItemFromRecipe():\n" + ex);
            return false;
        }
    }

    @Override
    public boolean linkRecipeAndMenuItem(Long recipeId, Long MenuItemId) {
        System.out.println("linkRecipeAndMenuItem() called with id:" + recipeId);
        try {
            RecipeEntity recipeEntity = em.find(RecipeEntity.class, recipeId);
            MenuItemEntity menuItem = em.find(MenuItemEntity.class, MenuItemId);
            recipeEntity.setMenuItem(menuItem);
            menuItem.setRecipe(recipeEntity);
            System.out.println("linkRecipeAndMenuItem() is successful.");
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to linkRecipeAndMenuItem():\n" + ex);
            return false;
        }
    }

    @Override
    public List<RecipeEntity> listAllRecipe() {
        System.out.println("listAllRecipe() called.");
        try {
            Query q = em.createQuery("SELECT b FROM RecipeEntity b");
            List<RecipeEntity> recipeEntity = q.getResultList();
            System.out.println("listAllRecipe() is successful.");
            return recipeEntity;
        } catch (Exception ex) {
            System.out.println("\nServer failed to listAllRecipe:\n" + ex);
            return null;
        }
    }

    @Override
    public ItemEntity getItemBySKU(String SKU) {
        System.out.println("getItemBySKU() called with SKU: " + SKU);
        try {
            Query q = em.createQuery("Select i from ItemEntity i where i.SKU=:SKU and i.isDeleted=false");
            q.setParameter("SKU", SKU);
            ItemEntity item = (ItemEntity) q.getSingleResult();
            System.out.println("getItemBySKU() is successful.");
            return item;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getItemBySKU():\n" + ex);
            return null;
        }
    }

    @Override
    public boolean checkSKUExists(String SKU) {
        try {
            Query q = em.createQuery("Select i from ItemEntity i where i.SKU=:SKU and i.isDeleted=false");
            q.setParameter("SKU", SKU);
            q.getSingleResult();
            return true;
        } catch (NoResultException n) {
            System.out.println("\nServer return no result:\n" + n);
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer failed to perform checkSKUExists:\n" + ex);
            return false;
        }
    }

    @Override
    public List<ComboEntity> getAllCombo() {
        try {
            Query q = em.createQuery("select c from ComboEntity c where c.isDeleted=false");
            return q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public ComboEntity createCombo(String SKU, String name, String description, String imageURL) {
        try {         
                ComboEntity combo = new ComboEntity(SKU, name, description, imageURL);
                em.persist(combo);
                em.flush();
                em.merge(combo);
                return combo;           
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean removeCombo(Long comboID) {
        System.out.println("removeCombo() called with SKU:" + comboID);
        try {
            ComboEntity comboEntity = em.getReference(ComboEntity.class, comboID);
            comboEntity.setIsDeleted(true);
            em.merge(comboEntity);
            em.flush();
            System.out.println("Combo removed succesfully");
            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("Combo not found");
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer failed to remove combo:\n" + ex);
            return false;
        }
    }

    @Override
    public Boolean editCombo(Long comboID, String SKU, String name, String description, String imageURL) {
        System.out.println("editCombo() called");
        try {
            Query q = em.createQuery("select c from ComboEntity c where c.SKU = ?1").setParameter(1, SKU);
            List<ComboEntity> listOfComboEntity = q.getResultList();
            ComboEntity comboEntity = em.getReference(ComboEntity.class, comboID);
            if (listOfComboEntity == null || listOfComboEntity.isEmpty() || comboEntity.getId().equals(comboID)) {
                comboEntity.setSKU(SKU);
                comboEntity.setName(name);
                comboEntity.setDescription(description);
                comboEntity.setImageURL(imageURL);
                em.merge(comboEntity);
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean addLineItemToCombo(Long comboId, Long lineItemId) {
        try {
            ComboLineItemEntity lineItem = em.find(ComboLineItemEntity.class, lineItemId);
                ComboEntity combo = em.find(ComboEntity.class, comboId);
                lineItem.setCombo(combo);
                combo.getLineItemList().add(lineItem);
                em.merge(combo);
                return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public ComboLineItemEntity createComboLineItem(String SKU) {
        System.out.println("createComboLineItem() called");
        try {
            Query q = em.createQuery("Select i from ItemEntity i where i.SKU=:SKU");
            q.setParameter("SKU", SKU);
            MenuItemEntity item = (MenuItemEntity) q.getSingleResult();
            ComboLineItemEntity lineItem = new ComboLineItemEntity();
            lineItem.setMenuItem(item);
            em.persist(lineItem);
            return lineItem;
        } catch (NoResultException ex) {
            System.out.println("Could not find menu Item with SKU.");
        } catch (Exception ex) {
            System.out.println("Failed to createComboLineItem()");
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean removeLineItemFromCombo(Long comboId, Long lineItemId) {
        System.out.println("removeLineItemFromCombo() called");
        try {
            ComboEntity combo = em.find(ComboEntity.class, comboId);
            ComboLineItemEntity lineItem = em.find(ComboLineItemEntity.class, lineItemId);
            combo.getLineItemList().remove(lineItem);
            em.merge(combo);
            this.deleteComboLineItem(lineItemId);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public Boolean deleteComboLineItem(Long id) {
        try {
            em.remove(em.find(ComboLineItemEntity.class, id));
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
