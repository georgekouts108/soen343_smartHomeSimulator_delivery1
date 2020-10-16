package sample;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.*;

public class TestGUI extends Application {

    private static javafx.scene.control.Label createLabel(String text, String styleClass){
        javafx.scene.control.Label label = new javafx.scene.control.Label(text);
        label.getStyleClass().add(styleClass);
        BorderPane.setMargin(label, new javafx.geometry.Insets(5));
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return label;
    }
    public static BorderPane createGUI(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight()-screenSize.getHeight()*0.1;

        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("bg-1");
        borderPane.setPadding(new Insets(5));
        borderPane.setMinWidth(750);
        borderPane.setMinHeight(450);

        javafx.scene.control.Label left = createLabel("Left", "bg-2");
        left.setMinWidth(100);
        borderPane.setLeft(left);

        javafx.scene.control.Label center = createLabel("Center", "bg-3");
        center.setMinWidth(200);
        borderPane.setCenter(center);

        javafx.scene.control.Label right = createLabel("Right", "bg-4");
        right.setMinWidth(400);
        borderPane.setRight(right);

        Label bottom = createLabel("Bottom", "bg-5");
        bottom.setMinHeight(100);
        borderPane.setBottom(bottom);
        return borderPane;
    }


    @Override
    public void start(Stage stage) {

        Scene scene = new Scene(createGUI(), 1024, 600);
        String styleSheet = getClass().getResource("/sample/style.css").toExternalForm();
        scene.getStylesheets().add(styleSheet);
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
