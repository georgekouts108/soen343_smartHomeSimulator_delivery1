package utilities;
import house.*;

/**
 * Utility class
 */
public class Utility implements Utilities {

    private static int ID_count = 1;
    private boolean isOpen; // for example, is a window or door open, A.C. turned on, sensor triggered, etc.
    private Room room; // the room that the utility is located within, if any
    private int utility_ID; // the ID of the utility

    /**
     * Utility constructor
     */
    public Utility() {
        this.isOpen = false; // by default, windows, doors, lights, A.C. are all closed.
        this.room = null; // by default, a utility should have no valid room (a 'null' room)
        this.utility_ID = (ID_count++); // each instantiated utility has a unique ID (can never be changed).
    }

    /**
     * Return the state of the utility (is is activated or not?)
     * @return
     */
    public boolean getState() {
        return this.isOpen;
    }

    /**
     * manually switch the state
     * @param state
     */
    public void setState(boolean state) {
        this.isOpen = state;
    }

    /**
     * Access the Room that the utility is located in
     * (for MotionDetector, AirConditioner, Light, and Window objects only)
     * @return
     */
    public Room getRoom() {

        if ( (this instanceof MotionDetector) || (this instanceof AirConditioner) ||
                (this instanceof Light) || (this instanceof Window) ) {
            return this.room;
        }
        else {
            return null;
        }
    }

    /**
     * Access a Utility's ID
     * @return
     */
    public int getUtilityID() {
        return this.utility_ID;
    }

}
