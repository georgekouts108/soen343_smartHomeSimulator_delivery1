package sample;

import house.House;
import house.Room;
import house.UserProfile;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.Month;

import sample.*;

/**
 * Class with accessor and mutator methods for the application's
 * graphics or elements to help especially with any JUnit tests
 */
public class SHSHelpers {

    public static Month getSimulationMonth() {
        return Main.shsModule.simulationMonth;
    }

    public static void setSimulationMonth(Month m) {
        Main.shsModule.simulationMonth = m;
    }

    public static void setShhModuleObject(SHHModule shh) {
        Main.shhModule = shh;
    }
    public static SHCModule getShcModuleObject() {
        return Main.shcModule;
    }
    public static SHHModule getShhModuleObject() {
        return Main.shhModule;
    }
    public static SHPModule getShpModuleObject() {
        return Main.shpModule;
    }
    public static SHSModule getShsModuleObject() {
        return Main.shsModule;
    }
    public static String[] getCountries() {
        return Main.countries;
    }
    public static int getDashboardWidth() {
        return Main.DASHBOARD_WIDTH;
    }
    public static int getDashboardHeight() {
        return Main.DASHBOARD_HEIGHT;
    }
    public static int getLoginpageWidth() {
        return Main.LOGINPAGE_WIDTH;
    }
    public static int getLoginpageHeight() {
        return Main.LOGINPAGE_HEIGHT;
    }
    public static Stage getMain_stage() {
        return Main.main_stage;
    }
    public static Stage getProfileBox() {
        return Main.profileBox;
    }
    public static Stage getEditContextStage() {
        return Main.editContextStage;
    }
    public static Scene getDashboardScene() {
        return Main.dashboardScene;
    }
    public static Scene getProfileScene() {
        return Main.profileScene;
    }
    public static Scene getEditContextScene() {
        return Main.editContextScene;
    }
    public static Scene getEditContextScene2() {
        return Main.editContextScene2;
    }
    public static AnchorPane getEditContextLayout2() {
        return Main.editContextLayout2;
    }
    public static AnchorPane getEditContextLayout() {
        return Main.editContextLayout;
    }
    public static AnchorPane getProfileSelection() {
        return Main.profileSelection;
    }
    public static AnchorPane getShsModule() {
        return Main.SHS_MODULE;
    }
    public static AnchorPane getShcModule() {
        return Main.SHC_MODULE;
    }
    public static AnchorPane getShpModule() {
        return Main.SHP_MODULE;
    }
    public static AnchorPane getShhModule() {
        return Main.SHH_MODULE;
    }
    public static Button getEditContextButton() {
        return Main.editContextButton;
    }
    public static TextArea getOutputConsole() {
        return Main.outputConsole;
    }
    public static AnchorPane getHouseLayout() {
        return Main.houseLayout;
    }
    public static TabPane getModulesInterface() {
        return Main.modulesInterface;
    }
    public static boolean isSimulationIsOn() {
        return Main.simulationIsOn;
    }
    public static FileChooser getFileChooser() {
        return Main.fileChooser;
    }
    public static String getHouseLayoutFileName() {
        return Main.houseLayoutFileName;
    }
    public static File getHouseLayoutFile() {
        return Main.houseLayoutFile;
    }
    public static String getHouseLayoutFilePathName() {
        return Main.houseLayoutFilePathName;
    }
    public static boolean isIs_away() {
        return Main.is_away;
    }
    public static int getTimeLimitBeforeAlert() {
        return Main.timeLimitBeforeAlert;
    }
    public static TextArea getSuspBox() {
        return Main.suspBox;
    }
    public static int getNumberOfProfiles() {
        return Main.numberOfProfiles;
    }
    public static int getNumberOfTimesDashboardPageLoaded() {
        return Main.numberOfTimesDashboardPageLoaded;
    }
    public static int getNumberOfTimesProfilePageSelected() {
        return Main.numberOfTimesProfilePageSelected;
    }
    public static int getNumberOfTimesSHSModuleCreated() {
        return Main.numberOfTimesSHSModuleCreated;
    }
    public static int getNumberOfTimesSHCModuleCreated() {
        return Main.numberOfTimesSHCModuleCreated;
    }
    public static int getNumberOfTimesSHPModuleCreated() {
        return Main.numberOfTimesSHPModuleCreated;
    }
    public static int getNumberOfTimesSHHModuleCreated() {
        return Main.numberOfTimesSHHModuleCreated;
    }
    public static UserProfile[] getProfiles() {
        return Main.profiles;
    }
    public static UserProfile getCurrentActiveProfile() {
        return Main.currentActiveProfile;
    }
    public static Hyperlink[] getProfileLinks() {
        return Main.profileLinks;
    }
    public static double getOutsideTemperature() {
        return Main.outsideTemperature;
    }
    public static Room getCurrentLocation() {
        return Main.currentLocation;
    }
    public static House getHouse() {
        return Main.house;
    }
    public static Room[] gethouseholdLocations(){ return Main.householdLocations; }
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
        Main.SHS_MODULE = shsModule;
    }
    public static void setShcModule(AnchorPane shcModule) {
        Main.SHC_MODULE = shcModule;
    }
    public static void setShpModule(AnchorPane shpModule) {
        Main.SHP_MODULE = shpModule;
    }
    public static void setShhModule(AnchorPane shhModule) {
        Main.SHH_MODULE = shhModule;
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

    public static void setShpModuleObject(SHPModule shp) {
    }
}
