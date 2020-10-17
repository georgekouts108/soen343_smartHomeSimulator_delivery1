package sample;
import utilities.*;
import house.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import sample.Controller.*;

import javax.sound.sampled.Control;
import javax.swing.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.concurrent.*;

public class Main extends Application {

    /**FIXED PIXEL DIMENSIONS*/
    protected static final int DASHBOARD_WIDTH = 700;
    protected static final int DASHBOARD_HEIGHT = 1300;
    protected static final int LOGINPAGE_WIDTH = 400;
    protected static final int LOGINPAGE_HEIGHT = 700;

    /**FIXED GUI ELEMENTS*/
    protected static Stage main_stage;
    protected static Stage profileBox;
    protected static Stage editContextStage;

    protected static Scene dashboardScene;
    protected static Scene profileScene;
    protected static Scene editContextScene;
    protected static Scene editContextScene2; // will be used for moving inhabitants around the house

    protected static AnchorPane editContextLayout2; // will be used for moving inhabitants around the house
    protected static AnchorPane editContextLayout;
    protected static AnchorPane profileSelection;
    protected static AnchorPane main_dashboard;

    /**GLOBAL SIMULATION VARIABLES - START*/
    // main dashboard
    protected static Button editContextButton; // LINE 139
    protected static TextArea outputConsole; // LINE 157
    protected static AnchorPane houseLayout; // LINE 169
    protected static TabPane modulesInterface; // LINE 176
    protected static boolean simulationIsOn;

    // SHP module
    protected static boolean is_away;
    protected static int timeLimitBeforeAlert;
    protected static TextArea suspBox;

    // SHC module

    // SHS module
    protected static int numberOfProfiles = 0; // the number of user profiles for a House

    /**TODO: implement everything about user profiles*/
    protected static UserProfile[] profiles; // an array of UserProfile objects
    protected static UserProfile currentActiveProfile; // the current logged in profile
    protected static Hyperlink[] profileLinks;

    /**HOUSE INFO VARIABLES */
    /**TODO: @AntoTurc -- create more protected static variables houshold elements*/
    protected static int numberOfRooms;
    protected static double outsideTemperature;
    protected static Room[] householdLocations; // an array of household locations
    protected static Room currentLocation; // the current location or room of the logged in user

    public static Room[] getHouseholdLocations() {
        return householdLocations;
    }

    public static AnchorPane getMain_dashboard() {return main_dashboard;}
    /**GLOBAL SIMULATION VARIBLES - END*/

    @Override
    public void start(Stage primaryStage) throws Exception {

        /**
         * TODO: create a House object and read the input of a house layout file
         * TODO: and extract information; all global House info will be initialized here... */

//        House house = new House("dummyfile");

        Room testRoom = new Room("Kitchen", 2, 1, 5, true);
        Room testRoom2 = new Room("Bedroom", 1, 2, 2, true);
        Room testRoom3 = new Room("Dining Room", 1, 1, 2, true);
        Room testRoom4 = new Room("Bathroom", 2, 2, 5,  true);
        Room testRoom5 = new Room("Family Room", 1, 1, 4, false);
        Room testRoom6 = new Room("TV Room", 1, 2, 3, false);
        Room testRoom7 = new Room("Basement", 2, 1, 5, false);
        Room testRoom8 = new Room("Garage", 1, 2, 2, false);
        Room testRoom9 = new Room("Bedroom 2", 1, 1, 2, false);

        householdLocations = new Room[]{testRoom, testRoom2, testRoom3, testRoom4, testRoom5, testRoom6, testRoom7,
        testRoom8, testRoom9};

        House house = new House("dummyfile");
        houseLayout = house.getLayout();

        main_stage = primaryStage;
        main_stage.setResizable(false);
        profileSelection = new AnchorPane();
        profileScene = new Scene(profileSelection, LOGINPAGE_HEIGHT, LOGINPAGE_WIDTH);

        main_dashboard = new AnchorPane();
        createMainDashboardNecessities();
        dashboardScene = new Scene(main_dashboard, DASHBOARD_HEIGHT, DASHBOARD_WIDTH);

        editContextLayout = new AnchorPane();
        editContextScene = new Scene(editContextLayout, 650, 650);
        editContextLayout2 = new AnchorPane();
        editContextScene2 = new Scene(editContextLayout2, 650, 650);

        main_stage.setTitle("Smart Home Simulator - No user");
        main_stage.setScene(dashboardScene);
        main_stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void transformProfileSelectionPageScene() {

        Button closeButton = new Button("Close");
        closeButton.setId("profilePageCloseButton");
        closeButton.setTranslateX(150); closeButton.setTranslateY(350);
        closeButton.setOnAction(e->Main.profileBox.close());
        Main.profileSelection.getChildren().add(closeButton);

        Label profileListLabel = new Label();
        profileListLabel.setId("profileListLabel");
        profileListLabel.setTranslateX(350); profileListLabel.setTranslateY(40);
        profileListLabel.setText("LIST OF PROFILES:"); Main.profileSelection.getChildren().add(profileListLabel);

        TextField newProfileTextField = new TextField(); newProfileTextField.setPromptText("P,C,G,or S...");
        newProfileTextField.setId("newProfileTextField");
        newProfileTextField.setPrefWidth(85); newProfileTextField.setTranslateX(40);
        newProfileTextField.setTranslateY(290); Main.profileSelection.getChildren().add(newProfileTextField);

        RadioButton setAdminitrator = new RadioButton("Set Administrator");
        setAdminitrator.setId("setAdminButton");
        setAdminitrator.setTranslateX(40); setAdminitrator.setTranslateY(320);
        Main.profileSelection.getChildren().add(setAdminitrator);

        Button addButton = new Button("Add new\nProfile");
        addButton.setId("addNewProfileButton");
        addButton.setTranslateX(40); addButton.setTranslateY(350);
        addButton.setOnAction(e -> Controller.createNewProfile(newProfileTextField, setAdminitrator));
        Main.profileSelection.getChildren().addAll(addButton);
    }

    public static void createMainDashboardNecessities() { // THIS ONLY CREATES THE NECESSITIES OF THE MAIN DASHBOARD

        AnchorPane anchorPane = new AnchorPane();

        Rectangle rectangle = new Rectangle(); rectangle.setVisible(true); rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setStroke(javafx.scene.paint.Paint.valueOf("BLACK")); rectangle.setArcWidth(5.0); rectangle.setArcHeight(5.0);
        rectangle.setHeight(DASHBOARD_WIDTH); rectangle.setOpacity(0.95); rectangle.setWidth(110); rectangle.setFill(Color.AQUA);
        rectangle.setId("rectangle");
        anchorPane.getChildren().add(rectangle);

        Label simLabel = new Label(); simLabel.setText("SIMULATION"); simLabel.setTranslateX(15);
        simLabel.setId("simulationLabel");
        anchorPane.getChildren().add(simLabel);

        ToggleButton triggerSim = new ToggleButton(); triggerSim.setId("simulationOnOffButton");
        triggerSim.prefHeight(45); triggerSim.prefWidth(75); triggerSim.setText("Start\nSimulation");
        triggerSim.setTranslateY(30); triggerSim.setTranslateX(15); triggerSim.setTextAlignment(TextAlignment.CENTER);
        triggerSim.setOnAction(e-> sample.Controller.startSimulation(triggerSim, editContextButton, outputConsole, modulesInterface));
        anchorPane.getChildren().add(triggerSim);

        /**TODO: implement the remaining backend for Edit Context button. */
        editContextButton = new Button(); editContextButton.setDisable(true); editContextButton.setId("editContextButton");
        editContextButton.prefHeight(45); editContextButton.prefWidth(75); editContextButton.setText("Edit\nContext");
        editContextButton.setTranslateY(90); editContextButton.setTranslateX(15); editContextButton.setTextAlignment(TextAlignment.CENTER);
        editContextButton.setOnAction(e->Controller.editContext()); anchorPane.getChildren().add(editContextButton);

        Label temperatureLabel = new Label(); temperatureLabel.setId("temp");
        temperatureLabel.setText("Outside Temp.\n15°C"); temperatureLabel.setTextAlignment(TextAlignment.CENTER);
        temperatureLabel.setTranslateY(400); temperatureLabel.setTranslateX(15);
        anchorPane.getChildren().add(temperatureLabel);

        /**TODO: find a way to display the real local date and time that updates every second */
        Label localDateTime = new Label(); localDateTime.setTextAlignment(TextAlignment.CENTER);
        localDateTime.setTranslateX(15); localDateTime.setTranslateY(600); localDateTime.setText("LOCAL TIME");
        localDateTime.setId("localDateAndTimeLabel"); anchorPane.getChildren().add(localDateTime);

        outputConsole = new TextArea(); outputConsole.setDisable(true); outputConsole.setId("OutputConsole");
        outputConsole.setPrefHeight(130); outputConsole.setPrefWidth(450); outputConsole.setTranslateY(540);
        outputConsole.setTranslateX(150); outputConsole.setWrapText(true);
        anchorPane.getChildren().add(outputConsole);

        Label outputConsoleLabel = new Label(); outputConsoleLabel.setId("outputConsoleLabel");
        outputConsoleLabel.setText("Output Console"); outputConsoleLabel.setTextAlignment(TextAlignment.CENTER);
        outputConsoleLabel.setTranslateY(520); outputConsoleLabel.setTranslateX(150);
        anchorPane.getChildren().add(outputConsoleLabel);

        houseLayout.setPrefHeight(675); houseLayout.setPrefWidth(675); houseLayout.setId("houseLayout");
        houseLayout.setTranslateX(615); houseLayout.setTranslateY(10); houseLayout.setDisable(true);
        anchorPane.getChildren().add(houseLayout);

        modulesInterface = createModuleInterface(); modulesInterface.setDisable(true);
        modulesInterface.setPrefHeight(500); modulesInterface.setPrefWidth(500);
        modulesInterface.setTranslateX(110);
        modulesInterface.setId("modulesInterface");
        modulesInterface.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        modulesInterface.setStyle("-fx-border-width: 2; -fx-border-color: black;");
        anchorPane.getChildren().add(modulesInterface);

        main_dashboard = anchorPane;
    }

    public static TabPane createModuleInterface() {
        /**TODO: globalize each module */
        TabPane modules = new TabPane();
        Tab shsTab = new Tab("SHS", makeSHSmodule()); shsTab.setId("shsTab");
        Tab shcTab = new Tab("SHC", makeSHCmodule()); shcTab.setId("shcTab");
        Tab shpTab = new Tab("SHP", makeSHPmodule()); shpTab.setId("shpTab");
        Tab shhTab = new Tab("SHH", makeSHHmodule()); shhTab.setId("shhTab");
        modules.getTabs().addAll(shsTab, shcTab, shpTab, shhTab);
        return modules;
    }

    /**TODO: give IDs to each GUI element in here */
    public static AnchorPane makeSHSmodule() {
        AnchorPane shs_module = new AnchorPane();

        Button manageOrSwitchProfileButton = new Button("Manage or\nSwitch Profiles");
        manageOrSwitchProfileButton.setTranslateX(200); manageOrSwitchProfileButton.setTranslateY(20);
        manageOrSwitchProfileButton.setTextAlignment(TextAlignment.CENTER);
        manageOrSwitchProfileButton.setOnAction(e -> Controller.returnToProfileSelectionPage());

        Line line = new Line(); line.setStartX(0); line.setEndX(500); line.setTranslateY(100);

        Label setHouseLocationLabel = new Label("Set House Location");
        setHouseLocationLabel.setTranslateX(200); setHouseLocationLabel.setTranslateY(110);

        /**TODO: for each room in the house, create a radio button with the room name; */

        /**TODO: code a method that will set the location when clicking this button */
        Button confirmLocationButton = new Button("Set Location");
        confirmLocationButton.setTranslateX(200); confirmLocationButton.setTranslateY(260);

        Line line2 = new Line(); line2.setStartX(0); line2.setEndX(500); line2.setTranslateY(300);

        Label setDateTimeLabel = new Label("Set Date and Time");
        setDateTimeLabel.setTranslateX(200); setDateTimeLabel.setTranslateY(300);

        DatePicker datePicker = new DatePicker();
        datePicker.setTranslateX(150); datePicker.setTranslateY(340); datePicker.setPromptText("Select date...");

        Label setTimeLabel = new Label("Hour     Minute");
        setTimeLabel.setTranslateX(200); setTimeLabel.setTranslateY(380);

        TextField hourField = new TextField(); hourField.setPrefHeight(30); hourField.setPrefWidth(40);
        hourField.setTranslateX(200); hourField.setTranslateY(400); hourField.setPromptText("hh");

        Label colon = new Label(":");
        colon.setTranslateX(245); colon.setTranslateY(400);

        TextField minuteField = new TextField(); minuteField.setPrefHeight(30); minuteField.setPrefWidth(40);
        minuteField.setTranslateX(250); minuteField.setTranslateY(400); minuteField.setPromptText("mm");

        /**TODO: code a method in class Controller that will set the new date and time "setDateTime(hourField, minuteField, datePicker)"*/
        Button confirmTimeButton = new Button("Confirm New Time");
        confirmTimeButton.setTranslateX(200); confirmTimeButton.setTranslateY(435);
        confirmTimeButton.setTextAlignment(TextAlignment.CENTER);
//        confirmTimeButton.setOnAction(e -> { });

        shs_module.getChildren().addAll(manageOrSwitchProfileButton, line, setDateTimeLabel, datePicker,
                setTimeLabel, hourField, colon, minuteField, confirmTimeButton, line2, setHouseLocationLabel,
                confirmLocationButton);

        return shs_module;
    }

    /**TODO: give IDs to each GUI element in here */
    public static AnchorPane makeSHCmodule() {
        AnchorPane shc_module = new AnchorPane();
        return shc_module;
    }

    /**THIS IS NOT FOR DELIVERY #1*/
    /**TODO: give IDs to each GUI element in here */
    public static AnchorPane makeSHHmodule() {
        AnchorPane shh_module = new AnchorPane();
        return shh_module;
    }

    /**THIS WAS NOT FOR DELIVERY #1*/
    /**TODO: give IDs to each GUI element in here */
    public static AnchorPane makeSHPmodule(){
        AnchorPane shp_module = new AnchorPane();

        Text warningText = new Text(); warningText.setTranslateY(40);
        warningText.setText("Enter the amount of time (minutes) " +
                "before alerting the authorities after any motion detectors are triggered (AWAY mode only):");
        warningText.setTextAlignment(TextAlignment.CENTER); warningText.setWrappingWidth(480); shp_module.getChildren().add(warningText);

        TextField timeBox = new TextField(); timeBox.setPrefHeight(Region.USE_COMPUTED_SIZE); timeBox.setPrefWidth(60);
        timeBox.setTranslateX(200); timeBox.setTranslateY(70); timeBox.setDisable(true);
        shp_module.getChildren().add(timeBox);

        Label timeLimit = new Label(); timeLimit.setText("Time before Alert: 0 min."); timeLimit.setTranslateY(70); timeLimit.setTranslateX(10);
        shp_module.getChildren().add(timeLimit); timeLimit.setVisible(false);

        Line line = new Line(); line.setStartX(0);line.setEndX(500); line.setTranslateY(120); shp_module.getChildren().add(line);

        Label suspiciousLabel = new Label("Suspicious Activity Log"); suspiciousLabel.setTranslateY(120); suspiciousLabel.setTranslateX(50);
        shp_module.getChildren().add(suspiciousLabel);

        suspBox = new TextArea(); suspBox.setPrefHeight(253); suspBox.setPrefWidth(357);
        suspBox.setTranslateX(50); suspBox.setTranslateY(140); suspBox.setWrapText(true);
        shp_module.getChildren().add(suspBox);

        Button confirmButton = new Button("Confirm"); confirmButton.setTranslateY(70); confirmButton.setTranslateX(270); confirmButton.setDisable(true);
        confirmButton.setOnAction(e->sample.Controller.setTimeLimitAwayMode(timeBox, timeLimit));
        shp_module.getChildren().add(confirmButton);

        ToggleButton tb = new ToggleButton(); tb.setText("Set to AWAY mode"); tb.setTranslateX(250);
        tb.setOnAction(e->sample.Controller.toggleAwayButton(tb, timeBox, timeLimit, confirmButton)); shp_module.getChildren().add(tb);

        return shp_module;
    }
}
