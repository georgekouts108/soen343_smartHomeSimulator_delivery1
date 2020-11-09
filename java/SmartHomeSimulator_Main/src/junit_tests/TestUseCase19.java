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
 * Test for notifying users of motion detectors triggered during away mode
 */
public class TestUseCase19 extends ApplicationTest {

    /**
     * Test the notification to users when motion detectors trigger during Away mode
     */
    @org.junit.Test
    public void motionDetectorTriggered(){
        try{
            House house = new House(".\\src\\housetest_junit.txt");

            SHSHelpers.setIs_away(true);
            SHPModule shp = new SHPModule();
            house.getRooms()[0].getMd().setState(true);

            boolean MDON = false;
            for (int r = 0; r < house.getRooms().length; r++) {
                if (house.getRooms()[r].getMd().getState()) {
                    MDON = true;
                    break;
                }
            }

            if (SHSHelpers.isIs_away()) {
                if (MDON) {
                    Controller.appendMessageToConsole("CRITICAL [SHP]: One or more motion detectors are illegitimately triggered");
                    shp.startOrStopThread(true);
                }
                else {
                    shp.startOrStopThread(false);
                    Controller.appendMessageToConsole("SHP -- No more M.D's are illegitimately triggered");
                    Controller.appendMessageToConsole("Alarm deactivated.");
                }
            }

        } catch (Exception e){ }
    }
}
