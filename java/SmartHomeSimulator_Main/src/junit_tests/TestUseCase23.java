package junit_tests;

import house.Room;
import house.Zone;
import org.junit.Assert;
import org.testfx.framework.junit.*;
import sample.Main;
import sample.SHHModule;
import sample.SHSHelpers;

/**
 * Class for testing use case #23 - SHH displays current temperature of a room
 * when requesTed by a home's user
 */
public class TestUseCase23 extends ApplicationTest {

    @org.junit.Test
    public void testUC23() {
        SHHModule shhModule = new SHHModule();
        Room room1 = new Room("Room One", 1, 2, 1, true);
        SHSHelpers.setHouseholdLocations(new Room[]{room1});

        Zone testZone = new Zone();
        testZone.setZoneRoomIDs(new int[]{room1.getRoomID()});
        testZone.setZoneTemperature(10);
        shhModule.setZones(new Zone[]{testZone});

        SHSHelpers.setShhModuleObject(shhModule);

        int temp = (int) SHSHelpers.getShhModuleObject().getZones()[0].getZoneTemperature();
        Assert.assertEquals(10, temp);

        testZone.setZoneTemperature(18);
        shhModule.setZones(new Zone[]{testZone});

        SHSHelpers.setShhModuleObject(shhModule);

        int temp2 = (int) SHSHelpers.getShhModuleObject().getZones()[0].getZoneTemperature();
        Assert.assertEquals(18, temp2);
    }
}
