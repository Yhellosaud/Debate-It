package dicomp.debateit;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import SharedModels.*;

public class MarketActivity extends AppCompatActivity {
    MarketAdapter adaptor;
    ListView listItems;
    Button but;
    ArrayList<Item> items;
    ServerBridge sb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        but = (Button)findViewById(R.id.getItems);
        listItems = (ListView)findViewById(R.id.itemList);
        //listItems.setVisibility(View.GONE);

        items = new ArrayList<Item>();
        items.add(Title.getTitle(2));
        items.add(new Avatar(100)); items.add(new Avatar(101)); items.add(new Avatar(102));
        items.add(new Avatar(102)); items.add(Title.getTitle(0));
        adaptor = new MarketAdapter(MarketActivity.this, items);
        listItems.setAdapter(adaptor);
        listItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position, long a){
                Button item = (Button)adapter.getItemAtPosition(position);
                
            }
        });
    }
}
