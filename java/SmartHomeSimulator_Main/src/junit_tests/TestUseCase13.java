package junit_tests;

import house.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
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
import java.util.Scanner;

import org.testfx.framework.junit.*;

/**
 * Test class for testing the opening and closing of doors, windows, and lights
 */
public class TestUseCase13 extends ApplicationTest {
    // SHC unit testing
    boolean awaymode = true;

    /**
     * Test case 1 for controlling lights
     */
    @org.junit.Test
    public void LightControl(){
        try{
            House house = new House(".\\src\\housetest_junit.txt");
            Room room = house.getRooms()[0];
            if (awaymode){
                for(int i = 0; i < room.getLightCollection().length; i++){
                    room.getLightCollection()[i].setState(false);
                }
                Light testlight = room.getLightCollection()[0];
                testlight.setState(true);
                Assert.assertTrue(testlight.getState());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Test case 2 for controlling lights
     */
    @org.junit.Test
    public void LightControlFail(){
        try{
            House house = new House(".\\src\\housetest_junit.txt");
            Room room = house.getRooms()[0];
            if (awaymode){
                for(int i = 0; i < room.getLightCollection().length; i++){
                    room.getLightCollection()[i].setState(false);
                }
                Light testlight = room.getLightCollection()[0];
                testlight.setState(false);
                Assert.assertFalse(testlight.getState());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Test case 1 for controlling windows
     */
    @org.junit.Test
    public void WindowControl(){
        try{
            House house = new House(".\\src\\housetest_junit.txt");
            Room room = house.getRooms()[0];
            if (awaymode) {
                for (int i = 0; i < room.getWindowCollection().length; i++) {
                    room.getWindowCollection()[i].setState(false);
                }
                utilities.Window testwindow = room.getWindowCollection()[0];
                testwindow.setState(true);
                Assert.assertTrue(testwindow.getState());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Test case 2 for controlling windows
     */
    @org.junit.Test
    public void WindowControlFail(){
        try{
            House house = new House(".\\src\\housetest_junit.txt");
            Room room = house.getRooms()[0];
            if (awaymode) {
                for (int i = 0; i < room.getWindowCollection().length; i++) {
                    room.getWindowCollection()[i].setState(false);
                }
                utilities.Window testwindow = room.getWindowCollection()[0];
                testwindow.setState(false);
                Assert.assertFalse(testwindow.getState());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Test case for controlling doors
     */
    @org.junit.Test
    public void DoorControl(){
        try{
            House house = new House(".\\src\\housetest_junit.txt");
            Room room = house.getRooms()[0];
            if (awaymode) {
                for (int i = 0; i < room.getDoorCollection().length; i++) {
                    room.getDoorCollection()[i].setState(false);
                }
                utilities.Door testdoor = room.getDoorCollection()[0];
                testdoor.setState(true);
                Assert.assertTrue(testdoor.getState());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Test case 2 for controlling doors
     */
    @org.junit.Test
    public void DoorControlFail(){
        try{
            House house = new House(".\\src\\housetest_junit.txt");
            Room room = house.getRooms()[0];
            if (awaymode) {
                for (int i = 0; i < room.getDoorCollection().length; i++) {
                    room.getDoorCollection()[i].setState(false);
                }
                utilities.Door testdoor = room.getDoorCollection()[0];
                testdoor.setState(false);
                Assert.assertFalse(testdoor.getState());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
