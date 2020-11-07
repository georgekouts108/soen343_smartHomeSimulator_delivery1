package junit_tests;

import house.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
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
import org.testfx.framework.junit.*;

public class TestUseCase18 extends ApplicationTest {
    /**todo: implement*/
    File testLayoutFile;
    House house = null;
    UserProfile dummyProfile = null;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(false);
        SHSHelpers.setMain_stage(primaryStage);

        /**PROFILE SELECTION PAGE INITIALIZATION*/
        SHSHelpers.setProfileSelection(new AnchorPane());
        SHSHelpers.setProfileScene(new Scene(SHSHelpers.getProfileSelection()
                , SHSHelpers.getLoginpageHeight(), SHSHelpers.getLoginpageWidth()));

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

    @org.junit.Test
    public void testCase14_user() {
        Platform.runLater(()-> {

            ///////////////////////
            /**Load the house layout text file*/
            try {
                testLayoutFile = new File("src/housetest.txt");
                if (!testLayoutFile.exists()) {
                    testLayoutFile.createNewFile();
                }
            }catch (Exception e){}

            SHSHelpers.setHouseLayoutFile(testLayoutFile);
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

            /**Create a new user profile */
            for (int a = 0; a < SHSHelpers.getShsModule().getChildren().size(); a++) {
                try {
                    if (SHSHelpers.getShsModule().getChildren().get(a).getId().equals("manageOrSwitchProfileButton")) {
                        Button tempButton = (Button) SHSHelpers.getShsModule().getChildren().get(a);
                        tempButton.fire();

                        SHSHelpers.getShsModule().getChildren().set(a, tempButton);
                        SHSHelpers.setShsModule(SHSHelpers.getShsModule());
                        break;
                    }
                }catch (Exception e){}
            }

            for (int a = 0; a < SHSHelpers.getProfileSelection().getChildren().size(); a++) {
                if (SHSHelpers.getProfileSelection().getChildren().get(a).getId()
                        .equals("newProfileTextField")) {
                    TextField tf = (TextField) SHSHelpers.getProfileSelection().getChildren().get(a);
                    tf.setText("P");

                    for (int b = 0; b < SHSHelpers.getProfileSelection().getChildren().size(); b++) {
                        if (SHSHelpers.getProfileSelection().getChildren().get(b).getId()
                                .equals("addNewProfileButton")) {
                            javafx.scene.control.Button button = (javafx.scene.control.Button) SHSHelpers.getProfileSelection().getChildren().get(b);
                            button.fire();
                            break;
                        }
                    }
                    break;
                }
            }

            /**Log in with the new user profile */
            Hyperlink tempLoginLink = null;
            for (int a = 0; a < SHSHelpers.getProfileSelection().getChildren().size(); a++) {
                if (SHSHelpers.getProfileSelection().getChildren().get(a).getId()
                        .equals("loginLinkForProfile" + SHSHelpers.getProfiles()[0].getProfileID())) {
                    tempLoginLink = (Hyperlink) SHSHelpers.getProfileSelection().getChildren().get(a);
                    break;
                }
            }
            dummyProfile = SHSHelpers.getProfiles()[0];
            Controller.goToMainDashboardScene(dummyProfile, tempLoginLink);
            ///////////////////////
        });
    }
}
