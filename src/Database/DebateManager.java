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

public class DebateManager {
    
    private Statement s;
    private ResultSet rs;
    private String debateData;
    private final Connection c;
    private final String url = "jdbc:mysql://localhost:3306/diDatabase";
    private final String username = "root";
    private final String password = "dicomp";
    
    public DebateManager(Connection c) throws SQLException {

        this.c = c;
        s = c.createStatement();
        c.setAutoCommit(true);
    }
    
    public Debate getDebate(int dID) throws SQLException {

        int debateID = 0;
        int yesVotes = 0;
        int noVotes = 0;
        int stage1Length = 0;
        int stage2Length = 0;
        int stage3Length = 0;
        
        debateData = "SELECT * FROM debates";

        s = c.createStatement();
        ResultSet rs = s.executeQuery(debateData);

        int index = 0;

        while (rs.next()) {
            
            if(rs.getInt(1) == dID) {
            
                debateID = rs.getInt(1);
                yesVotes = rs.getInt(2);
                noVotes = rs.getInt(3);
                stage1Length = rs.getInt(4);
                stage2Length = rs.getInt(5);
                stage3Length = rs.getInt(6);

                /*String output = "User #%d: %i - %i - %i - %i";
                System.out.println(String.format(output, ++index, debateID, yesVotes, noVotes, stage1Length, stage2Length, stage3Length));*/
                
                break;
            }
        }
        
        return (new Debate(null, null, debateID, 0, yesVotes, noVotes, stage1Length, stage2Length, stage3Length));
    }
    
    public Debate[] getUserDebates(int[] debateIDs) throws SQLException {

        debateData = "SELECT * FROM debates";

        s = c.createStatement();
        rs = s.executeQuery(debateData);

        Debate[] debates = new Debate[10];
        
        int index = 0;

        while (rs.next()) {
            
            for(int i = 0; i < debateIDs.length; i++) {
                
                if(rs.getInt(1) == debateIDs[i]) {
                    
                    int debateID = rs.getInt(1);
                    int yesVotes = rs.getInt(2);
                    int noVotes = rs.getInt(3);
                    int stage1Length = rs.getInt(4);
                    int stage2Length = rs.getInt(5);
                    int stage3Length = rs.getInt(6);

                    debates[index] = new Debate(null, null, debateID, 0, yesVotes, noVotes, stage1Length, stage2Length, stage3Length);
                    
                    /*String output = "User #%d: %i - %i - %i - %i";
                    System.out.println(String.format(output, ++index, debateID, yesVotes, noVotes, stage1Length, stage2Length, stage3Length));*/
                }
            }
            
        }
        
        return debates;
    }
    
    public void InsertDebate(Debate debate) throws SQLException {
        
        int debateID = debate.getDebateID();
        int yesVotes = debate.getYesVotes();
        int noVotes = debate.getNoVotes();
        int stage1Length = debate.getStage1Length();
        int stage2Length = debate.getStage2Length();
        int stage3Length = debate.getStage3Length();
        
        debateData = "INSERT INTO debates (debateID, yesVotes, noVotes, stage1Length, stage2Length, stage3Length) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = c.prepareStatement(debateData);
        
        statement.setInt(1, 00001);
        statement.setInt(2, 3);
        statement.setInt(3, 1);
        statement.setInt(4, 1000);
        statement.setInt(5, 1000);
        statement.setInt(6, 750);
        
        int rowsInserted = statement.executeUpdate();
        
        if (rowsInserted > 0) {
            System.out.println("A new debate was inserted successfully!");
        }
    }
}
