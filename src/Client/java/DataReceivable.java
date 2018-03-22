package dicomp.debateit;

/**
 * Created by Cagatay on 11.03.2018.
 */

/**
 * UI classes who receive data from server and update their ui according to received data must should this interface.
 */
public interface DataReceivable {

    public boolean receiveAndUpdateUI(Object[] objects);
    public void updateRetrieveProgress(int progress);
}
