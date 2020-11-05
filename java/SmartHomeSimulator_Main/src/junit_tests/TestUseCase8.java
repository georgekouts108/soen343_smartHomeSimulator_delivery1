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
        Main.setMain_stage(primaryStage);

        /**PROFILE SELECTION PAGE INITIALIZATION*/
        Main.setProfileSelection(new AnchorPane());
        Main.setProfileScene(new Scene(Main.getProfileSelection(),
                Main.getLoginpageHeight(), Main.getLoginpageWidth()));

        /**MODULE INITIALIZATIONS */
        Main.setShsModule(new AnchorPane());
        Main.setShcModule(new AnchorPane());
        Main.setShpModule(new AnchorPane());
        Main.setShhModule(new AnchorPane());

        /**MAIN DASHBOARD INITIALIZATIONS */
        Main.setMain_dashboard(new AnchorPane());
        Main.createMainDashboardNecessities();

        Main.setDashboardScene(new Scene(Main.getMain_dashboard(),
                Main.getDashboardHeight(), Main.getDashboardWidth()));

        /**EDIT SIMULATION CONTEXT INITIALIZATIONS */
        Main.setEditContextLayout(new AnchorPane());
        Main.setEditContextScene(new Scene(Main.getEditContextLayout(), 650, 650));
        Main.setEditContextLayout2(new AnchorPane());
        Main.setEditContextScene2(new Scene(Main.getEditContextLayout2(), 650, 650));

        /**SET THE MAIN STAGE*/
        Main.getMain_stage().setTitle("Smart Home Simulator - No user");
        Main.getMain_stage().setScene(Main.getDashboardScene());
        Main.getMain_stage().show();
    }

    @Before
    public void setup() {
        try {
            Controller.createNewProfile(new TextField("P")); // profile 1
            Controller.createNewProfile(new TextField("C")); // profile 2

            for (int a = 0; a < Main.getProfileSelection().getChildren().size(); a++) {
                if (Main.getProfileSelection().getChildren().get(a).getId()
                        .equals("hyperlinkForProfile" + Main.getProfiles()[0].getProfileID())) {
                    Hyperlink loginLink = (Hyperlink) Main.getProfileSelection().getChildren().get(a);
                    Controller.goToMainDashboardScene(Main.getProfiles()[0], loginLink);
                    break;
                }
            }

            House house = null;
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            Main.setHouseLayoutFile(fileChooser.showOpenDialog(Main.getMain_stage()));
            if (Main.getHouseLayoutFile() != null) {
                Main.setHouseLayoutFileName(Main.getHouseLayoutFile().getName());
                Main.setHouseLayoutFilePathName(Main.getHouseLayoutFile().getPath());
                Main.getHouseLayoutFile().setReadOnly();
                try {
                    house = new House(Main.getHouseLayoutFilePathName());
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                Main.setHouseholdLocations(house.getRooms());
                Main.setHouseLayout(house.getLayout());
                Main.getHouseLayout().setPrefHeight(675);
                Main.getHouseLayout().setPrefWidth(675);
                Main.getHouseLayout().setId("houseLayout");
                Main.getHouseLayout().setTranslateX(615);
                Main.getHouseLayout().setTranslateY(10);
                Main.getHouseLayout().setDisable(true);
                Main.getMain_dashboard().getChildren().add(Main.getHouseLayout());
                Main.createMainDashboardNecessities();
            }

            Label currentRoom = new Label("Your location: ");
            currentRoom.setId("currentRoomOfLoggedInUser");
            currentRoom.setTranslateY(200);
            currentRoom.setTranslateX(450);
            Main.getEditContextLayout().getChildren().add(currentRoom);

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
                        if ((Main.getCurrentActiveProfile() != null)) {
                            if (Main.getCurrentLocation() == null) {
                                Main.getHouseholdLocations()[fr].setNumberOfPeopleInside(Main.getHouseholdLocations()[fr].getNumberOfPeopleInside() + 1);

                                // put that for loop here
                                for (int i = 0; i < Main.getEditContextLayout().getChildren().size(); i++) {
                                    try {
                                        if (Main.getEditContextLayout().getChildren().get(i).getId().equals("numOfPeopleOutside")) {
                                            Label label = (Label) Main.getEditContextLayout().getChildren().get(i);
                                            int numberOfPeopleInHouse = 0;
                                            int numberOfPeopleOutside;
                                            for (int p = 0; p < Main.getHouseholdLocations().length; p++) {
                                                numberOfPeopleInHouse += Main.getHouseholdLocations()[p].getNumberOfPeopleInside();
                                            }
                                            numberOfPeopleOutside = Main.getProfiles().length - numberOfPeopleInHouse;

                                            label.setText("# of people: " + numberOfPeopleOutside);
                                            Main.getEditContextLayout().getChildren().set(i, label);
                                            break;
                                        }
                                    } catch (Exception ex) {
                                    }
                                }
                            } else {
                                if (!(Main.getCurrentLocation() == Main.getHouseholdLocations()[fr])) {
                                    Main.getHouseholdLocations()[fr].setNumberOfPeopleInside(Main.getHouseholdLocations()[fr].getNumberOfPeopleInside() + 1);
                                    Main.getCurrentLocation().setNumberOfPeopleInside(Main.getCurrentLocation().getNumberOfPeopleInside() - 1);
                                }
                            }
                        }
                        for (int j = 0; j < Main.getEditContextLayout().getChildren().size(); j++) {
                            try {
                                if (Main.getEditContextLayout().getChildren().get(j).getId().equals("numOfPeopleInRoom" + Main.getHouseholdLocations()[finalR].getRoomID())) {
                                    Label label = (Label) Main.getEditContextLayout().getChildren().get(j);
                                    label.setText("# of people: " + Main.getHouseholdLocations()[finalR].getNumberOfPeopleInside());
                                    Main.getEditContextLayout().getChildren().set(j, label);
                                    break;
                                }
                            } catch (Exception ex) {
                            }
                        }

                        for (int i = 0; i < Main.getEditContextLayout().getChildren().size(); i++) {
                            try {
                                if (Main.getEditContextLayout().getChildren().get(i).getId().equals("numOfPeopleInRoom" + Main.getCurrentLocation().getRoomID())) {
                                    Label label = (Label) Main.getEditContextLayout().getChildren().get(i);
                                    label.setText("# of people: " + Main.getCurrentLocation().getNumberOfPeopleInside());
                                    Main.getEditContextLayout().getChildren().set(i, label);
                                    break;
                                }
                            } catch (Exception ex) {
                            }
                        }
                        Main.setCurrentLocation(Main.getHouseholdLocations()[fr]);
                        for (int a = 0; a < Main.getEditContextLayout2().getChildren().size(); a++) {
                            try {
                                if (Main.getEditContextLayout2().getChildren().get(a).getId().equals("currentRoomOfProfile" + Main.getCurrentActiveProfile().getProfileID())) {
                                    Label label = (Label) Main.getEditContextLayout2().getChildren().get(a);
                                    label.setText(Main.getCurrentLocation().getName());
                                    Main.getEditContextLayout2().getChildren().set(a, label);
                                    break;
                                }
                            } catch (Exception ex) {
                            }
                        }
                        for (int i = 0; i < Main.getEditContextLayout().getChildren().size(); i++) {
                            try {
                                if (Main.getEditContextLayout().getChildren().get(i).getId().equals("currentRoomOfLoggedInUser")) {
                                    Label label = (Label) Main.getEditContextLayout().getChildren().get(i);
                                    label.setText("Your location:\n" + Main.getCurrentLocation().getName());
                                    Main.getEditContextLayout().getChildren().set(i, label);
                                    break;
                                }
                            } catch (Exception ex) {
                            }
                        }
                        //========================================================================================================================
                    });
                    Main.getEditContextLayout().getChildren().addAll(hyp, l1);
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
            Main.setHouseholdLocations(test_rooms);

            // try moving the logged user to the Room at index 3
            Main.setCurrentLocation(Main.getHouseholdLocations()[3]);
            for (int a = 0; a < Main.getEditContextLayout().getChildren().size(); a++) {
                if (Main.getEditContextLayout().getChildren().get(a).getId().equals("numOfPeopleInRoom"+Main.getHouseholdLocations()[3].getRoomID())) {
                    Label label = (Label) Main.getEditContextLayout().getChildren().get(a);
                    assertEquals(true, label.getText().equals("# of people: 1"));
                    break;
                }
            }
        });
    }
}
