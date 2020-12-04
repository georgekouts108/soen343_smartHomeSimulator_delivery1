package junit_tests;

import org.junit.Assert;
import org.testfx.framework.junit.*;
import sample.SHHModule;
import sample.SHSHelpers;

/**
 * Class for testing use case #26 - SHH detects when AWAY mode has been turned on
 */
public class TestUseCase26 extends ApplicationTest {

    @org.junit.Test
    public void testUC26() {
        SHHModule shhModule = new SHHModule();
        SHSHelpers.setIs_away(true);
        Assert.assertTrue(SHSHelpers.isIs_away());

        shhModule.notifySHHOFAwayMode();
        Assert.assertTrue(SHSHelpers.isIs_away());
    }

    @org.junit.Test
    public void testUC26Failure() {
        SHHModule shhModule = new SHHModule();
        SHSHelpers.setIs_away(false);
        Assert.assertFalse(SHSHelpers.isIs_away());

        shhModule.notifySHHOFAwayMode();
        Assert.assertFalse(SHSHelpers.isIs_away());
    }

}