package sample;
import house.*;

public class SHHZoneThread {

    private boolean isRunning;
    private Thread mainThread;
    private Thread zoneTemperatureThread;
    private Thread zoneAwayModeWeatherThread;

    private Zone zone;

    public SHHZoneThread(Zone zone) {
        this.zone = zone;
        initializeAndStartMainThread();
        initializeAndStartTemperatureThread();
    }

    public void initializeAndStartTemperatureThread() {
        this.zoneTemperatureThread = new Thread(()->{
            try {
                while (true) {
                    try {
                        double outdoorTemp = Main.outsideTemperature;

                        for (int room = 0; room < zone.getZoneRoomIDs().length; room++) {
                            try {
                                for (int h = 0; h < Main.householdLocations.length; h++) {
                                    try {
                                        if (Main.householdLocations[h].getRoomID() == zone.getZoneRoomIDs()[room]) {
                                            double roomTemp = Main.householdLocations[h].getRoomTemperature();
                                            if ((outdoorTemp < roomTemp) && Main.shhModule.isSummer() && !SHSHelpers.isIs_away()) {
                                                try {
                                                    /**todo: fix repetition bug (implementation okay) */
                                                    Main.house.autoOpenWindows(Main.householdLocations[h]);
                                                }
                                                catch (Exception e){}
                                            }
                                            break;
                                        }
                                    } catch (Exception F) {
                                        System.out.println("EXCEPTION F: " + F.getMessage());
                                    }
                                }
                            } catch (Exception G) {
                                System.out.println("EXCEPTION G: " + G.getMessage());
                            }
                        }
                    } catch (Exception E) {
                        System.out.println("EXCEPTION E: " + E.getMessage());
                    } finally {
                        try {
                            Thread.sleep(1);
                        } catch (Exception H) {}
                    }
                }
            }catch (Exception e){}
        });

        this.zoneTemperatureThread.start();
    }
    public void initializeAndStartMainThread() {

        // as soon as a new zone is created, its corresponding thread will immediately start running
        this.mainThread = new Thread(()-> {
            boolean anyRoomsInZoneHaveTempInBounds = false;
            // this thread will run until the program is closed
            while (true) {
                try {
                    if (!Main.shhModule.isHAVCsystemActive()) {

                        // the temperature increases/decreases by 0.05 deg. Celsius
                        // per ms until it reaches the temperature outside the house.
                        if (this.zone.getZoneTemperature() != Main.outsideTemperature) {
                            double roundedTemp;
                            if (this.zone.getZoneTemperature() > Main.outsideTemperature) {
                                roundedTemp = (double) Math.round((this.zone.getZoneTemperature()-0.05) * 100)/100;
                            }
                            else {
                                roundedTemp = (double) Math.round((this.zone.getZoneTemperature()+0.05) * 100)/100;
                            }
                            try {
                                Main.shhModule.overrideZoneTemperature(this.zone.getZoneID(), roundedTemp);
                            }
                            catch (Exception e){}
                        }
                    }

                    /**todo: might need to change this block when the HAVC is working...
                     *  also add debug statements for future testing*/
                    else {
                        while (Main.shhModule.isHAVCsystemPaused()) {
                            int numOfRoomsWithTempOOB = 0;
                            for (int r = 0; r < this.zone.getZoneRoomIDs().length; r++) {
                                try {
                                    if (!Main.shhModule.isRoomTempInBetweenQuartDegreeBounds(this.zone.getZoneID(),
                                            this.zone.getZoneRoomIDs()[r])) {
                                        numOfRoomsWithTempOOB++;
                                    }
                                }
                                catch (Exception e){}
                            }

                            // when all rooms in the zone have temperatures within (temp +- 0.25)
                            // the HAVC may unpause itself
                            if (numOfRoomsWithTempOOB == 0) {
                                Main.shhModule.setHAVCsystemPaused(false);
                                break;
                            }
                        }
                    }
                }
                catch (Exception e){}
                finally {
                    try { Thread.sleep((long) (1000/Controller.simulationTimeSpeed)); } catch (Exception e){}
                }
            }
        });

        this.mainThread.start();
    }
}
