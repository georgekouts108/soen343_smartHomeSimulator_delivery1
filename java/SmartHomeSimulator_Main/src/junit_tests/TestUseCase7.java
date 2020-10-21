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

    @org.junit.Test
    public void testCase7() {

        Platform.runLater(() -> {
            Controller.createNewProfile(new TextField("P"), new RadioButton());

            for (int a = 0; a < Main.getProfileSelection().getChildren().size(); a++) {
                if (Main.getProfileSelection().getChildren().get(a).getId()
                        .equals("hyperlinkForProfile"+Main.getProfiles()[0].getProfileID())) {
                    Hyperlink loginLink = (Hyperlink) Main.getProfileSelection().getChildren().get(a);
                    Controller.goToMainDashboardScene(Main.getProfiles()[0], loginLink);
                    break;
                }
            }

            ComboBox locationMenu = new ComboBox();
            locationMenu.setId("locationMenu");
            locationMenu.setTranslateX(160);
            locationMenu.setTranslateY(180);
            locationMenu.setItems(FXCollections.observableArrayList(Main.getCountries()));
            locationMenu.setPrefWidth(200);
            locationMenu.setPromptText("Select country...");
            if ((Main.getCurrentActiveProfile() == null)) {
                locationMenu.setDisable(true);
            } else {
                if (Main.isSimulationIsOn()) {
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
