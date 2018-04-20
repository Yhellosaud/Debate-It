package SharedModels;

import java.io.Serializable;
import java.util.*;

public class Frame extends Item implements Serializable {
    int frameID;

    public Frame(int frameID) {
        super(frameID);
    }

    public Frame() {
        super();
    }

    public String toString() {
        return super.toString() +
                ", type= Frame" +
                '}';
    }
}