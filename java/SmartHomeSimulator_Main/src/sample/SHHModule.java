package sample;
import house.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.Month;


/**
 * SHH Class
 */
public class SHHModule extends Module {

    private static Stage SHHZoneConfigStage;
    private static Scene SHHZoneConfigScene;
    private static AnchorPane SHHZoneConfigPane;
    private static int numberOfTimesZoneConfigSelected = 0;

    private int currentNumberOfZones;
    private double outdoorTemperature;
    private double winterTemperature;
    private double summerTemperature;
    private Zone[] zones;
    private boolean isWinter;
    private boolean isSummer;

    private SHHZoneThread[] zoneThreads;
    private boolean HAVCsystemActive;
    private boolean HAVCsystemPaused;
    private double TEMP_THRESHOLD;

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
        this.HAVCsystemPaused = false;
        this.currentNumberOfZones = 0;
        this.zones = null;
        this.zoneThreads = null;
        SHHZoneConfigPane = new AnchorPane();
        SHHZoneConfigScene = new Scene(SHHZoneConfigPane, 600,900);
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

        Label winterTempSHHLabel = new Label("Winter temp (AWAY mode only): "+this.winterTemperature+"°C");
        winterTempSHHLabel.setId("winterTempSHHLabel");
        winterTempSHHLabel.setTranslateY(40);

        Label summerTempSHHLabel = new Label("Summer temp (AWAY mode only): "+this.summerTemperature+"°C");
        summerTempSHHLabel.setId("summerTempSHHLabel");
        summerTempSHHLabel.setTranslateY(60);

        Label maxNumOfZonesSHHLabel = new Label("Maximum number of zones allowed: Unlimited");
        maxNumOfZonesSHHLabel.setId("maxNumOfZonesLabel");
        maxNumOfZonesSHHLabel.setTranslateY(80);

        Label currentNumOfZonesSHHLabel = new Label("Current number of zones: "+this.currentNumberOfZones);
        currentNumOfZonesSHHLabel.setId("currentNumOfZonesLabel");
        currentNumOfZonesSHHLabel.setTranslateY(100);

        Line borderLine1 = new Line();
        borderLine1.setStartX(0); borderLine1.setEndX(500);
        borderLine1.setTranslateY(120);

        Label nullHouseError = new Label("ERROR: House layout null");
        nullHouseError.setTranslateX(300); nullHouseError.setTranslateY(10);
        nullHouseError.setVisible(false);

        Button configZoneButton = new Button("Add or\nConfigure\nZones");
        configZoneButton.setId("configZoneButton");
        configZoneButton.setTranslateX(300); configZoneButton.setTranslateY(30);
        configZoneButton.setOnAction(e->{
            if (Main.householdLocations != null) {
                goToZoneConfigPage();
            }
            else {
                new Thread(()->{
                    nullHouseError.setVisible(true);
                    try {Thread.sleep(1500);}catch (Exception ex){}
                    nullHouseError.setVisible(false);
                }).start();
            }
        });

        Button configWinterSummerTemp = new Button("Configure\nWinter or\nSummer Temp.\nSettings");
        configWinterSummerTemp.setId("configSummerWeatherTempButton");
        configWinterSummerTemp.setTranslateX(380); configWinterSummerTemp.setTranslateY(30);
        configWinterSummerTemp.setOnAction(e-> popupWinterSummerTempSettingsStage(pane));
        
        Label zonesLabel = new Label("Zones");
        zonesLabel.setId("zonesLabel");
        zonesLabel.setTranslateY(130);

        if (Main.numberOfTimesSHHModuleCreated==0) {
            pane.getChildren().addAll(awayModeSHHLabel, outdoorTempSHHLabel,
                    winterTempSHHLabel, summerTempSHHLabel, maxNumOfZonesSHHLabel,
                    currentNumOfZonesSHHLabel, borderLine1, configZoneButton, zonesLabel, nullHouseError, configWinterSummerTemp);
        }

        Main.numberOfTimesSHHModuleCreated++;
        return pane;
    }

    public void configureWinterSummerMonths() {
        Stage tempStage = new Stage();
        tempStage.setTitle("Winter/Summer Temp Settings");
        tempStage.setResizable(false);
        AnchorPane tempPane = new AnchorPane();

        Label setWinterMonthRange = new Label("Winter Months: ");
        setWinterMonthRange.setTranslateX(50); setWinterMonthRange.setTranslateY(80);
        TextField winterMonthsTempTextField = new TextField();
        winterMonthsTempTextField.setTranslateY(100); winterMonthsTempTextField.setTranslateX(50);
        winterMonthsTempTextField.setPromptText("EX: \"12,3\" for DEC->MAR");
        winterMonthsTempTextField.setPrefWidth(200);
        Button confirmWinterMonthRange = new Button("Confirm");
        confirmWinterMonthRange.setTranslateY(100); confirmWinterMonthRange.setTranslateX(250);
        confirmWinterMonthRange.setOnAction(e->setWeatherMonthRange(winterMonthsTempTextField, false));

        Label setSummerMonthRange = new Label("Summer Months: ");
        setSummerMonthRange.setTranslateX(50); setSummerMonthRange.setTranslateY(140);
        TextField summerMonthsTempTextField = new TextField();
        summerMonthsTempTextField.setTranslateY(160); summerMonthsTempTextField.setTranslateX(50);
        summerMonthsTempTextField.setPromptText("EX: \"6,9\" for JUN->SEP");
        summerMonthsTempTextField.setPrefWidth(200);
        Button confirmSummerMonthRange = new Button("Confirm");
        confirmSummerMonthRange.setTranslateY(160); confirmSummerMonthRange.setTranslateX(250);
        confirmSummerMonthRange.setOnAction(e->setWeatherMonthRange(summerMonthsTempTextField, true));

        Button closeButton = new Button("Return");
        closeButton.setOnAction(e->tempStage.close());
        closeButton.setTranslateY(250); closeButton.setTranslateX(100);

        tempPane.getChildren().addAll(closeButton, setWinterMonthRange, winterMonthsTempTextField,
                setSummerMonthRange, summerMonthsTempTextField, confirmSummerMonthRange, confirmWinterMonthRange);
        tempStage.setScene(new Scene(tempPane, 500, 300));
        tempStage.showAndWait();
    }

    private Month winterMonthLowerBound;
    private Month winterMonthUpperBound;
    private Month summerMonthLowerBound;
    private Month summerMonthUpperBound;

    public boolean isWinter() {
        try {
            int winterLB_Value = this.winterMonthLowerBound.getValue();
            int winterUB_Value = this.winterMonthUpperBound.getValue();
            int simMonth_Value = Main.shsModule.simulationMonth.getValue();
            return (simMonth_Value <= winterUB_Value && simMonth_Value >= winterLB_Value);
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean isSummer() {
        try {
            int summerLB_Value = this.summerMonthLowerBound.getValue();
            int summerUB_Value = this.summerMonthUpperBound.getValue();
            int simMonth_Value = Main.shsModule.simulationMonth.getValue();
            return (simMonth_Value <= summerUB_Value && simMonth_Value >= summerLB_Value);
        }
        catch (Exception e){
            return false;
        }
    }

    public void setWeatherMonthRange(TextField textField, boolean summer) {
        try {
            String input = textField.getText();
            String[] months = input.split(",");
            int lowerBound = Integer.parseInt(months[0]);
            int upperBound = Integer.parseInt(months[1]);

            Month lowerBoundMonth = null;
            Month upperBoundMonth = null;

            switch (lowerBound) {
                case 1: lowerBoundMonth = Month.JANUARY; break;
                case 2: lowerBoundMonth = Month.FEBRUARY; break;
                case 3: lowerBoundMonth = Month.MARCH; break;
                case 4: lowerBoundMonth = Month.APRIL; break;
                case 5: lowerBoundMonth = Month.MAY; break;
                case 6: lowerBoundMonth = Month.JUNE; break;
                case 7: lowerBoundMonth = Month.JULY; break;
                case 8: lowerBoundMonth = Month.AUGUST; break;
                case 9: lowerBoundMonth = Month.SEPTEMBER; break;
                case 10: lowerBoundMonth = Month.OCTOBER; break;
                case 11: lowerBoundMonth = Month.NOVEMBER; break;
                case 12: lowerBoundMonth = Month.DECEMBER; break;
                default:
                    throw new Exception();
            }

            switch (upperBound) {
                case 1: upperBoundMonth = Month.JANUARY; break;
                case 2: upperBoundMonth = Month.FEBRUARY; break;
                case 3: upperBoundMonth = Month.MARCH; break;
                case 4: upperBoundMonth = Month.APRIL; break;
                case 5: upperBoundMonth = Month.MAY; break;
                case 6: upperBoundMonth = Month.JUNE; break;
                case 7: upperBoundMonth = Month.JULY; break;
                case 8: upperBoundMonth = Month.AUGUST; break;
                case 9: upperBoundMonth = Month.SEPTEMBER; break;
                case 10: upperBoundMonth = Month.OCTOBER; break;
                case 11: upperBoundMonth = Month.NOVEMBER; break;
                case 12: upperBoundMonth = Month.DECEMBER; break;
                default:
                    throw new Exception();
            }

            if (summer) {
                this.summerMonthLowerBound = lowerBoundMonth;
                this.summerMonthUpperBound = upperBoundMonth;
                String lb = (""+lowerBoundMonth).substring(0,3);
                String ub = (""+upperBoundMonth).substring(0,3);

                for (int i = 0; i < Main.main_dashboard.getChildren().size(); i++) {
                    try {
                        if (Main.main_dashboard.getChildren().get(i).getId().equals("summerMonthLabel")) {
                            Label label = (Label) Main.main_dashboard.getChildren().get(i);
                            label.setText("Summer:\n"+lb+"->"+ub);
                            Main.main_dashboard.getChildren().set(i, label);
                            break;
                        }
                    }
                    catch (Exception e){}
                }
            }
            else {
                this.winterMonthLowerBound = lowerBoundMonth;
                this.winterMonthUpperBound = upperBoundMonth;
                String lb = (""+lowerBoundMonth).substring(0,3);
                String ub = (""+upperBoundMonth).substring(0,3);

                for (int i = 0; i < Main.main_dashboard.getChildren().size(); i++) {
                    try {
                        if (Main.main_dashboard.getChildren().get(i).getId().equals("winterMonthLabel")) {
                            Label label = (Label) Main.main_dashboard.getChildren().get(i);
                            label.setText("Winter:\n"+lb+"->"+ub);
                            Main.main_dashboard.getChildren().set(i, label);
                            break;
                        }
                    }
                    catch (Exception e){}
                }
            }
        }
        catch (Exception e){
            Controller.appendMessageToConsole("Failed attempt to set Winter or Summer Months.");
        }
    }

    public void popupWinterSummerTempSettingsStage(AnchorPane pane) {
        //////////
        Stage tempStage = new Stage();
        tempStage.setTitle("Winter/Summer Temp Settings");
        tempStage.setResizable(false);
        AnchorPane tempPane = new AnchorPane();

        Label setWinterTempLabel = new Label("Winter Temp: ");
        setWinterTempLabel.setTranslateX(50); setWinterTempLabel.setTranslateY(80);
        TextField winterTempTextField = new TextField();
        winterTempTextField.setTranslateY(100); winterTempTextField.setTranslateX(50);
        winterTempTextField.setPromptText("degrees °C");
        winterTempTextField.setPrefWidth(90);
        Button confirmWinterTemp = new Button("Confirm");
        confirmWinterTemp.setTranslateY(100); confirmWinterTemp.setTranslateX(160);
        confirmWinterTemp.setOnAction(ev1->{
            try {
                for (int f = 0; f < pane.getChildren().size(); f++) {
                    try {
                        if (pane.getChildren().get(f).getId().equals("winterTempSHHLabel")) {
                            Label label = (Label) pane.getChildren().get(f);
                            this.winterTemperature = Integer.parseInt(winterTempTextField.getText());
                            label.setText("Winter temp (AWAY mode only): "+this.winterTemperature+"°C");
                            pane.getChildren().set(f, label);
                            break;
                        }
                    }
                    catch (Exception foo){}
                }
            }
            catch (Exception ex1){}
        });

        Label setSummerTempLabel = new Label("Summer Temp: ");
        setSummerTempLabel.setTranslateX(50); setSummerTempLabel.setTranslateY(140);
        TextField summerTempTextField = new TextField();
        summerTempTextField.setTranslateY(160); summerTempTextField.setTranslateX(50);
        summerTempTextField.setPromptText("degrees °C");
        summerTempTextField.setPrefWidth(90);
        Button confirmSummerTemp = new Button("Confirm");
        confirmSummerTemp.setTranslateY(160); confirmSummerTemp.setTranslateX(160);
        confirmSummerTemp.setOnAction(ev1->{
            try {
                for (int f = 0; f < pane.getChildren().size(); f++) {
                    try {
                        if (pane.getChildren().get(f).getId().equals("summerTempSHHLabel")) {
                            Label label = (Label) pane.getChildren().get(f);
                            this.summerTemperature = Integer.parseInt(summerTempTextField.getText());
                            label.setText("Summer temp (AWAY mode only): "+this.summerTemperature+"°C");
                            pane.getChildren().set(f, label);
                            break;
                        }
                    }
                    catch (Exception foo){}
                }
            }
            catch (Exception ex1){}
        });

        Button closeButton = new Button("Return");
        closeButton.setOnAction(e->tempStage.close());
        closeButton.setTranslateY(250); closeButton.setTranslateX(100);

        tempPane.getChildren().addAll(setSummerTempLabel, setWinterTempLabel,
                summerTempTextField, winterTempTextField, confirmSummerTemp, confirmWinterTemp, closeButton);
        tempStage.setScene(new Scene(tempPane, 300, 300));
        tempStage.showAndWait();
        //////////
    }

    public boolean isHAVCsystemPaused() {
        return HAVCsystemPaused;
    }

    public void setHAVCsystemPaused(boolean HAVCsystemPaused) {
        this.HAVCsystemPaused = HAVCsystemPaused;
    }

    public void goToZoneConfigPage() {
        SHHZoneConfigStage = new Stage();
        SHHZoneConfigStage.setResizable(false);
        SHHZoneConfigStage.initModality(Modality.APPLICATION_MODAL);
        SHHZoneConfigStage.setTitle("Configure SHH Zones");
        SHHZoneConfigStage.setWidth(900);
        SHHZoneConfigStage.setHeight(600);
        generateZoneConfigInterface();
        SHHZoneConfigStage.setScene(SHHZoneConfigScene);
        SHHZoneConfigStage.showAndWait();
    }

    public void generateZoneConfigInterface() {

        Label createNewZoneLabel = new Label("CREATE A NEW ZONE");
        createNewZoneLabel.setTranslateY(10); createNewZoneLabel.setTranslateX(20);

        Label roomListLabel = new Label("Select rooms that\nyou would like\nto add to a\nnew zone:");
        roomListLabel.setTranslateY(40); roomListLabel.setTranslateX(30);
        roomListLabel.setTextAlignment(TextAlignment.CENTER);

        if (numberOfTimesZoneConfigSelected == 0) {
            int transY = 130;
            for (int room = 0; room < Main.householdLocations.length; room++) {
                try {
                    CheckBox zoneRoomCB = new CheckBox(Main.householdLocations[room].getName());
                    zoneRoomCB.setId("zoneRoomCheckBoxID#"+Main.householdLocations[room].getRoomID());
                    zoneRoomCB.setTranslateX(30); zoneRoomCB.setTranslateY(transY);
                    if (isRoomInAzone(Main.householdLocations[room])) {
                        zoneRoomCB.setDisable(true);
                    }
                    SHHZoneConfigPane.getChildren().add(zoneRoomCB);
                    transY+=20;
                }
                catch (Exception e){}
            }
        }

        Button addZoneButton = new Button("Add New Zone");
        addZoneButton.setId("addNewZoneButton");
        addZoneButton.setTranslateX(30); addZoneButton.setTranslateY(330);
        addZoneButton.setOnAction(e->addZone());

        Line borderline1 = new Line();
        borderline1.setStartY(0); borderline1.setEndY(530);
        borderline1.setTranslateX(200);

        /**todo: option for configuring an existing zone */

        Line borderline2 = new Line();
        borderline2.setStartX(0); borderline2.setEndX(900);
        borderline2.setTranslateY(530);

        Button closeButton = new Button("Close");
        closeButton.setId("zoneConfigPageCloseButton");
        closeButton.setTranslateX(400); closeButton.setTranslateY(550);
        closeButton.setOnAction(e->SHHZoneConfigStage.close());

        if (numberOfTimesZoneConfigSelected == 0) {

            /**todo: add all elements */
            SHHZoneConfigPane.getChildren().addAll(createNewZoneLabel, borderline1, borderline2,
                    closeButton, roomListLabel, addZoneButton);
        }
        numberOfTimesZoneConfigSelected++;
    }

    public int getCurrentNumberOfZones() {
        return currentNumberOfZones;
    }

    public void incrementNumberOfZones() {
        this.currentNumberOfZones++;
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

    public double getTEMP_THRESHOLD() {
        return TEMP_THRESHOLD;
    }

    public void setTEMP_THRESHOLD(double TEMP_THRESHOLD) {
        this.TEMP_THRESHOLD = TEMP_THRESHOLD;
    }

    public Zone[] getZones() {
        return zones;
    }
    public void setZones(Zone[] zones) {
        this.zones = zones;
    }

    public boolean isHAVCsystemActive() {
        return HAVCsystemActive;
    }
    public void setHAVCsystemActive(boolean HAVCsystemActive) {
        this.HAVCsystemActive = HAVCsystemActive;
    }

    public void overrideZoneTemperature(int zoneID, double newTemp) {
        for (int z = 0; z < this.zones.length; z++) {
            try {
                if (this.zones[z].getZoneID() == zoneID) {

                    // check if newTemp has a difference of at least 1 degree from the initial temperature...
                    double tempDifference = (newTemp - this.zones[z].getZoneTemperature());
                    double absoluteTempDifference = (tempDifference < 0 ? (tempDifference * -1) : (tempDifference * 1));

                    if (absoluteTempDifference >= 1) {

                        this.HAVCsystemActive = true;
                        double roundedTemp;

                        while (this.zones[z].getZoneTemperature() != newTemp) {

                            if (newTemp < this.zones[z].getZoneTemperature()) {
                                roundedTemp = (double) Math.round((this.zones[z].getZoneTemperature() - 0.1) * 100) / 100;
                            } else {
                                roundedTemp = (double) Math.round((this.zones[z].getZoneTemperature() + 0.1) * 100) / 100;
                            }
                            try {

                                this.zones[z].setZoneTemperature(roundedTemp);
                                this.zones[z].overrideZoneRoomTemperaturesInHouse(roundedTemp);
                                Controller.appendMessageToConsole("SHH: Zone #" + zoneID + " temperature changed to " + roundedTemp + "°C");
                            }
                            catch (Exception e) {}
                            finally {
                                try { Thread.sleep((long) (1000 / Controller.simulationTimeSpeed)); } catch (Exception e) {}
                            }
                        }
                        this.HAVCsystemPaused = true;
                    }
                    break;
                }
            }
            catch (Exception e){}
        }
    }

    public boolean isRoomTempInBetweenQuartDegreeBounds(int zoneID, int roomID) {
        boolean yes = false;
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

                    if (!roomIsInZone) {
                        throw new Exception("ERROR [SHH]: Room #"+roomID+" is not in Zone "+zoneID);
                    }

                    for (int roomIndex = 0; roomIndex < this.zones[z].getZoneRoomIDs().length; roomIndex++) {
                        try {
                            for (int houseroomIndex = 0; houseroomIndex < Main.getHouseholdLocations().length; houseroomIndex++) {
                                if (Main.getHouseholdLocations()[houseroomIndex].getRoomID() == roomID) {
                                    double temperature = Main.getHouseholdLocations()[houseroomIndex].getRoomTemperature();
                                    double lowerBound = (double) Math.round((temperature-0.25)*100)/100;
                                    double upperBound = (double) Math.round((temperature+0.25)*100)/100;
                                    yes = ((temperature >= (lowerBound)) && (temperature <= upperBound));
                                    break;
                                }
                            }
                        }
                        catch (Exception e){}
                    }
                    break;
                }
            }
        }
        catch (Exception e){
            Controller.appendMessageToConsole(e.getMessage());
        }
        return yes;
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

    /**
     * Check if a Room is a member of an existing Zone
     * @param room
     * @return
     */
    public boolean isRoomInAzone(Room room) {
        boolean inAzone = false;
        try {
            if (this.zones == null) {
                throw new Exception();
            }

            for (int z = 0; z < this.zones.length; z++) {
                for (int roomIndex = 0; roomIndex < this.zones[z].getZoneRoomIDs().length; roomIndex++) {
                    if (this.zones[z].getZoneRoomIDs()[roomIndex] == room.getRoomID()) {
                        inAzone = true;
                        break;
                    }
                }
            }
        }
        catch (Exception e){}
        return inAzone;
    }

    public void addZone() {
        try {
            Zone newZone = new Zone();

            for (int elem = 0; elem < SHHZoneConfigPane.getChildren().size(); elem++) {
                try {
                    if (SHHZoneConfigPane.getChildren().get(elem).getId().contains("zoneRoomCheckBoxID")){
                        CheckBox tempCB = (CheckBox) SHHZoneConfigPane.getChildren().get(elem);
                        if (tempCB.isSelected()) {
                            int roomID = Integer.parseInt(tempCB.getId().substring(19));
                            for (int r = 0; r < Main.householdLocations.length; r++) {
                                if (Main.householdLocations[r].getRoomID() == roomID) {
                                    newZone.addRoomToZone(Main.householdLocations[r]);
                                    break;
                                }
                            }
                            tempCB.setSelected(false);
                            tempCB.setDisable(true);
                            tempCB.setText(tempCB.getText()+" (Z"+newZone.getZoneID()+")");
                            SHHZoneConfigPane.getChildren().set(elem, tempCB);
                        }
                    }
                }
                catch (Exception ex){}
            }

            if (this.zones==null) {
                this.zones = new Zone[1];
                this.zones[0] = newZone;
            }
            else {
                Zone[] tempZoneArray = new Zone[this.zones.length + 1];
                for (int z = 0; z < this.zones.length; z++) {
                    tempZoneArray[z] = this.zones[z];
                }
                tempZoneArray[tempZoneArray.length - 1] = newZone;
                this.zones = tempZoneArray;
            }

            // all rooms in the new zone take on the outdoor temperature
            newZone.overrideZoneRoomTemperaturesInHouse(Main.outsideTemperature);
            incrementNumberOfZones();
            updateSHSModuleWithZones();

            try {
                addNewZoneThread(newZone);
            }
            catch (Exception e){}
        }
        catch (Exception e) {
            Controller.appendMessageToConsole(e.getMessage());
        }
    }

    public void addNewZoneThread(Zone zone) {
        if (this.zoneThreads == null) {
            this.zoneThreads = new SHHZoneThread[1];
            this.zoneThreads[0] = new SHHZoneThread(zone);
        }
        else {
            SHHZoneThread[] tempThreadArray = new SHHZoneThread[this.zoneThreads.length + 1];
            for (int zt = 0; zt < this.zoneThreads.length; zt++) {
                try {
                    tempThreadArray[zt] = this.zoneThreads[zt];
                }
                catch (Exception e){}
            }
            tempThreadArray[tempThreadArray.length - 1] = new SHHZoneThread(zone);
            this.zoneThreads = tempThreadArray;
        }
    }

    public void updateSHSModuleWithZones() {

        for (int elem = 0; elem < Main.SHH_MODULE.getChildren().size(); elem++) {
            try {
                if (Main.SHH_MODULE.getChildren().get(elem).getId().equals("currentNumOfZonesLabel")) {
                    Label label = (Label) Main.SHH_MODULE.getChildren().get(elem);
                    label.setText("Current number of zones: "+this.currentNumberOfZones);
                    Main.SHH_MODULE.getChildren().set(elem, label);
                    break;
                }
            }
            catch (Exception e){}
        }

        int transY = 150; int transX = 250;
        for (int z = 0; z < this.zones.length; z++) {
            try {
                Button zoneInfoButton = new Button("Zone "+this.zones[z].getZoneID());
                zoneInfoButton.setTranslateY(transY); zoneInfoButton.setTranslateX(transX);
                int finalZ = z;
                zoneInfoButton.setOnAction(event->{
                    Stage zoneStage = new Stage();
                    zoneStage.setResizable(false);
                    zoneStage.setTitle(""+zoneInfoButton.getText()+" Info");
                    zoneStage.setWidth(450); zoneStage.setHeight(450);
                    AnchorPane zonePane = new AnchorPane();

                    Label zoneTempLabel = new Label("Zone temperature: "+this.zones[finalZ].getZoneTemperature()+"°C");
                    zoneTempLabel.setTranslateY(10); zoneTempLabel.setTranslateX(20);
                    zoneTempLabel.setId("zone"+this.zones[finalZ].getZoneID()+"tempLabel");
                    zonePane.getChildren().add(zoneTempLabel);

                    TextField changeZoneTempTF = new TextField(); changeZoneTempTF.setPrefWidth(80);
                    changeZoneTempTF.setPromptText("Change"); changeZoneTempTF.setId("changeZoneTempTF");
                    changeZoneTempTF.setTranslateX(20); changeZoneTempTF.setTranslateY(30);
                    zonePane.getChildren().add(changeZoneTempTF);

                    Button changeZoneTempButton = new Button("Change");
                    changeZoneTempButton.setId("changeZoneTempButton");
                    changeZoneTempButton.setTranslateX(110); changeZoneTempButton.setTranslateY(30);
                    changeZoneTempButton.setOnAction(e->{
                        try {
                            int newTemperature = Integer.parseInt(changeZoneTempTF.getText());
                            overrideZoneTemperature(this.zones[finalZ].getZoneID(), newTemperature);
                            changeZoneInfoPane(zonePane, newTemperature, this.zones[finalZ]);
                        }
                        catch (Exception ex){
                            Controller.appendMessageToConsole("Invalid attempt to change temperature of Zone "+
                                    this.zones[finalZ].getZoneID());
                            changeZoneTempTF.clear();
                            changeZoneTempTF.setPromptText("Change");
                        }
                    });

                    zonePane.getChildren().add(changeZoneTempButton);


                    Label roomsLabel = new Label("Rooms:");
                    roomsLabel.setTranslateY(90); roomsLabel.setTranslateX(20);
                    zonePane.getChildren().add(roomsLabel);

                    int tempTransX = 20, tempTransY = 110;
                    for (int r = 0; r < this.zones[finalZ].getZoneRoomIDs().length; r++) {
                        try {
                            for (int h = 0; h < Main.householdLocations.length; h++) {
                                try {
                                    if (Main.householdLocations[h].getRoomID()==this.zones[finalZ].getZoneRoomIDs()[r]) {
                                        Label tempLabel = new Label("#"+Main.householdLocations[h].getRoomID()+" "+Main.householdLocations[h].getName()+
                                                " ("+Main.householdLocations[h].getRoomTemperature()+"°C)");
                                        tempLabel.setId("#"+Main.householdLocations[h].getRoomID()+"_tempLabel");
                                        tempLabel.setTranslateX(tempTransX); tempLabel.setTranslateY(tempTransY);
                                        zonePane.getChildren().add(tempLabel);

                                        Hyperlink tempHypLink = new Hyperlink("Change this temperature");
                                        tempHypLink.setTranslateY(tempTransY+20);
                                        tempHypLink.setTranslateX(tempTransX);
                                        tempHypLink.setId("linkToChangeTempOfRoom#"+Main.householdLocations[h].getRoomID());

                                        int finalH = h;
                                        tempHypLink.setOnAction(e->{
                                            Stage changeRoomTempStage = new Stage();
                                            changeRoomTempStage.setTitle("Change Room #"+Main.householdLocations[finalH].getRoomID() +" 's temperature");
                                            changeRoomTempStage.setResizable(false);
                                            AnchorPane changeRoomTempPane = new AnchorPane();

                                            TextField textField = new TextField();
                                            textField.setId("changeTempSpecRoom#"+Main.householdLocations[finalH].getRoomID());
                                            textField.setTranslateY(30);
                                            textField.setTranslateX(20);
                                            textField.setPrefWidth(80);
                                            textField.setPromptText("New temp");
                                            changeRoomTempPane.getChildren().add(textField);

                                            Button changeSpecRoomTempButton = new Button("Change");
                                            changeSpecRoomTempButton.setId("changeTempButtonSpecRoom#"+Main.householdLocations[finalH].getRoomID());
                                            changeSpecRoomTempButton.setTranslateY(30);
                                            changeSpecRoomTempButton.setTranslateX(120);
                                            /**todo: set on action*/
                                            changeSpecRoomTempButton.setOnAction(ev->{
                                                try {
                                                    int newTemperature = Integer.parseInt(textField.getText());
                                                    overrideTempInSpecificRoomInZone(this.zones[finalZ].getZoneID(), Main.householdLocations[finalH].getRoomID(), newTemperature);
                                                    changeZoneInfoPaneSpecificRoom(zonePane, newTemperature, this.zones[finalZ], Main.householdLocations[finalH].getRoomID());
                                                }
                                                catch (Exception exception) {
                                                    Controller.appendMessageToConsole("Invalid attempt to change temperature of Room #"+
                                                            Main.householdLocations[finalH].getRoomID()+" in "+this.zones[finalZ].getZoneID());
                                                    textField.clear();
                                                    textField.setPromptText("New temp");
                                                }
                                            });

                                            changeRoomTempPane.getChildren().add(changeSpecRoomTempButton);

                                            Button closeButton = new Button("Close");
                                            closeButton.setId("closeButtonForChangeTempPaneSpecRoom#"+Main.householdLocations[finalH].getRoomID());
                                            closeButton.setTranslateY(300); closeButton.setTranslateX(175);
                                            closeButton.setOnAction(ev->{
                                                textField.clear();
                                                textField.setPromptText("New temp");
                                                changeRoomTempStage.close();
                                            });
                                            changeRoomTempPane.getChildren().add(closeButton);

                                            changeRoomTempStage.setScene(new Scene(changeRoomTempPane, 350,350));
                                            changeRoomTempStage.showAndWait();
                                        });

                                        zonePane.getChildren().add(tempHypLink);
                                        tempTransY+=60;
                                    }
                                }
                                catch (Exception e){}
                            }
                        }
                        catch (Exception e){}
                    }

                    Button zonePaneCloseButton = new Button("Return");
                    zonePaneCloseButton.setOnAction(e->zoneStage.close());
                    zonePaneCloseButton.setTranslateX(150); zonePaneCloseButton.setTranslateY(400);
                    zonePane.getChildren().add(zonePaneCloseButton);

                    zoneStage.setScene(new Scene(zonePane));
                    zoneStage.showAndWait();
                });
                Main.SHH_MODULE.getChildren().add(zoneInfoButton);
                transY+=40;
            }
            catch (Exception e){}
        }
    }

    public void changeZoneInfoPane(AnchorPane pane, double temperature, Zone zone) {
        for (int el = 0; el < pane.getChildren().size(); el++) {
            if (pane.getChildren().get(el).getId().equals("zone"+zone.getZoneID()+"tempLabel")) {
                Label label = (Label) pane.getChildren().get(el);
                label.setText("Zone temperature: "+zone.getZoneTemperature()+"°C");
                pane.getChildren().set(el,label);
                break;
            }
        }

        // for each room in the zone...
        for (int r = 0; r < zone.getZoneRoomIDs().length; r++) {
            try {
                // for each room in the house...
                for (int h = 0; h < Main.householdLocations.length; h++) {
                    try {
                        // if the room's ID matches the one at the zone's index...
                        if (Main.householdLocations[h].getRoomID()==zone.getZoneRoomIDs()[r]) {

                            // find the label with the matching room ID...
                            for (int el = 0; el < pane.getChildren().size(); el++) {
                                try {
                                    if (pane.getChildren().get(el).getId().contains("#"+Main.householdLocations[h].getRoomID()+"_tempLabel")) {
                                        Label label = (Label) pane.getChildren().get(el);
                                        label.setText("#"+Main.householdLocations[h].getRoomID()+" "+Main.householdLocations[h].getName()+
                                                " ("+temperature+"°C)");
                                        pane.getChildren().set(el,label);
                                        break;
                                    }
                                }
                                catch (Exception e){}
                            }
                        }
                    }
                    catch (Exception e){}
                }
            }
            catch (Exception e){}
        }
    }

    public void changeZoneInfoPaneSpecificRoom(AnchorPane pane, double temperature, Zone zone, int roomID) {
        // for each room in the zone...
        for (int r = 0; r < zone.getZoneRoomIDs().length; r++) {
            try {
                // for each room in the house...
                for (int h = 0; h < Main.householdLocations.length; h++) {
                    try {
                        // if the room's ID matches the one at the zone's index and method parameter...
                        if (Main.householdLocations[h].getRoomID()==zone.getZoneRoomIDs()[r] &&
                                zone.getZoneRoomIDs()[r]==roomID) {

                            // find the label with the matching room ID...
                            for (int el = 0; el < pane.getChildren().size(); el++) {
                                try {
                                    if (pane.getChildren().get(el).getId().contains("#"+Main.householdLocations[h].getRoomID()+"_tempLabel")) {
                                        Label label = (Label) pane.getChildren().get(el);
                                        label.setText("#"+Main.householdLocations[h].getRoomID()+" "+Main.householdLocations[h].getName()+
                                                " ("+temperature+"°C -- overridden)");
                                        pane.getChildren().set(el,label);
                                        break;
                                    }
                                }
                                catch (Exception e){}
                            }
                            break;
                        }
                    }
                    catch (Exception e){}
                }
            }
            catch (Exception e){}
        }
    }
}
