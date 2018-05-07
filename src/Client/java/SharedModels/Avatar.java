package SharedModels;

import java.io.Serializable;
import java.util.*;

public class Avatar extends Item implements Serializable {

    private static final long serialVersionUID = 6L;

    int avatarID;

    public Avatar(int avatarID) {
        super(avatarID);
        this.avatarID = avatarID;
    }

    public Avatar() {
        super();
        avatarID = 0;
    }

    public int getAvatarID() {
        return avatarID;
    }

    public String toString() {
        return super.toString() +
                ", type= Avatar" +
                '}';
    }
}
