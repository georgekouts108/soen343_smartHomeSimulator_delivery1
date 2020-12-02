package sample;
import house.*;

/**
 * Class for zone time periods
 */
public class ZoneTimePeriodSet {

    private int zoneID;
    private double temperaturePeriod1;
    private double temperaturePeriod2;
    private double temperaturePeriod3;

    private String hoursPeriod1;
    private String hoursPeriod2;
    private String hoursPeriod3;
    private String[] timePeriods;

    /**
     * Constructor for a Zone time period set
     * @param zoneID
     */
    public ZoneTimePeriodSet(int zoneID) {
        this.zoneID = zoneID;
        this.hoursPeriod1 = "";
        this.hoursPeriod2 = "";
        this.hoursPeriod3 = "";
        this.timePeriods = new String[]{this.hoursPeriod1, this.hoursPeriod2, this.hoursPeriod3};

        this.temperaturePeriod1 = getZoneTemperature();
        this.temperaturePeriod2 = getZoneTemperature();
        this.temperaturePeriod3 = getZoneTemperature();

        triggerTimePeriodThread();
    }

    /**
     * Return a zone's temperature
     * @return
     */
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

    /**
     * return the hour range for a specific time period
     * @param period
     * @return
     */
    public int[] getPeriodHours(int period) {
        int[] hours = null;

        switch (period - 1) {
            case 0:
                String[] period1bounds = null;
                if (!this.hoursPeriod1.equals("")) { period1bounds = this.hoursPeriod1.split(","); }
                if (!(period1bounds == null)) {
                    int p1_lower = Integer.parseInt(period1bounds[0]);
                    int p1_upper = Integer.parseInt(period1bounds[1]);
                    hours = new int[]{p1_lower, p1_upper};
                }
                break;
            case 1:
                String[] period2bounds = null;
                if (!this.hoursPeriod2.equals("")) { period2bounds = this.hoursPeriod2.split(","); }
                if (!(period2bounds == null)) {
                    int p2_lower = Integer.parseInt(period2bounds[0]);
                    int p2_upper = Integer.parseInt(period2bounds[1]);
                    hours = new int[]{p2_lower, p2_upper};
                }
                break;
            case 2:
                String[] period3bounds = null;
                if (!this.hoursPeriod3.equals("")) { period3bounds = this.hoursPeriod3.split(","); }
                if (!(period3bounds == null)) {
                    int p3_lower = Integer.parseInt(period3bounds[0]);
                    int p3_upper = Integer.parseInt(period3bounds[1]);
                    hours = new int[]{p3_lower, p3_upper};
                }
                break;
        }
        return hours;
    }

    /**
     * get the temperature which a zone is set to for a specific period
     * @param period
     * @return
     */
    public double getPeriodTemperature(int period) {
        double temp = 0;
        switch (period - 1) {
            case 0:
                temp = this.temperaturePeriod1;
                break;
            case 1:
                temp = this.temperaturePeriod2;
                break;
            case 2:
                temp = this.temperaturePeriod3;
                break;
        }
        return temp;
    }

    /**
     * mutate a zone's temperature preference along with the period's hour bounds for automatically setting it
     * @param lowerBound
     * @param upperBound
     * @param periodNumber
     * @param periodTemperature
     */
    public void setPeriodHoursAndTemperature(int lowerBound, int upperBound, int periodNumber, double periodTemperature) {

        if (upperBound > 23 || lowerBound > 23 || upperBound < 0 || lowerBound < 0) {
            return;
        }

        int p1_lower = 0, p1_upper = 0;
        String[] period1bounds = null;
        if (!this.hoursPeriod1.equals("")) { period1bounds = this.hoursPeriod1.split(","); }
        if (!(period1bounds == null)) {
            p1_lower = Integer.parseInt(period1bounds[0]);
            p1_upper = Integer.parseInt(period1bounds[1]);
        }

        int p2_lower = 0, p2_upper = 0;
        String[] period2bounds = null;
        if (!this.hoursPeriod2.equals("")) { period2bounds = this.hoursPeriod2.split(","); }
        if (!(period2bounds == null)) {
            p2_lower = Integer.parseInt(period2bounds[0]);
            p2_upper = Integer.parseInt(period2bounds[1]);
        }

        int p3_lower = 0, p3_upper = 0;
        String[] period3bounds = null;
        if (!this.hoursPeriod3.equals("")) { period3bounds = this.hoursPeriod3.split(","); }
        if (!(period3bounds == null)) {
            p3_lower = Integer.parseInt(period3bounds[0]);
            p3_upper = Integer.parseInt(period3bounds[1]);
        }

        boolean overlapsWithP1 = false, overlapsWithP2 = false, overlapsWithP3 = false;

        switch (periodNumber - 1) {
            case 0:
                if (!(period2bounds==null)) { overlapsWithP2 = doesTimeRangeOverlapWithOther(lowerBound, upperBound, p2_lower, p2_upper); }
                if (!(period3bounds==null)) { overlapsWithP3 = doesTimeRangeOverlapWithOther(lowerBound, upperBound, p3_lower, p3_upper); }

                if (!overlapsWithP2 && !overlapsWithP3) {
                    this.temperaturePeriod1 = periodTemperature;
                    this.hoursPeriod1 = lowerBound+","+upperBound;
                    this.timePeriods[0] = hoursPeriod1;
                }
                else {
                    System.out.println("Overlap error");
                }
                break;
            case 1:
                if (!(period1bounds==null)) { overlapsWithP1 = doesTimeRangeOverlapWithOther(lowerBound, upperBound, p1_lower, p1_upper); }
                if (!(period3bounds==null)) { overlapsWithP3 = doesTimeRangeOverlapWithOther(lowerBound, upperBound, p3_lower, p3_upper); }

                if (!overlapsWithP1 && !overlapsWithP3) {
                    this.temperaturePeriod2 = periodTemperature;
                    this.hoursPeriod2 = lowerBound+","+upperBound;
                    this.timePeriods[1] = hoursPeriod2;
                }
                else {
                    System.out.println("Overlap error");
                }
                break;
            case 2:
                if (!(period1bounds==null)) { overlapsWithP1 = doesTimeRangeOverlapWithOther(lowerBound, upperBound, p1_lower, p1_upper); }
                if (!(period2bounds==null)) { overlapsWithP2 = doesTimeRangeOverlapWithOther(lowerBound, upperBound, p2_lower, p2_upper); }
                if (!overlapsWithP1 && !overlapsWithP2) {
                    this.temperaturePeriod3 = periodTemperature;
                    this.hoursPeriod3 = lowerBound+","+upperBound;
                    this.timePeriods[2] = hoursPeriod3;
                }
                else {
                    System.out.println("Overlap error");
                }
                break;
        }
    }

    /**
     * Start a continuous thread that will automatically update zone temperatures based on the current simulation time
     */
    public void triggerTimePeriodThread() {
        new Thread(()->{
            while (true) {
                try {
                    String[] period1bounds = null;
                    String[] period2bounds = null;
                    String[] period3bounds = null;

                    if (!this.hoursPeriod1.equals("")) { period1bounds = this.hoursPeriod1.split(","); }
                    if (!this.hoursPeriod2.equals("")) { period2bounds = this.hoursPeriod2.split(","); }
                    if (!this.hoursPeriod3.equals("")) { period3bounds = this.hoursPeriod3.split(","); }

                    if (!(period1bounds == null)) {
                        int p1_lower = Integer.parseInt(period1bounds[0]);
                        int p1_upper = Integer.parseInt(period1bounds[1]);
                        if (isSimHourWithinRange(p1_lower, p1_upper)) {
                            try {
                                Main.shhModule.overrideZoneTemperature(this.zoneID, temperaturePeriod1);
                            }
                            catch (Exception e){}
                        }
                    }

                    if (!(period2bounds == null)) {
                        int p2_lower = Integer.parseInt(period2bounds[0]);
                        int p2_upper = Integer.parseInt(period2bounds[1]);
                        if (isSimHourWithinRange(p2_lower, p2_upper)) {
                            try {
                                Main.shhModule.overrideZoneTemperature(this.zoneID, temperaturePeriod2);
                            }
                            catch (Exception e){}
                        }
                    }

                    if (!(period3bounds == null)) {
                        int p3_lower = Integer.parseInt(period3bounds[0]);
                        int p3_upper = Integer.parseInt(period3bounds[1]);
                        if (isSimHourWithinRange(p3_lower, p3_upper)) {
                            try {
                                Main.shhModule.overrideZoneTemperature(this.zoneID, temperaturePeriod3);
                            }
                            catch (Exception e){}
                        }
                    }
                }
                catch (Exception e){}
                finally {
                    try {Thread.sleep( 1000 / (long) Controller.getSimulationTimeSpeed());}catch (Exception e){}
                }
            }
        }).start();
    }

    /**
     * checks if the hour of the current simulation time is within the bounds of a specific period
     * @param lowerHourBound
     * @param upperHourBound
     * @return
     */
    public boolean isSimHourWithinRange(int lowerHourBound, int upperHourBound) {
        int simulationHour = Controller.currentSimulationHour;
        return (simulationHour >= lowerHourBound && simulationHour <= upperHourBound);
    }

    /**
     * compares a configured hour time range with another to check for any overlaps
     * @param inputLowerHour
     * @param inputUpperHour
     * @param otherLowerHour
     * @param otherUpperHour
     * @return
     */
    public boolean doesTimeRangeOverlapWithOther(int inputLowerHour, int inputUpperHour, int otherLowerHour, int otherUpperHour) {
        boolean overlaps = false;

        int inputAbsoluteDiff = inputLowerHour - inputUpperHour;
        inputAbsoluteDiff = ( (inputAbsoluteDiff >= 0) ? inputAbsoluteDiff : inputAbsoluteDiff*-1);

        int otherAbsoluteDiff = otherLowerHour - otherUpperHour;
        otherAbsoluteDiff = ( (otherAbsoluteDiff >= 0) ? otherAbsoluteDiff : otherAbsoluteDiff*-1);

        int[] otherHourRange = new int[24 - otherAbsoluteDiff + 1];
        int[] inputHourRange = new int[24 - inputAbsoluteDiff + 1];

        /**todo: fix*/
        int currentOtherHour = otherLowerHour;
        for (int i = 0; i < otherHourRange.length; i++) {
            otherHourRange[i] = (currentOtherHour % 24);
            currentOtherHour = (currentOtherHour + 1) % 24;
        }

        int currentInputHour = inputLowerHour;
        for (int i = 0; i < inputHourRange.length; i++) {
            inputHourRange[i] = (inputLowerHour % 24);
            currentInputHour = (currentInputHour + 1) % 24;
        }

        for (int inputHours = 0; inputHours < inputHourRange.length; inputHours++) {
            for (int otherHours = 0; otherHours < otherHourRange.length; otherHours++) {
                if (otherHourRange[otherHours] == inputHourRange[inputHours]) {
                    overlaps = true;
                    break;
                }
            }
            if (overlaps) {
                break;
            }
        }

        return overlaps;
    }
}
