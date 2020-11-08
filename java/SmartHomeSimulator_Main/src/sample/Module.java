package sample;

import javafx.scene.layout.AnchorPane;

/**
 * Abstract class for Modules
 */
public abstract class Module {

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
