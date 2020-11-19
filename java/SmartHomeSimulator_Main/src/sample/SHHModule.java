package sample;
import house.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * SHH Class
 */
public class SHHModule extends Module {

    protected static Stage SHHZoneConfigStage;
    private static Scene SHHZoneConfigScene;
    private static AnchorPane SHHZoneConfigPane;
    private static int numberOfTimesZoneConfigSelected = 0;

    //private final int MAX_NUMBER_OF_ZONES = 4;
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

        Label winterTempSHHLabel = new Label("Winter temp (AWAY only): "+this.winterTemperature+"°C");
        winterTempSHHLabel.setId("winterTempSHHLabel");
        winterTempSHHLabel.setTranslateY(40);

        Label summerTempSHHLabel = new Label("Summer temp (AWAY only): "+this.summerTemperature+"°C");
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

        Label nullHouseError = new Label("ERROR:\nHouse layout\nnull");
        nullHouseError.setTranslateX(400); nullHouseError.setTranslateY(30);
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

        Label zonesLabel = new Label("Zones");
        zonesLabel.setId("zonesLabel");
        zonesLabel.setTranslateY(130);

        if (Main.numberOfTimesSHHModuleCreated==0) {
            pane.getChildren().addAll(awayModeSHHLabel, outdoorTempSHHLabel,
                    winterTempSHHLabel, summerTempSHHLabel, maxNumOfZonesSHHLabel,
                    currentNumOfZonesSHHLabel, borderLine1, configZoneButton, zonesLabel, nullHouseError);
        }

        Main.numberOfTimesSHHModuleCreated++;
        return pane;
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
                this.zones[z].setZoneTemperature(newTemp);
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

            for (int houseroomIndex = 0; houseroomIndex < Main.householdLocations.length; houseroomIndex++) {
                try {
                    System.out.println("DEBUG: room temp for "+Main.householdLocations[houseroomIndex].getName()+
                            " is "+Main.householdLocations[houseroomIndex].getRoomTemperature());
                }
                catch (Exception e){}
            }
            incrementNumberOfZones();
            updateSHSModuleWithZones();
        }
        catch (Exception e) {
            Controller.appendMessageToConsole(e.getMessage());
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
