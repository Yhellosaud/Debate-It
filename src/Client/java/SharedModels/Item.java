package SharedModels;

import java.awt.image.*;
import java.io.*;
public abstract class Item{
  int itemID;
  
  public Item(int itemID){
    this.itemID = itemID;
  }
  
  public String toString(){
    return "Item{" +
                "itemID=" + itemID;
  }
}