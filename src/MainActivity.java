package cstudio.client;

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
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView serversMessage = (TextView)findViewById(R.id.serversMessage);



    }

    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.button):
                client=new Client();
                client.execute();
                break;
            case (R.id.button2):
                client.disconnectClient();
                break;
        }

    }
}






