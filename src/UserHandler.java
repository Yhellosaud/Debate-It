package server;

import java.net.*;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;



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
    private boolean running;
    private PrintWriter out;
    private BufferedReader in;

    public UserHandler(Socket socket)  {

        this.socket = socket;
        this.clientAddress = socket.getRemoteSocketAddress().toString();
        running = true;
        
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } 
        catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Failed to initiate input output streams");
            
        }

    }

    public void run() {

        //Try block with resources
        //This block closes the resources when it is done.
        try  {
            String inputLine;
            while (running) {
                
                inputLine = in.readLine();
                
                if (inputLine == null) {
                    System.out.println("Client disconnected");
                    return;
                }
                
                /*boolean requestHandledSuccessfully =handleIncomingRequests(requestId,Object[] requestParameters);
                if(requestHandledSuccessfully){
                    System.out.println("Request handled successfully.");
                }else{
                    System.out.println("Request handle failed.");
                }*/
                
            
                
                if (inputLine.equals("HELLO SERVER")) {
                    out.println("HELLO CLIENT");
                } else {
                    out.println("Not hello");
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    
    public synchronized void sendMessage(String message){
        
        if(out == null){
                    System.out.println("out is null");
                }
        out.println(message);
                
        
    }

    public void terminate() {
        running = false;
    }

    /*private boolean handleIncomingRequests(int requestId, Object[] requestParameters) {

        switch (requestId) {

            case (REQUEST_REGISTER):
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
    }
    
    private boolean handleRequestRegister(){
        return false;
    }
    private boolean handleRequestSignIn(){
        User user;
        
        forwardUserObject(user);
        return false;
    }
    private boolean handleRequestAddNewUserItem(){
        return false;
    }
    private boolean handleRequestAddNewPlayedDebate(){
        return false;
    }
    private boolean handleRequestAddNewPastDebate(){
        return false;
    }
    private boolean handleRequestEnterLooby(){
        return false;
    }
    private boolean handleRequestChangeSelectedAvatar(){
        return false;
    }
    private boolean handleRequestChangeSelectedTitle(){
        return false;
    }
    private boolean handleRequestChangeSelectedFrame(){
        return false;
    }
    private boolean handleRequestGetInventory(){
        Inventory inventory;
        forwardInventory(inventory);
        return false;
    }
    private boolean handleRequestGetPlayedDebates(){
        Debate[] debates;
        return false;
        
    }
    private boolean handleRequestGetPastDebates(){
        Debate[] debates;
        return false;
    }
    private boolean handleRequestGetBuyableItems(){
        Item[] items;
        return false;
    }
    
    private boolean forwardUserObject(User user){
        return false;
    }
    private boolean forwardInventory(Inventory inventory){
        return false;
    }
    private boolean forwardPlayedDebates(Debate[] debates){
        return false;
    }
    private boolean forwardPastDebates(Debate[] debates){
        return false;
    }
    private boolean forwardBuyableItems(Item[] items){
        
        return false;
    }
    */
    
    
     
    
    
    

}
