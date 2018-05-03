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
import java.sql.ResultSetMetaData;
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
    private ResultSet rs1, rs2, rs3;
    private String userData1, userData2, userData3;
    private ResultSetMetaData rsmd1, rsmd2, rsmd3; 
    private PreparedStatement statement1, statement2, statement3;
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
        String password = " ";
        ArrayList<Integer> pastDebateIDs = new ArrayList<Integer>();
        ArrayList<Integer> votedDebates = new ArrayList<Integer>();
        
        userData1 = "SELECT * FROM user";
        userData2 = "SELECT * FROM user_pastdebateid";
        userData3 = "SELECT * FROM user_voteddebateid";

        s = c.createStatement();
        rs1 = s.executeQuery(userData1);
        rsmd1 = rs1.getMetaData();
        
        s = c.createStatement();
        rs2 = s.executeQuery(userData2);
        rsmd2 = rs2.getMetaData();
        
        s = c.createStatement();
        rs3 = s.executeQuery(userData3);
        rsmd3 = rs3.getMetaData();
        
        int index = 0;

        while (rs1.next()) {
            ///////////////////////////////
            /////////TABLE DISPLAY/////////
            ///////////////////////////////
            for(int i = 1; i <= rsmd1.getColumnCount(); i++) { 
                if (i > 1) { 
                    System.out.print(",  ");
                }
                
                System.out.print(rsmd1.getColumnName(i) + ": " + rs1.getString(i));
                
                while(rs2.next()) {

                    System.out.print(",  ");
                    System.out.print(rsmd2.getColumnName(2) + ": " + rs2.getString(2));
                }
                
                while(rs3.next()) {

                    System.out.print(",  ");
                    System.out.print(rsmd3.getColumnName(2) + ": " + rs3.getString(2));
                }
            }
            
            System.out.println("");
            ///////////////////////////////
            /////////TABLE DISPLAY/////////
            ///////////////////////////////
            //Searched user data is found and retrieved respectively
            if(rs1.getString(2).equals(username)) {

                userID = rs1.getInt(1);
                password = rs1.getString(3);
                //pastDebateIDs[index] = rs.getInt(4);
                //votedDebates = rs.getString(5);
                index++;
                /*String output = "User #%d: %i - %i - %i - %i";
                System.out.println(String.format(output, ++index, debateID, yesVotes, noVotes, stage1Length, stage2Length, stage3Length));*/
            }     
        }
        
        return (new User(username, password, userID, null, null));
    }
    
    public void InsertUser(User user) throws SQLException {
        
        //Each single information data of user is attained one by one
        int id = user.getUserID();
        String username = user.getUsername();
        String password = user.getPassword();
        ArrayList<Integer> pastDebateIDs = user.getPastDebateIDs();
        ArrayList<Integer> votedDebateIDs = user.getVotedDebates();
        
        //Related database tables are prepared in order to perform insertion
        userData1 = "INSERT INTO user (id, username, password) VALUES (?, ?, ?)";
        userData2 = "INSERT INTO user_pastdebateid (userID, debateID) VALUES (?, ?)"; 
        userData3 = "INSERT INTO user_pastdebateid (userID, debateID) VALUES (?, ?)";
        
        //The table is transfered into a prepared statement
        statement1 = c.prepareStatement(userData1);
        statement2 = c.prepareStatement(userData2); 
        statement3 = c.prepareStatement(userData3); 
        
        /*Integer[] pdIDs = new Integer[pastDebateIDs.size()];
        pdIDs = pastDebateIDs.toArray(pdIDs);*/
        
        //Statement variants are loaded with user information
        statement1.setInt(1, id);
        statement1.setString(2, username);
        statement1.setString(3, password);
        
        statement1.executeBatch();
        statement1.executeUpdate();

        for(int i = 0; i < pastDebateIDs.size(); i++) {
            
            statement2.setInt(1, id);
            statement2.setInt(2, pastDebateIDs.get(i));
        }
        
        statement2.executeBatch();
        statement2.executeUpdate();
        
        for(int i = 0; i < pastDebateIDs.size(); i++) {
            
            statement3.setInt(1, id);
            statement3.setInt(2, votedDebateIDs.get(i));
        }
        
        statement3.executeBatch();
        statement3.executeUpdate();

        /*Iterator<Integer> it = pastDebateIDs.iterator();
        
        while(it.hasNext()){
            
            int pastDebateID = it.next();
            statement.setInt(4, pastDebateID);
            statement.addBatch();                      
        }

        int[] numUpdates = statement.executeBatch();
        
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
        }*/
    }
}
