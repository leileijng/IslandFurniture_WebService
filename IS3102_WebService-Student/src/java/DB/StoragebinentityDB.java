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
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

/**
 *
 * @author Vanesssa Jiang Lei
 */
public class StoragebinentityDB {

    public StoragebinentityDB() {
    }

    public boolean decreaseFreeVolume(int quantity) throws SQLException {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?user=root&password=12345");

            String upadtestoragebinentity = "Update storagebinentity set storagebinentity.freeVolume = freeVolume - ? where id = 9;";
            PreparedStatement ps = conn.prepareStatement(upadtestoragebinentity);
            ps.setInt(1, quantity);
            int rs3 = ps.executeUpdate();

            if (rs3 > 0) {
                conn.close();
                System.out.println("Update storagebinentity successfully");
                return true;
            } else {
                conn.close();
                return false;
            }
        } catch (Exception ex) {
            System.out.println("StoragebinentityDB, decreaseFreeVolume" + ex.getMessage());
            ex.printStackTrace();

        }
        return true;
    }

    public boolean increaseFreeVolume(int quantity) throws SQLException {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?user=root&password=12345");

            String upadtestoragebinentityAdd = "Update storagebinentity set storagebinentity.freeVolume = freeVolume + ? where id = 20;";
            PreparedStatement ps = conn.prepareStatement(upadtestoragebinentityAdd);
            ps.setInt(1, quantity);
            int rsAddFreeVolume = ps.executeUpdate();

            if (rsAddFreeVolume > 0) {
                System.out.println("Update storagebinentity addd volume successfully");
            } else {
                conn.close();
                return false;
            }
        } catch (Exception ex) {
            System.out.println("StoragebinentityDB, increaseFreeVolume" + ex.getMessage());
            ex.printStackTrace();

        }
        return true;
    }
}
