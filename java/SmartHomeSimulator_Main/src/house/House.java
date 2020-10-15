package house;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import sample.*;
import utilities.*;

import java.io.*;

public class House {

    /** ICON IMAGES */
//    private Image iconAC = new Image("static/images/airconditioning.png");
//    private  Image iconLight = new Image("static/images/lightbulb.png");
//    private Image iconMotionDet = new Image("static/images/motiondetector_triggered.png");
//    private  Image iconOccupied = new Image("static/images/people.png");
//    private  Image iconWindow = new Image("static/images/window.png");

    private int numOfRooms;
    private Room[] rooms;
    private AnchorPane layout;

    /**TODO: create attributes for the small image icons of utilities (i.e. lightbulb, AC, window, etc.)*/
    /**TODO: create an attribute for a house layout 2D drawing */

    public House(String houseLayoutFileName) {
        this.layout = new AnchorPane();
        this.rooms = new Room[3];
        this.numOfRooms = 3;

        /**TODO: using File IO, read a plain text (.txt) file called "houseLayoutFileName"
         * TODO: and assign the values accordingly
         *
         * TODO: in the file, the number of rooms should come first (initialize to "numOfRooms"
         * TODO: and set the length of "roomArray" to this value)
         *
         * TODO: from the text file, read the information about each room (name,
         * TODO: number of doors, windows, lights, etc.) and create a new Room object with these attributes
         * TODO: [HINT == use the setter methods in the "Room" class to do this]
         *
         * TODO: put the new Room object into "roomArray" and repeat the process for the rest of the Rooms in the file
         *
         *
         * TODO: after all rooms have been created, create a house layout 2D drawing (this will be dynamically
         * TODO: changed over time during the simulation (example, the appearance of icons in certain rooms, etc.)* */

    }

    public int getNumOfRooms() {
        return numOfRooms;
    }
    public void setNumOfRooms(int numOfRooms) {
        this.numOfRooms = numOfRooms;
    }
    public Room[] getRooms() {
        return rooms;
    }
    public void setRooms(Room[] rooms) {
        this.rooms = rooms;
    }
    public AnchorPane getLayout() {
        return layout;
    }
    public void setLayout(AnchorPane layout) {
        this.layout = layout;
    }

    // called only once, in the constructor
    public void setupHouseLayout(Room[] roomArray) {
        this.layout = new AnchorPane();
        this.layout.setPrefHeight(675); this.layout.setPrefHeight(675);

        // construct each room
        // the length should not exceed 9 Rooms
        int transX = 0, transY = 0;
        for (int r = 0; r < roomArray.length; r++) {
            switch (r%3) {
                case 0: transX = 0; break;
                case 1: transX = 225; break;
                case 2: transX = 450; break;
            }
            // construct a room
            AnchorPane tempRoom = constructRoomLayout(roomArray[r]);
            tempRoom.setTranslateX(transX); tempRoom.setTranslateY(transY);
            this.layout.getChildren().add(tempRoom);

            if (r%3==2) {
                transX=0; transY+=225;
            }
        }
    }

    /**TODO: finish this method */
    public AnchorPane constructRoomLayout(Room room) {
        AnchorPane roomLayout = new AnchorPane();
        roomLayout.setPrefWidth(225); roomLayout.setPrefHeight(225);
        roomLayout.setStyle("-fx-border-color:#000000");

        int ID = room.getRoomID();
        String name = room.getName();
        Door[] doors = room.getDoorCollection();
        Window[] windows = room.getWindowCollection();
        Light[] lights = room.getLightCollection();
        AirConditioner airConditioner = room.getAc();
        MotionDetector motionDetector = room.getMd();

        return roomLayout;
    }

    /**TODO: implement methods for setting and getting the appropriate attributes,
     * TODO: accessing and modifying the house layout view depending on what happens during the simulation
     * TODO: (for example, if a Light is turned on, determine the Room where the light is, and on the
     * TODO: 2D layout make visible a light bulb icon in that Room)
     *
     * TODO: implement methods for updating information about objects around the house
     * TODO: [for example, if a Light is turned on, determine the Room where the light is, and on the
     * TODO: 2D layout make visible a light bulb icon in that Room]
     *
     *
     * TODO: implement a method "outputMessage(String message, TextArea outputConsole)" that will
     * TODO: print "message" to the main dashboard's output console
     * TODO: [HINT == use the "Main" class to access its main_dashboard scene, and further its output console,
     * TODO: to which you will append "message"]
     * TODO: for example, if a Light is turned on, generate a string with the current Local Time and the room where
     * TODO: the light was turned on, and append this string to the outputConsole
     * */


}
