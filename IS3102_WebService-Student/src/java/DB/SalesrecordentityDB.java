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
public class SalesrecordentityDB {

    public SalesrecordentityDB() {
    }
    public String insertNewRecord(Long memberID, double amountPaid, String currency, String currentDate) throws SQLException{
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?user=root&password=12345");
            //syntax tried out in mysql, which successful
            //INSERT INTO salesrecordentity (AMOUNTDUE, AMOUNTPAID, AMOUNTPAIDUSINGPOINTS,CREATEDDATE,CURRENCY,LOYALTYPOINTSDEDUCTED,POSNAME,SERVEDBYSTAFF,MEMBER_ID,STORE_ID) VALUES (12,12, 0,'2020-01-11 12:08:01','SGD',0,' Counter 1','Cashier 1',23,59)
            String stmt = "INSERT INTO salesrecordentity (AMOUNTDUE, AMOUNTPAID, AMOUNTPAIDUSINGPOINTS,CREATEDDATE,CURRENCY,LOYALTYPOINTSDEDUCTED,POSNAME,SERVEDBYSTAFF,MEMBER_ID,STORE_ID) VALUES (?,?,0,?,?,0,' Counter 1','Cashier 1',?,59)";

            System.out.println(amountPaid + "," + currentDate + "," + currency + "," + memberID);
            PreparedStatement ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, amountPaid);
            ps.setDouble(2, amountPaid);
            ps.setString(3, currentDate);
            ps.setString(4, currency);
            ps.setLong(5, memberID);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("Result Set: " + rs.toString());
                int generatedKey = rs.getInt(1);
                String id = String.valueOf(generatedKey);
                System.out.println("inserted record id: " + id);
                conn.close();
                return id;
            } else {
                conn.close();
                return null;
            }

        } catch (Exception ex) {
            System.out.println("ERROR:" + ex.getMessage());
            return null;
        }
    }
}
