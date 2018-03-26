/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqlconnection;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Yhellosaud
 */
public class MySqlConnection {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String url = "jdbc:mysql://localhost:3306/diDatabase";
        String username = "root";
        String password = "dicomp319";
        
        System.out.println("Connecting database...");
        
        try(Connection connection = (Connection)DriverManager.getConnection(url, username, password)) {
            
            System.out.println("Database connected!");
        }
        catch (SQLException e) {
            
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
    
}
