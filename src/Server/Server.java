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
import java.net.*;
import java.io.*;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    public static final String serverIpAccessPoint = "192.168.43.193";
    public static final String serverIpBilkent = "139.179.200.208";
    public static final int serverPort = 54134;
    public static final AtomicInteger numConnectedClients = new AtomicInteger(0);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int numTotalClients = 0;
        
        List<UserHandler> userHandlers = Collections.synchronizedList(new ArrayList<UserHandler>());
        
        

        try {

            ServerSocket serverSocket = new ServerSocket(serverPort, 0, InetAddress.getByName(serverIpBilkent));
            int serverPort = serverSocket.getLocalPort();
            String serverAddress = serverSocket.getLocalSocketAddress().toString();
            System.out.println("Server is running on port: " + serverPort + " and adress: " + serverAddress);

            while (true) {
                Socket newClient = serverSocket.accept();

                System.out.println("Client connected.");
                System.out.println("Address: " + newClient.getRemoteSocketAddress().toString() + " port: " + newClient.getLocalPort());
                UserHandler userHandler = new UserHandler(newClient, numTotalClients);
                //userHandlers.add(userHandler);
                numTotalClients++;

                new Thread(userHandler).start();

            }

        } catch (Exception e) {
            System.out.println("bura hata verdi");
            System.out.println(e.getMessage());
            e.printStackTrace();

        } finally {

        }
    }

    public static void decreaseNumConnectedClients() {
        numConnectedClients.getAndIncrement();

    }

    public static void increaseNumConnectedClients() {
        numConnectedClients.getAndDecrement();

    }
    
    
}
