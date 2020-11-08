package junit_tests;
import javafx.application.Platform;
//import com.sun.media.jfxmediaimpl.platform.Platform;
import house.*;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utilities.*;
import sample.*;

import org.junit.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.testfx.framework.junit.*;

public class TestUseCase8 extends ApplicationTest {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(false);
        SHSHelpers.setMain_stage(primaryStage);

        /**PROFILE SELECTION PAGE INITIALIZATION*/
        SHSHelpers.setProfileSelection(new AnchorPane());
        SHSHelpers.setProfileScene(new Scene(SHSHelpers.getProfileSelection(),
                SHSHelpers.getLoginpageHeight(), SHSHelpers.getLoginpageWidth()));

        /**MODULE INITIALIZATIONS */
        SHSHelpers.setShsModule(new AnchorPane());
        SHSHelpers.setShcModule(new AnchorPane());
        SHSHelpers.setShpModule(new AnchorPane());
        SHSHelpers.setShhModule(new AnchorPane());

        /**MAIN DASHBOARD INITIALIZATIONS */
        SHSHelpers.setMain_dashboard(new AnchorPane());
        Main.createMainDashboardNecessities();

        SHSHelpers.setDashboardScene(new Scene(Main.getMain_dashboard(),
                SHSHelpers.getDashboardHeight(), SHSHelpers.getDashboardWidth()));

        /**EDIT SIMULATION CONTEXT INITIALIZATIONS */
        SHSHelpers.setEditContextLayout(new AnchorPane());
        SHSHelpers.setEditContextScene(new Scene(SHSHelpers.getEditContextLayout(), 650, 650));
        SHSHelpers.setEditContextLayout2(new AnchorPane());
        SHSHelpers.setEditContextScene2(new Scene(SHSHelpers.getEditContextLayout2(), 650, 650));

        /**SET THE MAIN STAGE*/
        SHSHelpers.getMain_stage().setTitle("Smart Home Simulator - No user");
        SHSHelpers.getMain_stage().setScene(SHSHelpers.getDashboardScene());
        SHSHelpers.getMain_stage().show();
    }

    @Before
    public void setup() {
        try {
            //Controller.createNewProfile(new TextField("P")); // profile 1
            //Controller.createNewProfile(new TextField("C")); // profile 2

            for (int a = 0; a < SHSHelpers.getProfileSelection().getChildren().size(); a++) {
                if (SHSHelpers.getProfileSelection().getChildren().get(a).getId()
                        .equals("hyperlinkForProfile" + SHSHelpers.getProfiles()[0].getProfileID())) {
                    Hyperlink loginLink = (Hyperlink) SHSHelpers.getProfileSelection().getChildren().get(a);
                    Controller.goToMainDashboardScene(SHSHelpers.getProfiles()[0], loginLink);
                    break;
                }
            }

            House house = null;
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            SHSHelpers.setHouseLayoutFile(fileChooser.showOpenDialog(SHSHelpers.getMain_stage()));
            if (SHSHelpers.getHouseLayoutFile() != null) {
                SHSHelpers.setHouseLayoutFileName(SHSHelpers.getHouseLayoutFile().getName());
                SHSHelpers.setHouseLayoutFilePathName(SHSHelpers.getHouseLayoutFile().getPath());
                SHSHelpers.getHouseLayoutFile().setReadOnly();
                try {
                    house = new House(SHSHelpers.getHouseLayoutFilePathName());
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                SHSHelpers.setHouseholdLocations(house.getRooms());
                SHSHelpers.setHouseLayout(house.getLayout());
                SHSHelpers.getHouseLayout().setPrefHeight(675);
                SHSHelpers.getHouseLayout().setPrefWidth(675);
                SHSHelpers.getHouseLayout().setId("houseLayout");
                SHSHelpers.getHouseLayout().setTranslateX(615);
                SHSHelpers.getHouseLayout().setTranslateY(10);
                SHSHelpers.getHouseLayout().setDisable(true);
                Main.getMain_dashboard().getChildren().add(SHSHelpers.getHouseLayout());
                Main.createMainDashboardNecessities();
            }

            Label currentRoom = new Label("Your location: ");
            currentRoom.setId("currentRoomOfLoggedInUser");
            currentRoom.setTranslateY(200);
            currentRoom.setTranslateX(450);
            SHSHelpers.getEditContextLayout().getChildren().add(currentRoom);

            int transY = 120;
            for (int r = 0; r < Main.getHouseholdLocations().length + 1; r++) {

                if (r != Main.getHouseholdLocations().length) {
                    Label l1 = new Label("# of people: " + Main.getHouseholdLocations()[r].getNumberOfPeopleInside());
                    l1.setId("numOfPeopleInRoom" + Main.getHouseholdLocations()[r].getRoomID());
                    l1.setTranslateX(120);
                    l1.setTranslateY(transY + 5);
                    int fr = r;
                    Hyperlink hyp = new Hyperlink(Main.getHouseholdLocations()[r].getName());
                    hyp.setId("hyperlinkForRoom-" + Main.getHouseholdLocations()[r].getName());
                    hyp.setTranslateX(20);
                    hyp.setTranslateY(transY);
                    int finalR = r;
                    hyp.setOnAction(e -> {

                        // change the label of the number of people in the origin and destination rooms
                        if ((SHSHelpers.getCurrentActiveProfile() != null)) {
                            if (SHSHelpers.getCurrentLocation() == null) {
                                Main.getHouseholdLocations()[fr].setNumberOfPeopleInside(Main.getHouseholdLocations()[fr].getNumberOfPeopleInside() + 1);

                                // put that for loop here
                                for (int i = 0; i < SHSHelpers.getEditContextLayout().getChildren().size(); i++) {
                                    try {
                                        if (SHSHelpers.getEditContextLayout().getChildren().get(i).getId().equals("numOfPeopleOutside")) {
                                            Label label = (Label) SHSHelpers.getEditContextLayout().getChildren().get(i);
                                            int numberOfPeopleInHouse = 0;
                                            int numberOfPeopleOutside;
                                            for (int p = 0; p < Main.getHouseholdLocations().length; p++) {
                                                numberOfPeopleInHouse += Main.getHouseholdLocations()[p].getNumberOfPeopleInside();
                                            }
                                            numberOfPeopleOutside = SHSHelpers.getProfiles().length - numberOfPeopleInHouse;

                                            label.setText("# of people: " + numberOfPeopleOutside);
                                            SHSHelpers.getEditContextLayout().getChildren().set(i, label);
                                            break;
                                        }
                                    } catch (Exception ex) {
                                    }
                                }
                            } else {
                                if (!(SHSHelpers.getCurrentLocation() == Main.getHouseholdLocations()[fr])) {
                                    Main.getHouseholdLocations()[fr].setNumberOfPeopleInside(Main.getHouseholdLocations()[fr].getNumberOfPeopleInside() + 1);
                                    SHSHelpers.getCurrentLocation().setNumberOfPeopleInside(SHSHelpers.getCurrentLocation().getNumberOfPeopleInside() - 1);
                                }
                            }
                        }
                        for (int j = 0; j < SHSHelpers.getEditContextLayout().getChildren().size(); j++) {
                            try {
                                if (SHSHelpers.getEditContextLayout().getChildren().get(j).getId().equals("numOfPeopleInRoom" + Main.getHouseholdLocations()[finalR].getRoomID())) {
                                    Label label = (Label) SHSHelpers.getEditContextLayout().getChildren().get(j);
                                    label.setText("# of people: " + Main.getHouseholdLocations()[finalR].getNumberOfPeopleInside());
                                    SHSHelpers.getEditContextLayout().getChildren().set(j, label);
                                    break;
                                }
                            } catch (Exception ex) {
                            }
                        }

                        for (int i = 0; i < SHSHelpers.getEditContextLayout().getChildren().size(); i++) {
                            try {
                                if (SHSHelpers.getEditContextLayout().getChildren().get(i).getId().equals("numOfPeopleInRoom" + SHSHelpers.getCurrentLocation().getRoomID())) {
                                    Label label = (Label) SHSHelpers.getEditContextLayout().getChildren().get(i);
                                    label.setText("# of people: " + SHSHelpers.getCurrentLocation().getNumberOfPeopleInside());
                                    SHSHelpers.getEditContextLayout().getChildren().set(i, label);
                                    break;
                                }
                            } catch (Exception ex) {
                            }
                        }
                        SHSHelpers.setCurrentLocation(Main.getHouseholdLocations()[fr]);
                        for (int a = 0; a < SHSHelpers.getEditContextLayout2().getChildren().size(); a++) {
                            try {
                                if (SHSHelpers.getEditContextLayout2().getChildren().get(a).getId().equals("currentRoomOfProfile" + SHSHelpers.getCurrentActiveProfile().getProfileID())) {
                                    Label label = (Label) SHSHelpers.getEditContextLayout2().getChildren().get(a);
                                    label.setText(SHSHelpers.getCurrentLocation().getName());
                                    SHSHelpers.getEditContextLayout2().getChildren().set(a, label);
                                    break;
                                }
                            } catch (Exception ex) {
                            }
                        }
                        for (int i = 0; i < SHSHelpers.getEditContextLayout().getChildren().size(); i++) {
                            try {
                                if (SHSHelpers.getEditContextLayout().getChildren().get(i).getId().equals("currentRoomOfLoggedInUser")) {
                                    Label label = (Label) SHSHelpers.getEditContextLayout().getChildren().get(i);
                                    label.setText("Your location:\n" + SHSHelpers.getCurrentLocation().getName());
                                    SHSHelpers.getEditContextLayout().getChildren().set(i, label);
                                    break;
                                }
                            } catch (Exception ex) {
                            }
                        }
                        //========================================================================================================================
                    });
                    SHSHelpers.getEditContextLayout().getChildren().addAll(hyp, l1);
                    transY += 20;
                }
            }
        }catch (Exception e){}
    }

    @org.junit.Test
    public void testCase8() {
        Platform.runLater(()-> {

            // for testing purposes
            Room testRoom = new Room("Kitchen", 2, 1, 5, true);
            Room testRoom2 = new Room("Bedroom", 1, 2, 2, true);
            Room testRoom3 = new Room("Dining Room", 1, 1, 2, true);
            Room testRoom4 = new Room("Bathroom", 2, 2, 5,  true);

            Room[] test_rooms = new Room[]{testRoom, testRoom2, testRoom3, testRoom4};
            SHSHelpers.setHouseholdLocations(test_rooms);

            // try moving the logged user to the Room at index 3
            SHSHelpers.setCurrentLocation(Main.getHouseholdLocations()[3]);
            for (int a = 0; a < SHSHelpers.getEditContextLayout().getChildren().size(); a++) {
                if (SHSHelpers.getEditContextLayout().getChildren().get(a).getId().equals("numOfPeopleInRoom"+Main.getHouseholdLocations()[3].getRoomID())) {
                    Label label = (Label) SHSHelpers.getEditContextLayout().getChildren().get(a);
                    assertEquals(true, label.getText().equals("# of people: 1"));
                    break;
                }
            }
        });
    }
}
