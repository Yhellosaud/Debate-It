package SharedModels;

import java.io.Serializable;
import java.util.*;

public class Expression implements Serializable {
    private int expressionID;

    public Expression(int expressionID) {
        this.expressionID = expressionID;
    }

    public Expression() {
        expressionID = 0;
    }

    public String toString() {
        return "Expression{" +
                "expressionID=" + expressionID +
                '}';
    }

    public int getExpressionID(){
        return expressionID;
    }
}
