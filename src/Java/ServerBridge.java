package cstudio.client;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by Cagatay on 11.03.2018.
 */

public class ServerBridge {

    public static final String serverIpAccessPoint = "192.168.43.193";
    public static final String serverIpBilkent = "139.179.200.208";
    public static final int serverPort = 54134;

    public static final int INVALID_ID = -1;
    public static final int FAILED_REQUEST = -2;
    public static final int SUCCESFUL_REQUEST = -3;

    //Request in which client does not expect to receive data
    public static final int REQUEST_REGISTER = 0;
    public static final int REQUEST_ADD_NEW_USER_ITEM = 1;
    public static final int REQUEST_ADD_NEW_PLAYED_DEBATE = 2;
    public static final int REQUEST_ADD_NEW_PAST_DEBATE = 3;
    public static final int REQUEST_ENTER_LOBBY = 4;
    public static final int REQUEST_CHANGE_SELECTED_AVATAR = 5;
    public static final int REQUEST_CHANGE_SELECTED_TITLE = 6;
    public static final int REQUEST_CHANGE_SELECTED_FRAME = 7;

    //Request in which client will expect to receive data
    public static final int REQUEST_GET_INVENTORY = 10;
    public static final int REQUEST_GET_PLAYED_DEBATES = 11;
    public static final int REQUEST_GET_PAST_DEBATES = 12;
    public static final int REQUEST_GET_BUYABLE_ITEMS = 13;
    public static final int REQUEST_SIGN_IN = 14;

    //IDs that will be used while sending data from server side.
    public static final int FORWARD_USER_OBJECT = 100;
    public static final int FORWARD_INVENTORY = 101;
    public static final int FORWARD_PLAYED_DEBATES = 102;
    public static final int FORWARD_PAST_DEBATES = 103;
    public static final int FORWARD_BUYABLE_ITEMS = 104;

    private ArrayList<Object> leastRecentlyReceivedData;
    private boolean isDataReady;
    private DataReceivable content;


    public ServerBridge(DataReceivable content) {
        leastRecentlyReceivedData = new ArrayList<Object>();
        isDataReady = false;
        this.content = content;
    }

    public void request(int requestId, Object[] requestParams) {
        isDataReady = false;
        leastRecentlyReceivedData.clear();
        Client client = new Client(content, requestId, requestParams);
        client.execute();

    }

    public ArrayList<Object> getLeastRecentlyReceivedData() {

        int attemptsToRetrieveData = 0;

        while (attemptsToRetrieveData < 10) {
            attemptsToRetrieveData++;
            System.out.println("Attempts to retrieve data: " + attemptsToRetrieveData);

            if (isDataReady) {
                return leastRecentlyReceivedData;
            } else {

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println("Couldn't put thread to sleep");
                    e.printStackTrace();
                } finally {
                    if (isDataReady) {
                        return leastRecentlyReceivedData;
                    } else {
                        System.out.println("Couldn't retrieved the data after " + attemptsToRetrieveData + " tries");
                        return null;
                    }
                }

            }
        }
        System.out.println("Couldn't retrieved the data after " + attemptsToRetrieveData + " tries");
        return null;
    }

    private class Client extends AsyncTask<Void, Void, Object[]> {

        private Socket socket;
        ObjectOutputStream outToClient;
        ObjectInputStream inFromClient;
        private DataReceivable content;
        private int requestId;
        private Object[] requestParams;

        public Client(DataReceivable content, int requestId, Object[] requestParams) {
            this.content = content;
            this.requestId = requestId;
            this.requestParams = requestParams;

        }

        @Override
        protected Object[] doInBackground(Void... arg0) {

            System.out.println("Async task started");
            try{
                socket = new Socket(serverIpBilkent, serverPort);
                outToClient = new ObjectOutputStream(socket.getOutputStream());
                inFromClient = new ObjectInputStream(socket.getInputStream());

            }catch(Exception e){
                System.out.println("Couldn't opened input output sockets");
                e.printStackTrace();
            }
            try {
                outToClient.writeInt(requestId);
                outToClient.writeObject(requestParams[0]);
                outToClient.writeObject(requestParams[1]);
                outToClient.writeObject(null);
                outToClient.flush();

                System.out.println("objects sent.");

                boolean endOfFileReached = false;
                while(!endOfFileReached){

                    try{

                        Object curObject = inFromClient.readObject();
                        if(curObject==null){
                            endOfFileReached = true;
                        }else{
                            leastRecentlyReceivedData.add(curObject);
                        }

                    }catch(EOFException ignored){
                        endOfFileReached = true;
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                        endOfFileReached = true;
                    }
                }
                isDataReady = true;

            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object[] results) {
            super.onPostExecute(results);
            for(int i=0;i<leastRecentlyReceivedData.size();i++){
                System.out.println(leastRecentlyReceivedData.get(i));
            }
            isDataReady=true;
            disconnectClient();
        }


        public void disconnectClient() {
            try {
                if (socket != null) {
                    socket.close();
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("Could not closed socket");
            }
        }

    }


}


