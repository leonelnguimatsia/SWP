package de.thkoeln.swp.wawi.admingui.control.dialogs;


import de.thkoeln.swp.wawi.admingui.application.AdminApp;
import de.thkoeln.swp.wawi.admingui.util.*;
import de.thkoeln.swp.wawi.admingui.util.DialogEvent;
import de.thkoeln.swp.wawi.steuerungapi.admin.*;
import de.thkoeln.swp.wawi.steuerungapi.grenz.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.*;
import javafx.stage.*;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.time.LocalDate;


public class ProductController {
    
    
    @FXML private Button btConfirm;
    @FXML private TextField tfName;
    @FXML private TextField tfDescription;
    @FXML private Spinner<Integer> spCount;
    @FXML private Spinner<Double> spPrice;
    @FXML private Spinner<Integer> spTax;
    @FXML private CheckBox cbActive;
    @FXML private ComboBox<KategorieGrenz> cmCategory;
    @FXML private ComboBox<LagerortGrenz> cmLocation;
    
    private Stage stage;
    private ProduktGrenz product;
    
    
    @FXML
    private void initialize() {
        stage = AdminApp.getInstance().getLatestStage();
        stage.addEventHandler(DialogEvent.INIT, this::init);
        
        spCount.setValueFactory(new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        spPrice.setValueFactory(new DoubleSpinnerValueFactory(0, Double.MAX_VALUE, 10));
        spTax.setValueFactory(new IntegerSpinnerValueFactory(0, 100, 19));
        
        cmCategory.setConverter(new StringConverter<>() {
            @Override
            public String toString(KategorieGrenz object) {
                return object == null ? null : object.getName();
            }
    
            @Override
            public KategorieGrenz fromString(String string) {
                return cmCategory.getItems().stream().filter(v -> v.getName().equals(string)).findFirst().orElse(null);
            }
        });
        
        cmLocation.setConverter(new StringConverter<>() {
            @Override
            public String toString(LagerortGrenz object) {
                return object == null ? null : object.getBezeichnung();
            }
    
            @Override
            public LagerortGrenz fromString(String string) {
                return cmLocation.getItems().stream().filter(v -> v.getBezeichnung().equals(string)).findFirst().orElse(null);
            }
        });
    
        tfName.textProperty().addListener((u, v, w) -> change());
        spCount.valueProperty().addListener((u, v, w) -> change());
        spPrice.valueProperty().addListener((u, v, w) -> change());
        spTax.valueProperty().addListener((u, v, w) -> change());
        cmCategory.getSelectionModel().selectedItemProperty().addListener((u, v, w) -> change());
        cmLocation.getSelectionModel().selectedItemProperty().addListener((u, v, w) -> change());
    }
    
    private void init(DialogEvent e) {
        IKategorieSteuerung categoryControl = e.get("categoryControl", IKategorieSteuerung.class);
        ILagerSteuerung warehouseControl = e.get("warehouseControl", ILagerSteuerung.class);
        KategorieGrenz selectedCategory = e.get("selectedCategory", KategorieGrenz.class);
        product = e.get("product", ProduktGrenz.class);
        
        cmCategory.getItems().addAll(categoryControl.getKategorien());
        cmLocation.getItems().addAll(warehouseControl.getLagerorte());
        
        if (product != null) {
            tfName.setText(product.getName());
            tfDescription.setText(product.getBeschreibung());
            spCount.getValueFactory().setValue(product.getStueckzahl());
            spPrice.getValueFactory().setValue(product.getNettopreis().doubleValue());
            spTax.getValueFactory().setValue(product.getMwstsatz());
            cbActive.setSelected(product.isAktiv());
            cmCategory.getSelectionModel().select(product.getKategorieGrenz());
            cmLocation.getSelectionModel().select(product.getLagerortGrenz());
        } else if (selectedCategory != null)
            cmCategory.getSelectionModel().select(selectedCategory);
        change();
    }
    
    
    private boolean validateInput() {
        if (tfName.getText().isEmpty())
            return false;
        if (spCount.getValue() == null || spCount.getValue() < 0)
            return false;
        if (spPrice.getValue() == null || spPrice.getValue() < 0)
            return false;
        if (spTax.getValue() == null || spTax.getValue() < 0)
            return false;
        if (cmCategory.getSelectionModel().getSelectedItem() == null)
            return false;
        return cmLocation.getSelectionModel().getSelectedItem() != null;
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
            ProduktGrenz p;
            if (product == null) {
                p = new ProduktGrenz();
                p.setAngelegt(LocalDate.now());
            } else
                p = product;
            p.setName(tfName.getText());
            p.setBeschreibung(tfDescription.getText());
            p.setStueckzahl(spCount.getValue());
            p.setNettopreis(BigDecimal.valueOf(spPrice.getValue()));
            p.setMwstsatz(spTax.getValue());
            p.setAktiv(cbActive.isSelected());
            p.setKategorieGrenz(cmCategory.getSelectionModel().getSelectedItem());
            p.setLagerortGrenz(cmLocation.getSelectionModel().getSelectedItem());
            stage.getOwner().fireEvent(new DialogEvent(false, stage, new Parameter("product", p)));
            AdminApp.getInstance().closeStage(stage);
        }
    }
    
}
