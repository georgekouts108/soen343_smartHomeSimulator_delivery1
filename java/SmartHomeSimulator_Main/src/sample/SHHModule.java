package sample;

import javafx.scene.layout.AnchorPane;

public class SHHModule extends Module {

    public SHHModule() {
        super();
    }

    /**
     * Generate a module by creating and returning a local AnchorPane
     * @return
     */
    @Override
    public AnchorPane generateModule() {

        Main.numberOfTimesSHHModuleCreated++;
        return null;
    }

    /**
     * Generate a module by populating an existing AnchorPane
     * @param pane
     * @return
     */
    @Override
    public AnchorPane generateModule(AnchorPane pane) {

        Main.numberOfTimesSHHModuleCreated++;
        return null;
    }
}