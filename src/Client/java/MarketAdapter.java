package dicomp.debateit;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import SharedModels.*;

public class MarketAdapter extends ArrayAdapter<Item> {
    private final LayoutInflater inflater;
    private final Context context;
    private MarketAdapter.ViewHolder holder;
    private final ArrayList<Item> items;

    public MarketAdapter(Context context, ArrayList<Item> items) {
        super(context,0, items);
        this.context = context;
        this.items = items;
        inflater = LayoutInflater.from(context);
    }
    public int getCount(){
        return items.size();
    }

    public Item getItem(int pos){
        return items.get(pos);
    }

    public int getItemID(int pos){
        return items.get(pos).getItemID();
    }
/*
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        holder.replayDebate.setVisibility(View.VISIBLE);
        holder.viewDebate.setVisibility(View.VISIBLE);
        long viewId = view.getId();
        view.setTag(holder);
    }
*/
    public View getView(int position, View convertView, ViewGroup parent) {
        MarketAdapter.ViewHolder holder = new MarketAdapter.ViewHolder();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.market_items, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.select = (Button)convertView.findViewById(R.id.select);
            holder.title = (TextView)convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        }
        else{
            //Get viewholder we already created
            holder = (MarketAdapter.ViewHolder)convertView.getTag();
        }
        Item item = items.get(position);
        if(item != null){
            if(item instanceof Avatar) {
                System.out.println("Avatar DAAAAAAAAAAAAAAAAA");
                int itemID = getItemID(position);
                String avatarFile = "a" + itemID + "l";
                int id = context.getResources().getIdentifier(avatarFile, "drawable", context.getPackageName());
                holder.image.setImageResource(id);
                holder.image.setTag(avatarFile);
                holder.title.setVisibility(View.GONE);
                holder.image.setVisibility(View.VISIBLE);
            }
            else if(item instanceof Title){
                System.out.println("Title EEEEEEEEEEEEEEEEEE");
                int itemID = getItemID(position);
                holder.title.setText(((Title)getItem(position)).getTitleName());
                holder.title.setTag(itemID);
                holder.image.setVisibility(View.GONE);
                holder.title.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }
    //View Holder Pattern for better performance
    public class ViewHolder {
        TextView title;
        ImageView image;
        Button select;
    }
}

