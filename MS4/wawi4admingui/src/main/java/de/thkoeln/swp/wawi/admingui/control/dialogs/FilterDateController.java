package de.thkoeln.swp.wawi.admingui.control.dialogs;


import de.thkoeln.swp.wawi.admingui.application.AdminApp;
import de.thkoeln.swp.wawi.admingui.util.DialogEvent;
import de.thkoeln.swp.wawi.admingui.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;

import java.time.LocalDate;
import java.util.function.Predicate;


public class FilterDateController {
    
    
    @FXML private Button btConfirm;
    @FXML private DatePicker dpValue;
    @FXML private ToggleButton tgBefore;
    @FXML private ToggleButton tgAfter;
    @FXML private CheckBox cbOn;
    @FXML private ToggleGroup condition;
    @FXML private TextField tfAttribute;
    
    private Stage stage;
    private DateKeyExtractor keyExtractor;
    
    
    @FXML
    private void initialize() {
        stage = AdminApp.getInstance().getLatestStage();
        stage.addEventHandler(DialogEvent.INIT, this::init);
        
        dpValue.chronologyProperty().addListener((obs, o, n) -> change());
        condition.selectedToggleProperty().addListener((obs, o, n) -> change());
        cbOn.selectedProperty().addListener((obs, o, n) -> change());
    }
    
    private void init(DialogEvent e) {
        keyExtractor = e.get("keyExtractor", DateKeyExtractor.class);
        tfAttribute.setText(e.get("attribute", String.class));
        change();
    }
    
    
    private boolean validateInput() {
        return dpValue.getValue() != null && (condition.getSelectedToggle() != null || cbOn.isSelected());
    }
    
    private void change() {
        btConfirm.setDisable(!validateInput());
    }
    
    
    @FXML
    private void cancel() {
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
    
    @FXML
    private void confirm() {
        if (validateInput()) {
            LocalDate value = dpValue.getValue();
            Predicate<?> p = v -> false;
            if (condition.getSelectedToggle() == tgBefore)
                p = v -> keyExtractor.extract(v).isBefore(value);
            else if (condition.getSelectedToggle() == tgAfter)
                p = v -> keyExtractor.extract(v).isAfter(value);
            if (cbOn.isSelected())
                p = p.or(v -> keyExtractor.extract(v).equals(value));
            
            stage.getOwner().fireEvent(new DialogEvent(false, stage, new Parameter("filter", p)));
            AdminApp.getInstance().closeStage(stage);
        }
    }
    
}
