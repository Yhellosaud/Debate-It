package dicomp.debateit;

import java.io.Serializable;

/**
 * Created by Yasin on 11.03.2018.
 */

public class Idea implements Serializable {
    private int ideaID;
    private String statement;
    private int category;

    public Idea(int ideaID, String statement, int category){
        this.ideaID = ideaID;
        this.category = category;
        this.statement = statement;
    }
}
