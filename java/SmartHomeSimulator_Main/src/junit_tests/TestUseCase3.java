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
        SHSHelpers.setMain_stage(primaryStage);

        /**PROFILE SELECTION PAGE INITIALIZATION*/
        SHSHelpers.setProfileSelection(new AnchorPane());
        SHSHelpers.setProfileScene(new Scene(SHSHelpers.getProfileSelection()
                , SHSHelpers.getLoginpageHeight(), SHSHelpers.getLoginpageWidth()));

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
    public void testCase3_editProfile() {

        Controller.createNewProfile(new TextField("P"));
        Hyperlink tempEditLink = new Hyperlink();
        Hyperlink tempHyperLink = new Hyperlink();
        for (int a = 0; a < SHSHelpers.getProfileSelection().getChildren().size(); a++) {
            if (SHSHelpers.getProfileSelection().getChildren().get(a).getId()
                    .equals("hyperlinkForProfile" + SHSHelpers.getProfiles()[0].getProfileID())) {
                tempHyperLink = (Hyperlink) SHSHelpers.getProfileSelection().getChildren().get(a);

            } else if (SHSHelpers.getProfileSelection().getChildren().get(a).getId()
                    .equals("editLinkForProfile" + SHSHelpers.getProfiles()[0].getProfileID())) {
                tempEditLink = (Hyperlink) SHSHelpers.getProfileSelection().getChildren().get(a);
            }
        }
        Controller.editProfile(SHSHelpers.getProfiles()[0],
                tempHyperLink, tempEditLink);


        for (int a = 0; a < SHSHelpers.getProfileSelection().getChildren().size(); a++) {
            if (SHSHelpers.getProfileSelection().getChildren().get(a).getId()
                    .equals("editProfileTextField")) {
                TextField tf = (TextField) SHSHelpers.getProfileSelection().getChildren().get(a);
                tf.setText("C");
                for (int b = 0; b < SHSHelpers.getProfileSelection().getChildren().size(); b++) {
                    if (SHSHelpers.getProfileSelection().getChildren().get(b).getId()
                            .equals("acceptEditProfileButton")) {
                        javafx.scene.control.Button button = (javafx.scene.control.Button) SHSHelpers.getProfileSelection().getChildren().get(b);
                        button.fire();
                        break;
                    }
                }
                break;
            }
        }

        assertEquals("Child", SHSHelpers.getProfiles()[0].getType());
    }

    @org.junit.Test
    public void testCase3_removeProfile() {

        Controller.createNewProfile(new TextField("P"));

        for (int a = 0; a < SHSHelpers.getProfileSelection().getChildren().size(); a++) {
            if (SHSHelpers.getProfileSelection().getChildren().get(a).getId()
                    .equals("deleteLinkForProfile" + SHSHelpers.getProfiles()[0].getProfileID())) {
                Controller.deleteProfile(SHSHelpers.getProfiles()[0],
                        (Hyperlink) SHSHelpers.getProfileSelection().getChildren().get(a));
                assertEquals(true, SHSHelpers.getProfiles().length==0);
                break;
            }
        }

    }
}
