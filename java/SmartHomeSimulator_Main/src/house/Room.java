package house;
import utilities.*;
import sample.*;

public class Room {

    private static int ID_count = 1;

    private String name; // e.g. "Garage", etc.
    private int roomID;
    private int numberOfWindows;
    private int numberOfLights;
    private int numberOfDoors;
    private int numberOfPeopleInside;
    private boolean isVacant;
    private double temperature; /**TODO: wait until after delivery 1*/

    /** ROOM UTILITIES */

    private AirConditioner ac;
    private MotionDetector md;
    private Door[] doorCollection;
    private Light[] lightCollection;
    private Window[] windowCollection;

    public Room(String roomName, int numberOfDoors, int numberOfWindows, int numberOfLights,
                AirConditioner ac) {
        this.name = roomName;
        this.roomID = (ID_count++);
        this.numberOfDoors = numberOfDoors;
        this.numberOfWindows = numberOfWindows;
        this.numberOfLights = numberOfLights;
        this.numberOfPeopleInside = 0;
        this.isVacant = true;
        this.temperature = 0;
        this.ac = ac;
        this.md = new MotionDetector();
        this.doorCollection = new Door[numberOfDoors];
        this.lightCollection = new Light[numberOfLights];
        this.windowCollection = new Window[numberOfWindows];
    }
    /** ROOM INFORMATION */
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getRoomID() {
        return roomID;
    }
    public double getTemperature() {
        return temperature;
    }
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    /** ROOM VACANCY */
    public int getNumberOfPeopleInside() {
        return numberOfPeopleInside;
    }
    public void setNumberOfPeopleInside(int numberOfPeopleInside) {
        this.numberOfPeopleInside = numberOfPeopleInside;
    }
    public boolean isVacant() {
        return isVacant;
    }
    public void updateVacantStatus() {
        if (this.numberOfPeopleInside > 0) {
            this.isVacant = false;
        }
        else {
            this.isVacant = true;
        }
    }

    /**TODO: Implement methods for adding a window to a Room (repeat for Lights and Doors)
     * TODO-- make sure you add the new objects to the appropriate array collections */

    /** ROOM UTILITIES INFO */

    public int getNumberOfWindows() {
        return numberOfWindows;
    }
    public int getNumberOfLights() {
        return numberOfLights;
    }
    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfWindows(int numberOfWindows) {
        this.numberOfWindows = numberOfWindows;
    }
    public void setNumberOfLights(int numberOfLights) {
        this.numberOfLights = numberOfLights;
    }
    public void setNumberOfDoors(int numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    public AirConditioner getAc() {
        return ac;
    }
    public MotionDetector getMd() {
        return md;
    }
    public Door[] getDoorCollection() {
        return doorCollection;
    }
    public Light[] getLightCollection() {
        return lightCollection;
    }
    public Window[] getWindowCollection() {
        return windowCollection;
    }

    /**TODO: Implement methods for changing the state of Lights, Doors, and Windows within their array collections */

    /** MISCELLANEOUS */

    public String toString() {
        return "Room ID: #"+this.roomID+"\nRoom Name: "+this.name+"\nNumber of Doors: "+this.numberOfDoors+
                "\nNumber of Windows: "+this.numberOfWindows+"\nNumber of Lights: "+this.numberOfLights+"";
    }

}