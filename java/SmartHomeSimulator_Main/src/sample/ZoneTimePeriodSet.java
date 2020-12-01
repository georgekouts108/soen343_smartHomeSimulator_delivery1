package sample;
import house.*;

public class ZoneTimePeriodSet {

    private int zoneID;
    private double temperaturePeriod1;
    private double temperaturePeriod2;
    private double temperaturePeriod3;

    private String hoursPeriod1;
    private String hoursPeriod2;
    private String hoursPeriod3;
    private String[] timePeriods;

    public ZoneTimePeriodSet(int zoneID) {
        this.zoneID = zoneID;
        this.hoursPeriod1 = "";
        this.hoursPeriod2 = "";
        this.hoursPeriod3 = "";
        this.timePeriods = new String[]{this.hoursPeriod1, this.hoursPeriod2, this.hoursPeriod3};

        this.temperaturePeriod1 = getZoneTemperature();
        this.temperaturePeriod2 = getZoneTemperature();
        this.temperaturePeriod3 = getZoneTemperature();
    }

    public double getZoneTemperature() {
        double temp = 0;
        for (int z = 0; z < Main.shhModule.getZones().length; z++) {
            try {
                if (Main.shhModule.getZones()[z].getZoneID()==this.zoneID) {
                    temp = Main.shhModule.getZones()[z].getZoneTemperature();
                    break;
                }
            }
            catch (Exception e){}
        }
        return temp;
    }

    public void setPeriodHoursAndTemperature(int lowerBound, int upperBound, int periodNumber, double periodTemperature) {
        switch (periodNumber - 1) {
            case 0:
                temperaturePeriod1 = periodTemperature;
                hoursPeriod1 = lowerBound+","+upperBound;
                this.timePeriods[0] = hoursPeriod1;
                break;
            case 1:
                temperaturePeriod2 = periodTemperature;
                hoursPeriod2 = lowerBound+","+upperBound;
                this.timePeriods[1] = hoursPeriod2;
                break;
            case 2:
                temperaturePeriod3 = periodTemperature;
                hoursPeriod3 = lowerBound+","+upperBound;
                this.timePeriods[2] = hoursPeriod3;
                break;
        }
    }
}
