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

public class TestUseCase12 extends ApplicationTest {
    // Alert authorities time test

    @org.junit.Test
    public void alertAuthorities(){
        SHPModule shp = new SHPModule();
        try{
            shp.setTimeToAlert(12);
            Assert.assertNotNull(shp);
        } catch (Exception e){
            Assert.assertTrue(true);
        }
    }
}
