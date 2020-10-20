package junit_tests;

import house.*;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
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
    @Before
    public void setup() throws Exception {
        try {
            Controller.createNewProfile(new TextField("P"), new RadioButton());

            for (int a = 0; a < Main.getProfileSelection().getChildren().size(); a++) {
                if (Main.getProfileSelection().getChildren().get(a).getId()
                        .equals("hyperlinkForProfile"+Main.getProfiles()[0].getProfileID())) {
                    Hyperlink loginLink = (Hyperlink) Main.getProfileSelection().getChildren().get(a);
                    Controller.goToMainDashboardScene(Main.getProfiles()[0], loginLink);
                    break;
                }
            }

        }catch (Exception e){}
    }

//    @org.junit.Test
//    public void testUseCase1_turnOnSim() {
//        for (int a = 0; a < Main.getMain_dashboard().getChildren().size(); a++) {
//            if (Main.getMain_dashboard().getChildren().get(a).getId().equals("simulationOnOffButton")) {
//                ToggleButton tb = (ToggleButton) Main.getMain_dashboard().getChildren().get(a);
//                tb.setSelected(true);
//                break;
//            }
//        }
//        assertEquals(true, Main.isSimulationIsOn());
//    }

    @org.junit.Test
    public void testUseCase1_turnOffSim() {
        for (int a = 0; a < Main.getMain_dashboard().getChildren().size(); a++) {
            if (Main.getMain_dashboard().getChildren().get(a).getId().equals("simulationOnOffButton")) {
                ToggleButton tb = (ToggleButton) Main.getMain_dashboard().getChildren().get(a);
                tb.setSelected(false);
                break;
            }
        }
        assertEquals(false, Main.isSimulationIsOn());
    }


}
