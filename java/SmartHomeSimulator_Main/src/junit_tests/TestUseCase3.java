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

public class TestUseCase3 extends ApplicationTest {

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

    @org.junit.Test
    public void testCase3_editProfile() {

        Controller.createNewProfile(new TextField("P"));
        Hyperlink tempEditLink = new Hyperlink();
        Hyperlink tempHyperLink = new Hyperlink();
        for (int a = 0; a < Main.getProfileSelection().getChildren().size(); a++) {
            if (Main.getProfileSelection().getChildren().get(a).getId()
                    .equals("hyperlinkForProfile" + Main.getProfiles()[0].getProfileID())) {
                tempHyperLink = (Hyperlink) Main.getProfileSelection().getChildren().get(a);

            } else if (Main.getProfileSelection().getChildren().get(a).getId()
                    .equals("editLinkForProfile" + Main.getProfiles()[0].getProfileID())) {
                tempEditLink = (Hyperlink) Main.getProfileSelection().getChildren().get(a);
            }
        }
        Controller.editProfile(Main.getProfiles()[0],
                tempHyperLink, tempEditLink);


        for (int a = 0; a < Main.getProfileSelection().getChildren().size(); a++) {
            if (Main.getProfileSelection().getChildren().get(a).getId()
                    .equals("editProfileTextField")) {
                TextField tf = (TextField) Main.getProfileSelection().getChildren().get(a);
                tf.setText("C");
                for (int b = 0; b < Main.getProfileSelection().getChildren().size(); b++) {
                    if (Main.getProfileSelection().getChildren().get(b).getId()
                            .equals("acceptEditProfileButton")) {
                        javafx.scene.control.Button button = (javafx.scene.control.Button) Main.getProfileSelection().getChildren().get(b);
                        button.fire();
                        break;
                    }
                }
                break;
            }
        }

        assertEquals("Child", Main.getProfiles()[0].getType());
    }

    @org.junit.Test
    public void testCase3_removeProfile() {

        Controller.createNewProfile(new TextField("P"));

        for (int a = 0; a < Main.getProfileSelection().getChildren().size(); a++) {
            if (Main.getProfileSelection().getChildren().get(a).getId()
                    .equals("deleteLinkForProfile" + Main.getProfiles()[0].getProfileID())) {
                Controller.deleteProfile(Main.getProfiles()[0],
                        (Hyperlink)Main.getProfileSelection().getChildren().get(a));
                assertEquals(true, Main.getProfiles().length==0);
                break;
            }
        }

    }
}
