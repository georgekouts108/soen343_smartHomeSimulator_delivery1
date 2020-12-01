package house;

import sample.SHSHelpers;
import sample.*;
public class Zone {

    private static int ZONE_ID = 1;

    private double zoneTemperature;
    private int[] zoneRoomIDs;
    private int zoneID;
    private ZoneTimePeriodSet timePeriodSet;

    public Zone() {
        this.zoneTemperature = SHSHelpers.getOutsideTemperature();
        this.zoneID = (ZONE_ID++);
        this.zoneRoomIDs = null;
    }

    public void initZoneTimePeriodSet() {
        this.timePeriodSet = new ZoneTimePeriodSet(this.zoneID);
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
        this.setZoneRoomIDs(this.zoneRoomIDs);
    }

    public void deleteRoomFromZone(int roomID) {
        int[] tempZoneRoomsArray = new int[this.zoneRoomIDs.length - 1];

        for (int z = 0; z < this.zoneRoomIDs.length; z++) {
            if (this.zoneRoomIDs[z] == roomID) {
                this.zoneRoomIDs[z] = -1;
            }
        }
        int tempIndex = 0;
        for (int t = 0; t < this.zoneRoomIDs.length; t++) {
            if (!(this.zoneRoomIDs[t] == -1)) {
                tempZoneRoomsArray[tempIndex++] = this.zoneRoomIDs[t];
            }
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

    public void setTimePeriodRangeAndTemperature(int lowerBound, int upperBound, int periodNumber, double zoneTemperature) {
        this.timePeriodSet.setPeriodHoursAndTemperature(lowerBound, upperBound, periodNumber, zoneTemperature);
    }
}
