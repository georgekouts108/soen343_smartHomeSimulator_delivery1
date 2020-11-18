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

        /**TODO: render the SHH module to show the change in the zone*/
    }

    public void deleteRoomFromZone(Room room, int zoneID) {

        int[] tempZoneRoomsArray = new int[this.zoneRoomIDs.length - 1];

        for (int z = 0; z < this.zoneRoomIDs.length; z++) {
            if (this.zoneRoomIDs[z] == room.getRoomID()) {
                this.zoneRoomIDs[z] = -1;
                break;
            }
        }

        int tempIndex = 0;
        for (int t = 0; t < this.zoneRoomIDs.length; t++) {
            if (this.zoneRoomIDs[t] != -1) {
                tempZoneRoomsArray[tempIndex] = this.zoneRoomIDs[t];
                tempIndex++;
            }
        }

        this.zoneRoomIDs = tempZoneRoomsArray;

        /**TODO: render the SHH module to show the change in the zone*/
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
        //SHSHelpers.setHouseholdLocations(Main.getHouseholdLocations());
        /**TODO: render the SHH module to show the change in the zone*/
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
        //SHSHelpers.setHouseholdLocations(Main.getHouseholdLocations());
        /**TODO: render the SHH module to show the change in the zone*/
    }
}
