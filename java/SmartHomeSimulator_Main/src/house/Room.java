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
    private double temperature;

    /** ROOM UTILITIES */

    private AirConditioner ac;
    private MotionDetector md;
    private Door[] doorCollection;
    private Light[] lightCollection;
    private Window[] windowCollection;

    public Room(String roomName, int numberOfDoors, int numberOfWindows, int numberOfLights, boolean AC)
    {
        this.roomID = (ID_count++);
        this.name = roomName;
        this.numberOfDoors = numberOfDoors;
        this.numberOfWindows = numberOfWindows;
        this.numberOfLights = numberOfLights;
        this.numberOfPeopleInside = 0;
        this.isVacant = true;
        this.temperature = 0;
        this.md = new MotionDetector();
        if (AC) {
            this.ac = new AirConditioner();
        }
        else {
            this.ac = null;
        }

        this.doorCollection = new Door[numberOfDoors];

        for (int d = 0; d < this.doorCollection.length; d++) {
            this.doorCollection[d] = new Door();
        }

        this.lightCollection = new Light[numberOfLights];

        for (int L = 0; L < this.lightCollection.length; L++) {
            this.lightCollection[L] = new Light();
        }

        this.windowCollection = new Window[numberOfWindows];

        for (int w = 0; w < this.windowCollection.length; w++) {
            this.windowCollection[w] = new Window();
        }
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

    public void setNumberOfPeopleInside(int numberOfPeopleInside) {
        this.numberOfPeopleInside = numberOfPeopleInside;
    }
    /** ROOM VACANCY */
    public int getNumberOfPeopleInside() {
        return numberOfPeopleInside;
    }
    public void incrementNumOfPeopleInside() {
        this.numberOfPeopleInside++;
    }
    public void decrementNumOfPeopleInside() {
        this.numberOfPeopleInside--;
        try {
            if (numberOfPeopleInside < 0) {
                throw new Exception();
            }
        }catch(Exception e){
            this.numberOfPeopleInside = 0;
        }
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