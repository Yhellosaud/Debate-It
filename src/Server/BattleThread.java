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

    private static final int STAGE_INITIAL_ARGUMENTS = 0;
    private static final int STAGE_COUNTER_ARGUMENTS = 1;
    private static final int STAGE_ANSWERS = 2;
    private static final int STAGE_CONCLUSION = 3;
    private static final int MAX_PLAYERS = 1;

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
            newPlayer.setId(numPlayers);
            numPlayers++;
            PlayerHandler newHandler = new PlayerHandler(newPlayer, socket, out, in, this);
            playerHandlers.add(newHandler);
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

        System.out.println("Battle thread started");
        //Starting timer thread
        new Thread(this.timer).start();

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

                //Stage 1
                System.out.println("Stage 1 starting");
                //Updating players
                ArrayList<Serializable> responseParams = new ArrayList<Serializable>();
                responseParams.add(STAGE_INITIAL_ARGUMENTS);
                updatePlayers(PlayerHandler.RESPONSE_NEW_STAGE, responseParams);
                responseParams.clear();
                //Starting timer
                this.timer.startTimer(60);     
                
                while (currentStage == STAGE_INITIAL_ARGUMENTS & numPlayers == MAX_PLAYERS) {
                    //System.out.println("Stage 1");
                }

                //Stage 2
                System.out.println("Stage 2 starting");
                //Updating players
                responseParams.add(STAGE_COUNTER_ARGUMENTS);
                updatePlayers(PlayerHandler.RESPONSE_NEW_STAGE, responseParams);
                responseParams.clear();
                //Starting timer
                timer.startTimer(60);

                while (currentStage == STAGE_COUNTER_ARGUMENTS & numPlayers == MAX_PLAYERS) {
                    //System.out.println("Stage 2");
                }

                //Stage 3
                System.out.println("Stage 3 starting");
                //Updating players
                responseParams.add(STAGE_ANSWERS);
                updatePlayers(PlayerHandler.RESPONSE_NEW_STAGE, responseParams);
                responseParams.clear();
                //Starting timer
                timer.startTimer(60);

                while (currentStage == STAGE_ANSWERS & numPlayers == MAX_PLAYERS) {
                    //System.out.println("Stage 3");
                }

                //Stage 4
                System.out.println("Stage 4 starting");
                //Updating players
                responseParams.add(STAGE_CONCLUSION);
                updatePlayers(PlayerHandler.RESPONSE_NEW_STAGE, responseParams);
                responseParams.clear();
                //Starting timer
                timer.startTimer(60);

                while (currentStage == STAGE_CONCLUSION & numPlayers == MAX_PLAYERS) {
                    //System.out.println("Stage 4");
                }
            }
            
            timer.stopTimer();
        }

    }

    public void closeBattle() {
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
