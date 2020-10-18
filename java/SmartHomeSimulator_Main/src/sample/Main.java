package sample;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
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
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.concurrent.*;

public class Main extends Application {

    /**FIXED PIXEL DIMENSIONS*/
    protected static final int DASHBOARD_WIDTH = 700;
    protected static final int DASHBOARD_HEIGHT = 1300;
    protected static final int LOGINPAGE_WIDTH = 400;
    protected static final int LOGINPAGE_HEIGHT = 700;

    /**STAGE ELEMENTS*/
    protected static Stage main_stage;
    protected static Stage profileBox;
    protected static Stage editContextStage;

    /**SCENE ELEMENTS*/
    protected static Scene dashboardScene;
    protected static Scene profileScene;
    protected static Scene editContextScene;
    protected static Scene editContextScene2;

    /**PANES FOR USER-INTERFACE ELEMENTS*/
    protected static AnchorPane editContextLayout2;
    protected static AnchorPane editContextLayout;
    protected static AnchorPane profileSelection;
    protected static AnchorPane main_dashboard;

    /**PANES FOR SIMULATION MODULES*/
    protected static AnchorPane SHS_MODULE;
    protected static AnchorPane SHC_MODULE;
    protected static AnchorPane SHP_MODULE;
    protected static AnchorPane SHH_MODULE;

    /**G.U.I ELEMENTS FOR THE MAIN DASHBOARD */
    protected static Button editContextButton;
    protected static TextArea outputConsole;
    protected static AnchorPane houseLayout;
    protected static TabPane modulesInterface;
    protected static boolean simulationIsOn;

    /**todo: use these variables to load a house layout text file */
    protected static FileChooser fileChooser;
    protected static String houseLayoutFileName;
    protected static File houseLayoutFile;
    protected static String houseLayoutFilePathName;

    /**G.U.I ELEMENTS FOR THE SHP MODULE */
    protected static boolean is_away;
    protected static int timeLimitBeforeAlert;
    protected static TextArea suspBox;

    /**G.U.I ELEMENTS FOR THE SHS MODULE */
    protected static int numberOfProfiles = 0;
    protected static int numberOfTimesDashboardPageLoaded = 0;
    protected static int numberOfTimesProfilePageSelected = 0;

    /**USER PROFILE STATIC VARIABLES */
    protected static UserProfile[] profiles;
    protected static UserProfile currentActiveProfile;
    protected static Hyperlink[] profileLinks;

    /**STATIC VARIABLES AND METHODS FOR THE HOUSE */
    /**TODO: create more protected static variables household elements*/
    protected static double outsideTemperature;
    protected static Room[] householdLocations;
    protected static Room currentLocation;
    protected static House house;
    public static Room[] getHouseholdLocations() {
        return householdLocations;
    }
    public static AnchorPane getMain_dashboard() {return main_dashboard;}

    /**
     * START method for the JavaFX Application "Smart Home Simulator"
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        /**
         * TODO: create a House object and read the input of a house layout file
         * TODO: and extract information; all global House info will be initialized here... */


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

        //house = new House("dummyfile"); commented out for testing
        //houseLayout = house.getLayout();

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        main_stage = primaryStage;
        main_stage.setResizable(false);

        /**PROFILE SELECTION PAGE INITIALIZATION*/
        profileSelection = new AnchorPane();
        profileScene = new Scene(profileSelection, LOGINPAGE_HEIGHT, LOGINPAGE_WIDTH);

        /**MODULE INITIALIZATIONS */
        SHS_MODULE = new AnchorPane();
        SHC_MODULE = new AnchorPane();
        SHP_MODULE = new AnchorPane();
        SHH_MODULE = new AnchorPane();

        /**MAIN DASHBOARD INITIALIZATIONS */
        main_dashboard = new AnchorPane();

        // when first running the program, there should be a file selector button where the house layout image will go

        createMainDashboardNecessities(); /**TODO: see the "TODO" instructions in this method...*/

        dashboardScene = new Scene(main_dashboard, DASHBOARD_HEIGHT, DASHBOARD_WIDTH);

        /**EDIT SIMULATION CONTEXT INITIALIZATIONS */
        editContextLayout = new AnchorPane();
        editContextScene = new Scene(editContextLayout, 650, 650);
        editContextLayout2 = new AnchorPane();
        editContextScene2 = new Scene(editContextLayout2, 650, 650);

        /**SET THE MAIN STAGE*/
        main_stage.setTitle("Smart Home Simulator - No user");
        main_stage.setScene(dashboardScene);
        main_stage.show();
    }

    /**
     * MAIN method to launch the JavaFX Application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Create or update the window for adding, editing, deleting, or managing user Profiles.
     */
    public static void transformProfileSelectionPageScene() {

        Button closeButton = new Button("Close");
        closeButton.setId("profilePageCloseButton");
        closeButton.setTranslateX(150); closeButton.setTranslateY(350);
        closeButton.setOnAction(e->Main.profileBox.close());

        Label profileListLabel = new Label();
        profileListLabel.setId("profileListLabel");
        profileListLabel.setTranslateX(350); profileListLabel.setTranslateY(40);
        profileListLabel.setText("LIST OF PROFILES:");

        TextField newProfileTextField = new TextField(); newProfileTextField.setPromptText("P,C,G,or S...");
        newProfileTextField.setId("newProfileTextField");
        newProfileTextField.setPrefWidth(85); newProfileTextField.setTranslateX(40);
        newProfileTextField.setTranslateY(290);

        RadioButton setAdministrator = new RadioButton("Set Administrator");
        setAdministrator.setId("setAdminButton");
        setAdministrator.setTranslateX(40); setAdministrator.setTranslateY(320);

        Button addButton = new Button("Add new\nProfile");
        addButton.setId("addNewProfileButton");
        addButton.setTranslateX(40); addButton.setTranslateY(350);
        addButton.setOnAction(e -> Controller.createNewProfile(newProfileTextField, setAdministrator));

        if (numberOfTimesProfilePageSelected == 0) {
            Main.profileSelection.getChildren().addAll(addButton);
            Main.profileSelection.getChildren().add(setAdministrator);
            Main.profileSelection.getChildren().add(newProfileTextField);
            Main.profileSelection.getChildren().add(profileListLabel);
            Main.profileSelection.getChildren().add(closeButton);
        }
        numberOfTimesProfilePageSelected++;
    }

    /**
     * Create or update the window for the main dashboard.
     */
    public static void createMainDashboardNecessities() {

        Rectangle rectangle = new Rectangle(); rectangle.setVisible(true); rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setStroke(javafx.scene.paint.Paint.valueOf("BLACK")); rectangle.setArcWidth(5.0); rectangle.setArcHeight(5.0);
        rectangle.setHeight(DASHBOARD_WIDTH); rectangle.setOpacity(0.95); rectangle.setWidth(110); rectangle.setFill(Color.AQUA);
        rectangle.setId("rectangle");

        Label simLabel = new Label(); simLabel.setText("SIMULATION"); simLabel.setTranslateX(15);
        simLabel.setId("simulationLabel");

        ToggleButton triggerSim = new ToggleButton(); triggerSim.setId("simulationOnOffButton");
        triggerSim.prefHeight(45); triggerSim.prefWidth(75); triggerSim.setText("Start\nSimulation");
        triggerSim.setTranslateY(30); triggerSim.setTranslateX(15); triggerSim.setTextAlignment(TextAlignment.CENTER);
        triggerSim.setOnAction(e-> sample.Controller.startSimulation(triggerSim, editContextButton, outputConsole, modulesInterface));
        triggerSim.setDisable(true);

        /**TODO: implement the remaining backend for Edit Context button. */
        editContextButton = new Button(); editContextButton.setId("editContextButton");
        editContextButton.prefHeight(45); editContextButton.prefWidth(75); editContextButton.setText("Edit\nContext");
        editContextButton.setTranslateY(90); editContextButton.setTranslateX(15); editContextButton.setTextAlignment(TextAlignment.CENTER);
        editContextButton.setOnAction(e->Controller.editContext());
        editContextButton.setDisable(true);

        Label temperatureLabel = new Label(); temperatureLabel.setId("temp");
        temperatureLabel.setText("Outside Temp.\n15°C"); temperatureLabel.setTextAlignment(TextAlignment.CENTER);
        temperatureLabel.setTranslateY(400); temperatureLabel.setTranslateX(15);

        /**TODO: find a way to display the real local date and time that updates every second */
        Label localDateTime = new Label(); localDateTime.setTextAlignment(TextAlignment.CENTER);
        localDateTime.setTranslateX(15); localDateTime.setTranslateY(600); localDateTime.setText("LOCAL TIME");
        localDateTime.setId("localDateAndTimeLabel");

        Label dateText = new Label(); dateText.setTextAlignment(TextAlignment.CENTER);
        dateText.setId("dateText");
        dateText.setTranslateX(15); dateText.setTranslateY(620);

        Label timeText = new Label(); timeText.setTextAlignment(TextAlignment.CENTER);
        timeText.setId("timeText");
        timeText.setTranslateX(15); timeText.setTranslateY(640);
        timeText.setText("TEST");

        //thread to start the current date/time display when application starts
        new Thread(()->{sample.Controller.CurrentDate(dateText, timeText);}).start();

        outputConsole = new TextArea(); outputConsole.setId("OutputConsole");
        outputConsole.setPrefHeight(130); outputConsole.setPrefWidth(450); outputConsole.setTranslateY(540);
        outputConsole.setTranslateX(150); outputConsole.setWrapText(true);
        outputConsole.setDisable(true);

        Label outputConsoleLabel = new Label(); outputConsoleLabel.setId("outputConsoleLabel");
        outputConsoleLabel.setText("Output Console"); outputConsoleLabel.setTextAlignment(TextAlignment.CENTER);
        outputConsoleLabel.setTranslateY(520); outputConsoleLabel.setTranslateX(150);

        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setTitle("Load House Layout File");
        Button chooseFileButton = new Button("Select a .txt file\nfor house layout...");
        chooseFileButton.setTranslateY(300); chooseFileButton.setTranslateX(900);
        chooseFileButton.setOnAction(e -> {
            houseLayoutFile = fileChooser.showOpenDialog(main_stage);
            if (houseLayoutFile != null) {
                houseLayoutFileName = houseLayoutFile.getName();
                houseLayoutFilePathName = houseLayoutFile.getPath();
                houseLayoutFile.setReadOnly();
                try {
                    house = new House(houseLayoutFilePathName);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } /**TODO: implement File IO for reading a text file in
                 the constructor of the House class */
                householdLocations = house.getRooms();
                houseLayout = house.getLayout();
                houseLayout.setPrefHeight(675);
                houseLayout.setPrefWidth(675);
                houseLayout.setId("houseLayout");
                houseLayout.setTranslateX(615);
                houseLayout.setTranslateY(10);
                houseLayout.setDisable(true);
                main_dashboard.getChildren().remove(chooseFileButton);
                main_dashboard.getChildren().add(houseLayout);
                createMainDashboardNecessities();
            }
        });

        modulesInterface = createModuleInterface();
        modulesInterface.setPrefHeight(500); modulesInterface.setPrefWidth(500);
        modulesInterface.setTranslateX(110);
        modulesInterface.setId("modulesInterface");
        modulesInterface.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        modulesInterface.setStyle("-fx-border-width: 2; -fx-border-color: black;");

        if (numberOfTimesDashboardPageLoaded == 0) {
            main_dashboard.getChildren().add(rectangle);
            main_dashboard.getChildren().add(simLabel);
            main_dashboard.getChildren().add(triggerSim);
            main_dashboard.getChildren().add(editContextButton);
            main_dashboard.getChildren().add(temperatureLabel);
            main_dashboard.getChildren().add(localDateTime);
            main_dashboard.getChildren().add(outputConsole);
            main_dashboard.getChildren().add(outputConsoleLabel);
            //main_dashboard.getChildren().add(houseLayout);
            main_dashboard.getChildren().add(modulesInterface);
            main_dashboard.getChildren().add(chooseFileButton);
            main_dashboard.getChildren().add(dateText);
            main_dashboard.getChildren().add(timeText);
        }

        // is someone logged in?
        if (currentActiveProfile != null) {
            for (int a = 0; a < main_dashboard.getChildren().size(); a++) {
                try {
                    if (main_dashboard.getChildren().get(a).getId().equals("simulationOnOffButton")) {
                        if (houseLayout != null) {
                            ToggleButton tb = (ToggleButton) main_dashboard.getChildren().get(a);
                            tb.setDisable(false);
                            main_dashboard.getChildren().set(a, tb);
                        }
                    } else if (main_dashboard.getChildren().get(a).getId().equals("editContextButton")) {
                        if (houseLayout != null) {
                            Button b = (Button) main_dashboard.getChildren().get(a);
                            b.setDisable(false);
                            main_dashboard.getChildren().set(a, b);
                        }
                    } else if (main_dashboard.getChildren().get(a).getId().equals("OutputConsole")) {
                        TextArea ta = (TextArea) main_dashboard.getChildren().get(a);
                        if (simulationIsOn) {
                            ta.setDisable(false);
                        } else {
                            ta.setDisable(true);
                        }
                        main_dashboard.getChildren().set(a, ta);
                    } else if (main_dashboard.getChildren().get(a).getId().equals("simulationDate")) {
                        Label ta = (Label) main_dashboard.getChildren().get(a);
                        if (simulationIsOn) {
                            ta.setDisable(true);
                        } else {
                            ta.setDisable(true);
                        }
                        main_dashboard.getChildren().set(a, ta);
                    }
              /*  else if (main_dashboard.getChildren().get(a).getId().equals("simulationTime")) {
                    Label ta = (Label) main_dashboard.getChildren().get(a);
                    if (simulationIsOn) {
                        ta.setDisable(true);
                    }
                    else {
                        ta.setDisable(true);
                    }
                    main_dashboard.getChildren().set(a, ta);
                }*/
                    else if (main_dashboard.getChildren().get(a).getId().equals("houseLayout")) {
                        AnchorPane hl = (AnchorPane) main_dashboard.getChildren().get(a);
                        if (simulationIsOn) {
                            hl.setDisable(false);
                        } else {
                            hl.setDisable(true);
                        }
                        main_dashboard.getChildren().set(a, hl);
                    } else if (main_dashboard.getChildren().get(a).getId().equals("modulesInterface")) {
                        SHS_MODULE = makeSHSmodule();
                        TabPane tabPane = (TabPane) main_dashboard.getChildren().get(a);
                        Tab innerTab = tabPane.getTabs().get(0);
                        innerTab.setContent(SHS_MODULE);

                        //added code attempting to render simulationDate/Time visible
                        for (int i = 0; i < SHS_MODULE.getChildren().size(); i++) {
                            try {
                                if (SHS_MODULE.getChildren().get(i).getId().equals("simulationDate")) {
                                    Label l1 = (Label) SHS_MODULE.getChildren().get(i);
                                    l1.setVisible(true);
                                    SHS_MODULE.getChildren().set(i, l1);
                                    break;
                                }
                            } catch (Exception e) {
                            }
                        }

                        innerTab.setContent(SHS_MODULE);
                        tabPane.getTabs().set(0, innerTab);
                        main_dashboard.getChildren().set(a, tabPane);

                        // repeat for other modules
                    }
                } catch(Exception e){}
            }
        }
        // is nobody logged in?
        else {
            for (int a = 0; a < main_dashboard.getChildren().size(); a++) {
                try {
                    if (main_dashboard.getChildren().get(a).getId().equals("simulationOnOffButton")) {
                        ToggleButton tb = (ToggleButton) main_dashboard.getChildren().get(a);
                        tb.setDisable(true);
                        main_dashboard.getChildren().set(a, tb);
                    } else if (main_dashboard.getChildren().get(a).getId().equals("editContextButton")) {
                        Button b = (Button) main_dashboard.getChildren().get(a);
                        b.setDisable(true);
                        main_dashboard.getChildren().set(a, b);
                    } else if (main_dashboard.getChildren().get(a).getId().equals("OutputConsole")) {
                        TextArea ta = (TextArea) main_dashboard.getChildren().get(a);
                        ta.setDisable(true);
                        main_dashboard.getChildren().set(a, ta);
                    } else if (main_dashboard.getChildren().get(a).getId().equals("houseLayout")) {
                        AnchorPane hl = (AnchorPane) main_dashboard.getChildren().get(a);
                        hl.setDisable(true);
                        main_dashboard.getChildren().set(a, hl);
                    } else if (main_dashboard.getChildren().get(a).getId().equals("modulesInterface")) {
                        SHS_MODULE = makeSHSmodule();
                        TabPane tabPane = (TabPane) main_dashboard.getChildren().get(a);
                        Tab innerTab = tabPane.getTabs().get(0);
                        innerTab.setContent(SHS_MODULE);
                        tabPane.getTabs().set(0, innerTab);
                        main_dashboard.getChildren().set(a, tabPane);

                        // repeat for other modules
                    }
                }catch(Exception e){}
            }
        }
        numberOfTimesDashboardPageLoaded++;
    }

    /**
     * Instantiate the interface for the tabs of modules SHS, SHC, SHP, and SHH.
     * @return
     */
    public static TabPane createModuleInterface() {
        TabPane modules = new TabPane();
        Tab shcTab = new Tab("SHC", makeSHCmodule()); shcTab.setId("shcTab");
        Tab shpTab = new Tab("SHP", makeSHPmodule()); shpTab.setId("shpTab");
        Tab shhTab = new Tab("SHH", makeSHHmodule()); shhTab.setId("shhTab");
        Tab shsTab = new Tab("SHS", makeSHSmodule()); shsTab.setId("shsTab");
        modules.getTabs().addAll(shsTab, shcTab, shpTab, shhTab);
        return modules;
    }

    /**
     * Create or update the SHS module interface.
     * @return
     */
    public static AnchorPane makeSHSmodule() {
        SHS_MODULE = new AnchorPane();

        Button manageOrSwitchProfileButton = new Button("Manage or\nSwitch Profiles");
        manageOrSwitchProfileButton.setId("manageOrSwitchProfileButton");
        manageOrSwitchProfileButton.setTranslateX(200); manageOrSwitchProfileButton.setTranslateY(20);
        manageOrSwitchProfileButton.setTextAlignment(TextAlignment.CENTER);
        manageOrSwitchProfileButton.setOnAction(e -> Controller.returnToProfileSelectionPage());

        Line line = new Line(); line.setStartX(0); line.setEndX(500); line.setTranslateY(100);

        Label setHouseLocationLabel = new Label("Set House Location");
        setHouseLocationLabel.setTranslateX(200); setHouseLocationLabel.setTranslateY(110);

        /**TODO: code a method that will set the location when clicking this button */
        Button confirmLocationButton = new Button("Set Location"); confirmLocationButton.setId("confirmLocationButton");
        confirmLocationButton.setTranslateX(200); confirmLocationButton.setTranslateY(260);
        if ((currentActiveProfile==null)) {
            confirmLocationButton.setDisable(true);
        }
        else {
            if (simulationIsOn) {
                confirmLocationButton.setDisable(true);
            }
            else {
                confirmLocationButton.setDisable(false);
            }
        }

        Line line2 = new Line(); line2.setStartX(0); line2.setEndX(500); line2.setTranslateY(300);

        Label setDateTimeLabel = new Label("Set Date and Time");
        setDateTimeLabel.setTranslateX(200); setDateTimeLabel.setTranslateY(300);

        DatePicker datePicker = new DatePicker(); datePicker.setId("datePicker");
        datePicker.setTranslateX(150); datePicker.setTranslateY(340); datePicker.setPromptText("Select date...");
        if ((currentActiveProfile==null)) {
            datePicker.setDisable(true);
        }
        else {
            if (simulationIsOn) {
                datePicker.setDisable(true);
            }
            else {
                datePicker.setDisable(false);
            }
        }

        Label setTimeLabel = new Label("Hour     Minute");
        setTimeLabel.setTranslateX(200); setTimeLabel.setTranslateY(380);

        TextField hourField = new TextField(); hourField.setPrefHeight(30); hourField.setPrefWidth(40);
        hourField.setTranslateX(200); hourField.setTranslateY(400); hourField.setPromptText("hh");
        hourField.setId("hourField");
        if ((currentActiveProfile==null)) {
            hourField.setDisable(true);
        }
        else {
            if (simulationIsOn) {
                hourField.setDisable(true);
            }
            else {
                hourField.setDisable(false);
            }
        }

        Label colon = new Label(":");
        colon.setTranslateX(245); colon.setTranslateY(400);

        TextField minuteField = new TextField(); minuteField.setPrefHeight(30); minuteField.setPrefWidth(40);
        minuteField.setId("minuteField");
        minuteField.setTranslateX(250); minuteField.setTranslateY(400); minuteField.setPromptText("mm");
        if ((currentActiveProfile==null)) {
            minuteField.setDisable(true);
        }
        else {
            if (simulationIsOn) {
                minuteField.setDisable(true);
            }
            else {
                minuteField.setDisable(false);
            }
        }

        Label simulationDate = new Label(); simulationDate.setId("simulationDate");
        simulationDate.setTranslateX(400); simulationDate.setTranslateY(380);

        Label simulationTime = new Label(); simulationTime.setId("simulationTime");
        simulationTime.setTranslateX(400); simulationTime.setTranslateY(400);

        Button confirmTimeButton = new Button("Confirm New Time"); confirmTimeButton.setId("confirmTimeButton");
        confirmTimeButton.setTranslateX(200); confirmTimeButton.setTranslateY(435);
        confirmTimeButton.setTextAlignment(TextAlignment.CENTER);
        confirmTimeButton.setOnAction(e -> {new Thread(()->{sample.Controller.CurrentDateSimulation(datePicker, simulationDate, simulationTime, hourField, minuteField);}).start();});

        if ((currentActiveProfile==null)) {
            confirmTimeButton.setDisable(true);
        }
        else {
            if (simulationIsOn) {
                confirmTimeButton.setDisable(true);
            }
            else {
                confirmTimeButton.setDisable(false);
            }
        }

        try {
            SHS_MODULE.getChildren().addAll(manageOrSwitchProfileButton, line, setDateTimeLabel, datePicker,
                    setTimeLabel, simulationDate, simulationTime, hourField, colon, minuteField, confirmTimeButton, line2, setHouseLocationLabel,
                    confirmLocationButton);
        }catch (Exception e){}

        return SHS_MODULE;
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

//        Button confirmButton = new Button("Confirm"); confirmButton.setTranslateY(70); confirmButton.setTranslateX(270); confirmButton.setDisable(true);
//        confirmButton.setOnAction(e->sample.Controller.setTimeLimitAwayMode(timeBox, timeLimit));
//        shp_module.getChildren().add(confirmButton);
//
//        ToggleButton tb = new ToggleButton(); tb.setText("Set to AWAY mode"); tb.setTranslateX(250);
//        tb.setOnAction(e->sample.Controller.toggleAwayButton(tb, timeBox, timeLimit, confirmButton)); shp_module.getChildren().add(tb);

        return shp_module;
    }
}
