package sample;
import house.*;
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
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import sample.Main.*;

public class Controller {

    private static int pixelY = 70;
    private static int numberOfTimesProfileHyperlinkClicked = 0;

    /**SCENE-SWITCHING METHODS START */
    public static void goToMainDashboardScene(UserProfile userProfile) {
        for (int prof = 0; prof < Main.profiles.length; prof++) {
            if (Main.profiles[prof].getProfileID()==userProfile.getProfileID()) {
                Main.profiles[prof].setLoggedIn(true);
                Main.currentLocation = null;
                break;
            }
        }
        String stageTitle = "Smart Home Simulator -- logged in as \""+userProfile.getType().toUpperCase()+"\"";
        if (userProfile.isAdmin()) {stageTitle+=" (ADMIN)";}
        Main.main_stage.setTitle(stageTitle);

        if (Main.currentActiveProfile != null) {
            Main.currentActiveProfile.setLoggedIn(false);
        }

        Main.currentActiveProfile = userProfile;
        Main.main_stage.setScene(Main.dashboardScene);
    }

    public static void returnToProfileSelectionPage() {
        Main.profileBox = new Stage();
        Main.profileBox.setResizable(false);
        Main.profileBox.initModality(Modality.APPLICATION_MODAL);
        Main.profileBox.setTitle("Manage Profiles");
        Main.profileBox.setWidth(600);
        Main.profileBox.setHeight(600);
        Main.transformProfileSelectionPageScene();
        Main.profileBox.setScene(Main.profileScene);
        Main.profileBox.showAndWait();
    }
    /**SCENE-SWITCHING METHODS END */

    /**DASHBOARD METHODS*/
    public static void startSimulation(ToggleButton t, Button b, TextArea ta, TabPane tab) {
        if (!t.isSelected()) { t.setText("ON"); ta.setDisable(true); b.setDisable(true); tab.setDisable(true); Main.simulationIsOn = false; }
        else { t.setText("OFF"); b.setDisable(false); ta.setDisable(false); tab.setDisable(false); Main.simulationIsOn = true; }
    }

    public static void editContext() {
        Main.editContextStage = new Stage(); Main.editContextStage.setResizable(false);
        Main.editContextStage.initModality(Modality.APPLICATION_MODAL); Main.editContextStage.setTitle("Edit Simulation Context");
        Main.editContextStage.setWidth(650); Main.editContextStage.setHeight(650);
        generateEditContextScene();
        Main.editContextStage.setScene(Main.editContextScene);
        Main.editContextStage.showAndWait();
    }

    public static void generateEditContextScene() {

            DatePicker datePicker = new DatePicker(); datePicker.setPromptText("select date...");
            datePicker.setTranslateX(80); datePicker.setTranslateY(30);

            Label datePickerLabel = new Label("Set date:"); datePickerLabel.setTranslateX(20);
            datePickerLabel.setTranslateY(30);

            Label timePickerLabel = new Label("Set time:"); timePickerLabel.setTranslateX(20);
            timePickerLabel.setTranslateY(60);

            TextField hourField = new TextField(); hourField.setPrefHeight(30); hourField.setPrefWidth(60);
            hourField.setTranslateX(100); hourField.setTranslateY(60); hourField.setPromptText("00-23");

            TextField minuteField = new TextField(); minuteField.setPrefHeight(30); minuteField.setPrefWidth(60);
            minuteField.setTranslateX(180); minuteField.setTranslateY(60); minuteField.setPromptText("00-59");

            Button confirmDTbutton = new Button("Confirm\nDate &\nTime"); confirmDTbutton.setTranslateX(300);
            confirmDTbutton.setTranslateY(30);

            Main.editContextLayout.getChildren().addAll(datePicker, datePickerLabel, timePickerLabel, hourField,
                    minuteField, confirmDTbutton);

            Line line1 = new Line(); line1.setStartX(0); line1.setEndX(650); line1.setTranslateY(100);
            Main.editContextLayout.getChildren().add(line1);

            Line line2 = new Line(); line2.setStartY(0); line2.setEndY(100);
            line2.setTranslateX(400); Main.editContextLayout.getChildren().add(line2);

            Label temperatureLabel = new Label("Modify\nOutdoor Temperature"); temperatureLabel.setTranslateX(500);
            temperatureLabel.setTranslateY(20); Main.editContextLayout.getChildren().add(temperatureLabel);

            TextField tempText = new TextField(); tempText.setPrefHeight(30); tempText.setPrefWidth(60);
            tempText.setTranslateX(450); tempText.setTranslateY(60); tempText.setPromptText("Temp");
            Main.editContextLayout.getChildren().add(tempText);

            Button tempButton = new Button("Confirm"); tempButton.setTranslateX(540); tempButton.setTranslateY(60);
            Main.editContextLayout.getChildren().add(tempButton);

            Label roomsLabel = new Label("Click on a room you would like to move to, or change a room's number of occupants");
            roomsLabel.setTranslateY(105); Main.editContextLayout.getChildren().add(roomsLabel);

            int transY = 120;
            for (int r = 0; r < Main.householdLocations.length; r++) {
                Label l1 = new Label("# of people: " + Main.householdLocations[r].getNumberOfPeopleInside());
                l1.setTranslateX(120); l1.setTranslateY(transY+5); l1.setId("roomID_"+Main.householdLocations[r].getRoomID());
                int fr = r;
                Hyperlink hyp = new Hyperlink(Main.householdLocations[r].getName());
                hyp.setTranslateX(20);
                hyp.setTranslateY(transY);
                hyp.setOnAction(e -> {

                    // change the label of the number of people in the origin and destination rooms
                    Main.householdLocations[fr].setNumberOfPeopleInside(Main.householdLocations[fr].getNumberOfPeopleInside() + 1);

                    try {
                        Main.currentLocation.setNumberOfPeopleInside(Main.currentLocation.getNumberOfPeopleInside() - 1);
                    }catch(Exception excep){}


                    for (int j = 0; j < Main.editContextLayout.getChildren().size(); j++) {
                        try {
                            if (Integer.parseInt(Main.editContextLayout.getChildren().get(j).getId().substring(7)) == Main.currentLocation.getRoomID()) {
                                Label label = (Label) Main.editContextLayout.getChildren().get(j);
                                label.setText("# of people: "+Main.currentLocation.getNumberOfPeopleInside());
                                Main.editContextLayout.getChildren().set(j, label);
                                break;
                            }
                        } catch(Exception ex){}
                    }
                    for (int i = 0; i < Main.editContextLayout.getChildren().size(); i++) {
                        try {
                            if (Main.editContextLayout.getChildren().get(i).equals(l1)) {
                                Label label = (Label) Main.editContextLayout.getChildren().get(i);
                                label.setText("# of people: "+Main.householdLocations[fr].getNumberOfPeopleInside());
                                Main.editContextLayout.getChildren().set(i, label);
                                break;
                            }
                        } catch(Exception ex){}
                    }
                    changeLocation(Main.householdLocations[fr]);
                });
                Main.editContextLayout.getChildren().addAll(hyp, l1);
                transY += 20;
            }
            Line line3 = new Line(); line3.setStartX(0); line3.setEndX(650); line3.setTranslateY(350);
            Main.editContextLayout.getChildren().add(line3);

            Label windowsLabel = new Label("Window-Blocking: Check a box " +
                    "(or many) whose windows you would like to prevent from opening\n" +
                    "or closing"); windowsLabel.setTranslateY(360); Main.editContextLayout.getChildren().add(windowsLabel);

            transY = 400;
            int transX = 20;
            for (int r = 0; r < Main.householdLocations.length; r++) {

                /*** TODO: inner for loop has "w < Main.householdLocations[r].getWindowCollection().length" */
                for (int w = 0; w < 5; w++) {
                    CheckBox checkBox = new CheckBox("Window #"+w);
                    checkBox.setTranslateX(transX);
                    checkBox.setTranslateY(transY);
                    Main.editContextLayout.getChildren().add(checkBox);
                    if (w <= 6) {
                        transY += 20;
                    }
                    else {
                        transY = 310;
                        transX = 80;
                    }
                }
            }
            Button closeButton = new Button("Close"); closeButton.setTranslateX(325); closeButton.setTranslateY(600);
            closeButton.setOnAction(e -> Main.editContextStage.close()); Main.editContextLayout.getChildren().add(closeButton);

            Button moreButton = new Button("More..."); moreButton.setTranslateX(400); moreButton.setTranslateY(600);
            moreButton.setOnAction(e -> {
                generateEditContextScene2(); // will design Main.editContextLayout2
                Main.editContextStage.setScene(Main.editContextScene2);
            });
            Main.editContextLayout.getChildren().add(moreButton);
    }

    /**
     * Build the user interface for modifying the locations of house inhabitants.
     */
    public static void generateEditContextScene2() {
        Label movepplLabel = new Label("Select one non-active Profile, and one Room where you would like to place that user");
        movepplLabel.setTranslateY(10); Main.editContextLayout2.getChildren().add(movepplLabel);

        Label profilelistLabel = new Label("PROFILES"); profilelistLabel.setTranslateY(60); profilelistLabel.setTranslateX(100);
        Main.editContextLayout2.getChildren().add(profilelistLabel);

        Label roomlistLabel = new Label("ROOMS"); roomlistLabel.setTranslateY(60); roomlistLabel.setTranslateX(400);
        Main.editContextLayout2.getChildren().add(roomlistLabel);

        int transY = 80;
        CheckBox[] profileCheckboxes = null;
        if (Main.profiles != null) {
            int index = 0;
            profileCheckboxes = new CheckBox[Main.profiles.length];
            for (int p = 0; p < Main.profiles.length; p++) {
                if (!Main.profiles[p].isLoggedIn()) {
                    CheckBox checkBox = new CheckBox(Main.profiles[p].getProfileID()+"--"+Main.profiles[p].getType());
                    checkBox.setTranslateY(transY); checkBox.setTranslateX(100); checkBox.setId("checkboxProfile"+Main.profiles[p].getProfileID());
                    profileCheckboxes[index++] = checkBox;
                    Main.editContextLayout2.getChildren().add(checkBox);

                    Label currentRoomLabel = new Label();
                    currentRoomLabel.setId("currentRoomOfProfile"+Main.profiles[p].getProfileID());
                    if (Main.profiles[p].getCurrentLocation() != null) {
                        currentRoomLabel.setText(Main.profiles[p].getCurrentLocation().getName());
                    }
                    else {
                        currentRoomLabel.setText("Null room");
                    }
                    currentRoomLabel.setTranslateY(transY); currentRoomLabel.setTranslateX(250);
                    Main.editContextLayout2.getChildren().add(currentRoomLabel);
                }
                transY += 20;
            }
        }
        transY = 80;
        CheckBox[] roomCheckboxes = new CheckBox[Main.householdLocations.length];
        for (int r = 0; r < Main.householdLocations.length; r++) {
            CheckBox checkBox = new CheckBox(Main.householdLocations[r].getName());
            checkBox.setTranslateY(transY); checkBox.setTranslateX(400); checkBox.setId("checkboxRoom"+Main.householdLocations[r].getRoomID());
            roomCheckboxes[r] = checkBox;
            Main.editContextLayout2.getChildren().add(checkBox);
            transY += 20;
        }

        Button confirmButton2 = new Button("Relocate Profile"); confirmButton2.setTranslateX(300); confirmButton2.setTranslateY(400);
        CheckBox[] finalProfileCheckboxes = profileCheckboxes;
        confirmButton2.setOnAction(e -> relocateProfile(finalProfileCheckboxes, roomCheckboxes));
        Main.editContextLayout2.getChildren().add(confirmButton2);

        Button closeButton = new Button("Close"); closeButton.setTranslateX(325); closeButton.setTranslateY(600);
        closeButton.setOnAction(e -> Main.editContextStage.close()); Main.editContextLayout2.getChildren().add(closeButton);

        Button gobackButton = new Button("Go Back"); gobackButton.setTranslateX(225); gobackButton.setTranslateY(600);
        gobackButton.setOnAction(e -> Main.editContextStage.setScene(Main.editContextScene));
        Main.editContextLayout2.getChildren().add(gobackButton);
    }

    /**
     * Place house inhabitants (created Profiles) in specific rooms, or outside home
     * @param profileBoxes
     * @param roomBoxes
     */
    public static void relocateProfile(CheckBox[] profileBoxes, CheckBox[] roomBoxes) {

        // find the profile checkbox that was selected
        for (int pcb = 0; pcb < profileBoxes.length; pcb++) { // exactly 1 checkbox in this array must be selected

            if (profileBoxes[pcb].isSelected()) {

                // find the room checkbox that was selected
                for (int rcb = 0; rcb < roomBoxes.length; rcb++) { // exactly 1 checkbox in this array must be selected
                    if (roomBoxes[rcb].isSelected()) {

                        // find the matching profile and room objects
                        for (int L = 0; L < Main.editContextLayout2.getChildren().size(); L++) {

                            if (Main.editContextLayout2.getChildren().get(L).equals(profileBoxes[pcb])) {
                               int selectedProfileID = Integer.parseInt(Main.editContextLayout2.getChildren().get(L).getId().substring
                                        (Main.editContextLayout2.getChildren().get(L).getId().length()-1));

                                CheckBox cb2 = (CheckBox) Main.editContextLayout2.getChildren().get(L);
                                cb2.setSelected(false);
                                Main.editContextLayout2.getChildren().set(L, cb2);

                                for (int M = 0; M < Main.editContextLayout2.getChildren().size(); M++){

                                    if (Main.editContextLayout2.getChildren().get(M).equals(roomBoxes[rcb])) {
                                        int selectedRoomID = Integer.parseInt(Main.editContextLayout2.getChildren().get(M).getId().substring
                                                (Main.editContextLayout2.getChildren().get(M).getId().length()-1));


                                        CheckBox cb1 = (CheckBox) Main.editContextLayout2.getChildren().get(M);
                                        cb1.setSelected(false);
                                        Main.editContextLayout2.getChildren().set(M, cb1);

                                        // do the relocation

                                        for (int up = 0; up < Main.profiles.length; up++) {
                                            if (Main.profiles[up].getProfileID() == selectedProfileID) {

                                                for (int r = 0; r < Main.householdLocations.length; r++) {
                                                    if (Main.householdLocations[r].getRoomID()==selectedRoomID) {
                                                        Main.profiles[up].setCurrentLocation(Main.householdLocations[r]);
                                                        for (int i = 0; i < Main.editContextLayout2.getChildren().size(); i++) {
                                                            try {
                                                                if (Main.editContextLayout2.getChildren().get(i).getId().equals("currentRoomOfProfile" +
                                                                        Main.profiles[up].getProfileID())) {
                                                                    Label label = (Label) Main.editContextLayout2.getChildren().get(i);
                                                                    label.setText(Main.profiles[up].getCurrentLocation().getName());
                                                                    Main.editContextLayout2.getChildren().set(i, label);
                                                                    break;
                                                                }
                                                            } catch(Exception e){}
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
                break;
            }
        }
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

            Main.transformProfileSelectionPageScene(); // will update "profileSelection"
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

                editPromptLabel = new Label("Enter the new\ntype of Profile:");
                editPromptLabel.setTranslateX(30); editPromptLabel.setTranslateY(70);
                Main.profileSelection.getChildren().add(editPromptLabel);

                editTextField = new TextField(); editTextField.setPromptText("P,C,G,or S...");
                editTextField.setPrefWidth(85); editTextField.setTranslateX(30);
                editTextField.setTranslateY(110);
                Main.profileSelection.getChildren().add(editTextField);

                RadioButton editAdmin = new RadioButton("Sed Admin");
                if (profile.isAdmin()) {editAdmin.setSelected(true);}

                editAdmin.setTranslateX(30); editAdmin.setTranslateY(140);
                Main.profileSelection.getChildren().add(editAdmin);

                accept = new Button("Modify");
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
                        Main.transformProfileSelectionPageScene();
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

    /**TODO: Fix the frontend** graphics when the Delete button is used */
    public static void deleteProfile(UserProfile UP, Hyperlink hyperlink) {

        if (UP.isLoggedIn()) {
            Main.main_stage.setTitle("Smart Home Simulator");
        }

        // UPDATE THE HYPERLINK ARRAY
        Hyperlink[] temp = new Hyperlink[Main.profileLinks.length - 1];

        for (int hyper = 0; hyper < Main.profileLinks.length; hyper++) {
            if (Main.profileLinks[hyper]==hyperlink) {
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
            if (Main.profiles[prof].getProfileID()==UP.getProfileID()) {
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

        Main.profileSelection.getChildren().removeAll(hyperlink);
        Main.numberOfProfiles--;
        Main.transformProfileSelectionPageScene();
        Main.profileBox.setScene(Main.profileScene);
    }

    public static Hyperlink generateProfileHyperlink(UserProfile userProfile) {

        // START TO GENERATE A NEW HYPERLINK FOR THE NEW PROFILE

        int translateX = Main.LOGINPAGE_HEIGHT/2;

        Hyperlink hyperlink = new Hyperlink();
        hyperlink.setTranslateX(translateX);
        hyperlink.setTranslateY(pixelY);
        pixelY += 20;
        String hyperText = "{{"+userProfile.getType()+"}}";
        if (userProfile.isAdmin()){hyperText+=" [A]";}

        hyperlink.setText(hyperText);

        hyperlink.setOnAction(e-> {

            if (numberOfTimesProfileHyperlinkClicked == 0) {
                Hyperlink editLink = new Hyperlink(); editLink.setText("[Edit]");
                editLink.setTranslateX((Main.LOGINPAGE_HEIGHT/2)+175); editLink.setTranslateY(hyperlink.getTranslateY());
                editLink.setOnAction(act -> Controller.editProfile(userProfile, hyperlink, editLink));

                Hyperlink loginLink = new Hyperlink(); loginLink.setText("[Login]");
                loginLink.setTranslateX((Main.LOGINPAGE_HEIGHT/2)+225); loginLink.setTranslateY(hyperlink.getTranslateY());
                loginLink.setOnAction(act -> {Controller.goToMainDashboardScene(userProfile); Main.profileBox.close();});

                Main.profileSelection.getChildren().addAll(editLink,loginLink);

                Hyperlink deleteLink = new Hyperlink(); deleteLink.setText("[Delete]");
                deleteLink.setTranslateX((Main.LOGINPAGE_HEIGHT/2)+100); deleteLink.setTranslateY(hyperlink.getTranslateY());
                deleteLink.setOnAction(act -> {
                    numberOfTimesProfileHyperlinkClicked = 0;
                    Main.profileSelection.getChildren().removeAll(editLink, loginLink, deleteLink);

                    // delete the profile object and hyperlink for it
                    Controller.deleteProfile(userProfile,hyperlink);
                });
                Main.profileSelection.getChildren().add(deleteLink);
            }
            numberOfTimesProfileHyperlinkClicked++;
        });
        Main.profileSelection.getChildren().add(hyperlink);
        return hyperlink;
        // FINISH GENERATING A NEW HYPERLINK FOR THE NEW PROFILE
    }

    public static void changeLocation(Room newRoom) {
        for (int prof = 0; prof < Main.profiles.length; prof++) {
            if (Main.profiles[prof].getProfileID()==Main.currentActiveProfile.getProfileID()) {
                Main.profiles[prof].setCurrentLocation(newRoom);
                Main.currentLocation = newRoom;
                break;
            }
        }
    }
}
