package SharedModels;

import java.io.*;

public abstract class Item implements Serializable {
    int itemID;

    public Item(int itemID) {
        this.itemID = itemID;
    }

    public Item() {
        itemID = 0;
    }

    public int getItemID() {
        return itemID;
    }

    public String toString() {
        return "Item{" +
                "itemID=" + itemID;
    }
}