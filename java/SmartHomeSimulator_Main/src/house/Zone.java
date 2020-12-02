package house;
import sample.SHSHelpers;
import sample.*;

/**
 * Class for Zones
 */
public class Zone {

    private static int ZONE_ID = 1;
    private double zoneTemperature;
    private int[] zoneRoomIDs;
    private int zoneID;
    private ZoneTimePeriodSet timePeriodSet;

    /**
     * Zone constructor
     */
    public Zone() {
        this.zoneTemperature = SHSHelpers.getOutsideTemperature();
        this.zoneID = (ZONE_ID++);
        this.zoneRoomIDs = null;
    }

    /**
     * initializer for the zone's time period set
     */
    public void initZoneTimePeriodSet() {
        this.timePeriodSet = new ZoneTimePeriodSet(this.zoneID);
    }

    /**
     * return a zone's ID
     * @return
     */
    public int getZoneID() {
        return zoneID;
    }

    /**
     * return a zone's temperature
     * @return
     */
    public double getZoneTemperature() {
        return zoneTemperature;
    }

    /**
     * change a zone's temperature, which thus overrides all of its room's temperatures
     * @param zoneTemperature
     */
    public void setZoneTemperature(double zoneTemperature) {
        this.zoneTemperature = zoneTemperature;
        overrideZoneRoomTemperaturesInHouse(this.zoneTemperature);
    }

    /**
     * return the integer array of the zone's rooms' IDs
     * @return
     */
    public int[] getZoneRoomIDs() {
        return zoneRoomIDs;
    }

    /**
     * update the array of a zone's rooms' IDs
     * @param zoneRoomIDs
     */
    public void setZoneRoomIDs(int[] zoneRoomIDs) {
        this.zoneRoomIDs = zoneRoomIDs;
    }

    /**
     * add a Room to a zone
     * @param room
     */
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

    /**
     * delete a room from a zone
     * @param roomID
     */
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

    /**
     * override a specific room's temperature
     * @param roomID
     * @param newTemperature
     */
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

    /**
     * override the temperature of all rooms in a zone
     * @param newTemperature
     */
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

    /**
     * configure a zone's time period including its temperature for that period
     * @param lowerBound
     * @param upperBound
     * @param periodNumber
     * @param zoneTemperature
     */
    public void setTimePeriodRangeAndTemperature(int lowerBound, int upperBound, int periodNumber, double zoneTemperature) {
        this.timePeriodSet.setPeriodHoursAndTemperature(lowerBound, upperBound, periodNumber, zoneTemperature);
    }

    /**
     * return a two-cell integer array with the upper and lower hour bounds of a zone's specific time period
     * @param period
     * @return
     */
    public int[] getPeriodHours(int period) {
        return this.timePeriodSet.getPeriodHours(period);
    }

    /**
     * return a zone's temperature setting for one of its time periods
     * @param period
     * @return
     */
    public double getPeriodTemperature(int period) {
        return this.timePeriodSet.getPeriodTemperature(period);
    }
}
