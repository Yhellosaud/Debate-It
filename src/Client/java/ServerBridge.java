package dicomp.debateit;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

import SharedModels.User;

import static java.lang.Thread.sleep;

/**
 * Created by Cagatay on 11.03.2018.
 */

public class ServerBridge {

    public static final String serverIpAccessPoint = "192.168.43.193";
    public static final String serverIpBilkent = "139.179.134.139";
    public static final String serverIpEv = "192.168.1.42";
    public static final int serverPort = 54134;

    private static final long TIME_BETWEEN_ATTEMPTS = 1000;
    private static final long TIME_OUT_AFTER_ATTEMPTS = 10;

    public static final int INVALID_REQUEST_ID = -1;
    public static final int FAILED_REQUEST = -2;
    public static final int SUCCESFUL_REQUEST = -3;
    public static final int CLIENT_CONNECTED = -4;

    //Request in which client does not expect to receive data
    public static final int REQUEST_REGISTER = 0;
    public static final int REQUEST_ADD_NEW_USER_ITEM = 1;
    public static final int REQUEST_ADD_NEW_PLAYED_DEBATE = 2;
    public static final int REQUEST_ADD_NEW_PAST_DEBATE = 3;
    public static final int REQUEST_JOIN_BATTLE = 4;
    public static final int REQUEST_CHANGE_SELECTED_AVATAR = 5;
    public static final int REQUEST_CHANGE_SELECTED_TITLE = 6;
    public static final int REQUEST_CHANGE_SELECTED_FRAME = 7;
    public static final int REQUEST_SEND_ARGUMENT = 8;


    //Request in which client will expect to receive data
    public static final int REQUEST_GET_INVENTORY = 10;
    public static final int REQUEST_GET_PLAYED_DEBATES = 11;
    public static final int REQUEST_GET_PAST_DEBATES = 12;
    public static final int REQUEST_GET_BUYABLE_ITEMS = 13;
    public static final int REQUEST_SIGN_IN = 14;

    //IDs that will be used while sending data from server side.
    public static final int RESPONSE_USER_OBJECT = 100;
    public static final int RESPONSE_INVENTORY = 101;
    public static final int RESPONSE_PLAYED_DEBATES = 102;
    public static final int RESPONSE_PAST_DEBATES = 103;
    public static final int RESPONSE_BUYABLE_ITEMS = 104;
    public static final int RESPONSE_BATTLE_TIME = 105;
    public static final int RESPONSE_NEW_STAGE = 106;
    public static final int RESPONSE_NEW_ARGUMENT = 107;


    private volatile ArrayList<Serializable> leastRecentlyReceivedData;
    private volatile boolean isDataReady;
    private DataReceivable context;
    private Socket socket;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
    private volatile boolean listening;


    /**
     * Constructor
     *
     * @param context a reference to ui class that is calling this method
     */
    public ServerBridge(DataReceivable context) {
        leastRecentlyReceivedData = new ArrayList<Serializable>();
        isDataReady = false;
        listening = false;
        this.context = context;

    }

    public synchronized void requestGetPastDebates(){
        ArrayList<Serializable> requestParams = new ArrayList<Serializable>();
        request(REQUEST_GET_PAST_DEBATES,requestParams);
    }

    public synchronized void requestGetPlayedDebates(User user){
        ArrayList<Serializable> requestParams = new ArrayList<Serializable>();
        requestParams.add(user.getUserID());
        request(REQUEST_GET_PLAYED_DEBATES,requestParams);
    }


    public synchronized void requestRegister(String userName, String password){
        ArrayList<Serializable> requestParams = new ArrayList<Serializable>();
        requestParams.add(userName);
        requestParams.add(password);
        request(REQUEST_REGISTER,requestParams);
    }

    public synchronized void requestJoinBattle(User user){
        ArrayList<Serializable> requestParams = new ArrayList<Serializable>();
        requestParams.add(user);
        request(REQUEST_JOIN_BATTLE,requestParams);
    }

    public synchronized void requestSendArgument(User user,String argument){

        ArrayList<Serializable> requestParams = new ArrayList<Serializable>();
        requestParams.add(user.getUserID());
        requestParams.add(argument);
        request(REQUEST_SEND_ARGUMENT,requestParams);
    }

    public synchronized void requestSignIn(String userName, String password){
        ArrayList<Serializable> requestParams = new ArrayList<Serializable>();
        requestParams.add(userName);
        requestParams.add(password);
        request(REQUEST_SIGN_IN,requestParams);
    }

    /**
     * This method starts a new async task that communicates with server.
     *
     * @param requestId
     * @param requestParams
     */
    private synchronized void request(int requestId, ArrayList<Serializable> requestParams) {

        isDataReady = false;
        leastRecentlyReceivedData.clear();
        RequestTask client = new RequestTask(requestId, requestParams);
        client.execute();

    }


    /**
     * This method tries to return the data received from server. It tries 10 times before timeout with 1sec sleep between tries.
     * This method should'nt be called from ui threads so that ui thread does not get blocked by it.
     *
     * @return least recently requested data or null
     */
    public ArrayList<Serializable> getLeastRecentlyReceivedData() {

        int attemptsToRetrieveData = 1;

        while (attemptsToRetrieveData <= TIME_OUT_AFTER_ATTEMPTS) {

            context.updateRetrieveProgress(attemptsToRetrieveData);
            System.out.println("Attempts to retrieve data: " + attemptsToRetrieveData);

            if (isDataReady) {
                return leastRecentlyReceivedData;
            } else {

                try {
                    Thread.sleep(TIME_BETWEEN_ATTEMPTS);
                } catch (Exception e) {
                    System.out.println("Couldn't put thread to sleep");
                    e.printStackTrace();
                }
            }
            attemptsToRetrieveData++;
        }

        System.out.println("Couldn't retrieved the data after " + attemptsToRetrieveData + " tries");
        return null;
    }

    /**
     * Call this method before requesting.
     */
    public void startListeningToServer() {
        ServerListeningTask serverListeningTask = new ServerListeningTask();
        serverListeningTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        listening = true;
    }

    private class ServerListeningTask extends AsyncTask<Void, ArrayList<Serializable>,Void> {

        protected Void doInBackground(Void... args) {

            System.out.println("started listening.");
            try {
                socket = new Socket(serverIpBilkent, serverPort);
                outToServer = new ObjectOutputStream(socket.getOutputStream());
                inFromServer = new ObjectInputStream(socket.getInputStream());

            } catch (Exception e) {
                System.out.println("Couldn't opened input output sockets");
                e.printStackTrace();
            }

            while (listening) {
                //Reading servers response
                try {
                    int responseID = inFromServer.readInt();

                    //Checking if data is provided
                    if (responseID == INVALID_REQUEST_ID) {
                        System.out.println("Invalid request id!");
                        isDataReady = false;
                        return null;
                    } else if (responseID == FAILED_REQUEST) {
                        System.out.println("Server failed to provide the request!");
                        isDataReady = false;
                        return null;
                    }
                    System.out.println("------Servers response id: " + responseID+" -------");

                    //Reading data
                    boolean endOfFileReached = false;
                    leastRecentlyReceivedData.clear();
                    isDataReady = false;


                    //Reading data
                    /*if (responseID == RESPONSE_BATTLE_TIME) {
                        int time = inFromServer.readInt();
                        System.out.println("Time: " + time);

                        //Reading the terminator at the end of stream
                        Object terminator = inFromServer.readObject();

                        //Sending client connected request
                        request(CLIENT_CONNECTED, null);

                    } else */{
                        while (!endOfFileReached) {

                            try {
                                Object curObject = inFromServer.readObject();
                                if (curObject == null) {
                                    endOfFileReached = true;
                                } else {
                                    leastRecentlyReceivedData.add((Serializable) curObject);
                                }

                            } catch (EOFException ignored) {
                                endOfFileReached = true;
                                //ignored.printStackTrace();
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                                e.printStackTrace();
                                endOfFileReached = true;
                                isDataReady = false;
                            }
                        }

                        isDataReady = true;
                        //Printing retrieved data
                        System.out.println("Received data: ");
                        for (int i = 0; i < leastRecentlyReceivedData.size(); i++) {

                            System.out.println(leastRecentlyReceivedData.get(i).toString());
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    listening = false;
                }
            }
            System.out.print("Stopped listening to server.");
            return null;
        }


    }

    /**
     * This async task is responsible for communicating with server.
     * It writes the received data to leastRecentlyReceivedData of the caller ServerBridge object.
     * Received data could be retrieved with getLeastRecentlyReceivedData() method.
     */
    private class RequestTask extends AsyncTask<Void, Void, Void> {

        private int requestId;
        private ArrayList<Serializable> requestParams;

        /**
         * Constructor
         *
         * @param requestId     request ıd
         * @param requestParams request parameters
         */
        public RequestTask(int requestId, ArrayList<Serializable> requestParams) {


            this.requestId = requestId;
            this.requestParams = requestParams;
        }

        @Override
        /**
         * This method runs in the background.
         */
        protected Void doInBackground(Void... arg0) {

            System.out.println("--------Request task started----------");
            try {
                //Sending request id
                System.out.println("Request id: "+ requestId);
                outToServer.writeInt(requestId);
                //Sending request parameters
                System.out.println("Request data:");
                if (requestParams != null) {
                    int paramSize = requestParams.size();
                    for (int i = 0; i < paramSize; i++) {

                        outToServer.writeObject(requestParams.get(i));
                        System.out.println(requestParams.get(i).toString());
                    }

                }
                //Sending terminator
                outToServer.writeObject(null);
                outToServer.flush();

            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        /**
         * This method runs when the doInBackground method is finished.
         * It disconnects client device from server.
         */
        protected void onPostExecute(Void results) {
            //disconnectFromServer();
        }
    }

    /**
     * This method closes the socket to prevent memory leaks.
     */
    public void disconnectFromServer() {
        try {
            if (socket != null) {
                socket.close();
                System.out.println("socket closed");
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Could not closed socket");
        }
    }


}


