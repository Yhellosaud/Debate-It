package SharedModels;

import java.io.Serializable;
import java.util.Arrays;

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
    
    private int id;
    private String username;
    private int side;
    private String[] arguments;
    private int currentArguments;
    private int vote;
    private volatile int consecutiveGamesWatched;
    private volatile int gamesPlayedInSession;

    public Player(String username) {
        this.username = username;
    }

    public Player(int id, String username, int side, String[] arguments, int currentArguments, int vote, int consecutiveGamesPlayed) {
        this.id = id;
        this.username = username;
        this.side = side;
        this.arguments = arguments;
        this.currentArguments = currentArguments;
        this.vote = vote;
        this.consecutiveGamesWatched = consecutiveGamesPlayed;
    }

    public Player(int id, String username, int consecutiveGamesPlayed) {
        this.id = id;
        this.username = username;
        this.consecutiveGamesWatched = consecutiveGamesPlayed;
        currentArguments = 0;
        arguments = new String[4];
    }

    public Player(User user){
        this.id = user.getUserID();
        this.username = user.getUsername();
        consecutiveGamesWatched = 0;
        currentArguments = 0;
        arguments = new String[4];
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
    
    public void addArgument(String argument){
        arguments[currentArguments] = argument;
        currentArguments++;
    }

    @Override
    public String toString() {
        return username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
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

    
}
