package house;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import sample.*;
import utilities.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class House {

    private int numOfRooms;
    private Room[] rooms;
    private AnchorPane layout; // a layout of rooms only

    public House(String houseLayoutFileName) throws FileNotFoundException {

        //using File IO, read a plain text (.txt) file called "houseLayoutFileName"
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

        // after all Room initializations, the house layout will be set up
        setupHouseLayout(this.rooms);
    }

    public int getNumOfRooms() {
        return numOfRooms;
    }
    public Room[] getRooms() {
        return rooms;
    }
    public AnchorPane getLayout() {
        return layout;
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
    public AnchorPane constructRoomLayoutSHCversion(Room room, AnchorPane room_layout, int population, Stage stage) {
        AnchorPane roomLayout = new AnchorPane();
        roomLayout.setId("roomLayoutID"+room.getRoomID());
        roomLayout.setPrefWidth(225); roomLayout.setPrefHeight(350);
        roomLayout.setStyle("-fx-border-color:#000000");

        int ID = room.getRoomID();
        String name = room.getName();

        // labels (no need IDs)
        Label roomIDNameLabel = new Label("#"+ID+" "+name);
        roomIDNameLabel.setTranslateX(10);
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

        // borderlines (no need IDs)
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

            checkBox.setOnAction(e->{
                if (checkBox.isSelected()) {
                    // turn the light on
                    for (int light = 0; light < room.getLightCollection().length; light++) {
                        if (room.getLightCollection()[light].getUtilityID() == Integer.parseInt(checkBox.getId().substring(16))) {
                            room.getLightCollection()[light].setState(true);
                            appendMessageToConsole("Light #"+room.getLightCollection()[light].getUtilityID()+" opened in Room #"+room.getRoomID()+" "+room.getName());
                            break;
                        }
                    }
                }
                else {
                    // turn the light off
                    for (int light = 0; light < room.getLightCollection().length; light++) {
                        if (room.getLightCollection()[light].getUtilityID() == Integer.parseInt(checkBox.getId().substring(16))) {
                            room.getLightCollection()[light].setState(false);
                            appendMessageToConsole("Light #"+room.getLightCollection()[light].getUtilityID()+" closed in Room #"+room.getRoomID()+" "+room.getName());
                            break;
                        }
                    }
                }
                if (anyLightsOn(room)) {
                    boolean isLightIconThere = false;
                    for (int a = 0; a < room_layout.getChildren().size(); a++) {
                        if (room_layout.getChildren().contains(room.getIconLight_view())) {
                            isLightIconThere = true;
                            break;
                        }
                    }
                    if (!isLightIconThere){
                        room_layout.getChildren().add(room.getIconLight_view());
                    }
                }
                else {
                    try {
                        room_layout.getChildren().remove(room.getIconLight_view());
                    }catch (Exception ex){}
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

            checkBox.setOnAction(e->{
                if (checkBox.isSelected()) {
                    // open the window (if not blocked)
                    for (int win = 0; win < room.getWindowCollection().length; win++) {
                        if (room.getWindowCollection()[win].getUtilityID() == Integer.parseInt(checkBox.getId().substring(17))) {
                            if (!room.getWindowCollection()[win].isBlocked()) {
                                room.getWindowCollection()[win].setState(true);
                                appendMessageToConsole("Window #"+room.getWindowCollection()[win].getUtilityID()+" opened in Room #"+room.getRoomID()+" "+room.getName());
                            }
                            else {
                                appendMessageToConsole("User attempted to open blocked Window #"+room.getWindowCollection()[win].getUtilityID()+" in Room #"+room.getRoomID()+" "+room.getName());
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
                            if (!room.getWindowCollection()[win].isBlocked()) {
                                room.getWindowCollection()[win].setState(false);
                                appendMessageToConsole("Window #"+room.getWindowCollection()[win].getUtilityID()+" closed in Room #"+room.getRoomID()+" "+room.getName());
                            }
                            else {
                                appendMessageToConsole("User attempted to close blocked Window #"+room.getWindowCollection()[win].getUtilityID()+" in Room #"+room.getRoomID()+" "+room.getName());
                                checkBox.setSelected(true);

                            }
                            break;
                        }
                    }
                }
                if (anyWindowsOpen(room)) {
                    boolean isWindowIconThere = false;
                    for (int a = 0; a < room_layout.getChildren().size(); a++) {
                        if (room_layout.getChildren().contains(room.getIconWindow_view())) {
                            isWindowIconThere = true;
                            break;
                        }
                    }
                    if (!isWindowIconThere){
                        room_layout.getChildren().add(room.getIconWindow_view());
                    }
                }
                else {
                    try {
                        room_layout.getChildren().remove(room.getIconWindow_view());
                    }catch (Exception ex){}
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

            checkBox.setOnAction(e->{
                if (checkBox.isSelected()) {
                    // open the door
                    for (int door = 0; door < room.getDoorCollection().length; door++) {
                        if (room.getDoorCollection()[door].getUtilityID() == Integer.parseInt(checkBox.getId().substring(15))) {
                            room.getDoorCollection()[door].setState(true);
                            appendMessageToConsole("Door #"+room.getDoorCollection()[door].getUtilityID()+" opened in Room #"+room.getRoomID()+" "+room.getName());
                            break;
                        }
                    }
                }
                else {
                    // close the door
                    for (int door = 0; door < room.getDoorCollection().length; door++) {
                        if (room.getDoorCollection()[door].getUtilityID() == Integer.parseInt(checkBox.getId().substring(15))) {
                            room.getDoorCollection()[door].setState(false);
                            appendMessageToConsole("Door #"+room.getDoorCollection()[door].getUtilityID()+" closed in Room #"+room.getRoomID()+" "+room.getName());
                            break;
                        }
                    }
                }
                if (anyDoorsOpen(room)) {
                    boolean isDoorIconThere = false;
                    for (int a = 0; a < room_layout.getChildren().size(); a++) {
                        if (room_layout.getChildren().contains(room.getIconDoor_view())) {
                            isDoorIconThere = true;
                            break;
                        }
                    }
                    if (!isDoorIconThere){
                        room_layout.getChildren().add(room.getIconDoor_view());
                    }
                }
                else {
                    try {
                        room_layout.getChildren().remove(room.getIconDoor_view());
                    }catch (Exception ex){}
                }
            });
            roomLayout.getChildren().add(checkBox);
            translate_y += 20;
        }

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
            if (mdCheckbox.isSelected()) {
                room.getMd().setState(true);
                boolean isMdIconThere = false;
                for (int a = 0; a < room_layout.getChildren().size(); a++) {
                    if (room_layout.getChildren().contains(room.getIconMD_view())) {
                        isMdIconThere = true;
                        break;
                    }
                }
                if (!isMdIconThere){
                    room_layout.getChildren().add(room.getIconMD_view());
                }
                appendMessageToConsole("Motion detector triggered in Room #"+room.getRoomID()+" "+room.getName());
            }
            else {
                room.getMd().setState(false);
                try {
                    room_layout.getChildren().remove(room.getIconMD_view());
                }catch(Exception ex){}
                appendMessageToConsole("Motion detector disabled in Room #"+room.getRoomID()+" "+room.getName());
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
                if (acCheckbox.isSelected()) {
                    room.getAc().setState(true);
                    boolean isAcIconThere = false;
                    for (int a = 0; a < room_layout.getChildren().size(); a++) {
                        if (room_layout.getChildren().contains(room.getIconAC_view())) {
                            isAcIconThere = true;
                            break;
                        }
                    }
                    if (!isAcIconThere){
                        room_layout.getChildren().add(room.getIconAC_view());
                    }
                    appendMessageToConsole("AC turned on in Room #"+room.getRoomID()+" "+room.getName());
                }
                else {
                    room.getAc().setState(false);
                    try {
                        room_layout.getChildren().remove(room.getIconAC_view());
                    }catch(Exception ex){}
                    appendMessageToConsole("AC turned off in Room #"+room.getRoomID()+" "+room.getName());
                }
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

        return roomLayout;
    }
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
    public void appendMessageToConsole(String message) {
        for (int a = 0; a < Main.getMain_dashboard().getChildren().size(); a++) {
            if (Main.getMain_dashboard().getChildren().get(a).getId().equals("OutputConsole")) {
                TextArea textArea = (TextArea) Main.getMain_dashboard().getChildren().get(a);
                textArea.appendText(LocalDateTime.now().toString().substring(0,10)+ " "+
                        LocalDateTime.now().toString().substring(11,19)+" -- "+message+"\n");
                Main.getMain_dashboard().getChildren().set(a, textArea);
                break;
            }
        }
    }
}
