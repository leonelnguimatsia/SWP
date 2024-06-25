package de.thkoeln.swp.wawi.admingui.control.dialogs;


import de.thkoeln.swp.wawi.admingui.application.AdminApp;
import de.thkoeln.swp.wawi.admingui.util.DialogEvent;
import de.thkoeln.swp.wawi.admingui.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.stage.*;


/**
 * Init signature: {@code init(boolean activate)}
 */
public class ActivateByCountController {
    
    
    @FXML private Button btConfirm;
    @FXML private Spinner<Integer> spCount;
    @FXML private RadioButton rbActivate;
    @FXML private RadioButton rbDeactivate;
    @FXML private ToggleGroup activate;
    
    private Stage stage;
    
    
    @FXML
    private void initialize() {
        stage = AdminApp.getInstance().getLatestStage();
        stage.addEventHandler(DialogEvent.INIT, this::init);
        
        spCount.setValueFactory(new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        spCount.valueProperty().addListener((obs, o, n) -> change());
    }
    
    
    private void init(DialogEvent e) {
        if (e.get("activate", Boolean.class))
            activate.selectToggle(rbActivate);
        else
            activate.selectToggle(rbDeactivate);
        change();
    }
    
    private void change() {
        btConfirm.setDisable(spCount.getValue() == null);
    }
    
    @FXML
    private void cancel() {
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
    
    @FXML
    private void confirm() {
        Integer count = spCount.getValue();
        Boolean act = null;
        Toggle t = activate.getSelectedToggle();
        if (t == rbActivate)
            act = true;
        else if (t == rbDeactivate)
            act = false;
        
        if (count != null && act != null) {
            Parameter v = new Parameter("count", count);
            Parameter a = new Parameter("activate", act);
            stage.getOwner().fireEvent(new DialogEvent(false, stage, v, a));
            AdminApp.getInstance().closeStage(stage);
        }
    }
    
}
