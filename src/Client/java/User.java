package dicomp.debateit;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Yasin on 9.03.2018.
 */

public class User implements Serializable {
    private int userID;
    private String username;
    private String password;
    private int points;
    private ArrayList<Integer> pastDebateIDs;
    private ArrayList<Integer> votedDebates;

    public User(String username, String password, int userID, ArrayList<Integer> pastDebateIDs, ArrayList<Integer> votedDebates){
        this.username = username;
        this.password = password;
        this.points = points;
        this.votedDebates = votedDebates;
        this.pastDebateIDs = pastDebateIDs;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public ArrayList<Integer> getPastDebateIDs() {
        return pastDebateIDs;
    }

    public void setPastDebateIDs(ArrayList<Integer> pastDebateIDs) {
        this.pastDebateIDs = pastDebateIDs;
    }

    public ArrayList<Integer> getVotedDebates() {
        return votedDebates;
    }

    public void setVotedDebates(ArrayList<Integer> votedDebates) {
        this.votedDebates = votedDebates;
    }
    
}
