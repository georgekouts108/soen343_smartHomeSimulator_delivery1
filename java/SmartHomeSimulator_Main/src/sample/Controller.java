package sample;
import house.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import sample.Main.*;

public class Controller {

    protected static int pixelY = 70;
    protected static int numberOfTimesProfileHyperlinkClicked = 0;
    protected static int numberOfTimesEditContextLinkStage1Accessed = 0;
    protected static int numberOfTimesEditContextLinkStage2Accessed = 0;
    protected static int numberOfTimesAwayLightsPanelAccessed = 0;
    protected static int numberOfAddedProfiles = 0;
    protected static CheckBox[] profileCheckboxes;
    protected static CheckBox[] roomCheckboxes;

    //for LOCAL TIME
    /**
     * Display the local date and time in the main dashboard
     * @param dateText
     * @param timeText
     */
    public static void CurrentDate(Label dateText, Label timeText){
        try{
            for(;;){
                Calendar cal = new GregorianCalendar();
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                Platform.runLater(()->dateText.setText(year+"/"+(month+1)+"/"+day));
                int second = cal.get(Calendar.SECOND);
                int minute = cal.get(Calendar.MINUTE);
                int hour = cal.get(Calendar.HOUR);
                Platform.runLater(()->timeText.setText("Time "+hour+":"+(minute)+":"+second));
                Thread.sleep(1000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Set the date and time of the simulation
     * @param datePicker
     * @param dateText
     * @param timeText
     * @param hourField
     * @param minuteField
     */

    //to fix: undo previous thread when new one is created
    public static void CurrentDateSimulation(DatePicker datePicker, Label dateText, Label timeText, TextField hourField, TextField minuteField, float timeSpeed){
        try{
            int second = 0;
            int minute = 0;
            int hour = 0;
            minute = Integer.parseInt(minuteField.getText());
            hour = Integer.parseInt(hourField.getText());
            for(int i = 0;;i++){
                Calendar cal = new GregorianCalendar();
                Platform.runLater(()->dateText.setText(String.valueOf(datePicker.getValue())));
                if (i == 0) {
                    second = 0;
                    int finalSecond = second;
                    int finalHour = hour;
                    int finalMinute = minute;
                    Platform.runLater(()->timeText.setText("Time "+(finalHour)%24+":"+(finalMinute)%60+":"+ finalSecond%60));
                    //should make the sleep alternate depending on speed given.
                    Thread.sleep(1000);
                }
                else
                {
                    second = second + 1;
                    int finalSecond = second;

                    if (finalSecond == 60){
                        minute = minute + 1;
                        second = 0;
                    }
                    int finalMinute = minute;
                    if (finalMinute == 60 && finalSecond == 60){
                        hour = hour + 1;
                        minute = 0;
                    }
                    int finalHour = hour;
                    if (finalHour == 23 && finalMinute == 60 && finalSecond == 60){
                        hour = 0;
                        minute = 0;
                        second = 0;
                    }
                    Platform.runLater(()->timeText.setText("Time "+(finalHour%24)+":"+(finalMinute%60)+":"+ finalSecond%60));
                    Thread.sleep(1000);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Launch or return to the default dashboard.
     * @param userProfile
     * @param hyperlink
     */
    public static void goToMainDashboardScene(UserProfile userProfile, Hyperlink hyperlink) {
        try {
            for (int prof = 0; prof < Main.profiles.length; prof++) {
                if (Main.profiles[prof].getProfileID() == userProfile.getProfileID()) {
                    Main.profiles[prof].setLoggedIn(true);
                    Main.currentActiveProfile = Main.profiles[prof];
                    Main.currentLocation = null;
                    break;
                }
            }

            String stageTitle = "Smart Home Simulator -- logged in as #" +
                    userProfile.getProfileID() + " \"" + userProfile.getType().toUpperCase() + "\"";

            if (Main.isIs_away()) {
                stageTitle+=" {AWAY MODE ON}";
            }
            else {
                stageTitle+=" {AWAY MODE OFF}";
            }

            Main.main_stage.setTitle(stageTitle);

            Hyperlink logoutLink = new Hyperlink("Logout");
            logoutLink.setId("logoutLinkForProfile" + userProfile.getProfileID());
            logoutLink.setTranslateX(hyperlink.getTranslateX() + 60);
            logoutLink.setTranslateY(hyperlink.getTranslateY());

            logoutLink.setOnAction(ACT2 -> {
                if (!Main.simulationIsOn) {
                    Main.profiles[userProfile.getProfileID() - 1].setCurrentLocation(null);
                    Main.profiles[userProfile.getProfileID() - 1].setLoggedIn(false);
                    Main.currentActiveProfile = null;

                    Main.createMainDashboardNecessities();

                    logoutLink.setDisable(true);

                    for (int a = 0; a < Main.profileSelection.getChildren().size(); a++) {
                        if (Main.profileSelection.getChildren().get(a).getId().contains("loginLinkForProfile")) {
                            Hyperlink hp = (Hyperlink) Main.profileSelection.getChildren().get(a);
                            hp.setDisable(false);
                            Main.profileSelection.getChildren().set(a, hp);
                        }
                    }
                    Main.main_stage.setTitle("Smart Home Simulator");
                    Main.profileSelection.getChildren().remove(logoutLink);
                }
            });
            Main.profileSelection.getChildren().add(logoutLink);

            Main.createMainDashboardNecessities();

            Main.main_stage.setScene(Main.dashboardScene);
        }
        catch (Exception e){}
    }

    /**
     * Access the window for managing, adding, editing, and deleting user Profiles.
     */
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

    /**
     * Start the simulation.
     * @param t
     * @param b
     * @param ta
     * @param tab
     */
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

    /**
     * Access the window for editing the context of the simulation.
     */
    public static void editContext() {
        Main.editContextStage = new Stage(); Main.editContextStage.setResizable(false);
        Main.editContextStage.initModality(Modality.APPLICATION_MODAL);
        Main.editContextStage.setTitle("Edit Simulation Context");
        Main.editContextStage.setWidth(650); Main.editContextStage.setHeight(650);
        generateEditContextScene();
        Main.editContextStage.setScene(Main.editContextScene);
        Main.editContextStage.showAndWait();
    }

    /**
     * Create or update the default scene for editing the context of the simulation.
     */
    public static void generateEditContextScene() {
        //try {
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

            //for sim time speed
            Label timeSpeed = new Label("Time speed multiplier: ");
            timeSpeed.setId("timeSpeed");
            timeSpeed.setTranslateX(350);
            timeSpeed.setTranslateY(100);

            TextField timeMultiplier = new TextField("");
            timeMultiplier.setId("timeMultiplier");
            timeMultiplier.setTranslateX(425);
            timeMultiplier.setTranslateY(100);
            timeMultiplier.setPromptText("i.e. 0.1, 2, 100");

            Button speedButton = new Button("Go");
            speedButton.setId("timeSpeed");
            speedButton.setTranslateX(600);
            speedButton.setTranslateY(100);
            speedButton.setOnAction(e -> editTimeSpeed(timeMultiplier.getText()));
            //end sim time speed

            Main.editContextLayout.getChildren().addAll(datePicker, datePickerLabel, timePickerLabel, hourField,
                    minuteField, confirmDTbutton, timeSpeed, timeMultiplier, speedButton);

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
            tempText.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue,
                                    String newValue) {
                    if (!newValue.matches("\\d*")) {
                        tempText.setText(newValue.replaceAll("[^\\d*(\\.)?\\d*$]", ""));
                    }
                }
            });
            tempText.setPrefHeight(30);
            tempText.setPrefWidth(60);
            tempText.setTranslateX(450);
            tempText.setTranslateY(60);
            tempText.setPromptText("Temp");
            Main.editContextLayout.getChildren().add(tempText);

            Button tempButton = new Button("Confirm");
            tempButton.setId("confirmTemperatureButton");
            tempButton.setOnAction(e -> {
                try {
                    for (int i = 0; i < Main.main_dashboard.getChildren().size(); i++) {
                        if (Main.main_dashboard.getChildren().get(i).getId().equals("temp")) {
                            if (!tempText.getCharacters().toString().isEmpty()) {
                                Label label = (Label) Main.main_dashboard.getChildren().get(i);
                                label.setText("Outside Temp.\n" + tempText.getCharacters().toString() + "°C");
                                Main.main_dashboard.getChildren().set(i, label);
                                break;
                            }
                        }
                    }
                } catch (Exception err) {
                    System.out.print("There was an error while modifying the outdoor temperature.");
                }
            });
            tempButton.setTranslateX(540);
            tempButton.setTranslateY(60);
            Main.editContextLayout.getChildren().add(tempButton);

            Label roomsLabel = new Label("Click on a room you would like to move to.");
            roomsLabel.setTranslateY(105);
            roomsLabel.setTranslateX(20);
            roomsLabel.setId("roomsLabel");
            Main.editContextLayout.getChildren().add(roomsLabel);

            Label currentRoom = new Label("Your location: ");
            currentRoom.setId("currentRoomOfLoggedInUser");
            currentRoom.setTranslateY(200);
            currentRoom.setTranslateX(450);
            Main.editContextLayout.getChildren().add(currentRoom);

            int transY = 120;
            for (int r = 0; r < Main.householdLocations.length + 1; r++) {

                if (r != Main.householdLocations.length) {
                    Label l1 = new Label("# of people: " + Main.householdLocations[r].getNumberOfPeopleInside());
                    l1.setId("numOfPeopleInRoom" + Main.householdLocations[r].getRoomID());
                    l1.setTranslateX(120);
                    l1.setTranslateY(transY + 5);
                    int fr = r;
                    Hyperlink hyp = new Hyperlink(Main.householdLocations[r].getName());
                    hyp.setId("hyperlinkForRoom-" + Main.householdLocations[r].getName());
                    hyp.setTranslateX(20);
                    hyp.setTranslateY(transY);
                    int finalR = r;
                    hyp.setOnAction(e -> {

                        // change the label of the number of people in the origin and destination rooms
                        if ((Main.currentActiveProfile != null)) {
                            if (Main.currentLocation == null) {

                                /**TODO*/
                                if (Main.house.areDoorsLocked(Main.householdLocations[fr])) {
                                    appendMessageToConsole("ERROR: Profile #" + Main.currentActiveProfile.getProfileID() +
                                            " tried entering " + Main.householdLocations[fr].getName() + " when locked.");
                                    return;
                                }

                                Main.householdLocations[fr].setNumberOfPeopleInside(Main.householdLocations[fr].getNumberOfPeopleInside() + 1);

                                // put that for loop here
                                for (int i = 0; i < Main.editContextLayout.getChildren().size(); i++) {
                                    try {
                                        if (Main.editContextLayout.getChildren().get(i).getId().equals("numOfPeopleOutside")) {
                                            Label label = (Label) Main.editContextLayout.getChildren().get(i);
                                            int numberOfPeopleInHouse = 0;
                                            int numberOfPeopleOutside;
                                            for (int p = 0; p < Main.householdLocations.length; p++) {
                                                numberOfPeopleInHouse += Main.householdLocations[p].getNumberOfPeopleInside();
                                            }
                                            numberOfPeopleOutside = Main.profiles.length - numberOfPeopleInHouse;

                                            label.setText("# of people: " + numberOfPeopleOutside);
                                            Main.editContextLayout.getChildren().set(i, label);
                                            break;
                                        }
                                    } catch (Exception ex) {
                                    }
                                }
                            } else {
                                if (!(Main.currentLocation == Main.householdLocations[fr])) {

                                    // if the destination room is locked, do not enter
                                    if (Main.house.areDoorsLocked(Main.householdLocations[fr])) {
                                        appendMessageToConsole("ERROR: Profile #"+Main.currentActiveProfile.getProfileID()+
                                                " tried entering "+Main.householdLocations[fr].getName()+" when locked.");
                                        return;
                                    }

                                    Main.householdLocations[fr].setNumberOfPeopleInside(Main.householdLocations[fr].getNumberOfPeopleInside() + 1);
                                    Main.currentLocation.setNumberOfPeopleInside(Main.currentLocation.getNumberOfPeopleInside() - 1);
                                }
                            }
                        }

                        // update the label for the number of people in the new room
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

                        // update the label for the number of people in the old room
                        for (int i = 0; i < Main.editContextLayout.getChildren().size(); i++) {
                            try {
                                if (Main.editContextLayout.getChildren().get(i).getId().equals("numOfPeopleInRoom" + Main.currentLocation.getRoomID())) {
                                    Label label = (Label) Main.editContextLayout.getChildren().get(i);
                                    label.setText("# of people: " + Main.currentLocation.getNumberOfPeopleInside());
                                    Main.editContextLayout.getChildren().set(i, label);
                                    break;
                                }
                            } catch (Exception ex) {}
                        }

                        // if there are 0 people inside the old room, automatically turn off the old room's motion detector
                        // otherwise keep it on
                        if ((Main.currentLocation != null) && (Main.currentLocation.getNumberOfPeopleInside() == 0)) {
                            Main.householdLocations[fr].getMd().setState(false);
                            Main.house.autoTurnOnOffMD(Main.currentLocation, false);
                        }

                        // change the location of the room
                        changeLocation(Main.householdLocations[fr]);

                        // now that the new room's population is at least 1, if
                        // the room is in auto mode, automatically turn on any lights(s) within.
                        // the lights will remain on even when the room is empty again
                        if (Main.householdLocations[fr].getNumberOfPeopleInside() > 0 && Main.householdLocations[fr].getIsAutoModeOn()) {
                            Main.house.autoTurnOnLight(Main.householdLocations[fr]);
                        }

                        // if there are >0 people inside the new room, automatically turn on the new room's motion detector
                        // or keep it off if the room is empty
                        if (Main.householdLocations[fr].getNumberOfPeopleInside() > 0) {
                            Main.householdLocations[fr].getMd().setState(true);
                            Main.house.autoTurnOnOffMD(Main.householdLocations[fr], true);
                        }

                        // update the label in edit context Scene 1 of the current profile's location
                        for (int i = 0; i < Main.editContextLayout.getChildren().size(); i++) {
                            try {
                                if (Main.editContextLayout.getChildren().get(i).getId().equals("currentRoomOfLoggedInUser")) {
                                    Label label = (Label) Main.editContextLayout.getChildren().get(i);
                                    label.setText("Your location:\n" + Main.currentLocation.getName());
                                    Main.editContextLayout.getChildren().set(i, label);
                                    break;
                                }
                            } catch (Exception ex) {
                            }
                        }
                    });
                    Main.editContextLayout.getChildren().addAll(hyp, l1);
                    transY += 20;
                }
                else {
                    int numOfPeopleInHouse = 0;
                    int numOfPeopleOutside;
                    for (int p = 0; p < Main.householdLocations.length; p++) {
                        numOfPeopleInHouse += Main.householdLocations[p].getNumberOfPeopleInside();
                    }
                    numOfPeopleOutside = Main.profiles.length - numOfPeopleInHouse;


                    Label l1 = new Label("# of people: " + numOfPeopleOutside);
                    l1.setId("numOfPeopleOutside");
                    l1.setTranslateX(120);
                    l1.setTranslateY(transY + 5);
                    int fr = r;
                    Hyperlink hyp = new Hyperlink("Outside");
                    hyp.setId("hyperlinkForOutside");
                    hyp.setTranslateX(20);
                    hyp.setTranslateY(transY);
                    int finalR = r;

                    // clicking this hyperlink means you want to go outside
                    hyp.setOnAction(e -> {

                        // change the label of the number of people in the origin and destination rooms
                        if ((Main.currentActiveProfile != null)) {

                            // if the logged in user is outside...
                            if (Main.currentLocation == null) {
                                //Main.householdLocations[fr].setNumberOfPeopleInside(Main.householdLocations[fr].getNumberOfPeopleInside() + 1);
                            } else {
                                Main.currentLocation.setNumberOfPeopleInside(Main.currentLocation.getNumberOfPeopleInside() - 1);
                            }
                        }

                        for (int j = 0; j < Main.editContextLayout.getChildren().size(); j++) {
                            try {
                                if (Main.editContextLayout.getChildren().get(j).getId().equals("numOfPeopleInRoom" + Main.currentLocation.getRoomID())) {
                                    Label label = (Label) Main.editContextLayout.getChildren().get(j);
                                    label.setText("# of people: " + Main.currentLocation.getNumberOfPeopleInside());
                                    Main.editContextLayout.getChildren().set(j, label);
                                    break;
                                }
                            } catch (Exception ex) {
                            }
                        }

                        for (int i = 0; i < Main.editContextLayout.getChildren().size(); i++) {
                            try {
                                if (Main.editContextLayout.getChildren().get(i).getId().equals("numOfPeopleOutside")) {
                                    Label label = (Label) Main.editContextLayout.getChildren().get(i);
                                    int numberOfPeopleInHouse = 0;
                                    int numberOfPeopleOutside;
                                    for (int p = 0; p < Main.householdLocations.length; p++) {
                                        numberOfPeopleInHouse += Main.householdLocations[p].getNumberOfPeopleInside();
                                    }
                                    numberOfPeopleOutside = Main.profiles.length - numberOfPeopleInHouse;

                                    label.setText("# of people: " + numberOfPeopleOutside);
                                    Main.editContextLayout.getChildren().set(i, label);
                                    break;
                                }
                            } catch (Exception ex) {
                            }
                        }

                        // if there are 0 people inside the old room, automatically turn off the old room's motion detector
                        // or keep it on if the room is not empty
                        if ((Main.currentLocation != null) && (Main.currentLocation.getNumberOfPeopleInside() == 0)) {
                            Main.currentLocation.getMd().setState(false);
                            Main.house.autoTurnOnOffMD(Main.currentLocation, false);
                        }

                        changeLocation(null);

                        for (int a = 0; a < Main.editContextLayout2.getChildren().size(); a++) {
                            try {
                                if (Main.editContextLayout2.getChildren().get(a).getId().equals("currentRoomOfProfile" + Main.currentActiveProfile.getProfileID())) {
                                    Label label = (Label) Main.editContextLayout2.getChildren().get(a);
                                    if (Main.currentLocation == null) {
                                        label.setText("Outside");
                                    }
                                    Main.editContextLayout2.getChildren().set(a, label);
                                    break;
                                }
                            } catch (Exception ex) {}
                        }
                        for (int i = 0; i < Main.editContextLayout.getChildren().size(); i++) {
                            try {
                                if (Main.editContextLayout.getChildren().get(i).getId().equals("currentRoomOfLoggedInUser")) {
                                    Label label = (Label) Main.editContextLayout.getChildren().get(i);
                                    label.setText("Your location:\nOutside");
                                    Main.editContextLayout.getChildren().set(i, label);
                                    break;
                                }
                            } catch (Exception ex) {}
                        }
                    });
                    Main.editContextLayout.getChildren().addAll(hyp, l1);
                    transY += 20;
                }
            }
            Line line3 = new Line();
            line3.setId("line3");
            line3.setStartX(0);
            line3.setEndX(650);
            line3.setTranslateY(350);
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
                    CheckBox checkBox = new CheckBox("W#" + Main.householdLocations[r].getWindowCollection()[w].getUtilityID() +
                            " in " + Main.householdLocations[r].getName());
                    checkBox.setTranslateX(transX);
                    checkBox.setTranslateY(transY);
                    int finalR = r;
                    int finalW = w;
                    checkBox.setOnAction(e -> {
                        if (checkBox.isSelected()) {
                            Main.householdLocations[finalR].getWindowCollection()[finalW].setBlocked(true);
                        } else {
                            Main.householdLocations[finalR].getWindowCollection()[finalW].setBlocked(false);
                        }
                    });

                    Main.editContextLayout.getChildren().add(checkBox);
                    if (windowCount % 8 != 0) {
                        transY += 20;
                    } else {
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
     * Create or update the user interface for modifying the locations of house inhabitants.
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
                roomCheckboxes = new CheckBox[Main.householdLocations.length + 1];
                for (int r = 0; r < Main.householdLocations.length + 1; r++) {
                    if (r != Main.householdLocations.length) {
                        CheckBox checkBox = new CheckBox(Main.householdLocations[r].getName());
                        checkBox.setTranslateY(transY3);
                        checkBox.setTranslateX(400);
                        checkBox.setId("checkboxRoom" + Main.householdLocations[r].getRoomID());
                        roomCheckboxes[r] = checkBox;
                        Main.editContextLayout2.getChildren().add(checkBox);
                    }
                    else {
                        CheckBox checkBox = new CheckBox("Outside");
                        checkBox.setTranslateY(transY3);
                        checkBox.setTranslateX(400);
                        checkBox.setId("checkboxRoomOutside");
                        roomCheckboxes[r] = checkBox;
                        Main.editContextLayout2.getChildren().add(checkBox);
                    }
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
                                currentRoomLabel.setText("Outside");
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

                                                int selectedRoomID = 0;
                                                // does the profile want to be relocated outside?
                                                if (!Main.editContextLayout2.getChildren().get(M).getId().equals("checkboxRoomOutside")) {
                                                    selectedRoomID = Integer.parseInt(Main.editContextLayout2.getChildren().get(M).getId().substring
                                                            (Main.editContextLayout2.getChildren().get(M).getId().length() - 1));
                                                }
                                                // do the relocation

                                                for (int up = 0; up < Main.profiles.length; up++) {
                                                    if (Main.profiles[up].getProfileID() == selectedProfileID) {

                                                        Room dummyRoom = Main.profiles[up].getCurrentLocation();

                                                        for (int r = 0; r < Main.householdLocations.length; r++) {

                                                            // if you wanted to move a user outside, from a room inside
                                                            if (selectedRoomID==0) {

                                                                if (Main.profiles[up].getCurrentLocation() != null) {
                                                                    Main.householdLocations[Main.profiles[up].getCurrentLocation().getRoomID() - 1].setNumberOfPeopleInside(
                                                                            Main.householdLocations[Main.profiles[up].getCurrentLocation().getRoomID() - 1].getNumberOfPeopleInside() - 1);

                                                                    // if there are 0 people inside the old room, automatically turn off the old room's motion detector
                                                                    // otherwise keep it on
                                                                    if ((Main.profiles[up].getCurrentLocation() != null) && (Main.profiles[up].getCurrentLocation().getNumberOfPeopleInside() == 0)) {
                                                                        Main.profiles[up].getCurrentLocation().getMd().setState(false);
                                                                        Main.house.autoTurnOnOffMD(Main.profiles[up].getCurrentLocation(), false);
                                                                    }

                                                                    Main.profiles[up].setCurrentLocation(null);

                                                                    // UPDATE THE LABEL FOR THE DESTINATION ROOM, IN SCENE 2
                                                                    for (int i = 0; i < Main.editContextLayout2.getChildren().size(); i++) {
                                                                        try {
                                                                            if (Main.editContextLayout2.getChildren().get(i).getId().equals("currentRoomOfProfile" +
                                                                                    Main.profiles[up].getProfileID())) {
                                                                                Label label = (Label) Main.editContextLayout2.getChildren().get(i);
                                                                                label.setText("Outside");
                                                                                Main.editContextLayout2.getChildren().set(i, label);
                                                                                break;
                                                                            }
                                                                        } catch (Exception e) {}
                                                                    }

                                                                    // UPDATE THE LABEL FOR THE NUMBER OF PEOPLE OUTSIDE, IN SCENE 1
                                                                    for (int a = 0; a < Main.editContextLayout.getChildren().size(); a++) {
                                                                        try {
                                                                            if (Main.editContextLayout.getChildren().get(a).getId().equals("numOfPeopleOutside")) {
                                                                                Label label = (Label) Main.editContextLayout.getChildren().get(a);

                                                                                int numOfPeopleInHouse = 0;
                                                                                int numOfPeopleOutside;
                                                                                for (int p = 0; p < Main.householdLocations.length; p++) {
                                                                                    numOfPeopleInHouse += Main.householdLocations[p].getNumberOfPeopleInside();
                                                                                }
                                                                                numOfPeopleOutside = Main.profiles.length - numOfPeopleInHouse;

                                                                                label.setText("# of people: " + numOfPeopleOutside);
                                                                                Main.editContextLayout.getChildren().set(a, label);
                                                                            }
                                                                        } catch (Exception e) {}
                                                                    }

                                                                    // UPDATE THE LABEL FOR THE NUMBER OF PEOPLE IN THE ORIGIN ROOM, IN SCENE 1
                                                                    for (int a = 0; a < Main.editContextLayout.getChildren().size(); a++) {
                                                                        try {
                                                                            if (Main.editContextLayout.getChildren().get(a).getId().equals("numOfPeopleInRoom" + dummyRoom.getRoomID())) {
                                                                                Label label = (Label) Main.editContextLayout.getChildren().get(a);
                                                                                label.setText("# of people: " + dummyRoom.getNumberOfPeopleInside());
                                                                                Main.editContextLayout.getChildren().set(a, label);
                                                                            }
                                                                        } catch (Exception e) {}
                                                                    }
                                                                }
                                                            }

                                                            else {
                                                                if (Main.householdLocations[r].getRoomID() == selectedRoomID) {

                                                                    // if the destination room is locked, do not enter
                                                                    if (Main.house.areDoorsLocked(Main.householdLocations[r])) {
                                                                        appendMessageToConsole("ERROR: Profile #"+Main.profiles[up].getProfileID()+
                                                                                " tried entering "+Main.householdLocations[r].getName()+" when locked.");
                                                                        return;
                                                                    }

                                                                    dummyRoom = Main.profiles[up].getCurrentLocation();

                                                                    // IS THE USER BEING MOVED TRYING TO GO INTO A ROOM IT'S ALREADY IN?
                                                                    if (Main.profiles[up].getCurrentLocation() == Main.householdLocations[selectedRoomID - 1]) {
                                                                        System.out.println("Profile #" + Main.profiles[up] + " is already in the " + Main.householdLocations[selectedRoomID - 1].getName());
                                                                    }
                                                                    else {
                                                                        // IS THE USER BEING MOVED COMING FROM ANOTHER ROOM INDOORS?
                                                                        if (Main.profiles[up].getCurrentLocation() != null) {

                                                                            Main.householdLocations[Main.profiles[up].getCurrentLocation().getRoomID() - 1].setNumberOfPeopleInside(
                                                                                    Main.householdLocations[Main.profiles[up].getCurrentLocation().getRoomID() - 1].getNumberOfPeopleInside() - 1);

                                                                            // if there are 0 people inside the old room, automatically turn off the old room's motion detector
                                                                            // or keep it on if the old room is not empty
                                                                            if (Main.householdLocations[Main.profiles[up].getCurrentLocation().getRoomID() - 1].getNumberOfPeopleInside() == 0) {
                                                                                Main.householdLocations[Main.profiles[up].getCurrentLocation().getRoomID() - 1].getMd().setState(false);
                                                                                Main.house.autoTurnOnOffMD(Main.householdLocations[Main.profiles[up].getCurrentLocation().getRoomID() - 1], false);
                                                                            }

                                                                            Main.profiles[up].setCurrentLocation(Main.householdLocations[r]); // THE CHANGE OF ROOM***
                                                                            Main.householdLocations[r].setNumberOfPeopleInside(Main.householdLocations[r].getNumberOfPeopleInside() + 1);

                                                                            // if there are > 0 people inside the new room, automatically turn on the new room's motion detector
                                                                            // or keep it off if the new room is empty
                                                                            if (Main.householdLocations[r].getNumberOfPeopleInside() > 0) {
                                                                                Main.householdLocations[r].getMd().setState(true);
                                                                                Main.house.autoTurnOnOffMD(Main.householdLocations[r], true);
                                                                            }

                                                                            // if the destination room has Auto mode turned on, automatically turn on the lights inside that room
                                                                            if (Main.householdLocations[r].getNumberOfPeopleInside()>0 && Main.householdLocations[r].getIsAutoModeOn()) {
                                                                                Main.house.autoTurnOnLight(Main.householdLocations[r]);
                                                                            }

                                                                            //update the label in SCENE 2 for the room location of the profile that was just moved.
                                                                            for (int i = 0; i < Main.editContextLayout2.getChildren().size(); i++) {
                                                                                try {
                                                                                    if (Main.editContextLayout2.getChildren().get(i).getId().equals("currentRoomOfProfile" +
                                                                                            Main.profiles[up].getProfileID())) {
                                                                                        Label label = (Label) Main.editContextLayout2.getChildren().get(i);
                                                                                        label.setText(Main.profiles[up].getCurrentLocation().getName());
                                                                                        Main.editContextLayout2.getChildren().set(i, label);
                                                                                        break;
                                                                                    }
                                                                                } catch (Exception e) {}
                                                                            }

                                                                            //update the label in SCENE 1 for number of people in the destination room location of the profile that was just moved.
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

                                                                            //update the label in SCENE 1 for number of people in the origin room location of the profile that was just moved.
                                                                            for (int a = 0; a < Main.editContextLayout.getChildren().size(); a++) {
                                                                                try {
                                                                                    if (Main.editContextLayout.getChildren().get(a).getId().equals("numOfPeopleInRoom" + dummyRoom.getRoomID())) {
                                                                                        Label label = (Label) Main.editContextLayout.getChildren().get(a);
                                                                                        label.setText("# of people: " + dummyRoom.getNumberOfPeopleInside());
                                                                                        Main.editContextLayout.getChildren().set(a, label);
                                                                                    }
                                                                                } catch (Exception e) {}
                                                                            }
                                                                        }

                                                                        // OR IS THE USER BEING MOVED COMING FROM OUTSIDE?
                                                                        else {
                                                                            Main.profiles[up].setCurrentLocation(Main.householdLocations[r]); // THE CHANGE OF ROOM***
                                                                            Main.householdLocations[r].setNumberOfPeopleInside(Main.householdLocations[r].getNumberOfPeopleInside() + 1);

                                                                            // if there are > 0 people inside the new room, automatically turn on the new room's motion detector
                                                                            // or keep it off if the new room is empty
                                                                            if (Main.householdLocations[r].getNumberOfPeopleInside() > 0) {
                                                                                Main.householdLocations[r].getMd().setState(true);
                                                                                Main.house.autoTurnOnOffMD(Main.householdLocations[r], true);
                                                                            }

                                                                            // if the destination room has Auto mode turned on, automatically turn on the lights inside that room
                                                                            if (Main.householdLocations[r].getNumberOfPeopleInside()>0 && Main.householdLocations[r].getIsAutoModeOn()) {
                                                                                Main.house.autoTurnOnLight(Main.householdLocations[r]);
                                                                            }

                                                                            // update the label in SCENE 1 of the number of people who are in the destination room
                                                                            for (int a = 0; a < Main.editContextLayout.getChildren().size(); a++) {
                                                                                try {
                                                                                    if (Main.editContextLayout.getChildren().get(a).getId().equals("numOfPeopleInRoom" + Main.householdLocations[r].getRoomID())) {
                                                                                        Label label = (Label) Main.editContextLayout.getChildren().get(a);
                                                                                        label.setText("# of people: " + Main.householdLocations[r].getNumberOfPeopleInside());
                                                                                        Main.editContextLayout.getChildren().set(a, label);
                                                                                    }
                                                                                } catch (Exception e) { }
                                                                            }

                                                                            // update the label in SCENE 1 of the number of people who are outside (the "origin" room)
                                                                            for (int a = 0; a < Main.editContextLayout.getChildren().size(); a++) {
                                                                                try {
                                                                                    if (Main.editContextLayout.getChildren().get(a).getId().equals("numOfPeopleOutside")) {
                                                                                        Label label = (Label) Main.editContextLayout.getChildren().get(a);

                                                                                        int numOfPeopleInHouse = 0;
                                                                                        int numOfPeopleOutside;
                                                                                        for (int p = 0; p < Main.householdLocations.length; p++) {
                                                                                            numOfPeopleInHouse += Main.householdLocations[p].getNumberOfPeopleInside();
                                                                                        }
                                                                                        numOfPeopleOutside = Main.profiles.length - numOfPeopleInHouse;

                                                                                        label.setText("# of people: " + numOfPeopleOutside);
                                                                                        Main.editContextLayout.getChildren().set(a, label);
                                                                                    }
                                                                                } catch (Exception e) {}
                                                                            }

                                                                            //update the label in SCENE 2 for the room location of the profile that was just moved.
                                                                            for (int i = 0; i < Main.editContextLayout2.getChildren().size(); i++) {
                                                                                try {
                                                                                    if (Main.editContextLayout2.getChildren().get(i).getId().equals("currentRoomOfProfile" +
                                                                                            Main.profiles[up].getProfileID())) {
                                                                                        Label label = (Label) Main.editContextLayout2.getChildren().get(i);
                                                                                        label.setText(Main.profiles[up].getCurrentLocation().getName());
                                                                                        Main.editContextLayout2.getChildren().set(i, label);
                                                                                        break;
                                                                                    }
                                                                                } catch (Exception e) {}
                                                                            }
                                                                        }
                                                                    }
                                                                    break;
                                                                }
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

    /**
     * Create a new user Profile
     * @param textField
     *
     */
    public static void createNewProfile(TextField textField) {
        UserProfile newProfile;
        try {
            if (!(textField.getText().length() == 1)) {
                throw new Exception("Invalid");
            }
            else {
                switch (textField.getText().charAt(0)) {
                    case 'S':
                    case 's':
                        newProfile = new UserProfile("Stranger");
                        break;
                    case 'G':
                    case 'g':
                        newProfile = new UserProfile("Guest");
                        break;
                    case 'C':
                    case 'c':
                        newProfile = new UserProfile("Child");
                        break;
                    case 'P':
                    case 'p':
                        newProfile = new UserProfile("Parent");
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
            Main.profileBox.setScene(Main.profileScene);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Edit an existing user Profile
     * @param profile
     * @param hyperlink
     * @param editLink
     */
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
                                    Main.profiles[finalProf].setType(updatedType);
                                    break;
                                case 'G':
                                case 'g':
                                    updatedType = "Guest";
                                    Main.profiles[finalProf].setType(updatedType);
                                    break;
                                case 'C':
                                case 'c':
                                    updatedType = "Child";
                                    Main.profiles[finalProf].setType(updatedType);
                                    break;
                                case 'P':
                                case 'p':
                                    updatedType = "Parent";
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
                        Main.profileSelection.getChildren().removeAll(cancel, accept, editPromptLabel, editTextField);
                        Main.profileBox.setScene(Main.profileScene);
                    }
                    catch(Exception ex){
                        System.out.println(ex.getMessage());
                    }
                });
                Main.profileSelection.getChildren().add(accept);
                cancel.setTranslateX(30); cancel.setTranslateY(200);
                cancel.setOnAction(event -> {Main.profileSelection.getChildren().removeAll(cancel, accept, editPromptLabel, editTextField);
                    editLink.setDisable(false);
                });
                Main.profileSelection.getChildren().add(cancel);
                break;
            }
        }
    }

    /**
     * Delete an existing user profile.
     * @param UP
     * @param hyperlink
     */
    public static void deleteProfile(UserProfile UP, Hyperlink hyperlink) {
        try {
            if (!UP.isLoggedIn()) {
                Main.main_stage.setTitle("Smart Home Simulator");

                try {
                    Main.householdLocations[UP.getCurrentLocation().getRoomID() - 1].setNumberOfPeopleInside(
                            Main.householdLocations[UP.getCurrentLocation().getRoomID() - 1].getNumberOfPeopleInside() - 1);
                } catch (Exception e) {
                }

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
            } else {
                System.out.println("cannot delete if logged in!");
            }
        }catch (Exception e){}
    }

    /**
     * Upon creating a new Profile, create a hyperlink that will present the options to edit, delete, or login/logout.
     * @param userProfile
     * @return
     */
    public static Hyperlink generateProfileHyperlink(UserProfile userProfile) {

        int translateX = Main.LOGINPAGE_HEIGHT/2;

        Hyperlink hyperlink = new Hyperlink();
        hyperlink.setId("hyperlinkForProfile"+userProfile.getProfileID());
        hyperlink.setTranslateX(translateX);
        hyperlink.setTranslateY(pixelY);
        pixelY += 20;
        String hyperText = "{{"+userProfile.getType()+"}}";

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

    /**
     * Alter the room location of a user Profile
     * @param newRoom
     */
    public static void changeLocation(Room newRoom) {
        try {
            for (int prof = 0; prof < Main.profiles.length; prof++) {
                try {
                    if (Main.profiles[prof].getProfileID() == Main.currentActiveProfile.getProfileID()) {

                        // when you relocate a user into the house from outside, AWAY mode is turned off
                        if ((newRoom != null) && Main.isIs_away()) {
                                for (int s = 0; s < Main.SHP_MODULE.getChildren().size(); s++) {
                                    try {
                                        if (Main.SHP_MODULE.getChildren().get(s).getId().equals("setAwayModeButton")) {
                                            ToggleButton toggleButton = (ToggleButton) Main.SHP_MODULE.getChildren().get(s);
                                            toggleButton.setSelected(true);
                                            toggleButton.fire();

                                            toggleButton.setSelected(false);
                                            toggleButton.setText("Turn on AWAY mode");

                                            Main.SHP_MODULE.getChildren().set(s, toggleButton);

                                            TabPane tabPane3 = (TabPane) Main.main_dashboard.getChildren().get(8);
                                            Tab innerTab3 = tabPane3.getTabs().get(2);
                                            innerTab3.setContent(Main.SHP_MODULE);
                                            tabPane3.getTabs().set(2, innerTab3);
                                            Main.main_dashboard.getChildren().set(8, tabPane3);
                                            break;
                                        }
                                    } catch (Exception e){}
                                }

                                Main.currentActiveProfile.setAway(false);

                                // unlock all doors
                                for (int room = 0; room < Main.householdLocations.length; room++) {
                                    try {
                                        for (int door = 0; door < Main.householdLocations[room].getDoorCollection().length; door++) {
                                            try {
                                                Main.householdLocations[room].getDoorCollection()[door].setLocked(false);
                                                appendMessageToConsole("Door #" +
                                                        Main.householdLocations[room].getWindowCollection()[door].getUtilityID() + " to " +
                                                        Main.householdLocations[room].getName() + " unlocked by SHP module");
                                            } catch (Exception e) {
                                            }
                                        }
                                    }catch (Exception E){}
                                }

                                // unlock all "locked" lights, so they may be turned off whenever as usual
                                for (int room = 0; room < Main.householdLocations.length; room++) {
                                    try {
                                        for (int light = 0; light < Main.householdLocations[room].getLightCollection().length; light++) {
                                            try {
                                                for (int i = 0; i < Main.SHP_LightsConfigAWAYmode.getChildren().size(); i++) {
                                                    try {
                                                        if (Main.SHP_LightsConfigAWAYmode.getChildren().get(i).getId().equals("awayModeLight#" +
                                                                Main.householdLocations[room].getLightCollection()[light].getUtilityID())) {
                                                            CheckBox checkBox = (CheckBox) Main.SHP_LightsConfigAWAYmode.getChildren().get(i);
                                                            if (checkBox.isSelected()) {
                                                                Main.householdLocations[room].getLightCollection()[light].setLocked(false);
                                                                appendMessageToConsole("Light #" +
                                                                        Main.householdLocations[room].getLightCollection()[light].getUtilityID() + " in room " +
                                                                        Main.householdLocations[room].getName() + " unlocked by SHP module");
                                                            }
                                                        }
                                                    } catch (Exception e) { }
                                                }
                                            } catch (Exception e) { }
                                        }
                                    }catch (Exception e){}
                                }

                        }
                        Main.profiles[prof].setCurrentLocation(newRoom);
                        Main.currentLocation = newRoom;
                        break;
                    }
                } catch (Exception e) {}
            }
        }catch(Exception exc){}
    }

    /**todo: SHP MODULE METHODS*/

    public static void toggleAwayButton(ToggleButton tb) {
        try {
            // make sure all existing users, logged in or not, are outside the house
            int numOfPeopleOutside = 0;
            for (int u = 0; u < Main.profiles.length; u++) {
                try {
                    if (Main.profiles[u].getCurrentLocation()==null) {
                        numOfPeopleOutside++;
                    }
                }catch (Exception e){}
            }

            /**TODO: in this "if" statement, add an extra condition
             *  that will look at the currentActiveProfile's type; if
             *  the profile type is GUEST or STRANGER, throw an exception
             *  and do NOT turn on away mode. Only Parents and Children can do that.*/

            if ((numOfPeopleOutside == Main.profiles.length)) {

                if (tb.isSelected()) {
                    Main.main_stage.setTitle("Smart Home Simulator -- logged in as #" +
                            Main.currentActiveProfile.getProfileID() + " \"" + Main.currentActiveProfile.getType().toUpperCase() +
                            "\" {AWAY MODE ON}");
                    appendMessageToConsole("AWAY mode set to ON");
                    tb.setText("Turn off AWAY mode");
                    Main.currentActiveProfile.setAway(true);
                    Main.setIs_away(true);

                    // close all windows;
                    for (int room = 0; room < Main.householdLocations.length; room++) {
                        try {
                            for (int win = 0; win < Main.householdLocations[room].getWindowCollection().length; win++) {
                                try {
                                    Main.householdLocations[room].getWindowCollection()[win].setState(false);
                                    appendMessageToConsole("Window #" +
                                            Main.householdLocations[room].getWindowCollection()[win].getUtilityID() + " in " +
                                            Main.householdLocations[room].getName() + " closed by SHP module");
                                }catch (Exception e){}
                            }
                            Main.house.setIconVisibility(Main.householdLocations[room], "Window", false);
                        }catch(Exception e){}
                    }

                    // close and lock all doors
                    for (int room = 0; room < Main.householdLocations.length; room++) {
                        try {
                            for (int door = 0; door < Main.householdLocations[room].getDoorCollection().length; door++) {
                                try {
                                    Main.householdLocations[room].getDoorCollection()[door].setState(false);
                                    appendMessageToConsole("Door #" +
                                            Main.householdLocations[room].getWindowCollection()[door].getUtilityID() + " to " +
                                            Main.householdLocations[room].getName() + " closed by SHP module");
                                    Main.householdLocations[room].getDoorCollection()[door].setLocked(true);
                                    appendMessageToConsole("Door #" +
                                            Main.householdLocations[room].getWindowCollection()[door].getUtilityID() + " to " +
                                            Main.householdLocations[room].getName() + " locked by SHP module");
                                }
                                catch (Exception e){}
                            }
                            Main.house.setIconVisibility(Main.householdLocations[room], "Door", false);
                        }
                        catch (Exception e){}
                    }

                    // turn on a custom selection of lights and keep them "locked" until AWAY mode is off
                    for (int room = 0; room < Main.householdLocations.length; room++) {
                        try {
                            for (int light = 0; light < Main.householdLocations[room].getLightCollection().length; light++) {
                                try {
                                    for (int i = 0; i < Main.SHP_LightsConfigAWAYmode.getChildren().size(); i++) {
                                        try {
                                            if (Main.SHP_LightsConfigAWAYmode.getChildren().get(i).getId().equals("awayModeLight#" +
                                                    Main.householdLocations[room].getLightCollection()[light].getUtilityID())) {
                                                CheckBox checkBox = (CheckBox) Main.SHP_LightsConfigAWAYmode.getChildren().get(i);
                                                if (checkBox.isSelected()) {
                                                    Main.householdLocations[room].getLightCollection()[light].setState(true);
                                                    Main.householdLocations[room].getLightCollection()[light].setLocked(true);
                                                    appendMessageToConsole("Light #" +
                                                            Main.householdLocations[room].getLightCollection()[light].getUtilityID() + " in room " +
                                                            Main.householdLocations[room].getName() + " locked by SHP module");
                                                    Main.house.setIconVisibility(Main.householdLocations[room], "Light", true);
                                                }
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }
                        catch (Exception e){}
                    }
                }
                else {
                    Main.main_stage.setTitle("Smart Home Simulator -- logged in as #" +
                            Main.currentActiveProfile.getProfileID() + " \"" + Main.currentActiveProfile.getType().toUpperCase() +
                            "\" {AWAY MODE OFF}");
                    appendMessageToConsole("AWAY mode set to OFF");
                    tb.setText("Turn on AWAY mode");
                    Main.currentActiveProfile.setAway(false);
                    Main.setIs_away(false);

                    // unlock all doors
                    for (int room = 0; room < Main.householdLocations.length; room++) {
                        try {
                            for (int door = 0; door < Main.householdLocations[room].getDoorCollection().length; door++) {
                                try {
                                    Main.householdLocations[room].getDoorCollection()[door].setLocked(false);
                                    appendMessageToConsole("Door #" +
                                            Main.householdLocations[room].getWindowCollection()[door].getUtilityID() + " to " +
                                            Main.householdLocations[room].getName() + " unlocked by SHP module");
                                } catch (Exception e) {
                                }
                            }
                        }
                        catch (Exception e){}
                    }

                    // unlock all "locked" lights, so they may be turned off whenever as usual
                    for (int room = 0; room < Main.householdLocations.length; room++) {
                        try {
                            for (int light = 0; light < Main.householdLocations[room].getLightCollection().length; light++) {
                                try {
                                    for (int i = 0; i < Main.SHP_LightsConfigAWAYmode.getChildren().size(); i++) {
                                        try {
                                            if (Main.SHP_LightsConfigAWAYmode.getChildren().get(i).getId().equals("awayModeLight#" +
                                                    Main.householdLocations[room].getLightCollection()[light].getUtilityID())) {
                                                CheckBox checkBox = (CheckBox) Main.SHP_LightsConfigAWAYmode.getChildren().get(i);
                                                if (checkBox.isSelected()) {
                                                    Main.householdLocations[room].getLightCollection()[light].setLocked(false);
                                                    appendMessageToConsole("Light #" +
                                                            Main.householdLocations[room].getLightCollection()[light].getUtilityID() + " in room " +
                                                            Main.householdLocations[room].getName() + " unlocked by SHP module");
                                                }
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                } catch (Exception e) { }
                            }
                        }catch (Exception e){}
                    }
                }
            }
            else {
                throw new Exception();
            }
        } catch(Exception e) {
            tb.setSelected(false);
            appendMessageToConsole("ERROR: All profiles must be outside house\nbefore turning AWAY mode on.");
        }
    }

    public static void createAwayLightsPanel() {
        if (numberOfTimesAwayLightsPanelAccessed==0) {
            Label promptLabel = new Label("Select below which lights\nshould remain on during\nAWAY mode");
            promptLabel.setTranslateX(20); promptLabel.setTranslateY(20);
            promptLabel.setTextAlignment(TextAlignment.CENTER);

            int pixel_Y = 80, pixel_X = 20;
            for (int r = 0; r < Main.householdLocations.length; r++) {
                for (int l = 0; l < Main.householdLocations[r].getLightCollection().length; l++) {
                    CheckBox lightBox = new CheckBox("Light #"
                            +Main.householdLocations[r].getLightCollection()[l].getUtilityID()+" in "+
                            Main.householdLocations[r].getName());
                    lightBox.setTranslateX(pixel_X);
                    lightBox.setTranslateY(pixel_Y+=20);
                    lightBox.setId("awayModeLight#"+Main.householdLocations[r].getLightCollection()[l].getUtilityID());

                    int finalR = r;
                    int finalL = l;
                    lightBox.setOnAction(e->{

                        // if you click the checkbox during AWAY mode
                        if (Main.currentActiveProfile.isAway()) {
                            if (lightBox.isSelected()) {
                                lightBox.setSelected(false);
                            }
                            else {
                                lightBox.setSelected(true);
                            }
                        }
                    });

                    Main.SHP_LightsConfigAWAYmode.getChildren().add(lightBox);
                }
            }
            Button closeButton = new Button("Close");
            closeButton.setTranslateX(100); closeButton.setTranslateY(330);
            closeButton.setOnAction(event->Main.awayLightsStage.close());

            Main.SHP_LightsConfigAWAYmode.getChildren().add(promptLabel);
            Main.SHP_LightsConfigAWAYmode.getChildren().add(closeButton);
        }
        numberOfTimesAwayLightsPanelAccessed++;
    }

    public static void configureAwayLights() {
        Main.awayLightsStage = new Stage();
        Main.awayLightsStage.setResizable(false);
        Main.awayLightsStage.setHeight(380);
        Main.awayLightsStage.setWidth(250);
        Main.awayLightsStage.setTitle("AWAY mode - Lights Configuration");
        createAwayLightsPanel();
        Main.awayLightsStage.setScene(Main.SHP_LightsConfigAWAYscene);
        Main.awayLightsStage.showAndWait();
    }
    
    public static void appendMessageToConsole(String message) {
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

    public static void editTimeSpeed(String multiplier){
        System.out.println("Test " + Float.parseFloat(multiplier));
        //grab thread or info that is already shown in sim time, and speed it up...
        //i.e. make use of currentDateSimulation method
    }

}
