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
    private ArrayList<Integer> playedDebateIDs;
    private ArrayList<Integer> votedDebateIDs;
    private Avatar selectedAvatar;
	private Title selectedTitle;
    public User() {
        userID = 0;
        username = "";
        password = "";
        playedDebateIDs = new ArrayList<Integer>();
        votedDebateIDs = new ArrayList<Integer>();
        selectedAvatar = new Avatar();
        selectedTitle = new Title();
    }

    public User(String username, String password, int userID, ArrayList<Integer> playedDebateIDs, ArrayList<Integer> votedDebates, Avatar selectedAvatar, Title selectedTitle) {
        this.username = username;
        this.password = password;
        this.votedDebateIDs = votedDebates;
        this.playedDebateIDs = playedDebateIDs;
        this.userID = userID;
        this.selectedAvatar = selectedAvatar;
		this.selectedTitle = selectedTitle;
    }

    public Title getSelectedTitle() {
        return selectedTitle;
    }

    public void changeSelectedAvatar(Avatar selectedAvatar) {
        this.selectedAvatar = selectedAvatar;
    }

    public void changeSelectedTitle(Title selectedTitle) {
        this.selectedTitle = selectedTitle;
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

    public ArrayList<Integer> getPlayedDebateIDs() {
        return playedDebateIDs;
    }

    public void addPlayedDebateID(int playedDebateID) {
        playedDebateIDs.add(playedDebateID);
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
                ", playedDebateIDs=" + playedDebateIDs +
                ", votedDebates=" + votedDebateIDs +
                '}';
    }
}
