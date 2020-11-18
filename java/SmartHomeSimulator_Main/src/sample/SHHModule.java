package sample;
import house.*;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;


/**
 * SHH Class
 */
public class SHHModule extends Module {

    private final int MAX_NUMBER_OF_ZONES = 4;
    private int currentNumberOfZones;
    private double outdoorTemperature;
    private double winterTemperature;
    private double summerTemperature;
    private Zone[] zones;
    private boolean isWinter;
    private boolean isSummer;
    private boolean HAVCsystemActive;
    private Thread adjustToRoomTemperatureThread;
    private Thread adjustToDefaultTemperatureThread;

    /**
     * SHH Constructor
     */
    public SHHModule() {
        super();
        this.outdoorTemperature = Main.outsideTemperature;
        this.winterTemperature = 0;
        this.summerTemperature = 0;
        this.isWinter = false;
        this.isSummer = false;
        this.HAVCsystemActive = false;
        this.currentNumberOfZones = 0;
        this.zones = null;
    }

    /**
     * Generate a module by creating and returning a local AnchorPane
     * @return
     */
    @Override
    public AnchorPane generateModule() {

        Main.numberOfTimesSHHModuleCreated++;
        return null;
    }

    /**
     * Generate a module by populating an existing AnchorPane
     * @param pane
     * @return
     */
    @Override
    public AnchorPane generateModule(AnchorPane pane) {
        Label awayModeSHHLabel = new Label("Away mode: OFF");
        awayModeSHHLabel.setId("awayModeSHHLabel");

        Label outdoorTempSHHLabel = new Label("Outdoor temperature: "+Main.outsideTemperature+"°C");
        outdoorTempSHHLabel.setId("outdoorTempSHHLabel");
        outdoorTempSHHLabel.setTranslateY(20);

        Label winterTempSHHLabel = new Label("Winter temp (AWAY only): "+this.winterTemperature+"°C");
        winterTempSHHLabel.setId("winterTempSHHLabel");
        winterTempSHHLabel.setTranslateY(40);

        Label summerTempSHHLabel = new Label("Summer temp (AWAY only): "+this.summerTemperature+"°C");
        summerTempSHHLabel.setId("summerTempSHHLabel");
        summerTempSHHLabel.setTranslateY(60);

        Label maxNumOfZonesSHHLabel = new Label("Maximum number of zones allowed: "+MAX_NUMBER_OF_ZONES);
        maxNumOfZonesSHHLabel.setId("maxNumOfZonesLabel");
        maxNumOfZonesSHHLabel.setTranslateY(80);

        Label currentNumOfZonesSHHLabel = new Label("Current number of zones: "+this.currentNumberOfZones);
        currentNumOfZonesSHHLabel.setId("currentNumOfZonesLabel");
        currentNumOfZonesSHHLabel.setTranslateY(100);

        Line borderLine1 = new Line();
        borderLine1.setStartX(0); borderLine1.setEndX(500);
        borderLine1.setTranslateY(120);

        Label zonesLabel = new Label("Zones");
        zonesLabel.setId("zonesLabel");
        zonesLabel.setTranslateY(140);

        if (Main.numberOfTimesSHHModuleCreated==0) {
            pane.getChildren().addAll(awayModeSHHLabel, outdoorTempSHHLabel,
                    winterTempSHHLabel, summerTempSHHLabel, maxNumOfZonesSHHLabel,
                    currentNumOfZonesSHHLabel, borderLine1, zonesLabel);
        }

        Main.numberOfTimesSHHModuleCreated++;
        return pane;
    }

    public int getCurrentNumberOfZones() {
        return currentNumberOfZones;
    }

    public void incrementNumberOfZones() {
        try {
            if (this.currentNumberOfZones + 1 <= MAX_NUMBER_OF_ZONES) {
                this.currentNumberOfZones++;
            }
            else {
                throw new Exception("ERROR [SHH]: Can only have up to 4 Zones.");
            }
        }
        catch (Exception e){
            Controller.appendMessageToConsole(e.getMessage());
        }
    }
    public void decrementNumberOfZones() {
        try {
            if (this.currentNumberOfZones - 1 >= MAX_NUMBER_OF_ZONES) {
                this.currentNumberOfZones--;
            }
            else {
                throw new Exception("ERROR [SHH]: Number of Zones must be >= 0");
            }
        }
        catch (Exception e){
            Controller.appendMessageToConsole(e.getMessage());
        }
    }

    public double getOutdoorTemperature() {
        return outdoorTemperature;
    }
    public void setOutdoorTemperature(double outdoorTemperature) {
        this.outdoorTemperature = outdoorTemperature;
    }

    public double getWinterTemperature() {
        return winterTemperature;
    }
    public void setWinterTemperature(double winterTemperature) {
        this.winterTemperature = winterTemperature;
        changeSHHTempLabel("winterTempSHHLabel", winterTemperature);
    }

    public double getSummerTemperature() {
        return summerTemperature;
    }
    public void setSummerTemperature(double summerTemperature) {
        this.summerTemperature = summerTemperature;
        changeSHHTempLabel("summerTempSHHLabel", summerTemperature);
    }

    public Zone[] getZones() {
        return zones;
    }
    public void setZones(Zone[] zones) {
        this.zones = zones;
    }

    public boolean isWinter() {
        return isWinter;
    }
    public void setWinter(boolean winter) {
        isWinter = winter;
    }

    public boolean isSummer() {
        return isSummer;
    }
    public void setSummer(boolean summer) {
        isSummer = summer;
    }

    public boolean isHAVCsystemActive() {
        return HAVCsystemActive;
    }
    public void setHAVCsystemActive(boolean HAVCsystemActive) {
        this.HAVCsystemActive = HAVCsystemActive;
    }

    public void overrideZoneTemperature(int zoneID, double newTemp) {
        for (int z = 0; z < this.zones.length; z++) {
            if (this.zones[z].getZoneID() == zoneID) {
                this.zones[z].overrideZoneRoomTemperaturesInHouse(newTemp);
                Controller.appendMessageToConsole("SHH: Zone #"+zoneID+" temperature changed to "+newTemp+"°C");
                break;
            }
        }
    }
    public void overrideTempInSpecificRoomInZone(int zoneID, int roomID, double newTemp) {
        try {
            boolean roomIsInZone = false;
            for (int z = 0; z < this.zones.length; z++) {
                if (this.zones[z].getZoneID() == zoneID) {
                    for (int r = 0; r < this.zones[z].getZoneRoomIDs().length; r++) {
                        if (this.zones[z].getZoneRoomIDs()[r]==roomID) {
                            roomIsInZone = true;
                            break;
                        }
                    }

                    if (roomIsInZone) {
                        this.zones[z].overrideSpecificRoomTemperature(roomID, newTemp);
                        Controller.appendMessageToConsole("SHH: Room #"+roomID+" in Zone #"+zoneID+" temperature changed to "+newTemp+"°C");
                    }
                    else {
                        throw new Exception("ERROR [SHH]: Room #"+roomID+" is not in Zone "+zoneID);
                    }
                    break;
                }
            }
        }
        catch (Exception e){
            Controller.appendMessageToConsole(e.getMessage());
        }
    }

    public void setAllZoneTempsToSummerTemp() {
        for (int z = 0; z < this.zones.length; z++) {
            overrideZoneTemperature(this.zones[z].getZoneID(), this.summerTemperature);
        }
    }
    public void setAllZoneTempsToWinterTemp() {
        for (int z = 0; z < this.zones.length; z++) {
            overrideZoneTemperature(this.zones[z].getZoneID(), this.winterTemperature);
        }
    }

    public void changeSHHTempLabel(String labelID, double newTemp) {
        for (int e = 0; e < Main.SHH_MODULE.getChildren().size(); e++) {
            try {
                if (Main.SHH_MODULE.getChildren().get(e).getId().equals(labelID)) {
                    Label tempLabel = (Label) Main.SHH_MODULE.getChildren().get(e);
                    if (tempLabel.getText().contains("Summer")) {
                        tempLabel.setText("Summer temperature: "+newTemp+"°C");
                    }
                    else if (tempLabel.getText().contains("Winter")) {
                        tempLabel.setText("Winter temperature: "+newTemp+"°C");
                    }
                    else if (tempLabel.getText().contains("Outdoor")) {
                        tempLabel.setText("Outdoor temperature: "+newTemp+"°C");
                    }
                    Main.SHH_MODULE.getChildren().set(e, tempLabel);
                    break;
                }
            }
            catch (Exception ex){}
        }
    }
    public void changeSHHZoneCountLabel(int numOfZones) {
        for (int e = 0; e < Main.SHH_MODULE.getChildren().size(); e++) {
            try {
                if (Main.SHH_MODULE.getChildren().get(e).getId().equals("currentNumOfZonesLabel")) {
                    Label tempLabel = (Label) Main.SHH_MODULE.getChildren().get(e);
                    tempLabel.setText("Current number of zones: "+numOfZones);
                    Main.SHH_MODULE.getChildren().set(e, tempLabel);
                    break;
                }
            }
            catch (Exception ex){}
        }
    }
    public void changeSHHAwayModeLabel(boolean isAwayModeOn) {
        for (int e = 0; e < Main.SHH_MODULE.getChildren().size(); e++) {
            try {
                if (Main.SHH_MODULE.getChildren().get(e).getId().equals("awayModeSHHLabel")) {
                    Label tempLabel = (Label) Main.SHH_MODULE.getChildren().get(e);
                    if (isAwayModeOn) { tempLabel.setText("Away mode: ON"); }
                    else { tempLabel.setText("Away mode: OFF"); }
                    Main.SHH_MODULE.getChildren().set(e, tempLabel);
                    break;
                }
            }
            catch (Exception ex){}
        }
    }
}
