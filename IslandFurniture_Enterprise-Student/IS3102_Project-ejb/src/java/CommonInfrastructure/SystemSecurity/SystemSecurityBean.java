package CommonInfrastructure.SystemSecurity;

import CommonInfrastructure.AccountManagement.AccountManagementBeanLocal;
import Config.Config;
import EntityManager.StaffEntity;
import EntityManager.MemberEntity;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.Properties;
import javax.ejb.EJBException;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.persistence.NoResultException;

@Stateless
public class SystemSecurityBean implements SystemSecurityBeanLocal, SystemSecurityBeanRemote {

    @EJB
    AccountManagementBeanLocal accountManagementBean;

    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;
    String emailServerName = "mailauth.comp.nus.edu.sg";
    String emailFromAddress = "a0105466@comp.nus.edu.sg";
    String toEmailAddress = "a0105466@comp.nus.edu.sg";
    String mailer = "JavaMailer";

    public SystemSecurityBean() {

    }

    public Boolean discountMemberLoyaltyPoints(String memberId, Integer loyaltyPoints) {
        System.out.println("Server called discountMemberLoyaltyPoints():" + memberId);
        MemberEntity member = em.find(MemberEntity.class, Long.valueOf(memberId).longValue());
        member.setLoyaltyPoints(member.getLoyaltyPoints() + loyaltyPoints);
        em.merge(member);
        String activationCode = "";
        StaffEntity staff = null;
        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");
            javax.mail.Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            Message msg = new MimeMessage(session);
            if (msg != null) {
                msg.setFrom(InternetAddress.parse(emailFromAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(member.getEmail(), false));
                msg.setSubject("Island Furniture Loyalty Points Added");
                String messageText = "Greetings from Island Furniture... \n\n"
                        + "Thank you for being our loyal customer and as an appreciation for your support, "
                        + "we have added loyalty points to your account : " + loyaltyPoints + "\n\n"
                        + "The points can be used for discount on your next purchase in any of our store!" + "\n\n"
                        + "Link to our site: http://localhost:8080/IS3102_Project-war/B/index.jsp";
                msg.setText(messageText);
                msg.setHeader("X-Mailer", mailer);
                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);
                Transport.send(msg);
            }
            return true;
        } catch (Exception e) {

            e.printStackTrace();
            staff.setAccountActivationStatus(true);
            return false;
        }
    }

    //When staff user account is created, this function should be invoked
    public Boolean sendActivationEmailForStaff(String email) {
        System.out.println("Server called sendActivationEmailForStaff():" + email);
        String activationCode = "";
        StaffEntity staff = null;
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t");

            for (Object o : q.getResultList()) {
                StaffEntity i = (StaffEntity) o;
                if (i.getEmail().equalsIgnoreCase(email)) {
                    activationCode += i.getActivationCode();
                    staff = em.find(StaffEntity.class, i.getId());

                    System.out.println("\nServer returns activation code of staff:\n" + activationCode);

                    String passwordSalt = accountManagementBean.generatePasswordSalt();
                    String passwordHash = accountManagementBean.generatePasswordHash(passwordSalt, activationCode);

                    staff.setPasswordHash(passwordHash);
                    staff.setPasswordSalt(passwordSalt);
                    em.merge(staff);
                }
            }

        } catch (Exception ex) {
            System.out.println("\nServer failed to retrieve activationCode:\n" + ex);
            return false;
        }

        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");
            javax.mail.Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            Message msg = new MimeMessage(session);
            if (msg != null) {
                msg.setFrom(InternetAddress.parse(emailFromAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
                msg.setSubject("Island Furniture Staff Account Activation");
                String messageText = "Greetings from Island Furniture... \n\n"
                        + "Here is your activation code to be keyed in in order to activate your staff account :\n\n"
                        + "Email: " + email + "\n\n"
                        + "Activation Code: " + activationCode + "\n\n"
                        + "Link to activate your staff account: http://localhost:8080/IS3102_Project-war/A1/staffActivateAccount.jsp";
                msg.setText(messageText);
                msg.setHeader("X-Mailer", mailer);
                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);
                Transport.send(msg);
            }
            return true;
        } catch (Exception e) {

            e.printStackTrace();
            staff.setAccountActivationStatus(true);
            return false;
        }
    }

    //When member user account is created, this function should be invoked
    public Boolean sendActivationEmailForMember(String email) {
        System.out.println("Server called sendActivationEmailForMember():");
        String activationCode = "";
        try {
            Query q = em.createQuery("SELECT t FROM MemberEntity t");

            for (Object o : q.getResultList()) {
                MemberEntity i = (MemberEntity) o;
                if (i.getEmail().equalsIgnoreCase(email)) {
                    activationCode += i.getActivationCode();
                }
            }
        } catch (Exception ex) {
            System.out.println("\nServer failed to retrieve activation code:\n" + ex);
            return false;
        }

        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");
            javax.mail.Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            Message msg = new MimeMessage(session);
            if (msg != null) {
                msg.setFrom(InternetAddress.parse(emailFromAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
                msg.setSubject("Island Furniture Member Account Activation");
                String messageText = "Greetings from Island Furniture... \n\n"
                        + "Here is your activation code to be keyed in in order to activate your member account :\n\n"
                        + "Link to activate your member account: http://localhost:8080/IS3102_Project-war/ECommerce_ActivateMemberServlet?email=" + email + "&activateCode=" + activationCode;
                msg.setText(messageText);
                msg.setHeader("X-Mailer", mailer);
                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);
                Transport.send(msg);

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new EJBException(e.getMessage());
        }
    }

    public Boolean sendPasswordResetEmailForStaff(String email) {
        System.out.println("Server called sendPasswordResetEmailForStaff():");
        String passwordReset = "";
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t");
            for (Object o : q.getResultList()) {
                StaffEntity i = (StaffEntity) o;
                if (i.getEmail().equalsIgnoreCase(email)) {
                    i.setPasswordReset();
                    em.merge(i);
                    passwordReset += i.getPasswordReset();
                }
            }
        } catch (Exception ex) {
            System.out.println("\nServer failed to get activation code of staff:\n" + ex);
            return false;
        }

        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");
            javax.mail.Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            Message msg = new MimeMessage(session);
            if (msg != null) {
                msg.setFrom(InternetAddress.parse(emailFromAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
                msg.setSubject("Island Furniture Staff Account Password Reset");
                String messageText = "Greetings from Island Furniture... \n\n"
                        + "Here is your activation code to be keyed in in order to reset your staff account password :\n\n"
                        + "Email: " + email + "\n\n"
                        + "Activation Code: " + passwordReset + "\n\n"
                        + "Link to activate your staff account: http://localhost:8080/IS3102_Project-war/A1/staffResetPasswordCode.jsp";
                msg.setText(messageText);
                msg.setHeader("X-Mailer", mailer);
                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);
                Transport.send(msg);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new EJBException(e.getMessage());
        }
    }

    public Boolean sendPasswordResetEmailForMember(String email) {
        System.out.println("Server called sendPasswordResetEmailForStaff():");
        String passwordReset = "";
        try {
            Query q = em.createQuery("SELECT t FROM MemberEntity t");
            for (Object o : q.getResultList()) {
                MemberEntity i = (MemberEntity) o;
                if (i.getEmail().equalsIgnoreCase(email)) {
                    i.setPasswordReset();
                    em.merge(i);
                    passwordReset += i.getPasswordReset();
                }
            }
        } catch (Exception ex) {
            System.out.println("\nServer failed to get activation code of staff:\n" + ex);
            return false;
        }

        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");
            javax.mail.Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            Message msg = new MimeMessage(session);
            if (msg != null) {
                msg.setFrom(InternetAddress.parse(emailFromAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
                msg.setSubject("Island Furniture Member Account Password Reset");
                String messageText = "Greetings from Island Furniture... \n\n"
                        + "Here is your activation code to be keyed in in order to reset your member account password :\n\n"
                        + "Email: " + email + "\n\n"
                        + "Activation Code: " + passwordReset + "\n\n"
                        + "Link to reset your password: http://localhost:8080/IS3102_Project-war/B/memberResetPassword.jsp?email=" + email;
                msg.setText(messageText);
                msg.setHeader("X-Mailer", mailer);
                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);
                Transport.send(msg);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new EJBException(e.getMessage());
        }
    }

    public Boolean activateStaffAccount(String email, String code) {
        System.out.println("Server called validateCodeForStaff():");
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t");

            for (Object o : q.getResultList()) {
                StaffEntity staffEntity = (StaffEntity) o;
                if (staffEntity.getEmail().equalsIgnoreCase(email)) {
                    if (staffEntity.getActivationCode().equals(code)) {
                        System.out.println("\nServer activation code valid for staff:\n" + email);
                        staffEntity.setAccountActivationStatus(true);
                        staffEntity.setInvalidLoginAttempt(0);
                        em.merge(staffEntity);
                        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
                        out.println(new Date().toString() + ";" + "System" + ";activateStaffAccount();" + staffEntity.getId() + ";");
                        out.close();
                        return true;
                    } else {
                        System.out.println("\nServer activation code invalid for staff:\n" + email);
                        return false;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("\nServer failed to validate email for staff:\n" + ex);
            return false;
        }
        return false;
    }

    public Boolean activateMemberAccount(String email, String code) {
        try {
            Query q = em.createQuery("SELECT t FROM MemberEntity t WHERE t.email=:email");
            q.setParameter("email", email);

            MemberEntity memberEntity = null;
            try {
                memberEntity = (MemberEntity) q.getSingleResult();
            } catch (NoResultException ex) {
                return false;
            }

            if (memberEntity.getActivationCode().equals(code)) {
                System.out.println("\nServer activation code valid of member:\n" + email);
                memberEntity.setAccountActivationStatus(true);
                em.merge(memberEntity);
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
                out.println(new Date().toString() + ";" + "System" + ";activateMemberAccount();" + memberEntity.getId() + ";");
                out.close();
                return true;
            } else {
                System.out.println("\nServer activation code invalid of member:\n" + email);
                return false;
            }
        } catch (Exception ex) {
            System.out.println("\nServer failed to validate email for member:\n" + ex);
            return false;
        }
    }

    public Boolean validatePasswordResetForStaff(String email, String code) {
        System.out.println("Server called validatePasswordResetForStaff():");
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t");

            for (Object o : q.getResultList()) {
                StaffEntity i = (StaffEntity) o;
                if (i.getEmail().equalsIgnoreCase(email)) {
                    if (i.getPasswordReset().equals(code)) {
                        System.out.println("\nReset Password valid of staff:\n" + email);
                        return true;
                    } else {
                        System.out.println("\nReset Password invalid of staff:\n" + email);
                        return false;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("\nServer failed to validate email for member:\n" + ex);
            return false;
        }
        return false;
    }

    public Boolean validatePasswordResetForMember(String email, String code) {
        System.out.println("Server called validatePasswordResetForStaff():");
        try {
            Query q = em.createQuery("SELECT t FROM MemberEntity t");

            for (Object o : q.getResultList()) {
                MemberEntity i = (MemberEntity) o;
                if (i.getEmail().equalsIgnoreCase(email)) {
                    if (i.getPasswordReset().equals(code)) {
                        System.out.println("\nServer activation code valid of member:\n" + email);
                        return true;
                    } else {
                        System.out.println("\nServer activation code invalid of member:\n" + email);
                        return false;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("\nServer failed to validate email for member:\n" + ex);
            return false;
        }
        return false;
    }
}
