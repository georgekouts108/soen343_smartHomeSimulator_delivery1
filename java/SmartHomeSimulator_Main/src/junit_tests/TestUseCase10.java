package junit_tests;

import house.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import javafx.scene.control.Label;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;

import org.testfx.framework.junit.*;

public class TestUseCase10 extends ApplicationTest {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(false);
        Main.setMain_stage(primaryStage);

        /**PROFILE SELECTION PAGE INITIALIZATION*/
        Main.setProfileSelection(new AnchorPane());
        Main.setProfileScene(new Scene(Main.getProfileSelection(), Main.getLoginpageHeight(), Main.getLoginpageWidth()));

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

    House house = null;
    FileChooser fileChooser = null;

    @org.junit.Test
    public void testCase10() {
        Platform.runLater(() -> {
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            Main.setHouseLayoutFile(fileChooser.showOpenDialog(Main.getMain_stage()));
            try{
                boolean expectedBool = true;
                for (int r = 0; r < sample.Main.gethouseholdLocations().length; r++) {
                    for (int w = 0; w < Main.gethouseholdLocations()[r].getWindowCollection().length; w++) {
                        if(r == 2 && w == 3) {
                            utilities.Window window = Main.gethouseholdLocations()[r].getWindowCollection()[w];
                            window.setBlocked(expectedBool);

                            assertTrue(window.isBlocked());
                            break;
                        }
                        System.out.println("okay");
                    }
                }
            }catch (Exception err){
                System.out.print("There was an error while checking if the windows block.");
            }
        });
    }

}
