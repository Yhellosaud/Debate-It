package dicomp.debateit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import SharedModels.Debate;
import SharedModels.Idea;
import SharedModels.Player;

public class PastDebatesActivity extends AppCompatActivity implements DataReceivable {

    ListView pastDebates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_debates);
        Player p1 = new Player("ali");
        Player p2 = new Player("veli");
        Player p3 = new Player("49");
        Player p4 = new Player("50");
        ArrayList<Player> playerAL = new ArrayList<Player>();
        playerAL.add(p1); playerAL.add(p2); playerAL.add(p3); playerAL.add(p4);
        Idea idea = new Idea(1, "Euthanasia should be legal.", 1);
        Debate d1 = new Debate(idea,playerAL);
        String[] debates = {d1.menuDisplay(), d1.menuDisplay(), d1.menuDisplay(), d1.menuDisplay(), d1.menuDisplay(), d1.menuDisplay()};
        pastDebates = (ListView)findViewById(R.id.debateList);

        ArrayAdapter<String> veriAdaptoru=new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1, debates);
        pastDebates.setAdapter(veriAdaptoru);
    }


    @Override
    public boolean receiveAndUpdateUI(Object[] objects) {
        return false;
    }

    @Override
    public void updateRetrieveProgress(int progress) {

    }
}

