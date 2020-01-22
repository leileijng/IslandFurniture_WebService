/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommonInfrastructure.SystemSecurity;

import javax.mail.*;

/**
 *
 * @author yang
 */
public class SMTPAuthenticator extends javax.mail.Authenticator {
    // Replace with your actual unix id

    private static final String SMTP_AUTH_USER = "A0097787";
// Replace with your actual unix password
    private static final String SMTP_AUTH_PWD = "opkl3255";

    public SMTPAuthenticator() {
        
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        System.out.println("Password authentication() called:");
        String username = SMTP_AUTH_USER;
        String password = SMTP_AUTH_PWD;
        return new PasswordAuthentication(username, password);
    }

}
