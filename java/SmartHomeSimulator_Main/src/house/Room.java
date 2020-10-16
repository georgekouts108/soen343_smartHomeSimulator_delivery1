package house;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utilities.*;
import sample.*;

public class Room {

    private static int ID_count = 1;
    private Image iconAC = new Image("https://image.flaticon.com/icons/png/512/114/114735.png");
    private Image iconLight = new Image("https://w7.pngwing.com/pngs/285/389/png-transparent-lightbulb-icon-incandescent-light-bulb-computer-icons-idea-silhouette-light-black-and-white.png");
    private Image iconMotionDet = new Image("https://image.flaticon.com/icons/png/512/1633/1633071.png");
    private Image iconDoor = new Image("https://image.flaticon.com/icons/png/512/59/59801.png");
    private Image iconWindow = new Image("https://img.icons8.com/ios/452/open-window.png");

    private ImageView iconAC_view = new ImageView(iconAC);
    private ImageView iconLight_view = new ImageView(iconLight);
    private ImageView iconMD_view = new ImageView(iconMotionDet);
    private ImageView iconDoor_view = new ImageView(iconDoor);
    private ImageView iconWindow_view = new ImageView(iconWindow);

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

        this.iconLight_view.setImage(iconLight); this.iconLight_view.setId("iconLightViewRoom#"+this.roomID);
        this.iconLight_view.setFitWidth(42); this.iconLight_view.setFitHeight(20);
        this.iconLight_view.setTranslateX(2); this.iconLight_view.setTranslateY(180);

        this.iconWindow_view.setImage(iconWindow); this.iconWindow_view.setId("iconWindowViewRoom#"+this.roomID);
        this.iconWindow_view.setFitWidth(45); this.iconWindow_view.setFitHeight(25);
        this.iconWindow_view.setTranslateX(45); this.iconWindow_view.setTranslateY(180);

        this.iconDoor_view.setImage(iconDoor); this.iconDoor_view.setId("iconDoorViewRoom#"+this.roomID);
        this.iconDoor_view.setFitWidth(45); this.iconDoor_view.setFitHeight(25);
        this.iconDoor_view.setTranslateX(90); this.iconDoor_view.setTranslateY(180);

        this.iconMD_view.setImage(iconMotionDet); this.iconMD_view.setId("iconMDViewRoom#"+this.roomID);
        this.iconMD_view.setFitWidth(45); this.iconMD_view.setFitHeight(25);
        this.iconMD_view.setTranslateX(135); this.iconMD_view.setTranslateY(180);

        this.iconAC_view.setImage(iconAC); this.iconAC_view.setId("iconACViewRoom#"+this.roomID);
        this.iconAC_view.setFitWidth(45); this.iconAC_view.setFitHeight(25);
        this.iconAC_view.setTranslateX(180); this.iconAC_view.setTranslateY(180);
    }

    public ImageView getIconLight_view() {
        return iconLight_view;
    }

    public ImageView getIconDoor_view() {
        return iconDoor_view;
    }

    public ImageView getIconAC_view() {
        return iconAC_view;
    }

    public ImageView getIconWindow_view() {
        return iconWindow_view;
    }

    public ImageView getIconMD_view() {
        return iconMD_view;
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