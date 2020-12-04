package sample;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;

import java.time.Month;
import java.util.Observable;

/**
 * SHS module class
 */
public class SHSModule extends Module {

    protected Month simulationMonth;

    /**
     * SHS Constructor
     */
    public SHSModule() {
        super();
        simulationMonth = null;
    }

    /**
     * Generate a module by creating and returning a local AnchorPane
     * @return
     */
    @Override
    public AnchorPane generateModule() {
        AnchorPane shsModule = new AnchorPane();

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
        locationMenu.setTranslateX(150); locationMenu.setTranslateY(180);
        locationMenu.setItems(FXCollections.observableArrayList(Main.countries));
        locationMenu.setPrefWidth(200); locationMenu.setPromptText("Select country...");
        if ((Main.currentActiveProfile==null)) {
            locationMenu.setDisable(true);
        }
        else {
            if (Main.simulationIsOn) {
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
            for (int a = 0; a < Main.main_dashboard.getChildren().size(); a++) {
                try {
                    if (Main.main_dashboard.getChildren().get(a).getId().equals("locationLabel")) {
                        Label updatedLabel = (Label) Main.main_dashboard.getChildren().get(a);
                        if (locationMenu.getValue() != null) {
                            updatedLabel.setText("House\nLocation:\n" + locationMenu.getValue().toString());
                            Main.house.setLocation(locationMenu.getValue().toString());
                            Main.main_dashboard.getChildren().set(a, updatedLabel);
                        }
                        else {
                            throw new SHSException("ERROR [SHS]: You did not select a location");
                        }

                        break;
                    }
                }
                catch (SHSException s){Controller.appendMessageToConsole(s.getMessage());}
                catch(Exception ex){}
            }
        });
        if ((Main.currentActiveProfile==null)) {
            confirmLocationButton.setDisable(true);
        }
        else {
            if (Main.simulationIsOn) {
                confirmLocationButton.setDisable(true);
            }
            else {
                confirmLocationButton.setDisable(false);
            }
        }

        Line line2 = new Line(); line2.setStartX(0); line2.setEndX(500); line2.setTranslateY(300);

        Label setDateTimeLabel = new Label("Set Date and Time");
        setDateTimeLabel.setTranslateX(200); setDateTimeLabel.setTranslateY(310);

        DatePicker datePicker = new DatePicker(); datePicker.setId("datePicker");
        datePicker.setTranslateX(160); datePicker.setTranslateY(340); datePicker.setPromptText("Select date...");
        if ((Main.currentActiveProfile==null)) {
            datePicker.setDisable(true);
        }
        else {
            if (Main.simulationIsOn) {
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
        if ((Main.currentActiveProfile==null)) {
            hourField.setDisable(true);
        }
        else {
            if (Main.simulationIsOn) {
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
        if ((Main.currentActiveProfile==null)) {
            minuteField.setDisable(true);
        }
        else {
            if (Main.simulationIsOn) {
                minuteField.setDisable(true);
            }
            else {
                minuteField.setDisable(false);
            }
        }

        Button confirmTimeButton = new Button("Confirm New Time"); confirmTimeButton.setId("confirmTimeButton");
        confirmTimeButton.setTranslateX(190); confirmTimeButton.setTranslateY(435);
        confirmTimeButton.setTextAlignment(TextAlignment.CENTER);
        confirmTimeButton.setOnAction(e -> {
            Controller.simulationTimeThread = new Thread(()-> {

            int indexOfSimDateLabel = 0, indexOfSimTimeLabel = 0;

            for (int a = 0; a < Main.main_dashboard.getChildren().size(); a++) {
                try {
                    if (Main.main_dashboard.getChildren().get(a).getId().equals("simulationDate")) {
                        indexOfSimDateLabel = a;
                    } else if (Main.main_dashboard.getChildren().get(a).getId().equals("simulationTime")) {
                        indexOfSimTimeLabel = a;
                    }
                }
                catch (Exception excep){}
            }
            sample.Controller.CurrentDateSimulation(datePicker, (Label) Main.main_dashboard.getChildren().get(indexOfSimDateLabel),
                    (Label) Main.main_dashboard.getChildren().get(indexOfSimTimeLabel), hourField, minuteField, Controller.simulationTimeSpeed);
            });

            Controller.simulationTimeThread.start();
            //now need to kill this thread everytime the method is called.. in order to reset...
        });

        if ((Main.currentActiveProfile==null)) {
            confirmTimeButton.setDisable(true);
        }
        else {
            if (Main.simulationIsOn) {
                confirmTimeButton.setDisable(true);
            }
            else {
                confirmTimeButton.setDisable(false);
            }
        }

        try {
            shsModule.getChildren().addAll(manageOrSwitchProfileButton, line, setDateTimeLabel, datePicker,
                    setTimeLabel, hourField, colon, minuteField, confirmTimeButton, line2, setHouseLocationLabel,
                    confirmLocationButton, locationMenu);
        }catch (Exception e){}

        Main.numberOfTimesSHSModuleCreated++;

        return shsModule;
    }

    /**
     * Generate a module by populating an existing AnchorPane
     * @param pane
     * @return
     */
    @Override
    public AnchorPane generateModule(AnchorPane pane) {
        return null;
    }

}


