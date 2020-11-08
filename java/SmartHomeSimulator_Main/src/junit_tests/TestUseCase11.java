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
 * Class for testing use case #11
 */
public class TestUseCase11 extends ApplicationTest {
    // Users for each type
//    private UserProfile child = new UserProfile("child");
//    private UserProfile parent = new UserProfile("parent");
//    private UserProfile guest = new UserProfile("guest");
//    private UserProfile stranger = new UserProfile("stranger");

    /**todo: implement*/
    // Permissions testing

    /**
     * Test the permission for users to open windows
     */
    @org.junit.Test
    public void WindowPermissions(){

    }

    /**
     * Test the denial of users' permission to open windows
     */
    @org.junit.Test
    public void WindowPermissionsFail(){

    }

    /**
     * Test the permission for users to open doors
     */
    @org.junit.Test
    public void DoorPermissions(){

    }

    /**
     * Test the denial of users' permission to open doors
     */
    @org.junit.Test
    public void DoorPermissionsFail(){

    }

    /**
     * Test the permission for users to open lights
     */
    @org.junit.Test
    public void LightPermissions(){

    }

    /**
     * Test the denial of users' permission to open lights
     */
    @org.junit.Test
    public void LightPermissionsFail(){

    }

}
