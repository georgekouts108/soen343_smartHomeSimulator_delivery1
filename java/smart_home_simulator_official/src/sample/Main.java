package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import sample.Controller.*;


public class Main extends Application {

    private final int DASHBOARD_WIDTH = 700;
    private final int DASHBOARD_HEIGHT = 1300;
    private final int LOGINPAGE_WIDTH = 400;
    private final int LOGINPAGE_HEIGHT = 600;


    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setResizable(false);

        /** SET THE STAGE */
        Parent login = FXMLLoader.load(getClass().getResource("login.fxml"));
        Parent main_dashboard = FXMLLoader.load(getClass().getResource("dashboard.fxml"));

        Scene loginScene = new Scene(login, LOGINPAGE_HEIGHT, LOGINPAGE_WIDTH);
        Scene dashboardScene = new Scene(main_dashboard, DASHBOARD_HEIGHT, DASHBOARD_WIDTH);

        /** upon launching... */

        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Smart Home Simulator - Sign In");

        Button b = (Button) login.getChildrenUnmodifiable().get(0);
        b.setOnAction(e ->
        {
            primaryStage.setScene(dashboardScene);
            primaryStage.setTitle("Smart Home Simulator -- logged in as ");
        });

        /** DISPLAY THE STAGE */
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
