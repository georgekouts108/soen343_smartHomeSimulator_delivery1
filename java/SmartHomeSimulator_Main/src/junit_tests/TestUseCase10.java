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
        SHSHelpers.setMain_stage(primaryStage);

        /**PROFILE SELECTION PAGE INITIALIZATION*/
        SHSHelpers.setProfileSelection(new AnchorPane());
        SHSHelpers.setProfileScene(new Scene(SHSHelpers.getProfileSelection(), SHSHelpers.getLoginpageHeight(), SHSHelpers.getLoginpageWidth()));

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

    House house = null;
    FileChooser fileChooser = null;

    @org.junit.Test
    public void testCase10() {
        Platform.runLater(() -> {
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            SHSHelpers.setHouseLayoutFile(fileChooser.showOpenDialog(SHSHelpers.getMain_stage()));
            try{
                boolean expectedBool = true;
                for (int r = 0; r < sample.SHSHelpers.gethouseholdLocations().length; r++) {
                    for (int w = 0; w < SHSHelpers.gethouseholdLocations()[r].getWindowCollection().length; w++) {
                        if(r == 2 && w == 3) {
                            utilities.Window window = SHSHelpers.gethouseholdLocations()[r].getWindowCollection()[w];
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
