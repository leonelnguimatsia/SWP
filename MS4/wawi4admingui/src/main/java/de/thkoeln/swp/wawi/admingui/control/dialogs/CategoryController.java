package de.thkoeln.swp.wawi.admingui.control.dialogs;


import de.thkoeln.swp.wawi.admingui.application.AdminApp;
import de.thkoeln.swp.wawi.admingui.util.*;
import de.thkoeln.swp.wawi.admingui.util.DialogEvent;
import de.thkoeln.swp.wawi.steuerungapi.admin.IKategorieSteuerung;
import de.thkoeln.swp.wawi.steuerungapi.grenz.KategorieGrenz;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.util.StringConverter;

import java.util.Comparator;


public class CategoryController {
    
    
    @FXML private Button btConfirm;
    @FXML private TextField tfName;
    @FXML private TextField tfDescription;
    @FXML private ComboBox<KategorieGrenz> cmCategory;
    
    private Stage stage;
    private KategorieGrenz edit;
    
    private final KategorieGrenz root = new KategorieGrenz(0, "Keine", null, null);
    
    
    @FXML
    private void initialize() {
        stage = AdminApp.getInstance().getLatestStage();
        stage.addEventHandler(DialogEvent.INIT, this::init);
        
        tfName.textProperty().addListener((obs, o, n) -> change());
        tfDescription.textProperty().addListener((obs, o, n) -> change());
        cmCategory.valueProperty().addListener((obs, o, n) -> change());
        cmCategory.setConverter(new StringConverter<>() {
            @Override
            public String toString(KategorieGrenz object) {
                return object != null ? object.getName() : null;
            }
    
            @Override
            public KategorieGrenz fromString(String string) {
                return cmCategory.getItems().stream().filter(v -> v.getName().equals(string)).findAny().orElse(null);
            }
        });
    }
    
    private void init(DialogEvent e) {
        IKategorieSteuerung ks = e.get("categoryControl", IKategorieSteuerung.class);
        edit = e.get("selectedCategory", KategorieGrenz.class);
        
        if (edit == null) {
            cmCategory.getItems().addAll(ks.getKategorien());
            cmCategory.getItems().sort(Comparator.comparing(KategorieGrenz::getName));
            cmCategory.getItems().add(0, root);
            KategorieGrenz parent = e.get("parent", KategorieGrenz.class);
            if (parent != null)
                cmCategory.getSelectionModel().select(parent);
        } else {
            tfName.setText(edit.getName());
            tfDescription.setText(edit.getBeschreibung());
            if (edit.getParentKategorieId() == 0)
                cmCategory.setValue(root);
            else
                cmCategory.setValue(ks.getKategorieById(edit.getParentKategorieId()));
            cmCategory.setDisable(true);
        }
        
        change();
    }
    
    
    private boolean validateInput() {
        return !tfName.getText().isEmpty() && cmCategory.getSelectionModel().getSelectedItem() != null;
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
            KategorieGrenz k;
            if (edit != null) {
                k = edit;
                k.setName(tfName.getText());
                k.setBeschreibung(tfDescription.getText());
            } else
                k = new KategorieGrenz(null, tfName.getText(), tfDescription.getText(),
                                       cmCategory.getSelectionModel().getSelectedItem().getKategorieId());
            stage.getOwner().fireEvent(new DialogEvent(false, stage, new Parameter("category", k)));
            AdminApp.getInstance().closeStage(stage);
        }
    }
    
}
