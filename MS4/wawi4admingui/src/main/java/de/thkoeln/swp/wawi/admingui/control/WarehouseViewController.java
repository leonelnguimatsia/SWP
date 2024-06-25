package de.thkoeln.swp.wawi.admingui.control;


import de.thkoeln.swp.wawi.admingui.application.AdminApp;
import de.thkoeln.swp.wawi.admingui.util.DialogEvent;
import de.thkoeln.swp.wawi.admingui.util.*;
import de.thkoeln.swp.wawi.adminsteuerung.impl.*;
import de.thkoeln.swp.wawi.steuerungapi.admin.*;
import de.thkoeln.swp.wawi.steuerungapi.grenz.*;
import javafx.beans.property.*;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

import java.time.LocalDate;
import java.util.*;
import java.util.function.*;
import java.util.logging.Logger;

import static de.thkoeln.swp.wawi.admingui.util.Alerts.*;


public class WarehouseViewController {
    
    
    /*
     * UI Elements
     */
    
    @FXML private Label lbStatus;
    @FXML private Menu muWtFilter;
    @FXML private Menu muOFilter;
    @FXML private Menu muDFilter;
    @FXML private TabPane tabPane;
    @FXML private Tab tabWarehouseTraffic;
    @FXML private Tab tabOrders;
    @FXML private Tab tabDeliveries;
    
    // tabWarehouseTraffic
    @FXML private TableView<LagerverkehrGrenz> tbWarehouseTraffic;
    @FXML private TableColumn<LagerverkehrGrenz, Number> tcWtID;
    @FXML private TableColumn<LagerverkehrGrenz, String> tcWtType;
    @FXML private TableColumn<LagerverkehrGrenz, LocalDate> tcWtDate;
    @FXML private TableColumn<LagerverkehrGrenz, Number> tcWtPrice;
    @FXML private TableColumn<LagerverkehrGrenz, String> tcWtStatus;
    @FXML private TableView<Integer> tbWtItemInfo;
    @FXML private TableColumn<Integer, String> tcWtInfoAttribute;
    @FXML private TableColumn<Integer, String> tcWtInfoValue;
    @FXML private TableView<Object> tbWtPositions;
    @FXML private TableColumn<Object, Number> tcWtPosID;
    @FXML private TableColumn<Object, String> tcWtPosProduct;
    @FXML private TableColumn<Object, Number> tcWtPosCount;
    @FXML private MenuItem miWtID;
    @FXML private MenuItem miWtType;
    @FXML private MenuItem miWtDate;
    @FXML private MenuItem miWtPrice;
    @FXML private MenuItem miWtState;
    @FXML private MenuItem miWtReset;
    
    // tabOrders
    @FXML private TableView<BestellungGrenz> tbOrders;
    @FXML private TableColumn<BestellungGrenz, Number> tcOID;
    @FXML private TableColumn<BestellungGrenz, LocalDate> tcODate;
    @FXML private TableColumn<BestellungGrenz, String> tcOState;
    @FXML private TableColumn<BestellungGrenz, Number> tcONetPrice;
    @FXML private TableColumn<BestellungGrenz, Number> tcOGrossPrice;
    @FXML private TableColumn<BestellungGrenz, String> tcODeliveryAddress;
    @FXML private TableColumn<BestellungGrenz, String> tcOBillingAddress;
    @FXML private TableView<BestellungspositionGrenz> tbOPos;
    @FXML private TableColumn<BestellungspositionGrenz, Number> tcOPosID;
    @FXML private TableColumn<BestellungspositionGrenz, String> tcOPosProduct;
    @FXML private TableColumn<BestellungspositionGrenz, Number> tcOPosCount;
    @FXML private MenuItem miOID;
    @FXML private MenuItem miODate;
    @FXML private MenuItem miOState;
    @FXML private MenuItem miONetPrice;
    @FXML private MenuItem miOGrossPrice;
    @FXML private MenuItem miODeliveryAddress;
    @FXML private MenuItem miOBillingAddress;
    @FXML private MenuItem miOReset;
    
    // tabDeliveries
    @FXML private TableView<EinlieferungGrenz> tbDeliveries;
    @FXML private TableColumn<EinlieferungGrenz, Number> tcDID;
    @FXML private TableColumn<EinlieferungGrenz, LocalDate> tcDDate;
    @FXML private TableColumn<EinlieferungGrenz, Number> tcDPrice;
    @FXML private TableView<LieferpositionGrenz> tbDPos;
    @FXML private TableColumn<LieferpositionGrenz, Number> tcDPosID;
    @FXML private TableColumn<LieferpositionGrenz, String> tcDPosProduct;
    @FXML private TableColumn<LieferpositionGrenz, Number> tcDPosCount;
    @FXML private TableColumn<LieferpositionGrenz, Number> tcDPosPrice;
    @FXML private TableColumn<LieferpositionGrenz, String> tcDPosState;
    @FXML private MenuItem miDID;
    @FXML private MenuItem miDDate;
    @FXML private MenuItem miDPrice;
    @FXML private MenuItem miDReset;
    
    
    /*
     * Caches
     */
    
    private final Map<Integer, LagerverkehrGrenz> wtCache = new HashMap<>();
    private final Map<Integer, BestellungGrenz> oCache = new HashMap<>();
    private final Map<Integer, EinlieferungGrenz> dCache = new HashMap<>();
    
    
    /*
     * Attribute mapping for info table on tabWarehouseTraffic
     */
    
    private final List<String> orderAttributes, deliveryAttributes;
    private final List<Function<BestellungGrenz, ?>> orderExtractors;
    private final List<Function<EinlieferungGrenz, ?>> deliveryExtractors;
    
    {
        orderAttributes = Arrays.asList("Bestellungs-ID", "Erstellungsdatum", "Nettopreis", "Bruttopreis",
                                        "Lieferadresse", "Rechnungsadresse", "Status");
        orderExtractors = Arrays.asList(BestellungGrenz::getBestellungId, BestellungGrenz::getCreated,
                                        BestellungGrenz::getGesamtnetto, BestellungGrenz::getGesamtbrutto,
                                        BestellungGrenz::getLieferadresse, BestellungGrenz::getRechnungsadresse,
                                        BestellungGrenz::getStatus);
        deliveryAttributes = Arrays.asList("Einlieferuns-ID", "Erstellungsdatum", "Gesamtpreis");
        deliveryExtractors = Arrays.asList(EinlieferungGrenz::getEinlieferungId, EinlieferungGrenz::getCreated,
                                           EinlieferungGrenz::getGesamtpreis);
    }
    
    
    /*
     * Attributes
     */
    
    private Stage stage;
    private ILagerSteuerung lagerSteuerung;
    private IEinlieferungSteuerung einlieferungSteuerung;
    private IBestellungSteuerung bestellungSteuerung;
    private Object wtSelectedPos;
    
    private final List<TableFilter<LagerverkehrGrenz>> wtFilters = new LinkedList<>();
    private final List<TableFilter<BestellungGrenz>> oFilters = new LinkedList<>();
    private final List<TableFilter<EinlieferungGrenz>> dFilters = new LinkedList<>();
    private final Map<Object, Consumer<DialogEvent>> dialogCallbacks = new HashMap<>();
    
    private static final Logger LOGGER = Logger.getLogger(WarehouseViewController.class.getName());
    private static final Color information = Color.BLACK;
    private static final Color progress = Color.BLUE;
    private static final Color warning = Color.ORANGE.darker();
    private static final Color error = Color.RED;
    
    
    /*
     * Init
     */
    
    @FXML
    private void initialize() {
        LOGGER.fine("Initializing Controller");
        
        stage = AdminApp.getInstance().getWarehouseStage();
        stage.addEventHandler(DialogEvent.RETURN, this::dialogReturn);
        
        lagerSteuerung = new LagerSteuerungImpl();
        einlieferungSteuerung = new EinlieferungSteuerungImpl();
        bestellungSteuerung = new BestellungSteuerungImpl();
        
        initWtTables();
        initOTables();
        initDTables();
        
        tbOrders.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> oSelectionChange(n));
        tbDeliveries.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> dSelectionChange(n));
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> tabChange(n));
        
        tabPane.getSelectionModel().select(tabWarehouseTraffic);
        tabChange(tabWarehouseTraffic);
    }
    
    private void initWtTables() {
        tbWarehouseTraffic.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> wtSelectionChange(n));
        
        tcWtID.setCellValueFactory(v -> new SimpleIntegerProperty(v.getValue().getLagerverkehrId()));
        tcWtType.setCellValueFactory(v -> new SimpleStringProperty(type(v.getValue())));
        tcWtDate.setCellValueFactory(v -> new SimpleObjectProperty<>(v.getValue().getCreated()));
        tcWtPrice.setCellValueFactory(v -> new SimpleDoubleProperty(
                v.getValue().getBestellungGrenz() != null ?
                v.getValue().getBestellungGrenz().getGesamtnetto().doubleValue() :
                v.getValue().getEinlieferungGrenz().getGesamtpreis().doubleValue()));
        tcWtStatus.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getStatus()));
        
        tcWtInfoAttribute.setCellValueFactory(v -> new SimpleStringProperty(
                wtSelectedPos instanceof BestellungGrenz ? orderAttributes.get(v.getValue()) : deliveryAttributes.get(v.getValue())));
        tcWtInfoValue.setCellValueFactory(v -> new SimpleStringProperty(
                (wtSelectedPos instanceof BestellungGrenz b ? orderExtractors.get(v.getValue()).apply(b) :
                 deliveryExtractors.get(v.getValue()).apply((EinlieferungGrenz) wtSelectedPos)).toString()));
        
        tcWtPosID.setCellValueFactory(v -> new SimpleIntegerProperty(
                v.getValue() instanceof BestellungspositionGrenz b ? b.getBestellungspositionId() :
                ((LieferpositionGrenz) v.getValue()).getLieferpositionId()));
        tcWtPosProduct.setCellValueFactory(v -> new SimpleStringProperty(
                v.getValue() instanceof BestellungspositionGrenz b ? b.getProduktName() :
                ((LieferpositionGrenz) v.getValue()).getProduktName()));
        tcWtPosCount.setCellValueFactory(v -> new SimpleIntegerProperty(
                v.getValue() instanceof BestellungspositionGrenz b ? b.getAnzahl() :
                ((LieferpositionGrenz) v.getValue()).getAnzahl()));
        
        PseudoClass order = PseudoClass.getPseudoClass("order"), delivery = PseudoClass.getPseudoClass("delivery");
        tbWarehouseTraffic.setRowFactory(v -> new TableRow<>() {
            @Override
            protected void updateItem(LagerverkehrGrenz item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    if (item.getBestellungGrenz() != null) {
                        pseudoClassStateChanged(order, true);
                        pseudoClassStateChanged(delivery, false);
                    } else if (item.getEinlieferungGrenz() != null) {
                        pseudoClassStateChanged(delivery, true);
                        pseudoClassStateChanged(order, false);
                    }
                }
            }
        });
    }
    
    private void initOTables() {
        tbOrders.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> oSelectionChange(n));
        
        tcOID.setCellValueFactory(v -> new SimpleIntegerProperty(v.getValue().getBestellungId()));
        tcOBillingAddress.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getRechnungsadresse()));
        tcODeliveryAddress.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getLieferadresse()));
        tcODate.setCellValueFactory(v -> new SimpleObjectProperty<>(v.getValue().getCreated()));
        tcONetPrice.setCellValueFactory(v -> new SimpleDoubleProperty(v.getValue().getGesamtnetto().doubleValue()));
        tcOGrossPrice.setCellValueFactory(v -> new SimpleDoubleProperty(v.getValue().getGesamtbrutto().doubleValue()));
        tcOState.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getStatus()));
        
        tcOPosID.setCellValueFactory(v -> new SimpleIntegerProperty(v.getValue().getBestellungspositionId()));
        tcOPosProduct.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getProduktName()));
        tcOPosCount.setCellValueFactory(v -> new SimpleIntegerProperty(v.getValue().getAnzahl()));
    }
    
    private void initDTables() {
        tbDeliveries.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> dSelectionChange(n));
        
        tcDID.setCellValueFactory(v -> new SimpleIntegerProperty(v.getValue().getEinlieferungId()));
        tcDDate.setCellValueFactory(v -> new SimpleObjectProperty<>(v.getValue().getCreated()));
        tcDPrice.setCellValueFactory(v -> new SimpleDoubleProperty(v.getValue().getGesamtpreis().doubleValue()));
        
        tcDPosID.setCellValueFactory(v -> new SimpleIntegerProperty(v.getValue().getLieferpositionId()));
        tcDPosCount.setCellValueFactory(v -> new SimpleIntegerProperty(v.getValue().getAnzahl()));
        tcDPosPrice.setCellValueFactory(v -> new SimpleDoubleProperty(v.getValue().getKaufpreis().doubleValue()));
        tcDPosProduct.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getProduktName()));
        tcDPosState.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getStatus()));
    }
    
    
    /*
     * Status display
     */
    
    private void clearStatus() {
        showInformation("");
    }
    
    private void showInformation(String content) {
        lbStatus.setTextFill(information);
        lbStatus.setText(content);
    }
    
    private void showProgress(String content) {
        lbStatus.setTextFill(progress);
        lbStatus.setText(content);
    }
    
    private void showWarning(String content) {
        lbStatus.setTextFill(warning);
        lbStatus.setText(content);
    }
    
    private void showError(String content) {
        lbStatus.setTextFill(error);
        lbStatus.setText(content);
    }
    
    
    /*
     * Update
     */
    
    private void reloadWarehouseTraffic(boolean reloadFromDB) {
        if (reloadFromDB || wtCache.isEmpty()) {
            showProgress("Lagerverkehr wird aus Datenbank geladen...");
            LOGGER.fine("Reloading from DB");
            wtCache.clear();
            lagerSteuerung.getLagerverkehre().forEach(v -> wtCache.put(v.getLagerverkehrId(), v));
            clearStatus();
        }
        
        showProgress("Lagerverkehrstabelle wird erstellt...");
        wtSelectedPos = null;
        tbWtItemInfo.getItems().clear();
        tbWtPositions.getItems().clear();
        tbWarehouseTraffic.getSelectionModel().clearSelection();
        tbWarehouseTraffic.getItems().clear();
        tbWarehouseTraffic.getItems().addAll(wtCache.values());
        
        wtFilters.forEach(v -> tbWarehouseTraffic.getItems().removeIf(v.negate()));
        
        tbWarehouseTraffic.refresh();
        tbWtItemInfo.refresh();
        tbWtPositions.refresh();
        clearStatus();
    }
    
    private void reloadOrders(boolean reloadFromDB) {
        if (reloadFromDB || oCache.isEmpty()) {
            showProgress("Bestellungen werden aus Datenbank geladen...");
            LOGGER.fine("Reloading from DB");
            oCache.clear();
            bestellungSteuerung.getBestellungen().forEach(v -> oCache.put(v.getBestellungId(), v));
            clearStatus();
        }
        
        showProgress("Bestellungstabelle wird erstellt...");
        tbOPos.getItems().clear();
        tbOrders.getSelectionModel().clearSelection();
        tbOrders.getItems().clear();
        tbOrders.getItems().addAll(oCache.values());
        
        oFilters.forEach(v -> tbOrders.getItems().removeIf(v.negate()));
        
        tbOrders.refresh();
        tbOPos.refresh();
        clearStatus();
    }
    
    private void reloadDeliveries(boolean reloadFromDB) {
        if (reloadFromDB || dCache.isEmpty()) {
            showProgress("Einlieferungen werden aus Datenbank geladen...");
            LOGGER.fine("Reloading from DB");
            dCache.clear();
            einlieferungSteuerung.getEinlieferungen().forEach(v -> dCache.put(v.getEinlieferungId(), v));
            clearStatus();
        }
        
        showProgress("Einlieferungstabelle wird erstellt...");
        tbDPos.getItems().clear();
        tbDeliveries.getSelectionModel().clearSelection();
        tbDeliveries.getItems().clear();
        tbDeliveries.getItems().addAll(dCache.values());
        
        dFilters.forEach(v -> tbDeliveries.getItems().removeIf(v.negate()));
        
        tbDeliveries.refresh();
        tbDPos.refresh();
        clearStatus();
    }
    
    
    /*
     * Callbacks
     */
    
    private void unresolvedCallback(WindowEvent e) {
        Stage dialog = (Stage) e.getSource();
        AdminApp.getInstance().closeStage(dialog);
        if (dialogCallbacks.remove(dialog) != null)
            LOGGER.info("DialogCallback(WarehouseViewController): Unsatisfied return callback");
    }
    
    private void filterWtCallback(DialogEvent e) {
        try {
            Predicate<?> filter = e.get("filter", Predicate.class);
            if (filter == null)
                showError("Tabellenfilter hinzufügen fehlgeschlagen!");
            else {
                //noinspection CastCanBeRemovedNarrowingVariableType,unchecked
                wtFilters.add(new TableFilter<>((Predicate<LagerverkehrGrenz>) filter));
                reloadWarehouseTraffic(false);
                miWtReset.setDisable(false);
            }
        } catch (ClassCastException ex) {
            LOGGER.warning("DialogCallback: Bad generic type on attribute 'filter'");
            showError("Tabellenfilter hinzufügen fehlgeschlagen!");
        }
    }
    
    private void filterOCallback(DialogEvent e) {
        try {
            Predicate<?> filter = e.get("filter", Predicate.class);
            if (filter == null)
                showError("Tabellenfilter hinzufügen fehlgeschlagen!");
            else {
                //noinspection CastCanBeRemovedNarrowingVariableType,unchecked
                oFilters.add(new TableFilter<>((Predicate<BestellungGrenz>) filter));
                reloadOrders(false);
                miOReset.setDisable(false);
            }
        } catch (ClassCastException ex) {
            LOGGER.warning("DialogCallback: Bad generic type on attribute 'filter'");
            showError("Tabellenfilter hinzufügen fehlgeschlagen!");
        }
    }
    
    private void filterDCallback(DialogEvent e) {
        try {
            Predicate<?> filter = e.get("filter", Predicate.class);
            if (filter == null)
                showError("Tabellenfilter hinzufügen fehlgeschlagen!");
            else {
                //noinspection CastCanBeRemovedNarrowingVariableType,unchecked
                dFilters.add(new TableFilter<>((Predicate<EinlieferungGrenz>) filter));
                reloadDeliveries(false);
                miDReset.setDisable(false);
            }
        } catch (ClassCastException ex) {
            LOGGER.warning("DialogCallback: Bad generic type on attribute 'filter'");
            showError("Tabellenfilter hinzufügen fehlgeschlagen!");
        }
    }
    
    private void searchWtCallback(DialogEvent e) {
        TableColumn<?, ?> c = e.get("attribute", TableColumn.class);
        String value = e.get("value", String.class);
        
        Predicate<LagerverkehrGrenz> p;
        if (c == tcWtID)
            p = v -> v.getLagerverkehrId().toString().equals(value);
        else if (c == tcWtDate)
            p = v -> v.getCreated().toString().equals(value);
        else if (c == tcWtPrice)
            p = v -> v.getBestellungGrenz() != null ? String.valueOf(v.getBestellungGrenz().getGesamtnetto().doubleValue()).equals(value) :
                     String.valueOf(v.getEinlieferungGrenz().getGesamtpreis().doubleValue()).equals(value);
        else if (c == tcWtStatus)
            p = v -> v.getStatus().equals(value);
        else if (c == tcWtType)
            p = v -> type(v).equals(value);
        else {
            LOGGER.warning("DialogCallback: Bad value on attribute 'attribute'");
            showError("Suche fehlgeschlagen!");
            return;
        }
        
        Optional<LagerverkehrGrenz> o = tbWarehouseTraffic.getItems().stream().filter(p).findFirst();
        if (o.isPresent())
            tbWarehouseTraffic.getSelectionModel().select(o.get());
        else
            showInformation("Lagerverkehr wurde nicht in der Tabelle gefunden.");
    }
    
    private void searchOCallback(DialogEvent e) {
        TableColumn<?, ?> c = e.get("attribute", TableColumn.class);
        String value = e.get("value", String.class);
    
        Predicate<BestellungGrenz> p;
        if (c == tcOID)
            p = v -> v.getBestellungId().toString().equals(value);
        else if (c == tcOBillingAddress)
            p = v -> v.getRechnungsadresse().equals(value);
        else if (c == tcODeliveryAddress)
            p = v -> v.getLieferadresse().equals(value);
        else if (c == tcODate)
            p = v -> v.getCreated().toString().equals(value);
        else if (c == tcONetPrice)
            p = v -> String.valueOf(v.getGesamtnetto().doubleValue()).equals(value);
        else if (c == tcOGrossPrice)
            p = v -> String.valueOf(v.getGesamtbrutto().doubleValue()).equals(value);
        else if (c == tcOState)
            p = v -> v.getStatus().equals(value);
        else {
            LOGGER.warning("DialogCallback: Bad value on attribute 'attribute'");
            showError("Suche fehlgeschlagen!");
            return;
        }
        
        Optional<BestellungGrenz> o = tbOrders.getItems().stream().filter(p).findFirst();
        if (o.isPresent())
            tbOrders.getSelectionModel().select(o.get());
        else
            showInformation("Bestellung wurde nicht in der Tabelle gefunden.");
    }
    
    private void searchDCallback(DialogEvent e) {
        TableColumn<?, ?> c = e.get("attribute", TableColumn.class);
        String value = e.get("value", String.class);
    
        Predicate<EinlieferungGrenz> p;
        if (c == tcDID)
            p = v -> v.getEinlieferungId().toString().equals(value);
        else if (c == tcDDate)
            p = v -> v.getCreated().toString().equals(value);
        else if (c == tcDPrice)
            p = v -> String.valueOf(v.getGesamtpreis().doubleValue()).equals(value);
        else {
            LOGGER.warning("DialogCallback: Bad value on attribute 'attribute'");
            showError("Suche fehlgeschlagen!");
            return;
        }
    
        Optional<EinlieferungGrenz> o = tbDeliveries.getItems().stream().filter(p).findFirst();
        if (o.isPresent())
            tbDeliveries.getSelectionModel().select(o.get());
        else
            showInformation("Einlieferung wurde nicht in der Tabelle gefunden.");
    }
    
    
    /*
     * Util
     */
    
    private String type(LagerverkehrGrenz lg) {
        return lg.getBestellungGrenz() != null ? "Bestellung" : "Einlieferung";
    }
    
    private Parameter param(String name, Object value) {
        return new Parameter(name, value);
    }
    
    
    /*
     * Listeners
     */
    
    private void dialogReturn(DialogEvent data) {
        if (!dialogCallbacks.containsKey(data.SOURCE))
            LOGGER.warning("Unhandled dialog return");
        else
            dialogCallbacks.remove(data.SOURCE).accept(data);
    }
    
    
    private void tabChange(Tab t) {
        muWtFilter.setVisible(false);
        muOFilter.setVisible(false);
        muDFilter.setVisible(false);
        
        if (t == tabWarehouseTraffic) {
            muWtFilter.setVisible(true);
            reloadWarehouseTraffic(false);
        } else if (t == tabOrders) {
            muOFilter.setVisible(true);
            reloadOrders(false);
        } else if (t == tabDeliveries) {
            muDFilter.setVisible(true);
            reloadDeliveries(false);
        }
    }
    
    private void wtSelectionChange(LagerverkehrGrenz n) {
        tbWtItemInfo.getItems().clear();
        tbWtPositions.getItems().clear();
        
        if (n != null) {
            if (n.getBestellungGrenz() != null) {
                wtSelectedPos = n.getBestellungGrenz();
                for (int i = 0; i < orderAttributes.size(); i++)
                    tbWtItemInfo.getItems().add(i, i);
                n.getBestellungGrenz().getBestellungspositionGrenzList().forEach(v -> tbWtPositions.getItems().add(v));
            } else if (n.getEinlieferungGrenz() != null) {
                wtSelectedPos = n.getEinlieferungGrenz();
                for (int i = 0; i < deliveryAttributes.size(); i++)
                    tbWtItemInfo.getItems().add(i, i);
                n.getEinlieferungGrenz().getLieferpositionenGrenz().forEach(v -> tbWtPositions.getItems().add(v));
            }
        }
        
        tbWtItemInfo.refresh();
        tbWtPositions.refresh();
    }
    
    private void oSelectionChange(BestellungGrenz n) {
        tbOPos.getItems().clear();
        if (n != null)
            tbOPos.getItems().addAll(n.getBestellungspositionGrenzList());
        tbOPos.refresh();
    }
    
    private void dSelectionChange(EinlieferungGrenz n) {
        tbDPos.getItems().clear();
        if (n != null)
            tbDPos.getItems().addAll(n.getLieferpositionenGrenz());
        tbDPos.refresh();
    }
    
    
    @FXML
    private void reload() {
        wtCache.clear();
        oCache.clear();
        dCache.clear();
        
        Tab t = tabPane.getSelectionModel().getSelectedItem();
        if (tabWarehouseTraffic == t)
            reloadWarehouseTraffic(true);
        else if (tabOrders == t)
            reloadOrders(true);
        else if (tabDeliveries == t)
            reloadDeliveries(true);
        showInformation("Daten wurden neu geladen");
    }
    
    @FXML
    private void openMainMenu() {
        AdminApp.getInstance().showMainMenu();
    }
    
    @FXML
    private void closeWindow() {
        AdminApp.getInstance().getWarehouseStage().close();
        AdminApp.getInstance().showMainMenu();
    }
    
    @FXML
    private void exit() {
        if (agreed(showYesNoDialog(null, "Möchten Sie das Programm beenden?", "Programm beenden",
                                   "SWP - WAWI - Admin: Programm Beenden")))
            AdminApp.getInstance().exit();
    }
    
    @FXML
    private void filterWt(ActionEvent e) {
        Object s = e.getSource();
        Stage dialog;
        
        if (s == miWtID) {
            Parameter a = param("attribute", tcWtID.getText());
            Parameter d = param("decimal", false);
            Parameter k = param("keyExtractor", (NumberKeyExtractor) v -> ((LagerverkehrGrenz) v).getLagerverkehrId());
            dialog = AdminApp.getInstance().createStage(stage, "/dialogs/filterNumber.fxml", a, d, k);
            dialog.setTitle("SWP - WAWI - Admin: Nach " + a.value() + " filtern");
        } else if (s == miWtType) {
            Parameter a = param("attribute", tcWtID.getText());
            Parameter k = param("keyExtractor", (StringKeyExtractor) v -> type((LagerverkehrGrenz) v));
            dialog = AdminApp.getInstance().createStage(stage, "/dialogs/filterString.fxml", a, k);
            dialog.setTitle("SWP - WAWI - Admin: Nach " + a.value() + " filtern");
        } else if (s == miWtDate) {
            Parameter a = param("attribute", tcWtDate.getText());
            Parameter k = param("keyExtractor", (DateKeyExtractor) v -> ((LagerverkehrGrenz) v).getCreated());
            dialog = AdminApp.getInstance().createStage(stage, "/dialogs/filterDate.fxml", a, k);
            dialog.setTitle("SWP - WAWI - Admin: Nach " + a.value() + " filtern");
        } else if (s == miWtPrice) {
            Parameter a = param("attribute", tcWtPrice.getText());
            Parameter d = param("decimal", true);
            Parameter k = param("keyExtractor", (NumberKeyExtractor) v -> ((LagerverkehrGrenz) v).getBestellungGrenz() != null ?
                                                                          ((LagerverkehrGrenz) v).getBestellungGrenz().getGesamtnetto() :
                                                                          ((LagerverkehrGrenz) v).getEinlieferungGrenz().getGesamtpreis());
            dialog = AdminApp.getInstance().createStage(stage, "/dialogs/filterNumber.fxml", a, d, k);
            dialog.setTitle("SWP - WAWI - Admin: Nach " + a.value() + " filtern");
        } else {
            Parameter a = param("attribute", tcWtStatus.getText());
            Parameter k = param("keyExtractor", (StringKeyExtractor) v -> ((LagerverkehrGrenz) v).getStatus());
            dialog = AdminApp.getInstance().createStage(stage, "/dialogs/filterString.fxml", a, k);
            dialog.setTitle("SWP - WAWI - Admin: Nach " + a.value() + " filtern");
        }
        
        dialogCallbacks.put(dialog, this::filterWtCallback);
        dialog.setOnCloseRequest(this::unresolvedCallback);
        dialog.show();
    }
    
    @FXML
    private void removeFiltersWt() {
        wtFilters.clear();
        miWtReset.setDisable(true);
        reloadWarehouseTraffic(false);
    }
    
    @FXML
    private void filterO(ActionEvent e) {
        Object s = e.getSource();
        Stage dialog;
        
        if (s == miOID) {
            Parameter a = param("attribute", tcOID.getText());
            Parameter d = param("decimal", false);
            Parameter k = param("keyExtractor", (NumberKeyExtractor) v -> ((BestellungGrenz) v).getBestellungId());
            dialog = AdminApp.getInstance().createStage(stage, "/dialogs/filterNumber.fxml", a, d, k);
            dialog.setTitle("SWP - WAWI - Admin: Nach " + a.value() + " filtern");
        } else if (s == miOBillingAddress) {
            Parameter a = param("attribute", tcOBillingAddress.getText());
            Parameter k = param("keyExtractor", (StringKeyExtractor) v -> ((BestellungGrenz) v).getRechnungsadresse());
            dialog = AdminApp.getInstance().createStage(stage, "/dialogs/filterString.fxml", a, k);
            dialog.setTitle("SWP - WAWI - Admin: Nach " + a.value() + " filtern");
        } else if (s == miODeliveryAddress) {
            Parameter a = param("attribute", tcODeliveryAddress.getText());
            Parameter k = param("keyExtractor", (StringKeyExtractor) v -> ((BestellungGrenz) v).getLieferadresse());
            dialog = AdminApp.getInstance().createStage(stage, "/dialogs/filterString.fxml", a, k);
            dialog.setTitle("SWP - WAWI - Admin: Nach " + a.value() + " filtern");
        } else if (s == miODate) {
            Parameter a = param("attribute", tcODate.getText());
            Parameter k = param("keyExtractor", (DateKeyExtractor) v -> ((BestellungGrenz) v).getCreated());
            dialog = AdminApp.getInstance().createStage(stage, "/dialogs/filterDate.fxml", a, k);
            dialog.setTitle("SWP - WAWI - Admin: Nach " + a.value() + " filtern");
        } else if (s == miOGrossPrice) {
            Parameter a = param("attribute", tcOGrossPrice.getText());
            Parameter d = param("decimal", true);
            Parameter k = param("keyExtractor", (NumberKeyExtractor) v -> ((BestellungGrenz) v).getGesamtbrutto());
            dialog = AdminApp.getInstance().createStage(stage, "/dialogs/filterNumber.fxml", a, d, k);
            dialog.setTitle("SWP - WAWI - Admin: Nach " + a.value() + " filtern");
        } else if (s == miONetPrice) {
            Parameter a = param("attribute", tcONetPrice.getText());
            Parameter d = param("decimal", true);
            Parameter k = param("keyExtractor", (NumberKeyExtractor) v -> ((BestellungGrenz) v).getGesamtnetto());
            dialog = AdminApp.getInstance().createStage(stage, "/dialogs/filterNumber.fxml", a, d, k);
            dialog.setTitle("SWP - WAWI - Admin: Nach " + a.value() + " filtern");
        } else {
            Parameter a = param("attribute", tcOState.getText());
            Parameter k = param("keyExtractor", (StringKeyExtractor) v -> ((BestellungGrenz) v).getStatus());
            dialog = AdminApp.getInstance().createStage(stage, "/dialogs/filterString.fxml", a, k);
            dialog.setTitle("SWP - WAWI - Admin: Nach " + a.value() + " filtern");
        }
        
        dialogCallbacks.put(dialog, this::filterOCallback);
        dialog.setOnCloseRequest(this::unresolvedCallback);
        dialog.show();
    }
    
    @FXML
    private void removeFiltersO() {
        oFilters.clear();
        miOReset.setDisable(true);
        reloadOrders(false);
    }
    
    @FXML
    private void filterD(ActionEvent e) {
        Object s = e.getSource();
        Stage dialog;
        
        if (s == miDID) {
            Parameter a = param("attribute", tcDID.getText());
            Parameter d = param("decimal", false);
            Parameter k = param("keyExtractor", (NumberKeyExtractor) v -> ((EinlieferungGrenz) v).getEinlieferungId());
            dialog = AdminApp.getInstance().createStage(stage, "/dialogs/filterNumber.fxml", a, d, k);
            dialog.setTitle("SWP - WAWI - Admin: Nach " + a.value() + " filtern");
        } else if (s == miDDate) {
            Parameter a = param("attribute", tcDDate.getText());
            Parameter k = param("keyExtractor", (DateKeyExtractor) v -> ((EinlieferungGrenz) v).getCreated());
            dialog = AdminApp.getInstance().createStage(stage, "/dialogs/filterDate.fxml", a, k);
            dialog.setTitle("SWP - WAWI - Admin: Nach " + a.value() + " filtern");
        } else {
            Parameter a = param("attribute", tcDPrice.getText());
            Parameter d = param("decimal", true);
            Parameter k = param("keyExtractor", (NumberKeyExtractor) v -> ((EinlieferungGrenz) v).getGesamtpreis());
            dialog = AdminApp.getInstance().createStage(stage, "/dialogs/filterNumber.fxml", a, d, k);
            dialog.setTitle("SWP - WAWI - Admin: Nach " + a.value() + " filtern");
        }
        
        dialogCallbacks.put(dialog, this::filterDCallback);
        dialog.setOnCloseRequest(this::unresolvedCallback);
        dialog.show();
    }
    
    @FXML
    private void removeFiltersD() {
        dFilters.clear();
        miDReset.setDisable(true);
        reloadDeliveries(false);
    }
    
    @FXML
    private void search() {
        Tab t = tabPane.getSelectionModel().getSelectedItem();
        Stage dialog;
    
        if (t == tabWarehouseTraffic) {
            Parameter a = param("attributes", tbWarehouseTraffic.getColumns());
            dialog = AdminApp.getInstance().createStage(stage, "/dialogs/search.fxml", a);
            dialogCallbacks.put(dialog, this::searchWtCallback);
        } else if (t == tabOrders) {
            Parameter a = param("attributes", tbOrders.getColumns());
            dialog = AdminApp.getInstance().createStage(stage, "/dialogs/search.fxml", a);
            dialogCallbacks.put(dialog, this::searchOCallback);
        } else {
            Parameter a = param("attributes", tbDeliveries.getColumns());
            dialog = AdminApp.getInstance().createStage(stage, "/dialogs/search.fxml", a);
            dialogCallbacks.put(dialog, this::searchDCallback);
        }
    
        dialog.setTitle("SWP - WAWI - Admin: Tabelle durchsuchen");
        dialog.setOnCloseRequest(this::unresolvedCallback);
        dialog.show();
    }
    
}
