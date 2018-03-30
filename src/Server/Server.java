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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This is the main thread of server. This thread accepts user connections and
 * creates userhandler to handle user requests.
 *
 * @author Cagatay
 */
public class Server {

    public static final String serverIpAccessPoint = "192.168.43.193";
    public static final String serverIpBilkent = "139.179.224.73";
    public static final String serverIpEv = "192.168.1.42";
    public static final String databaseIp = "139.179.227.218";
    public static final String databasePort = "3306";

    public static final int serverPort = 54134;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int numTotalConnections = 0;
        ServerSocket serverSocket = null;
        BattleThread battleThread = null;

        DebateManager dm = null;
        //ItemManager im = null;
        UserManager um = null;
        Connection conn = null;

        ArrayList<Integer> pastDebateIDs = new ArrayList<Integer>();
        ArrayList<Integer> votedDebates = new ArrayList<Integer>();
        pastDebateIDs.add(23);
        pastDebateIDs.add(24);
        votedDebates.add(33);
        votedDebates.add(34);
        User user1 = new User("Cagatay", "password", 1, pastDebateIDs, votedDebates);

        //////////////////////////////////////////Establishing connection to database///////////////////////////////////
        /*System.out.println("Connecting database...");
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            //String url = "jdbc:mysql://139.179.227.218:3306/diDatabase";
            String connectionURL = "jdbc:mysql://139.179.227.218:3306/diDatabase?verifyServerCertificate=false&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Turkey";
            String userName = "root";
            String pas = "dicomp319";
            conn = DriverManager.getConnection(connectionURL, userName, pas);
            // Do something with the Connection

            dm = new DebateManager(conn);
            //im = new ItemManager(connection);
            um = new UserManager(conn);

            System.out.println("Database connection established!");

        } catch (SQLException ex) {
            // handle any errors
            ex.printStackTrace();
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (Exception e) {
            System.out.println("Could not connected to database");
            e.printStackTrace();
        }*/
        ///////////////////////////////////////////////Starting server///////////////////////////////////////////////////////////////
        try {

            //Opening server socket
            serverSocket = new ServerSocket(serverPort, 0, InetAddress.getByName(serverIpBilkent));
            int serverPort = serverSocket.getLocalPort();
            String serverAddress = serverSocket.getLocalSocketAddress().toString();
            System.out.println("Server is running on port: " + serverPort + " and adress: " + serverAddress);

            //Starting battle thread      
            battleThread = new BattleThread();
            new Thread(battleThread).start();
            System.out.println("Battle started");

            //Accepting clients
            while (true) {

                Socket newClient = serverSocket.accept();
                System.out.println("Client connected.");
                System.out.println("Address: " + newClient.getRemoteSocketAddress().toString() + " port: " + newClient.getLocalPort() + "\n");
                UserHandler userHandler = new UserHandler(newClient, numTotalConnections, battleThread);
                new Thread(userHandler).start();
                //userHandlers.add(userHandler);
                numTotalConnections++;
            }

        } catch (Exception e) {
            System.out.println("Could not opened the server");
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

}
