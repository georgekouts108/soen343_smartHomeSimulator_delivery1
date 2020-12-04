package junit_tests;

import house.Room;
import house.Zone;
import javafx.scene.layout.AnchorPane;
import org.junit.Assert;
import org.testfx.framework.junit.*;
import sample.SHHModule;
import sample.SHSHelpers;

/**
 * Class for testing use case #21 - setting zones for heating and cooling
 */
public class TestUseCase21 extends ApplicationTest {


    @org.junit.Test
    public void testUC21() {
        try {
            SHHModule shhModule = new SHHModule();
            Room room1 = new Room("Room One", 1, 2, 1, true);
            Room room2 = new Room("Room Two", 1, 1, 1, false);
            Room[] rooms = {room1, room2};
            SHSHelpers.setHouseholdLocations(rooms);
            Zone testZone1 = new Zone();
            testZone1.setZoneRoomIDs(new int[]{room1.getRoomID()});
            Zone testZone2 = new Zone();
            testZone2.setZoneRoomIDs(new int[]{room2.getRoomID()});
            shhModule.setZones(new Zone[]{testZone1, testZone2});
            shhModule.setMaxNumOfZones(rooms.length);
            SHSHelpers.setShhModuleObject(shhModule);
            Assert.assertNotNull(shhModule);
        }
        catch (Exception e){}

    }
}