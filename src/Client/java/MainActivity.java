package dicomp.debateit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import SharedModels.*;


public class MainActivity extends AppCompatActivity implements DataReceivable {
    CustomListViewAdapter adaptor;
    LinearLayout mainScreen;
    TextView username, title;
    ListView debateList;
    ImageView imavatar;
    Button but;
    ServerBridge sb;
    ArrayList<Serializable> coming;
    User user;
    public static final int x = 0;
    ArrayList<Debate> debates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sb = new ServerBridge(this);
        sb.startListeningToServer();
        but = (Button)findViewById(R.id.getDebates);
        mainScreen = (LinearLayout)findViewById(R.id.mainScreen);
        imavatar = (ImageView) findViewById(R.id.avatar);
        username = (TextView) findViewById(R.id.textView2);
        debateList = (ListView) findViewById(R.id.list1);
        title = (TextView)findViewById(R.id.title);

        user = (User) getIntent().getSerializableExtra("user");
        int avatarID = user.getSelectedAvatar().getItemID();
        String avatarFile = "a" + avatarID+"m";
        int id = getResources().getIdentifier(avatarFile, "drawable", getPackageName());
        imavatar.setImageResource(id);
        title.setText(user.getSelectedTitle().getTitleName());
        System.out.println("çalıştı");
        /*
        System.out.println(user);
        sb = new ServerBridge(this);
        sb.startListeningToServer();
        sb.requestGetPlayedDebates(user);
        */

        if (user == null) {
            username.setVisibility(View.GONE);
        } else {
            username.setText("Welcome " + user.getUsername());
            username.setVisibility(View.VISIBLE);
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case(x): {
                if (resultCode == Activity.RESULT_OK) {
                    user = (User)data.getSerializableExtra("user");
                    System.out.println("****************************CASE X - INSIDE IF -- got user" + user.getSelectedAvatar());
                    int avatarID = user.getSelectedAvatar().getItemID();
                    String avatarFile = "a" + avatarID+"m";
                    int id = getResources().getIdentifier(avatarFile, "drawable", getPackageName());
                    imavatar.setImageResource(id);
                    title.setText(user.getSelectedTitle().getTitleName());
                }
                break;
            }
        }
    }
    public void changeAvatar(View view) {
        Intent intent = new Intent(this, MarketActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void browseBattle(View view) {
        Intent intent = new Intent(this, BattleMenuActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void finishedDebates(View view) {
        Intent intent = new Intent(this, PastDebatesActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
    public void goToMarket(View view){
        Intent intent = new Intent(this, MarketActivity.class);
        intent.putExtra("user", user);
        startActivityForResult(intent, x);
    }
    public void getPlayedDebates(View view){
        sb.requestGetPlayedDebates(user);
        but.setVisibility(View.GONE);
    }

    public boolean receiveAndUpdateUI(int responseId,ArrayList<Serializable> responseData) {
        debates = new ArrayList<Debate>();
        for(int i = 0; i < responseData.size(); i++)
            debates.add((Debate)responseData.get(i));
        adaptor = new CustomListViewAdapter(MainActivity.this, debates);
        debateList.setAdapter(adaptor);
        mainScreen.setVisibility(View.VISIBLE);
        sb.disconnectFromServer();
        return false;
    }
    public void seePlayedDebates(View view){
        //sb.requestChangeSelectedAvatar(user,new Avatar(101));
        sb.requestChangeSelectedTitle(user, new Title(2));
    }

    @Override
    public void updateRetrieveProgress(int progress) {
    }
}
