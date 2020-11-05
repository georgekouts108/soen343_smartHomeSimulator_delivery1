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

    @Test
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
