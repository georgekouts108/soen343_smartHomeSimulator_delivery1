package sample;
import house.*;

public class SHHZoneThread {

    private boolean isRunning;
    private Thread mainThread;
    private Thread[] zoneRoomThreads;
    private Zone zone;

    public SHHZoneThread(Zone zone) {
        this.zone = zone;
        this.zoneRoomThreads = new Thread[zone.getZoneRoomIDs().length];
        initializeAndStartMainThread();
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
