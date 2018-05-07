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
    
    private Statement s;
    private ResultSet rs;
    private String userData;
    private ResultSetMetaData rsmd;
    private PreparedStatement statement;
    private final Connection c;
    private int numberOfUsers;
    
    public UserManager(Connection c) throws SQLException {

        this.c = c;
        s = c.createStatement();
        c.setAutoCommit(true);
        numberOfUsers = 0;
    }
    
    public boolean signUp(String username, String password) throws SQLException {
                
        userData = "SELECT * FROM user";
        s = c.createStatement();
        rs = s.executeQuery(userData);
        rsmd = rs.getMetaData();
        
        int userID = 0;
        
        while(rs.next()) {

            if(username.equals(rs.getString(2)) || password.equals(rs.getString(3))) {
                
                return false;
            }
            
            userID = rs.getInt(1);
        }
        
        insertUser(new User(username, password, ++userID, null, null, null));
        numberOfUsers++;
        
        return true;
    }
    
    public User signIn(String username, String password) throws SQLException {
        
        userData = "SELECT * FROM user";
        s = c.createStatement();
        rs = s.executeQuery(userData);
        rsmd = rs.getMetaData();
        
        int userID = 0;
        
        while(rs.next()) {

            if(username.equals(rs.getString(2)) && password.equals(rs.getString(3))) {
                
                
                return getUserDetails(username);
            }
        }

        return null;
    }
    
    public boolean setSelectedAvatar(String username, Avatar selectedAvatar) throws SQLException {
        
        
        userData = "SELECT * FROM user";
        s = c.createStatement();
        rs = s.executeQuery(userData);
        rsmd = rs.getMetaData();
        
        int userID = 0;
        
        while(rs.next()) {

            if(username.equals(rs.getString(2))) {

                statement.setInt(4, selectedAvatar.getAvatarID());
                return true;
            }
        }
        
        return false;
    }
    
    
    
    public boolean checkUserName(String username) {
        
    
        return true;
    }
    
    public int getNumberOfUsers() {
        
        return numberOfUsers;
    }
    
    public User getUserDetails(String username) throws SQLException {
        
        int userID = 0;
        String password = " ";
        Avatar selectedAvatar = null;
        ArrayList<Integer> playedDebateIDs = new ArrayList<Integer>();
        ArrayList<Integer> votedDebateIDs = new ArrayList<Integer>();
        
        userData = "SELECT * FROM user";
        s = c.createStatement();
        rs = s.executeQuery(userData);
        rsmd = rs.getMetaData();

        while (rs.next()) {
            //Table display
            for(int i = 1; i <= rsmd.getColumnCount(); i++) {

                if (i > 1) { 
                    System.out.print(",  ");
                }

                System.out.print(rsmd.getColumnName(i) + ": " + rs.getString(i));
            }
            System.out.println("");
            //Searched user data is found and retrieved respectively
            if(rs.getString(2).equals(username)) {

                userID = rs.getInt(1);
                password = rs.getString(3);
                selectedAvatar = new Avatar(rs.getInt(4));
            }
        }
            
        userData = "SELECT * FROM user_playeddebateid";
        rs = s.executeQuery(userData);
        rsmd = rs.getMetaData();

        String[] dids = new String[20];

        while(rs.next()) {

            System.out.print(",  ");
            System.out.println(rsmd.getColumnName(2) + " " + rs.getString(2));
            String ids = rs.getString(2);
            dids = ids.split(",");
        }

        for(int i = 0; i < dids.length; i++) {

            int id = Integer.parseInt(dids[i]);
            playedDebateIDs.add(i, id);

        }

        userData = "SELECT * FROM user_voteddebateid";
        rs = s.executeQuery(userData);
        rsmd = rs.getMetaData();

        while(rs.next()) {

            System.out.print(",  ");
            System.out.println(rsmd.getColumnName(2) + " " + rs.getString(2));
            String ids = rs.getString(2);
            dids = ids.split(",");
        }

        for(int i = 0; i < dids.length; i++) {

            int id = Integer.parseInt(dids[i]);
            votedDebateIDs.add(i, id);

        }
        
        return (new User(username, password, userID, playedDebateIDs, votedDebateIDs, selectedAvatar));
    }
    
    public void insertUser(User user) throws SQLException {
        
        //Each single information data of user is attained one by one
        int userID = user.getUserID();
        String username = user.getUsername();
        String password = user.getPassword();
        int avatarID = user.getSelectedAvatar().getAvatarID();
        ArrayList<Integer> pastDebateIDs = user.getPlayedDebateIDs();
        ArrayList<Integer> votedDebateIDs = user.getVotedDebateIDs();
        
        String pdids = "";
        String vdids = "";
        
        for(int i = 0; i < pastDebateIDs.size(); i++) {
            
            pdids += "" + pastDebateIDs.get(i) + ",";
        }
        for(int i = 0; i < votedDebateIDs.size(); i++) {
        
            vdids += "" + votedDebateIDs.get(i) + ",";
        }
        
        //Related database tables are prepared in order to perform insertion
        //The table is transfered into a prepared statement
        //Statement variants are loaded with user information
        userData = "INSERT INTO user (userID, username, password) VALUES (?, ?, ?)";
        statement = c.prepareStatement(userData); 
        statement.setInt(1, userID);
        statement.setString(2, username);
        statement.setString(3, password);
        statement.setInt(4, avatarID);
        statement.executeBatch();
        statement.executeUpdate();
        //////////////////////////////////////////////////////////////////////
        userData = "INSERT INTO user_playeddebateid (userID, debateID) VALUES (?, ?)";
        statement = c.prepareStatement(userData); 
        statement.setInt(1, userID);
        statement.setString(2, pdids);
        statement.executeBatch();
        statement.executeUpdate();
        //////////////////////////////////////////////////////////////////////
        userData = "INSERT INTO user_voteddebateid (userID, debateID) VALUES (?, ?)";
        statement = c.prepareStatement(userData);
        statement.setInt(1, userID);
        statement.setString(2, vdids);
        statement.executeBatch();
        statement.executeUpdate();
    }
}
