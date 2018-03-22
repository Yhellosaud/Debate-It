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
import java.util.ArrayList;
public class BattleTimer implements Runnable {

    private volatile int timer;
    private ArrayList<PlayerHandler>playerHandlers;
    private BattleThread battleThread;

    public BattleTimer(int timer,ArrayList<PlayerHandler> playerHandlers) {

        this.timer = timer;
        this.playerHandlers = playerHandlers;
        
            
    }

    public void run() {

        
        while (true) {
            while (timer != 0) {
                
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                System.out.println(timer);
                
                for(int i=0;i<playerHandlers.size();i++){
                    PlayerHandler curHandler = playerHandlers.get(i);
                    
                    Thread timeSenderThread =new Thread(){
                        public void run(){
                            curHandler.sendCurTimeToClient(timer);
                        }
                    };
                    timeSenderThread.start();
                    
                }
                timer--;
            }            
        }

    }

    public synchronized void startTimer(int timer) {
        this.timer = timer;
    }
    
    public synchronized int getTimer(){
        return timer;
    }
    
    public synchronized void stopTimer(){
        timer = 0;
    }

}
