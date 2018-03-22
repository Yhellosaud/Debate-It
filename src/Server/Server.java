/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author cagatay.sel-ug
 */
import SharedModels.User;
import java.net.*;
import java.io.*;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is the main thread of server. This thread accepts user connections and
 * creates userhandler to handle user requests.
 *
 * @author Cagatay
 */
public class Server {

    public static final String serverIpAccessPoint = "192.168.43.193";
    public static final String serverIpBilkent = "139.179.227.149";
    public static final String serverIpEv = "192.168.1.43";
    public static final int serverPort = 54134;
    public static final AtomicInteger numConnectedClients = new AtomicInteger(0);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int numTotalConnections = 0;
        ServerSocket serverSocket = null;
        BattleThread battleThread = null;
        //List<UserHandler> userHandlers = Collections.synchronizedList(new ArrayList<UserHandler>());    

        try {

            //Starting server
            serverSocket = new ServerSocket(serverPort, 0, InetAddress.getByName(serverIpEv));
            int serverPort = serverSocket.getLocalPort();
            String serverAddress = serverSocket.getLocalSocketAddress().toString();
            System.out.println("Server is running on port: " + serverPort + " and adress: " + serverAddress);
            
            battleThread = new BattleThread();
            new Thread(battleThread).start();
            System.out.println("Battle started");
            while (true) {

                //Accepting clients
                Socket newClient = serverSocket.accept();
                System.out.println("Client connected.");
                System.out.println("Address: " + newClient.getRemoteSocketAddress().toString() + " port: " + newClient.getLocalPort());
                UserHandler userHandler = new UserHandler(newClient, numTotalConnections,battleThread);
                new Thread(userHandler).start();
                //userHandlers.add(userHandler);
                numTotalConnections++;

            }

        } catch (Exception e) {
            System.out.println("bura hata verdi");
            System.out.println(e.getMessage());
            e.printStackTrace();

        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                    System.out.println("Server closed");
                }
            } catch (Exception e) {
                System.out.println("Couldn't closed server socket.");
            }
        }
    }

    public static void increaseNumConnectedClients() {
        numConnectedClients.getAndIncrement();

    }

    public static void decreaseNumConnectedClients() {
        numConnectedClients.getAndDecrement();

    }

}
