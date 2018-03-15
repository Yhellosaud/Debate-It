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

    public int getIdeaID() {
        return ideaID;
    }

    public void setIdeaID(int ideaID) {
        this.ideaID = ideaID;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
