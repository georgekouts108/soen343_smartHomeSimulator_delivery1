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
import org.assertj.core.util.TextFileWriter;
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
 * Class for testing use case #17
 */
public class TestUseCase17 extends ApplicationTest {

    /**
     * Test for setting the Away mode to on
     */
    @org.junit.Test
    public void setAwayMode() {
        SHSHelpers.setIs_away(true);
        Assert.assertTrue(SHSHelpers.isIs_away());
    }

    /**
     * Test for setting the Away mode to off
     */
    @org.junit.Test
    public void setAwayModeFail() {
        SHSHelpers.setIs_away(false);
        Assert.assertFalse(SHSHelpers.isIs_away());
    }
}
