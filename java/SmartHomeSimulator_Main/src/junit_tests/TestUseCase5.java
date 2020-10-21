package junit_tests;

import house.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.*;

import static org.junit.Assert.*;

import org.junit.Before;

import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicReference;

import org.testfx.framework.junit.*;

public class TestUseCase5 extends ApplicationTest {



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

    FileChooser fileChooser = null;
    House house = null;

    @org.junit.Test
    public void testCase5() {
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
                //Main.getMain_dashboard().getChildren().remove(chooseFileButton);
                Main.getMain_dashboard().getChildren().add(Main.getHouseLayout());
                Main.createMainDashboardNecessities();
            }
            assertEquals(true, Main.getHouseLayout() != null);
        });
    }
}
