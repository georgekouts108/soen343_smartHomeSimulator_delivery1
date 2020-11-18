package house;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import utilities.*;
import sample.*;

/**
 * Room class
 */
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

    private String name;
    private int roomID;
    private int numberOfPeopleInside;
    private AirConditioner ac;
    private MotionDetector md;
    private Door[] doorCollection;
    private Light[] lightCollection;
    private Window[] windowCollection;
    private boolean autoMode;

    private double roomTemperature;

    /**
     * Room constructor
     * @param roomName
     * @param numberOfDoors
     * @param numberOfWindows
     * @param numberOfLights
     * @param AC
     */
    public Room(String roomName, int numberOfDoors, int numberOfWindows, int numberOfLights, boolean AC) {
        this.roomID = (ID_count++);
        this.name = roomName;
        this.numberOfPeopleInside = 0;
        this.autoMode = false;
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

            // automatically lock doors by default for the garage, backyard, and entrance
            if (this.name.equals("garage") || this.name.equals("backyard") || this.name.equals("entrance")) {
                this.doorCollection[d].setLocked(true);
            }
        }

        this.lightCollection = new Light[numberOfLights];

        for (int L = 0; L < this.lightCollection.length; L++) {
            this.lightCollection[L] = new Light();
        }

        this.windowCollection = new Window[numberOfWindows];

        for (int w = 0; w < this.windowCollection.length; w++) {
            this.windowCollection[w] = new Window();
        }

        this.roomTemperature = SHSHelpers.getOutsideTemperature();

        this.iconLight_view.setImage(iconLight); this.iconLight_view.setId("iconLightViewRoom#"+this.roomID);
        this.iconLight_view.setFitWidth(42); this.iconLight_view.setFitHeight(20);
        this.iconLight_view.setTranslateX(2); this.iconLight_view.setTranslateY(180);
        this.iconLight_view.setVisible(false);

        this.iconWindow_view.setImage(iconWindow); this.iconWindow_view.setId("iconWindowViewRoom#"+this.roomID);
        this.iconWindow_view.setFitWidth(45); this.iconWindow_view.setFitHeight(25);
        this.iconWindow_view.setTranslateX(45); this.iconWindow_view.setTranslateY(180);
        this.iconWindow_view.setVisible(false);

        this.iconDoor_view.setImage(iconDoor); this.iconDoor_view.setId("iconDoorViewRoom#"+this.roomID);
        this.iconDoor_view.setFitWidth(45); this.iconDoor_view.setFitHeight(25);
        this.iconDoor_view.setTranslateX(90); this.iconDoor_view.setTranslateY(180);
        this.iconDoor_view.setVisible(false);

        this.iconMD_view.setImage(iconMotionDet); this.iconMD_view.setId("iconMDViewRoom#"+this.roomID);
        this.iconMD_view.setFitWidth(45); this.iconMD_view.setFitHeight(25);
        this.iconMD_view.setTranslateX(135); this.iconMD_view.setTranslateY(180);
        this.iconMD_view.setVisible(false);

        this.iconAC_view.setImage(iconAC); this.iconAC_view.setId("iconACViewRoom#"+this.roomID);
        this.iconAC_view.setFitWidth(45); this.iconAC_view.setFitHeight(25);
        this.iconAC_view.setTranslateX(180); this.iconAC_view.setTranslateY(180);
        this.iconAC_view.setVisible(false);
    }

    /**
     * set the visibility of a Room's Light Icon
     * @param v
     */
    public void setIconLightVisibility(boolean v) { this.iconLight_view.setVisible(v); }

    /**
     * set the visibility of a Room's Window Icon
     * @param v
     */
    public void setIconWindowVisibility(boolean v) { this.iconWindow_view.setVisible(v); }

    /**
     * set the visibility of a Room's Door Icon
     * @param v
     */
    public void setIconDoorVisibility(boolean v) { this.iconDoor_view.setVisible(v); }

    /**
     * set the visibility of a Room's Motion Detector Icon
     * @param v
     */
    public void setIconMDVisibility(boolean v) { this.iconMD_view.setVisible(v); }

    /**
     * set the visibility of a Room's AC Icon
     * @param v
     */
    public void setIconACVisibility(boolean v) { this.iconAC_view.setVisible(v); }

    /**
     * Access the Light-bulb icon
     * @return
     */
    public ImageView getIconLight_view() {
        return iconLight_view;
    }

    /**
     * Access the Door icon
     * @return
     */
    public ImageView getIconDoor_view() {
        return iconDoor_view;
    }

    /**
     * Access the Air Conditioner icon
     * @return
     */
    public ImageView getIconAC_view() {
        return iconAC_view;
    }

    /**
     * Access the Window icon
     * @return
     */
    public ImageView getIconWindow_view() {
        return iconWindow_view;
    }

    /**
     * Access the Motion Detector icon
     * @return
     */
    public ImageView getIconMD_view() {
        return iconMD_view;
    }

    /**
     * Access the name of a room
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Access a room's ID
     * @return
     */
    public int getRoomID() {
        return roomID;
    }

    /**
     * Mutate a room's population
     * @param numberOfPeopleInside
     */
    public void setNumberOfPeopleInside(int numberOfPeopleInside) {
        this.numberOfPeopleInside = numberOfPeopleInside;
    }

    /**
     * Access a room's population
     * @return
     */
    public int getNumberOfPeopleInside() {
        return numberOfPeopleInside;
    }

    /**
     * Return the AUTO mode state of a Room
     * @return
     */
    public boolean getIsAutoModeOn() {
        return autoMode;
    }

    /**
     * Configure the AUTO mode of a Room
     * @param autoMode
     */
    public void setAutoMode(boolean autoMode) {
        this.autoMode = autoMode;
    }

    /**
     * Return a room's AirConditioner object, if there is one in the room (null otherwise)
     * @return
     */
    public AirConditioner getAc() {
        return ac;
    }

    /**
     * Return a room's MotionDetector object
     * @return
     */
    public MotionDetector getMd() {
        return md;
    }

    /**
     * Access a room's array of Door objects
     * @return
     */
    public Door[] getDoorCollection() {
        return doorCollection;
    }

    /**
     * Access a room's array of Light objects
     * @return
     */
    public Light[] getLightCollection() {
        return lightCollection;
    }

    /**
     * Access a room's array of Window objects
     * @return
     */
    public Window[] getWindowCollection() {
        return windowCollection;
    }

    /**
     * Return a room's indoor temperature
     * @return
     */
    public double getRoomTemperature() {
        return roomTemperature;
    }

    /**
     * Set a room's indoor temperature
     * @param roomTemperature
     */
    public void setRoomTemperature(double roomTemperature) {
        this.roomTemperature = roomTemperature;
    }
}