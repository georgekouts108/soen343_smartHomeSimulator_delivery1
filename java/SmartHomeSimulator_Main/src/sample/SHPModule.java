package sample;

import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.Observable;

public class SHPModule extends Module {

    private static int timeToAlert = 0;

    public static void setTimeToAlert(int time){
        timeToAlert = time;
    }

    public static int getTimeToAlert(){
        return timeToAlert;
    }

    public SHPModule(){
        super();
    }

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
        tb.setText("Turn on AWAY mode");
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

    @Override
    public void update(Observable o, Object arg) {

        /**if MDs are triggered during AWAY mode, call the method to count down the alert timer*/
        if (SHSHelpers.isIs_away()) {
            if (Main.house.anyMDsOn()) {
                /**todo: implement a method in Controller that will start counting down the alert time*/
                //test
                System.out.println("test alarm");
                System.out.println(timeToAlert);
                //

                final int[] secondsBeforeAlert = {timeToAlert * 60};
                Thread t = new Thread(() -> {
                    while (secondsBeforeAlert[0] > 0) {
                        secondsBeforeAlert[0] = secondsBeforeAlert[0] - 1;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    sample.Controller.appendMessageToConsole("The authorities have been alerted");
                });
                t.start();
            }
        }

    }
}
