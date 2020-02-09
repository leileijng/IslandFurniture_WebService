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
public class Storagebinentity_lineitementityDB {

    public Storagebinentity_lineitementityDB() {
    }

    public boolean insertNewRecord(int newlineItemId) throws SQLException {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?user=root&password=12345");

            String insertStorageBinLineItem = "insert into storagebinentity_lineitementity values(21,?);";
            PreparedStatement ps = conn.prepareStatement(insertStorageBinLineItem);
            ps.setInt(1, newlineItemId);
            int rs2 = ps.executeUpdate();

            if (rs2 > 0) {
                System.out.println("Insert into storagebinentity_lineitementity successfully");
                return true;
            } else {
                conn.close();
                return false;
            }
        } catch (Exception ex) {
            
            System.out.println("Storagebinentity_lineitementityDB" + ex.getMessage());
            ex.printStackTrace();
        }
        return true;
    }
}
