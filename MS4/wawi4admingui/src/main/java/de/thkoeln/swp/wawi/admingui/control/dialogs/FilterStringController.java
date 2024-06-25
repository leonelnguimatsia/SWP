package de.thkoeln.swp.wawi.admingui.control.dialogs;


import de.thkoeln.swp.wawi.admingui.application.AdminApp;
import de.thkoeln.swp.wawi.admingui.util.*;
import de.thkoeln.swp.wawi.admingui.util.DialogEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;

import java.util.function.Predicate;


public class FilterStringController {
    
    
    @FXML private Button btConfirm;
    @FXML private TextField tfValue;
    @FXML private TextField tfAttribute;
    @FXML private RadioButton rbContains;
    @FXML private RadioButton rbStartsWith;
    @FXML private RadioButton rbEndsWith;
    @FXML private RadioButton rbEquals;
    @FXML private ToggleGroup condition;
    
    private Stage stage;
    private StringKeyExtractor keyExtractor;
    
    
    @FXML
    private void initialize() {
        stage = AdminApp.getInstance().getLatestStage();
        stage.addEventHandler(DialogEvent.INIT, this::init);
        
        tfValue.textProperty().addListener((obs, o, n) -> change());
    }
    
    private void init(DialogEvent e) {
        keyExtractor = e.get("keyExtractor", StringKeyExtractor.class);
        tfAttribute.setText(e.get("attribute", String.class));
        change();
    }
    
    
    private boolean validateInput() {
        return !tfValue.getText().isEmpty();
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
            String value = tfValue.getText();
            Predicate<?> p;
            if (condition.getSelectedToggle() == rbContains)
                p = v -> keyExtractor.extract(v).contains(value);
            else if (condition.getSelectedToggle() == rbEquals)
                p = v -> keyExtractor.extract(v).equals(value);
            else if (condition.getSelectedToggle() == rbStartsWith)
                p = v -> keyExtractor.extract(v).startsWith(value);
            else
                p = v -> keyExtractor.extract(v).endsWith(value);
            stage.getOwner().fireEvent(new DialogEvent(false, stage, new Parameter("filter", p)));
            AdminApp.getInstance().closeStage(stage);
        }
    }
    
}
