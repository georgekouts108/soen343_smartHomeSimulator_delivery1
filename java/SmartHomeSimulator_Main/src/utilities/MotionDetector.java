package utilities;


import house.*;

public class MotionDetector extends Utility {

    // if motion detectors are activated for longer this amount of time, alert authorities.
    private int timeBeforeCopsAlert;

    private Room room;

    /**
     * Motion detector constructor
     */
    public MotionDetector() {
        super();
        this.timeBeforeCopsAlert = 0;
    }

    /**
     * Switch a motion detector's state to the opposite of its current state (ex. if triggered, turn it off).
     */
    public void toggleState() {
        super.toggleState();
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
     * Access how much time should pass before alerting authorities
     * @return
     */
    public int getTimeBeforeCopsAlert() {
        return timeBeforeCopsAlert;
    }

    /**
     * Set the amount of time that should pass before alerting authorities
     * @param timeBeforeCopsAlert
     */
    public void setTimeBeforeCopsAlert(int timeBeforeCopsAlert) {
        this.timeBeforeCopsAlert = timeBeforeCopsAlert;
    }

    /**
     * Access a motion detector's Room
     * @return
     */
    public Room getRoom() {
        return super.getRoom();
    }

    /**
     * Change or set a motion detector's room
     * @param new_room
     */
    public void setRoom(Room new_room) {
        super.setRoom(new_room);
    }

    /**
     * Access a motion detector's utility ID
     * @return
     */
    public int getUtilityID() {
        return super.getUtilityID();
    }

}
