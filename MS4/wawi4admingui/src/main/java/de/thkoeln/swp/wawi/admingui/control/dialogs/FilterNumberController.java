package de.thkoeln.swp.wawi.admingui.control.dialogs;


import de.thkoeln.swp.wawi.admingui.application.AdminApp;
import de.thkoeln.swp.wawi.admingui.util.*;
import de.thkoeln.swp.wawi.admingui.util.DialogEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.*;
import javafx.stage.*;

import java.util.function.Predicate;


@SuppressWarnings("unchecked")
public class FilterNumberController {
    
    
    @FXML private Button btConfirm;
    @SuppressWarnings("rawtypes")
    @FXML private Spinner spValue;
    @FXML private ToggleButton tgLarger;
    @FXML private ToggleButton tgSmaller;
    @FXML private CheckBox cbEqual;
    @FXML private ToggleGroup condition;
    @FXML private TextField tfAttribute;
    
    private Stage stage;
    private NumberKeyExtractor keyExtractor;
    
    
    @FXML
    private void initialize() {
        stage = AdminApp.getInstance().getLatestStage();
        stage.addEventHandler(DialogEvent.INIT, this::init);
        
        spValue.valueProperty().addListener((obs, o, n) -> change());
        condition.selectedToggleProperty().addListener((obs, o, n) -> change());
        cbEqual.selectedProperty().addListener((obs, o, n) -> change());
    }
    
    private void init(DialogEvent e) {
        keyExtractor = e.get("keyExtractor", NumberKeyExtractor.class);
        tfAttribute.setText(e.get("attribute", String.class));
        
        if (e.get("decimal", Boolean.class))
            spValue.setValueFactory(new DoubleSpinnerValueFactory(Double.MIN_VALUE, Double.MAX_VALUE, 0));
        else
            spValue.setValueFactory(new IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, 0));
        change();
    }
    
    
    private boolean validateInput() {
        return spValue.getValue() != null && (condition.getSelectedToggle() != null || cbEqual.isSelected());
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
            double value = ((Number) spValue.getValue()).doubleValue();
            Predicate<?> p = v -> false;
            if (condition.getSelectedToggle() == tgLarger)
                p = v -> keyExtractor.extract(v).doubleValue() > value;
            else if (condition.getSelectedToggle() == tgSmaller)
                p = v -> keyExtractor.extract(v).doubleValue() < value;
            if (cbEqual.isSelected())
                p = p.or(v -> keyExtractor.extract(v).doubleValue() == value);
            
            stage.getOwner().fireEvent(new DialogEvent(false, stage, new Parameter("filter", p)));
            AdminApp.getInstance().closeStage(stage);
        }
    }
    
}
