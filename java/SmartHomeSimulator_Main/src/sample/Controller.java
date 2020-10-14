package sample;
import house.*;
import javafx.scene.layout.AnchorPane;
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

        Main.main_stage.setTitle("Smart Home Simulator -- logged in as \""+userProfile.getType().toUpperCase()+"\"");
        Main.currentActiveProfile = userProfile;
        Main.main_stage.setScene(Main.dashboardScene);
    }

    public static void returnToProfileSelectionPage() {
        Main.profileBox = new Stage();
        Main.profileBox.initModality(Modality.APPLICATION_MODAL);
        Main.profileBox.setTitle("Manage Profiles");
        Main.profileBox.setWidth(600);
        Main.profileBox.setHeight(600);

        Main.transformProfileSelectionPageScene();

//        Button closeButton = new Button("Close");
//        closeButton.setTranslateX(150); closeButton.setTranslateY(350);
//        closeButton.setOnAction(e->Main.profileBox.close());
//        Main.profileSelection.getChildren().add(closeButton);


        Main.profileBox.setScene(Main.profileScene);
        Main.profileBox.showAndWait();

        //////////////////////


//        for (int prof = 0; prof < Main.profiles.length; prof++) {
//            if (Main.profiles[prof].getProfileID()==Main.currentActiveProfile.getProfileID()) {
//                Main.profiles[prof].setLoggedIn(false);
//                Main.currentLocation = null;
//                break;
//            }
//        }
//
//        Main.currentActiveProfile = null;
//        Main.stage.setScene(Main.profileScene);
    }
    /**SCENE-SWITCHING METHODS END */

    /**DASHBOARD METHODS*/
    public static void startSimulation(ToggleButton t, Button b, TextArea ta, TabPane tab) {

        if (!t.isSelected()) {
            ta.setDisable(true);
            b.setDisable(true);
            tab.setDisable(true);
            Main.simulationIsOn = false;
        }
        else {
            b.setDisable(false);
            ta.setDisable(false);
            tab.setDisable(false);
            Main.simulationIsOn = true;
        }
    }

    /**TODO: implement the frontend and backend for the Edit Context button
     * todo: see the "Context of the simulation" section in the grading scheme for its functions
     * */

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

        String report = translateCurrentDateTime() + " -- " + warning;
        Main.suspBox.appendText(report+"\n");
    }

    /**SHP MODULE METHODS END */



    /**USER PROFILE MANAGEMENT METHODS START HERE*/
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

            Main.transformProfileSelectionPageScene(); // will update "profileSelection"
            Main.profileBox.setScene(Main.profileScene);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    public static void editProfile(UserProfile profile, Hyperlink hyperlink, Hyperlink editLink) {





        editLink.setDisable(true);

//        Hyperlink edit = (Hyperlink) Main.profileSelection.getChildren().get(8); edit.setDisable(true);
//        Main.profileSelection.getChildren().set(8,edit);

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

                accept = new Button("Modify");
                accept.setTranslateX(30); accept.setTranslateY(140);
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
                                    updatedType = "{{Stranger}}";
                                    Main.profiles[finalProf].setType("Stranger");
//
//                                    Hyperlink hy1 = (Hyperlink) Main.profileSelection.getChildren().get(6);
//                                    hy1.setText("{{Stranger}}"); Main.profileSelection.getChildren().set(6,hy1);

                                    break;
                                case 'G':
                                case 'g':
                                    updatedType = "{{Guest}}";
                                    Main.profiles[finalProf].setType(updatedType);

//                                    Hyperlink hy2 = (Hyperlink) Main.profileSelection.getChildren().get(6);
//                                    hy2.setText("{{Guest}}"); Main.profileSelection.getChildren().set(6,hy2);
                                    break;
                                case 'C':
                                case 'c':
                                    updatedType = "{{Child}}";
                                    Main.profiles[finalProf].setType(updatedType);

//                                    Hyperlink hy3 = (Hyperlink) Main.profileSelection.getChildren().get(6);
//                                    hy3.setText("{{Child}}"); Main.profileSelection.getChildren().set(6,hy3);
                                    break;
                                case 'P':
                                case 'p':
                                    updatedType = "{{Parent}}";
                                    Main.profiles[finalProf].setType(updatedType);

//                                    Hyperlink hy4 = (Hyperlink) Main.profileSelection.getChildren().get(6);
//                                    hy4.setText("{{Parent}}"); Main.profileSelection.getChildren().set(6,hy4);
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
//                        Hyperlink edit2 = (Hyperlink) Main.profileSelection.getChildren().get(8); edit2.setDisable(false);
//                        Main.profileSelection.getChildren().set(8,edit2);

                        Main.profileSelection.getChildren().removeAll(cancel, accept, editPromptLabel, editTextField);

                        Main.transformProfileSelectionPageScene();
                        Main.profileBox.setScene(Main.profileScene);
                    }
                    catch(Exception ex){
                        System.out.println(ex.getMessage());
                    }
                });
                Main.profileSelection.getChildren().add(accept);


                cancel.setTranslateX(30); cancel.setTranslateY(170);
                cancel.setOnAction(event -> {Main.profileSelection.getChildren().removeAll(cancel, accept, editPromptLabel, editTextField);

                editLink.setDisable(false);
//                    Hyperlink edit2 = (Hyperlink) Main.profileSelection.getChildren().get(8); edit2.setDisable(false);
//                    Main.profileSelection.getChildren().set(8,edit2);
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
        hyperlink.setText("{{"+userProfile.getType()+"}}");

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



    /**TODO: implement the frontend** for changing a current user's Room location
     * TODO: by finishing "Set House Location" in main dashboard module SHS */
    public static void changeLocation(Room newRoom) {
        for (int prof = 0; prof < Main.profiles.length; prof++) {
            if (Main.profiles[prof].getProfileID()==Main.currentActiveProfile.getProfileID()) {
                Main.profiles[prof].setCurrentLocation(newRoom);
                Main.currentLocation = newRoom;
                break;
            }
        }
    }

    /**MISCELLANEOUS METHODS */
    public static void countCurrentDateTime(Text text) {
        while (true){
            try{
                Thread.sleep(1000);
            }
            catch (Exception e) {

            }
            finally {
                text.setText(translateCurrentDateTime());
            }
        }
    }

    // return current date and time
    public static String translateCurrentDateTime() {
        String timeDisplay = "";

        String localTime = LocalDateTime.now().toString();
        String year = localTime.substring(0,4);
        String month = localTime.substring(5,7);
        int day = Integer.parseInt(localTime.substring(8,10));
        String time = localTime.substring(11,19);

        String month_name = "";
        switch (month.charAt(0)) {
            case '0':
                switch (month.charAt(1)) {
                    case '1': month_name += "Jan"; break;
                    case '2': month_name += "Feb"; break;
                    case '3': month_name += "Mar"; break;
                    case '4': month_name += "Apr"; break;
                    case '5': month_name += "May"; break;
                    case '6': month_name += "Jun"; break;
                    case '7': month_name += "Jul"; break;
                    case '8': month_name += "Aug"; break;
                    case '9': month_name += "Sep"; break;
                }
                break;
            case '1':
                switch (month.charAt(1)) {
                    case '0': month_name += "Oct"; break;
                    case '1': month_name += "Nov"; break;
                    case '2': month_name += "Dec"; break;
                }
                break;
        }
        timeDisplay += month_name+" "+day+" "+year+" at "+time+"";
        return timeDisplay;
    }

}
