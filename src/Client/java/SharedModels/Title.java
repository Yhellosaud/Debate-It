package SharedModels;

import java.io.Serializable;
import java.util.*;

public class Title extends Item implements Serializable {
    int titleID;
    String title;

    public Title(int titleID, String title) {
        super(titleID);
        this.title = title;
    }

    public Title() {
        super();
    }

    public String toString() {
        return super.toString() +
                ", type= Title" +
                ", title:" + title +
                '}';
    }
}