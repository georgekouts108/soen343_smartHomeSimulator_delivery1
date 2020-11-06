package sample;

import javafx.scene.layout.AnchorPane;

import java.util.Observer;

public abstract class Module implements Observer {

    /**
     * Module abstract constructor
     */
    public Module() {}

    /**
     * Generate a module by creating and returning a local AnchorPane
     * @return
     */
    public abstract AnchorPane generateModule();

    /**
     * Generate a module by populating an existing AnchorPane
     * @param pane
     * @return
     */
    public abstract AnchorPane generateModule(AnchorPane pane);
}
