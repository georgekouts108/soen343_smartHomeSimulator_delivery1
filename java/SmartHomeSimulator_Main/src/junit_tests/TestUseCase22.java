package junit_tests;

import house.Room;
import house.Zone;
import org.junit.Assert;
import org.testfx.framework.junit.*;
import sample.SHHModule;
import sample.SHSHelpers;

/**
 * Class for testing use case #22 - accept desired temperature settings
 * for each zone, for 1-3 periods in the day
 */
public class TestUseCase22 extends ApplicationTest {

    @org.junit.Test
    public void testUC22() {

        SHHModule shhModule = new SHHModule();
        Room room1 = new Room("Room One", 1, 2, 1, true);
        SHSHelpers.setHouseholdLocations(new Room[]{room1});
        Zone testZone = new Zone();
        testZone.setZoneRoomIDs(new int[]{room1.getRoomID()});
        shhModule.setZones(new Zone[]{testZone});
        SHSHelpers.setShhModuleObject(shhModule);
        testZone.setZoneTemperature(15);
        testZone.initZoneTimePeriodSet();

        testZone.setTimePeriodRangeAndTemperature(0, 8, 1, 17);
        testZone.setTimePeriodRangeAndTemperature(9, 16, 2, 19);
        testZone.setTimePeriodRangeAndTemperature(17, 23, 3, 21);

        Assert.assertEquals(17, (int) testZone.getPeriodTemperature(1));
        Assert.assertEquals(19, (int) testZone.getPeriodTemperature(2));
        Assert.assertEquals(21, (int) testZone.getPeriodTemperature(3));
    }

}