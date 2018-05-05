package dicomp.debateit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;

import SharedModels.Debate;
import SharedModels.User;

public class BrowseBattleActivity extends AppCompatActivity implements DataReceivable{

    private User user;
    private ServerBridge sb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_battle);

        user = (User) getIntent().getSerializableExtra("user");
        Button browseButton = (Button) findViewById(R.id.joinBattle);
        sb = new ServerBridge(this);
        sb.startListeningToServer();


    }


    /**
     * This method handles all the button presses.
     * @param v
     */
    public void onClick(View v) {

        if (v.getId() == R.id.joinBattle ) {
            sb.requestJoinBattle(user);

        }
    }

    @Override
    public boolean receiveAndUpdateUI(int responseId, ArrayList<Serializable> responseData) {


        if(responseId == ServerBridge.RESPONSE_UPDATED_DEBATE){
            Intent myIntent = new Intent(getApplicationContext(), BattleMenuActivity.class);
            Debate debate = (Debate)responseData.get(0);
            myIntent.putExtra("DEBATE",debate);
            myIntent.putExtra("user",user);
            startActivity(myIntent);

        }


        return false;
    }

    @Override
    public void updateRetrieveProgress(int progress) {

    }
}
