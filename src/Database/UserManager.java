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
import SharedModels.*;

/**
 *
 * @author Yhellosaud
 */
public class UserManager {
    
    private Statement s;
    private ResultSet rs;
    private String userData;
    private final Connection c;
    private final String url = "jdbc:mysql://localhost:3306/diDatabase";
    private final String username = "root";
    private final String password = "dicomp";
    
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
        int points = 0;
        String selectedAvatar = "";
        String selectedTitle = "";
        String selectedFrame = "";
        int pastDebateIDs = 0;
        String myInventory = "";
        String votedDebates = "";
        
        userData = "SELECT * FROM debates";

        s = c.createStatement();
        rs = s.executeQuery(userData);

        int index = 0;

        while (rs.next()) {

            if(rs.getString(2).equals(username)) {

                userID = rs.getInt(1);
                password = rs.getString(3);
                points = rs.getInt(4);
                selectedAvatar = rs.getString(5);
                selectedTitle = rs.getString(6);
                selectedFrame = rs.getString(7);
                pastDebateIDs = rs.getInt(8);
                myInventory = rs.getString(9);
                votedDebates = rs.getString(10);

                /*String output = "User #%d: %i - %i - %i - %i";
                System.out.println(String.format(output, ++index, debateID, yesVotes, noVotes, stage1Length, stage2Length, stage3Length));*/
            }     
        }
        
        return (new User(username, password, userID, null, null));
    }
    
    public void InsertUser(User user) throws SQLException {
        
        String username = user.getUsername();
        String password = user.getPassword();
        int userID = user.getPoints();
        String pastDebateIDs = "";
        String votedDebates = "";
        
        userData = "INSERT INTO debates (username, password, "
                + "userID, pastDebateIDs, votedDebates) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement statement = c.prepareStatement(userData);
        
        /*statement.setString(2, "Eda Hanım");
        statement.setString(3, "141511");
        statement.setInt(1, 001);
        statement.setString(5, "");
        statement.setString(6, "");*/
        
        statement.setString(1, username);
        statement.setString(2, password);
        statement.setInt(3, userID);
        statement.setString(4, pastDebateIDs);
        statement.setString(5, votedDebates);
        
        int rowsInserted = statement.executeUpdate();
        
        if (rowsInserted > 0) {
            System.out.println("A new user was inserted successfully!");
        }        
    }
}
