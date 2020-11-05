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


public class TestUseCase7 extends ApplicationTest {

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

    @org.junit.Test
    public void testCase7() {

        Platform.runLater(() -> {
            Controller.createNewProfile(new TextField("P"));

            for (int a = 0; a < SHSHelpers.getProfileSelection().getChildren().size(); a++) {
                if (SHSHelpers.getProfileSelection().getChildren().get(a).getId()
                        .equals("hyperlinkForProfile"+SHSHelpers.getProfiles()[0].getProfileID())) {
                    Hyperlink loginLink = (Hyperlink) SHSHelpers.getProfileSelection().getChildren().get(a);
                    Controller.goToMainDashboardScene(SHSHelpers.getProfiles()[0], loginLink);
                    break;
                }
            }

            ComboBox locationMenu = new ComboBox();
            locationMenu.setId("locationMenu");
            locationMenu.setTranslateX(160);
            locationMenu.setTranslateY(180);
            locationMenu.setItems(FXCollections.observableArrayList(SHSHelpers.getCountries()));
            locationMenu.setPrefWidth(200);
            locationMenu.setPromptText("Select country...");
            if ((SHSHelpers.getCurrentActiveProfile() == null)) {
                locationMenu.setDisable(true);
            } else {
                if (SHSHelpers.isSimulationIsOn()) {
                    locationMenu.setDisable(true);
                } else {
                    locationMenu.setDisable(false);
                }
            }

            locationMenu.setValue("AG");
            for (int a = 0; a < Main.getMain_dashboard().getChildren().size(); a++) {
                try {
                    if (Main.getMain_dashboard().getChildren().get(a).getId().equals("locationLabel")) {
                        Label updatedLabel = (Label) Main.getMain_dashboard().getChildren().get(a);
                        updatedLabel.setText("House\nLocation:\n" + locationMenu.getValue().toString());
                        Main.getMain_dashboard().getChildren().set(a, updatedLabel);
                        assertEquals("House\nLocation:\nAG",updatedLabel.getText());
                        break;
                    }
                } catch (Exception ex) {}
            }
        });
    }
}
