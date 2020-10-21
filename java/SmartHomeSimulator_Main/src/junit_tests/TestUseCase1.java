package junit_tests;

import house.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
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

    /**TODO: FIX THIS TEST CASE*/
    @org.junit.Test
    public void testUseCase1_turnOnSim() {
        Controller.createNewProfile(new TextField("P"), new RadioButton());

        for (int a = 0; a < Main.getProfileSelection().getChildren().size(); a++) {
            if (Main.getProfileSelection().getChildren().get(a).getId()
                    .equals("hyperlinkForProfile"+Main.getProfiles()[0].getProfileID())) {
                Hyperlink loginLink = (Hyperlink) Main.getProfileSelection().getChildren().get(a);
                Controller.goToMainDashboardScene(Main.getProfiles()[0], loginLink);
                break;
            }
        }

        ToggleButton tb = new ToggleButton();
        Button b = new Button();
        TextArea oc = new TextArea();
        TabPane modules = new TabPane();
        for (int a = 0; a < Main.getMain_dashboard().getChildren().size(); a++) {

            if (Main.getMain_dashboard().getChildren().get(a).getId().equals("simulationOnOffButton")) {
                tb = (ToggleButton) Main.getMain_dashboard().getChildren().get(a);
                tb.setDisable(false);
                tb.setSelected(true);
            } else if (Main.getMain_dashboard().getChildren().get(a).getId().equals("editContextButton")) {
                b = (Button) Main.getMain_dashboard().getChildren().get(a);
            } else if (Main.getMain_dashboard().getChildren().get(a).getId().equals("OutputConsole")) {
                oc = (TextArea) Main.getMain_dashboard().getChildren().get(a);
            } else if (Main.getMain_dashboard().getChildren().get(a).getId().equals("modulesInterface")) {
                modules = (TabPane) Main.getMain_dashboard().getChildren().get(a);
            }
        }
        Controller.startSimulation(tb, b, oc, modules);
        assertEquals(true, Main.isSimulationIsOn());
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
