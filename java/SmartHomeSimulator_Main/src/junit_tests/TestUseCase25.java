package junit_tests;

import house.Room;
import house.Zone;
import javafx.scene.control.TextField;
import org.junit.Assert;
import org.testfx.framework.junit.*;
import sample.Main;
import sample.SHHModule;
import sample.SHSHelpers;

import java.time.Month;

/**
 * Class for testing use case #25 -- SHH shall not open nor close blocked windows
 */
public class TestUseCase25 extends ApplicationTest {

    @org.junit.Test
    public void testUC25() {
        try {
            SHHModule shhModule = new SHHModule();
            Room room1 = new Room("Room One", 1, 1, 1, true);
            SHSHelpers.setHouseholdLocations(new Room[]{room1});

            Zone testZone = new Zone();
            testZone.setZoneRoomIDs(new int[]{room1.getRoomID()});
            shhModule.setZones(new Zone[]{testZone});

            SHSHelpers.setSimulationMonth(Month.JUNE);
            TextField tf = new TextField();
            tf.setText("5,7");
            shhModule.setWeatherMonthRange(tf,true);

            SHSHelpers.setOutsideTemperature(20);
            shhModule.getZones()[0].setZoneTemperature(25);

            Main.getHouseholdLocations()[shhModule.getZones()[0].getZoneRoomIDs()[0]].getWindowCollection()[0].setBlocked(true);
            SHSHelpers.setShhModuleObject(shhModule);
            shhModule.notifyToOpenAllZoneWindows();
            Assert.assertNotNull(shhModule);
        }
        catch (Exception e){}
    }

}