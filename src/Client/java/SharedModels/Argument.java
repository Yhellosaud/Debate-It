package SharedModels;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Yasin on 12.04.2018.
 */

public class Argument implements Serializable {

    private static final long serialVersionUID = 5L;

    private int sentTime;
    private int stage;
    private String argument;

    public Argument() {
        sentTime = 0;
        stage = 0;
        argument = "";
    }

    public Argument(int sentTime, int stage, String argument) {
        this.sentTime = sentTime;
        this.stage = stage;
        this.argument = argument;
    }

    public int getSentTime() {
        return sentTime;
    }

    public int getStage() {
        return stage;
    }

    public String getArgument() {
        return argument;
    }

    public String toString() {
        return "Argument{" +
                "sentTime=" + sentTime +
                ", stage='" + stage + '\'' +
                ", argument='" + argument + '\'' +
                '}';
    }
}