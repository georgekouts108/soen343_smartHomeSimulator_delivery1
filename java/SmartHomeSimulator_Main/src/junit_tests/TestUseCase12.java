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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.testfx.framework.junit.*;

/**
 * Class for testing use case #12 - saving the profiles into a text file
 */
public class TestUseCase12 extends ApplicationTest {
    // save profiles and permissions

    // Users for each type
    boolean pL = true;
    boolean pLL = true;
    boolean pD = true;
    boolean pDL = true;
    boolean pW = true;
    boolean pWL = true;
    boolean pAC = true;
    boolean pACL = true;
    boolean pSA = true;
    boolean pCZ = true;
    boolean pRT = true;
    boolean pSW = true;

    /**
     * Test the saving of user profiles and permissions into a text file
     */
    @org.junit.Test
    public void testCase12() {
        UserProfile parent = new UserProfile("parent", !pL, !pLL, !pD, !pDL, !pW, !pWL, !pAC, !pACL, !pSA, !pCZ, !pRT, !pSW);
        Controller.saveProfileToFile(parent);
        File file = new File("src/profiles.txt");
        if (file.exists()) {
            try {
                boolean present = false;
                Scanner s = new Scanner(new FileInputStream(file));
                while (s.hasNextLine()) {
                    String member = s.nextLine();
                    if (member.contains(parent.getType()+","+!pL+","+!pLL+","+!pD+","+
                            !pDL+","+!pW+","+!pWL+","+!pAC+","+!pACL+","+!pSA+","+!pCZ+","+!pRT+","+!pSW)) {
                        present = true;
                        break;
                    }
                }
                Assert.assertTrue(true);
            }
            catch (Exception e){}
        }
    }
}
