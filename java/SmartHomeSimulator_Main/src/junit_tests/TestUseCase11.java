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
    boolean pL = true;
    boolean pLL = true;
    boolean pD = true;
    boolean pDL = true;
    boolean pW = true;
    boolean pWL = true;
    boolean pAC = true;
    boolean pACL = true;


    /**todo: implement*/
    // Permissions testing

    /**
     * Test the permission for users to open windows
     */
    @org.junit.Test
    public void WindowPermissions(){
        UserProfile parent = new UserProfile("parent", pL, pLL, pD, pDL, pW, pWL, pAC, pACL);
        UserProfile child = new UserProfile("child", pL, pLL, pD, pDL, pW, pWL, pAC, pACL);

        Assert.assertTrue(parent.getPermWindows());
        Assert.assertTrue(parent.getPermWindowsLocation());
        Assert.assertTrue(child.getPermWindows());
        Assert.assertTrue(child.getPermWindowsLocation());
    }

    /**
     * Test the denial of users' permission to open windows
     */
    @org.junit.Test
    public void WindowPermissionsFail(){
        UserProfile parent = new UserProfile("parent", !pL, !pLL, !pD, !pDL, !pW, !pWL, !pAC, !pACL);
        UserProfile child = new UserProfile("child", !pL, !pLL, !pD, !pDL, !pW, !pWL, !pAC, !pACL);

        Assert.assertFalse(parent.getPermWindowsLocation());
        Assert.assertFalse(parent.getPermWindows());
        Assert.assertFalse(child.getPermWindowsLocation());
        Assert.assertFalse(child.getPermWindows());
    }

    /**
     * Test the permission for users to open doors
     */
    @org.junit.Test
    public void DoorPermissions(){
        UserProfile parent = new UserProfile("parent", pL, pLL, pD, pDL, pW, pWL, pAC, pACL);
        UserProfile child = new UserProfile("child", pL, pLL, pD, pDL, pW, pWL, pAC, pACL);

        Assert.assertTrue(parent.getPermDoors());
        Assert.assertTrue(parent.getPermDoorsLocation());
        Assert.assertTrue(child.getPermWindows());
        Assert.assertTrue(child.getPermWindowsLocation());
    }

    /**
     * Test the denial of users' permission to open doors
     */
    @org.junit.Test
    public void DoorPermissionsFail(){
        UserProfile parent = new UserProfile("parent", !pL, !pLL, !pD, !pDL, !pW, !pWL, !pAC, !pACL);
        UserProfile child = new UserProfile("child", !pL, !pLL, !pD, !pDL, !pW, !pWL, !pAC, !pACL);

        Assert.assertFalse(parent.getPermDoors());
        Assert.assertFalse(parent.getPermDoorsLocation());
        Assert.assertFalse(child.getPermDoors());
        Assert.assertFalse(child.getPermDoorsLocation());
    }

    /**
     * Test the permission for users to open lights
     */
    @org.junit.Test
    public void LightPermissions(){
        UserProfile parent = new UserProfile("parent", pL, pLL, pD, pDL, pW, pWL, pAC, pACL);
        UserProfile child = new UserProfile("child", pL, pLL, pD, pDL, pW, pWL, pAC, pACL);

        Assert.assertTrue(parent.getPermLights());
        Assert.assertTrue(parent.getPermLightsLocation());
        Assert.assertTrue(child.getPermLights());
        Assert.assertTrue(child.getPermLightsLocation());
    }

    /**
     * Test the denial of users' permission to open lights
     */
    @org.junit.Test
    public void LightPermissionsFail(){
        UserProfile parent = new UserProfile("parent", !pL, !pLL, !pD, !pDL, !pW, !pWL, !pAC, !pACL);
        UserProfile child = new UserProfile("child", !pL, !pLL, !pD, !pDL, !pW, !pWL, !pAC, !pACL);

        Assert.assertFalse(parent.getPermLights());
        Assert.assertFalse(parent.getPermLightsLocation());
        Assert.assertFalse(child.getPermLights());
        Assert.assertFalse(child.getPermLightsLocation());
    }

}
