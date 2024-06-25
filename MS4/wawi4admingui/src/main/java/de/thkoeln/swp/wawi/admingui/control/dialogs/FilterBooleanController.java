package de.thkoeln.swp.wawi.admingui.control.dialogs;


import de.thkoeln.swp.wawi.admingui.application.AdminApp;
import de.thkoeln.swp.wawi.admingui.util.DialogEvent;
import de.thkoeln.swp.wawi.admingui.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;

import java.util.function.Predicate;


public class FilterBooleanController {
    
    
    @FXML private Button btConfirm;
    @FXML private TextField tfAttribute;
    @FXML private RadioButton rbActivated;
    @FXML private RadioButton rbDeactivated;
    @FXML private ToggleGroup condition;
    
    private Stage stage;
    private BooleanKeyExtractor keyExtractor;
    
    
    @FXML
    private void initialize() {
        stage = AdminApp.getInstance().getLatestStage();
        stage.addEventHandler(DialogEvent.INIT, this::init);
    }
    
    private void init(DialogEvent e) {
        tfAttribute.setText(e.get("attribute", String.class));
        keyExtractor = e.get("keyExtractor", BooleanKeyExtractor.class);
    }
    
    
    @FXML
    private void cancel() {
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
    
    @FXML
    private void confirm() {
        boolean value = rbActivated.isSelected();
        Parameter f = new Parameter("filter", (Predicate<?>) p -> keyExtractor.extract(p) == value);
        stage.getOwner().fireEvent(new DialogEvent(false, stage, f));
        AdminApp.getInstance().closeStage(stage);
    }
    
}
