package ECommerce;

import EntityManager.FeedbackEntity;
import EntityManager.ItemEntity;
import EntityManager.MemberEntity;
import EntityManager.SubscriptionEntity;
import EntityManager.WishListEntity;
import java.util.Date;
import java.util.Properties;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ECommerceBean implements ECommerceBeanLocal {

    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;
    FeedbackEntity feedback;

    String emailServerName = "mailauth.comp.nus.edu.sg";
    String emailFromAddress = "a0105466@comp.nus.edu.sg";
    String toEmailAddress = "a0105466@comp.nus.edu.sg";
    String mailer = "JavaMailer";

    @Override
    public boolean addFeedback(String subject, String name, String email, String message) {
        System.out.println("addFeedback() called");
        try {
            feedback = new FeedbackEntity(subject, name, email, message);
            em.persist(feedback);
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to add new feedback:\n" + ex);
            return false;
        }
    }

    @Override
    public Boolean addItemToWishlist(String sku, Long memberId) {
        try {
            System.out.println("addItemToWishlist() sku is " + sku + " memberId is " + memberId);

            MemberEntity member = em.find(MemberEntity.class, memberId);

            Query q = em.createQuery("SELECT t FROM ItemEntity t where t.SKU=:sku");
            q.setParameter("sku", sku);
            ItemEntity item = (ItemEntity) q.getSingleResult();

            System.out.println("addItemToWishlist(): item found SKU is " + item.getSKU());
            WishListEntity wishList = member.getWishList();

            Boolean itemExistInWishList = false;
            for (int i = 0; i < wishList.getItems().size(); i++) {
                if (wishList.getItems().get(i) == item) {
                    itemExistInWishList = true;
                }
            }

            if (itemExistInWishList == true) {
                return false;
            } else {
                wishList.getItems().add(item);
                em.merge(member);
                return true;
            }
        } catch (Exception ex) {
            System.out.println("addItemToWishlist(): Error");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean removeItemFromWishlist(String sku, Long memberId) {
        System.out.println("removeItemFromWishlist() sku is " + sku + " memberId is " + memberId);
        try {
            MemberEntity member = em.find(MemberEntity.class, memberId);

            Query q = em.createQuery("SELECT t FROM ItemEntity t where t.SKU=:sku");
            q.setParameter("sku", sku);
            ItemEntity item = (ItemEntity) q.getSingleResult();

            System.out.println("removeItemFromWishlist(): item found SKU is " + item.getSKU());
            WishListEntity wishList = member.getWishList();

            for (int i = 0; i < wishList.getItems().size(); i++) {
                if (wishList.getItems().get(i) == item) {
                    wishList.getItems().remove(i);
                }
            }
            em.merge(member);
            return true;
        } catch (Exception ex) {
            System.out.println("removeItemFromWishlist(): Error");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean addEmailToSubscription(String email) {
        System.out.println("addEmailToSubscription()");
        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setEmail(email);
        em.merge(subscription);

        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");
            javax.mail.Authenticator auth = new CommonInfrastructure.SystemSecurity.SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            Message msg = new MimeMessage(session);
            if (msg != null) {
                msg.setFrom(InternetAddress.parse(emailFromAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
                msg.setSubject("Island Furniture Monthly Newsletter");
                String messageText = "Greetings from Island Furniture... \n\n"
                        + "You have been successfully added to our monthly newsletter! :\n\n"
                        + "Click here to unsubscribe: http://localhost:8080/IS3102_Project-war/ECommerce_UnsubscribeServlet?email=" + email;
                msg.setText(messageText);
                msg.setHeader("X-Mailer", mailer);
                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);
                Transport.send(msg);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public WishListEntity getWishList(String email) {
        System.out.println("shoppingList() called.");
        try {
            Query q = em.createQuery("SELECT t FROM MemberEntity t where t.email=:email");
            q.setParameter("email", email);
            MemberEntity member = (MemberEntity) q.getSingleResult();

            return member.getWishList();
        } catch (Exception ex) {
            System.out.println("\nServer failed to list shopping list:\n" + ex);
            return null;
        }
    }

    @Override
    public Boolean sendMonthlyNewsletter() {
        System.out.println("sendMonthlyNewsletter():");
        try {
            Query q = em.createQuery("SELECT t FROM SubscriptionEntity t");
            for (Object o : q.getResultList()) {
                SubscriptionEntity subscriber = (SubscriptionEntity) o;
                    try {
                        Properties props = new Properties();
                        props.put("mail.transport.protocol", "smtp");
                        props.put("mail.smtp.host", emailServerName);
                        props.put("mail.smtp.port", "25");
                        props.put("mail.smtp.auth", "true");
                        props.put("mail.smtp.starttls.enable", "true");
                        props.put("mail.smtp.debug", "true");
                        javax.mail.Authenticator auth = new CommonInfrastructure.SystemSecurity.SMTPAuthenticator();
                        Session session = Session.getInstance(props, auth);
                        session.setDebug(true);
                        Message msg = new MimeMessage(session);
                        if (msg != null) {
                            msg.setFrom(InternetAddress.parse(emailFromAddress, false)[0]);
                            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(subscriber.getEmail(), false));
                            msg.setSubject("Island Furniture Staff Monthly Newsletter");
                            String messageText = "Greetings from Island Furniture... \n\n"
                                    + "Here is your monthly newsletter :\n\n"
                                    + "Promotion for this week is as follow"
                                    + "Click here to unsubscribe: http://localhost:8080/IS3102_Project-war/ECommerce_UnsubscribeServlet?email=" + subscriber.getEmail();
                            msg.setText(messageText);
                            msg.setHeader("X-Mailer", mailer);
                            Date timeStamp = new Date();
                            msg.setSentDate(timeStamp);
                            Transport.send(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                    return true;
            }
        } catch (Exception ex) {
            System.out.println("\nServer failed to send monthly code :\n" + ex);
            return false;
        }
        return true;
    }

    @Override
    public Boolean removeEmailFromSubscription(String email) {
        Query q = em.createQuery("SELECT t FROM SubscriptionEntity t");
        for (Object o : q.getResultList()) {
            SubscriptionEntity subscriber = (SubscriptionEntity) o;
                if (subscriber.getEmail().equalsIgnoreCase(email)) {
                    em.remove(subscriber);
                    em.flush();
                    return true;
                }
        }
        return false;
    }

}
