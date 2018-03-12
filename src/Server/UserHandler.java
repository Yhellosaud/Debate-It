package server;

import java.net.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class UserHandler implements Runnable {

    public final int INVALID_ID = -1;
    public final int FAILED_REQUEST = -2;
    public final int SUCCESFUL_REQUEST = -3;

    //Request in which client does not expect to receive data
    public final int REQUEST_REGISTER = 0;
    public final int REQUEST_ADD_NEW_USER_ITEM = 1;
    public final int REQUEST_ADD_NEW_PLAYED_DEBATE = 2;
    public final int REQUEST_ADD_NEW_PAST_DEBATE = 3;
    public final int REQUEST_ENTER_LOBBY = 4;
    public final int REQUEST_CHANGE_SELECTED_AVATAR = 5;
    public final int REQUEST_CHANGE_SELECTED_TITLE = 6;
    public final int REQUEST_CHANGE_SELECTED_FRAME = 7;

    //Request in which client will expect to receive data
    public final int REQUEST_GET_INVENTORY = 10;
    public final int REQUEST_GET_PLAYED_DEBATES = 11;
    public final int REQUEST_GET_PAST_DEBATES = 12;
    public final int REQUEST_GET_BUYABLE_ITEMS = 13;
    public final int REQUEST_SIGN_IN = 14;

    //IDs that will be used while sending data from server side.
    public final int FORWARD_USER_OBJECT = 100;
    public final int FORWARD_INVENTORY = 101;
    public final int FORWARD_PLAYED_DEBATES = 102;
    public final int FORWARD_PAST_DEBATES = 103;
    public final int FORWARD_BUYABLE_ITEMS = 104;

    private Socket socket;
    private String clientAddress;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    public UserHandler(Socket socket, int threadId) {

        this.socket = socket;
        this.clientAddress = socket.getRemoteSocketAddress().toString();

        try {

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

        } catch (Exception e) {

            System.out.println(e.getMessage());
            System.out.println("Failed to initiate input output streams");
        }

    }

    public void run() {

        try {

            ArrayList<Object> requestParams = new ArrayList<Object>();

            
            boolean endOfStreamReached = false;
            int requestId = in.readInt();
            System.out.println("Request id: " + requestId);

            while (!endOfStreamReached) {
                try {
                    Object curObject = in.readObject();
                    if(curObject==null){
                        endOfStreamReached = true;
                    }else{
                        requestParams.add(curObject);
                    }
                    
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    endOfStreamReached = true;
                }
            }
            System.out.println("params size: "+requestParams.size());
            for(int i=0;i<requestParams.size();i++){
                System.out.println(requestParams.get(i).toString());
            }

            handleIncomingRequests(requestId, requestParams);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                System.out.println("Client connection ended");
            } catch (Exception ignored) {

            }

        }

    }

    /*public synchronized void sendMessage(String message) {

        if (out == null) {
            System.out.println("out is null");
        }
        out.println(message);
        

    }*/
//Below method were not needed and they probably wont work as intented.
/*public synchronized void terminate() {
        listening = false;
        try {
            socket.close();

        } catch (Exception e) {
            System.out.println("Couldn't closed client socket");
            System.out.println(e.getMessage());
            e.printStackTrace();

        }
    }

    public synchronized void pauseThread() {
        listening = false;
    }

    public synchronized void continueThread() {
        listening = true;
    }*/
    private boolean handleIncomingRequests(int requestId, ArrayList<Object> requestParams) {

        switch (requestId) {

            case (REQUEST_REGISTER):
                handleRequestRegister(requestParams);
                break;
            case (REQUEST_SIGN_IN):
                break;
            case (REQUEST_ADD_NEW_USER_ITEM):
                break;
            case (REQUEST_ADD_NEW_PLAYED_DEBATE):
                break;
            case (REQUEST_ADD_NEW_PAST_DEBATE):
                break;
            case (REQUEST_ENTER_LOBBY):

                break;
            case (REQUEST_CHANGE_SELECTED_AVATAR):
                break;
            case (REQUEST_CHANGE_SELECTED_TITLE):
                break;
            case (REQUEST_CHANGE_SELECTED_FRAME):
                break;
            case (REQUEST_GET_INVENTORY):
                break;
            case (REQUEST_GET_PLAYED_DEBATES):
                break;
            case (REQUEST_GET_PAST_DEBATES):
                break;
            case (REQUEST_GET_BUYABLE_ITEMS):
                break;
        }
        return false;
    }

    private boolean handleRequestRegister(ArrayList<Object> requestParams) {
        String data1 = "data1";
        String data2 = "data2";
        try {
            out.writeObject(data1);
            out.writeObject(data2);
            out.writeObject(null);
            out.flush();
            System.out.println("objects send");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return false;
        }

    }
    /*
    private boolean handleRequestSignIn(ArrayList<Object> requestParams) {
        User user;

        forwardUserObject(user);
        return false;
    }

    private boolean handleRequestAddNewUserItem(ArrayList<Object> requestParams) {
        return false;
    }

    private boolean handleRequestAddNewPlayedDebate(ArrayList<Object> requestParams) {
        DebateManager.addNewPlayedDebate(debate);
        return false;
    }

    private boolean handleRequestAddNewPastDebate(ArrayList<Object> requestParams) {
        return false;
    }

    private boolean handleRequestEnterLooby(ArrayList<Object> requestParams) {
        return false;
    }

    private boolean handleRequestChangeSelectedAvatar(ArrayList<Object> requestParams) {
        return false;
    }

    private boolean handleRequestChangeSelectedTitle(ArrayList<Object> requestParams) {
        return false;
    }

    private boolean handleRequestChangeSelectedFrame(ArrayList<Object> requestParams) {
        return false;
    }

    private boolean handleRequestGetInventory(ArrayList<Object> requestParams) {
        Inventory inventory;
        forwardInventory(inventory);
        return false;
    }

    private boolean handleRequestGetPlayedDebates(ArrayList<Object> requestParams) {
        Debate[] debates = DebateManager.getPlayaedDebates(userId);
        return false;

    }

    private boolean handleRequestGetPastDebates(ArrayList<Object> requestParams) {
        Debate[] debates;
        return false;
    }

    private boolean handleRequestGetBuyableItems(ArrayList<Object> requestParams) {
        Item[] items;
        return false;
    }

    private boolean forwardUserObject(User user) {
        return false;
    }

    private boolean forwardInventory(Inventory inventory) {
        return false;
    }

    private boolean forwardPlayedDebates(Debate[] debates) {
        return false;
    }

    private boolean forwardPastDebates(Debate[] debates) {
        return false;
    }

    private boolean forwardBuyableItems(Item[] items) {

        return false;
    }
     */
}
