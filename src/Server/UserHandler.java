package server;

import SharedModels.*;
import java.net.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * This thread is responsible for handling client request.
 *
 * @author Cagatay
 */
public class UserHandler implements Runnable {

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

    private Socket socket;
    private String clientAddress;
    private BattleThread battleThread;

    private volatile ObjectOutputStream out;
    private volatile ObjectInputStream in;

    /*private DebateManager dm;
    private ItemManager im;
    private UserManager um;*/
    public UserHandler(Socket socket, int threadId, BattleThread battleThread) {

        this.socket = socket;
        this.clientAddress = socket.getRemoteSocketAddress().toString();
        this.battleThread = battleThread;

        /*this.dm = dm;
        this.im = im;
        this.um = um;*/
        //Initiating input and output streams
        try {

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

        } catch (Exception e) {

            System.out.println(e.getMessage());
            System.out.println("Failed to initiate input output streams");
        }

    }

    public void run() {

        ArrayList<Serializable> requestParams = new ArrayList<Serializable>();
        boolean endOfStreamReached = false;
        try {

            //Reading request id
            int requestId = in.readInt();
            System.out.println("User handler request id: " + requestId);

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
            System.out.println("params size: " + requestParams.size());
            for (int i = 0; i < requestParams.size(); i++) {
                System.out.println(requestParams.get(i).toString());
            }

            //Handling request
            handleIncomingRequests(requestId, requestParams);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            /*try {
                socket.close();
                System.out.println("Client connection ended");
            } catch (Exception ignored) {

            }*/

        }

    }

    /**
     * This method handles the user request by calling necessary methods with
     * respect to request id.
     *
     * @param requestId
     * @param requestParams
     * @return
     */
    private boolean handleIncomingRequests(int requestId, ArrayList<Serializable> requestParams) {

        switch (requestId) {

            case (REQUEST_REGISTER):
                handleRequestRegister(requestParams);
                break;
            case (REQUEST_SIGN_IN):
                handleRequestSignIn(requestParams);
                break;
            case (REQUEST_ADD_NEW_USER_ITEM):
                break;
            case (REQUEST_ADD_NEW_PLAYED_DEBATE):
                break;
            case (REQUEST_ADD_NEW_PAST_DEBATE):
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
                handleRequestGetPlayedDebates(requestParams);
                break;
            case (REQUEST_GET_PAST_DEBATES):
                break;
            case (REQUEST_GET_BUYABLE_ITEMS):
                break;
            case (REQUEST_JOIN_BATTLE):
                handleRequestJoinBattle(requestParams);
                break;
        }
        return false;
    }

    /**
     * This method handles register requests.
     *
     *
     * @param requestParams
     * @return
     */
    private boolean handleRequestRegister(ArrayList<Serializable> requestParams) {
        String data1 = "data1";
        String data2 = "data2";

        ArrayList<Integer> pastDebateIDs = new ArrayList<Integer>();
        pastDebateIDs.add(5);
        pastDebateIDs.add(10);
        ArrayList<Integer> votedDebates = new ArrayList<Integer>();
        votedDebates.add(100);
        votedDebates.add(101);
        try {
            /*out.writeObject(data1);
            out.writeObject(data2);
            out.writeObject(null);*/
            User user1 = new User("cagatay", "123", 1, pastDebateIDs, votedDebates);
            out.writeObject(user1);
            out.flush();
            System.out.println("objects send");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return false;
        }

    }

    private boolean handleRequestSignIn(ArrayList<Serializable> requestParams) {
        ArrayList<Integer> pastDebateIDs = new ArrayList<Integer>();
        ArrayList<Integer> votedDebates = new ArrayList<Integer>();
        pastDebateIDs.add(23);
        pastDebateIDs.add(24);
        votedDebates.add(33);
        votedDebates.add(34);

        String userName = (String) requestParams.get(0);
        String password = (String) requestParams.get(1);

        User user = new User(userName, password, 1, pastDebateIDs, votedDebates);

        forwardUserObject(user);
        return false;
    }

    private boolean handleRequestAddNewUserItem(ArrayList<Serializable> requestParams) {
        return false;
    }

    private boolean handleRequestAddNewPlayedDebate(ArrayList<Serializable> requestParams) {
        //DebateManager.addNewPlayedDebate(debate);
        return false;
    }

    private boolean handleRequestAddNewPastDebate(ArrayList<Serializable> requestParams) {
        return false;
    }

    private boolean handleRequestEnterLooby(ArrayList<Serializable> requestParams) {
        return false;
    }

    private boolean handleRequestChangeSelectedAvatar(ArrayList<Serializable> requestParams) {
        return false;
    }

    private boolean handleRequestChangeSelectedTitle(ArrayList<Serializable> requestParams) {
        return false;
    }

    private boolean handleRequestChangeSelectedFrame(ArrayList<Serializable> requestParams) {
        return false;
    }

    /*private boolean handleRequestGetInventory(ArrayList<Serializable> requestParams) {
        Inventory inventory;
        forwardInventory(inventory);
        return false;
    }*/
    private boolean handleRequestGetPlayedDebates(ArrayList<Serializable> requestParams) {
        //Debate[] debates = DebateManager.getPlayaedDebates(userId);
        //Debate(Idea idea, ArrayList<Player> players, int debateID, long debateLength, int yesVotes, int noVotes, int stage1Length, int stage2Length, int stage3Length)
        Idea idea = new Idea(22,"Should street animals be allowed to Bilkent?",5);
        Player player1 = new Player(1,"Player 1",0);
        Player player2 = new Player(2,"Player 2",0);
        Player player3 = new Player(3,"Player 3",0);
        Player player4 = new Player(4,"Player 4",0);
        ArrayList<Player> players = new ArrayList<Player>();
        Debate debate1 = new Debate(idea,players,1,15,1,3,4,3,4,5);
        Debate debate2 = new Debate(idea,players,2,15,1,3,4,3,4,5);
        ArrayList<Serializable> responseData = new ArrayList<Serializable>();
        responseData.add(debate1);
        responseData.add(debate2);
        
        response(RESPONSE_PLAYED_DEBATES,responseData);      

        return false;

    }

    private boolean handleRequestGetPastDebates(ArrayList<Serializable> requestParams) {
        Debate[] debates;
        return false;
    }

    /*private boolean handleRequestGetBuyableItems(ArrayList<Object> requestParams) {
        Item[] items;
        return false;
    }*/
    private boolean handleRequestJoinBattle(ArrayList<Serializable> requestParams) {

        //Player player = (Player)requestParams.get(0);
        Player player = new Player(123, "Cagatay", 123, null, 0, 0, 0);
        battleThread.joinNewPlayer(player, socket, out, in);

        return false;
    }

    private boolean forwardUserObject(User user) {

        ArrayList<Serializable> responseData = new ArrayList<Serializable>();
        responseData.add(user);
        response(RESPONSE_USER_OBJECT, responseData);
        return false;
    }

    /*private boolean forwardInventory(Inventory inventory) {
        return false;
    }*/
    private boolean forwardPlayedDebates(Debate[] debates) {
        return false;
    }

    private boolean forwardPastDebates(Debate[] debates) {
        return false;
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
            System.out.println("Response send");
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /*private boolean forwardBuyableItems(Item[] items) {

        return false;
    }*/
}
