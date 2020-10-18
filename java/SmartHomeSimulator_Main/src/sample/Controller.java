package sample;
import house.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import utilities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateTimeStringConverter;
import javax.swing.*;
import javax.swing.text.html.ImageView;
import java.awt.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import sample.Main.*;

public class Controller {

    private static int pixelY = 70;
    private static int numberOfTimesProfileHyperlinkClicked = 0;
    private static int numberOfTimesEditContextLinkStage1Accessed = 0;
    private static int numberOfTimesEditContextLinkStage2Accessed = 0;
    private static int numberOfAddedProfiles = 0;

    private static CheckBox[] profileCheckboxes;
    private static CheckBox[] roomCheckboxes;

    /**SCENE-SWITCHING METHODS START */
    public static void goToMainDashboardScene(UserProfile userProfile, Hyperlink hyperlink) {
        for (int prof = 0; prof < Main.profiles.length; prof++) {
            if (Main.profiles[prof].getProfileID()==userProfile.getProfileID()) {
                Main.profiles[prof].setLoggedIn(true);
                Main.currentActiveProfile = Main.profiles[prof];
                /**DEBUGGING*/ System.out.println(Main.currentActiveProfile.getProfileID()+" - "+ Main.currentActiveProfile.getType());
                Main.currentLocation = null;
                break;
            }
        }

        String stageTitle = "Smart Home Simulator -- logged in as #"+userProfile.getProfileID()+" \""+userProfile.getType().toUpperCase()+"\"";
        if (userProfile.isAdmin()) {stageTitle+=" (ADMIN)";}
        Main.main_stage.setTitle(stageTitle);

        ///
        Hyperlink logoutLink = new Hyperlink("Logout");
        logoutLink.setId("logoutLinkForProfile"+userProfile.getProfileID());
        logoutLink.setTranslateX(hyperlink.getTranslateX()+60); logoutLink.setTranslateY(hyperlink.getTranslateY());

        logoutLink.setOnAction(ACT2 -> {
            Main.profiles[userProfile.getProfileID()-1].setCurrentLocation(null);
            Main.profiles[userProfile.getProfileID() - 1].setLoggedIn(false);
            Main.currentActiveProfile = null;

            Main.createMainDashboardNecessities();

            logoutLink.setDisable(true);

            for (int a = 0; a < Main.profileSelection.getChildren().size(); a++) {
                if (Main.profileSelection.getChildren().get(a).getId().contains("loginLinkForProfile")) {
                    Hyperlink hp = (Hyperlink) Main.profileSelection.getChildren().get(a);
                    hp.setDisable(false);
                    Main.profileSelection.getChildren().set(a,hp);
                }
            }
            Main.main_stage.setTitle("Smart Home Simulator");
            Main.profileSelection.getChildren().remove(logoutLink);
        });
        Main.profileSelection.getChildren().add(logoutLink);

        Main.createMainDashboardNecessities();
        Main.main_stage.setScene(Main.dashboardScene);
    }

    public static void returnToProfileSelectionPage() {
        Main.profileBox = new Stage();
        Main.profileBox.setResizable(false);
        Main.profileBox.initModality(Modality.APPLICATION_MODAL);
        Main.profileBox.setTitle("Manage Profiles");
        Main.profileBox.setWidth(900);
        Main.profileBox.setHeight(600);
        Main.transformProfileSelectionPageScene();
        Main.profileBox.setScene(Main.profileScene);
        Main.profileBox.showAndWait();
    }
    /**SCENE-SWITCHING METHODS END */

    /**DASHBOARD METHODS*/
    public static void startSimulation(ToggleButton t, Button b, TextArea ta, TabPane tab) {
        Main.createMainDashboardNecessities();
        if (!t.isSelected()) {
            t.setText("Start\nSimulation");
            t.setPrefWidth(t.getPrefWidth()); t.setPrefHeight(t.getPrefHeight());
            ta.setDisable(true);
            Main.houseLayout.setDisable(true);
            b.setDisable(true);
            tab.setDisable(true);
            Main.simulationIsOn = false;
        }
        else {
            t.setText("Stop\nSimulation");
            t.setPrefWidth(t.getPrefWidth()); t.setPrefHeight(t.getPrefHeight());
            b.setDisable(false);
            Main.houseLayout.setDisable(false);
            ta.setDisable(false);
            tab.setDisable(false);
            Main.simulationIsOn = true;
        }
        Main.createMainDashboardNecessities();
    }

    public static void editContext() {
        Main.editContextStage = new Stage(); Main.editContextStage.setResizable(false);
        Main.editContextStage.initModality(Modality.APPLICATION_MODAL);
        Main.editContextStage.setTitle("Edit Simulation Context");
        Main.editContextStage.setWidth(650); Main.editContextStage.setHeight(650);
        generateEditContextScene();
        Main.editContextStage.setScene(Main.editContextScene);
        Main.editContextStage.showAndWait();
    }

    public static void generateEditContextScene() {
        if (numberOfTimesEditContextLinkStage1Accessed == 0) {

            DatePicker datePicker = new DatePicker();
            datePicker.setPromptText("select date...");
            datePicker.setId("dateChooser");
            datePicker.setTranslateX(80);
            datePicker.setTranslateY(30);

            Label datePickerLabel = new Label("Set date:");
            datePickerLabel.setId("datePickerLabel");
            datePickerLabel.setTranslateX(20);
            datePickerLabel.setTranslateY(30);

            Label timePickerLabel = new Label("Set time:");
            timePickerLabel.setId("timePickerLabel");
            timePickerLabel.setTranslateX(20);
            timePickerLabel.setTranslateY(60);

            TextField hourField = new TextField();
            hourField.setId("hourField");
            hourField.setPrefHeight(30);
            hourField.setPrefWidth(60);
            hourField.setTranslateX(100);
            hourField.setTranslateY(60);
            hourField.setPromptText("00-23");

            TextField minuteField = new TextField();
            minuteField.setId("minuteField");
            minuteField.setPrefHeight(30);
            minuteField.setPrefWidth(60);
            minuteField.setTranslateX(180);
            minuteField.setTranslateY(60);
            minuteField.setPromptText("00-59");

            Button confirmDTbutton = new Button("Confirm\nDate &\nTime");
            confirmDTbutton.setId("confirmDateAndTimeButton");
            confirmDTbutton.setTranslateX(300);
            confirmDTbutton.setTranslateY(30);

            Main.editContextLayout.getChildren().addAll(datePicker, datePickerLabel, timePickerLabel, hourField,
                    minuteField, confirmDTbutton);

            Line line1 = new Line();
            line1.setId("line1");
            line1.setStartX(0);
            line1.setEndX(650);
            line1.setTranslateY(100);
            Main.editContextLayout.getChildren().add(line1);

            Line line2 = new Line();
            line2.setId("line2");
            line2.setStartY(0);
            line2.setEndY(100);
            line2.setTranslateX(400);
            Main.editContextLayout.getChildren().add(line2);

            Label temperatureLabel = new Label("Modify\nOutdoor Temperature");
            temperatureLabel.setId("temperatureLabel");
            temperatureLabel.setTranslateX(450);
            temperatureLabel.setTranslateY(20);
            Main.editContextLayout.getChildren().add(temperatureLabel);

            TextField tempText = new TextField();
            tempText.setId("temperatureText");
            tempText.setPrefHeight(30);
            tempText.setPrefWidth(60);
            tempText.setTranslateX(450);
            tempText.setTranslateY(60);
            tempText.setPromptText("Temp");
            Main.editContextLayout.getChildren().add(tempText);

            Button tempButton = new Button("Confirm");
            tempButton.setId("confirmTemperatureButton");
            tempButton.setOnAction(e -> {
                try{
                    for(int i=0; i < Main.main_dashboard.getChildren().size(); i++){
                        if (Main.main_dashboard.getChildren().get(i).getId().equals("temp")){
                            if(!tempText.getCharacters().toString().isEmpty()) {
                                Label label = (Label) Main.main_dashboard.getChildren().get(i);
                                label.setText("Outside Temp.\n" + tempText.getCharacters().toString() + "°C");
                                Main.main_dashboard.getChildren().set(i, label);
                                break;
                            }
                        }
                    }
                }catch (Exception err){
                    System.out.print("There was an error while modifying the outdoor temperature.");
                }
            });
            tempButton.setTranslateX(540);
            tempButton.setTranslateY(60);
            Main.editContextLayout.getChildren().add(tempButton);

            Label roomsLabel = new Label("Click on a room you would like to move to, or change a room's number of occupants");
            roomsLabel.setTranslateY(105);
            roomsLabel.setTranslateX(20);
            roomsLabel.setId("roomsLabel");
            Main.editContextLayout.getChildren().add(roomsLabel);

            int transY = 120;
            for (int r = 0; r < Main.householdLocations.length; r++) {
                Label l1 = new Label("# of people: " + Main.householdLocations[r].getNumberOfPeopleInside());
                l1.setId("numOfPeopleInRoom"+Main.householdLocations[r].getRoomID());
                l1.setTranslateX(120);
                l1.setTranslateY(transY + 5);
                int fr = r;
                Hyperlink hyp = new Hyperlink(Main.householdLocations[r].getName());
                hyp.setId("hyperlinkForRoom-"+Main.householdLocations[r].getName());
                hyp.setTranslateX(20);
                hyp.setTranslateY(transY);
                int finalR = r;
                hyp.setOnAction(e -> {

                    // change the label of the number of people in the origin and destination rooms
                    if ( (Main.currentActiveProfile != null)) {
                        if (Main.currentLocation == null) {
                            Main.householdLocations[fr].setNumberOfPeopleInside(Main.householdLocations[fr].getNumberOfPeopleInside() + 1);
                        }
                        else {
                            if (!(Main.currentLocation == Main.householdLocations[fr])) {
                                Main.householdLocations[fr].setNumberOfPeopleInside(Main.householdLocations[fr].getNumberOfPeopleInside() + 1);
                                Main.currentLocation.setNumberOfPeopleInside(Main.currentLocation.getNumberOfPeopleInside() - 1);
                            }
                        }
                    }
                    for (int j = 0; j < Main.editContextLayout.getChildren().size(); j++) {
                        try {
                            if (Main.editContextLayout.getChildren().get(j).getId().equals("numOfPeopleInRoom" + Main.householdLocations[finalR].getRoomID())) {
                                Label label = (Label) Main.editContextLayout.getChildren().get(j);
                                label.setText("# of people: " + Main.householdLocations[finalR].getNumberOfPeopleInside());
                                Main.editContextLayout.getChildren().set(j, label);
                                break;
                            }
                        } catch (Exception ex) {}
                    }

                    for (int i = 0; i < Main.editContextLayout.getChildren().size(); i++) {
                        try {
                            if (Main.editContextLayout.getChildren().get(i).getId().equals("numOfPeopleInRoom"+Main.currentLocation.getRoomID())) {
                                Label label = (Label) Main.editContextLayout.getChildren().get(i);
                                label.setText("# of people: " + Main.currentLocation.getNumberOfPeopleInside());
                                Main.editContextLayout.getChildren().set(i, label);
                                break;
                            }
                        } catch (Exception ex) {}
                    }
                    changeLocation(Main.householdLocations[fr]);
                    for (int a = 0; a < Main.editContextLayout2.getChildren().size(); a++) {
                        try {
                            if (Main.editContextLayout2.getChildren().get(a).getId().equals("currentRoomOfProfile" + Main.currentActiveProfile.getProfileID())){
                                Label label = (Label) Main.editContextLayout2.getChildren().get(a);
                                label.setText(Main.currentLocation.getName());
                                Main.editContextLayout2.getChildren().set(a, label);
                                break;
                            }
                        }catch (Exception ex){}
                    }
                });
                Main.editContextLayout.getChildren().addAll(hyp, l1);
                transY += 20;
            }
            Line line3 = new Line(); line3.setId("line3"); line3.setStartX(0); line3.setEndX(650); line3.setTranslateY(350);
            Main.editContextLayout.getChildren().add(line3);

            Label windowsLabel = new Label("Window-Blocking: Check a box " +
                    "(or many) whose windows you would like to prevent from opening\n" +
                    "or closing");
            windowsLabel.setId("labelForWindowBlocking");
            windowsLabel.setTranslateY(360);
            windowsLabel.setTranslateX(20);
            Main.editContextLayout.getChildren().add(windowsLabel);

            transY = 400;
            int transX = 20;
            int windowCount = 0;
            for (int r = 0; r < Main.householdLocations.length; r++) {

                /**for window-blocking purposes */
                for (int w = 0; w < Main.householdLocations[r].getWindowCollection().length; w++) {
                    windowCount++;
                    CheckBox checkBox = new CheckBox("W#"+Main.householdLocations[r].getWindowCollection()[w].getUtilityID()+
                            " in "+Main.householdLocations[r].getName());
                    checkBox.setTranslateX(transX);
                    checkBox.setTranslateY(transY);
                    int finalR = r;
                    int finalW = w;
                    checkBox.setOnAction(e->{
                        if (checkBox.isSelected()) {
                            Main.householdLocations[finalR].getWindowCollection()[finalW].setBlocked(true);
                        }
                        else {
                            Main.householdLocations[finalR].getWindowCollection()[finalW].setBlocked(false);
                        }
                    });

                    Main.editContextLayout.getChildren().add(checkBox);
                    if (windowCount % 8 != 0) {
                        transY += 20;
                    }
                    else {
                        transY = 400;
                        transX += 180;
                    }
                }
            }
            Button moreButton = new Button("More...");
            moreButton.setId("GoToEditContextScene2FromScene1");
            moreButton.setTranslateX(200);
            moreButton.setTranslateY(580);
            moreButton.setOnAction(e -> {
                generateEditContextScene2(); // will design Main.editContextLayout2
                Main.editContextStage.setScene(Main.editContextScene2);
            });
            Main.editContextLayout.getChildren().add(moreButton);

            Button closeButton = new Button("Close");
            closeButton.setId("closeEditContextFromScene1Button");
            closeButton.setTranslateX(400);
            closeButton.setTranslateY(580);
            closeButton.setOnAction(e -> Main.editContextStage.close());
            Main.editContextLayout.getChildren().add(closeButton);



            numberOfTimesEditContextLinkStage1Accessed++;
        }
    }

    /**
     * Build the user interface for modifying the locations of house inhabitants.
     */
    public static void generateEditContextScene2() {

        Label movepplLabel = new Label("Select one or more Profiles, and only one Room where you would like to place it/them.");
        movepplLabel.setTranslateY(10); movepplLabel.setId("movePeopleLabel");

        Label profilelistLabel = new Label("Profiles");
        profilelistLabel.setId("profileListLabel");
        profilelistLabel.setTranslateY(60);
        profilelistLabel.setTranslateX(100);

        Label roomlistLabel = new Label("Rooms");
        roomlistLabel.setId("roomListLabel");
        roomlistLabel.setTranslateY(60);
        roomlistLabel.setTranslateX(400);

        Label currentlocationLabel = new Label("Current Location");
        currentlocationLabel.setTranslateY(60);
        currentlocationLabel.setId("currentLocationLabel");
        currentlocationLabel.setTranslateX(250);

        Button relocateProfileButton = new Button("Relocate Profile(s)");
        relocateProfileButton.setId("relocateProfileButton2");
        relocateProfileButton.setTranslateX(300); relocateProfileButton.setTranslateY(400);
        relocateProfileButton.setDisable(true);
        relocateProfileButton.setOnAction(e -> relocateProfile(profileCheckboxes, roomCheckboxes));

        Button closeButton = new Button("Close");
        closeButton.setId("closeFromEditContextScene2Button");
        closeButton.setTranslateX(325);
        closeButton.setTranslateY(600);
        closeButton.setOnAction(e -> Main.editContextStage.close());

        Button gobackButton = new Button("Go Back");
        gobackButton.setId("returnToEditContextScene1Button");
        gobackButton.setTranslateX(225);
        gobackButton.setTranslateY(600);
        gobackButton.setOnAction(e -> Main.editContextStage.setScene(Main.editContextScene));

        if (numberOfTimesEditContextLinkStage2Accessed == 0 || Main.numberOfProfiles != numberOfAddedProfiles) {

            // GUI elements to add only the first time you enter Scene 2
            if (numberOfTimesEditContextLinkStage2Accessed == 0) {

                Main.editContextLayout2.getChildren().add(movepplLabel);
                Main.editContextLayout2.getChildren().add(profilelistLabel);
                Main.editContextLayout2.getChildren().add(roomlistLabel);
                Main.editContextLayout2.getChildren().add(currentlocationLabel);
                Main.editContextLayout2.getChildren().add(relocateProfileButton);
                Main.editContextLayout2.getChildren().add(closeButton);
                Main.editContextLayout2.getChildren().add(gobackButton);

                // show the room checkboxes
                int transY3 = 80;
                roomCheckboxes = new CheckBox[Main.householdLocations.length];
                for (int r = 0; r < Main.householdLocations.length; r++) {
                    CheckBox checkBox = new CheckBox(Main.householdLocations[r].getName());
                    checkBox.setTranslateY(transY3);
                    checkBox.setTranslateX(400);
                    checkBox.setId("checkboxRoom" + Main.householdLocations[r].getRoomID());
                    roomCheckboxes[r] = checkBox;
                    Main.editContextLayout2.getChildren().add(checkBox);
                    transY3 += 20;
                }
            }

            if (Main.numberOfProfiles != numberOfAddedProfiles) {
                // show the profile checkboxes

                int transY2 = 80;
                if (Main.profiles != null) {
                    int index = 0;
                    profileCheckboxes = new CheckBox[Main.profiles.length];
                    for (int p = 0; p < Main.profiles.length; p++) {

                        if (!Main.profiles[p].isLoggedIn()) {
                            CheckBox checkBox = new CheckBox(Main.profiles[p].getProfileID() + "--" + Main.profiles[p].getType());
                            checkBox.setTranslateY(transY2);
                            checkBox.setTranslateX(100);
                            checkBox.setId("checkboxProfile" + Main.profiles[p].getProfileID());

                            Label currentRoomLabel = new Label();
                            currentRoomLabel.setId("currentRoomOfProfile" + Main.profiles[p].getProfileID());
                            if (Main.profiles[p].getCurrentLocation() != null) {
                                currentRoomLabel.setText(Main.profiles[p].getCurrentLocation().getName());
                            } else {
                                currentRoomLabel.setText("No location");
                            }
                            currentRoomLabel.setTranslateY(transY2);
                            currentRoomLabel.setTranslateX(250);

                            boolean alreadyThere = false;
                            for (int i = 0; i < Main.editContextLayout2.getChildren().size(); i++) {
                                if (Main.editContextLayout2.getChildren().get(i).getId().equals("checkboxProfile"+Main.profiles[p].getProfileID())) {
                                    alreadyThere = true;
                                    break;
                                }
                            }

                            if (!alreadyThere) {
                                profileCheckboxes[index++] = checkBox;
                                Main.editContextLayout2.getChildren().add(checkBox);
                                Main.editContextLayout2.getChildren().add(currentRoomLabel);
                            }
                            transY2 += 20;
                        }
                    }
                    for (int a = 0; a < Main.editContextLayout2.getChildren().size(); a++) {
                        try {
                            if (Main.editContextLayout2.getChildren().get(a).getId().equals("checkboxProfile" + Main.currentActiveProfile.getProfileID()) ||
                                    Main.editContextLayout2.getChildren().get(a).getId().equals("currentRoomOfProfile" + Main.currentActiveProfile.getProfileID())) {
                                Main.editContextLayout2.getChildren().remove(a);
                            }
                        } catch (Exception e) {}
                    }
                    numberOfAddedProfiles = Main.numberOfProfiles;
                }

            }
        }

        if (Main.currentActiveProfile != null) {
            for (int a = 0; a < Main.editContextLayout2.getChildren().size(); a++) {
                try {
                    if (Main.editContextLayout2.getChildren().get(a).getId().equals("relocateProfileButton2")) {
                        Button b = (Button) Main.editContextLayout2.getChildren().get(a);
                        b.setDisable(false);
                        Main.editContextLayout2.getChildren().set(a, b);
                        break;
                    }
                }catch(Exception e){}
            }
        }
        numberOfTimesEditContextLinkStage2Accessed++;
    }

    /**
     * Place house inhabitants (created Profiles) in specific rooms, or outside home
     * @param profileBoxes
     * @param roomBoxes
     */
    public static void relocateProfile(CheckBox[] profileBoxes, CheckBox[] roomBoxes) {
        try {
            // find the profile checkbox that was selected
            for (int pcb = 0; pcb < profileBoxes.length; pcb++) { // exactly 1 checkbox in this array must be selected
                try {
                    if (profileBoxes[pcb].isSelected()) {

                        // find the room checkbox that was selected
                        for (int rcb = 0; rcb < roomBoxes.length; rcb++) { // exactly 1 checkbox in this array must be selected
                            if (roomBoxes[rcb].isSelected()) {

                                // find the matching profile and room objects
                                for (int L = 0; L < Main.editContextLayout2.getChildren().size(); L++) {

                                    if (Main.editContextLayout2.getChildren().get(L).equals(profileBoxes[pcb])) {
                                        int selectedProfileID = Integer.parseInt(Main.editContextLayout2.getChildren().get(L).getId().substring
                                                (15));

                                        for (int M = 0; M < Main.editContextLayout2.getChildren().size(); M++) {

                                            if (Main.editContextLayout2.getChildren().get(M).equals(roomBoxes[rcb])) {
                                                int selectedRoomID = Integer.parseInt(Main.editContextLayout2.getChildren().get(M).getId().substring
                                                        (Main.editContextLayout2.getChildren().get(M).getId().length() - 1));

                                                // do the relocation

                                                for (int up = 0; up < Main.profiles.length; up++) {
                                                    if (Main.profiles[up].getProfileID() == selectedProfileID) {

                                                        for (int r = 0; r < Main.householdLocations.length; r++) {

                                                            if (Main.householdLocations[r].getRoomID() == selectedRoomID) {
                                                                Room dummyRoom = Main.profiles[up].getCurrentLocation();
                                                                if (Main.profiles[up].getCurrentLocation() == Main.householdLocations[selectedRoomID - 1]) {
                                                                    System.out.println("Profile #" + Main.profiles[up] + " is already in the " + Main.householdLocations[selectedRoomID - 1].getName());
                                                                } else {
                                                                    if (Main.profiles[up].getCurrentLocation() != null) {
                                                                        Main.householdLocations[Main.profiles[up].getCurrentLocation().getRoomID() - 1].setNumberOfPeopleInside(
                                                                                Main.householdLocations[Main.profiles[up].getCurrentLocation().getRoomID() - 1].getNumberOfPeopleInside() - 1);
                                                                    }
                                                                    Main.profiles[up].setCurrentLocation(Main.householdLocations[r]); // THE CHANGE OF ROOM***

                                                                    Main.householdLocations[r].setNumberOfPeopleInside(Main.householdLocations[r].getNumberOfPeopleInside() + 1);
                                                                }
                                                                for (int i = 0; i < Main.editContextLayout2.getChildren().size(); i++) {
                                                                    try {
                                                                        if (Main.editContextLayout2.getChildren().get(i).getId().equals("currentRoomOfProfile" +
                                                                                Main.profiles[up].getProfileID())) {
                                                                            Label label = (Label) Main.editContextLayout2.getChildren().get(i);
                                                                            label.setText(Main.profiles[up].getCurrentLocation().getName());
                                                                            Main.editContextLayout2.getChildren().set(i, label);
                                                                            break;
                                                                        }
                                                                    } catch (Exception e) {
                                                                    }
                                                                }
                                                                for (int a = 0; a < Main.editContextLayout.getChildren().size(); a++) {
                                                                    try {
                                                                        if (Main.editContextLayout.getChildren().get(a).getId().equals("numOfPeopleInRoom" + Main.householdLocations[r].getRoomID())) {
                                                                            Label label = (Label) Main.editContextLayout.getChildren().get(a);
                                                                            label.setText("# of people: " + Main.householdLocations[r].getNumberOfPeopleInside());
                                                                            Main.editContextLayout.getChildren().set(a, label);
                                                                        }
                                                                    } catch (Exception e) {
                                                                    }
                                                                }
                                                                for (int a = 0; a < Main.editContextLayout.getChildren().size(); a++) {
                                                                    try {
                                                                        if (Main.editContextLayout.getChildren().get(a).getId().equals("numOfPeopleInRoom" + dummyRoom.getRoomID())) {
                                                                            Label label = (Label) Main.editContextLayout.getChildren().get(a);
                                                                            label.setText("# of people: " + dummyRoom.getNumberOfPeopleInside());
                                                                            Main.editContextLayout.getChildren().set(a, label);
                                                                        }
                                                                    } catch (Exception e) {
                                                                    }
                                                                }

                                                                break;
                                                            }
                                                        }
                                                        break;
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }catch (Exception e){}
            }
        } catch (Exception e){}
    }

    /**SHP MODULE METHODS START */
    public static void toggleAwayButton(ToggleButton t, TextField tf, Label tf2, Button b) {
        if (!t.isSelected()) {
            tf.setText("");
            tf.setDisable(true);
            b.setDisable(true);
            tf2.setText("Time before Alert: 0 min.");
            tf2.setVisible(false);
            sample.Main.is_away = false;
            Main.timeLimitBeforeAlert = 0;
        }
        else {
            tf2.setVisible(true);
            tf.setDisable(false);
            b.setDisable(false);
            Main.is_away = true;
        }
    }

    public static void setTimeLimitAwayMode(TextField tf, Label label) {
        String time_limit = "";
        try {
            for (int c = 0; c < tf.getText().length(); c++) {
                char character = tf.getText().charAt(c);
                switch (character) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        time_limit += character;
                        break;
                    default:
                        throw new Exception();
                }
            }
            int limit = Integer.parseInt(time_limit);
            if (limit > 10) {
                throw new Exception();
            }
            label.setText("Time before Alert: "+ limit +" min.");
            Main.timeLimitBeforeAlert = limit;
        } catch (Exception e) {
            label.setText("Invalid. Must be a\nnon-negative # <= 10 min.");
        }
    }

    /**TODO: create a method that will send an alert message to the output console
     * TODO: called "notifySuspiciousAct(String warning, MotionDetector md, Sensor sensor)"
     * */
    public static void notifySuspiciousAct(String warning, MotionDetector motionDetector, Sensor sensor) {

        /**TODO: figure out if it was a sensor or motion detector that went off*/
        /**TODO: depending which tool it was, get it's utility_ID and its associated utility*/
        /**TODO: turn on all lights in and outside the house */

        //String report = translateCurrentDateTime() + " -- " + warning;
        //Main.suspBox.appendText(report+"\n");
    }

    /**SHP MODULE METHODS END */

    /**USER PROFILE MANAGEMENT METHODS START HERE*/
    public static void createNewProfile(TextField textField, RadioButton radioButton) {
        UserProfile newProfile;
        try {
            if (!(textField.getText().length() == 1)) {
                throw new Exception("Invalid");
            }
            else {
                switch (textField.getText().charAt(0)) {
                    case 'S':
                    case 's':
                        if (radioButton.isSelected()) {newProfile = new UserProfile("Stranger", true);}
                        else {newProfile = new UserProfile("Stranger", false);}
                        break;
                    case 'G':
                    case 'g':
                        if (radioButton.isSelected()) {newProfile = new UserProfile("Guest", true);}
                        else {newProfile = new UserProfile("Guest", false);}
                        break;
                    case 'C':
                    case 'c':
                        if (radioButton.isSelected()) {newProfile = new UserProfile("Child", true);}
                        else {newProfile = new UserProfile("Child", false);}
                        break;
                    case 'P':
                    case 'p':
                        if (radioButton.isSelected()) {newProfile = new UserProfile("Parent", true);}
                        else {newProfile = new UserProfile("Parent", false);}
                        break;
                    default:
                        throw new Exception("Invalid");
                }
            }

            // UPDATE THE PROFILE OBJECT ARRAY
            if (Main.profiles == null) {
                Main.profiles = new UserProfile[1];
                Main.profiles[0] = newProfile;
            }
            else {
                UserProfile[] temp = new UserProfile[Main.numberOfProfiles + 1];
                for (int i = 0; i < Main.profiles.length; i++) {
                    temp[i] = Main.profiles[i];
                }
                temp[temp.length - 1] = newProfile;
                Main.profiles = temp;
            }
            Main.numberOfProfiles++;

            // generate a hyperlink for the newly-created profile and add it to the static array of profile hyperlinks
            if (Main.profileLinks == null) {
                Main.profileLinks = new Hyperlink[1];
                Main.profileLinks[0] = generateProfileHyperlink(newProfile);
            }
            else {
                int index = 0;
                Hyperlink[] templink = new Hyperlink[Main.profileLinks.length+1];
                for (int up = 0; up < Main.profileLinks.length; up++) {
                    templink[index++] = Main.profileLinks[up];
                }
                templink[templink.length-1] = generateProfileHyperlink(newProfile);
                Main.profileLinks = templink;
            }

            //Main.transformProfileSelectionPageScene(); // will update "profileSelection"
            Main.profileBox.setScene(Main.profileScene);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void editProfile(UserProfile profile, Hyperlink hyperlink, Hyperlink editLink) {

        editLink.setDisable(true);

        for (int prof = 0; prof < Main.profiles.length; prof++) {
            if (Main.profiles[prof].getProfileID()==profile.getProfileID()) {

                Button cancel; Button accept;
                Label editPromptLabel; TextField editTextField;
                cancel = new Button("Cancel");
                cancel.setId("cancelEditButton");

                editPromptLabel = new Label("Enter the new\ntype of Profile:");
                editPromptLabel.setId("editProfileLabel");
                editPromptLabel.setTranslateX(30); editPromptLabel.setTranslateY(70);
                Main.profileSelection.getChildren().add(editPromptLabel);

                editTextField = new TextField(); editTextField.setPromptText("P,C,G,or S...");
                editTextField.setPrefWidth(85); editTextField.setTranslateX(30);
                editTextField.setTranslateY(110); editTextField.setId("editProfileTextField");
                Main.profileSelection.getChildren().add(editTextField);

                RadioButton editAdmin = new RadioButton("Set Admin");
                editAdmin.setId("editAdminButton");
                if (profile.isAdmin()) {editAdmin.setSelected(true);}

                editAdmin.setTranslateX(30); editAdmin.setTranslateY(140);
                Main.profileSelection.getChildren().add(editAdmin);

                accept = new Button("Modify");
                accept.setId("acceptEditProfileButton");
                accept.setTranslateX(30); accept.setTranslateY(170);
                int finalProf = prof;
                accept.setOnAction(e->{
                    String updatedType = "";
                    try {
                        if (!(editTextField.getText().length() == 1)) {
                            throw new Exception("Invalid");
                        }
                        else {
                            switch (editTextField.getText().charAt(0)) {
                                case 'S':
                                case 's':
                                    updatedType = "Stranger";
                                    if (editAdmin.isSelected()) {profile.setAdmin(true);updatedType+=" [A]";}
                                    else {profile.setAdmin(false);}
                                    Main.profiles[finalProf].setType(updatedType);
                                    break;
                                case 'G':
                                case 'g':
                                    updatedType = "Guest";
                                    if (editAdmin.isSelected()) {profile.setAdmin(true);updatedType+=" [A]";}
                                    else {profile.setAdmin(false);}
                                    Main.profiles[finalProf].setType(updatedType);
                                    break;
                                case 'C':
                                case 'c':
                                    updatedType = "Child";
                                    if (editAdmin.isSelected()) {profile.setAdmin(true);updatedType+=" [A]";}
                                    else {profile.setAdmin(false);}
                                    Main.profiles[finalProf].setType(updatedType);
                                    break;
                                case 'P':
                                case 'p':
                                    updatedType = "Parent";
                                    if (editAdmin.isSelected()) {profile.setAdmin(true);updatedType+=" [A]";}
                                    else {profile.setAdmin(false);}
                                    Main.profiles[finalProf].setType(updatedType);
                                    break;
                                default:
                                    throw new Exception("Invalid");
                            }
                        }

                        // EDIT THE HYPERLINK ARRAY
                        for (int hyper = 0; hyper < Main.profileLinks.length; hyper++) {
                            if (Main.profileLinks[hyper]==hyperlink) {
                                Main.profileLinks[hyper].setText(updatedType);
                                break;
                            }
                        }
                        editLink.setDisable(false);
                        Main.profileSelection.getChildren().removeAll(cancel, accept, editPromptLabel, editTextField, editAdmin);
                        //Main.transformProfileSelectionPageScene();
                        Main.profileBox.setScene(Main.profileScene);
                    }
                    catch(Exception ex){
                        System.out.println(ex.getMessage());
                    }
                });
                Main.profileSelection.getChildren().add(accept);
                cancel.setTranslateX(30); cancel.setTranslateY(200);
                cancel.setOnAction(event -> {Main.profileSelection.getChildren().removeAll(cancel, accept, editAdmin, editPromptLabel, editTextField);
                    editLink.setDisable(false);
                });
                Main.profileSelection.getChildren().add(cancel);
                break;
            }
        }
    }

    public static void deleteProfile(UserProfile UP, Hyperlink hyperlink) {

        if (!UP.isLoggedIn()) {
            Main.main_stage.setTitle("Smart Home Simulator");


            try{Main.householdLocations[UP.getCurrentLocation().getRoomID() - 1].setNumberOfPeopleInside(
                    Main.householdLocations[UP.getCurrentLocation().getRoomID() - 1].getNumberOfPeopleInside() - 1);}catch (Exception e){}

            for (int a = 0; a < Main.editContextLayout.getChildren().size(); a++) {
                try {
                    if (Main.editContextLayout.getChildren().get(a).getId().equals("numOfPeopleInRoom" + UP.getCurrentLocation().getRoomID())) {
                        Label label = (Label) Main.editContextLayout.getChildren().get(a);
                        label.setText("# of people: " + UP.getCurrentLocation().getNumberOfPeopleInside());
                        Main.editContextLayout.getChildren().set(a, label);
                    }
                } catch (Exception e) {
                }
            }
            UP.setCurrentLocation(null);


            // UPDATE THE HYPERLINK ARRAY
            Hyperlink[] temp = new Hyperlink[Main.profileLinks.length - 1];

            for (int hyper = 0; hyper < Main.profileLinks.length; hyper++) {
                if (Main.profileLinks[hyper] == hyperlink) {
                    Main.profileLinks[hyper] = null;

                    int index = 0;
                    for (int h2 = 0; h2 < Main.profileLinks.length; h2++) {
                        if (Main.profileLinks[h2] != null) {
                            temp[index++] = Main.profileLinks[h2];
                        }
                    }
                    break;
                }
            }
            Main.profileLinks = temp;

            // UPDATE THE PROFILE OBJECT ARRAY
            for (int prof = 0; prof < Main.profiles.length; prof++) {
                if (Main.profiles[prof].getProfileID() == UP.getProfileID()) {
                    Main.profiles[prof] = null;
                    break;
                }
            }

            UserProfile[] temp2 = new UserProfile[Main.numberOfProfiles - 1];
            int tempIndex = 0;
            for (int i = 0; i < Main.profiles.length; i++) {
                if (Main.profiles[i] != null) {
                    temp2[tempIndex++] = Main.profiles[i];
                }
            }
            Main.profiles = temp2;

            for (int p = 0; p < Main.profiles.length; p++) {
                Main.profiles[p].setProfileID(p + 1);
            }
            UserProfile.setstaticProfileId(UserProfile.getstaticProfileId() - 1);

            Main.profileSelection.getChildren().removeAll(hyperlink);
            Main.numberOfProfiles--;
            Main.profileBox.setScene(Main.profileScene);
        }
        else {
            System.out.println("cannot delete if logged in!");
        }
    }

    public static Hyperlink generateProfileHyperlink(UserProfile userProfile) {

        int translateX = Main.LOGINPAGE_HEIGHT/2;

        Hyperlink hyperlink = new Hyperlink();
        hyperlink.setId("hyperlinkForProfile"+userProfile.getProfileID());
        hyperlink.setTranslateX(translateX);
        hyperlink.setTranslateY(pixelY);
        pixelY += 20;
        String hyperText = "{{"+userProfile.getType()+"}}";
        if (userProfile.isAdmin()){hyperText+=" [A]";}

        hyperlink.setText(hyperText);

        hyperlink.setOnAction(e-> {

            if (userProfile.getNumberOfTimesHyperlinkClicked()==0) {
                Hyperlink editLink = new Hyperlink(); editLink.setText("[Edit]");
                editLink.setId("editLinkForProfile"+userProfile.getProfileID());
                editLink.setTranslateX((Main.LOGINPAGE_HEIGHT/2)+175); editLink.setTranslateY(hyperlink.getTranslateY());
                editLink.setOnAction(act -> {
                    if (!userProfile.isLoggedIn()) {
                        Controller.editProfile(userProfile, hyperlink, editLink);
                    }
                });

                Hyperlink loginLink = new Hyperlink(); loginLink.setText("[Login]");
                loginLink.setId("loginLinkForProfile"+userProfile.getProfileID());
                loginLink.setTranslateX((Main.LOGINPAGE_HEIGHT/2)+225); loginLink.setTranslateY(hyperlink.getTranslateY());
                loginLink.setOnAction(act -> {
                    if (Main.currentActiveProfile==null) {
                        Controller.goToMainDashboardScene(userProfile, loginLink);
                        Main.profileBox.close();

                        for (int a = 0; a < Main.profileSelection.getChildren().size(); a++) {
                            if (Main.profileSelection.getChildren().get(a).getId().contains("loginLinkForProfile")) {
                                Hyperlink hp = (Hyperlink) Main.profileSelection.getChildren().get(a);
                                hp.setDisable(true);
                                Main.profileSelection.getChildren().set(a,hp);
                            }
                        }

                    }
                    else {
                        Label errorLabel = new Label("Please wait until\nProfile #"+Main.currentActiveProfile.getProfileID() + " "+
                                Main.currentActiveProfile.getType()+ "\nis logged out."); errorLabel.setId("loginErrorLabel");
                        errorLabel.setTranslateX(30); errorLabel.setTranslateY(250);
                        Main.profileSelection.getChildren().add(errorLabel);
                        try {Thread.sleep(3000);}catch (Exception ex){}
                        Main.profileSelection.getChildren().remove(errorLabel);
                    }
                });

                Hyperlink deleteLink = new Hyperlink(); deleteLink.setText("[Delete]");
                deleteLink.setId("deleteLinkForProfile"+userProfile.getProfileID());
                deleteLink.setTranslateX((Main.LOGINPAGE_HEIGHT/2)+100); deleteLink.setTranslateY(hyperlink.getTranslateY());
                deleteLink.setOnAction(act -> {

                    if (!userProfile.isLoggedIn())
                    {
                        Main.profileSelection.getChildren().removeAll(editLink, loginLink, deleteLink);
                        Controller.deleteProfile(userProfile,hyperlink);
                        userProfile.setNumberOfTimesHyperlinkClicked(0);
                    }
                });

                Main.profileSelection.getChildren().addAll(deleteLink, editLink);
                Main.profileSelection.getChildren().addAll(loginLink);
                if (Main.currentActiveProfile != null) {
                    for (int a = 0; a < Main.profileSelection.getChildren().size(); a++) {
                        if (Main.profileSelection.getChildren().get(a).getId().equals("loginLinkForProfile" + userProfile.getProfileID())) {
                            Hyperlink hp = (Hyperlink) Main.profileSelection.getChildren().get(a);
                            hp.setDisable(true);
                        }
                    }
                }
                else {
                    for (int a = 0; a < Main.profileSelection.getChildren().size(); a++) {
                        if (Main.profileSelection.getChildren().get(a).getId().equals("loginLinkForProfile" + userProfile.getProfileID())) {
                            Hyperlink hp = (Hyperlink) Main.profileSelection.getChildren().get(a);
                            hp.setDisable(false);
                        }
                    }
                }
            }

            userProfile.setNumberOfTimesHyperlinkClicked(userProfile.getNumberOfTimesHyperlinkClicked()+1);

        });

        Main.profileSelection.getChildren().add(hyperlink);

        return hyperlink;
    }

    public static void changeLocation(Room newRoom) {
        try {
            for (int prof = 0; prof < Main.profiles.length; prof++) {
                try {
                    if (Main.profiles[prof].getProfileID() == Main.currentActiveProfile.getProfileID()) {
                        Main.profiles[prof].setCurrentLocation(newRoom);
                        Main.currentLocation = newRoom;
                        break;
                    }
                } catch (Exception e) {
                }
            }
        }catch(Exception exc){}
    }
}
