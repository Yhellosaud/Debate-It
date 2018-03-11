package cstudio.client;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends AsyncTask<Void, Void, Void> {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public static final String serverIpAccessPoint = "192.168.43.193";
    public static final String serverIpBilkent = "139.179.225.19";
    public static final int serverPort =54134;

    @Override
    protected Void doInBackground(Void... arg0) {

        System.out.println("Async task started");
        try {
            socket = new Socket(serverIpBilkent, serverPort);
            System.out.println("Client started");
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            while (true) {
                //out.println("HELLO SERVER");
                String line = in.readLine();
                if(line !=null){
                    System.out.println(line);
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
             disconnectClient();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        super.onPostExecute(result);
    }

    public void disconnectClient() {
        try {
            if(socket!=null){
                socket.close();
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Could not closed socket");
        }
    }

}