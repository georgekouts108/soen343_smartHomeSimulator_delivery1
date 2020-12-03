package utilities;

import house.*;

/**
 * AirConditioner utility class
 */
public class AirConditioner extends Utility {

    private boolean isLocked;

    /**
     * Air conditioner constructor
     */
    public AirConditioner() {
        super();
        this.isLocked = false;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    /**
     * Set an AC's state to open (true) or closed (false)
     * @param new_state
     */
    public void setState(boolean new_state) {
        super.setState(new_state);
    }

    /**
     * Check if an AC machine is open or closed.
     * @return
     */
    public boolean getState() {
        return super.getState();
    }

    /**
     * Access an AC's Room
     * @return
     */
    public Room getRoom() {
        return super.getRoom();
    }

    /**
     * Access an AC's utility ID
     * @return
     */
    public int getUtilityID() {
        return super.getUtilityID();
    }
}

