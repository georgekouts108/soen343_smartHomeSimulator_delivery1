package sample;

import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.time.LocalTime;
import java.util.Observable;

/**
 * SHP Module class
 */
public class SHPModule extends Module {

    private int awayLightsHourLower;
    private int awayLightsMinuteLower;
    private int awayLightsHourUpper;
    private int awayLightsMinuteUpper;
    private int numberOfMDsOn;
    private Thread alertTimeThread;
    private boolean isAlertTimeThreadRunning;
    private static int timeToAlert = 0;
    private Thread localTimeAwayLightBoundaryCheckThread;
    private boolean localTimeAwayLightInRange;

    /**
     * SHP Module constructor
     */
    public SHPModule() {
        super();
        this.numberOfMDsOn = 0;
        this.isAlertTimeThreadRunning = false;
        this.alertTimeThread = null;
        this.localTimeAwayLightBoundaryCheckThread = null;
        this.localTimeAwayLightInRange = false;
        awayLightsHourLower = 0;
        awayLightsMinuteLower = 0;
        awayLightsHourUpper = 0;
        awayLightsMinuteUpper = 0;
    }

    /**
     * Return the lower hour boundary for when lights should be automatically turned on for during Away mode
     * @return
     */
    public int getAwayLightsHourLower() {
        return awayLightsHourLower;
    }

    /**
     * Return the upper hour boundary for when lights should be automatically turned on for during Away mode
     * @return
     */
    public int getAwayLightsHourUpper() {
        return awayLightsHourUpper;
    }

    /**
     * Set the upper hour boundary for when lights should be automatically turned on for during Away mode
     * @param awayLightsHourUpper
     */
    public void setAwayLightsHourUpper(int awayLightsHourUpper) {
        this.awayLightsHourUpper = awayLightsHourUpper;
    }

    /**
     * Set the lower hour boundary for when lights should be automatically turned on for during Away mode
     * @param awayLightsHourLower
     */
    public void setAwayLightsHourLower(int awayLightsHourLower) {
        this.awayLightsHourLower = awayLightsHourLower;
    }

    /**
     * Return the lower minute boundary for when lights should be automatically turned on for during Away mode
     * @return
     */
    public int getAwayLightsMinuteLower() {
        return awayLightsMinuteLower;
    }

    /**
     * Set the lower minute boundary for when lights should be automatically turned on for during Away mode
     * @param awayLightsMinuteLower
     */
    public void setAwayLightsMinuteLower(int awayLightsMinuteLower) {
        this.awayLightsMinuteLower = awayLightsMinuteLower;
    }

    /**
     * Return the upper minute boundary for when lights should be automatically turned on for during Away mode
     * @return
     */
    public int getAwayLightsMinuteUpper() {
        return awayLightsMinuteUpper;
    }

    /**
     * Set the upper minute boundary for when lights should be automatically turned on for during Away mode
     * @param awayLightsMinuteUpper
     */
    public void setAwayLightsMinuteUpper(int awayLightsMinuteUpper) {
        this.awayLightsMinuteUpper = awayLightsMinuteUpper;
    }

    /**
     * Configure the amount of time before alerting authorities
     * @param time
     */
    public static void setTimeToAlert(int time){
        timeToAlert = time;
    }

    /**
     * Return the amount of time before alerting authorities
     * @return
     */
    public static int getTimeToAlert(){
        return timeToAlert;
    }

    /**
     * Increment the number of motion detectors currently activated
     */
    public void incrementNumberOfMDsOn() {
        this.numberOfMDsOn++;
    }

    /**
     * Decrement the number of motion detectors currently activated
     */
    public void decrementNumberOfMDsOn() {
        this.numberOfMDsOn--;
    }

    /**
     * Return the current number of motion detectors on in the house
     * @return
     */
    public int getNumberOfMDsOn(){return numberOfMDsOn;};

    /**
     * Generate a module by creating and returning a local AnchorPane
     * @return
     */
    @Override
    public AnchorPane generateModule() {
        AnchorPane shpPane = new AnchorPane();

        Text warningText = new Text(); warningText.setTranslateY(40);
        warningText.setText("Enter the amount of time (minutes) " +
                "before alerting authorities if motion detectors are triggered during AWAY mode:");
        warningText.setWrappingWidth(480);
        warningText.setTranslateX(20);

        Label timeLimit = new Label();
        timeLimit.setText("Time before Alert:\n0 min.");
        timeLimit.setTranslateY(70);
        timeLimit.setTranslateX(20);

        TextField timeBox = new TextField();
        timeBox.setId("authAlertTimeBox");
        timeBox.setPromptText("0 <= t <= 5");
        timeBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
        timeBox.setPrefWidth(100);
        timeBox.setTranslateX(160);
        timeBox.setTranslateY(70);

        if (Main.is_away) {
            timeBox.setDisable(true);
        }

        Button confirmButton = new Button("Confirm");
        confirmButton.setId("alertTimeConfirmButton");
        confirmButton.setTranslateY(70);
        confirmButton.setTranslateX(270);
        confirmButton.setOnAction(e->Controller.setTimeBeforeAlert(timeBox, timeLimit));

        if (Main.is_away) {
            confirmButton.setDisable(true);
        }

        Line line = new Line(); line.setStartX(0);line.setEndX(500); line.setTranslateY(120);

        Label suspiciousLabel = new Label("Suspicious Activity Log");
        suspiciousLabel.setTranslateY(120); suspiciousLabel.setTranslateX(50);

        Main.suspBox = new TextArea();
        Main.suspBox.setPrefHeight(253);
        Main.suspBox.setPrefWidth(357);
        Main.suspBox.setTranslateX(50);
        Main.suspBox.setTranslateY(140);
        Main.suspBox.setWrapText(true);

        if ((Main.currentActiveProfile==null)) {
            Main.suspBox.setDisable(true);
        }
        else {
            if (Main.simulationIsOn) {
                Main.suspBox.setDisable(true);
            }
            else {
                Main.suspBox.setDisable(false);
            }
        }

        ToggleButton tb = new ToggleButton();
        tb.setId("setAwayModeButton");
        tb.setText("Turn ON Away Mode");
        tb.setTranslateX(250);
        tb.setOnAction(e->sample.Controller.toggleAwayButton(tb));

        Button awayLightsButton = new Button("Lights\nSettings for\nAWAY mode");
        awayLightsButton.setTranslateX(380); awayLightsButton.setTranslateY(50);
        awayLightsButton.setOnAction(e->Controller.configureAwayLights());

        shpPane.getChildren().addAll(tb, confirmButton, Main.suspBox, suspiciousLabel, line, timeLimit, timeBox, warningText, awayLightsButton);
        Main.numberOfTimesSHPModuleCreated++;
        return shpPane;
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

    /**
     * Notify the SHP module of any motion detectors triggered during AWAY mode
     * so the module may begin counting time time before alerting authorities
     * (or cancel this thread is all motion detectors are off again during AWAY mode)
     */
    public void getNotified() {

        /**if MDs are triggered during AWAY mode, call the method to count down the alert timer*/
        if (SHSHelpers.isIs_away()) {
            if (Main.house.anyMDsOn()) {
                Controller.appendMessageToConsole("CRITICAL [SHP]: One or more motion detectors are illegitimately triggered");
                startOrStopThread(true);
            }
            else {
                startOrStopThread(false);
                Controller.appendMessageToConsole("SHP -- No more M.D's are illegitimately triggered");
                Controller.appendMessageToConsole("Alarm deactivated.");
            }
        }

    }

    /**
     * Start or stop counting down the time before alerting authorities.
     * @param trigger
     */
    public void startOrStopThread(boolean trigger) {

        if (trigger) {
            final int[] secondsBeforeAlert = {timeToAlert * 60};
            Controller.appendMessageToConsole("WARNING: The authorities will be alerted in " + secondsBeforeAlert[0]/60 + " minute(s)...");

            this.alertTimeThread = new Thread(() -> {
                int second = 0;
                while (secondsBeforeAlert[0] > 0 && numberOfMDsOn!=0) {
                    secondsBeforeAlert[0] = secondsBeforeAlert[0] - 1;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                    second = secondsBeforeAlert[0];
                }
                try {
                    if (second==0 && numberOfMDsOn!=0) {
                        Controller.appendMessageToConsole("The authorities have been alerted");
                    }

                }
                catch (Exception ex){}
            });

            if (!isAlertTimeThreadRunning) {
                isAlertTimeThreadRunning = true;
                alertTimeThread.start();
            }
        }
        else {
            if (isAlertTimeThreadRunning && numberOfMDsOn==0) {
                isAlertTimeThreadRunning = false;
                try {
                    alertTimeThread.stop();
                }
                catch (Exception e){}

            }
        }
    }

    /**
     * Check to see if the current local time (hour and minute) fall with the time boundaries
     * @return
     */
    public boolean isCurrentTimeWithinRange() {

        boolean nullTime = (awayLightsHourUpper==0 && awayLightsHourLower==0 &&
                awayLightsMinuteLower==0 && awayLightsMinuteUpper==0);

        int hour = LocalTime.now().getHour();
        int minute = LocalTime.now().getMinute();

        boolean hourInRange = (hour >= awayLightsHourLower && hour <= awayLightsHourUpper);
        boolean minuteInRange = (minute >= awayLightsMinuteLower && minute <= awayLightsMinuteUpper);

        return ((hourInRange && minuteInRange) || nullTime);
    }

    /**
     * This method runs constantly while the application is running,
     * validating, each second, if the local time falls within the
     * specified time boundaries for automatically turning on and off
     * lights during Away mode
     * @return
     */
    public void startTimeBoundaryThread() {

        this.localTimeAwayLightBoundaryCheckThread = new Thread(()->{

            boolean lightsON_transitionOccurred = false;
            boolean lightsOFF_transitionOccurred = false;
            boolean lightsCHOICE_transitionOccurred = false;

            while (true) {

                    this.localTimeAwayLightInRange = isCurrentTimeWithinRange();

                    if (SHSHelpers.isIs_away()) {
                        lightsCHOICE_transitionOccurred = false;

                        // time is appropriate to turn on specific lights automatically
                        if (this.localTimeAwayLightInRange) {

                            // automatically turn on and lock all specified lights
                            try {
                                if (!lightsON_transitionOccurred) {
                                    Controller.awayModeAutoSwitchAndLockCustomLightsON();
                                    lightsON_transitionOccurred = true;
                                    lightsOFF_transitionOccurred = false;
                                }
                            } catch (Exception e){}
                        }
                        else {

                            // automatically turn off and unlock all specified lights
                            try {

                                if (!lightsOFF_transitionOccurred) {
                                    Controller.awayModeAutoSwitchAndLockCustomLightsOFF();
                                    lightsOFF_transitionOccurred = true;
                                    lightsON_transitionOccurred = false;
                                }

                            } catch (Exception e){}
                        }
                    }
                    else {
                        // unlock the selected lights, but don't necessarily turn them off
                        try {
                            if (!lightsCHOICE_transitionOccurred) {
                                Controller.awayModeAutoSwitchAndLockCustomLightsCHOICE(true);
                                lightsCHOICE_transitionOccurred = true;
                            }
                            lightsON_transitionOccurred = false;
                        }
                        catch(Exception e){}
                    }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        this.localTimeAwayLightBoundaryCheckThread.start();
    }
}
