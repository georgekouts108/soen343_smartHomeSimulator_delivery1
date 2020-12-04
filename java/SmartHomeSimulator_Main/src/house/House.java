package house;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import sample.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * House class
 */
public class House {

    private int numOfRooms;
    private Room[] rooms;
    private AnchorPane layout;
    private String location;

    /**
     * Constructor for the House class
     * @param houseLayoutFileName
     * @throws FileNotFoundException
     */
    public House(String houseLayoutFileName) throws FileNotFoundException {

        File house = new File(houseLayoutFileName);
        Scanner scan = new Scanner(house);
        numOfRooms = scan.nextInt();

        this.rooms = new Room[numOfRooms];

        int x = 0;
        String room_name = "";
        int numOfDoors = 0;
        int numOfWindows = 0;
        int numOfLights = 0;
        boolean has_ac = true;

        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            if (line.contains("Name=")){
                room_name = line.substring(6);
            }
            if (line.contains("Doors=")){
                numOfDoors = Integer.parseInt(line.substring(7));
            }
            if (line.contains("Windows=")){
                numOfWindows = Integer.parseInt(line.substring(9));
            }
            if (line.contains("Lights=")){
                numOfLights = Integer.parseInt(line.substring(8));
            }
            if (line.contains("Ac=")){
                has_ac = Boolean.parseBoolean(line.substring(4));
            }
            if (line.contains("-end-")){
                this.rooms[x] = new Room(room_name, numOfDoors, numOfWindows, numOfLights, has_ac);
                x++;
            }
        }

        setupHouseLayout(this.rooms);
    }

    /**
     * Mutator for a house's location
     * @param loc
     */
    public void setLocation(String loc) {
        this.location = loc;
    }

    /**
     * Accessor for a house's location
     * @return
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Accessor for a house's array of Room objects
     * @return
     */
    public Room[] getRooms() {
        return rooms;
    }

    /**
     * Accessor for a house's display layout
     * @return
     */
    public AnchorPane getLayout() {
        return layout;
    }

    /**
     * Generate a house's display layout containing each Room
     * @param roomArray
     */
    public void setupHouseLayout(Room[] roomArray) {
        this.layout = new AnchorPane();
        this.layout.setPrefHeight(675); this.layout.setPrefHeight(675);

        // construct each room
        int transX = 0, transY = 0;
        for (int r = 0; r < roomArray.length; r++) {
            switch (r%3) {
                case 0: transX = 0; break;
                case 1: transX = 225; break;
                case 2: transX = 450; break;
            }

            // construct the room layout
            AnchorPane tempRoom = constructRoomLayout(roomArray[r]);
            tempRoom.setTranslateX(transX); tempRoom.setTranslateY(transY);
            this.layout.getChildren().add(tempRoom);

            if (r%3==2) {
                transX=0; transY+=225;
            }
        }
    }

    /**
     * Generate the interactive user interface to configure utilities of a specific room
     * @param room
     * @param room_layout
     * @param population
     * @param stage
     * @return
     */
    public AnchorPane constructRoomLayoutSHCversion(Room room, AnchorPane room_layout, int population, Stage stage) {

        AnchorPane roomLayout = new AnchorPane();
        roomLayout.setId("roomLayoutID"+room.getRoomID());
        roomLayout.setPrefWidth(225); roomLayout.setPrefHeight(350);
        roomLayout.setStyle("-fx-border-color:#000000");

        int ID = room.getRoomID();
        String name = room.getName();

        // labels
        Label roomIDNameLabel = new Label("#"+ID+" "+name);
        roomIDNameLabel.setTranslateX(10);
        roomIDNameLabel.setId("labelRoom#"+room.getRoomID());
        roomLayout.getChildren().add(roomIDNameLabel);

        Label lightsLabel = new Label("Lights");
        lightsLabel.setTranslateX(20);
        lightsLabel.setTranslateY(20);
        roomLayout.getChildren().add(lightsLabel);

        Label windowsLabel = new Label("Windows");
        windowsLabel.setTranslateX(85);
        windowsLabel.setTranslateY(20);
        roomLayout.getChildren().add(windowsLabel);

        Label doorsLabel = new Label("Doors");
        doorsLabel.setTranslateX(170);
        doorsLabel.setTranslateY(20);
        roomLayout.getChildren().add(doorsLabel);

        // borderlines
        Line line1 = new Line(); line1.setStartX(0); line1.setEndX(225); line1.setTranslateY(20); roomLayout.getChildren().add(line1);
        Line line4 = new Line(); line4.setStartY(20); line4.setEndY(180); line4.setTranslateX(75); roomLayout.getChildren().add(line4);
        Line line5 = new Line(); line5.setStartY(20); line5.setEndY(180); line5.setTranslateX(150); roomLayout.getChildren().add(line5);
        Line line3 = new Line(); line3.setStartX(0); line3.setEndX(225); line3.setTranslateY(180); roomLayout.getChildren().add(line3);
        Line line2 = new Line(); line2.setStartX(0); line2.setEndX(225); line2.setTranslateY(205); roomLayout.getChildren().add(line2);

        // checkboxes for Lights
        int translate_y = 50;
        for (int L = 0; L < room.getLightCollection().length; L++) {

            CheckBox checkBox = new CheckBox("L#"+room.getLightCollection()[L].getUtilityID());
            checkBox.setId("lightCheckboxID#"+room.getLightCollection()[L].getUtilityID());
            checkBox.setTranslateX(5); checkBox.setTranslateY(translate_y);

            if (room.getLightCollection()[L].getState()) {
                checkBox.setSelected(true);
            }
            else {
                checkBox.setSelected(false);
            }

            if (room.getLightCollection()[L].isLocked()) {
                checkBox.setStyle("-fx-border-color:#00ff00");
            }

            checkBox.setOnAction(e->{
                if (checkBox.isSelected()) {
                    // turn the light on
                    for (int light = 0; light < room.getLightCollection().length; light++) {
                        if (room.getLightCollection()[light].getUtilityID() == Integer.parseInt(checkBox.getId().substring(16))) {
                            try {
                                //if user has light permissions or has location permissions and is currently in the room
                                if(Main.getCurrentActiveProfile().getPermLights() == true ||
                                        (Main.getCurrentActiveProfile().getPermLightsLocation() == true
                                                && SHSHelpers.getCurrentLocation()!=null && SHSHelpers.getCurrentLocation().getName().toLowerCase() == room.getName())){
                                    room.getLightCollection()[light].setState(true);
                                    Controller.appendMessageToConsole("SHC -- Light #"+room.getLightCollection()[light].getUtilityID()+" opened in Room #"+room.getRoomID()+" "+room.getName()+" by user");
                                    break;
                                }
                                else{
                                    throw new SHSException("SHC -- You are not allowed to turn on the " + room.getName() + " light.");
                                }
                            }
                            catch (SHSException s){
                                checkBox.setSelected(false);
                                Controller.appendMessageToConsole(s.getMessage());
                            }
                        }
                    }
                }
                else {
                    // turn the light off
                    for (int light = 0; light < room.getLightCollection().length; light++) {
                        if (room.getLightCollection()[light].getUtilityID() == Integer.parseInt(checkBox.getId().substring(16))) {
                            if (!room.getLightCollection()[light].isLocked()) {

                                //if user has light permissions or has location permissions and is currently in the room
                                if(Main.getCurrentActiveProfile().getPermLights() == true ||
                                        (Main.getCurrentActiveProfile().getPermLightsLocation() == true &&
                                                SHSHelpers.getCurrentLocation()!=null && SHSHelpers.getCurrentLocation().getName().toLowerCase() == room.getName())){
                                    room.getLightCollection()[light].setState(false);
                                    Controller.appendMessageToConsole("SHC -- Light #"+room.getLightCollection()[light].getUtilityID()+" closed in Room #"+room.getRoomID()+" "+room.getName() + " by user");
                                }
                                else {
                                    checkBox.setSelected(true);
                                    Controller.appendMessageToConsole("SHC -- You are not allowed to turn on the " + room.getName() + " light.");
                                }

                            }
                            else {
                                checkBox.setSelected(true);
                                Controller.appendMessageToConsole("SHC -- Attempt to toggle locked Light #"+room.getLightCollection()[light].getUtilityID()+" " +
                                        " in Room #"+room.getRoomID()+" "+room.getName() + " by user");
                            }

                            break;
                        }
                    }
                }
                if (anyLightsOn(room)) {
                    setIconVisibility(room, "Light", true);
                }
                else {
                    setIconVisibility(room, "Light", false);
                }
            });
            roomLayout.getChildren().add(checkBox);
            translate_y += 20;
        }

        // checkboxes for Windows
        translate_y = 50;
        for (int w = 0; w < room.getWindowCollection().length; w++) {
            CheckBox checkBox = new CheckBox("W#"+room.getWindowCollection()[w].getUtilityID());
            checkBox.setId("windowCheckboxID#"+room.getWindowCollection()[w].getUtilityID());
            checkBox.setTranslateX(85); checkBox.setTranslateY(translate_y);

            if (room.getWindowCollection()[w].getState()) {
                checkBox.setSelected(true);
            }
            else {
                checkBox.setSelected(false);
            }

            if (room.getWindowCollection()[w].isBlocked()) {
                checkBox.setStyle("-fx-border-color:#ff0000");
            }

            checkBox.setOnAction(e->{
                if (checkBox.isSelected()) {
                    // open the window (if not blocked)
                    for (int win = 0; win < room.getWindowCollection().length; win++) {
                        if (room.getWindowCollection()[win].getUtilityID() == Integer.parseInt(checkBox.getId().substring(17))) {
                            try {
                                if (!room.getWindowCollection()[win].isBlocked()) {

                                    //if user has window permissions or has location permissions and is currently in the room
                                    if(Main.getCurrentActiveProfile().getPermWindows() == true ||
                                            (Main.getCurrentActiveProfile().getPermWindowsLocation() == true &&
                                                    SHSHelpers.getCurrentLocation()!=null && SHSHelpers.getCurrentLocation().getName().toLowerCase() == room.getName())) {
                                        room.getWindowCollection()[win].setState(true);
                                        Controller.appendMessageToConsole("SHC -- Window #" + room.getWindowCollection()[win].getUtilityID() + " opened in Room #" + room.getRoomID() + " " + room.getName() + " by user");
                                    }
                                    else{
                                        throw new SHSException("SHC -- You are not allowed to open the " + room.getName() + " window.");
                                    }
                                }
                                else {
                                   throw new SHSException("SHC -- Attempt to open blocked Window #"+room.getWindowCollection()[win].getUtilityID()+" in Room #"+room.getRoomID()+" "+room.getName() +" by user");
                                }
                            }
                            catch (SHSException s) {
                                Controller.appendMessageToConsole(s.getMessage());
                                checkBox.setSelected(false);
                            }
                            break;
                        }
                    }
                }
                else {
                    // close the window
                    for (int win = 0; win < room.getWindowCollection().length; win++) {
                        if (room.getWindowCollection()[win].getUtilityID() == Integer.parseInt(checkBox.getId().substring(17))) {
                            try {
                                if (!room.getWindowCollection()[win].isBlocked()) {

                                    //if user has window permissions or has location permissions and is currently in the room
                                    if(Main.getCurrentActiveProfile().getPermWindows() == true ||
                                            (Main.getCurrentActiveProfile().getPermWindowsLocation() == true &&
                                                    SHSHelpers.getCurrentLocation()!=null && SHSHelpers.getCurrentLocation().getName().toLowerCase() == room.getName())) {
                                        room.getWindowCollection()[win].setState(false);
                                        Controller.appendMessageToConsole("Window #" + room.getWindowCollection()[win].getUtilityID() + " closed in Room #" + room.getRoomID() + " " + room.getName());
                                    }
                                    else {
                                        throw new SHSException("SHC -- You are not allowed to close the " + room.getName() + " window.");
                                    }
                                }
                                else {
                                    throw new SHSException("User attempted to close blocked Window #"+room.getWindowCollection()[win].getUtilityID()+" in Room #"+room.getRoomID()+" "+room.getName());
                                }
                            }
                            catch (SHSException s) {
                                Controller.appendMessageToConsole(s.getMessage());
                                checkBox.setSelected(true);
                            }

                            break;
                        }
                    }
                }
                if (anyWindowsOpen(room)) {
                    setIconVisibility(room, "Window", true);
                }
                else {
                    setIconVisibility(room, "Window", false);
                }
            });
            roomLayout.getChildren().add(checkBox);
            translate_y += 20;
        }

        // checkboxes for Doors
        translate_y = 50;
        for (int d = 0; d < room.getDoorCollection().length; d++) {
            CheckBox checkBox = new CheckBox("D#"+room.getDoorCollection()[d].getUtilityID());
            checkBox.setId("doorCheckboxID#"+room.getDoorCollection()[d].getUtilityID());
            checkBox.setTranslateX(160); checkBox.setTranslateY(translate_y);

            if (room.getDoorCollection()[d].getState()) {
                checkBox.setSelected(true);
            }
            else {
                checkBox.setSelected(false);
            }

            if (room.getDoorCollection()[d].isLocked()) {
                checkBox.setStyle("-fx-border-color:#0000ff");
            }


            checkBox.setOnAction(e->{
                if (checkBox.isSelected()) {

                    // open the door (if unlocked)
                    for (int door = 0; door < room.getDoorCollection().length; door++) {
                        if (room.getDoorCollection()[door].getUtilityID() == Integer.parseInt(checkBox.getId().substring(15))) {
                            if (!room.getDoorCollection()[door].isLocked()) {

                                //if user has door permissions or has location permissions and is currently in the room
                                if(Main.getCurrentActiveProfile().getPermDoors() == true ||
                                        (Main.getCurrentActiveProfile().getPermDoorsLocation() == true &&
                                                SHSHelpers.getCurrentLocation()!=null && SHSHelpers.getCurrentLocation().getName().toLowerCase() == room.getName())) {
                                    room.getDoorCollection()[door].setState(true);
                                    Controller.appendMessageToConsole("SHC -- Door #" + room.getDoorCollection()[door].getUtilityID() + " opened in Room #" + room.getRoomID() + " " + room.getName() + " by user");
                                }
                                else{
                                    checkBox.setSelected(false);
                                    Controller.appendMessageToConsole("SHC -- You are not allowed to open the " + room.getName() + " door.");
                                }
                            }
                            else {
                                checkBox.setSelected(false);
                                Controller.appendMessageToConsole("SHC -- Failed to open locked Door #"+room.getDoorCollection()[door].getUtilityID()+" to Room #"+room.getRoomID()+" "+room.getName()+ " by user");
                            }
                            break;
                        }
                    }
                }
                else {
                    // close the door
                    for (int door = 0; door < room.getDoorCollection().length; door++) {
                        if (room.getDoorCollection()[door].getUtilityID() == Integer.parseInt(checkBox.getId().substring(15))) {
                            if (!room.getDoorCollection()[door].isLocked()) {

                                //if user has door permissions or has location permissions and is currently in the room
                                if(Main.getCurrentActiveProfile().getPermDoors() == true ||
                                        (Main.getCurrentActiveProfile().getPermDoorsLocation() == true &&
                                                SHSHelpers.getCurrentLocation()!=null && SHSHelpers.getCurrentLocation().getName().toLowerCase() == room.getName())) {
                                    room.getDoorCollection()[door].setState(false);
                                }
                                else{
                                    checkBox.setSelected(false);
                                    Controller.appendMessageToConsole("SHC -- You are not allowed to close the " + room.getName() + " door.");
                                    break;
                                }

                                // if the room is the garage, entrance, or backyard, automatically lock the door when closing it
                                if (room.getName().equals("backyard") || room.getName().equals("entrance") || room.getName().equals("garage")) {
                                    room.getDoorCollection()[door].setLocked(true);
                                    for (int a = 0; a < roomLayout.getChildren().size(); a++) {
                                        try {
                                            if (roomLayout.getChildren().get(a).getId().equals("doorLockRoom#" + room.getRoomID())) {
                                                ToggleButton tempTB = (ToggleButton) roomLayout.getChildren().get(a);
                                                tempTB.setText("Unlock");
                                                tempTB.setSelected(true);
                                                roomLayout.getChildren().set(a, tempTB);
                                                break;
                                            }
                                        }
                                        catch(Exception ex){}
                                    }
                                }
                                if (room.getName().equals("backyard") || room.getName().equals("entrance") || room.getName().equals("garage")) {
                                    Controller.appendMessageToConsole("Door #"+room.getDoorCollection()[door].getUtilityID()+" closed & locked in Room #"+room.getRoomID()+" "+room.getName());
                                }
                                else {
                                    Controller.appendMessageToConsole("Door #"+room.getDoorCollection()[door].getUtilityID()+" closed in Room #"+room.getRoomID()+" "+room.getName());
                                }
                            }
                            else {
                                checkBox.setSelected(false);
                                Controller.appendMessageToConsole("Failed to close locked Door #"+room.getDoorCollection()[door].getUtilityID()+" to Room #"+room.getRoomID()+" "+room.getName());
                            }
                            break;
                        }
                    }
                }
                if (anyDoorsOpen(room)) {
                    setIconVisibility(room, "Door", true);
                }
                else {
                    setIconVisibility(room, "Door", false);
                }
            });
            roomLayout.getChildren().add(checkBox);
            translate_y += 20;
        }

        // a toggle button for locking all doors of a room
        ToggleButton doorLock = new ToggleButton();
        doorLock.setId("doorLockRoom#"+room.getRoomID());
        doorLock.setTranslateX(160); doorLock.setTranslateY(100);

        doorLock.setOnAction(e->{
            try {
                if (doorLock.isSelected()) {
                    if(Main.getCurrentActiveProfile().getPermDoors() == true ||
                            (Main.getCurrentActiveProfile().getPermDoorsLocation() == true &&
                                    SHSHelpers.getCurrentLocation()!=null && SHSHelpers.getCurrentLocation().getName().toLowerCase() == room.getName())) {
                        doorLock.setText("Unlock");

                        for (int l = 0; l < room.getDoorCollection().length; l++) {
                            try {
                                room.getDoorCollection()[l].setState(false);
                                setIconVisibility(room, "Door", false);
                                for (int cb = 0; cb < roomLayout.getChildren().size(); cb++) {
                                    try {
                                        if (roomLayout.getChildren().get(cb).getId().equals
                                                ("doorCheckboxID#" + room.getDoorCollection()[l].getUtilityID())) {
                                            CheckBox tempCB = (CheckBox) roomLayout.getChildren().get(cb);
                                            tempCB.setStyle("-fx-border-color:#0000ff");
                                            tempCB.setSelected(false);
                                            roomLayout.getChildren().set(cb, tempCB);
                                            break;
                                        }
                                    } catch (Exception ex) {
                                    }
                                }
                                throw new SHSException("SHC -- Door to " + room.getName() + " automatically closed & locked by user.");
                            } catch (Exception ex) { }
                        }

                        for (int d = 0; d < room.getDoorCollection().length; d++) {
                            room.getDoorCollection()[d].setLocked(true);
                        }
                    }
                    else{
                        doorLock.setSelected(false);
                        throw new SHSException("SHC -- You are not allowed to lock the " + room.getName() + " doors.");
                    }
                }
                else {
                    if(Main.getCurrentActiveProfile().getPermDoors() == true ||
                            (Main.getCurrentActiveProfile().getPermDoorsLocation() == true &&
                                    SHSHelpers.getCurrentLocation()!=null && SHSHelpers.getCurrentLocation().getName().toLowerCase() == room.getName())) {
                        doorLock.setText("Lock");
                        for (int d = 0; d < room.getDoorCollection().length; d++) {
                            for (int cb = 0; cb < roomLayout.getChildren().size(); cb++) {
                                try {
                                    if (roomLayout.getChildren().get(cb).getId().equals
                                            ("doorCheckboxID#" + room.getDoorCollection()[d].getUtilityID())) {
                                        CheckBox tempCB = (CheckBox) roomLayout.getChildren().get(cb);
                                        tempCB.setStyle("");
                                        roomLayout.getChildren().set(cb, tempCB);
                                        break;
                                    }
                                } catch (Exception ex) {
                                }
                            }
                            room.getDoorCollection()[d].setLocked(false);
                        }
                    }
                    else{
                        doorLock.setSelected(true);
                        throw new SHSException("SHC -- You are not allowed to unlock the " + room.getName() + " doors.");
                    }
                }
            }
            catch (SHSException S){
                Controller.appendMessageToConsole(S.getMessage());
            }

        });

        boolean allDoorsLocked = true;
        for (int d = 0; d < room.getDoorCollection().length; d++) {
            if (!room.getDoorCollection()[d].isLocked()) {
                allDoorsLocked = false;
                break;
            }
        }

        if (allDoorsLocked) {
            doorLock.setText("Unlock");
            doorLock.setSelected(true);
        }
        else {
            doorLock.setText("Lock");
            doorLock.setSelected(false);
        }

        roomLayout.getChildren().add(doorLock);

        CheckBox mdCheckbox = new CheckBox("MD triggered");
        mdCheckbox.setId("motionDetectorCheckboxID#"+room.getMd().getUtilityID());
        mdCheckbox.setTranslateX(5); mdCheckbox.setTranslateY(206);


        if (room.getMd().getState()) {
            mdCheckbox.setSelected(true);
        }
        else {
            mdCheckbox.setSelected(false);
        }

        mdCheckbox.setOnAction(e->{
            try {
                if (SHSHelpers.isIs_away()) {
                    try {
                        /**during away mode, you may manually trigger motion detectors to
                         * simulate the presence of intruders */
                        if (mdCheckbox.isSelected()) {
                            room.getMd().setState(true);
                            setIconVisibility(room, "MD", true);

                            Controller.appendMessageToConsole("SHC -- Motion detector triggered in Room #" + room.getRoomID() + " " + room.getName());
                            SHSHelpers.getShpModuleObject().incrementNumberOfMDsOn();
                        }
                        else {
                            room.getMd().setState(false);
                            setIconVisibility(room, "MD", false);
                            Controller.appendMessageToConsole("SHC -- Motion detector disabled in Room #" + room.getRoomID() + " " + room.getName());
                            SHSHelpers.getShpModuleObject().decrementNumberOfMDsOn();
                        }
                        SHSHelpers.getShpModuleObject().getNotified();
                    }
                    catch (Exception ex){}
                }
                else {
                    throw new SHSException("ERROR: You can only manually control M.D's in AWAY mode.");
                }
            }
            catch(SHSException ex) {
                Controller.appendMessageToConsole("ERROR: You can only manually control M.D's in AWAY mode.");
                if (mdCheckbox.isSelected()) {
                    setIconVisibility(room, "MD", false);
                    mdCheckbox.setSelected(false);
                }
                else {
                    setIconVisibility(room, "MD", true);
                    mdCheckbox.setSelected(true);
                }
            }
        });
        roomLayout.getChildren().add(mdCheckbox);

        if (room.getAc() != null) {
            CheckBox acCheckbox = new CheckBox("AC on");
            acCheckbox.setId("airConditionerCheckboxID#"+room.getAc().getUtilityID());
            acCheckbox.setTranslateX(150); acCheckbox.setTranslateY(206);

            if (room.getAc().getState()) {
                acCheckbox.setSelected(true);
            }
            else {
                acCheckbox.setSelected(false);
            }

            acCheckbox.setOnAction(e->{
                try {
                    if (acCheckbox.isSelected()) {

                        if(Main.getCurrentActiveProfile().getPermAC() == true ||
                                (Main.getCurrentActiveProfile().getPermACLocation() == true
                                        && SHSHelpers.getCurrentLocation()!=null && SHSHelpers.getCurrentLocation().getName().toLowerCase() == room.getName())) {
                            room.getAc().setState(true);

                            boolean isAcIconThere = false;
                            for (int a = 0; a < room_layout.getChildren().size(); a++) {
                                if (room_layout.getChildren().contains(room.getIconAC_view())) {
                                    isAcIconThere = true;
                                    break;
                                }
                            }
                            setIconVisibility(room, "AC", true);
                            Controller.appendMessageToConsole("SHC -- AC turned on in Room #"+room.getRoomID()+" "+room.getName());
                        }
                        else {
                            acCheckbox.setSelected(false);
                            throw new SHSException("SHC -- You are not allowed to turn on the " + room.getName() + " AC.");
                        }
                    }
                    else {
                        if(Main.getCurrentActiveProfile().getPermAC() == true ||
                                (Main.getCurrentActiveProfile().getPermACLocation() == true &&
                                        SHSHelpers.getCurrentLocation()!=null && SHSHelpers.getCurrentLocation().getName().toLowerCase() == room.getName()))
                        {
                            if (!room.getAc().isLocked()) {
                                room.getAc().setState(false);
                                setIconVisibility(room, "AC", false);
                                Controller.appendMessageToConsole("SHC -- AC turned off in Room #" + room.getRoomID() + " " + room.getName());
                                room.startThreadToSetRoomTempToOutdoorTemp();
                            }
                            else {
                                acCheckbox.setSelected(true);
                                throw new SHSException("SHC -- Failed attempt to disable locked AC in Room #" + room.getRoomID());
                            }
                        }
                        else{
                            acCheckbox.setSelected(true);
                            throw new SHSException("SHC -- You are not allowed to turn off the " + room.getName() + " AC.");
                        }
                    }
                }
                catch (SHSException s){Controller.appendMessageToConsole(s.getMessage());}

            });
            roomLayout.getChildren().add(acCheckbox);
        }

        Label populationLabel = new Label();
        populationLabel.setText("Population: "+population);
        populationLabel.setTranslateX(20); populationLabel.setTranslateY(240);
        roomLayout.getChildren().add(populationLabel);

        Button closeButton = new Button("Return");
        closeButton.setOnAction(ev-> stage.close());
        closeButton.setTranslateX(20); closeButton.setTranslateY(270);
        closeButton.setId("closeButtonFor"+roomLayout.getId());
        roomLayout.getChildren().add(closeButton);

        ToggleButton autoModeButton = new ToggleButton("Auto ON");
        autoModeButton.setId("autoModeButtonRoom#"+room.getRoomID());
        autoModeButton.setTranslateX(100); autoModeButton.setTranslateY(270);
        autoModeButton.setOnAction(e->{
            if (autoModeButton.isSelected()) {
                autoModeButton.setText("Auto OFF");
                Controller.appendMessageToConsole("Auto mode turned ON in "+room.getName());
                room.setAutoMode(true);
            }
            else {
                autoModeButton.setText("Auto ON");
                Controller.appendMessageToConsole("Auto mode turned OFF in "+room.getName());
                room.setAutoMode(false);
            }
        });

        if (room.getIsAutoModeOn()) {
            autoModeButton.setText("Auto OFF");
            autoModeButton.setSelected(true);
        }
        else {
            autoModeButton.setText("Auto ON");
            autoModeButton.setSelected(false);
        }

        roomLayout.getChildren().add(autoModeButton);

        return roomLayout;
    }

    /**
     * Generate the display layout of a Room within a house, where icons will be displayed if utilities are activated.
     * @param room
     * @return
     */
    public AnchorPane constructRoomLayout(Room room) {
        AnchorPane roomLayout = new AnchorPane();
        roomLayout.setId("roomLayoutID"+room.getRoomID());
        roomLayout.setPrefWidth(225); roomLayout.setPrefHeight(225);
        roomLayout.setStyle("-fx-border-color:#000000");

        Label roomIDNameLabel = new Label("#"+room.getRoomID()+"\n"+room.getName());
        roomIDNameLabel.setTranslateX(50); roomIDNameLabel.setTranslateY(60);
        roomLayout.getChildren().add(roomIDNameLabel);

        Line line6 = new Line(); line6.setStartY(180); line6.setEndY(205); line6.setTranslateX(45); roomLayout.getChildren().add(line6);
        Line line7 = new Line(); line7.setStartY(180); line7.setEndY(205); line7.setTranslateX(90); roomLayout.getChildren().add(line7);
        Line line8 = new Line(); line8.setStartY(180); line8.setEndY(205); line8.setTranslateX(135); roomLayout.getChildren().add(line8);
        Line line9 = new Line(); line9.setStartY(180); line9.setEndY(205); line9.setTranslateX(180); roomLayout.getChildren().add(line9);

        roomLayout.getChildren().addAll(room.getIconAC_view(), room.getIconDoor_view(), room.getIconLight_view(),
                room.getIconMD_view(), room.getIconWindow_view());

        return roomLayout;
    }

    /**
     * Check if there are any lights on in a specific Room.
     * @param room
     * @return
     */
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

    /**
     * Check if there are any doors open for a specific Room.
     * @param room
     * @return
     */
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

    /**
     * Check if all of a Room's doors are locked
     * @param room
     * @return
     */
    public boolean areDoorsLocked(Room room){
        boolean yes = true;
        for (int door = 0; door < room.getDoorCollection().length; door++) {
            if (!room.getDoorCollection()[door].isLocked()) {
                yes = false;
                break;
            }
        }
        return yes;
    }

    /**
     * Check if there are any windows open for a specific Room.
     * @param room
     * @return
     */
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

    /**
     * Check if there are any Motion Detectors on in the house
     * @return
     */
    public boolean anyMDsOn() {
        boolean yes = false;
        for (int r = 0; r < Main.getHouseholdLocations().length; r++) {
            if (Main.getHouseholdLocations()[r].getMd().getState()) {
                yes = true;
                break;
            }
        }
        return yes;
    }

    /**
     * Set a specified icon's visibility in a given room
     * @param room
     * @param utilityType
     * @param isVisible
     */
    public void setIconVisibility(Room room, String utilityType, boolean isVisible) {
        try {
            for (int lay = 0; lay < this.layout.getChildren().size(); lay++) {
                try {
                    if (this.layout.getChildren().get(lay).getId().equals("roomLayoutID" + room.getRoomID())) {
                        AnchorPane room_layout = (AnchorPane) this.layout.getChildren().get(lay);

                        if (utilityType.equals("Light")) {
                            room.setIconLightVisibility(isVisible);
                        } else if (utilityType.equals("Window")) {
                            room.setIconWindowVisibility(isVisible);
                        } else if (utilityType.equals("Door")) {
                            room.setIconDoorVisibility(isVisible);
                        } else if (utilityType.equals("MD")) {
                            room.setIconMDVisibility(isVisible);
                            if (SHSHelpers.isIs_away()) {
                                if (isVisible) {
                                    room_layout.setStyle("-fx-background-color:red;-fx-border-color:black;");
                                } else {
                                    room_layout.setStyle("-fx-border-color:black;");
                                }
                            }
                        } else if (utilityType.equals("AC")) {
                            room.setIconACVisibility(isVisible);
                        }

                        for (int l = 0; l < room_layout.getChildren().size(); l++) {
                            try {
                                if ((room_layout.getChildren().get(l).getId().equals("icon" + utilityType + "ViewRoom#" + room.getRoomID()))) {

                                    if (utilityType.equals("Light")) {
                                        room_layout.getChildren().set(l, room.getIconLight_view());
                                    }
                                    else if (utilityType.equals("Window")) {
                                        room_layout.getChildren().set(l, room.getIconWindow_view());
                                    }
                                    else if (utilityType.equals("Door")) {
                                        room_layout.getChildren().set(l, room.getIconDoor_view());
                                    }
                                    else if (utilityType.equals("MD")) {
                                        room_layout.getChildren().set(l, room.getIconMD_view());
                                    }
                                    else if (utilityType.equals("AC")) {
                                        room_layout.getChildren().set(l, room.getIconAC_view());
                                    }
                                    break;
                                }
                            } catch (Exception e){}
                        }

                        this.layout.getChildren().set(lay, room_layout);
                        break;
                    }
                }
                catch (Exception e){}
            }
        }
        catch (Exception ex){}
    }

    /**
     * Automatically turn on all lights in a room
     * @param room
     */
    public void autoTurnOnLight(Room room) {
        try {
            for (int lay = 0; lay < this.layout.getChildren().size(); lay++) {
                if (this.layout.getChildren().get(lay).getId().equals("roomLayoutID"+room.getRoomID())) {

                    AnchorPane room_layout = (AnchorPane) this.layout.getChildren().get(lay);

                    for (int l = 0; l < room.getLightCollection().length; l++) {
                        try {
                            room.getLightCollection()[l].setState(true);
                            room.getLightCollection()[l].setLocked(false);
                            setIconVisibility(room, "Light", true);
                            for (int cb = 0; cb < room_layout.getChildren().size(); cb++) {
                                try {
                                    if (room_layout.getChildren().get(cb).getId().equals
                                            ("lightCheckboxID#"+room.getLightCollection()[l].getUtilityID())) {
                                        CheckBox tempCB = (CheckBox) room_layout.getChildren().get(cb);
                                        tempCB.setStyle("-fx-border-color:#00ff00");
                                        tempCB.setSelected(true);
                                        room_layout.getChildren().set(cb, tempCB);
                                        break;
                                    }
                                }
                                catch(Exception e){}
                            }
                            Controller.appendMessageToConsole("SHP -- Light in "+room.getName()+" automatically turned on.");
                        }
                        catch(Exception e){}
                    }
                    this.layout.getChildren().set(lay, room_layout);
                    break;
                }
            }
        }catch (Exception ex){}
    }

    /**
     * Automatically toggle the state of a room's motion detector
     * @param room
     * @param state
     */
    public void autoTurnOnOffMD(Room room, boolean state) {
        try {
            for (int lay = 0; lay < this.layout.getChildren().size(); lay++) {
                if (this.layout.getChildren().get(lay).getId().equals("roomLayoutID"+room.getRoomID())) {
                    AnchorPane room_layout = (AnchorPane) this.layout.getChildren().get(lay);
                    room.getMd().setState(state);
                    try {
                        setIconVisibility(room, "MD", state);
                        for (int cb = 0; cb < room_layout.getChildren().size(); cb++) {
                            try {
                                if (room_layout.getChildren().get(cb).getId().equals
                                        ("motionDetectorCheckboxID#"+room.getMd().getUtilityID())) {
                                    CheckBox tempCB = (CheckBox) room_layout.getChildren().get(cb);
                                    tempCB.setSelected(state);
                                    room_layout.getChildren().set(cb, tempCB);
                                    break;
                                }
                            }
                            catch(Exception e){}
                        }
                        if (room.getMd().getState()) {
                            Controller.appendMessageToConsole("[Edit Context] -- M.D automatically triggered in "+room.getName()+".");
                        }
                        else {
                            Controller.appendMessageToConsole("[Edit Context] -- M.D automatically deactivated in "+room.getName()+".");
                        }
                    }
                    catch(Exception e){}
                    this.layout.getChildren().set(lay, room_layout);
                    break;
                }
            }
        }
        catch(Exception e){}
    }

    /**
     * Automatically open all windows in a room, if there are any
     * @param room
     */
    public void autoOpenWindows(Room room) {
        try {
            for (int lay = 0; lay < this.layout.getChildren().size(); lay++) {
                try {
                    if (this.layout.getChildren().get(lay).getId().equals("roomLayoutID" + room.getRoomID())) {

                        AnchorPane room_layout = (AnchorPane) this.layout.getChildren().get(lay);

                        for (int l = 0; l < room.getWindowCollection().length; l++) {
                            try {
                                if (!room.getWindowCollection()[l].isBlocked()) {
                                    room.getWindowCollection()[l].setState(true);
                                    setIconVisibility(room, "Window", true);
                                    for (int cb = 0; cb < room_layout.getChildren().size(); cb++) {
                                        try {
                                            if (room_layout.getChildren().get(cb).getId().equals
                                                    ("windowCheckboxID#" + room.getWindowCollection()[l].getUtilityID())) {
                                                CheckBox tempCB = (CheckBox) room_layout.getChildren().get(cb);
                                                tempCB.setStyle("-fx-border-color:#00ff00");
                                                tempCB.setSelected(true);
                                                room_layout.getChildren().set(cb, tempCB);
                                                break;
                                            }
                                        } catch (Exception e) {}
                                    }
                                    Controller.appendMessageToConsole("SHH -- Window #" +
                                            room.getWindowCollection()[l].getUtilityID() + " in " + room.getName() + " automatically opened.");
                                }
                                else {
                                    throw new SHSException("SHH -- Blocked Window #" +
                                            room.getWindowCollection()[l].getUtilityID() + " in " + room.getName() + " attempted to open.");
                                }
                            } catch (SHSException e) {Controller.appendMessageToConsole(e.getMessage());}
                        }
                        this.layout.getChildren().set(lay, room_layout);
                        break;
                    }
                }
                catch (Exception e){}
            }
        }catch (Exception ex){}
    }

    /**
     * Automatically turn on or off a room's Air Conditioner
     * @param room
     * @param state
     */
    public void autoTurnOnOffAC(Room room, boolean state) {
        try {
            for (int lay = 0; lay < this.layout.getChildren().size(); lay++) {
                try {
                    if (this.layout.getChildren().get(lay).getId().equals("roomLayoutID"+room.getRoomID())) {
                        AnchorPane room_layout = (AnchorPane) this.layout.getChildren().get(lay);

                        try {
                            if (room.getAc() != null) {
                                room.getAc().setState(state);
                                setIconVisibility(room, "AC", state);
                                for (int cb = 0; cb < room_layout.getChildren().size(); cb++) {
                                    try {
                                        if (room_layout.getChildren().get(cb).getId().equals
                                                ("airConditionerCheckboxID#"+room.getAc().getUtilityID())) {
                                            CheckBox tempCB = (CheckBox) room_layout.getChildren().get(cb);
                                            tempCB.setSelected(state);
                                            room_layout.getChildren().set(cb, tempCB);
                                            break;
                                        }
                                    }
                                    catch(Exception e){}
                                }
                            }
                        }
                        catch(Exception e){}
                        this.layout.getChildren().set(lay, room_layout);
                        break;
                    }
                }
                catch (Exception e){}

            }
        }
        catch(Exception e){}
    }
}
