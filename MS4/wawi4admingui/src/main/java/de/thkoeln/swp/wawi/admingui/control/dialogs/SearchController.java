package de.thkoeln.swp.wawi.admingui.control.dialogs;


import de.thkoeln.swp.wawi.admingui.application.AdminApp;
import de.thkoeln.swp.wawi.admingui.util.*;
import de.thkoeln.swp.wawi.admingui.util.DialogEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.util.StringConverter;

import java.util.List;


public class SearchController {
    
    
    @FXML private Button btConfirm;
    @FXML private TextField tfValue;
    @FXML private ComboBox<TableColumn<?, ?>> cmAttribute;
    
    private Stage stage;
    
    
    @FXML
    private void initialize() {
        stage = AdminApp.getInstance().getLatestStage();
        stage.addEventHandler(DialogEvent.INIT, this::init);
        
        cmAttribute.setConverter(new StringConverter<>() {
            @Override
            public String toString(TableColumn<?, ?> object) {
                return object.getText();
            }
    
            @Override
            public TableColumn<?, ?> fromString(String string) {
                return cmAttribute.getItems().stream().filter(v -> v.getText().equals(string)).findFirst().orElse(null);
            }
        });
        
        tfValue.textProperty().addListener((u, v, w) -> change());
        cmAttribute.getSelectionModel().selectedItemProperty().addListener((u, v, w) -> change());
    }
    
    private void init(DialogEvent e) {
        //noinspection unchecked
        cmAttribute.getItems().addAll(e.get("attributes", List.class));
        if (cmAttribute.getItems().size() > 0)
            cmAttribute.getSelectionModel().select(0);
        change();
    }
    
    
    private boolean validateInput() {
        if (tfValue.getText().isEmpty())
            return false;
        return cmAttribute.getSelectionModel().getSelectedItem() != null;
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
            Parameter a = new Parameter("attribute", cmAttribute.getSelectionModel().getSelectedItem());
            Parameter v = new Parameter("value", tfValue.getText());
            stage.getOwner().fireEvent(new DialogEvent(false, stage, a, v));
            AdminApp.getInstance().closeStage(stage);
        }
    }
    
}
