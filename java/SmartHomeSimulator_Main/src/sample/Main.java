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
import java.util.concurrent.*;

public class Main extends Application {

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
    protected static int timeLimitBeforeAlert;
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

    /**STATIC VARIABLES AND METHODS FOR THE HOUSE */
    protected static double outsideTemperature;
    protected static Room[] householdLocations;
    protected static Room currentLocation;
    protected static House house;

    public static String[] getCountries() {
        return countries;
    }

    public static int getDashboardWidth() {
        return DASHBOARD_WIDTH;
    }

    public static int getDashboardHeight() {
        return DASHBOARD_HEIGHT;
    }

    public static int getLoginpageWidth() {
        return LOGINPAGE_WIDTH;
    }

    public static int getLoginpageHeight() {
        return LOGINPAGE_HEIGHT;
    }

    public static Stage getMain_stage() {
        return main_stage;
    }

    public static Stage getProfileBox() {
        return profileBox;
    }

    public static Stage getEditContextStage() {
        return editContextStage;
    }

    public static Scene getDashboardScene() {
        return dashboardScene;
    }

    public static Scene getProfileScene() {
        return profileScene;
    }

    public static Scene getEditContextScene() {
        return editContextScene;
    }

    public static Scene getEditContextScene2() {
        return editContextScene2;
    }

    public static AnchorPane getEditContextLayout2() {
        return editContextLayout2;
    }

    public static AnchorPane getEditContextLayout() {
        return editContextLayout;
    }

    public static AnchorPane getProfileSelection() {
        return profileSelection;
    }

    public static AnchorPane getShsModule() {
        return SHS_MODULE;
    }

    public static AnchorPane getShcModule() {
        return SHC_MODULE;
    }

    public static AnchorPane getShpModule() {
        return SHP_MODULE;
    }

    public static AnchorPane getShhModule() {
        return SHH_MODULE;
    }

    public static Button getEditContextButton() {
        return editContextButton;
    }

    public static TextArea getOutputConsole() {
        return outputConsole;
    }

    public static AnchorPane getHouseLayout() {
        return houseLayout;
    }

    public static TabPane getModulesInterface() {
        return modulesInterface;
    }

    public static boolean isSimulationIsOn() {
        return simulationIsOn;
    }

    public static FileChooser getFileChooser() {
        return fileChooser;
    }

    public static String getHouseLayoutFileName() {
        return houseLayoutFileName;
    }

    public static File getHouseLayoutFile() {
        return houseLayoutFile;
    }

    public static String getHouseLayoutFilePathName() {
        return houseLayoutFilePathName;
    }

    public static boolean isIs_away() {
        return is_away;
    }

    public static int getTimeLimitBeforeAlert() {
        return timeLimitBeforeAlert;
    }

    public static TextArea getSuspBox() {
        return suspBox;
    }

    public static int getNumberOfProfiles() {
        return numberOfProfiles;
    }

    public static int getNumberOfTimesDashboardPageLoaded() {
        return numberOfTimesDashboardPageLoaded;
    }

    public static int getNumberOfTimesProfilePageSelected() {
        return numberOfTimesProfilePageSelected;
    }

    public static int getNumberOfTimesSHSModuleCreated() {
        return numberOfTimesSHSModuleCreated;
    }

    public static int getNumberOfTimesSHCModuleCreated() {
        return numberOfTimesSHCModuleCreated;
    }

    public static int getNumberOfTimesSHPModuleCreated() {
        return numberOfTimesSHPModuleCreated;
    }

    public static int getNumberOfTimesSHHModuleCreated() {
        return numberOfTimesSHHModuleCreated;
    }

    public static UserProfile[] getProfiles() {
        return profiles;
    }

    public static UserProfile getCurrentActiveProfile() {
        return currentActiveProfile;
    }

    public static Hyperlink[] getProfileLinks() {
        return profileLinks;
    }

    public static double getOutsideTemperature() {
        return outsideTemperature;
    }

    public static Room getCurrentLocation() {
        return currentLocation;
    }

    public static House getHouse() {
        return house;
    }

    public static Room[] gethouseholdLocations(){ return householdLocations; }

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
     * START method for the JavaFX Application "Smart Home Simulator"
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

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

    public static void setCountries(String[] countries) {
        Main.countries = countries;
    }

    public static void setMain_stage(Stage main_stage) {
        Main.main_stage = main_stage;
    }

    public static void setProfileBox(Stage profileBox) {
        Main.profileBox = profileBox;
    }

    public static void setEditContextStage(Stage editContextStage) {
        Main.editContextStage = editContextStage;
    }

    public static void setDashboardScene(Scene dashboardScene) {
        Main.dashboardScene = dashboardScene;
    }

    public static void setProfileScene(Scene profileScene) {
        Main.profileScene = profileScene;
    }

    public static void setEditContextScene(Scene editContextScene) {
        Main.editContextScene = editContextScene;
    }

    public static void setEditContextScene2(Scene editContextScene2) {
        Main.editContextScene2 = editContextScene2;
    }

    public static void setEditContextLayout2(AnchorPane editContextLayout2) {
        Main.editContextLayout2 = editContextLayout2;
    }

    public static void setEditContextLayout(AnchorPane editContextLayout) {
        Main.editContextLayout = editContextLayout;
    }

    public static void setProfileSelection(AnchorPane profileSelection) {
        Main.profileSelection = profileSelection;
    }

    public static void setMain_dashboard(AnchorPane main_dashboard) {
        Main.main_dashboard = main_dashboard;
    }

    public static void setShsModule(AnchorPane shsModule) {
        SHS_MODULE = shsModule;
    }

    public static void setShcModule(AnchorPane shcModule) {
        SHC_MODULE = shcModule;
    }

    public static void setShpModule(AnchorPane shpModule) {
        SHP_MODULE = shpModule;
    }

    public static void setShhModule(AnchorPane shhModule) {
        SHH_MODULE = shhModule;
    }

    public static void setEditContextButton(Button editContextButton) {
        Main.editContextButton = editContextButton;
    }

    public static void setOutputConsole(TextArea outputConsole) {
        Main.outputConsole = outputConsole;
    }

    public static void setHouseLayout(AnchorPane houseLayout) {
        Main.houseLayout = houseLayout;
    }

    public static void setModulesInterface(TabPane modulesInterface) {
        Main.modulesInterface = modulesInterface;
    }

    public static void setSimulationIsOn(boolean simulationIsOn) {
        Main.simulationIsOn = simulationIsOn;
    }

    public static void setFileChooser(FileChooser fileChooser) {
        Main.fileChooser = fileChooser;
    }

    public static void setHouseLayoutFileName(String houseLayoutFileName) {
        Main.houseLayoutFileName = houseLayoutFileName;
    }

    public static void setHouseLayoutFile(File houseLayoutFile) {
        Main.houseLayoutFile = houseLayoutFile;
    }

    public static void setHouseLayoutFilePathName(String houseLayoutFilePathName) {
        Main.houseLayoutFilePathName = houseLayoutFilePathName;
    }

    public static void setIs_away(boolean is_away) {
        Main.is_away = is_away;
    }

    public static void setTimeLimitBeforeAlert(int timeLimitBeforeAlert) {
        Main.timeLimitBeforeAlert = timeLimitBeforeAlert;
    }

    public static void setSuspBox(TextArea suspBox) {
        Main.suspBox = suspBox;
    }

    public static void setNumberOfProfiles(int numberOfProfiles) {
        Main.numberOfProfiles = numberOfProfiles;
    }

    public static void setNumberOfTimesDashboardPageLoaded(int numberOfTimesDashboardPageLoaded) {
        Main.numberOfTimesDashboardPageLoaded = numberOfTimesDashboardPageLoaded;
    }

    public static void setNumberOfTimesProfilePageSelected(int numberOfTimesProfilePageSelected) {
        Main.numberOfTimesProfilePageSelected = numberOfTimesProfilePageSelected;
    }

    public static void setNumberOfTimesSHSModuleCreated(int numberOfTimesSHSModuleCreated) {
        Main.numberOfTimesSHSModuleCreated = numberOfTimesSHSModuleCreated;
    }

    public static void setNumberOfTimesSHCModuleCreated(int numberOfTimesSHCModuleCreated) {
        Main.numberOfTimesSHCModuleCreated = numberOfTimesSHCModuleCreated;
    }

    public static void setNumberOfTimesSHPModuleCreated(int numberOfTimesSHPModuleCreated) {
        Main.numberOfTimesSHPModuleCreated = numberOfTimesSHPModuleCreated;
    }

    public static void setNumberOfTimesSHHModuleCreated(int numberOfTimesSHHModuleCreated) {
        Main.numberOfTimesSHHModuleCreated = numberOfTimesSHHModuleCreated;
    }

    public static void setProfiles(UserProfile[] profiles) {
        Main.profiles = profiles;
    }

    public static void setCurrentActiveProfile(UserProfile currentActiveProfile) {
        Main.currentActiveProfile = currentActiveProfile;
    }

    public static void setProfileLinks(Hyperlink[] profileLinks) {
        Main.profileLinks = profileLinks;
    }

    public static void setOutsideTemperature(double outsideTemperature) {
        Main.outsideTemperature = outsideTemperature;
    }

    public static void setHouseholdLocations(Room[] householdLocations) {
        Main.householdLocations = householdLocations;
    }

    public static void setCurrentLocation(Room currentLocation) {
        Main.currentLocation = currentLocation;
    }

    public static void setHouse(House house) {
        Main.house = house;
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

        editContextButton = new Button(); editContextButton.setId("editContextButton");
        editContextButton.prefHeight(45); editContextButton.prefWidth(75); editContextButton.setText("Edit\nContext");
        editContextButton.setTranslateY(90); editContextButton.setTranslateX(15); editContextButton.setTextAlignment(TextAlignment.CENTER);
        editContextButton.setOnAction(e->Controller.editContext());
        editContextButton.setDisable(true);

        Label locationLabel = new Label("House\nLocation:\n[not set]");
        locationLabel.setId("locationLabel");
        locationLabel.setTranslateX(30); locationLabel.setTranslateY(170);

        Label simDateAndTimeLabel = new Label("SIM TIME");
        simDateAndTimeLabel.setTranslateX(20); simDateAndTimeLabel.setTranslateY(300);

        Label simulationDate = new Label(); simulationDate.setId("simulationDate");
        simulationDate.setTranslateX(20); simulationDate.setTranslateY(320);

        Label simulationTime = new Label(); simulationTime.setId("simulationTime");
        simulationTime.setTranslateX(20); simulationTime.setTranslateY(340);

        Label temperatureLabel = new Label(); temperatureLabel.setId("temp");
        temperatureLabel.setText("Outside Temp.\n15°C"); temperatureLabel.setTextAlignment(TextAlignment.CENTER);
        temperatureLabel.setTranslateY(400); temperatureLabel.setTranslateX(15);

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
                        SHS_MODULE = makeSHSmodule();
                        TabPane tabPane = (TabPane) main_dashboard.getChildren().get(a);
                        Tab innerTab = tabPane.getTabs().get(0);
                        innerTab.setContent(SHS_MODULE);

                        innerTab.setContent(SHS_MODULE);
                        tabPane.getTabs().set(0, innerTab);
                        main_dashboard.getChildren().set(a, tabPane);

                        SHC_MODULE = makeSHCmodule();
                        TabPane tabPane2 = (TabPane) main_dashboard.getChildren().get(a);
                        Tab innerTab2 = tabPane2.getTabs().get(1);
                        innerTab2.setDisable(false);
                        tabPane2.getTabs().set(1, innerTab2);
                        main_dashboard.getChildren().set(a, tabPane2);

                        SHP_MODULE = makeSHPmodule();
                        TabPane tabPane3 = (TabPane) main_dashboard.getChildren().get(a);
                        Tab innerTab3 = tabPane3.getTabs().get(2);
                        innerTab3.setDisable(false);
                        tabPane3.getTabs().set(2, innerTab3);
                        main_dashboard.getChildren().set(a, tabPane3);

                        SHH_MODULE = makeSHHmodule();
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

                        SHC_MODULE = makeSHCmodule();
                        TabPane tabPane2 = (TabPane) main_dashboard.getChildren().get(a);
                        Tab innerTab2 = tabPane2.getTabs().get(1);
                        innerTab2.setDisable(true);
                        tabPane2.getTabs().set(1, innerTab2);
                        main_dashboard.getChildren().set(a, tabPane2);

                        SHP_MODULE = makeSHPmodule();
                        TabPane tabPane3 = (TabPane) main_dashboard.getChildren().get(a);
                        Tab innerTab3 = tabPane3.getTabs().get(2);
                        innerTab3.setDisable(true);
                        tabPane3.getTabs().set(2, innerTab3);
                        main_dashboard.getChildren().set(a, tabPane3);

                        SHH_MODULE = makeSHHmodule();
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

        ComboBox locationMenu = new ComboBox();
        locationMenu.setId("locationMenu");
        locationMenu.setTranslateX(160); locationMenu.setTranslateY(180);
        locationMenu.setItems(FXCollections.observableArrayList(countries));
        locationMenu.setPrefWidth(200); locationMenu.setPromptText("Select country...");
        if ((currentActiveProfile==null)) {
            locationMenu.setDisable(true);
        }
        else {
            if (simulationIsOn) {
                locationMenu.setDisable(true);
            }
            else {
                locationMenu.setDisable(false);
            }
        }

        Button confirmLocationButton = new Button("Set Location");
        confirmLocationButton.setId("confirmLocationButton");
        confirmLocationButton.setTranslateX(200);
        confirmLocationButton.setTranslateY(260);
        confirmLocationButton.setOnAction(e->{
            for (int a = 0; a < main_dashboard.getChildren().size(); a++) {
                try {
                    if (main_dashboard.getChildren().get(a).getId().equals("locationLabel")) {
                        Label updatedLabel = (Label) main_dashboard.getChildren().get(a);
                        updatedLabel.setText("House\nLocation:\n" + locationMenu.getValue().toString());
                        main_dashboard.getChildren().set(a, updatedLabel);
                        break;
                    }
                } catch(Exception ex){}
            }
            house.setLocation(locationMenu.getValue().toString());
        });
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

        Button confirmTimeButton = new Button("Confirm New Time"); confirmTimeButton.setId("confirmTimeButton");
        confirmTimeButton.setTranslateX(200); confirmTimeButton.setTranslateY(435);
        confirmTimeButton.setTextAlignment(TextAlignment.CENTER);
        confirmTimeButton.setOnAction(e -> {new Thread(()->{

            int indexOfSimDateLabel = 0, indexOfSimTimeLabel = 0;

            for (int a = 0; a < main_dashboard.getChildren().size(); a++) {
                try {
                    if (main_dashboard.getChildren().get(a).getId().equals("simulationDate")) {
                        indexOfSimDateLabel = a;
                    } else if (main_dashboard.getChildren().get(a).getId().equals("simulationTime")) {
                        indexOfSimTimeLabel = a;
                    }
                }catch (Exception excep){}
            }
            sample.Controller.CurrentDateSimulation(datePicker, (Label) main_dashboard.getChildren().get(indexOfSimDateLabel),
                    (Label) main_dashboard.getChildren().get(indexOfSimTimeLabel), hourField, minuteField);}).start();

        });

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
                    setTimeLabel, hourField, colon, minuteField, confirmTimeButton, line2, setHouseLocationLabel,
                    confirmLocationButton, locationMenu);
        }catch (Exception e){}
        numberOfTimesSHSModuleCreated++;
        return SHS_MODULE;
    }

    /**
     * Create or update the SHC module interface.
     * @return
     */
    public static AnchorPane makeSHCmodule() {

        Label label = new Label("Select a room that you would like to configure");
        label.setTranslateX(20);
        label.setTranslateY(20);

        if (houseLayout != null) {
            int trans_X = 150; int trans_Y = 40;
            for (int room = 0; room < householdLocations.length; room++) {
                Button roomButton = new Button(householdLocations[room].getName());

                roomButton.setTranslateX(trans_X);
                roomButton.setTranslateY(trans_Y += 30);
                int finalRoom = room;
                roomButton.setOnAction(e -> {

                    Stage tempStage = new Stage();
                    tempStage.setResizable(false);
                    tempStage.setHeight(350);
                    tempStage.setWidth(225);
                    tempStage.setTitle("SHC - " + householdLocations[finalRoom].getName());

                    for (int panes = 0; panes < houseLayout.getChildren().size(); panes++) {
                        try {
                            if (houseLayout.getChildren().get(panes).getId().equals("roomLayoutID" + householdLocations[finalRoom].getRoomID())) {
                                tempStage.setScene(new Scene(house.constructRoomLayoutSHCversion(householdLocations[panes],
                                        (AnchorPane) houseLayout.getChildren().get(panes),
                                        householdLocations[panes].getNumberOfPeopleInside(), tempStage)));
                                tempStage.showAndWait();
                                break;
                            }
                        }
                        catch (Exception ex) {}
                    }
                });
                SHC_MODULE.getChildren().add(roomButton);
            }
        }
        if (numberOfTimesSHCModuleCreated==0) {
            SHC_MODULE.getChildren().add(label);
        }

        numberOfTimesSHCModuleCreated++;
        return SHC_MODULE;
    }

    /**
     * Create or update the SHH module interface.
     * @return
     */
    public static AnchorPane makeSHHmodule() {
        /**TODO: give IDs to each GUI element in here */
        numberOfTimesSHHModuleCreated++;
        return SHH_MODULE;
    }

    /**
     * Create or update the SHH module interface.
     * @return
     */
    public static AnchorPane makeSHPmodule(){
        /**TODO: give IDs to each GUI element in here */
        SHP_MODULE = new AnchorPane();

        Text warningText = new Text(); warningText.setTranslateY(40);
        warningText.setText("Enter the amount of time (minutes) " +
                "before alerting the authorities after any motion detectors are triggered (AWAY mode only):");
        warningText.setWrappingWidth(480);

        TextField timeBox = new TextField(); timeBox.setPrefHeight(Region.USE_COMPUTED_SIZE); timeBox.setPrefWidth(60);
        timeBox.setTranslateX(200); timeBox.setTranslateY(70);
        if ((currentActiveProfile==null)) {
            timeBox.setDisable(true);
        }
        else {
            if (simulationIsOn) {
                timeBox.setDisable(true);
            }
            else {
                timeBox.setDisable(false);
            }
        }

        Label timeLimit = new Label();
        timeLimit.setText("Time before Alert: 0 min.");
        timeLimit.setTranslateY(70);
        timeLimit.setTranslateX(10);

        Line line = new Line(); line.setStartX(0);line.setEndX(500); line.setTranslateY(120);

        Label suspiciousLabel = new Label("Suspicious Activity Log");
        suspiciousLabel.setTranslateY(120); suspiciousLabel.setTranslateX(50);

        suspBox = new TextArea();
        suspBox.setPrefHeight(253);
        suspBox.setPrefWidth(357);
        suspBox.setTranslateX(50);
        suspBox.setTranslateY(140);
        suspBox.setWrapText(true);

        if ((currentActiveProfile==null)) {
            suspBox.setDisable(true);
        }
        else {
            if (simulationIsOn) {
                suspBox.setDisable(true);
            }
            else {
                suspBox.setDisable(false);
            }
        }

        Button confirmButton = new Button("Confirm");
        confirmButton.setTranslateY(70);
        confirmButton.setTranslateX(270);
        confirmButton.setDisable(true);
        //confirmButton.setOnAction(e->sample.Controller.setTimeLimitAwayMode(timeBox, timeLimit));
        if ((currentActiveProfile==null)) {
            confirmButton.setDisable(true);
        }
        else {
            if (simulationIsOn) {
                confirmButton.setDisable(true);
            }
            else {
                confirmButton.setDisable(false);
            }
        }

        ToggleButton tb = new ToggleButton();
        tb.setId("setAwayModeButton");
        tb.setText("Turn on AWAY mode");
        tb.setTranslateX(250);
        tb.setOnAction(e->sample.Controller.toggleAwayButton(tb));

        Button awayLightsButton = new Button("Lights\nSettings for\nAWAY mode");
        awayLightsButton.setTranslateX(380); awayLightsButton.setTranslateY(50);
        awayLightsButton.setOnAction(e->Controller.configureAwayLights());

        if (numberOfTimesSHPModuleCreated==0) {
            SHP_MODULE.getChildren().addAll(tb, confirmButton, suspBox,
                    suspiciousLabel, line, timeLimit, timeBox, warningText, awayLightsButton);
        }


        numberOfTimesSHPModuleCreated++;

        return SHP_MODULE;
    }
}
