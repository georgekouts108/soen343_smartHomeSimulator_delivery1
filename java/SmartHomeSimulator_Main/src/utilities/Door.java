package utilities;

import house.Room;

/**
 * Door utility class
 */
public class Door extends Utility {

    private Sensor sensor; // a door's sensor
    private boolean isLocked; // is a door locked?

    /**
     * Door constructor
     */
    public Door() {
        super();
        this.sensor = new Sensor();
        this.isLocked = false;
    }

    /**
     * Set a door's state to open (true) or closed (false)
     * @param new_state
     */
    public void setState(boolean new_state) {
        super.setState(new_state);
    }

    /**
     * Check if a door is open or closed.
     * @return
     */
    public boolean getState() {
        return super.getState();
    }

    /**
     * Check if a door is locked.
     * @return
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * Manually change a door's locked state
     * @param locked
     */
    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    /**
     * Get a door's Sensor object instance.
     * @return
     */
    public Sensor getSensor() {
        return this.sensor;
    }

    /**
     * Access a door's Room
     * @return
     */
    public Room getRoom() {
        return super.getRoom();
    }

    /**
     * Access a Door's utility ID
     * @return
     */
    public int getUtilityID() {
        return super.getUtilityID();
    }
}