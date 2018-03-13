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
}
