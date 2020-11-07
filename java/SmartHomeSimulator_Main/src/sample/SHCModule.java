package sample;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Observable;

public class SHCModule extends Module {

    public SHCModule() {
        super();
    }

    /**
     * Generate a module by creating and returning a local AnchorPane
     * @return
     */
    @Override
    public AnchorPane generateModule() {
        return null;
    }

    /**
     * Generate a module by populating an existing AnchorPane
     * @param pane
     * @return
     */
    @Override
    public AnchorPane generateModule(AnchorPane pane) {

        Label label = new Label("Select a room that you would like to configure");
        label.setTranslateX(20);
        label.setTranslateY(20);

        if (Main.houseLayout != null) {
            int trans_X = 150; int trans_Y = 40;
            for (int room = 0; room < Main.householdLocations.length; room++) {
                Button roomButton = new Button(Main.householdLocations[room].getName());
                roomButton.setId("SHCRoomButtonFor_"+Main.householdLocations[room].getName());
                roomButton.setTranslateX(trans_X);
                roomButton.setTranslateY(trans_Y += 30);
                int finalRoom = room;
                roomButton.setOnAction(e -> {

                    Stage tempStage = new Stage();
                    tempStage.setResizable(false);
                    tempStage.setHeight(350);
                    tempStage.setWidth(225);
                    tempStage.setTitle("SHC - " + Main.householdLocations[finalRoom].getName());

                    for (int panes = 0; panes < Main.houseLayout.getChildren().size(); panes++) {
                        try {
                            if (Main.houseLayout.getChildren().get(panes).getId().equals("roomLayoutID" + Main.householdLocations[finalRoom].getRoomID())) {
                                tempStage.setScene(new Scene(Main.house.constructRoomLayoutSHCversion(Main.householdLocations[panes],
                                        (AnchorPane) Main.houseLayout.getChildren().get(panes),
                                        Main.householdLocations[panes].getNumberOfPeopleInside(), tempStage)));
                                tempStage.showAndWait();
                                break;
                            }
                        }
                        catch (Exception ex) {}
                    }
                });
                pane.getChildren().add(roomButton);
            }
        }
        if (Main.numberOfTimesSHCModuleCreated==0) {
            pane.getChildren().add(label);
        }

        Main.numberOfTimesSHCModuleCreated++;
        return pane;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
