package house;

import sample.SHSHelpers;
import sample.*;
public class Zone {

    private static int ZONE_ID = 1;

    private double zoneTemperature;
    private int[] zoneRoomIDs;
    private int zoneID;

    public Zone() {
        this.zoneTemperature = SHSHelpers.getOutsideTemperature();
        this.zoneID = (ZONE_ID++);
        this.zoneRoomIDs = null;
    }

    public static int getStaticZoneId() {
        return ZONE_ID;
    }

    public int getZoneID() {
        return zoneID;
    }

    public double getZoneTemperature() {
        return zoneTemperature;
    }

    public void setZoneTemperature(double zoneTemperature) {
        this.zoneTemperature = zoneTemperature;
        overrideZoneRoomTemperaturesInHouse(this.zoneTemperature);
    }

    public int[] getZoneRoomIDs() {
        return zoneRoomIDs;
    }
    public void setZoneRoomIDs(int[] zoneRoomIDs) {
        this.zoneRoomIDs = zoneRoomIDs;
    }

    public void addRoomToZone(Room room) {
        int[] tempZoneRoomsArray;
        if (this.zoneRoomIDs == null) {
            tempZoneRoomsArray = new int[1];
            tempZoneRoomsArray[0] = room.getRoomID();
        }
        else {
            tempZoneRoomsArray = new int[this.zoneRoomIDs.length + 1];
            for (int z = 0; z < this.zoneRoomIDs.length; z++) {
                tempZoneRoomsArray[z] = this.zoneRoomIDs[z];
            }
            tempZoneRoomsArray[tempZoneRoomsArray.length-1] = room.getRoomID();
        }
        this.zoneRoomIDs = tempZoneRoomsArray;
    }

    public void overrideSpecificRoomTemperature(int roomID, double newTemperature) {
        for (int roomIndex = 0; roomIndex < this.zoneRoomIDs.length; roomIndex++) {
            if (this.zoneRoomIDs[roomIndex] == roomID) {
                for (int houseroomIndex = 0; houseroomIndex < Main.getHouseholdLocations().length; houseroomIndex++) {
                    if (Main.getHouseholdLocations()[houseroomIndex].getRoomID() == roomID) {
                        Controller.changeSpecificRoomTemperature(roomID, newTemperature);
                        break;
                    }
                }
                break;
            }
        }
    }

    public void overrideZoneRoomTemperaturesInHouse(double newTemperature) {
        for (int roomIndex = 0; roomIndex < this.zoneRoomIDs.length; roomIndex++) {
            int roomID = this.zoneRoomIDs[roomIndex];
            for (int houseroomIndex = 0; houseroomIndex < Main.getHouseholdLocations().length; houseroomIndex++) {
                if (Main.getHouseholdLocations()[houseroomIndex].getRoomID() == roomID) {
                    Controller.changeSpecificRoomTemperature(roomID, newTemperature);
                    break;
                }
            }
        }
    }
}
