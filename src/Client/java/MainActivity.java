package dicomp.debateit;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import SharedModels.*;


public class MainActivity extends AppCompatActivity implements DataReceivable {
    TextView username;
    ListView debateList;
    ImageView imavatar;
    ServerBridge sb;
    ArrayList<Serializable> coming;
    User user;
    ArrayList<Debate> debatesAL;
    String[] output;
    Debate[] debates;
    Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("çalıştı");
        imavatar = (ImageView) findViewById(R.id.avatar);
        username = (TextView) findViewById(R.id.textView2);
        debateList = (ListView) findViewById(R.id.list1);
        output = new String[1];
        output[0] = "not ready";
        user = (User) getIntent().getSerializableExtra("user");
        int avatarID = user.getSelectedAvatar().getItemID();
        String avatarFile = "a" + avatarID;
        int id = getResources().getIdentifier(avatarFile, "drawable", getPackageName());
        imavatar.setImageResource(id);
        /*
        System.out.println(user);
        sb = new ServerBridge(this);
        sb.startListeningToServer();
        sb.requestGetPlayedDebates(user);
        helper = new Helper();
        helper.execute();
        ArrayAdapter<String> veriAdaptoru=new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1, output);
        debateList.setAdapter(veriAdaptoru);*/

        if (user == null) {
            username.setVisibility(View.GONE);
        } else {
            username.setText("Welcome " + user.getUsername());
            username.setVisibility(View.VISIBLE);
        }


    }

    public void changeAvatar(View view) {
        Intent intent = new Intent(this, BattleMenuActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void browseBattle(View view) {
        Intent intent = new Intent(this, BattleMenuActivity.class);
        startActivity(intent);
    }

    public void finishedDebates(View view) {
        Intent intent = new Intent(this, PastDebatesActivity.class);
        startActivity(intent);
    }

    public boolean receiveAndUpdateUI(int responseId,ArrayList<Serializable> responseData) {
        if(responseId == ServerBridge.RESPONSE_USER_OBJECT){}
        return false;
    }

    @Override
    public void updateRetrieveProgress(int progress) {
    }

    private class Helper extends AsyncTask<Void, Void, User> {
        protected User doInBackground(Void... arg0) {
            coming = sb.getLeastRecentlyReceivedData();
            for (int i = 0; i < coming.size(); i++)
                debatesAL.add((Debate) coming.get(i));
            debatesAL.toArray(debates);
            String[] temp = new String[coming.size()];
            for (int i = 0; i < debates.length; i++)
                temp[i] = debates[i].menuDisplay();
            output = temp;
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            return null;
        }
    }
}
