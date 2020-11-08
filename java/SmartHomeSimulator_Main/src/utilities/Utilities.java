package utilities;
import house.*;
import sample.*;

/**
 * Interface for Utilities
 */
public interface Utilities {

    /**
     * Return a utility's ID
     * @return
     */
    public abstract int getUtilityID();

    /**
     * Return a utility's room location
     * @return
     */
    public abstract Room getRoom();

    /**
     * Configure a utility's state
     * @param state
     */
    public abstract void setState(boolean state);

    /**
     * Return a utility's state
     * @return
     */
    public abstract boolean getState();

}
