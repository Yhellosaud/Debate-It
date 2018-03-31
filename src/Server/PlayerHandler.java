/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Cagatay
 */
import SharedModels.*;
import java.beans.Introspector;
import java.net.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.io.Serializable;

public class PlayerHandler implements Runnable {

    public static final int REQUEST_SEND_ARGUMENT = 8;

    public static final int RESPONSE_BATTLE_TIME = 105;
    public static final int RESPONSE_NEW_STAGE = 106;
    public static final int RESPONSE_NEW_ARGUMENT = 107;
    
    public static final int REQUEST_SUBMIT_SIDES = 108;
    public static final int REQUEST_SUBMIT_ARGUMENTS = 109;
    public static final int REQUEST_SUBMIT_VOTE = 110;
    

    private volatile Player player;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private BattleThread battleThread;
    private boolean running;

    public PlayerHandler(Player player, Socket clientSocket, ObjectOutputStream out, ObjectInputStream in, BattleThread battleThread) {
        this.player = player;
        this.clientSocket = clientSocket;
        this.out = out;
        this.in = in;
        this.battleThread = battleThread;
        running = true;

    }

    @Override
    /**
     * This method listens for players requests.
     */
    public void run() {

        System.out.println("Player handler started");
        ArrayList<Serializable> requestParams = new ArrayList<Serializable>();
        while (running) {

            boolean endOfStreamReached = false;
            try {

                synchronized (in) {
                    //Reading request id
                    int requestId = in.readInt();
                    requestParams.clear();
                    System.out.println("Player Handler request id: " + requestId);

                    //Reading objects from objectinputstream. There is a null object at the end of stream to mark the end.
                    while (!endOfStreamReached) {
                        try {
                            Object curObject = in.readObject();
                            if (curObject == null) {
                                endOfStreamReached = true;
                            } else {
                                requestParams.add((Serializable) curObject);
                            }

                        } catch (Exception e) {
                            
                            e.printStackTrace();
                            endOfStreamReached = true;
                        }
                    }
                    for (int i = 0; i < requestParams.size(); i++) {
                        System.out.println(i+": "+requestParams.get(i).toString());
                    }
                    if (requestId != UserHandler.CLIENT_CONNECTED) {
                        //Updating players
                        //Correct here
                        battleThread.updateDebate(requestId, requestParams);
                        battleThread.updatePlayers(requestId, requestParams);

                    }

                }

            } catch (Exception e) {
                battleThread.disconnectPlayer(player);
                System.out.println("user disconected");
                //e.printStackTrace();

            }
        }

    }

    /**
     * This method sends the responseId and responseData to client.
     *
     * @param responseId
     * @param responseData
     */
    public synchronized void updatePlayer(int requestId, ArrayList<Serializable> requestParams) {

        switch (requestId) {
            case (REQUEST_SEND_ARGUMENT):
                int argumentSenderId = (int) requestParams.get(0);
                String argument = (String) requestParams.get(1);
                responseSendArgument(argumentSenderId, argument);
                break;
            case (RESPONSE_NEW_STAGE):
                int newStage = (int) requestParams.get(0);
                responseNewStage(newStage);
                break;
            case (RESPONSE_BATTLE_TIME):
                int time = (int) requestParams.get(0);
                responseBattleTime(time);
                break;

        }
    }

    private void responseNewStage(int newStage) {
        ArrayList<Serializable> responseParams = new ArrayList<Serializable>();
        responseParams.add(newStage);
        response(RESPONSE_NEW_STAGE, responseParams);
    }

    private void responseSendArgument(int argumentSenderId, String argument) {
        ArrayList<Serializable> responseParams = new ArrayList<Serializable>();
        responseParams.add(player);
        responseParams.add(argument);
        response(RESPONSE_NEW_ARGUMENT, responseParams);
    }

    /**
     * This method sends battle time to client.
     *
     * @param time
     */
    private void responseBattleTime(int time) {

        System.out.println("Sending battle time to player: " + time);
        ArrayList<Serializable> responseParams = new ArrayList<Serializable>();
        responseParams.add(time);
        response(RESPONSE_BATTLE_TIME, responseParams);
    }

    /**
     * This method sends the responseId and responseData to clients.
     *
     * @param responseId
     * @param responseData
     */
    private void response(int responseId, ArrayList<Serializable> responseData) {

        try {
            out.writeInt(responseId);

            for (int i = 0; i < responseData.size(); i++) {
                out.writeObject(responseData.get(i));
            }
            //Sending terminator
            out.writeObject(null);
            out.flush();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void terminate() {

        running = false;
        try {
            out.close();
        } catch (Exception ignored) {

        }
    }

    public Player getPlayer() {
        return player;
    }

}
