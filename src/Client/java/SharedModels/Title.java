package SharedModels;

import java.io.Serializable;
import java.util.*;

public class Title extends Item implements Serializable {

    private static final long serialVersionUID = 7L;

    private static final String[] titles = {"Master", "King", "Socrates", "Sarkozy"};


    private String titleName;

    public Title(int titleID, String titleName) {
        super(titleID);
        this.titleName = titleName;
    }

    public int getTitleID() {
        return super.getItemID();
    }

    public String getTitleName() {
        return titleName;
    }

    public static Title getTitle(int titleID){
        return new Title(titleID, titles[titleID - 1]);
    }
    public ArrayList<Title> getAllTitles(){
        ArrayList<Title> titlesAL= new ArrayList<Title>();
        for(int i = 0; i < titles.length; i++)
            titlesAL.add(new Title(i, titles[i]));
        return titlesAL;
    }

    public Title() {
        super();
    }

    public String toString() {
        return super.toString() +
                ", type= Title" +
                ", title:" + titleName +
                '}';
    }
}
