package utilities;



public class Sensor extends Utility {

    /**
     * Sensor constructor
     */
    public Sensor() {
        super();
    }

    /**
     * Switch a sensor's state to the opposite of its current state (ex. if triggered, turn it off).
     */
    public void toggleState() {
        super.toggleState();
    }

    /**
     * Set a sensor's state to open (true) or closed (false)
     * @param new_state
     */
    public void setState(boolean new_state) {
        super.setState(new_state);
    }

    /**
     * Check if a sensor is open or closed.
     * @return
     */
    public boolean getState() {
        return super.getState();
    }

    /**
     * Access a Sensor's utility ID
     * @return
     */
    public int getUtilityID() {
        return super.getUtilityID();
    }
}
