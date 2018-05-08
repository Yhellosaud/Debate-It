package dicomp.debateit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import SharedModels.*;

public class MarketActivity extends AppCompatActivity implements DataReceivable {
    MarketAdapter avatarAdaptor, titleAdaptor;
    ListView listAvatars, listTitles;
    Button showAvatars, showTitles;
    ArrayList<Item> avatars;
    ArrayList<Item> titles;
    ServerBridge sb;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        sb = new ServerBridge(this);
        sb.startListeningToServer();
        avatars = new ArrayList<Item>();
        titles = new ArrayList<Item>();
        showAvatars = (Button)findViewById(R.id.getAvatars);
        showTitles = (Button)findViewById(R.id.getTitles);
        user = (User) getIntent().getSerializableExtra("user");

        listAvatars = (ListView)findViewById(R.id.avatarList);
        avatarAdaptor = new MarketAdapter(MarketActivity.this, avatars);
        listAvatars.setAdapter(avatarAdaptor);

        listTitles = (ListView)findViewById(R.id.titleList);
        titleAdaptor = new MarketAdapter(MarketActivity.this, titles);
        listTitles.setAdapter(titleAdaptor);

        listAvatars.setVisibility(View.GONE);
        listTitles.setVisibility(View.GONE);
        avatars.add(new Avatar(100)); avatars.add(new Avatar(101)); avatars.add(new Avatar(102));
        avatars.add(new Avatar(103));
        titles.add(Title.getTitle(1)); titles.add(Title.getTitle(2)); titles.add(Title.getTitle(3));
        titles.add(Title.getTitle(4));

        listAvatars.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position, long a){
                Avatar avatar = (Avatar)avatarAdaptor.getItem(position);
                user.changeSelectedAvatar(avatar);
                System.out.println("***************" + user.getSelectedAvatar());
            }
        });
    }

    public void seeAvatars(View view){
        listAvatars.setVisibility(View.VISIBLE);
        listTitles.setVisibility(View.GONE);
    }
    public void changeItem(View view){
        int avatarID, titleID;
        LinearLayout vwParentRow = (LinearLayout)view.getParent();
        TextView title = (TextView)vwParentRow.getChildAt(0);
        ImageView avatar = (ImageView)vwParentRow.getChildAt(1);
        if(title.getVisibility() == View.GONE && avatar.getVisibility() == View.VISIBLE) {
            avatarID = Integer.parseInt(((String) avatar.getTag()).substring(1, 4));
            Avatar av = new Avatar(avatarID);
            user.changeSelectedAvatar(av);
            System.out.println(user.getSelectedAvatar());
            sb.requestChangeSelectedAvatar(user, av);
        }
        else if(title.getVisibility() == View.VISIBLE && avatar.getVisibility() == View.GONE){
            titleID = (int)title.getTag();
            Title tit = new Title(titleID);
            user.changeSelectedTitle(tit);
            System.out.println(user.getSelectedTitle());
            sb.requestChangeSelectedTitle(user, tit);
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra("user", user);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
        //vwParentRow.refreshDrawableState();
    }

    public void seeTitles(View view){
        listAvatars.setVisibility(View.GONE);
        listTitles.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean receiveAndUpdateUI(int responseId, ArrayList<Serializable> responseData) {
        return false;
    }

    @Override
    public void updateRetrieveProgress(int progress) {

    }
}
