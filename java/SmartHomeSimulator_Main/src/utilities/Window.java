package utilities;
import house.*;

/**
 * Window utility class
 */
public class Window extends Utility {

    private boolean isBlocked;

    /**
     * Window constructor
     */
    public Window() {
        super();
        this.isBlocked = false;
    }

    /**
     * Return a boolean indicating if a window is blocked
     * @return
     */
    public boolean isBlocked() {
        return isBlocked;
    }

    /**
     * Set a window's blocked state
     * @param blocked
     */
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
     * Access a window's Room
     * @return
     */
    public Room getRoom() {
        return super.getRoom();
    }

    /**
     * Access a Window's utility ID
     * @return
     */
    public int getUtilityID() {
        return super.getUtilityID();
    }

}
