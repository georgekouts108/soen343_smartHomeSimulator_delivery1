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

public class TestUseCase6 extends ApplicationTest {

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
    public void testCase6() {

        Platform.runLater(() -> {
            try {
                DatePicker datePicker = new DatePicker();
                TextField hourField = new TextField();
                TextField minuteField = new TextField();
                Label sim_date = new Label();
                Label sim_time = new Label();

                for (int a = 0; a < Main.getMain_dashboard().getChildren().size(); a++) {
                    if (Main.getMain_dashboard().getChildren().get(a).getId() != null && Main.getMain_dashboard().getChildren().get(a).getId() != ""){
                        if (Main.getMain_dashboard().getChildren().get(a).getId().equals("datePicker")) {
                            datePicker = (DatePicker) Main.getMain_dashboard().getChildren().get(a);
                            datePicker.setValue(LocalDate.of(2020, 10, 25));
                            Main.getMain_dashboard().getChildren().set(a, datePicker);
                        }
                        else if (Main.getMain_dashboard().getChildren().get(a).getId().equals("hourField")) {
                            hourField = (TextField) Main.getMain_dashboard().getChildren().get(a);
                            hourField.setText("13");
                        }
                        else if (Main.getMain_dashboard().getChildren().get(a).getId().equals("minuteField")) {
                            minuteField = (TextField) Main.getMain_dashboard().getChildren().get(a);
                            minuteField.setText("45");
                        }
                        else if (Main.getMain_dashboard().getChildren().get(a).getId().equals("simulationDate")) {
                            sim_date = (Label) Main.getMain_dashboard().getChildren().get(a);
                        }
                        else if (Main.getMain_dashboard().getChildren().get(a).getId().equals("simulationTime")) {
                            sim_time = (Label) Main.getMain_dashboard().getChildren().get(a);
                        }
                    }
                }


                int second = 0;
                int minute = 0;
                int hour = 0;
                minute = Integer.parseInt(minuteField.getText());
                hour = Integer.parseInt(hourField.getText());
                for(int i = 0;i<=0;i++) {
                    Calendar cal = new GregorianCalendar();
                    DatePicker finalDatePicker = datePicker;
                    Label finalSim_date = sim_date;
                    Platform.runLater(()-> finalSim_date.setText(String.valueOf(finalDatePicker.getValue())));
                    if (i == 0) {
                        second = 0;
                        int finalSecond = second;
                        int finalHour = hour;
                        int finalMinute = minute;
                        Label finalSim_time = sim_time;
                        Platform.runLater(()-> finalSim_time.setText("Time "+(finalHour)%24+":"+(finalMinute)%60+":"+ finalSecond%60));
                        Thread.sleep(1000);
                    }
                    else
                    {
                        second = second + 1;
                        int finalSecond = second;

                        if (finalSecond == 60){
                            minute = minute + 1;
                            second = 0;
                        }
                        int finalMinute = minute;
                        if (finalMinute == 60 && finalSecond == 60){
                            hour = hour + 1;
                            minute = 0;
                        }
                        int finalHour = hour;
                        if (finalHour == 23 && finalMinute == 60 && finalSecond == 60){
                            hour = 0;
                            minute = 0;
                            second = 0;
                        }
                        Label finalSim_time1 = sim_time;
                        Platform.runLater(()-> finalSim_time1.setText("Time "+(finalHour%24)+":"+(finalMinute%60)+":"+ finalSecond%60));
                        Thread.sleep(1000);
                    }
                }

                boolean timeSuccessfullyUpdated =
                        ( sim_date.equals(datePicker.getValue()) &&
                                sim_time.equals("Time "+(hour)%24+":"+(minute)%60+":"+ second%60)

                        );



                assertEquals(true, timeSuccessfullyUpdated);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });

    }
}
