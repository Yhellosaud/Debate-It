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
            //////////////////////////////////////////////////////////////////////////
            UserManager um = new UserManager(c);
            ArrayList<Integer> playedDebateIDs = new ArrayList<>(Arrays.asList(3, 5, 8, 17, 1, 2));
            ArrayList<Integer> votedDebateIDs = new ArrayList<>(Arrays.asList(31, 71, 6, 10, 14));
            
            um.signUp("Eda Hanım", "141511");
            um.signUp("Ayşe Çavuş", "401230");
            um.signUp("Mehmet Öğretmen", "22234");
            um.signUp("Ali Kasap", "88adaj21");
            um.signUp("Komşu Hüseyin", "bentekim68");
            
            System.out.println();
            
            um.addPlayedDebateID("Eda Hanım", 15);
            um.addPlayedDebateID("Eda Hanım", 40);
            um.addPlayedDebateID("Eda Hanım", 20);
            um.addPlayedDebateID("Ayşe Çavuş", 112);
            um.addPlayedDebateID("Ayşe Çavuş", 5);
            um.addPlayedDebateID("Ayşe Çavuş", 62);
            um.addPlayedDebateID("Ayşe Çavuş", 8);
            um.addPlayedDebateID("Ayşe Çavuş", 41);
            um.addPlayedDebateID("Komşu Hüseyin", 7);
            um.addPlayedDebateID("Komşu Hüseyin", 4);
              
            um.addVotedDebateID("Eda Hanım", 9);
            um.addVotedDebateID("Eda Hanım", 18);
            um.addVotedDebateID("Eda Hanım", 27);
            um.addVotedDebateID("Eda Hanım", 36);
            um.addVotedDebateID("Eda Hanım", 45);
            um.addVotedDebateID("Ali Kasap", 6);
            um.addVotedDebateID("Ali Kasap", 12);
            um.addVotedDebateID("Ali Kasap", 19);
            um.addVotedDebateID("Ali Kasap", 34);
            um.addVotedDebateID("Ali Kasap", 23);
            um.addVotedDebateID("Ali Kasap", 117);
            um.addVotedDebateID("Ali Kasap", 5);
            
            um.setSelectedAvatar("Mehmet Öğretmen", new Avatar(31));
            um.setSelectedAvatar("Komşu Hüseyin", new Avatar(40));
            um.setSelectedAvatar("Mehmet Öğretmen", new Avatar(9));
            um.setSelectedAvatar("Ayşe Çavuş", new Avatar(1));
            
            System.out.println();
            
            um.displayAllUsers();
            
            System.out.println("\nNUMBER OF USERS: " + um.getNumberOfUsers());
            //////////////////////////////////////////////////////////////////////////
            ////////////////////////TEST CASE FOR ITEM MANAGER////////////////////////
            //////////////////////////////////////////////////////////////////////////
            
            
            //////////////////////////////////////////////////////////////////////////
            ////////////////////////TEST CASE FOR DEBATE MANAGER//////////////////////
            //////////////////////////////////////////////////////////////////////////
            /*DebateManager dm = new DebateManager(c);
            ArrayList<Player> players = new ArrayList<>();
            Idea idea = new Idea(1, "Anam bacım kardeşim midir", 8);
            int[] debateIDs = {3, 13, 31, 193, 131, 40};
            Debate debate = new Debate(idea, players, 1, 80, 1, 3, 110, 90, 35, 40);
            dm.getUserDebates(debateIDs);*/
            //////////////////////////////////////////////////////////////////////////
            ////////////////////////TEST CASE FOR DEBATE_MANAGER////////////////////////
            //////////////////////////////////////////////////////////////////////////
        }
        
        catch (SQLException e) {
            
            System.out.println("Cannot connect the database!");
            e.printStackTrace();
        }
    }
}
