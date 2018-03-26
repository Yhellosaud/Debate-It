package dicomp.debateit;

/**
 * Created by Cagatay on 11.03.2018.
 */

import java.io.Serializable;
import java.util.ArrayList;

/**
 * UI classes who receive data from server and update their ui according to received data must should this interface.
 */
public interface DataReceivable {

    public boolean receiveAndUpdateUI(int responseId,ArrayList<Serializable> responseData);
    public void updateRetrieveProgress(int progress);
}
