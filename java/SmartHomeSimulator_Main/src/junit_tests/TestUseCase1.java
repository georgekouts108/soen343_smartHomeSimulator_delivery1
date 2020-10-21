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
    private final Main main = new Main();
    private final Controller controller = new Controller();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(false);
        Main.setMain_stage(primaryStage);

        /**PROFILE SELECTION PAGE INITIALIZATION*/
        Main.setProfileSelection(new AnchorPane());
        Main.setProfileScene(new Scene(Main.getProfileSelection()
                , Main.getLoginpageHeight(), Main.getLoginpageWidth()));

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

    /**USE CASE #1 -- START AND STOP SIMULATOR*/

    House house = null;
    FileChooser fileChooser = null;


    /**TODO: FIX THIS TEST CASE*/
    @org.junit.Test
    public void testUseCase1_turnOnSim() {
        Controller.createNewProfile(new TextField("P"), new RadioButton());

        Platform.runLater(() -> {
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            Main.setHouseLayoutFile(fileChooser.showOpenDialog(Main.getMain_stage()));
            if (Main.getHouseLayoutFile() != null) {
                Main.setHouseLayoutFileName(Main.getHouseLayoutFile().getName());
                Main.setHouseLayoutFilePathName( Main.getHouseLayoutFile().getPath());
                Main.getHouseLayoutFile().setReadOnly();
                try {
                    house = new House(Main.getHouseLayoutFilePathName());
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                Main.setHouseholdLocations( house.getRooms());
                Main.setHouseLayout( house.getLayout());
                Main.getHouseLayout().setPrefHeight(675);
                Main.getHouseLayout().setPrefWidth(675);
                Main.getHouseLayout().setId("houseLayout");
                Main.getHouseLayout().setTranslateX(615);
                Main.getHouseLayout().setTranslateY(10);
                Main.getHouseLayout().setDisable(true);
                Main.getMain_dashboard().getChildren().add(Main.getHouseLayout());
                Main.createMainDashboardNecessities();

                for (int a = 0; a < Main.getProfileSelection().getChildren().size(); a++) {
                    if (Main.getProfileSelection().getChildren().get(a).getId()
                            .equals("hyperlinkForProfile"+Main.getProfiles()[0].getProfileID())) {
                        Hyperlink loginLink = (Hyperlink) Main.getProfileSelection().getChildren().get(a);
                        Controller.goToMainDashboardScene(Main.getProfiles()[0], loginLink);
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
                Main.getHouseLayout().setDisable(false);
                finalTa.setDisable(false);
                finalTab.setDisable(false);
                Main.setSimulationIsOn(true);
                Main.createMainDashboardNecessities();
                assertEquals(true, Main.isSimulationIsOn());
            }
        });
    }

    @org.junit.Test
    public void testUseCase1_turnOffSim() {
        Controller.createNewProfile(new TextField("P"), new RadioButton());

        for (int a = 0; a < Main.getProfileSelection().getChildren().size(); a++) {
            if (Main.getProfileSelection().getChildren().get(a).getId()
                    .equals("hyperlinkForProfile"+Main.getProfiles()[0].getProfileID())) {
                Hyperlink loginLink = (Hyperlink) Main.getProfileSelection().getChildren().get(a);
                Controller.goToMainDashboardScene(Main.getProfiles()[0], loginLink);
                break;
            }
        }

        for (int a = 0; a < Main.getMain_dashboard().getChildren().size(); a++) {
            if (Main.getMain_dashboard().getChildren().get(a).getId().equals("simulationOnOffButton")) {
                ToggleButton tb = (ToggleButton) Main.getMain_dashboard().getChildren().get(a);
                tb.setSelected(false);
                assertEquals(false, Main.isSimulationIsOn());
                break;
            }
        }
    }


}
