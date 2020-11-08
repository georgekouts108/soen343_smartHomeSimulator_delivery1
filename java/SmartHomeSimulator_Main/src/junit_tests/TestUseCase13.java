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
            House house = new House("src/housetest_junit.txt");
            Room room = house.getRooms()[0];
            if (awaymode){
                for(int i = 0; i < room.getLightCollection().length; i++){
                    room.getLightCollection()[i].setState(false);
                }
                Light testlight = room.getLightCollection()[0];
                testlight.setState(true);
                Assert.assertTrue(testlight.getState());
                Assert.assertEquals(false, testlight.getState());
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
}
