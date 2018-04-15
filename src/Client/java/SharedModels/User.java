package SharedModels;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Yasin on 9.03.2018.
 */

public class User implements Serializable {

    private static final long serialVersionUID = 1L;


    private int userID;
    private String username;
    private String password;
    private ArrayList<Integer> pastDebateIDs;
    private ArrayList<Integer> votedDebates;

    public User(String username, String password, int userID, ArrayList<Integer> pastDebateIDs, ArrayList<Integer> votedDebates){
        this.username = username;
        this.password = password;
        this.votedDebates = votedDebates;
        this.pastDebateIDs = pastDebateIDs;
    }
    
    public User(String username, String password) {

        this.username = username;
        this.password = password;
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

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", pastDebateIDs=" + pastDebateIDs +
                ", votedDebates=" + votedDebates +
                '}';
    }
}
