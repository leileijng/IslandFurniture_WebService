package ECommerce;

import EntityManager.WishListEntity;
import javax.ejb.Local;

@Local
public interface ECommerceBeanLocal {
    public boolean addFeedback(String subject, String name, String email, String message);
    public WishListEntity getWishList(String email);
    public Boolean addItemToWishlist(String sku, Long memberId);
    public Boolean removeItemFromWishlist(String sku, Long memberId);
    public Boolean addEmailToSubscription(String email);
    public Boolean removeEmailFromSubscription(String email);
    public Boolean sendMonthlyNewsletter();
}
