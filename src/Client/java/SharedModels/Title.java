package SharedModels;

import java.io.Serializable;
import java.util.*;

public class Title extends Item implements Serializable {

    private static final long serialVersionUID = 7L;

    private static final String[] titles = {"Master", "King", "Socrates", "Sarkozy"};

    private int titleID;
    private String titleName;

    public Title(int titleID, String titleName) {
        super(titleID);
        this.titleName = titleName;
    }

    public int getTitleID() {
        return titleID;
    }

    public String getTitleName() {
        return titleName;
    }

    public static Title getTitle(int val){
        switch (val){
            case 0: return new Title(0, titles[0]);
            case 1: return new Title(1, titles[1]);
            case 2: return new Title(2, titles[2]);
            default: return new Title(3, titles[3]);
        }
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