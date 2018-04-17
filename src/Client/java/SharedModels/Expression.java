package SharedModels;

import java.io.Serializable;
import java.util.*;

public class Expression implements Serializable{
  int expressionID;
  public Expression(int titleID){
    this.expressionID = expressionID;
  }
  
  public String toString(){
    return "Expression{" +
                "expressionID=" + expressionID +
                '}';
  }
}