package utilities;

import house.*;

public class AirConditioner extends Utility {

    private double temperatureSetting; // the temperature level of the A.C. (in Celcius)
    private Room room;               // the room that the A.C. machine is in;

    /**
     * Air conditioner constructor
     */
    public AirConditioner() {
        super();
        this.temperatureSetting = 0; // A.C. is initially turned off
    }

    /**
     * Get the AC's current temperature setting
     * @return
     */
    public double getTemperatureLevel() {
        return temperatureSetting;
    }

    /**
     * Set the AC's temperature setting
     * @param temperatureLevel
     */
    public void setTemperatureLevel(double temperatureLevel) {
        this.temperatureSetting = temperatureLevel;
    }

    /**
     * Switch an AC's state to the opposite of its current state.
     */
    public void toggleState() {
        super.toggleState();
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

