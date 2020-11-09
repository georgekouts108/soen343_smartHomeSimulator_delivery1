package junit_tests;

import house.*;
import javafx.application.Platform;
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
import org.assertj.core.error.AssertionErrorCreator;
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

import org.testfx.framework.junit.*;

/**
 * Test class for testing the simulation time speed change
 */
public class TestUseCase13 extends ApplicationTest {
    /**
     * Test the change of the simulation speed
     */
    @Test
    public void speedChangeTest(){
        DatePicker datePicker = new DatePicker();
        Label dateText = new Label();
        Label timeText = new Label();
        TextField hourField = new TextField("10");
        TextField minuteField = new TextField("24");
        float timeSpeed = 100;
        int stop = 100;


        try{
            int second = 0;
            int minute;
            int hour;
            minute = Integer.parseInt(minuteField.getText());
            hour = Integer.parseInt(hourField.getText());
            for(int i = 0; i < stop;i++){
                Calendar cal = new GregorianCalendar();
                Platform.runLater(()->dateText.setText(String.valueOf(datePicker.getValue())));
                if (i == 0) {
                    second = 0;
                    int finalSecond = second;
                    int finalHour = hour;
                    int finalMinute = minute;
                    Platform.runLater(()->timeText.setText("Time "+(finalHour)%24+":"+(finalMinute)%60+":"+ finalSecond%60));
                    Thread.sleep((long) (1000/timeSpeed));
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
                    Platform.runLater(()->timeText.setText("Time "+(finalHour%24)+":"+(finalMinute%60)+":"+ finalSecond%60));
                    Thread.sleep((long) (1000/timeSpeed));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
