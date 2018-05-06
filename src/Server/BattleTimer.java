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
import java.io.Serializable;
import java.util.ArrayList;

public class BattleTimer implements Runnable {

    private volatile int timer;
    private volatile boolean running;
    private volatile boolean counting;
    private volatile ArrayList<PlayerHandler> playerHandlers;
    private BattleThread battleThread;

    public BattleTimer(BattleThread battleThread, ArrayList<PlayerHandler> playerHandlers) {

        this.battleThread = battleThread;
        this.playerHandlers = playerHandlers;
        this.timer = 0;
        this.running = true;
        this.counting = false;

    }

    /**
     * Timer stops when counting is done.
     */
    public void run() {

        while (running) {

            while (counting) {
                while (timer != 0 && counting) {

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                    System.out.println("Battle time: "+timer);

                    for (int i = 0; i < playerHandlers.size(); i++) {
                        PlayerHandler curHandler = playerHandlers.get(i);

                        Thread timeSenderThread = new Thread() {
                            public void run() {
                                ArrayList<Serializable> responseParams = new ArrayList<Serializable>();
                                responseParams.add(timer);
                                curHandler.updatePlayer(PlayerHandler.RESPONSE_BATTLE_TIME, responseParams);
                            }
                        };
                        timeSenderThread.start();
                    }
                    timer--;
                }
                //Going to next stage
                stopTimer();
                battleThread.nextStage();
                
            }

        }

    }

    /**
     * This method start timer if the thread is running.
     * @param timer 
     */
    public void startTimer(int timer) {

        this.counting = true;
        this.timer = timer;
    }

    public synchronized int getTimer() {
        return timer;
    }

    /**
     * This method stops counting.
     */
    public void stopTimer() {
        timer = 0;
        this.counting = false;

    }

    /**
     * This method terminates the run method
     */
    public void terminate() {
        stopTimer();
        this.running = false;
        
    }

}
