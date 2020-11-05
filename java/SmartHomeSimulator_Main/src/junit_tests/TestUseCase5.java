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

    FileChooser fileChooser = null;
    House house = null;

    @org.junit.Test
    public void testCase5() {
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
                //Main.getMain_dashboard().getChildren().remove(chooseFileButton);
                Main.getMain_dashboard().getChildren().add(SHSHelpers.getHouseLayout());
                Main.createMainDashboardNecessities();
            }
            assertEquals(true, SHSHelpers.getHouseLayout() != null);
        });
    }
}
