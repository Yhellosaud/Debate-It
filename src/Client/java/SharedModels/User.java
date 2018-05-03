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
    private ArrayList<Integer> votedDebateIDs;
    private Avatar selectedAvatar;

    public User() {
        userID = 0;
        username = "";
        password = "";
        pastDebateIDs = new ArrayList<Integer>();
        votedDebateIDs = new ArrayList<Integer>();
        selectedAvatar = new Avatar();
    }

    public User(String username, String password, int userID, ArrayList<Integer> pastDebateIDs, ArrayList<Integer> votedDebates, Avatar selectedAvatar) {
        this.username = username;
        this.password = password;
        this.votedDebateIDs = votedDebates;
        this.pastDebateIDs = pastDebateIDs;
        this.userID = userID;
        this.selectedAvatar = selectedAvatar;
    }

    public int getUserID() {
        return userID;
    }

    public Avatar getSelectedAvatar() {
        return selectedAvatar;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Integer> getPastDebateIDs() {
        return pastDebateIDs;
    }

    public void addPastDebateID(int pastDebateID) {
        pastDebateIDs.add(pastDebateID);
    }

    public ArrayList<Integer> getVotedDebateIDs() {
        return votedDebateIDs;
    }

    public void addVotedDebateID(int votedDebateID) {
        votedDebateIDs.add(votedDebateID);
    }

    //Debug Constructor
    public User(String username, String password, Avatar selectedAvatar) {
        this.selectedAvatar = selectedAvatar;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", pastDebateIDs=" + pastDebateIDs +
                ", votedDebates=" + votedDebateIDs +
                '}';
    }
}
