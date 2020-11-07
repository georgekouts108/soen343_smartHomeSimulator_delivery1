package utilities;



import house.Room;

public class Light extends Utility {

    private boolean isLocked;

    /**
     * Light constructor
     */
    public Light() {
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
     * Set a light's state to open (true) or closed (false)
     * @param new_state
     */
    public void setState(boolean new_state) {
        super.setState(new_state);
    }

    /**
     * Check if a light is open or closed.
     * @return
     */
    public boolean getState() {
        return super.getState();
    }

    /**
     * Access a light's Room
     * @return
     */
    public Room getRoom() {
        return super.getRoom();
    }

    /**
     * Access a Light's utility ID
     * @return
     */
    public int getUtilityID() {
        return super.getUtilityID();
    }

}
