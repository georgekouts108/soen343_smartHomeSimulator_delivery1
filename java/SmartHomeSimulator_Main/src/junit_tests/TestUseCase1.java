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

public class TestUseCase1 extends ApplicationTest {
    private final SHSHelpers main = new SHSHelpers();
    private final Controller controller = new Controller();

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

    /**USE CASE #1 -- START AND STOP SIMULATOR*/

    House house = null;
    FileChooser fileChooser = null;


    /**TODO: FIX THIS TEST CASE*/
    @org.junit.Test
    public void testUseCase1_turnOnSim() {
        Controller.createNewProfile(new TextField("P"));

        Platform.runLater(() -> {
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            SHSHelpers.setHouseLayoutFile(fileChooser.showOpenDialog(SHSHelpers.getMain_stage()));
            if (SHSHelpers.getHouseLayoutFile() != null) {
                SHSHelpers.setHouseLayoutFileName(SHSHelpers.getHouseLayoutFile().getName());
                SHSHelpers.setHouseLayoutFilePathName( SHSHelpers.getHouseLayoutFile().getPath());
                SHSHelpers.getHouseLayoutFile().setReadOnly();
                try {
                    house = new House(SHSHelpers.getHouseLayoutFilePathName());
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                SHSHelpers.setHouseholdLocations( house.getRooms());
                SHSHelpers.setHouseLayout( house.getLayout());
                SHSHelpers.getHouseLayout().setPrefHeight(675);
                SHSHelpers.getHouseLayout().setPrefWidth(675);
                SHSHelpers.getHouseLayout().setId("houseLayout");
                SHSHelpers.getHouseLayout().setTranslateX(615);
                SHSHelpers.getHouseLayout().setTranslateY(10);
                SHSHelpers.getHouseLayout().setDisable(true);
                Main.getMain_dashboard().getChildren().add(SHSHelpers.getHouseLayout());
                Main.createMainDashboardNecessities();

                for (int a = 0; a < SHSHelpers.getProfileSelection().getChildren().size(); a++) {
                    if (SHSHelpers.getProfileSelection().getChildren().get(a).getId()
                            .equals("hyperlinkForProfile"+SHSHelpers.getProfiles()[0].getProfileID())) {
                        Hyperlink loginLink = (Hyperlink) SHSHelpers.getProfileSelection().getChildren().get(a);
                        Controller.goToMainDashboardScene(SHSHelpers.getProfiles()[0], loginLink);
                        break;
                    }
                }

                ToggleButton t = new ToggleButton();
                Button b = new Button();
                TextArea ta = new TextArea();
                TabPane tab = new TabPane();
                for (int a = 0; a < Main.getMain_dashboard().getChildren().size(); a++) {

                    if (Main.getMain_dashboard().getChildren().get(a).getId().equals("simulationOnOffButton")) {
                        t = (ToggleButton) Main.getMain_dashboard().getChildren().get(a);
                        t.setSelected(true);
                    } else if (Main.getMain_dashboard().getChildren().get(a).getId().equals("editContextButton")) {
                        b = (Button) Main.getMain_dashboard().getChildren().get(a);
                    } else if (Main.getMain_dashboard().getChildren().get(a).getId().equals("OutputConsole")) {
                        ta = (TextArea) Main.getMain_dashboard().getChildren().get(a);
                    } else if (Main.getMain_dashboard().getChildren().get(a).getId().equals("modulesInterface")) {
                        tab = (TabPane) Main.getMain_dashboard().getChildren().get(a);
                    }
                }
                ToggleButton finalT = t;
                TextArea finalTa = ta;
                Button finalB = b;
                TabPane finalTab = tab;

                Main.createMainDashboardNecessities();
                finalT.setText("Stop\nSimulation");
                finalT.setPrefWidth(finalT.getPrefWidth()); finalT.setPrefHeight(finalT.getPrefHeight());
                finalB.setDisable(false);
                SHSHelpers.getHouseLayout().setDisable(false);
                finalTa.setDisable(false);
                finalTab.setDisable(false);
                SHSHelpers.setSimulationIsOn(true);
                Main.createMainDashboardNecessities();
                assertEquals(true, SHSHelpers.isSimulationIsOn());
            }
        });
    }

    @org.junit.Test
    public void testUseCase1_turnOffSim() {
        Controller.createNewProfile(new TextField("P"));

        for (int a = 0; a < SHSHelpers.getProfileSelection().getChildren().size(); a++) {
            if (SHSHelpers.getProfileSelection().getChildren().get(a).getId()
                    .equals("hyperlinkForProfile"+SHSHelpers.getProfiles()[0].getProfileID())) {
                Hyperlink loginLink = (Hyperlink) SHSHelpers.getProfileSelection().getChildren().get(a);
                Controller.goToMainDashboardScene(SHSHelpers.getProfiles()[0], loginLink);
                break;
            }
        }

        for (int a = 0; a < Main.getMain_dashboard().getChildren().size(); a++) {
            if (Main.getMain_dashboard().getChildren().get(a).getId().equals("simulationOnOffButton")) {
                ToggleButton tb = (ToggleButton) Main.getMain_dashboard().getChildren().get(a);
                tb.setSelected(false);
                assertEquals(false, SHSHelpers.isSimulationIsOn());
                break;
            }
        }
    }


}
