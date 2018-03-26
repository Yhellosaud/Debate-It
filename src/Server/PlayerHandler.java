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

    private Player player;
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
                            System.out.println(e.getMessage());
                            e.printStackTrace();
                            endOfStreamReached = true;
                        }
                    }
                    for (int i = 0; i < requestParams.size(); i++) {
                        System.out.println(requestParams.get(i).toString());
                    }
                    if (requestId != UserHandler.CLIENT_CONNECTED) {
                        //Updating players
                        //Correct here
                        battleThread.updateDebate(requestId,requestParams);
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
    public synchronized void updateBattle(int responseId, ArrayList<Serializable> responseData) {

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

    /**
     * This method sends time to client and reads the response to see if client
     * is still connected. If client does not send connected response it
     * disconnects the client from battle.
     *
     * @param time
     */
    public synchronized void sendCurTimeToClient(int time) {

        System.out.println("----send cur time calisti. time: " + time);
        try {
            //Sending request id
            out.writeInt(UserHandler.RESPONSE_BATTLE_TIME);
            //Sending time
            out.writeInt(time);
            System.out.println("time has sent");
            //Sending terminator of stream
            out.writeObject(null);
            out.flush();

            //Reading response
            /*try {

                int response = in.readInt();
                System.out.println("Response: " + response);

                //reading terminator
                Object nullObj = in.readObject();

                if (response != UserHandler.CLIENT_CONNECTED) {
                    battleThread.disconnectPlayer(player);
                    System.out.println("user disconected");
                }

            } catch (Exception e) {
                battleThread.disconnectPlayer(player);
                System.out.println("user disconected");
            }*/
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
