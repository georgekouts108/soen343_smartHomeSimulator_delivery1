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
import org.testfx.framework.junit.*;

/**
 * Unit test class for testing the Auto mode feature
 */
public class TestUseCase16 extends ApplicationTest {

    /**
     * Set the Auto mode to on
     */
    @org.junit.Test
    public void testSetAutoMode() throws FileNotFoundException {
        House house = new House("src/housetest_junit.txt");
        Room room = house.getRooms()[0];
        room.setAutoMode(true);
        Assert.assertTrue(room.getIsAutoModeOn());
    }

    /**
     * Leave the Auto mode off
     */
    @org.junit.Test
    public void testSetAutoModeFail() throws FileNotFoundException {
        House house = new House("src/housetest_junit.txt");
        Room room = house.getRooms()[0];
        Assert.assertFalse(room.getIsAutoModeOn());
    }


}
