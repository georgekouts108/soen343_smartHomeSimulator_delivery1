package utilities;
import house.*;

/**
 * MotionDetector utility class
 */
public class MotionDetector extends Utility {

    /**
     * Motion detector constructor
     */
    public MotionDetector() {
        super();
    }

    /**
     * Set a motion detector's state to triggered (true) or off (false)
     * @param new_state
     */
    public void setState(boolean new_state) {
        super.setState(new_state);
    }

    /**
     * Check if a motion detector is currently triggered.
     * @return
     */
    public boolean getState() {
        return super.getState();
    }

    /**
     * Access a motion detector's Room
     * @return
     */
    public Room getRoom() {
        return super.getRoom();
    }

    /**
     * Access a motion detector's utility ID
     * @return
     */
    public int getUtilityID() {
        return super.getUtilityID();
    }
}
