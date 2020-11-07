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

public class TestUseCase11 extends ApplicationTest {
    // Users for each type
    private UserProfile child = new UserProfile("child");
    private UserProfile parent = new UserProfile("parent");
    private UserProfile guest = new UserProfile("guest");
    private UserProfile stranger = new UserProfile("stranger");

    /**todo: implement*/
    // Permissions testing
    public void WindowPermissions(){


    }
    public void WindowPermissionsFail(){

    }
    public void DoorPermissions(){

    }
    public void DoorPermissionsFail(){

    }
    public void LightPermissions(){

    }
    public void LightPermissionsFail(){

    }

}
