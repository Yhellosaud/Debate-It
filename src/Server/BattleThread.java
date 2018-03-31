package server;

/**
 *
 * @author Cagatay
 */
import SharedModels.*;
import java.net.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.io.Serializable;

public class BattleThread implements Runnable {

    private static final int STAGE_SIDE_SELECTION = 0;
    private static final int STAGE_INITIAL_ARGUMENTS = 1;
    private static final int STAGE_COUNTER_ARGUMENTS = 2;
    private static final int STAGE_ANSWERS = 3;
    private static final int STAGE_CONCLUSION = 4;
    private static final int STAGE_VOTING = 5;
    
    
    private static final int MAX_PLAYERS = 2;

    private static final int REQUEST_SEND_ARGUMENT = 9;

    private Debate currentDebate;
    private volatile ArrayList<PlayerHandler> playerHandlers;    
    private BattleTimer timer;
    private volatile int numPlayers;
    private volatile int currentStage;
    private volatile boolean running;

    public BattleThread() {

        running = true;

        playerHandlers = new ArrayList<PlayerHandler>();
        numPlayers = 0;
        currentStage = 0;
        timer = new BattleTimer(this, playerHandlers);

    }

    public synchronized void joinNewPlayer(Player newPlayer, Socket socket, ObjectOutputStream out, ObjectInputStream in) {

        if (numPlayers == MAX_PLAYERS) {
            System.out.println("Maximum number of players reached. Couldn't joined new player");
        } else {
            System.out.println("new player joined");
            /////////////////////////////Change This set id part ////////////////////////////////////////
            newPlayer.setId(numPlayers);
            numPlayers++;
            PlayerHandler newHandler = new PlayerHandler(newPlayer, socket, out, in, this);
            playerHandlers.add(newHandler);
            currentDebate.addPlayer(newPlayer);
            new Thread(newHandler).start();
        }

    }

    public synchronized void disconnectPlayer(Player player) {

        int playerId = player.getId();

        for (int i = 0; i < playerHandlers.size(); i++) {

            PlayerHandler curPlayerHandler = playerHandlers.get(i);
            int curPlayerId = curPlayerHandler.getPlayer().getId();

            //Terminating the player handler with matching id and removing from list
            if (playerId == curPlayerId) {
                curPlayerHandler.terminate();
                playerHandlers.remove(i);
                currentDebate.removePlayer(player);
            }
        }
        numPlayers--;
    }

    /**
     * This method calls updatePlayer method for each of the playerHandlers.
     *
     * @param responseId
     * @param responseData
     */
    public synchronized void updatePlayers(int responseId, ArrayList<Serializable> responseData) {

        synchronized (playerHandlers) {
            for (int i = 0; i < playerHandlers.size(); i++) {
                playerHandlers.get(i).updatePlayer(responseId, responseData);

            }
        }
    }

    public synchronized void updateDebate(int requestId, ArrayList<Serializable> requestData) {

        switch (requestId) {
            case (REQUEST_SEND_ARGUMENT):

                break;

        }

    }

    public void run() {

        System.out.println("Battle thread started\n");
        //Starting timer thread
        new Thread(timer).start();

        while (running) {

            //Waiting untill max number of players is reached
            while (numPlayers < MAX_PLAYERS) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
            }

            //Battle keeps repeating as long as max number of players is reached
            while (numPlayers == MAX_PLAYERS) {
                currentStage = 0;

                System.out.println("Game starting");

            }

            timer.stopTimer();
        }

    }
    
    private void playStageSideSelection(int stageTime){
        
        //Stage 0
        System.out.println("Stage side selection is starting");
        //Updating players
        ArrayList<Serializable> responseParams = new ArrayList<Serializable>();
        responseParams.add(STAGE_SIDE_SELECTION);
        updatePlayers(PlayerHandler.RESPONSE_NEW_STAGE, responseParams);
        responseParams.clear();
        //Starting timer
        timer.startTimer(stageTime);

        while (currentStage == STAGE_SIDE_SELECTION & numPlayers == MAX_PLAYERS) {
            //System.out.println("Stage 1");
        }
        //Ask for arguments at the end of each stage
        
    }

    private void playStageInitialArguments(int stageTime) {

        //Stage 1
        System.out.println("Stage initial arguments is starting");
        //Updating players
        ArrayList<Serializable> responseParams = new ArrayList<Serializable>();
        responseParams.add(STAGE_INITIAL_ARGUMENTS);
        updatePlayers(PlayerHandler.RESPONSE_NEW_STAGE, responseParams);
        responseParams.clear();
        //Starting timer
        timer.startTimer(stageTime);

        while (currentStage == STAGE_INITIAL_ARGUMENTS & numPlayers == MAX_PLAYERS) {
            //System.out.println("Stage 1");
        }

    }

    private void playStageCounterArguments(int stageTime) {

        //Stage 2
        System.out.println("Stage counter arguments is starting");
        //Updating players
        ArrayList<Serializable> responseParams = new ArrayList<Serializable>();
        responseParams.add(STAGE_COUNTER_ARGUMENTS);
        updatePlayers(PlayerHandler.RESPONSE_NEW_STAGE, responseParams);
        responseParams.clear();
        //Starting timer
        timer.startTimer(60);

        while (currentStage == STAGE_COUNTER_ARGUMENTS & numPlayers == MAX_PLAYERS) {
            //System.out.println("Stage 2");
        }

    }

    private void playStageAnswers(int stageTime) {

        //Stage 3
        System.out.println("Stage conclusion is starting");
        //Updating players
        ArrayList<Serializable> responseParams = new ArrayList<Serializable>();
        responseParams.add(STAGE_ANSWERS);
        updatePlayers(PlayerHandler.RESPONSE_NEW_STAGE, responseParams);
        responseParams.clear();
        //Starting timer
        timer.startTimer(60);

        while (currentStage == STAGE_ANSWERS & numPlayers == MAX_PLAYERS) {
            //System.out.println("Stage 3");
        }

    }

    private void playStageConclusion(int stageTime) {

        //Stage 4
        System.out.println("Stage 4 starting");
        //Updating players
        ArrayList<Serializable> responseParams = new ArrayList<Serializable>();
        responseParams.add(STAGE_CONCLUSION);
        updatePlayers(PlayerHandler.RESPONSE_NEW_STAGE, responseParams);
        responseParams.clear();
        //Starting timer
        timer.startTimer(60);

        while (currentStage == STAGE_CONCLUSION & numPlayers == MAX_PLAYERS) {
            //System.out.println("Stage 4");
        }

    }

    /**
     * This method generates and returns a new debate in the given category.
     *
     * @param category
     * @return
     */
    private Debate generateNewDebate(int category) {

        return null;
    }

    /**
     * This method closes current debate when debate finished or if one of the
     * debaters leave. It saves the current debate to database.
     */
    private void closeBattle(int yesVotes, int noVotes) {
        
        currentDebate.closeDebate(yesVotes,noVotes);
        currentStage =STAGE_SIDE_SELECTION;
        

    }

    /**
     * This method decides who will debate and who will watch.
     *
     * @return false if all players chose same side, true otherwise
     */
    private boolean designateSidesToPlayers() {
        ArrayList<Player> yesSidePlayers = new ArrayList<Player>();
        ArrayList<Player> noSidePlayers = new ArrayList<Player>();

        int size = playerHandlers.size();
        int lastRandomSide = Player.SIDE_POSITIVE;
        //Dividing players into yes and no sides.
        for (int i = 0; i < size; i++) {

            Player curPlayer = playerHandlers.get(i).getPlayer();
            int curPlayerSide = curPlayer.getSide();

            if (curPlayerSide == Player.SIDE_POSITIVE) {
                yesSidePlayers.add(curPlayer);
            } else if (curPlayerSide == Player.SIDE_NEGATIVE) {
                noSidePlayers.add(curPlayer);
            } else {
                //Assigning sides to players that did not choose a side.
                System.out.println("Player without side");
                if (lastRandomSide == Player.SIDE_POSITIVE) {
                    curPlayer.setAsNegativeDebater();
                    noSidePlayers.add(curPlayer);
                    lastRandomSide = Player.SIDE_NEGATIVE;
                } else {
                    curPlayer.setAsPositiveDebater();
                    yesSidePlayers.add(curPlayer);
                    lastRandomSide = Player.SIDE_POSITIVE;
                }
            }
        }

        if (yesSidePlayers.size() > 0 && noSidePlayers.size() > 0) {
            //Choosing 1 player from both sides.
            choosePlayer(yesSidePlayers);
            choosePlayer(noSidePlayers);
            return true;
        } else {
            return false;
        }

    }

    /**
     * This method chooses one player amon the players that chose the same side.
     * This method changes side of the players to spectator if they weren't
     * picked.
     *
     * @param playersOnOneSide list of players that chose same side.
     */
    private void choosePlayer(ArrayList<Player> playersOnOneSide) {

        //Setting all players as spectator
        for (int i = 0; i < playersOnOneSide.size(); i++) {
            playersOnOneSide.get(i).setAsSpectator();

        }

        ArrayList<Player> highPriorityPlayers = new ArrayList<Player>();
        Player curPlayer = playersOnOneSide.get(0);
        Player lastPriorityPlayer = curPlayer;

        highPriorityPlayers.add(curPlayer);

        //Checking the consecutive games that players weren't chosen as debater.
        for (int i = 1; i < playersOnOneSide.size(); i++) {

            curPlayer = playersOnOneSide.get(i);

            //If a player has more games watched than the last player on high priority list, removing all players in high priority list and adding the cur player.
            if (curPlayer.getConsecutiveGamesWatched() > lastPriorityPlayer.getConsecutiveGamesWatched()) {
                highPriorityPlayers.clear();
                highPriorityPlayers.add(curPlayer);

                //If a player has same amount of games as last player on high priority list, adding new player to list
            } else if (curPlayer.getConsecutiveGamesWatched() == lastPriorityPlayer.getConsecutiveGamesWatched()) {

                lastPriorityPlayer = curPlayer;
                highPriorityPlayers.add(curPlayer);

            }
        }

        //Comparing total games played in a session that players in high priority list played.
        Player chosenPlayer = highPriorityPlayers.get(0);
        for (int i = 1; i < highPriorityPlayers.size(); i++) {

            if (chosenPlayer.getGamesPlayedInSession() > highPriorityPlayers.get(i).getGamesPlayedInSession()) {
                chosenPlayer = highPriorityPlayers.get(i);
            }
        }

    }

    public void terminate() {
        running = false;
    }

    public void nextStage() {

        if (currentStage != STAGE_CONCLUSION) {
            currentStage++;
        } else {
            currentStage = STAGE_INITIAL_ARGUMENTS;
        }
    }

}
