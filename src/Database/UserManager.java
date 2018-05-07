/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqlconnection;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Connection;
import SharedModels.*;
import java.util.*;

/**
 *
 * @author Yhellosaud
 */
public class UserManager {
    

    private ResultSet rs1, rs2, rs3;
    private ResultSetMetaData rsmd1, rsmd2, rsmd3;
    private PreparedStatement statement;
    private final Connection c;
    private final Statement s1, s2, s3, s4;
    private final String str1, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11;
    
    public UserManager(Connection c) throws SQLException {

        this.c = c;
        
        s1 = c.createStatement();
        s2 = c.createStatement();
        s3 = c.createStatement();
        s4 = c.createStatement();
        
        c.setAutoCommit(true);
        
        str1 = "SELECT * FROM user";
        str2 = "SELECT * FROM user_playeddebateid";
        str3 = "SELECT * FROM user_voteddebateid";
        str4 = "INSERT INTO user (userID, username, password, avatarID, playedDebateNumber, votedDebateNumber) VALUES (?, ?, ?, ?, ?, ?)";
        str5 = "INSERT INTO user_playeddebateid (userID, debateID) VALUES (?, ?)";
        str6 = "INSERT INTO user_voteddebateid (userID, debateID) VALUES (?, ?)";
        str7 = "UPDATE user_playeddebateid SET debateID = ? WHERE userID = ?";
        str8 = "UPDATE user_voteddebateid SET debateID = ? WHERE userID = ?";
        str9 = "UPDATE user SET playedDebateNumber = ? WHERE userID = ?";
        str10 = "UPDATE user SET votedDebateNumber = ? WHERE userID = ?";
        str11 = "UPDATE user SET avatarID = ? WHERE userID = ?";
    }
    
    public boolean signUp(String username, String password) throws SQLException {
                
        rs1 = s1.executeQuery(str1);
        
        int userID = 0;
        
        while(rs1.next()) {

            if(username.equals(rs1.getString(2))) {
                
                return false;
            }
            
            userID = rs1.getInt(1);
        }
        
        insertUser(new User(username, password, ++userID, new ArrayList<>(), new ArrayList<>(), new Avatar()));
        System.out.println("User '" + username + "' has been successfully signed up!");
        return true;
    }
    
    public User signIn(String username, String password) throws SQLException {
        
        rs1 = s1.executeQuery(str1);

        while(rs1.next()) {

            if(username.equals(rs1.getString(2)) && password.equals(rs1.getString(3))) {
                
                System.out.println("User '" + username + "' has been successfully signed in!");
                return getUser(username);
            }
        }

        return null;
    }
    
    public boolean addPlayedDebateID(String username, int debateID) throws SQLException {
        
        rs1 = s1.executeQuery(str1);
        String ids = "";
        int userID = 0;
        int playedDebateNumber = 0;
        
        while(rs1.next()) {
            if(username.equals(rs1.getString(2))) {
                userID = rs1.getInt(1);
                playedDebateNumber = rs1.getInt(5);
                break;
            }
        }
        String[] pdids = new String[playedDebateNumber];
        // Played debateID's of user are retrieved
        rs2 = s2.executeQuery(str2);
        while(rs2.next()) {
            if(rs2.getInt(1) == userID) {   
                ids = rs2.getString(2);
                pdids = ids.split(", ");
            }
        }
        for(int i = 0; i < pdids.length; i++) {  
            if(pdids[i].equals("" + debateID)) {
                return false;
            }
        }

        ids += "" + debateID + ", ";
        
        statement = c.prepareStatement(str7);
        statement.setString(1, ids);
        statement.setInt(2, userID);
        statement.executeBatch();
        statement.executeUpdate();
        
        statement = c.prepareStatement(str9);
        statement.setInt(1, playedDebateNumber + 1);
        statement.setInt(2, userID);
        statement.executeBatch();
        statement.executeUpdate();
                
        System.out.println("Played debate with the id '" + debateID + "' has been successfully inserted to user '" + username + "'!");
        return true;
    }
    
    public boolean addVotedDebateID(String username, int debateID) throws SQLException {
        
        rs1 = s1.executeQuery(str1);
        String ids = "";
        int userID = 0;
        int votedDebateNumber = 0;
        
        while(rs1.next()) {
            if(username.equals(rs1.getString(2))) {
                userID = rs1.getInt(1);
                votedDebateNumber = rs1.getInt(6);
                break;
            }
        }
        String[] vdids = new String[votedDebateNumber];
        // Voted debateID's of user are retrieved
        rs3 = s3.executeQuery(str3);
        while(rs3.next()) {
            if(rs3.getInt(1) == userID) {   
                ids = rs3.getString(2);
                vdids = ids.split(", ");
            }
        }
        for(int i = 0; i < vdids.length; i++) {  
            if(vdids[i].equals("" + debateID)) {
                return false;
            }
        }

        ids += "" + debateID + ", ";

        statement = c.prepareStatement(str8);
        statement.setString(1, ids);
        statement.setInt(2, userID);
        statement.executeBatch();
        statement.executeUpdate();
        
        statement = c.prepareStatement(str10);
        statement.setInt(1, votedDebateNumber + 1);
        statement.setInt(2, userID);
        statement.executeBatch();
        statement.executeUpdate();
                
        System.out.println("Played debate with the id '" + debateID + "' has been successfully inserted to user '" + username + "'!");
        return true;
    }
    
    public boolean setSelectedAvatar(String username, Avatar selectedAvatar) throws SQLException {
        int avatarID = selectedAvatar.getAvatarID();
        rs1 = s1.executeQuery(str1);
        while(rs1.next()) {
            if(username.equals(rs1.getString(2))) {
                statement = c.prepareStatement(str11);
                statement.setInt(1, avatarID);
                statement.setInt(2, rs1.getInt(1));
                statement.executeBatch();
                statement.executeUpdate();
                System.out.println("Avatar with ID '" + avatarID + "' of the user '" + username + "' has been successfully updated!");
                return true;
            }
        }
        return false;
    }
    
    public int getNumberOfUsers() throws SQLException {
        
        rs1 = s1.executeQuery(str1);
        
        int numberOfUsers = 0;

        while(rs1.next()) {

            numberOfUsers++;
        }
        
        return numberOfUsers;
    }
    
    public void displayAllUsers() throws SQLException {
        
        try {

            rsmd1 = rs1.getMetaData();
            rsmd2 = rs2.getMetaData();
            rsmd3 = rs3.getMetaData();

            rs1 = s1.executeQuery(str1);
            rs2 = s2.executeQuery(str2);
            rs3 = s3.executeQuery(str3);
            
            while (rs1.next()) {
                rs2.next();
                rs3.next();
                //Table display
                System.out.println("\n------------------------");
                for(int i = 1; i <= rsmd1.getColumnCount(); i++) {

                    if (i > 1) { 
                        System.out.print("->  ");
                    }
                    
                    System.out.println(rsmd1.getColumnName(i) + ": " + rs1.getString(i));
                }
                
                System.out.print("-> played debates: " + rs2.getString(2));
                System.out.println("");

                System.out.print("-> voted debates: " + rs3.getString(2));
                System.out.println("\n------------------------\n");
                

            }
        }
        
        catch (NullPointerException e) {
            
        }
    }
    
    public User getUser(String username) throws SQLException {
        
        int userID = 0;
        int playedDebateNumber = 0;
        int votedDebateNumber = 0;
        String password = " ";
        Avatar selectedAvatar = null;
        ArrayList<Integer> playedDebateIDs = new ArrayList<>();
        ArrayList<Integer> votedDebateIDs = new ArrayList<>();
        String[] pdids = new String[playedDebateIDs.size()];
        String[] vdids = new String[votedDebateIDs.size()];
        // User details are retrieved
        rs1 = s1.executeQuery(str1);
        while (rs1.next()) {
            if(rs1.getString(2).equals(username)) {
                userID = rs1.getInt(1);
                password = rs1.getString(3);
                selectedAvatar = new Avatar(rs1.getInt(4));
                playedDebateNumber = rs1.getInt(5);
                votedDebateNumber = rs2.getInt(6);
            }
        }
        // Played debateID's of user are retrieved
        rs2 = s2.executeQuery(str2);
        while(rs2.next()) {
            if(rs2.getInt(1) == userID) {   
                String ids = rs2.getString(2);
                pdids = ids.split(", ");
            }
        }
        for(int i = 0; i < playedDebateNumber; i++) {
            playedDebateIDs.add(i, Integer.parseInt(pdids[i]));
        }
        // Voted debateID's of user are retrieved
        rs3 = s3.executeQuery(str3);
        while(rs3.next()) {
            if(rs3.getInt(1) == userID) {   
                String ids = rs3.getString(2);
                vdids = ids.split(", ");
            }
        }
        for(int i = 0; i < votedDebateNumber; i++) {
            votedDebateIDs.add(i, Integer.parseInt(vdids[i]));
        }
        
        return (new User(username, password, userID, playedDebateIDs, votedDebateIDs, selectedAvatar));
    }
    
    public void insertUser(User user) throws SQLException {
        
        String pdids = "";
        String vdids = "";
        
        for(int i = 0; i < user.getPlayedDebateIDs().size(); i++) {
            
            pdids += "" + user.getPlayedDebateIDs().get(i) + ", ";
        }
        for(int i = 0; i < user.getVotedDebateIDs().size(); i++) {
        
            vdids += "" + user.getVotedDebateIDs().get(i) + ", ";  
        }
        //Related database tables are prepared in order to perform insertion
        //The table is transfered into a prepared statement
        //Statement variants are loaded with user information
        statement = c.prepareStatement(str4);
        statement.setInt(1, 0);
        statement.setString(2, user.getUsername());
        statement.setString(3, user.getPassword());
        statement.setInt(4, 0);
        statement.setInt(5, 0);
        statement.setInt(6, 0);
        statement.executeBatch();
        statement.executeUpdate();
        //////////////////////////////////////////////////////////////////////
        statement = c.prepareStatement(str5); 
        statement.setInt(1, 0);
        statement.setString(2, pdids);
        statement.executeBatch();
        statement.executeUpdate();
        //////////////////////////////////////////////////////////////////////
        statement = c.prepareStatement(str6);
        statement.setInt(1, 0);
        statement.setString(2, vdids);
        statement.executeBatch();
        statement.executeUpdate();
    }
}
