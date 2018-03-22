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
public class BattleThread implements Runnable{
    
    
    private Debate currentDebate;
    private volatile ArrayList<PlayerHandler> playerHandlers;
    private BattleTimer timer;
    
   public BattleThread(){
       
       playerHandlers = new ArrayList<PlayerHandler>();
       timer = new BattleTimer(60,playerHandlers);
       
       
       
   }
   
   public synchronized void joinNewPlayer(Player newPlayer,Socket socket,ObjectOutputStream out, ObjectInputStream in){
       
       System.out.println("new player joined");
       PlayerHandler newHandler = new PlayerHandler(newPlayer,socket,out,in,this);
       playerHandlers.add(newHandler);
       //new Thread(newHandler).start();
       
   }
   
   public synchronized  void disconnectPlayer(Player player){
       
       int playerId = player.getId();
       
       for(int i=0;i<playerHandlers.size();i++){
           
           PlayerHandler curPlayerHandler =playerHandlers.get(i);
           int curPlayerId = curPlayerHandler.getPlayer().getId();
           
           //Terminating the player handler with matching id and removing from list
           if(playerId == curPlayerId){
               curPlayerHandler.terminate();
               playerHandlers.remove(i);
           }
       }
   }
   
   /**
    * This method calls updateBattle method for each of the playerHandlers.
    * @param responseId
    * @param responseData 
    */
   public synchronized void updatePlayers(int responseId,ArrayList<Serializable> responseData){
       for(int i=0;i<playerHandlers.size();i++){
           playerHandlers.get(i).updateBattle(responseId, responseData);
           
       }
   }
    
    public void run(){
        new Thread(timer).start();    
                          
    }
    
    
    
}
