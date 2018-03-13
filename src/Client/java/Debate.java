package dicomp.debateit;

import java.io.Serializable;

/**
 * Created by Yasin on 9.03.2018.
 */

public class Debate implements Serializable {
    private Idea idea;
    private Player[] players;
    private int debateID;
    private long debateLength;
    private int yesVotes;
    private int noVotes;
    private int stage1Length;
    private int stage2Length;
    private int stage3Length;

    public Debate(Idea idea, Player[] players, int debateID, long debateLength, int yesVotes, int noVotes, int stage1Length, int stage2Length, int stage3Length) {
        this.idea = idea;
        this.players = players;
        this.debateID = debateID;
        this.debateLength = debateLength;
        this.yesVotes = yesVotes;
        this.noVotes = noVotes;
        this.stage1Length = stage1Length;
        this.stage2Length = stage2Length;
        this.stage3Length = stage3Length;
    }
}
