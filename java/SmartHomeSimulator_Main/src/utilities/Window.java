package utilities;

import house.*;

public class Window extends Utility {

    private boolean isOpening; // is a window in the middle of opening?
    private boolean isClosing; // is a window in the middle of closing?
    private final int TIME_TO_OPEN_OR_CLOSE = 5000; // it takes 5 seconds for a window to open or close
    private Sensor sensor; // a window's sensor
    private Room room; // the room that the window is located in.
    private boolean isBlocked;

    /**
     * Window constructor
     */
    public Window() {
        super();
        this.isOpening = false;
        this.isClosing = false;
        this.sensor = new Sensor();
        this.isBlocked = false;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    /**
     * Set a window's state to open (true) or closed (false)
     * @param new_state
     */
    public void setState(boolean new_state) {
        super.setState(new_state);
    }

    /**
     * Check if a window is open or closed.
     * @return
     */
    public boolean getState() {
        return super.getState();
    }

    /**
     * Toggle between opening and closing transitions of a window, in the case of a disturbance.
     */
    public void toggleTransition() {
        if (this.isClosing) {
            this.isClosing = false;
            this.isOpening = true;
        }
        else {
            this.isClosing = true;
            this.isOpening = false;
        }
    }

    /**
     * Set a window's transition state to Opening
     * @param is_opening
     */
    public void setOpening(boolean is_opening) {
        this.isOpening = is_opening;
    }

    /**
     * Set a window's transition state to Closing
     * @param is_closing
     */
    public void setClosing(boolean is_closing) {
        this.isClosing = is_closing;
    }

    /**
     * See if a window is currently opening.
     * @return
     */
    public boolean getIsOpening() {
        return this.isOpening;
    }

    /**
     * See if a window is currently closing.
     * @return
     */
    public boolean getIsClosing() {
        return this.isClosing;
    }

    /**
     * Retrieve a window's Sensor object instance.
     * @return
     */
    public Sensor getSensor() {
        return this.sensor;
    }

    /**
     * Access a window's Room
     * @return
     */
    public Room getRoom() {
        return super.getRoom();
    }

    /**
     * Change or set a window's room
     * @param new_room
     */
    public void setRoom(Room new_room) {
        super.setRoom(new_room);
    }

    /**
     * Retrieve the time needed to open or close a window
     * @return
     */
    public int getTIME_TO_OPEN_OR_CLOSE() {
        return this.TIME_TO_OPEN_OR_CLOSE;
    }

    /**
     * Access a Window's utility ID
     * @return
     */
    public int getUtilityID() {
        return super.getUtilityID();
    }

}
