package SharedModels;

import java.io.Serializable;
import java.util.*;

public class Title extends Item implements Serializable {
    int titleID;

    public Title(int titleID) {
        super(titleID);
    }

    public Title() {
        super();
    }

    public String toString() {
        return super.toString() +
                ", type= Title" +
                '}';
    }
}