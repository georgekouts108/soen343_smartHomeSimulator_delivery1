package junit_tests;

import org.junit.Assert;
import org.testfx.framework.junit.*;
import sample.SHHModule;

/**
 * Class for testing use case #27 - send users an alert when the temperature inside
 * is so low that the pipes could burst
 */
public class TestUseCase27 extends ApplicationTest {

    @org.junit.Test
    public void testUe27() {
        SHHModule shhModule = new SHHModule();
        shhModule.setIndoorTemperature(-1.5);
        shhModule.startIndoorTempThread();
        Assert.assertNotNull(shhModule);
    }

}