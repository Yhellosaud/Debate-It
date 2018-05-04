package SharedModels;

import java.io.Serializable;

/**
 * Created by Yasin on 11.03.2018.
 */

public class Idea implements Serializable {

    public static final int CATEGORY_HEALTH =0;
    public static final int CATEGORY_ECONOMY =1;
    public static final int CATEGORY_PHILOSOPHY =2;
    public static final int CATEGORY_HISTORY =3;
    public static final int CATEGORY_EDUCATION =4;
    
    
    private static final long serialVersionUID = 3L;
    


    private int ideaID;
    private String statement;
    private int category;

    public Idea() {
        ideaID = 0;
        statement = "";
        category = 0;
    }

    public Idea(int ideaID, String statement, int category) {
        this.ideaID = ideaID;
        this.category = category;
        this.statement = statement;
    }

    public int getIdeaID() {
        return ideaID;
    }

    public String getStatement() {
        return statement;
    }

    public int getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Idea{" +
                "ideaID=" + ideaID +
                ", statement='" + statement + '\'' +
                ", category=" + category +
                '}';
    }
}
