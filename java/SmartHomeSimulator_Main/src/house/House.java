package house;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import sample.*;
import utilities.*;

public class House {

    /** ICON IMAGES */
    private Image iconAC = new Image("https://image.flaticon.com/icons/png/512/114/114735.png");
    private Image iconLight = new Image("https://www.vhv.rs/viewpic/hbwiihT_lightbulb-icon-png-icon-for-light-bulb-transparent/#");
    private Image iconMotionDet = new Image("https://icon-library.com/images/motion-sensor-icon/motion-sensor-icon-16.jpg");
    private Image iconDoor = new Image("https://image.flaticon.com/icons/png/512/59/59801.png");
    private Image iconWindow = new Image("https://img.icons8.com/ios/452/open-window.png");

    private ImageView iconAC_view = new ImageView(iconAC);
    private ImageView iconLight_view = new ImageView(iconLight);
    private ImageView iconMD_view = new ImageView(iconMotionDet);
    private ImageView iconDoor_view = new ImageView(iconDoor);
    private ImageView iconWindow_view = new ImageView(iconWindow);

    private int numOfRooms;
    private Room[] rooms;
    private AnchorPane layout;

    /**TODO: create attributes for the small image icons of utilities (i.e. lightbulb, AC, window, etc.)*/
    /**TODO: create an attribute for a house layout 2D drawing */

    public House(String houseLayoutFileName) {
        this.layout = new AnchorPane();

        this.iconLight_view.setImage(iconLight);
        this.iconLight_view.setFitWidth(45); this.iconLight_view.setFitHeight(25);
        this.iconLight_view.setTranslateX(0); this.iconLight_view.setTranslateY(180);

        this.iconWindow_view.setImage(iconWindow);
        this.iconWindow_view.setFitWidth(45); this.iconWindow_view.setFitHeight(25);
        this.iconWindow_view.setTranslateX(45); this.iconWindow_view.setTranslateY(180);

        this.iconDoor_view.setImage(iconDoor);
        this.iconDoor_view.setFitWidth(45); this.iconDoor_view.setFitHeight(25);
        this.iconDoor_view.setTranslateX(90); this.iconDoor_view.setTranslateY(180);

        this.iconMD_view.setImage(iconMotionDet);
        this.iconMD_view.setFitWidth(45); this.iconMD_view.setFitHeight(25);
        this.iconMD_view.setTranslateX(135); this.iconMD_view.setTranslateY(180);

        this.iconAC_view.setImage(iconAC);
        this.iconAC_view.setFitWidth(45); this.iconAC_view.setFitHeight(25);
        this.iconAC_view.setTranslateX(180); this.iconAC_view.setTranslateY(180);


        /**TODO: using File IO, read a plain text (.txt) file called "houseLayoutFileName"
         * TODO: and assign the values accordingly
         *
         *
         * */

        /** TODO: in the file, the number of rooms should come first (initialize to "numOfRooms"
         * TODO: and set the length of "roomArray" to this value)*/

        /**dummy for now*/
        this.rooms = Main.getHouseholdLocations();

//        for (int r = 0; r < this.rooms.length; r++) {
//
//            /**TODO: from the text file, read the information about each room: room name,
//             * TODO: number of doors, windows, lights, and if it has an AC machine) */
//            String room_name = "";
//            int numOfDoors = 0;
//            int numOfWindows = 0;
//            int numOfLights = 0;
//            boolean has_ac = true;
//
//            /**TODO: create a new Room object with these attributes*/
//            this.rooms[r] = new Room(room_name, numOfDoors, numOfWindows, numOfLights, has_ac);
//        }

        setupHouseLayout(this.rooms);
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
            // construct an individual room layout
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

        // labels (no need IDs)
        Label roomIDNameLabel = new Label("#"+ID+" "+name); roomIDNameLabel.setTranslateX(10); roomLayout.getChildren().add(roomIDNameLabel);
        Label populationLabel = new Label("Pop: "+room.getNumberOfPeopleInside()); populationLabel.setTranslateX(175); roomLayout.getChildren().add(populationLabel);
        Label lightsLabel = new Label("Lights"); lightsLabel.setTranslateX(20); lightsLabel.setTranslateY(20); roomLayout.getChildren().add(lightsLabel);
        Label windowsLabel = new Label("Windows"); windowsLabel.setTranslateX(85); windowsLabel.setTranslateY(20); roomLayout.getChildren().add(windowsLabel);
        Label doorsLabel = new Label("Doors"); doorsLabel.setTranslateX(170); doorsLabel.setTranslateY(20); roomLayout.getChildren().add(doorsLabel);

        // borderlines (no need IDs)
        Line line1 = new Line(); line1.setStartX(0); line1.setEndX(225); line1.setTranslateY(20); roomLayout.getChildren().add(line1);
        Line line4 = new Line(); line4.setStartY(20); line4.setEndY(180); line4.setTranslateX(75); roomLayout.getChildren().add(line4);
        Line line5 = new Line(); line5.setStartY(20); line5.setEndY(180); line5.setTranslateX(150); roomLayout.getChildren().add(line5);
        Line line3 = new Line(); line3.setStartX(0); line3.setEndX(225); line3.setTranslateY(180); roomLayout.getChildren().add(line3);
        Line line2 = new Line(); line2.setStartX(0); line2.setEndX(225); line2.setTranslateY(205); roomLayout.getChildren().add(line2);
        Line line6 = new Line(); line6.setStartY(180); line6.setEndY(205); line6.setTranslateX(45); roomLayout.getChildren().add(line6);
        Line line7 = new Line(); line7.setStartY(180); line7.setEndY(205); line7.setTranslateX(90); roomLayout.getChildren().add(line7);
        Line line8 = new Line(); line8.setStartY(180); line8.setEndY(205); line8.setTranslateX(135); roomLayout.getChildren().add(line8);
        Line line9 = new Line(); line9.setStartY(180); line9.setEndY(205); line9.setTranslateX(180); roomLayout.getChildren().add(line9);

        // checkboxes for Lights
        /**todo: replace the count limit with the actual size of the lights collection array -- dummy limit for now*/
        int translate_y = 50;
        for (int L = 0; L < room.getLightCollection().length; L++) {
            CheckBox checkBox = new CheckBox("L#"+room.getLightCollection()[L].getUtilityID());
            checkBox.setId("lightCheckboxID#"+room.getLightCollection()[L].getUtilityID());
            checkBox.setTranslateX(5); checkBox.setTranslateY(translate_y);
            checkBox.setOnAction(e->{
                if (checkBox.isSelected()) {
                    // turn the light on
                    for (int light = 0; light < room.getLightCollection().length; light++) {
                        if (room.getLightCollection()[light].getUtilityID() == Integer.parseInt(checkBox.getId().substring(16))) {
                            room.getLightCollection()[light].setState(true);
                            break;
                        }
                    }
                }
                else {
                    // turn the light off
                    for (int light = 0; light < room.getLightCollection().length; light++) {
                        if (room.getLightCollection()[light].getUtilityID() == Integer.parseInt(checkBox.getId().substring(16))) {
                            room.getLightCollection()[light].setState(false);
                            break;
                        }
                    }
                }
                if (anyLightsOn(room)) {
                    boolean isLightIconThere = false;
                    for (int a = 0; a < roomLayout.getChildren().size(); a++) {
                        if (roomLayout.getChildren().contains(iconLight_view)) {
                            isLightIconThere = true;
                            break;
                        }
                    }
                    if (!isLightIconThere){
                        roomLayout.getChildren().add(iconLight_view);
                    }
                }
                else {
                    try {
                        roomLayout.getChildren().remove(iconLight_view);
                    }catch (Exception ex){}
                }
            });
            roomLayout.getChildren().add(checkBox);
            translate_y += 20;
        }

        // checkboxes for Windows
        /**todo: replace the count limit with the actual size of the windows collection array -- dummy limit for now*/
        translate_y = 50;
        for (int w = 0; w < room.getWindowCollection().length; w++) {
            CheckBox checkBox = new CheckBox("W#"+room.getWindowCollection()[w].getUtilityID());
            checkBox.setId("windowCheckboxID#"+room.getWindowCollection()[w].getUtilityID());
            checkBox.setTranslateX(85); checkBox.setTranslateY(translate_y);
            checkBox.setOnAction(e->{
                if (checkBox.isSelected()) {
                    // open the window
                    for (int win = 0; win < room.getWindowCollection().length; win++) {
                        if (room.getWindowCollection()[win].getUtilityID() == Integer.parseInt(checkBox.getId().substring(17))) {
                            room.getWindowCollection()[win].setState(true);
                            break;
                        }
                    }
                }
                else {
                    // close the window
                    for (int win = 0; win < room.getWindowCollection().length; win++) {
                        if (room.getWindowCollection()[win].getUtilityID() == Integer.parseInt(checkBox.getId().substring(17))) {
                            room.getWindowCollection()[win].setState(false);
                            break;
                        }
                    }
                }
                if (anyWindowsOpen(room)) {
                    boolean isWindowIconThere = false;
                    for (int a = 0; a < roomLayout.getChildren().size(); a++) {
                        if (roomLayout.getChildren().contains(iconWindow_view)) {
                            isWindowIconThere = true;
                            break;
                        }
                    }
                    if (!isWindowIconThere){
                        roomLayout.getChildren().add(iconWindow_view);
                    }
                }
                else {
                    try {
                        roomLayout.getChildren().remove(iconWindow_view);
                    }catch (Exception ex){}
                }
            });
            roomLayout.getChildren().add(checkBox);
            translate_y += 60;
        }

        // checkboxes for Doors
        /**todo: replace the count limit with the actual size of the doors collection array -- dummy limit for now*/
        translate_y = 50;
        for (int d = 0; d < room.getDoorCollection().length; d++) {
            CheckBox checkBox = new CheckBox("D#"+room.getDoorCollection()[d].getUtilityID());
            checkBox.setId("doorCheckboxID#"+room.getDoorCollection()[d].getUtilityID());
            checkBox.setTranslateX(160); checkBox.setTranslateY(translate_y);
            checkBox.setOnAction(e->{
                if (checkBox.isSelected()) {
                    // open the door
                    for (int door = 0; door < room.getDoorCollection().length; door++) {
                        if (room.getDoorCollection()[door].getUtilityID() == Integer.parseInt(checkBox.getId().substring(15))) {
                            room.getDoorCollection()[door].setState(true);
                            break;
                        }
                    }
                }
                else {
                    // close the door
                    for (int door = 0; door < room.getDoorCollection().length; door++) {
                        if (room.getDoorCollection()[door].getUtilityID() == Integer.parseInt(checkBox.getId().substring(15))) {
                            room.getDoorCollection()[door].setState(false);
                            break;
                        }
                    }
                }
                if (anyDoorsOpen(room)) {
                    boolean isDoorIconThere = false;
                    for (int a = 0; a < roomLayout.getChildren().size(); a++) {
                        if (roomLayout.getChildren().contains(iconDoor_view)) {
                            isDoorIconThere = true;
                            break;
                        }
                    }
                    if (!isDoorIconThere){
                        roomLayout.getChildren().add(iconDoor_view);
                    }
                }
                else {
                    try {
                        roomLayout.getChildren().remove(iconDoor_view);
                    }catch (Exception ex){}
                }
            });
            roomLayout.getChildren().add(checkBox);
            translate_y += 60;
        }

        CheckBox mdCheckbox = new CheckBox("MD triggered");
        mdCheckbox.setId("motionDetectorID#"+room.getMd().getUtilityID());
        mdCheckbox.setTranslateX(5); mdCheckbox.setTranslateY(206);
        mdCheckbox.setOnAction(e->{
            if (mdCheckbox.isSelected()) {
                room.getMd().setState(true);
                boolean isMdIconThere = false;
                for (int a = 0; a < roomLayout.getChildren().size(); a++) {
                    if (roomLayout.getChildren().contains(iconMD_view)) {
                        isMdIconThere = true;
                        break;
                    }
                }
                if (!isMdIconThere){
                    roomLayout.getChildren().add(iconMD_view);
                }
            }
            else {
                room.getMd().setState(false);
                try {
                    roomLayout.getChildren().remove(iconMD_view);
                }catch(Exception ex){}
            }
        });
        roomLayout.getChildren().add(mdCheckbox);

        if (room.getAc()!=null) {
            CheckBox acCheckbox = new CheckBox("AC on");
            acCheckbox.setId("airConditionerID#"+room.getAc().getUtilityID());
            acCheckbox.setTranslateX(150); acCheckbox.setTranslateY(206);
            acCheckbox.setOnAction(e->{
                if (acCheckbox.isSelected()) {
                    room.getAc().setState(true);
                    boolean isAcIconThere = false;
                    for (int a = 0; a < roomLayout.getChildren().size(); a++) {
                        if (roomLayout.getChildren().contains(iconAC_view)) {
                            isAcIconThere = true;
                            break;
                        }
                    }
                    if (!isAcIconThere){
                        roomLayout.getChildren().add(iconAC_view);
                    }
                }
                else {
                    room.getAc().setState(false);
                    try {
                        roomLayout.getChildren().remove(iconAC_view);
                    }catch(Exception ex){}
                }
            });
            roomLayout.getChildren().add(acCheckbox);
        }
        return roomLayout;
    }

    public boolean anyLightsOn(Room room) {
        boolean yes = false;
        for (int light = 0; light < room.getLightCollection().length; light++) {
            if (room.getLightCollection()[light].getState()) {
                yes = true;
                break;
            }
        }
        return yes;
    }
    public int howManyLightsOn(Room room) {
        int count = 0;
        for (int i = 0; i < room.getLightCollection().length; i++) {
            if (room.getLightCollection()[i].getState()) {
                count++;
            }
        }
        return count;
    }
    public boolean anyDoorsOpen(Room room) {
        boolean yes = false;
        for (int door = 0; door < room.getDoorCollection().length; door++) {
            if (room.getDoorCollection()[door].getState()) {
                yes = true;
                break;
            }
        }
        return yes;
    }
    public int howManyDoorsOpen(Room room) {
        int count = 0;
        for (int i = 0; i < room.getDoorCollection().length; i++) {
            if (room.getDoorCollection()[i].getState()) {
                count++;
            }
        }
        return count;
    }
    public boolean anyWindowsOpen(Room room) {
        boolean yes = false;
        for (int window = 0; window < room.getWindowCollection().length; window++) {
            if (room.getWindowCollection()[window].getState()) {
                yes = true;
                break;
            }
        }
        return yes;
    }
    public int howManyWindowsOpen(Room room) {
        int count = 0;
        for (int i = 0; i < room.getWindowCollection().length; i++) {
            if (room.getWindowCollection()[i].getState()) {
                count++;
            }
        }
        return count;
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
