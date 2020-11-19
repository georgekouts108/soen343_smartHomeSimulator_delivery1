package sample;
import house.*;

public class SHHZoneThread {

    private boolean isRunning;
    private Thread mainThread;
    private Thread[] zoneRoomThreads;
    private Zone zone;

    public SHHZoneThread(Zone zone) {
        this.zone = zone;
        this.isRunning = false;
        this.zoneRoomThreads = new Thread[zone.getZoneRoomIDs().length];
        initializeMainThread();
        this.mainThread.start();
        //initializeAndStartZoneRoomThreads();
    }

    public void initializeMainThread() {

        // as soon as a new zone is created, its corresponding thread will immediately start running
        this.mainThread = new Thread(()-> {
            System.out.println("THREAD DEBUG 1 -- main thread started");
            // this thread will run until the program is closed
            while (true) {
                try {
                    this.isRunning = false;

                    // if HAVC is stopped, the temperature increases/decreases 0.05 deg. Celsius
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
                catch (Exception e){}
                finally {
                    /**todo: the time should be controlled by the simulation time */
                    try {Thread.sleep(1000); }catch (Exception e){}// Appendix D says every 1 ms ???
                }
            }
        });
    }
}
