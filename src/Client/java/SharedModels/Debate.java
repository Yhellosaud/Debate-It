package SharedModels;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Yasin on 9.03.2018.
 */
public class Debate implements Serializable {

    private static final long serialVersionUID = 4L;

    private Idea idea;
    private ArrayList<Player> players;
    private int debateID;
    private int debateLength;
    private int yesVotes;
    private int noVotes;
    private int stage1Length;
    private int stage2Length;
    private int stage3Length;
    private int stage4Length;

    public Debate() {
        this.idea = new Idea();
        this.players = new ArrayList<Player>();
        this.debateID = 0;
        this.debateLength = 0;
        this.yesVotes = 0;
        this.noVotes = 0;
        this.stage1Length = 0;
        this.stage2Length = 0;
        this.stage3Length = 0;
        this.stage4Length = 0;
    }

    public Debate(Idea idea, ArrayList<Player> players, int yesVotes, int noVotes) {
        this.idea = idea;
        this.players = players;
        this.yesVotes = yesVotes;
        this.noVotes = noVotes;
    }

    public Debate(Idea idea, ArrayList<Player> players, int debateID, int debateLength, int yesVotes, int noVotes, int stage1Length, int stage2Length, int stage3Length, int stage4Length) {
        this.idea = idea;
        this.players = players;
        this.debateID = debateID;
        this.debateLength = debateLength;
        this.yesVotes = yesVotes;
        this.noVotes = noVotes;
        this.stage1Length = stage1Length;
        this.stage2Length = stage2Length;
        this.stage3Length = stage3Length;
        this.stage4Length = stage4Length;
    }

    public Debate(Idea idea, int debateID,ArrayList<Player> players) {
        this.idea = idea;
        this.debateID = debateID;
        
        if(players ==null){
            this.players = new ArrayList<Player>();
        }else{
            this.players = players;
        }        
        this.debateLength = 0;
        this.yesVotes = 0;
        this.noVotes = 0;
        this.stage1Length = 0;
        this.stage2Length = 0;
        this.stage3Length = 0;
        this.stage4Length = 0;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
    
    public void closeStage(int stage){
        Argument blankArgument = new Argument(0,stage,"Player passed this stage");
        for(int i=0;i<players.size();i++){
            players.get(i).addArgument(blankArgument);
        }
    }

    public int getProgress(){
        return 100 * yesVotes / (yesVotes + noVotes);
    }

    public void removePlayer(Player player) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getPlayerID() == player.getPlayerID()) {
                players.remove(i);
            }
        }
    }

    /**
     * This method adds an argument if the maximum number of arguments is not reached and 
     * if there is no argument with same stage.     
     * @param playerId
     * @param argument 
     */
    public void addArgument(int playerId, Argument argument) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getPlayerID() == playerId) {
                
                players.get(i).addArgument(argument);
                return;
            }
        }
    }

    public Idea getIdea() {
        return idea;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getDebateID() {
        return debateID;
    }

    public int getDebateLength() {
        return debateLength;
    }

    public int getYesVotes() {
        return yesVotes;
    }

    public int getNoVotes() {
        return noVotes;
    }

    public int getStage1Length() {
        return stage1Length;
    }

    public int getStage2Length() {
        return stage2Length;
    }

    public int getStage3Length() {
        return stage3Length;
    }

    public int getStage4Length() {
        return stage4Length;
    }

    public void setStage1Length(int stage1Length) {
        this.stage1Length = stage1Length;
    }

    public void setStage2Length(int stage2Length) {
        this.stage2Length = stage2Length;
    }

    public void setStage3Length(int stage3Length) {
        this.stage3Length = stage3Length;
    }

    public void setStage4Length(int stage4Length) {
        this.stage4Length = stage4Length;
    }

    public String menuDisplay() {
        return "Idea: " + idea.getStatement() + "\n" + "Players: " + players;
    }

    @Override
    public String toString() {
       String str = "------Debate--------\n";
        str +=  "Debate{"
                + "idea=" + idea                
                + ", debateID=" + debateID
                + ", debateLength=" + debateLength
                + ", yesVotes=" + yesVotes
                + ", noVotes=" + noVotes
                + ", stage1Length=" + stage1Length
                + ", stage2Length=" + stage2Length
                + ", stage3Length=" + stage3Length
                + '}'+"\n";
        System.out.println("Players");
        for(int i=0;i< players.size();i++){
            str +=players.get(i).toString();
        }
        str += "------Debate--------";
        return str;
    }

    public void closeDebate() {

        debateLength = stage1Length + stage2Length + stage3Length + stage4Length;

        synchronized (players) {
            for (int i = 0; i < players.size(); i++) {
                Player curPlayer = players.get(i);
                if (curPlayer.getSide() == Player.SIDE_SPECTATOR) {
                    curPlayer.incrementConsecutiveGamesWatched();

                } else {
                    curPlayer.setConsecutiveGamesWatched(0);
                    curPlayer.incrementGamesPlayedInSession();
                }
                curPlayer.setAsSpectator();
                curPlayer.getArguments().clear();
            }
        }
    }
    
    public void addYesVote(){
        yesVotes++;
        
    }
    public void addNoVote(){
        noVotes++;
    }

    public void printPlayers() {
        for (int i = 0; i < players.size(); i++) {
            System.out.println(players.get(i).toString());
        }
    }
}
