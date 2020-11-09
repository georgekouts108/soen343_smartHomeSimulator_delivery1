package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import sample.Main.*;
import javax.sound.sampled.Control;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.*;
import java.io.*;

/**
 * Main class for the Smart Home Simulator application
 */
public class Main extends Application {

    /**FILE I/O */
    protected static FileOutputStream fileWriter;
    protected static File logFile;

    /**STRING ARRAY FOR COUNTRIES (USER FOR SETTING HOUSE LOCATION) */
    protected static String[] countries = Locale.getISOCountries();

    /**FIXED PIXEL DIMENSIONS*/
    protected static final int DASHBOARD_WIDTH = 700;
    protected static final int DASHBOARD_HEIGHT = 1300;
    protected static final int LOGINPAGE_WIDTH = 400;
    protected static final int LOGINPAGE_HEIGHT = 700;

    /**STAGE ELEMENTS*/
    protected static Stage main_stage;
    protected static Stage profileBox;
    protected static Stage editContextStage;
    protected static Stage awayLightsStage;

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

    protected static SHSModule shsModule = new SHSModule();
    protected static SHCModule shcModule = new SHCModule();
    protected static SHPModule shpModule = new SHPModule();
    protected static SHHModule shhModule = new SHHModule();

    /**G.U.I ELEMENTS FOR THE MAIN DASHBOARD */
    protected static Button editContextButton;
    protected static TextArea outputConsole;
    protected static AnchorPane houseLayout;
    protected static TabPane modulesInterface;
    protected static boolean simulationIsOn;

    protected static FileChooser fileChooser;
    protected static String houseLayoutFileName;
    protected static File houseLayoutFile;
    protected static String houseLayoutFilePathName;

    /**G.U.I ELEMENTS FOR THE SHP MODULE */
    protected static boolean is_away;
    protected static int timeLimitBeforeAlert = 0;
    protected static TextArea suspBox;
    protected static AnchorPane SHP_LightsConfigAWAYmode;
    protected static Scene SHP_LightsConfigAWAYscene;

    /**VARIABLES FOR COUNTING ACCESSES TO CERTAIN SCENES */
    protected static int numberOfProfiles = 0;
    protected static int numberOfTimesDashboardPageLoaded = 0;
    protected static int numberOfTimesProfilePageSelected = 0;
    protected static int numberOfTimesSHSModuleCreated = 0;
    protected static int numberOfTimesSHCModuleCreated = 0;
    protected static int numberOfTimesSHPModuleCreated = 0;
    protected static int numberOfTimesSHHModuleCreated = 0;

    /**USER PROFILE STATIC VARIABLES */
    protected static UserProfile[] profiles;
    protected static UserProfile currentActiveProfile;
    protected static Hyperlink[] profileLinks;
    protected static String profilesFile;

    /**STATIC VARIABLES AND METHODS FOR THE HOUSE */
    protected static double outsideTemperature;
    protected static Room[] householdLocations;
    protected static Room currentLocation;
    protected static House house;

    /**
     * Access the default house's array of Rooms
     * @return
     */
    public static Room[] getHouseholdLocations() {
        return householdLocations;
    }

    /**
     * Access the UI of the main dashboard
     * @return
     */
    public static AnchorPane getMain_dashboard() {return main_dashboard;}

    /**
     * Access the current active profile
     * @return
     */
    public static UserProfile getCurrentActiveProfile() {
        return currentActiveProfile;
    }

    /**
     * START method for the JavaFX Application "Smart Home Simulator"
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        shpModule.startTimeBoundaryThread();

        logFile = new File("src/log.txt");
        if (logFile.exists()) {
            logFile.createNewFile();
        }
        fileWriter = new FileOutputStream(logFile);

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
        createMainDashboardNecessities();

        dashboardScene = new Scene(main_dashboard, DASHBOARD_HEIGHT, DASHBOARD_WIDTH);

        /**EDIT SIMULATION CONTEXT INITIALIZATIONS */
        editContextLayout = new AnchorPane();
        editContextScene = new Scene(editContextLayout, 650, 650);
        editContextLayout2 = new AnchorPane();
        editContextScene2 = new Scene(editContextLayout2, 650, 650);

        /**SHP MODULE ANCHORPANES*/
        SHP_LightsConfigAWAYmode = new AnchorPane();
        SHP_LightsConfigAWAYscene = new Scene(SHP_LightsConfigAWAYmode, 350,225);

        /**SET THE MAIN STAGE*/
        main_stage.setTitle("Smart Home Simulator - No user");
        main_stage.setScene(dashboardScene);
        main_stage.show();

        String profilesTxt = "src/profiles.txt";
        loadProfilesFromFile(profilesTxt);
    }

    /**
     * Load profiles from a file with pre-saved permissions
     * @param file
     */
    public static void loadProfilesFromFile(String file){

        File profiles = new File(file);
        Scanner sc;
        try {
            sc = new Scanner(profiles);

            while(sc.hasNext()){

                //each line in file wil be a profile. example: child,false,true,false,...
                String[] profileParams = sc.nextLine().split(",");
                String newType = profileParams[0];

                boolean newPermLights = Boolean.parseBoolean(profileParams[1]);
                boolean newPermLightsLocation = Boolean.parseBoolean(profileParams[2]);

                boolean newPermDoors = Boolean.parseBoolean(profileParams[3]);
                boolean newPermDoorsLocation = Boolean.parseBoolean(profileParams[4]);

                boolean newPermWindows = Boolean.parseBoolean(profileParams[5]);
                boolean newPermWindowsLocation = Boolean.parseBoolean(profileParams[6]);

                boolean newPermAC = Boolean.parseBoolean(profileParams[7]);
                boolean newPermACLocation = Boolean.parseBoolean(profileParams[8]);
                Controller.createNewProfile(newType,newPermLights,newPermLightsLocation,newPermDoors,newPermDoorsLocation,newPermWindows,newPermWindowsLocation,newPermAC,newPermACLocation);
            }
            sc.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

        TextField newProfileTextField = new TextField(); newProfileTextField.setPromptText("Parent, Child, Guest or Stranger");
        newProfileTextField.setId("newProfileTextField");
        newProfileTextField.setPrefWidth(230); newProfileTextField.setTranslateX(40);
        newProfileTextField.setTranslateY(290);

        CheckBox cbPermL = new CheckBox("Lights");
        cbPermL.setId("lightsPermissionCB");
        cbPermL.setTranslateX(5+150); cbPermL.setTranslateY(100);

        CheckBox cbPermLL = new CheckBox("Lights (L)");
        cbPermLL.setId("lightsLocationPermissionCB");
        cbPermLL.setTranslateX(100+150); cbPermLL.setTranslateY(100);

        CheckBox cbPermD = new CheckBox("Doors");
        cbPermD.setId("DoorsPermissionCB");
        cbPermD.setTranslateX(5+150); cbPermD.setTranslateY(150);

        CheckBox cbPermDL = new CheckBox("Doors (L)");
        cbPermDL.setId("DoorsLocationPermissionCB");
        cbPermDL.setTranslateX(100+150); cbPermDL.setTranslateY(150);

        CheckBox cbPermW = new CheckBox("Windows");
        cbPermW.setId("WindowsPermissionCB");
        cbPermW.setTranslateX(5+150); cbPermW.setTranslateY(200);

        CheckBox cbPermWL = new CheckBox("Windows (L)");
        cbPermWL.setId("WindowsLocationPermissionCB");
        cbPermWL.setTranslateX(100+150); cbPermWL.setTranslateY(200);

        CheckBox cbPermAC = new CheckBox("AC");
        cbPermAC.setId("ACPermissionCB");
        cbPermAC.setTranslateX(5+150); cbPermAC.setTranslateY(250);

        CheckBox cbPermACL = new CheckBox("AC (L)");
        cbPermACL.setId("ACLocationPermissionCB");
        cbPermACL.setTranslateX(100+150); cbPermACL.setTranslateY(250);

        Button addButton = new Button("Add new\nProfile");
        addButton.setId("addNewProfileButton");
        addButton.setTranslateX(40); addButton.setTranslateY(350);
        addButton.setOnAction(e -> Controller.createNewProfile(newProfileTextField, cbPermL, cbPermLL, cbPermD, cbPermDL, cbPermW, cbPermWL, cbPermAC, cbPermACL));

        if (numberOfTimesProfilePageSelected == 0) {
            Main.profileSelection.getChildren().add(addButton);
            Main.profileSelection.getChildren().add(newProfileTextField);
            Main.profileSelection.getChildren().add(profileListLabel);
            Main.profileSelection.getChildren().add(closeButton);
            Main.profileSelection.getChildren().add(cbPermL);
            Main.profileSelection.getChildren().add(cbPermLL);
            Main.profileSelection.getChildren().add(cbPermD);
            Main.profileSelection.getChildren().add(cbPermDL);
            Main.profileSelection.getChildren().add(cbPermW);
            Main.profileSelection.getChildren().add(cbPermWL);
            Main.profileSelection.getChildren().add(cbPermAC);
            Main.profileSelection.getChildren().add(cbPermACL);
        }
        numberOfTimesProfilePageSelected++;
    }

    /**
     * Create or update the window for the main dashboard.
     */
    public static void createMainDashboardNecessities() {
        String graycss = "#e6e6e6";

        Rectangle rectangle = new Rectangle(); rectangle.setVisible(true); rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setStroke(javafx.scene.paint.Paint.valueOf("BLACK")); rectangle.setArcWidth(5.0); rectangle.setArcHeight(5.0);
        rectangle.setHeight(DASHBOARD_WIDTH); rectangle.setOpacity(0.95); rectangle.setWidth(110); rectangle.setId("rectangle");

        Label simLabel = new Label(); simLabel.setText("SIMULATION"); simLabel.setTranslateX(20);
        simLabel.setTranslateY(10);
        simLabel.setStyle("-fx-text-fill: " + graycss);
        simLabel.setId("simulationLabel");

        ToggleButton triggerSim = new ToggleButton(); triggerSim.setId("simulationOnOffButton");
        triggerSim.prefHeight(45); triggerSim.setMinWidth(75); triggerSim.setText("Start\nSimulation");
        triggerSim.setStyle("-fx-text-inner-color: " + graycss);
        triggerSim.setTranslateY(35); triggerSim.setTranslateX(18); triggerSim.setTextAlignment(TextAlignment.CENTER);
        triggerSim.setOnAction(e-> sample.Controller.startSimulation(triggerSim, editContextButton, outputConsole, modulesInterface));
        triggerSim.setDisable(true);

        editContextButton = new Button(); editContextButton.setId("editContextButton");
        editContextButton.prefHeight(45); editContextButton.setMinWidth(75); editContextButton.setText("Edit\nContext");
        editContextButton.setTranslateY(90); editContextButton.setTranslateX(18); editContextButton.setTextAlignment(TextAlignment.CENTER);
        editContextButton.setOnAction(e->Controller.editContext());
        editContextButton.setDisable(true);

        Label locationLabel = new Label("House\nLocation:\n[not set]");
        locationLabel.setId("locationLabel"); locationLabel.setTextAlignment(TextAlignment.CENTER);
        locationLabel.setTranslateX(18); locationLabel.setTranslateY(210);
        locationLabel.setStyle("-fx-background-color: " + graycss + "; -fx-min-width: 75px; -fx-min-height: 70px;" +
                "-fx-alignment: center");

        Label simDateAndTimeLabel = new Label("SIM TIME");
        simDateAndTimeLabel.setTranslateX(18); simDateAndTimeLabel.setTranslateY(300);
        simDateAndTimeLabel.setStyle("-fx-background-color: " + graycss + "; -fx-min-width: 75px; -fx-min-height: 30px;" +
                "-fx-alignment: center");

        Label simulationDate = new Label(); simulationDate.setId("simulationDate");
        simulationDate.setStyle("-fx-background-color: " + graycss + "; -fx-min-width: 75px; -fx-min-height: 30px;" +
                "-fx-alignment: center");
        simulationDate.setTranslateX(18); simulationDate.setTranslateY(320);

        Label simulationTime = new Label(); simulationTime.setId("simulationTime");
        simulationTime.setStyle("-fx-background-color: " + graycss + "; -fx-min-width: 75px; -fx-min-height: 30px;" +
                "-fx-alignment: center");
        simulationTime.setTranslateX(18); simulationTime.setTranslateY(340);

        Label temperatureLabel = new Label(); temperatureLabel.setId("temp");
        temperatureLabel.setText("Outside\nTemp.\n15°C"); temperatureLabel.setTextAlignment(TextAlignment.CENTER);
        temperatureLabel.setStyle("-fx-background-color: " + graycss + "; -fx-min-width: 75px; -fx-min-height: 70px;" +
                "-fx-alignment: center");
        temperatureLabel.setTranslateY(390); temperatureLabel.setTranslateX(18);

        Label localDateTime = new Label(); localDateTime.setTextAlignment(TextAlignment.CENTER);
        localDateTime.setTranslateX(18); localDateTime.setTranslateY(620); localDateTime.setText("LOCAL TIME");
        localDateTime.setId("localDateAndTimeLabel");
        localDateTime.setStyle("-fx-background-color: " + graycss + "; -fx-min-width: 75px; -fx-min-height: 20px; -fx-alignment: center");

        Label dateText = new Label(); dateText.setTextAlignment(TextAlignment.CENTER);
        dateText.setId("dateText");
        dateText.setStyle("-fx-background-color: " + graycss + "; -fx-min-width: 75px; -fx-min-height: 20px; -fx-alignment: center");
        dateText.setTranslateX(18); dateText.setTranslateY(640);

        Label timeText = new Label(); timeText.setTextAlignment(TextAlignment.CENTER);
        timeText.setId("timeText");
        timeText.setStyle("-fx-background-color: " + graycss + "; -fx-min-width: 75px; -fx-min-height: 20px; -fx-alignment: center");
        timeText.setTranslateX(18); timeText.setTranslateY(660);
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
        chooseFileButton.setId("chooseFileButton");
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
                }
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
        modulesInterface.setStyle("-fx-border-width: 0 2 2 0; -fx-border-color: #6e6e6e;");

        if (numberOfTimesDashboardPageLoaded == 0) {
            main_dashboard.getChildren().add(rectangle);
            main_dashboard.getChildren().add(simLabel);
            main_dashboard.getChildren().add(triggerSim);
            main_dashboard.getChildren().add(editContextButton);
            main_dashboard.getChildren().add(temperatureLabel);
            main_dashboard.getChildren().add(localDateTime);
            main_dashboard.getChildren().add(outputConsole);
            main_dashboard.getChildren().add(outputConsoleLabel);
            main_dashboard.getChildren().add(modulesInterface);
            main_dashboard.getChildren().add(chooseFileButton);
            main_dashboard.getChildren().add(dateText);
            main_dashboard.getChildren().add(timeText);
            main_dashboard.getChildren().add(locationLabel);
            main_dashboard.getChildren().add(simulationDate);
            main_dashboard.getChildren().add(simulationTime);
            main_dashboard.getChildren().add(simDateAndTimeLabel);
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
                    }

                    else if (main_dashboard.getChildren().get(a).getId().equals("houseLayout")) {
                        AnchorPane hl = (AnchorPane) main_dashboard.getChildren().get(a);
                        if (simulationIsOn) {
                            hl.setDisable(false);
                        } else {
                            hl.setDisable(true);
                        }
                        main_dashboard.getChildren().set(a, hl);
                    } else if (main_dashboard.getChildren().get(a).getId().equals("modulesInterface")) {

                        SHS_MODULE = shsModule.generateModule();
                        TabPane tabPane = (TabPane) main_dashboard.getChildren().get(a);
                        Tab innerTab = tabPane.getTabs().get(0);
                        innerTab.setContent(SHS_MODULE);

                        innerTab.setContent(SHS_MODULE);
                        tabPane.getTabs().set(0, innerTab);
                        main_dashboard.getChildren().set(a, tabPane);


                        shcModule.generateModule(SHC_MODULE);
                        TabPane tabPane2 = (TabPane) main_dashboard.getChildren().get(a);
                        Tab innerTab2 = tabPane2.getTabs().get(1);
                        innerTab2.setDisable(false);
                        tabPane2.getTabs().set(1, innerTab2);
                        main_dashboard.getChildren().set(a, tabPane2);


                        SHP_MODULE = shpModule.generateModule();
                        TabPane tabPane3 = (TabPane) main_dashboard.getChildren().get(a);
                        Tab innerTab3 = tabPane3.getTabs().get(2);
                        innerTab3.setDisable(false);
                        tabPane3.getTabs().set(2, innerTab3);
                        main_dashboard.getChildren().set(a, tabPane3);


                        shhModule.generateModule(SHH_MODULE);
                        TabPane tabPane4 = (TabPane) main_dashboard.getChildren().get(a);
                        Tab innerTab4 = tabPane4.getTabs().get(3);
                        innerTab4.setDisable(false);
                        tabPane4.getTabs().set(3, innerTab4);
                        main_dashboard.getChildren().set(a, tabPane4);
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
                    }
                    else if (main_dashboard.getChildren().get(a).getId().equals("editContextButton")) {
                        Button b = (Button) main_dashboard.getChildren().get(a);
                        b.setDisable(true);
                        main_dashboard.getChildren().set(a, b);
                    }
                    else if (main_dashboard.getChildren().get(a).getId().equals("OutputConsole")) {
                        TextArea ta = (TextArea) main_dashboard.getChildren().get(a);
                        ta.setDisable(true);
                        main_dashboard.getChildren().set(a, ta);
                    }
                    else if (main_dashboard.getChildren().get(a).getId().equals("houseLayout")) {
                        AnchorPane hl = (AnchorPane) main_dashboard.getChildren().get(a);
                        hl.setDisable(true);
                        main_dashboard.getChildren().set(a, hl);
                    }
                    else if (main_dashboard.getChildren().get(a).getId().equals("modulesInterface")) {

                        SHS_MODULE = shsModule.generateModule();
                        TabPane tabPane = (TabPane) main_dashboard.getChildren().get(a);
                        Tab innerTab = tabPane.getTabs().get(0);
                        innerTab.setContent(SHS_MODULE);
                        tabPane.getTabs().set(0, innerTab);
                        main_dashboard.getChildren().set(a, tabPane);

                        shcModule.generateModule(SHC_MODULE);
                        TabPane tabPane2 = (TabPane) main_dashboard.getChildren().get(a);
                        Tab innerTab2 = tabPane2.getTabs().get(1);
                        innerTab2.setDisable(true);
                        tabPane2.getTabs().set(1, innerTab2);
                        main_dashboard.getChildren().set(a, tabPane2);

                        SHP_MODULE = shpModule.generateModule();
                        TabPane tabPane3 = (TabPane) main_dashboard.getChildren().get(a);
                        Tab innerTab3 = tabPane3.getTabs().get(2);
                        innerTab3.setDisable(true);
                        tabPane3.getTabs().set(2, innerTab3);
                        main_dashboard.getChildren().set(a, tabPane3);

                        shhModule.generateModule(SHH_MODULE);
                        TabPane tabPane4 = (TabPane) main_dashboard.getChildren().get(a);
                        Tab innerTab4 = tabPane4.getTabs().get(3);
                        innerTab4.setDisable(true);
                        tabPane4.getTabs().set(3, innerTab4);
                        main_dashboard.getChildren().set(a, tabPane4);
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

        SHS_MODULE = shsModule.generateModule();
        shcModule.generateModule(SHC_MODULE);
        SHP_MODULE = shpModule.generateModule();
        shhModule.generateModule(SHH_MODULE);

        Tab shcTab = new Tab("SHC", SHC_MODULE); shcTab.setId("shcTab");
        Tab shpTab = new Tab("SHP", SHP_MODULE); shpTab.setId("shpTab");
        Tab shhTab = new Tab("SHH", SHH_MODULE); shhTab.setId("shhTab");
        Tab shsTab = new Tab("SHS", SHS_MODULE); shsTab.setId("shsTab");
        modules.getTabs().addAll(shsTab, shcTab, shpTab, shhTab);
        return modules;
    }
}
