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
            //////////////////////////////////////////////////////////////////////////
            ////////////////////////TEST CASE FOR USER MANAGER////////////////////////
            //UserManager um = new UserManager(c);
            //ArrayList<Integer> pastDebateIDs = new ArrayList<>(Arrays.asList(3, 5, 8, 17, 1, 2));
            //ArrayList<Integer> votedDebateIDs = new ArrayList<>(Arrays.asList(31, 71, 6, 10, 14));
            //User user = new User("Eda Hanım", "141511", 1, pastDebateIDs, votedDebateIDs);
            //um.insertUser(user);
            //um.getUserDetails("Eda Hanım");
            ////////////////////////TEST CASE FOR USER MANAGER////////////////////////
            //////////////////////////////////////////////////////////////////////////
            
            
            //////////////////////////////////////////////////////////////////////////
            ////////////////////////TEST CASE FOR DEBATE MANAGER////////////////////////
            DebateManager dm = new DebateManager(c);
            ArrayList<Player> players = new ArrayList<>();
            Idea idea = new Idea(1, "Anam bacım kardeşim midir", 8);
            int[] debateIDs = {3, 13, 31, 193, 131, 40};
            Debate debate = new Debate(idea, players, 1, 80, 1, 3, 110, 90, 35, 40);
            dm.getUserDebates(debateIDs);
            ////////////////////////TEST CASE FOR USER_MANAGER////////////////////////
            //////////////////////////////////////////////////////////////////////////
        }
        
        catch (SQLException e) {
            
            System.out.println("Cannot connect the database!");
            e.printStackTrace();
        }
    }
}
