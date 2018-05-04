package SharedModels;

import java.io.Serializable;
import java.util.Random;

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
    private static final String[] statement0 = {"Alternative medicine is nothing but a lie.", "Marijuana should be legal."};
    private static final String[] statement1 = {"Statism is a better economic model than Neoliberalism.", "Communism is more fair than Capitalism."};
    private static final String[] statement2 = {"Humans have a good and a bad side inborn.", "There is life after death."};
    private static final String[] statement3 = {"Sultan Abdulhamid II was a high-skilled leader.", "Russians have no important effect on history."};
    private static final String[] statement4 = {"Private schools should not be allowed.", "Teachers should be tested regularly."};

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

    public static Idea generateIdea(int cat){
        Random rand = new Random();
        String s;
        int i = rand.nextInt(statement0.length);
        switch (cat){
            case 0: s = statement0[i];
                    break;
            case 1: s = statement1[i];
                    break;
            case 2: s = statement2[i];
                    break;
            case 3: s = statement3[i];
                    break;
            default: s = statement4[i];
                    break;
        }
        return new Idea(cat, s, cat);
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
