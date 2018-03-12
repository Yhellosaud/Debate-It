package dicomp.debateit;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements DataReceivable {

    Client client;
    TextView serversMessage;
    private ServerBridge sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serversMessage = (TextView) findViewById(R.id.serversMessage);
    }


    /**
     * This method will update to ui with respect to data coming from the server.
     *
     * @param objects
     * @return
     */
    public boolean receiveAndUpdateUI(Object[] objects) {
        return false;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.button):
                sb = new ServerBridge(this);
                String[] requestParams = {"param1", "param2"};
                sb.request(ServerBridge.REQUEST_REGISTER, requestParams);
                ArrayList<Object> receivedData = sb.getLeastRecentlyReceivedData();

                if (receivedData != null) {
                    serversMessage.setText(receivedData.get(0).toString());
                } else {
                    System.out.println("Received data null in main menu2");
                }

                break;
            case (R.id.button2):

                ArrayList<Object> receivedData2 = sb.getLeastRecentlyReceivedData();

                if (receivedData2 != null) {
                    serversMessage.setText(receivedData2.get(0).toString());
                } else {
                    System.out.println("Received data null in main menu2");
                }
                break;

        }

    }
}






