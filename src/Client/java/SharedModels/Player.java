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
    private Avatar selectedAvatar;
    private Title selectedTitle;

    public Player() {
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
    public Player(String username) {
        this.username = username;

    }

    public Player(User user) {
        this.playerID = user.getUserID();
        this.username = user.getUsername();
        side = SIDE_SPECTATOR;
        arguments = new ArrayList<Argument>();
        vote = VOTE_YES;
        consecutiveGamesWatched = 0;
        gamesPlayedInSession = 0;
        selectedAvatar = user.getSelectedAvatar();
        selectedTitle = user.getSelectedTitle();
    }

    public void setAsSpectator() {
        setSide(SIDE_SPECTATOR);
    }

    public void setAsPositiveDebater() {
        setSide(SIDE_POSITIVE);
    }

    public void setAsNegativeDebater() {
        setSide(SIDE_NEGATIVE);
    }

    public int getGamesPlayedInSession() {
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
        if (arguments.size() < 4) {

            if (arguments.size() == 0) {
                arguments.add(argument);
                return;
            }
            if (arguments.get(arguments.size() - 1).getStage() != argument.getStage()) {
                arguments.add(argument);
            }

        }

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

    public void incrementConsecutiveGamesWatched() {
        consecutiveGamesWatched++;
    }

    public void incrementGamesPlayedInSession() {
        gamesPlayedInSession++;
    }

    public void setConsecutiveGamesWatched(int consecutiveGamesPlayed) {
        this.consecutiveGamesWatched = consecutiveGamesPlayed;
    }

    @Override
    public String toString() {
        String str = "PlayerID: " + playerID + " Username: " + username + " side: " + side + " vote: " + vote + " consecutiveGamesWatched: " + consecutiveGamesWatched
                + " gamesPlayedInSession: " + gamesPlayedInSession + " selectedAvatar: " + selectedAvatar + " selectedTitle: " + selectedTitle + "\n";
        str += "Arguments: " + "\n";
        for (int i = 0; i < arguments.size(); i++) {
            str += arguments.get(i);
        }

        return str;
    }

    public Avatar getSelectedAvatar() {
        return selectedAvatar;
    }

    public Title getSelectedTitle() {
        return selectedTitle;
    }

}
