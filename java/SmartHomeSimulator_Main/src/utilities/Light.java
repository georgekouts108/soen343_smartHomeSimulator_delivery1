package utilities;



import house.Room;

public class Light extends Utility {

    private Room room; // the room that the window is located in.

    /**
     * Light constructor
     */
    public Light() {
        super();
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
     * Change or set a light's room
     * @param new_room
     */
    public void setRoom(Room new_room) {
        super.setRoom(new_room);
    }

    /**
     * Access a Light's utility ID
     * @return
     */
    public int getUtilityID() {
        return super.getUtilityID();
    }

}
