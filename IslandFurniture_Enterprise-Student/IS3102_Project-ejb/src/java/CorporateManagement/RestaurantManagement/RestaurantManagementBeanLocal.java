package CorporateManagement.RestaurantManagement;

import EntityManager.ComboEntity;
import EntityManager.ComboLineItemEntity;
import EntityManager.ItemEntity;
import EntityManager.MenuItemEntity;
import EntityManager.RawIngredientEntity;
import EntityManager.RecipeEntity;
import java.util.List;
import javax.ejb.Local;

@Local
public interface RestaurantManagementBeanLocal {

    public boolean addRawIngredients(String SKU, String name, String category, String description, Integer _length, Integer width, Integer height);

    public boolean editRawIngredients(String id, String SKU, String name, String category, String description);

    public boolean removeRawIngredients(String SKU);

    public RawIngredientEntity viewRawIngredients(String SKU);

    public boolean addMenuItem(String SKU, String name, String category, String description, String imageURL, Integer _length, Integer width, Integer height);

    public boolean editMenuItem(String id, String SKU, String name, String category, String description, String imageURL);

    public boolean removeMenuItem(String SKU);

    public MenuItemEntity viewMenuItem(String SKU);

    public List<RawIngredientEntity> listAllRawIngredients();

    public List<MenuItemEntity> listAllMenuItem();

    public List<MenuItemEntity> listAllMenuItemWithoutRecipe();

    public boolean createRecipe(String name, String description, Integer lotSize);

    public boolean editRecipe(Long recipeId, String name, String description, Integer lotSize);

    public boolean deleteRecipe(Long recipeId);

    public RecipeEntity viewSingleRecipe(Long recipeId);

    public boolean addLineItemToRecipe(String SKU, Integer qty, Long recipeId);

    public boolean deleteLineItemFromRecipe(Long lineItemId, Long recipeId);

    public boolean linkRecipeAndMenuItem(Long recipeId, Long menuItemId);

    public List<RecipeEntity> listAllRecipe();

    public ItemEntity getItemBySKU(String SKU);

    public boolean checkSKUExists(String SKU);

    public List<ComboEntity> getAllCombo();

    public ComboEntity createCombo(String SKU, String name, String Description, String imageURL);

    public boolean removeCombo(Long comboID);

    public Boolean editCombo(Long comboID, String SKU, String name, String description, String imageURL);

    public Boolean addLineItemToCombo(Long comboId, Long lineItemId);

    public ComboLineItemEntity createComboLineItem(String SKU);

    public Boolean removeLineItemFromCombo(Long comboId, Long lineItemId);

    public Boolean deleteComboLineItem(Long id);

}
