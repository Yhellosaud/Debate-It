package SharedModels;

import java.io.Serializable;
import java.util.*;

public class Avatar extends Item implements Serializable{
  int avatarID;
  public Avatar(int avatarID){
    super(avatarID);
  }
  
  public String toString(){
    return super.toString() +
                ", type= Avatar" +
                '}'; 
  }
}