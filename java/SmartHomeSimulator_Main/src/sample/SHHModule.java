package sample;
import house.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.time.Month;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * SHH Class
 */
public class SHHModule extends Module {

    private static int nextDestinationZoneID = -1;
    private static boolean awayModeON = false;
    private Month winterMonthLowerBound = null;
    private Month winterMonthUpperBound = null;
    private Month summerMonthLowerBound = null;
    private Month summerMonthUpperBound = null;
    private int currentNumberOfZones;
    private double outdoorTemperature;
    private double indoorTemperature;
    private double winterTemperature;
    private double summerTemperature;
    private Zone[] zones;
    private boolean isWinter;
    private boolean isSummer;
    private Thread indoorTemperatureThread;
    protected static int MAX_NUMBER_OF_ZONES = 0;

    /**
     * SHH Constructor
     */
    public SHHModule() {
        super();
        this.outdoorTemperature = Main.outsideTemperature;
        this.indoorTemperature = Main.outsideTemperature;
        this.winterTemperature = 0;
        this.summerTemperature = 0;
        this.isWinter = false;
        this.isSummer = false;
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

        Label winterTempSHHLabel = new Label("Winter temp (AWAY mode only): "+this.winterTemperature+"°C");
        winterTempSHHLabel.setId("winterTempSHHLabel");
        winterTempSHHLabel.setTranslateY(40);

        Label summerTempSHHLabel = new Label("Summer temp (AWAY mode only): "+this.summerTemperature+"°C");
        summerTempSHHLabel.setId("summerTempSHHLabel");
        summerTempSHHLabel.setTranslateY(60);

        Label maxNumOfZonesSHHLabel = new Label("Maximum number of zones allowed: "+MAX_NUMBER_OF_ZONES);
        maxNumOfZonesSHHLabel.setId("maxNumOfZonesLabel");
        maxNumOfZonesSHHLabel.setTranslateY(80);

        Label currentNumOfZonesSHHLabel = new Label("Current number of zones: "+this.currentNumberOfZones);
        currentNumOfZonesSHHLabel.setId("currentNumOfZonesLabel");
        currentNumOfZonesSHHLabel.setTranslateY(100);

        Label indoorTemperatureLabel = new Label("Indoor temperature: "+indoorTemperature+"°C");
        indoorTemperatureLabel.setId("indoorTemperatureLabel");
        indoorTemperatureLabel.setTranslateY(120);

        Line borderLine1 = new Line();
        borderLine1.setStartX(0); borderLine1.setEndX(500);
        borderLine1.setTranslateY(150);

        Label nullHouseError = new Label("ERROR: House layout null");
        nullHouseError.setTranslateX(300); nullHouseError.setTranslateY(10);
        nullHouseError.setVisible(false);

        Button configZoneButton = new Button("Add or\nConfigure\nZones");
        configZoneButton.setId("configZoneButton");
        configZoneButton.setTranslateX(300); configZoneButton.setTranslateY(30);
        configZoneButton.setOnAction(e->{
            if (Main.householdLocations != null) {
                openUpZoneConfigurationPanel();
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
        zonesLabel.setTranslateY(170);

        if (Main.numberOfTimesSHHModuleCreated==0) {
            pane.getChildren().addAll(awayModeSHHLabel, outdoorTempSHHLabel,
                    winterTempSHHLabel, summerTempSHHLabel, maxNumOfZonesSHHLabel, indoorTemperatureLabel,
                    currentNumOfZonesSHHLabel, borderLine1, configZoneButton, zonesLabel, nullHouseError, configWinterSummerTemp);
        }

        Main.numberOfTimesSHHModuleCreated++;
        return pane;
    }

    /**
     * set the indoor temperature (a pre-computed value that constantly updates during the simulation)
     * @param newTemperature
     */
    public void setIndoorTemperature(double newTemperature) {
        this.indoorTemperature = newTemperature;
    }

    /**
     * trigger a thread that, every millisecond, updates the indoor temperature by reassigning it to
     * the average temperature of all rooms in the house
     */
    public void startIndoorTempThread() {
        this.indoorTemperatureThread = new Thread(()->{
            while (true) {
                try {
                    double averageTemperatureOfRooms = 0;
                    for (int location = 0; location < Main.householdLocations.length; location++) {
                        try {
                            averageTemperatureOfRooms += Main.householdLocations[location].getRoomTemperature();
                        }
                        catch (Exception e){}
                    }
                    setIndoorTemperature(averageTemperatureOfRooms / Main.householdLocations.length);
                    Platform.runLater(()->changeSHHTempLabel("indoorTemperatureLabel", indoorTemperature));
                    if (this.indoorTemperature <= 0) {
                        Controller.appendMessageToConsole("WARNING [SHH]: Indoor temperature <= 0°C -- pipes might burst...");
                    }
                }
                catch (Exception e){}
                finally {
                    try {Thread.sleep((long) (1 / Controller.simulationTimeSpeed));} catch (Exception e){}
                }
            }
        });
        this.indoorTemperatureThread.start();
    }

    /**
     * Set the maximum number of zones
     * @param max
     */
    public void setMaxNumOfZones(int max) {
        try {
            MAX_NUMBER_OF_ZONES = max;
            for (int elem = 0; elem < Main.SHH_MODULE.getChildren().size(); elem++) {
                try {
                    if (Main.SHH_MODULE.getChildren().get(elem).getId().equals("maxNumOfZonesLabel")) {
                        Label maxNumOfZonesSHHLabel = (Label) Main.SHH_MODULE.getChildren().get(elem);
                        maxNumOfZonesSHHLabel.setText("Maximum number of zones allowed: "+MAX_NUMBER_OF_ZONES);
                        Main.SHH_MODULE.getChildren().set(elem,maxNumOfZonesSHHLabel);
                        break;
                    }
                }
                catch (Exception e){}
            }
        }
        catch (Exception e){}
    }

    /**
     * show the user interface for configuring the month ranges for Winter and Summer
     */
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
        tempStage.show();
    }

    /**
     * Helper method to check if a month's numerical value falls within a specified month range
     * @param lowerMonth
     * @param upperMonth
     * @param value
     * @return
     */
    public boolean isMonthValueInRange(int lowerMonth, int upperMonth, int value) {

        String months = "";
        int nextNumber = lowerMonth % 12;
        while (nextNumber != upperMonth) {
            months += nextNumber+"_";
            nextNumber = (nextNumber + 1) % 12;
        }
        months += upperMonth;

        String[] stringIntegers = months.split("_");
        int[] finalArray = new int[stringIntegers.length];

        for (int i = 0; i < stringIntegers.length; i++) {
            finalArray[i] = Integer.parseInt(stringIntegers[i]);
        }

        boolean inRange = false;
        for (int t = 0; t < finalArray.length; t++) {
            if (finalArray[t] == value) {
                inRange = true;
                break;
            }
        }

        return inRange;
    }

    /**
     * check to see if the simulation month falls within the winter months
     * @return
     */
    public boolean isWinter() {
        if (this.winterMonthUpperBound==null || this.winterMonthLowerBound==null || Main.shsModule.simulationMonth==null) {
            return false;
        }

        return isMonthValueInRange(this.winterMonthLowerBound.getValue(), this.winterMonthUpperBound.getValue(),
                Main.shsModule.simulationMonth.getValue());
    }

    /**
     * check to see if the simulation month falls within the summer months
     * @return
     */
    public boolean isSummer() {
        if (this.summerMonthUpperBound==null || this.summerMonthLowerBound==null || Main.shsModule.simulationMonth==null) {
            return false;
        }

        return isMonthValueInRange(this.summerMonthLowerBound.getValue(), this.summerMonthUpperBound.getValue(),
                Main.shsModule.simulationMonth.getValue());
    }

    /**
     * A helper method that returns a Month name based on the month number as a parameter
     * (example: value==12 returns 'Month.DECEMBER')
     * @param value
     * @return
     */
    public Month identifyMonth(int value) {
        try {
            Month month;
            switch (value) {
                case 1: month = Month.JANUARY; break;
                case 2: month = Month.FEBRUARY; break;
                case 3: month = Month.MARCH; break;
                case 4: month = Month.APRIL; break;
                case 5: month = Month.MAY; break;
                case 6: month = Month.JUNE; break;
                case 7: month = Month.JULY; break;
                case 8: month = Month.AUGUST; break;
                case 9: month = Month.SEPTEMBER; break;
                case 10: month = Month.OCTOBER; break;
                case 11: month = Month.NOVEMBER; break;
                case 12: month = Month.DECEMBER; break;
                default:
                    throw new Exception();
            }
            return month;
        }
        catch (Exception e){
            return null;
        }

    }

    /**
     * configure the month ranges for Winter and Summer.
     *
     * When the range for one of two seasons is set, the opposite season's range automatically
     * assumes the remaining months in the year.
     * @param textField
     * @param summer
     */
    public void setWeatherMonthRange(TextField textField, boolean summer) {
        try {
            String input = textField.getText();
            String[] months = input.split(",");
            int lowerBound = Integer.parseInt(months[0]);
            int upperBound = Integer.parseInt(months[1]);

            int otherUpperBound = lowerBound - 1;
            int otherLowerBound = (upperBound + 1) % 12;

            if (otherUpperBound % 12 == 0) {
                otherUpperBound = 12;
            }
            if (otherLowerBound % 12 == 0) {
                otherLowerBound = 12;
            }

            Month lowerBoundMonth = identifyMonth(lowerBound);
            Month upperBoundMonth = identifyMonth(upperBound);
            Month otherLowerBoundMonth = identifyMonth(otherLowerBound);
            Month otherUpperBoundMonth = identifyMonth(otherUpperBound);
            
            if (summer) {
                this.summerMonthLowerBound = lowerBoundMonth;
                this.summerMonthUpperBound = upperBoundMonth;
                this.winterMonthLowerBound = otherLowerBoundMonth;
                this.winterMonthUpperBound = otherUpperBoundMonth;

                String lb = (""+lowerBoundMonth).substring(0,3);
                String ub = (""+upperBoundMonth).substring(0,3);
                String lb2 = (""+otherLowerBoundMonth).substring(0,3);
                String ub2 = (""+otherUpperBoundMonth).substring(0,3);

                for (int i = 0; i < Main.main_dashboard.getChildren().size(); i++) {
                    try {
                        if (Main.main_dashboard.getChildren().get(i).getId().equals("summerMonthLabel")) {
                            Label label = (Label) Main.main_dashboard.getChildren().get(i);
                            label.setText("Summer:\n"+lb+"->"+ub);
                            Main.main_dashboard.getChildren().set(i, label);
                        }
                        else if (Main.main_dashboard.getChildren().get(i).getId().equals("winterMonthLabel")){
                            Label label = (Label) Main.main_dashboard.getChildren().get(i);
                            label.setText("Winter:\n"+lb2+"->"+ub2);
                            Main.main_dashboard.getChildren().set(i, label);
                        }
                    }
                    catch (Exception e){}
                }
            }
            else {
                this.winterMonthLowerBound = lowerBoundMonth;
                this.winterMonthUpperBound = upperBoundMonth;
                this.summerMonthLowerBound = otherLowerBoundMonth;
                this.summerMonthUpperBound = otherUpperBoundMonth;

                String lb = (""+lowerBoundMonth).substring(0,3);
                String ub = (""+upperBoundMonth).substring(0,3);
                String lb2 = (""+otherLowerBoundMonth).substring(0,3);
                String ub2 = (""+otherUpperBoundMonth).substring(0,3);

                for (int i = 0; i < Main.main_dashboard.getChildren().size(); i++) {
                    try {
                        if (Main.main_dashboard.getChildren().get(i).getId().equals("winterMonthLabel")) {
                            Label label = (Label) Main.main_dashboard.getChildren().get(i);
                            label.setText("Winter:\n"+lb+"->"+ub);
                            Main.main_dashboard.getChildren().set(i, label);
                        }
                        else if (Main.main_dashboard.getChildren().get(i).getId().equals("summerMonthLabel")){
                            Label label = (Label) Main.main_dashboard.getChildren().get(i);
                            label.setText("Summer:\n"+lb2+"->"+ub2);
                            Main.main_dashboard.getChildren().set(i, label);
                        }
                    }
                    catch (Exception e){}
                }
            }

            notifySHHOFAwayMode();
            notifyToOpenAllZoneWindows();
        }
        catch (Exception e){}
    }

    /**
     * show the user interface for configuring the default temperatures for Winter and Summer during AWAY mode
     * @param pane
     */
    public void popupWinterSummerTempSettingsStage(AnchorPane pane) {
        try {
            if (Main.currentActiveProfile.getPermSeasonWeather()) {
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
                                    setWinterTemperature(Double.parseDouble(winterTempTextField.getText()));
                                    notifySHHOFAwayMode();
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
                                    setSummerTemperature(Double.parseDouble(summerTempTextField.getText()));
                                    notifySHHOFAwayMode();
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
                tempStage.show();
            }
            else {
                throw new Exception("ERROR [SHH]: Permission denied to configure seasonal temperature settings.");
            }
        }
        catch (Exception e){}
    }

    /**
     * show the user interface for creating new zones or moving rooms around existing zones
     */
    public void openUpZoneConfigurationPanel() {
        Stage tempStage = new Stage();
        tempStage.setResizable(false);
        tempStage.setTitle("Relocate Rooms Around Zones");

        AnchorPane hostPane = new AnchorPane();

        Label instructionsLabel = new Label();
        instructionsLabel.setText("Select as many rooms from up to multiple zones, and then\n" +
                "select ONLY ONE zone where you would like to move those rooms, and click \"Confirm\"");
        hostPane.getChildren().add(instructionsLabel);

        int transX = 0, transY = 50;
        for (int z = 0; z <= this.zones.length; z++) {
            AnchorPane zonePane = new AnchorPane();
            zonePane.setPrefWidth(250);
            zonePane.setPrefHeight(250);
            zonePane.setStyle("-fx-border-color:black;");

            switch (z+1) {
                case 1: transY = 50; transX = 0; break;
                case 2: transY = 50; transX = 250; break;
                case 3: transY = 50; transX = 500; break;
                case 4: transY = 300; transX = 0; break;
                case 5: transY = 300; transX = 250; break;
                case 6: transY = 300; transX = 500; break;
                case 7: transY = 550; transX = 0; break;
                case 8: transY = 550; transX = 250; break;
                case 9: transY = 550; transX = 500; break;
            }

            if (z != this.zones.length) {
                zonePane.setId("zonePaneForZone#"+this.zones[z].getZoneID());
                CheckBox zoneSelectBox = new CheckBox("Zone #"+this.zones[z].getZoneID());
                zoneSelectBox.setId("zoneSelectBoxID#"+this.zones[z].getZoneID());
                zoneSelectBox.setTranslateX(20); zoneSelectBox.setTranslateY(10);
                zoneSelectBox.setOnAction(e->{
                    if (zoneSelectBox.isSelected()) {
                        nextDestinationZoneID = Integer.parseInt(zoneSelectBox.getId().substring(16));
                    }
                    else {
                        nextDestinationZoneID = -1;
                    }
                });
                zonePane.getChildren().add(zoneSelectBox);

                Line line = new Line();
                line.setEndX(0); line.setEndX(250);
                line.setTranslateY(30);
                zonePane.getChildren().add(line);

                int roomTransX = 20, roomTransY = 50;
                for (int zoneRoom = 0; zoneRoom < this.zones[z].getZoneRoomIDs().length; zoneRoom++) {
                    try {
                        for (int hr = 0; hr < Main.householdLocations.length; hr++) {
                            try {
                                if (Main.householdLocations[hr].getRoomID()==this.zones[z].getZoneRoomIDs()[zoneRoom]) {
                                    CheckBox zoneRoomBox = new CheckBox(""+Main.householdLocations[hr].getName());
                                    zoneRoomBox.setId("zoneRoomBoxID#"+Main.householdLocations[hr].getRoomID());
                                    zoneRoomBox.setTranslateX(roomTransX);
                                    zoneRoomBox.setTranslateY(roomTransY);
                                    zoneRoomBox.setOnAction(e->{
                                        int numOfRoomBoxesChecked = 0;
                                        for (int zboxFinder = 0; zboxFinder < zonePane.getChildren().size(); zboxFinder++) {
                                            try {
                                                if (zonePane.getChildren().get(zboxFinder).getId().contains("zoneRoomBoxID#")) {
                                                    CheckBox cb = (CheckBox) zonePane.getChildren().get(zboxFinder);
                                                    if (cb.isSelected()) {
                                                        numOfRoomBoxesChecked++;
                                                    }
                                                }
                                            }
                                            catch (Exception foo){}
                                        }

                                        if (numOfRoomBoxesChecked == 0) {
                                            for (int zboxFinder = 0; zboxFinder < zonePane.getChildren().size(); zboxFinder++) {
                                                try {
                                                    if (zonePane.getChildren().get(zboxFinder).getId().contains("zoneSelectBoxID#")) {
                                                        CheckBox cb = (CheckBox) zonePane.getChildren().get(zboxFinder);
                                                        cb.setDisable(false);
                                                        zonePane.getChildren().set(zboxFinder, cb);
                                                        break;
                                                    }
                                                }
                                                catch (Exception foo){}
                                            }
                                        }
                                        else {
                                            for (int zboxFinder = 0; zboxFinder < zonePane.getChildren().size(); zboxFinder++) {
                                                try {
                                                    if (zonePane.getChildren().get(zboxFinder).getId().contains("zoneSelectBoxID#")) {
                                                        CheckBox cb = (CheckBox) zonePane.getChildren().get(zboxFinder);
                                                        cb.setDisable(true);
                                                        zonePane.getChildren().set(zboxFinder, cb);
                                                        break;
                                                    }
                                                }
                                                catch (Exception foo){}
                                            }
                                        }
                                    });
                                    zonePane.getChildren().add(zoneRoomBox);
                                    roomTransY += 20;
                                    break;
                                }
                            }
                            catch (Exception e){}
                        }
                    }
                    catch (Exception e){}
                }
            }
            else {
                zonePane.setId("zonePaneForNewZone");
                CheckBox zoneSelectBox = new CheckBox("New Zone");
                zoneSelectBox.setId("zoneSelectBoxNewZone");
                zoneSelectBox.setTranslateX(20); zoneSelectBox.setTranslateY(10);
                zoneSelectBox.setOnAction(e->{
                    if (zoneSelectBox.isSelected()) {
                        nextDestinationZoneID = 0;
                    }
                    else {
                        nextDestinationZoneID = -1;
                    }
                });
                zonePane.getChildren().add(zoneSelectBox);

                Line line = new Line();
                line.setEndX(0); line.setEndX(250);
                line.setTranslateY(30);
                zonePane.getChildren().add(line);
            }

            zonePane.setTranslateX(transX);
            zonePane.setTranslateY(transY);
            hostPane.getChildren().add(zonePane);
        }

        Button confirmButton = new Button("Confirm");
        confirmButton.setTranslateY(50); confirmButton.setTranslateX(760);
        confirmButton.setOnAction(e->{
            int[] roomsToBeMoved = getRoomIDsToMoveInAnotherZone(hostPane);
            Room[] roomsDeletedFromExistingZones;
            if (roomsToBeMoved!=null) {
                switch (nextDestinationZoneID) {
                    case -1:
                        Controller.appendMessageToConsole("ERROR [SHH]: Failed to change Zones of Rooms");
                        break;
                    case 0:
                        if (currentNumberOfZones != MAX_NUMBER_OF_ZONES) {
                            try {
                                if (Main.currentActiveProfile.getPermCreateZone()) {
                                    roomsDeletedFromExistingZones = deleteRoomsFromZones(roomsToBeMoved);
                                    createNewZone(roomsDeletedFromExistingZones);
                                }
                                else {
                                    throw new Exception("ERROR [SHH]: Permission denied to create new zones");
                                }
                            }
                            catch (Exception exception){
                                Controller.appendMessageToConsole(exception.getMessage());
                            }

                        }
                        nextDestinationZoneID = -1;
                        break;
                    default:
                        roomsDeletedFromExistingZones = deleteRoomsFromZones(roomsToBeMoved);
                        for (int z = 0; z < zones.length; z++) {
                            try {
                                if (zones[z].getZoneID()==nextDestinationZoneID) {
                                    for (int r = 0; r < roomsDeletedFromExistingZones.length; r++) {
                                        try {
                                            zones[z].addRoomToZone(roomsDeletedFromExistingZones[r]);
                                            overrideTempInSpecificRoomInZone(zones[z].getZoneID(),
                                                    roomsDeletedFromExistingZones[r].getRoomID(), zones[z].getZoneTemperature());
                                        }
                                        catch (Exception f){}
                                    }
                                    break;
                                }
                            }
                            catch (Exception ex1){}
                        }
                        nextDestinationZoneID = -1;
                        break;
                }
            }
            else {
                Controller.appendMessageToConsole("ERROR [SHH]: Failed to change Zones of Rooms");
            }
            notifySHHOFAwayMode();
            tempStage.close();
        });
        hostPane.getChildren().add(confirmButton);

        Button moreOptionsButton = new Button("Set Time\nPeriods");
        moreOptionsButton.setTranslateY(200); moreOptionsButton.setTranslateX(760);
        moreOptionsButton.setOnAction(e->{
            tempStage.close();
            openUpZoneConfigurationPanel_TimePeriods();
        });
        hostPane.getChildren().add(moreOptionsButton);

        Button closeButton = new Button("Close");
        closeButton.setTranslateY(130); closeButton.setTranslateX(760);
        closeButton.setOnAction(e->tempStage.close());
        hostPane.getChildren().add(closeButton);

        tempStage.setScene(new Scene(hostPane, 850, 1000));
        tempStage.show();
    }

    /**
     * show the user interface for setting 1 to 3 time periods and their temperature settings
     */
    public void openUpZoneConfigurationPanel_TimePeriods() {
        Stage tempStage = new Stage();
        tempStage.setResizable(false);
        tempStage.setTitle("Configure Zone Time Periods");

        AnchorPane hostPane = new AnchorPane();

        Label instructionsLabel = new Label();
        instructionsLabel.setText("For each zone, enter a temperature and a range of hours when you would\n" +
                "like that zone to automatically take on that temperature.");
        hostPane.getChildren().add(instructionsLabel);

        int transX = 0, transY = 50;
        for (int z = 0; z < this.zones.length; z++) {
            AnchorPane zonePane = new AnchorPane();
            zonePane.setPrefWidth(250);
            zonePane.setPrefHeight(250);
            zonePane.setStyle("-fx-border-color:black;");

            switch (z+1) {
                case 1: transY = 50; transX = 0; break;
                case 2: transY = 50; transX = 250; break;
                case 3: transY = 50; transX = 500; break;
                case 4: transY = 300; transX = 0; break;
                case 5: transY = 300; transX = 250; break;
                case 6: transY = 300; transX = 500; break;
                case 7: transY = 550; transX = 0; break;
                case 8: transY = 550; transX = 250; break;
                case 9: transY = 550; transX = 500; break;
            }

            zonePane.setId("zone#"+this.zones[z].getZoneID()+"timePeriodConfigPane");
            Label zoneLabel = new Label("Zone #"+this.zones[z].getZoneID());
            zoneLabel.setTranslateX(20); zoneLabel.setTranslateY(10);
            zonePane.getChildren().add(zoneLabel);

            Line line = new Line();
            line.setEndX(0); line.setEndX(250);
            line.setTranslateY(30);
            zonePane.getChildren().add(line);

            int translateY = 35, translateX = 20;
            for (int per = 1; per <= 3; per++) {
                Label labelPeriod = new Label("Period "+per+"\n\nFrom:                to ");
                labelPeriod.setTranslateY(translateY);
                labelPeriod.setTranslateX(translateX);

                TextField tempField = new TextField();
                tempField.setId("period"+per+"_tempField_zone#"+this.zones[z].getZoneID());
                tempField.setTranslateX(translateX + 60);
                tempField.setTranslateY(translateY);
                tempField.setPromptText(""+this.zones[z].getPeriodTemperature(per));
                tempField.setPrefWidth(50);

                TextField lowerHourBoundInput = new TextField();
                lowerHourBoundInput.setId("period"+per+"_lowerHourBound__zone#"+this.zones[z].getZoneID());
                lowerHourBoundInput.setPrefWidth(40);

                if (!(this.zones[z].getPeriodHours(per)==null)) {
                    lowerHourBoundInput.setPromptText(""+this.zones[z].getPeriodHours(per)[0]);
                }

                lowerHourBoundInput.setTranslateY(translateY+35);
                lowerHourBoundInput.setTranslateX(translateX+50);

                TextField upperHourBoundInput = new TextField();
                upperHourBoundInput.setId("period"+per+"_upperHourBound__zone#"+this.zones[z].getZoneID());
                upperHourBoundInput.setPrefWidth(40);

                if (!(this.zones[z].getPeriodHours(per)==null)) {
                    upperHourBoundInput.setPromptText(""+this.zones[z].getPeriodHours(per)[1]);
                }

                upperHourBoundInput.setTranslateY(translateY+35);
                upperHourBoundInput.setTranslateX(translateX+110);

                Button confirmButton = new Button("Set");
                confirmButton.setTranslateY(translateY+35);
                confirmButton.setTranslateX(translateX+160);

                int finalZ = z;
                int finalPer = per;
                confirmButton.setOnAction(e->{
                    try {
                        int lowerHour = Integer.parseInt(lowerHourBoundInput.getText());
                        int upperHour = Integer.parseInt(upperHourBoundInput.getText());
                        double periodTemp = Double.parseDouble(tempField.getText());

                        this.zones[finalZ].setTimePeriodRangeAndTemperature(lowerHour, upperHour, finalPer, periodTemp);

                        tempField.setPromptText(""+this.zones[finalZ].getPeriodTemperature(finalPer));
                        lowerHourBoundInput.setPromptText(""+this.zones[finalZ].getPeriodHours(finalPer)[0]);
                        upperHourBoundInput.setPromptText(""+this.zones[finalZ].getPeriodHours(finalPer)[1]);
                    }
                    catch (Exception ex){
                        System.out.println("errorrrrrrr -- "+ex.getMessage());
                        upperHourBoundInput.clear();
                        lowerHourBoundInput.clear();
                        tempField.clear();
                    }
                });

                if (per < 3) {
                    Line borderLine = new Line();
                    borderLine.setEndX(250);
                    borderLine.setStartX(0);

                    switch (per) {
                        case 1:
                            borderLine.setTranslateY(100);
                            break;
                        case 2:
                            borderLine.setTranslateY(180);
                            break;
                    }
                    zonePane.getChildren().add(borderLine);
                }

                zonePane.getChildren().addAll(labelPeriod, tempField, lowerHourBoundInput, upperHourBoundInput, confirmButton);
                translateY += 75;
            }

            zonePane.setTranslateX(transX);
            zonePane.setTranslateY(transY);
            hostPane.getChildren().add(zonePane);
        }

        Button goBackButton = new Button("Go Back");
        goBackButton.setTranslateY(120); goBackButton.setTranslateX(760);
        goBackButton.setOnAction(e->{
            tempStage.close();
            openUpZoneConfigurationPanel();
        });
        hostPane.getChildren().add(goBackButton);

        Button closeButton = new Button("Close");
        closeButton.setTranslateY(80); closeButton.setTranslateX(760);
        closeButton.setOnAction(e->tempStage.close());
        hostPane.getChildren().add(closeButton);

        tempStage.setScene(new Scene(hostPane, 850, 1000));
        tempStage.show();
    }

    /**
     * delete an array of rooms, based on their room IDs
     * @param roomsToBeMoved
     * @return
     */
    public Room[] deleteRoomsFromZones(int[] roomsToBeMoved) {
        Room[] roomsArray = new Room[roomsToBeMoved.length];

        try {
            for (int roomID = 0; roomID < roomsToBeMoved.length; roomID++) {

                // store the corresponding Room object into the roomsArray
                for (int locs = 0; locs < Main.householdLocations.length; locs++) {
                    try {
                        if (Main.householdLocations[locs].getRoomID()==roomsToBeMoved[roomID]) {
                            roomsArray[roomID] = Main.householdLocations[locs];
                        }
                    }
                    catch (Exception e){}
                }

                // remove the room from the existing zone it's in
                removeRoomFromZone(roomsToBeMoved[roomID], getZoneOfRoom(roomsToBeMoved[roomID]));
            }
        }
        catch (Exception e){}
        return roomsArray;
    }

    /**
     * remove a room from a zone
     * @param roomID
     * @param zoneID
     */
    public void removeRoomFromZone(int roomID, int zoneID) {
        for (int z = 0; z < zones.length; z++) {
            try {
                if (zones[z].getZoneID()==zoneID) {
                    zones[z].deleteRoomFromZone(roomID);
                    break;
                }
            }
            catch (Exception e){}
        }
    }

    /**
     * return the zone ID of the zone where a room, identified by room ID, is part of
     * @param roomID
     * @return
     */
    public int getZoneOfRoom(int roomID) {
        int zoneID = 0;
        boolean zoneFound = false;
        for (int z = 0; z < zones.length; z++) {
            try {
                for (int r = 0; r < zones[z].getZoneRoomIDs().length; r++) {
                    if (zones[z].getZoneRoomIDs()[r]==roomID) {
                        zoneID = zones[z].getZoneID();
                        zoneFound = true;
                        break;
                    }
                }
                if (zoneFound) {
                    break;
                }
            }
            catch (Exception e){}
        }
        return zoneID;
    }

    /**
     * return an integer array of room IDs of rooms that are to be relocated to another zone, new or existing
     * @param hostPane
     * @return
     */
    public int[] getRoomIDsToMoveInAnotherZone(AnchorPane hostPane) {
        String roomIDsString = "";
        try {
            // in the host pane...
            for (int hp_elem = 0; hp_elem < hostPane.getChildren().size(); hp_elem++) {
                try {
                    // for each zone pane...
                    if (hostPane.getChildren().get(hp_elem).getId().contains("zonePaneFor")) {
                        AnchorPane zonePane = (AnchorPane) hostPane.getChildren().get(hp_elem);

                        // figure out how many room checkboxes are selected...
                        for (int zp = 0; zp < zonePane.getChildren().size(); zp++) {
                            try {
                                if (zonePane.getChildren().get(zp).getId().contains("zoneRoomBoxID#")) {
                                    CheckBox cb = (CheckBox) zonePane.getChildren().get(zp);
                                    if (cb.isSelected()) {
                                        roomIDsString += zonePane.getChildren().get(zp).getId().substring(14) + " ";
                                    }
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

        if (!roomIDsString.equals("")) {
            String[] stringIDsArray = (roomIDsString.strip()).split(" ");

            for (int s = 0; s < stringIDsArray.length; s++) {
                System.out.println("DEBUG -- " + stringIDsArray[s]);
            }

            int[] ids = new int[stringIDsArray.length];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = Integer.parseInt(stringIDsArray[i]);
            }
            return ids;
        }
        else {
            return null;
        }
    }

    /**
     * return the current number of zones in the SHH module
     * @return
     */
    public int getCurrentNumberOfZones() {
        return currentNumberOfZones;
    }

    /**
     * when a new zone is added, increment the number of zones by 1
     */
    public void incrementNumberOfZones() {
        this.currentNumberOfZones++;
    }

    /**
     * get the outdoor temperature
     * @return
     */
    public double getOutdoorTemperature() {
        return outdoorTemperature;
    }

    /**
     * set the outdoor temperature
     * @param outdoorTemperature
     */
    public void setOutdoorTemperature(double outdoorTemperature) {
        this.outdoorTemperature = outdoorTemperature;
    }

    /**
     * get the default winter temperature for AWAY mode
     * @return
     */
    public double getWinterTemperature() {
        return winterTemperature;
    }

    /**
     * set the default winter temperature for AWAY mode
     * @param winterTemperature
     */
    public void setWinterTemperature(double winterTemperature) {

        /**TODO: Put the contents of this method inside an IF condition;
         *  the condition is that Main.currentActiveProfile
         *  has permission to change a room's temperature */

        this.winterTemperature = winterTemperature;
        changeSHHTempLabel("winterTempSHHLabel", winterTemperature);
    }

    /**
     * get the default summer temperature for AWAY mode
     * @return
     */
    public double getSummerTemperature() {
        return summerTemperature;
    }

    /**
     * set the default summer temperature for AWAY mode
     * @param summerTemperature
     */
    public void setSummerTemperature(double summerTemperature) {

        /**TODO: Put the contents of this method inside an IF condition;
         *  the condition is that Main.currentActiveProfile
         *  has permission to change a room's temperature */

        this.summerTemperature = summerTemperature;
        changeSHHTempLabel("summerTempSHHLabel", summerTemperature);
    }

    /**
     * return an array of the zones within the SHH module
     * @return
     */
    public Zone[] getZones() {
        return zones;
    }

    /**
     * set the array of zones in the SHH module
     * @param zones
     */
    public void setZones(Zone[] zones) {
        this.zones = zones;
    }

    /**
     * override a zone's temperature (which all of its rooms will take on as a result), but
     * only if the simulation is in AWAY mode
     * @param zoneID
     * @param newTemp
     */
    public void overrideZoneTemperature(int zoneID, double newTemp) {
        if (!awayModeON) {
            for (int z = 0; z < this.zones.length; z++) {
                try {
                    if (this.zones[z].getZoneID() == zoneID) {

                        for (int r_id = 0; r_id < this.zones[z].getZoneRoomIDs().length; r_id++) {
                            try {
                                for (int hl = 0; hl < Main.householdLocations.length; hl++) {
                                    try {
                                        if (Main.householdLocations[hl].getRoomID()==r_id) {
                                            this.zones[z].setZoneTemperature(newTemp);
                                            break;
                                        }
                                    }
                                    catch (Exception e){}
                                }
                            }
                            catch (Exception e){}
                        }
                        break;
                    }
                }
                catch (Exception e){}
            }
            notifyToOpenZoneWindows(zoneID);
        }
    }

    /**
     * override a specific room's temperature in a zone
     * @param zoneID
     * @param roomID
     * @param newTemp
     */
    public void overrideTempInSpecificRoomInZone(int zoneID, int roomID, double newTemp) {

        if (!awayModeON) {
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
                        }
                        else {
                            throw new Exception("ERROR [SHH]: Room #"+roomID+" is not in Zone "+zoneID);
                        }
                        break;
                    }
                }
                notifyToOpenZoneWindows(zoneID);
            }
            catch (Exception e){
                Controller.appendMessageToConsole(e.getMessage());
            }
        }
    }

    /**
     * Adjust the temperature of each zone to the default summer temperature during AWAY mode
     */
    public void setAllZoneTempsToSummerTemp() {
        for (int z = 0; z < this.zones.length; z++) {
            try {
                overrideZoneTemperature(this.zones[z].getZoneID(), this.summerTemperature);
            }
            catch (Exception e){}
        }
    }

    /**
     * Adjust the temperature of each zone to the default winter temperature during AWAY mode
     */
    public void setAllZoneTempsToWinterTemp() {
        for (int z = 0; z < this.zones.length; z++) {
            try {
                overrideZoneTemperature(this.zones[z].getZoneID(), this.winterTemperature);
            }
            catch (Exception e){}
        }
    }

    /**
     * give the SHH module the signal whenever the AWAY mode is toggled on or off, and adjust temperatures as needed
     */
    public void notifySHHOFAwayMode() {
        if (SHSHelpers.isIs_away()) {
            if (Main.shhModule.isSummer()) {
                try {
                    setAllZoneTempsToSummerTemp();
                }
                catch (Exception e){}
            }
            else if (Main.shhModule.isWinter()) {
                try {
                    setAllZoneTempsToWinterTemp();
                }
                catch (Exception e){}
            }
            awayModeON = true;
        }
        else {
            awayModeON = false;
        }
    }

    /**
     * automatically open all windows is a specific zone when the outside temperature is cooler than the zone's temperature
     * @param zoneID
     */
    public void notifyToOpenZoneWindows(int zoneID) {
        try {
            double outdoorTemp = Main.outsideTemperature;
            for (int z = 0; z < this.zones.length; z++) {
                try {
                    if (this.zones[z].getZoneID() == zoneID) {
                        for (int room = 0; room < this.zones[z].getZoneRoomIDs().length; room++) {
                            try {
                                for (int h = 0; h < Main.householdLocations.length; h++) {
                                    try {
                                        if (Main.householdLocations[h].getRoomID() == this.zones[z].getZoneRoomIDs()[room]) {
                                            double roomTemp = Main.householdLocations[h].getRoomTemperature();
                                            if ((outdoorTemp < roomTemp) && isSummer() && !SHSHelpers.isIs_away()) {
                                                try {
                                                    /**todo: fix repetition bug (implementation okay) */
                                                    Main.house.autoOpenWindows(Main.householdLocations[h]);
                                                }
                                                catch (Exception e){}
                                            }
                                            break;
                                        }
                                    } catch (Exception F) {
                                        System.out.println();
                                    }
                                }
                            } catch (Exception G) {
                                System.out.println();
                            }
                        }
                        break;
                    }
                }
                catch (Exception e){}
            }
        }
        catch (Exception e){}
    }

    /**
     * automatically open all windows in all zones when the outside temperature is cooler than inside
     */
    public void notifyToOpenAllZoneWindows() {
        try {
            for (int z = 0 ; z < this.zones.length; z++) {
                try {
                    notifyToOpenZoneWindows(this.zones[z].getZoneID());
                }
                catch (Exception e){}
            }
        }
        catch (Exception e){}
    }

    /**
     * update a temperature label in the SHH user interface
     * @param labelID
     * @param newTemp
     */
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
                    else if (tempLabel.getText().contains("Indoor")) {
                        tempLabel.setText("Indoor temperature: "+newTemp+"°C");
                    }
                    Main.SHH_MODULE.getChildren().set(e, tempLabel);
                    break;
                }
            }
            catch (Exception ex){}
        }
    }

    /**
     * update the AWAY mode status label in the SHH user interface
     * @param isAwayModeOn
     */
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

    /***
     * Create a brand new Zone that will hold the rooms in the Room[] array parameter.
     * This method will also be used when first uploading a house layout, placing all
     * household locations in a single, first zone by default.
     * @param roomArray
     */
    public void createNewZone(Room[] roomArray) {
        try {
            if (currentNumberOfZones == MAX_NUMBER_OF_ZONES) {
                Controller.appendMessageToConsole("ERROR [SHH]: Max # of zones cannot be exceeded");
                return;
            }

            Zone newZone = new Zone();
            for (int r = 0; r < roomArray.length; r++) {
                try {
                    newZone.addRoomToZone(roomArray[r]);
                }
                catch (Exception e){}
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
            newZone.initZoneTimePeriodSet();
            incrementNumberOfZones();
            updateSHSModuleWithZones();
        }
        catch (Exception e){
            Controller.appendMessageToConsole(e.getMessage());
        }
    }

    /**
     * Update the user interface of the SHH module when a new zone has been added
     */
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

        int transY = 150; int transX = 50;
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
                            double newTemperature = Double.parseDouble(changeZoneTempTF.getText());
                            overrideZoneTemperature(this.zones[finalZ].getZoneID(), newTemperature);
                            if (!awayModeON) {
                                try {
                                    changeZoneInfoPane(zonePane, newTemperature, this.zones[finalZ]);
                                }
                                catch (Exception ex){}
                            }
                            else {
                                Controller.appendMessageToConsole("Cannot manually change zone "+this.zones[finalZ].getZoneID()+" temp "+
                                        "during Away mode");
                                changeZoneTempTF.clear();
                                changeZoneTempTF.setPromptText("Change");
                            }
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
                                        tempHypLink.setTranslateY(tempTransY - 5);
                                        tempHypLink.setTranslateX(tempTransX+260);
                                        tempHypLink.setId("linkToChangeTempOfRoom#"+Main.householdLocations[h].getRoomID());

                                        int finalH = h;
                                        tempHypLink.setOnAction(e->{
                                            try {
                                                if (Main.currentActiveProfile.getPermRoomTemp()) {

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

                                                    changeSpecRoomTempButton.setOnAction(ev->{
                                                        try {
                                                            double newTemperature = Double.parseDouble(textField.getText());
                                                            overrideTempInSpecificRoomInZone(this.zones[finalZ].getZoneID(), Main.householdLocations[finalH].getRoomID(), newTemperature);
                                                            if (!awayModeON) {
                                                                changeZoneInfoPaneSpecificRoom(zonePane, newTemperature, this.zones[finalZ], Main.householdLocations[finalH].getRoomID());
                                                            }
                                                            else {
                                                                Controller.appendMessageToConsole("Cannot change temp of Room #"+
                                                                        Main.householdLocations[finalH].getRoomID()+" in zone "+this.zones[finalZ].getZoneID()+ " " +
                                                                        "during Away mode");
                                                                textField.clear();
                                                                textField.setPromptText("New temp");
                                                            }
                                                        }
                                                        catch (Exception exception) {
                                                            Controller.appendMessageToConsole("Invalid attempt to change temperature of Room #"+
                                                                    Main.householdLocations[finalH].getRoomID()+" in zone "+this.zones[finalZ].getZoneID());
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
                                                    changeRoomTempStage.show();
                                                }
                                                else {
                                                    throw new Exception("ERROR [SHH]: Permission denied for manually modifying specific room temperatures.");
                                                }
                                            }
                                            catch (Exception ex){}
                                        });

                                        zonePane.getChildren().add(tempHypLink);
                                        tempTransY+=30;
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
                    zoneStage.show();
                });
                Main.SHH_MODULE.getChildren().add(zoneInfoButton);
                transY+=30;
            }
            catch (Exception e){}
        }
    }

    /**
     * update the user interface of a specific Zone's information after that zone's temperature has been mutated
     * @param pane
     * @param temperature
     * @param zone
     */
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

    /**
     * update the user interface of a specific Zone's information after a specific room in that zone
     * has its temperature overridden
     * @param pane
     * @param temperature
     * @param zone
     * @param roomID
     */
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
                                                " ("+temperature+"°C {overridden})");
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
