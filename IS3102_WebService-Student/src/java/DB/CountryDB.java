/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.ws.rs.core.Response;

/**
 *
 * @author Vanesssa Jiang Lei
 */
public class CountryDB {

    public CountryDB() {
    }
        public String getCurrency(Long countryCode) throws SQLException{
         try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?user=root&password=12345");
            String stmt = "SELECT * FROM countryentity c WHERE c.ID=?";
            PreparedStatement ps = conn.prepareStatement(stmt);
            ps.setLong(1, countryCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String currency = rs.getString("CURRENCY");
                System.out.println("currecy is: " + currency);
                return currency;
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error Message: " + ex.getMessage());
        }
         return null;
}
}
