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

    public Idea getIdea() {
        return idea;
    }

    public void setIdea(Idea idea) {
        this.idea = idea;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public int getDebateID() {
        return debateID;
    }

    public void setDebateID(int debateID) {
        this.debateID = debateID;
    }

    public long getDebateLength() {
        return debateLength;
    }

    public void setDebateLength(long debateLength) {
        this.debateLength = debateLength;
    }

    public int getYesVotes() {
        return yesVotes;
    }

    public void setYesVotes(int yesVotes) {
        this.yesVotes = yesVotes;
    }

    public int getNoVotes() {
        return noVotes;
    }

    public void setNoVotes(int noVotes) {
        this.noVotes = noVotes;
    }

    public int getStage1Length() {
        return stage1Length;
    }

    public void setStage1Length(int stage1Length) {
        this.stage1Length = stage1Length;
    }

    public int getStage2Length() {
        return stage2Length;
    }

    public void setStage2Length(int stage2Length) {
        this.stage2Length = stage2Length;
    }

    public int getStage3Length() {
        return stage3Length;
    }

    public void setStage3Length(int stage3Length) {
        this.stage3Length = stage3Length;
    }
}
