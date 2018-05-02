/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqlconnection;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Array;
import java.sql.Connection.*;
import SharedModels.*;
import java.util.*;
import java.lang.*;

/**
 *
 * @author Yhellosaud
 */
public class UserManager {
    
    private Statement s;
    private ResultSet rs;
    private String userData;
    private final Connection c;
    
    public UserManager(Connection c) throws SQLException {

        this.c = c;
        s = c.createStatement();
        c.setAutoCommit(true);
    }
    
    /*public boolean signUp(String username, String password) {
        
        
        return true;
    }
    
    public User signIn(String username, String password) {
        
        
    }*/
    
    public boolean checkUserName(String username) {
        
    
        return true;
    }
    
    public User getUserDetails(String username) throws SQLException {
        
        int userID = 0;
        String password = "";
        String votedDebates = "";
        
        userData = "SELECT * FROM user";

        s = c.createStatement();
        rs = s.executeQuery(userData);

        int index = 0;

        while (rs.next()) {

            if(rs.getString(2).equals(username)) {

                userID = rs.getInt(1);
                password = rs.getString(3);
                Integer[] pastDebateIDs = rs.getObject(4);
                votedDebates = rs.getString(5);

                /*String output = "User #%d: %i - %i - %i - %i";
                System.out.println(String.format(output, ++index, debateID, yesVotes, noVotes, stage1Length, stage2Length, stage3Length));*/
            }     
        }
        
        return (new User(username, password, userID, null, null));
    }
    
    public void InsertUser(User user) throws SQLException {
        
        int id = user.getUserID();
        String username = user.getUsername();
        String password = user.getPassword();
        ArrayList<Integer> pastDebateIDs = user.getPastDebateIDs();
        String votedDebates = "";
        
        userData = "INSERT INTO user (id, username, password, pastDebateIDs, votedDebates) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement statement = c.prepareStatement(userData);

        Integer[] pdIDs = new Integer[pastDebateIDs.size()];
        pdIDs = pastDebateIDs.toArray(pdIDs);
        
        //Array p = c.createArrayOf("VARCHAR", pastDebateIDs.toArray());
        
        statement.setInt(1, id);
        statement.setString(2, username);
        statement.setString(3, password);
        statement.setObject(4, pdIDs);
        statement.setString(5, votedDebates);

        Iterator<Integer> it = pastDebateIDs.iterator();
        
        while(it.hasNext()){
            
            int pastDebateID = it.next();
            statement.setInt(4, pastDebateID);
            statement.addBatch();                      
        }
        
        int [] numUpdates = statement.executeBatch();
        
        for (int i=0; i < numUpdates.length; i++) {

            if (numUpdates[i] == -2) {

                System.out.println("Execution " + i + 
                  ": unknown number of rows updated");
            }
            
            else {

                System.out.println("Execution " + i + 
                  "successful: " + numUpdates[i] + " rows updated");
            }
        }
        
        int rowsInserted = statement.executeUpdate();
        
        if (rowsInserted > 0) {
            System.out.println("A new user was inserted successfully!");
        }
    }
}
