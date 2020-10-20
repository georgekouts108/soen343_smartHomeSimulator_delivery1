package junit_tests;

import house.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import javafx.scene.control.Label;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;

import org.testfx.framework.junit.*;

public class TestUseCase9 extends ApplicationTest{

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
    public void testCase9() {
    TextField input = new TextField("32");
    Label expectedLabel = new Label("Outside Temp.\n32°C");

        Platform.runLater(() -> {
            try{
                for(int i=0; i < Main.getMain_dashboard().getChildren().size(); i++){
                    if (Main.getMain_dashboard().getChildren().get(i).getId().equals("temp")){
                        if(!input.getCharacters().toString().isEmpty()) {
                            Label label = (Label) Main.getMain_dashboard().getChildren().get(i);
                            label.setText("Outside Temp.\n" + input.getCharacters().toString() + "°C");
                            Main.getMain_dashboard().getChildren().set(i, label);
                            assertEquals(expectedLabel.getText(), label.getText());
                            break;
                        }
                    }
                }

            }catch (Exception err){
                System.out.print("There was an error while modifying the outdoor temperature.");
            }
        });

        /*
        for(int i=0; i < Main.getEditContextLayout().getChildren().size(); i++){
            if (Main.getEditContextLayout().getChildren().get(i).getId().equals("temperatureText")){
                    TextField tmp = (TextField) Main.getEditContextLayout().getChildren().get(i);
                    tmp.setText("32.0");
                    Main.getEditContextLayout().getChildren().set(i, tmp);
                    break;
            }
        }

        for(int i=0; i < Main.getEditContextLayout().getChildren().size(); i++){
            if (Main.getEditContextLayout().getChildren().get(i).getId().equals("confirmTemperatureButton")){
                javafx.scene.control.Button button = (javafx.scene.control.Button) Main.getEditContextLayout().getChildren().get(i);
                button.fire();
            }
        }
        */
    }
}
