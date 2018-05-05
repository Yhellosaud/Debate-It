package SharedModels;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Yasin on 9.03.2018.
 */

public class Player implements Serializable {

    private static final long serialVersionUID = 2L;

    public static final int SIDE_NEGATIVE = -1;
    public static final int SIDE_POSITIVE = 1;
    public static final int SIDE_SPECTATOR = 0;
    public static final int VOTE_YES = 2;
    public static final int VOTE_NO = 3;
    
    private int playerID;
    private String username;
    private int side;
    private ArrayList<Argument> arguments;
    private int vote;
    private volatile int consecutiveGamesWatched;
    private volatile int gamesPlayedInSession;

    public Player(){
        playerID = 0;
        username = "";
        side = 0;
        arguments = new ArrayList<Argument>();
        vote = 0;
        consecutiveGamesWatched = 0;
        gamesPlayedInSession = 0;
    }

    public Player(int playerID, String username, int side, ArrayList<Argument> arguments, int vote, int consecutiveGamesWatched, int gamesPlayedInSession) {
        this.playerID = playerID;
        this.username = username;
        this.side = side;
        this.arguments = arguments;
        this.vote = vote;
        this.consecutiveGamesWatched = consecutiveGamesWatched;
        this.gamesPlayedInSession = gamesPlayedInSession;
    }

    //Debug Constructor
    public Player(String username){
        this.username = username;
    }

    public Player(User user){
        this.playerID = user.getUserID();
        this.username = user.getUsername();
        side = SIDE_SPECTATOR;
        arguments = new ArrayList<Argument>();
        vote = VOTE_YES;
        consecutiveGamesWatched = 0;
        gamesPlayedInSession = 0;
    }

    public void setAsSpectator(){
        setSide(SIDE_SPECTATOR);
    }
    
    public void setAsPositiveDebater(){
        setSide(SIDE_POSITIVE);
    }
    
    public void setAsNegativeDebater(){
        setSide(SIDE_NEGATIVE);
    }
    
    public int getGamesPlayedInSession(){
        return gamesPlayedInSession;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String getUsername() {
        return username;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public ArrayList<Argument> getArguments() {
        return arguments;
    }

    public void addArgument(Argument argument) {
        arguments.add(argument);
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getConsecutiveGamesWatched() {
        return consecutiveGamesWatched;
    }
    
    public void incrementConsecutiveGamesWatched(){
        consecutiveGamesWatched++;
    }
    
    public void incrementGamesPlayedInSession(){
        gamesPlayedInSession++;
    }

    public void setConsecutiveGamesWatched(int consecutiveGamesPlayed) {
        this.consecutiveGamesWatched = consecutiveGamesPlayed;
    }

    @Override
    public String toString() {
        return username;
    }
    
}
