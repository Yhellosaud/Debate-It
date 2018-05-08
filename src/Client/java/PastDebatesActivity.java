package dicomp.debateit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import SharedModels.Debate;
import SharedModels.Idea;
import SharedModels.Player;

public class PastDebatesActivity extends AppCompatActivity implements DataReceivable {
    CustomListViewAdapter adaptor;
    ListView pastDebates;
    Button but;
    ArrayList<Debate> debates;
    ServerBridge sb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_debates);
        but = (Button)findViewById(R.id.getDebates);
        pastDebates = (ListView)findViewById(R.id.debateList);
        pastDebates.setVisibility(View.GONE);

        sb = new ServerBridge(this);
        sb.startListeningToServer();
        /*
        debates = new ArrayList<Debate>();
        Player p1 = new Player(0, "ali", 1, new ArrayList<Argument>(), 1, 1, 1);
        Player p2 = new Player(0, "veli", 1, new ArrayList<Argument>(), 1, 1, 1);
        Player p3 = new Player(0, "49", 1, new ArrayList<Argument>(), 1, 1, 1);
        Player p4 = new Player(0, "ali", 1, new ArrayList<Argument>(), 1, 1, 1);
        ArrayList<Player> playerAL = new ArrayList<Player>();
        playerAL.add(p1); playerAL.add(p2); playerAL.add(p3); playerAL.add(p4);
        Idea idea = new Idea(1, "Euthanasia should be legal.", 1);
        Debate d1 = new Debate(idea,playerAL,0,30, 20, 10, 6, 6, 6, 6);
        debates.add(d1); debates.add(d1); debates.add(d1);

        adaptor = new CustomListViewAdapter(PastDebatesActivity.this, debates);
        //String[] debates = {d1.menuDisplay(), d1.menuDisplay(), d1.menuDisplay(), d1.menuDisplay(), d1.menuDisplay(), d1.menuDisplay()};

        pastDebates.setAdapter(adaptor);
        */
    }


    @Override
    public boolean receiveAndUpdateUI(int responseId,ArrayList<Serializable> responseData) {
        debates = new ArrayList<Debate>();
        for(int i = 0; i < responseData.size(); i++)
            debates.add((Debate)responseData.get(i));
        adaptor = new CustomListViewAdapter(PastDebatesActivity.this, debates);
        pastDebates.setAdapter(adaptor);
        pastDebates.setVisibility(View.VISIBLE);

        sb.disconnectFromServer();
        return false;
    }

    public void getPastDebates(View view){
        sb.requestGetPastDebates();
        but.setVisibility(View.GONE);
    }

    @Override
    public void updateRetrieveProgress(int progress) {

    }
}