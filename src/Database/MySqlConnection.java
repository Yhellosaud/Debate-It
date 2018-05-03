/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqlconnection;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import SharedModels.*;
import java.util.*;

/**
 *
 * @author Yhellosaud
 */
public class MySqlConnection {

    public static void main(String[] args) {
        
        String url = "jdbc:mysql://localhost:3306/didatabase?autoReconnect=true&useSSL=false";
        String username = "root";
        String password = "dicomp319";
        Connection c;
        System.out.println("Connecting database...");
        
        try {
            
            c = (Connection)DriverManager.getConnection(url, username, password); 
            System.out.println("Database connected!\n");

            UserManager um = new UserManager(c);
            //DebateManager dm = new DebateManager(c);
            //ItemManager im = new ItemManager(c);
                        
            ArrayList<Integer> pastDebateIDs = new ArrayList<>(Arrays.asList(3, 5, 8, 17, 1, 2));
            ArrayList<Integer> votedDebateIDs = new ArrayList<>(Arrays.asList(31, 71, 6, 10, 14));
                    
            User user = new User("Eda Hanım", "141511", 1, pastDebateIDs, votedDebateIDs);
            
            um.InsertUser(user);
            um.getUserDetails("Eda Hanım");
        }
        
        catch (SQLException e) {
            
            System.out.println("Cannot connect the database!");
            e.printStackTrace();
        }
    }
}
