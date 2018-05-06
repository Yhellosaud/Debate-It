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
import java.util.Random;

public class BattleThread implements Runnable {

    private static final int STAGE_SIDE_SELECTION = 0;
    private static final int STAGE_INITIAL_ARGUMENTS = 1;
    private static final int STAGE_COUNTER_ARGUMENTS = 2;
    private static final int STAGE_ANSWERS = 3;
    private static final int STAGE_CONCLUSION = 4;
    private static final int STAGE_VOTING = 5;

    private static final int MAX_PLAYERS = 2;

    private static final int REQUEST_SEND_ARGUMENT = 9;

    private volatile Debate currentDebate;
    private volatile ArrayList<PlayerHandler> playerHandlers;
    private BattleTimer timer;
    private volatile int numPlayers;
    private volatile int currentStage;
    private volatile boolean running;
    private DebateManager dm;

    public BattleThread(DebateManager dm) {

        running = true;
        playerHandlers = new ArrayList<PlayerHandler>();
        numPlayers = 0;
        currentStage = 0;
        timer = new BattleTimer(this, playerHandlers);
        this.dm = dm;

    }

    public synchronized void joinNewPlayer(Player newPlayer, Socket socket, ObjectOutputStream out, ObjectInputStream in) {

        if (numPlayers == MAX_PLAYERS) {
            System.out.println("Maximum number of players reached. Couldn't joined new player");
        } else {
            System.out.println("new player joined");
            /////////////////////////////Change This set id part ////////////////////////////////////////
            //newPlayer.setId(numPlayers);
            numPlayers++;
            currentDebate.addPlayer(newPlayer);
            PlayerHandler newHandler = new PlayerHandler(newPlayer, socket, out, in, this);
            playerHandlers.add(newHandler);
            new Thread(newHandler).start();

            ArrayList<Serializable> responseParams = new ArrayList<Serializable>();
            responseParams.add(currentDebate);
            updatePlayers(PlayerHandler.RESPONSE_UPDATED_DEBATE, responseParams);
            //newHandler.updatePlayer(PlayerHandler.RESPONSE_UPDATED_DEBATE,responseParams);                     

        }

    }

    public synchronized void disconnectPlayer(Player player) {

        int playerId = player.getPlayerID();

        for (int i = 0; i < playerHandlers.size(); i++) {

            PlayerHandler curPlayerHandler = playerHandlers.get(i);
            int curPlayerId = curPlayerHandler.getPlayer().getPlayerID();

            //Terminating the player handler with matching id and removing from list
            if (playerId == curPlayerId) {
                curPlayerHandler.terminate();
                playerHandlers.remove(i);
                currentDebate.removePlayer(player);
            }
        }
        ArrayList<Serializable> responseParams = new ArrayList<Serializable>();
        responseParams.add(currentDebate);
        updatePlayers(PlayerHandler.RESPONSE_UPDATED_DEBATE, responseParams);
        numPlayers--;
    }

    /**
     * This method calls updatePlayer method for each of the playerHandlers.
     *
     * @param responseId
     * @param responseData
     */
    public synchronized void updatePlayers(int requestId, ArrayList<Serializable> requestParams) {

        int responseId = UserHandler.INVALID_REQUEST_ID;
        ArrayList<Serializable> responseData = new ArrayList<Serializable>();
        boolean respond = true;

        switch (requestId) {

            case (PlayerHandler.REQUEST_SEND_ARGUMENT):
                responseId = PlayerHandler.RESPONSE_UPDATED_DEBATE;

                int playerId = (int) requestParams.get(0);
                String argumentStr = (String) requestParams.get(1);

                Argument argument = new Argument(timer.getTimer(), currentStage, argumentStr);
                currentDebate.addArgument(playerId, argument);
                responseData.add(currentDebate);

                break;

            case (PlayerHandler.REQUEST_SEND_EXPRESSION):
                break;
            case (PlayerHandler.REQUEST_SUBMIT_SIDES):
                responseId = PlayerHandler.RESPONSE_UPDATED_DEBATE;
                playerId = (int) requestParams.get(0);
                int side = (int) requestParams.get(1);

                for (int i = 0; i < currentDebate.getPlayers().size(); i++) {
                    if (currentDebate.getPlayers().get(i).getPlayerID() == playerId) {
                        currentDebate.getPlayers().get(i).setSide(side);
                    }

                }
                respond = false;
                //responseData.add(currentDebate);

                break;
            case (PlayerHandler.RESPONSE_UPDATED_DEBATE):
                responseId = PlayerHandler.RESPONSE_UPDATED_DEBATE;
                responseData = requestParams;
                break;
            case (PlayerHandler.RESPONSE_NEW_STAGE):
                responseId = PlayerHandler.RESPONSE_NEW_STAGE;
                responseData = requestParams;
        }

        if (respond) {
            synchronized (playerHandlers) {
                for (int i = 0; i < playerHandlers.size(); i++) {
                    playerHandlers.get(i).updatePlayer(responseId, responseData);

                }
            }
        }

    }

    public synchronized void updateDebate(int requestId, ArrayList<Serializable> requestData) {

        switch (requestId) {
            case (REQUEST_SEND_ARGUMENT):
                int playerId = (int) requestData.get(0);
                String argument = (String) requestData.get(1);

                break;
        }
    }

    public void run() {

        System.out.println("Battle thread started\n");
        //Starting timer thread
        new Thread(timer).start();
        boolean initial = true;
        currentDebate = generateNewDebate(null);

        while (running) {

            //Waiting untill max number of players is reached
            while (numPlayers < MAX_PLAYERS) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
            }
            //Battle keeps repeating as long as max number of players is reached

            currentStage = 0;
            System.out.println("Game starting");
            if (!initial) {
                currentDebate = generateNewDebate(currentDebate.getPlayers());
            }

            initial = false;
            boolean sideSelectionSuccess = playStageSideSelection(10);

            if (sideSelectionSuccess) {
                playStageInitialArguments(15);

                playStageCounterArguments(15);

                playStageAnswers(15);

                playStageConclusion(15);

                playStageVoting(10);

                try {
                    //dm.insertDebate(currentDebate);
                } catch (Exception e) {
                    System.out.println("Could not add closing debate to db");
                    e.printStackTrace();

                }
                //Closing debate
                currentDebate.closeDebate();
            }

        }

    }

    private boolean playStageSideSelection(int stageTime) {

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
            //System.out.println("Stage 0");
        }

        boolean sideSelectionSuccess = designateSidesToPlayers(currentDebate.getPlayers());

        if (!sideSelectionSuccess) {
            return false;
        } else {
            responseParams.add(currentDebate);
            updatePlayers(PlayerHandler.RESPONSE_UPDATED_DEBATE, responseParams);
            responseParams.clear();
            return true;
        }

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
        currentDebate.closeStage(STAGE_INITIAL_ARGUMENTS);
        responseParams.add(currentDebate);
        updatePlayers(PlayerHandler.RESPONSE_UPDATED_DEBATE, responseParams);
        responseParams.clear();

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
        timer.startTimer(stageTime);

        while (currentStage == STAGE_COUNTER_ARGUMENTS & numPlayers == MAX_PLAYERS) {
            //System.out.println("Stage 2");
        }
        currentDebate.closeStage(STAGE_COUNTER_ARGUMENTS);
        responseParams.add(currentDebate);
        updatePlayers(PlayerHandler.RESPONSE_UPDATED_DEBATE, responseParams);
        responseParams.clear();

    }

    private void playStageAnswers(int stageTime) {

        //Stage 3
        System.out.println("Stage answers is starting");
        //Updating players
        ArrayList<Serializable> responseParams = new ArrayList<Serializable>();
        responseParams.add(STAGE_ANSWERS);
        updatePlayers(PlayerHandler.RESPONSE_NEW_STAGE, responseParams);
        responseParams.clear();
        //Starting timer
        timer.startTimer(stageTime);

        while (currentStage == STAGE_ANSWERS & numPlayers == MAX_PLAYERS) {
            //System.out.println("Stage 3");
        }
        currentDebate.closeStage(STAGE_ANSWERS);
        responseParams.add(currentDebate);
        updatePlayers(PlayerHandler.RESPONSE_UPDATED_DEBATE, responseParams);
        responseParams.clear();

    }

    private void playStageConclusion(int stageTime) {

        //Stage 4
        System.out.println("Stage conclusion is starting");
        //Updating players
        ArrayList<Serializable> responseParams = new ArrayList<Serializable>();
        responseParams.add(STAGE_CONCLUSION);
        updatePlayers(PlayerHandler.RESPONSE_NEW_STAGE, responseParams);
        responseParams.clear();
        //Starting timer
        timer.startTimer(stageTime);

        while (currentStage == STAGE_CONCLUSION & numPlayers == MAX_PLAYERS) {
            //System.out.println("Stage 4");
        }
        currentDebate.closeStage(STAGE_CONCLUSION);
        responseParams.add(currentDebate);
        updatePlayers(PlayerHandler.RESPONSE_UPDATED_DEBATE, responseParams);
        responseParams.clear();

    }

    private void playStageVoting(int stageTime) {

        //Stage 5
        System.out.println("Stage voting is starting");
        //Updating players
        ArrayList<Serializable> responseParams = new ArrayList<Serializable>();
        responseParams.add(STAGE_VOTING);
        updatePlayers(PlayerHandler.RESPONSE_NEW_STAGE, responseParams);
        responseParams.clear();
        //Starting timer
        timer.startTimer(stageTime);

        while (currentStage == STAGE_VOTING & numPlayers == MAX_PLAYERS) {
            //System.out.println("Stage 4");
        }

    }

    public Debate getCurDebate() {
        return currentDebate;
    }

    /**
     * This method generates and returns a new debate in the given category.
     *
     * @param category
     * @return
     */
    private Debate generateNewDebate(ArrayList<Player> players) {

        Random random = new Random();
        //Idea categories are between 0 inclusive and 5 exclusive
        int category = random.nextInt(5);
        Idea newIdea = Idea.generateIdea(category);

        //Databaseden debate id lazım
        Debate newDebate = new Debate(newIdea, 0, players);

        return newDebate;
    }

    /**
     * This method closes current debate when debate finished or if one of the
     * debaters leave. It saves the current debate to database.
     */
    /*private void closeBattle() {

        currentDebate.closeDebate();
        currentStage = STAGE_SIDE_SELECTION;

    }*/
    public static void testDesignateSides() {
        Player player1 = new Player("user 1");
        Player player2 = new Player("user 1");
        Player player3 = new Player("user 1");
        Player player4 = new Player("user 1");
        player1.setAsNegativeDebater();
        player2.setAsNegativeDebater();
        player3.setAsNegativeDebater();
        player4.setAsPositiveDebater();
        Idea idea = new Idea(10, "Idea 1", 0);
        Debate debate = new Debate(idea, 0, null);
        debate.addPlayer(player1);
        debate.addPlayer(player2);
        debate.addPlayer(player3);
        debate.addPlayer(player4);
        ArrayList<Player> players = debate.getPlayers();

        designateSidesToPlayers(players);
        debate.closeDebate();
        debate.printPlayers();
        System.out.println("end of first debate");

        player1.setAsPositiveDebater();
        player2.setAsPositiveDebater();
        player3.setAsPositiveDebater();
        player4.setAsNegativeDebater();
        designateSidesToPlayers(players);
        debate.closeDebate();
        debate.printPlayers();
        System.out.println("end of second debate");

        player1.setAsPositiveDebater();
        player2.setAsPositiveDebater();
        player3.setAsNegativeDebater();
        player4.setAsNegativeDebater();
        designateSidesToPlayers(players);
        debate.closeDebate();
        debate.printPlayers();
        System.out.println("end of third debate");

        player1.setAsNegativeDebater();
        player2.setAsPositiveDebater();
        player3.setAsPositiveDebater();
        player4.setAsNegativeDebater();
        designateSidesToPlayers(players);
        debate.closeDebate();
        debate.printPlayers();
        System.out.println("end of fourth debate");

    }

    /**
     * This method decides who will debate and who will watch.
     *
     * @return false if all players chose same side, true otherwise
     */
    private static boolean designateSidesToPlayers(ArrayList<Player> players) {

        ArrayList<Player> yesSidePlayers = new ArrayList<Player>();
        ArrayList<Player> noSidePlayers = new ArrayList<Player>();

        int size = players.size();
        int lastRandomSide = Player.SIDE_POSITIVE;

        //Dividing players into yes and no sides.
        for (int i = 0; i < size; i++) {

            Player curPlayer = players.get(i);
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
            choosePlayer(yesSidePlayers, Player.SIDE_POSITIVE);
            choosePlayer(noSidePlayers, Player.SIDE_NEGATIVE);
            return true;
        } else {
            for (int i = 0; i < players.size(); i++) {
                players.get(i).setAsSpectator();
            }
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
    private static void choosePlayer(ArrayList<Player> playersOnOneSide, int side) {

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
        System.out.println(chosenPlayer.getUsername() + " is chosen as side: " + side);
        chosenPlayer.setSide(side);
    }

    public void terminate() {
        running = false;
    }

    public void nextStage() {

        if (currentStage != STAGE_VOTING) {
            currentStage++;
        } else {
            currentStage = STAGE_SIDE_SELECTION;
        }

    }

}
