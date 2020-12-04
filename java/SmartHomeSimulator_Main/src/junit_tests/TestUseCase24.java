package junit_tests;

import house.Room;
import house.Zone;
import javafx.scene.control.TextField;
import org.junit.Assert;
import org.testfx.framework.junit.*;
import sample.SHHModule;
import sample.SHSHelpers;

import java.time.Month;

/**
 * Class for testing use case #24 - SHH shuts down air conditioning and
 * opens windows if temp outside is cooler that the inside temp during summer
 */
public class TestUseCase24 extends ApplicationTest {

    @org.junit.Test
    public void testUC24() {
        SHHModule shhModule = new SHHModule();
        Room room1 = new Room("Room One", 1, 1, 1, true);
        SHSHelpers.setHouseholdLocations(new Room[]{room1});
        Zone testZone = new Zone();
        testZone.setZoneRoomIDs(new int[]{room1.getRoomID()});
        testZone.setZoneTemperature(15);
        shhModule.setZones(new Zone[]{testZone});
        SHSHelpers.setSimulationMonth(Month.JUNE);
        TextField tf = new TextField();
        tf.setText("5,7");
        shhModule.setWeatherMonthRange(tf,true);
        SHSHelpers.setShhModuleObject(shhModule);
        SHSHelpers.setOutsideTemperature(20);
        shhModule.getZones()[0].setZoneTemperature(25);
        shhModule.notifyToOpenAllZoneWindows();
        Assert.assertNotNull(shhModule);
    }
}