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
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import SharedModels.*;

/**
 *
 * @author Yhellosaud
 */

public class DebateManager {
    
    private Statement s;
    private ResultSet rs;
    private String debateData;
    private ResultSetMetaData rsmd;
    private PreparedStatement statement;
    private final Connection c;
    public int numberOfDebates;
    
    public DebateManager(Connection c) throws SQLException {

        this.c = c;
        s = c.createStatement();
        c.setAutoCommit(true);
        
        int numberOfDebates = 0;
    }
    
    public Debate getDebate(int dID) throws SQLException {
        
        int ideaID = 0;
        String statement = "";
        int category = 0;
        
        int debateLength = 0;
        int yesVotes = 0;
        int noVotes = 0;
        int stage1Length = 0;
        int stage2Length = 0;
        int stage3Length = 0;
        int stage4Length = 0;
            
        Idea idea = new Idea(ideaID, statement, category);
        ArrayList<Player> players = new ArrayList<Player>();
        
        debateData = "SELECT * FROM debate";

        s = c.createStatement();
        ResultSet rs = s.executeQuery(debateData);

        int index = 0;

        while (rs.next()) {
            
            if(rs.getInt(3) == dID) {

                debateLength = rs.getInt(2);
                yesVotes = rs.getInt(3);
                noVotes = rs.getInt(4);
                stage1Length = rs.getInt(5);
                stage2Length = rs.getInt(6);
                stage3Length = rs.getInt(7);
                stage4Length = rs.getInt(8);
                
                break;
            }
        }
                
        return (new Debate(idea, players, dID, debateLength, yesVotes, noVotes, stage1Length, stage2Length, stage3Length, stage4Length));
    }
    
    public Debate[] getUserDebates(int[] debateIDs) throws SQLException {
        
        int index = 0;
        int size = debateIDs.length;
        
        ArrayList<Player> players = new ArrayList<>();
        Debate[] debates = new Debate[size];
        Idea[] ideas = new Idea[size];
        
        debateData = "SELECT * FROM debate_idea";
        s = c.createStatement();
        rs = s.executeQuery(debateData);
        rsmd = rs.getMetaData();

        while (rs.next()) {
            //Searched user data is found and retrieved respectively
            for(int i = 0; i < debateIDs.length; i++) {
                
                if(rs.getInt(1) == debateIDs[i]) {

                    ideas[index++] = new Idea(rs.getInt(2), "", 0);
                }
            }
        }
        
        debateData = "SELECT * FROM idea";
        s = c.createStatement();
        rs = s.executeQuery(debateData);
        rsmd = rs.getMetaData();
        
        while(rs.next()) {
            //Table display
            for(int i = 1; i <= rsmd.getColumnCount(); i++) {

                if(i > 1) { 
                    System.out.print(",  ");
                }

                System.out.print(rsmd.getColumnName(i) + ": " + rs.getString(i));
            }
            System.out.println("");
            
            for(int i = 0; i < size; i++) {
                
                if(rs.getInt(1) == ideas[i].getIdeaID()) {
                    
                    ideas[i].setStatement(rs.getString(2));
                    ideas[i].setCategory(rs.getInt(3));
                }
            }
        }
        
        debateData = "SELECT * FROM player_argument";
        s = c.createStatement();
        rs = s.executeQuery(debateData);
        rsmd = rs.getMetaData();
        
        while(rs.next()) {
            //Table display
            for(int i = 1; i <= rsmd.getColumnCount(); i++) {

                if(i > 1) { 
                    System.out.print(",  ");
                }

                System.out.print(rsmd.getColumnName(i) + ": " + rs.getString(i));
            }
            System.out.println("");
            
            for(int i = 0; i < size; i++) {
                
                if(rs.getInt(1) == ideas[i].getIdeaID()) {
                    
                    ideas[i].setStatement(rs.getString(2));
                    ideas[i].setCategory(rs.getInt(3));
                }
            }
        }
        
        debateData = "SELECT * FROM player";
        s = c.createStatement();
        rs = s.executeQuery(debateData);
        rsmd = rs.getMetaData();
        
        while(rs.next()) {
            //Table display
            for(int i = 1; i <= rsmd.getColumnCount(); i++) {

                if(i > 1) { 
                    System.out.print(",  ");
                }

                System.out.print(rsmd.getColumnName(i) + ": " + rs.getString(i));
            }
            System.out.println("");
            
            for(int i = 0; i < size; i++) {
                
                if(rs.getInt(1) == ideas[i].getIdeaID()) {
                    
                    ideas[i].setStatement(rs.getString(2));
                    ideas[i].setCategory(rs.getInt(3));
                }
            }
        }
        
        debateData = "SELECT * FROM debate";
        s = c.createStatement();
        rs = s.executeQuery(debateData);
        rsmd = rs.getMetaData();
        
        index = 0;
        
        while (rs.next()) {
            //Table display
            for(int i = 1; i <= rsmd.getColumnCount(); i++) {

                if (i > 1) { 
                    System.out.print(",  ");
                }

                System.out.print(rsmd.getColumnName(i) + ": " + rs.getString(i));
            }
            System.out.println("");
            //Desired debate datas are found and retrieved respectively by id's
            for(int i = 0; i < debateIDs.length; i++) {
                
                if(rs.getInt(1) == debateIDs[i]) {
                    
                    int debateID = rs.getInt(1);
                    int debateLength = rs.getInt(2);
                    int yesVotes = rs.getInt(3);
                    int noVotes = rs.getInt(4);
                    int stage1Length = rs.getInt(5);
                    int stage2Length = rs.getInt(6);
                    int stage3Length = rs.getInt(7);
                    int stage4Length = rs.getInt(8);

                    debates[index] = new Debate(ideas[index++], players, debateID, debateLength, yesVotes, noVotes, stage1Length, stage2Length, stage3Length, stage4Length);
                }
            }    
        }
        
        return debates;
    }
    
    public Debate[] getAllDebates() throws SQLException {
        
        Debate[] debates = new Debate[getNumberOfDebates()];
        
        debateData = "SELECT * FROM debate";
        s = c.createStatement();
        rs = s.executeQuery(debateData);
        rsmd = rs.getMetaData();
        
        for(int i = 0; i < numberOfDebates; i++) {
            
            
        }
        
        return debates;
    }
    
    public void updateNumberOfDebates() {
        
        numberOfDebates = 0;
    }
    
    public int getNumberOfDebates() {

        return numberOfDebates;
    }
    
    public void insertDebate(Debate debate) throws SQLException {
        
        int idea = 2750;
        int players = 10;
        int debateID = debate.getDebateID();
        long debateLength = debate.getDebateLength();
        int yesVotes = debate.getYesVotes();
        int noVotes = debate.getNoVotes();
        int stage1Length = debate.getStage1Length();
        int stage2Length = debate.getStage2Length();
        int stage3Length = debate.getStage3Length();
        
        debateData = "INSERT INTO debate (idea, players, debateID, debateLength, yesVotes, noVotes, stage1Length, stage2Length, stage3Length) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = c.prepareStatement(debateData);
        
        statement.setInt(1, idea);
        statement.setInt(2, players);
        statement.setInt(3, debateID);
        statement.setLong(4, debateLength);
        statement.setInt(5, yesVotes);
        statement.setInt(6, noVotes);
        statement.setInt(7, stage1Length);
        statement.setInt(8, stage2Length);
        statement.setInt(9, stage3Length);
        
        int rowsInserted = statement.executeUpdate();
        
        if (rowsInserted > 0) {
            System.out.println("A new debate was inserted successfully!");
        }
    }
}
