package dicomp.debateit;

import java.io.Serializable;

/**
 * Created by Yasin on 9.03.2018.
 */

public class Player implements Serializable {
    public final int SIDE_NEGATIVE = -1;
    public final int SIDE_POSITIVE = 1;
    public final int SIDE_SPECTATOR = 0;
    public final int VOTE_YES = 2;
    public final int VOTE_NO = 3;
    private int id;
    private String username;
    private int side;
    private String[] arguments;
    private int vote;
    private int consecutiveGamesPlayed;

    public Player(int id, String username, int side, String[] arguments, int vote, int consecutiveGamesPlayed) {
        this.id = id;
        this.username = username;
        this.side = side;
        this.arguments = arguments;
        this.vote = vote;
        this.consecutiveGamesPlayed = consecutiveGamesPlayed;
    }
}
