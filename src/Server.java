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

public class Server {
    
    public static final String serverIpAccessPoint = "192.168.43.193";
    public static final String serverIpBilkent = "139.179.200.208";
    public static final int serverPort =54134;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int numOfClients = 0;
        
        try {

            ServerSocket serverSocket = new ServerSocket(serverPort, 0, InetAddress.getByName(serverIpBilkent));
            int serverPort = serverSocket.getLocalPort();
            String serverAddress = serverSocket.getLocalSocketAddress().toString();
            System.out.println("Server is running on port: "+serverPort +" and adress: "+serverAddress);

            while (numOfClients != 4) {
                Socket newClient = serverSocket.accept();
                System.out.println("Client connected.");
                System.out.println("Address: "+newClient.getRemoteSocketAddress().toString()+" port: "+newClient.getLocalPort());
                new UserHandler(newClient).start();
                
            }            

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            
        }
    }
}
