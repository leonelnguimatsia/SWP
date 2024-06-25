package de.thkoeln.swp.wawi.admingui.control;


import de.thkoeln.swp.wawi.admingui.application.AdminApp;
import de.thkoeln.swp.wawi.admingui.util.DialogEvent;
import de.thkoeln.swp.wawi.admingui.util.*;
import de.thkoeln.swp.wawi.adminsteuerung.impl.*;
import de.thkoeln.swp.wawi.steuerungapi.admin.*;
import de.thkoeln.swp.wawi.steuerungapi.grenz.*;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

import java.io.IOException;
import java.util.*;
import java.util.function.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static de.thkoeln.swp.wawi.admingui.util.Alerts.*;


public class ProductViewController {
    
    
    /*
     * Inner classes
     */
    
    
    // class to display categories correctly in the tree
    private record CategoryView(KategorieGrenz category) {
        
        
        @Override
        public String toString() {
            return category.getName();
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof KategorieGrenz k)
                return Objects.equals(k.getKategorieId(), category.getKategorieId());
            if (obj instanceof CategoryView c)
                return Objects.equals(c.category.getKategorieId(), category.getKategorieId());
            if (obj instanceof Integer i)
                return Objects.equals(i, category.getKategorieId());
            if (obj == null)
                return category.getKategorieId() == null;
            return false;
        }
        
    }
    
    
    /*
     + Attributes
     */
    
    // UI elements
    @FXML private MenuItem miEditProduct;
    @FXML private MenuItem miActivateProduct;
    @FXML private MenuItem miRemoveProduct;
    @FXML private CheckMenuItem miShowSubcategories;
    @FXML private MenuItem miRemoveFilters;
    @FXML private MenuItem miEditCategory;
    @FXML private MenuItem miRemoveCategory;
    @FXML private Label lbStatus;
    @FXML private TreeView<CategoryView> trCategory;
    @FXML private TableView<ProduktGrenz> tbProduct;
    @FXML private TableColumn<ProduktGrenz, Number> tcProductID;
    @FXML private TableColumn<ProduktGrenz, String> tcProductName;
    @FXML private TableColumn<ProduktGrenz, String> tcProductCategory;
    @FXML private TableColumn<ProduktGrenz, Number> tcProductCount;
    @FXML private TableColumn<ProduktGrenz, String> tcProductActivated;
    
    private Stage stage;
    
    // caches
    private final Map<Integer, KategorieGrenz> categoryCache = new HashMap<>();
    private final Map<Integer, ProduktGrenz> productCache = new HashMap<>();
    
    // data access
    private IProduktSteuerung produktSteuerung;
    private IKategorieSteuerung kategorieSteuerung;
    private ILagerSteuerung lagerSteuerung;
    private IBestellungSteuerung bestellungSteuerung;
    private IEinlieferungSteuerung einlieferungSteuerung;
    
    // product table
    private boolean showSubcategories = true;
    private final List<TableFilter<ProduktGrenz>> tableFilters = new LinkedList<>();
    private ProduktGrenz selectedProduct;
    
    // category tree
    private TreeItem<CategoryView> selectedCategory;
    private Image treeIcon;
    
    // other
    private static final Logger LOGGER = Logger.getLogger(ProductViewController.class.getName());
    private static final Color information = Color.BLACK;
    private static final Color progress = Color.BLUE;
    private static final Color warning = Color.ORANGE.darker();
    private static final Color error = Color.RED;
    
    private final Map<Object, Consumer<DialogEvent>> dialogCallbacks = new HashMap<>();
    
    private ItemGroup categoryContext;
    private ItemGroup productContext;
    
    
    
    /*
     * Init
     */
    
    @FXML
    private void initialize() throws IOException {
        LOGGER.fine("Initializing controller");
        
        stage = AdminApp.getInstance().getProductStage();
        categoryContext = ItemGroup.of(miEditCategory, miRemoveCategory);
        productContext = ItemGroup.of(miEditProduct, miActivateProduct, miRemoveProduct);
        produktSteuerung = new ProduktSteuerungImpl();
        kategorieSteuerung = new KategorieSteuerungImpl();
        lagerSteuerung = new LagerSteuerungImpl();
        bestellungSteuerung = new BestellungSteuerungImpl();
        einlieferungSteuerung = new EinlieferungSteuerungImpl();
        treeIcon = new Image(getClass().getResource("/icons/trCategoryIcon.png").openStream());
        
        stage.addEventHandler(DialogEvent.RETURN, this::dialogReturn);
        
        initTree();
        initTable();
        reloadCategoryTree(true);
        reloadProductTable(true);
    }
    
    private void initTree() {
        trCategory.setRoot(new TreeItem<>(new CategoryView(
                new KategorieGrenz(0, "Alle Kategorien", null, null))));
        trCategory.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> treeSelectionUpdate(n));
    }
    
    private void initTable() {
        tcProductID.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getProduktId()));
        tcProductName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));
        tcProductCategory.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKategorieGrenz().getName()));
        tcProductCount.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getStueckzahl()));
        tcProductActivated.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().isAktiv() ? "✓" : "✕"));
        tbProduct.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> tableSelectionUpdate(n));
    }
    
    
    /*
     * Tree
     */
    
    private void buildTree(TreeItem<CategoryView> currentRoot, Set<KategorieGrenz> elementSet) {
        if (elementSet.isEmpty())
            return;
        
        currentRoot.getChildren().clear();
        List<KategorieGrenz> set = new LinkedList<>(elementSet);
        for (KategorieGrenz k : set) {
            if (currentRoot.getValue().equals(k.getParentKategorieId())) {
                elementSet.remove(k);
                currentRoot.getChildren().add(new TreeItem<>(new CategoryView(k), new ImageView(treeIcon)));
            }
        }
        currentRoot.getChildren().sort(Comparator.comparing(v -> v.getValue().category.getName()));
        currentRoot.getChildren().forEach(v -> buildTree(v, elementSet));
    }
    
    private List<TreeItem<CategoryView>> getAllChildrenOf(TreeItem<CategoryView> item) {
        List<TreeItem<CategoryView>> r = new LinkedList<>();
        if (item == null)
            return r;
        
        List<TreeItem<CategoryView>> c = item.getChildren();
        if (c.isEmpty())
            return r;
        
        r.addAll(c);
        c.forEach(v -> r.addAll(getAllChildrenOf(v)));
        return r;
    }
    
    
    /*
     * Update
     */
    
    private void reloadAll() {
        reloadCategoryTree(true);
        reloadProductTable(true);
    }
    
    private void reloadCategoryTree(boolean reloadFromDB) {
        if (reloadFromDB) {
            showProgress("Kategorien werden aus Datenbank geladen...");
            LOGGER.fine("Reloading from DB");
            categoryCache.clear();
            kategorieSteuerung.getKategorien().forEach(v -> categoryCache.put(v.getKategorieId(), v));
        }
        
        showProgress("Kategoriebaum wird erstellt...");
        buildTree(trCategory.getRoot(), new HashSet<>(categoryCache.values()));
        trCategory.refresh();
        clearStatus();
        
        treeSelectionUpdate(trCategory.getSelectionModel().getSelectedItem());
    }
    
    private void reloadProductTable(boolean reloadFromDB) {
        if (reloadFromDB) {
            showProgress("Produkte werden aus Datenbank geladen...");
            LOGGER.fine("Reloading from DB");
            productCache.clear();
            produktSteuerung.getProdukte().forEach(v -> productCache.put(v.getProduktId(), v));
        }
        
        showProgress("Produkttabelle wird erstellt...");
        
        tbProduct.getItems().clear();
        
        // add all products which satisfy the category condition
        if (showSubcategories) {
            if (selectedCategory == null)
                tbProduct.getItems().addAll(productCache.values());
            else {
                Set<Integer> childCategories = getAllChildrenOf(selectedCategory)
                        .stream().map(v -> v.getValue().category.getKategorieId()).collect(Collectors.toSet());
                childCategories.add(selectedCategory.getValue().category.getKategorieId());
                productCache.values().forEach(v -> {
                    if (childCategories.contains(v.getKategorieGrenz().getKategorieId()))
                        tbProduct.getItems().add(v);
                });
            }
        } else {
            if (selectedCategory != null) {
                productCache.values().forEach(v -> {
                    if (selectedCategory.getValue().equals(v.getKategorieGrenz().getKategorieId()))
                        tbProduct.getItems().add(v);
                });
            }
        }
        
        // apply other table filters
        tableFilters.forEach(v -> tbProduct.getItems().removeIf(v.negate()));
        clearStatus();
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
     * Callbacks
     */
    
    private void unresolvedCallback(WindowEvent e) {
        Stage dialog = (Stage) e.getSource();
        AdminApp.getInstance().closeStage(dialog);
        if (dialogCallbacks.remove(dialog) != null)
            LOGGER.info("DialogCallback(ProductViewController): Unsatisfied return callback");
    }
    
    /**
     * Callback signature: {@code callback(ProduktGrenz product)}
     */
    private void addProductCallback(DialogEvent e) {
        ProduktGrenz p = e.get("product", ProduktGrenz.class);
        if (p == null) {
            showError("Produkt hinzufügen fehlgeschlagen!");
            return;
        }
        
        if (!produktSteuerung.addProdukt(p)) {
            LOGGER.warning("Failed to add product to database!");
            showError("Produkt konnte nicht in Datenbank eingefügt werden!");
        } else {
            reloadProductTable(true);
            showInformation("Produkt wurde erfolgreich in die Datenbank eingefügt.");
        }
    }
    
    /**
     * Callback signature: {@code callback(ProduktGrenz product)}
     */
    private void editProductCallback(DialogEvent e) {
        ProduktGrenz p = e.get("product", ProduktGrenz.class);
        if (p == null) {
            showError("Produktbearbeitung fehlgeschlagen!");
            return;
        }
        
        if (!produktSteuerung.updateProdukt(p)) {
            LOGGER.warning("Failed to update product in database!");
            showError("Produkt konnte nicht bearbeitet werden!");
        } else {
            reloadAll();
            showInformation("Produkt wurde erfolgreich bearbeitet.");
        }
    }
    
    /**
     * Callback signature: {@code callback(int count, boolean activate)}
     */
    private void activateByCountCallback(DialogEvent e) {
        Integer count = e.get("count", Integer.class);
        Boolean activate = e.get("activate", Boolean.class);
        if (count == null || activate == null) {
            showError("Aktivierung/Deaktivierung fehlgeschlagen");
            return;
        }
        
        if (activate) {
            if (!produktSteuerung.aktiviereProdukte(count)) {
                LOGGER.warning("Failed to activate products!");
                showError("Produkte konnten nicht aktiviert werden!");
            } else {
                reloadProductTable(true);
                showInformation("Produkte wurden erfolgreich aktiviert.");
            }
        } else {
            if (!produktSteuerung.deaktiviereProdukte(count)) {
                LOGGER.warning("Failed to deactivate products!");
                showError("Produkte konnten nicht deaktiviert werden!");
            } else {
                reloadProductTable(true);
                showInformation("Produkte wurden erfolgreich deaktiviert.");
            }
        }
    }
    
    /**
     * Callback signature: {@code callback(TableColumn&lt;?, ?&gt; attribute, String value)}
     */
    private void searchCallback(DialogEvent e) {
        TableColumn<?, ?> c = e.get("attribute", TableColumn.class);
        String value = e.get("value", String.class);
        
        Predicate<ProduktGrenz> search;
        if (c == tcProductID)
            search = v -> v.getProduktId().equals(Integer.parseInt(value));
        else if (c == tcProductName)
            search = v -> v.getName().equals(value);
        else if (c == tcProductCategory)
            search = v -> v.getKategorieGrenz().getName().equals(value);
        else if (c == tcProductCount)
            search = v -> Integer.valueOf(value).equals(v.getStueckzahl());
        else {
            LOGGER.warning("DialogCallback: Bad value on attribute 'attribute'");
            showError("Suche fehlgeschlagen!");
            return;
        }
        
        Optional<ProduktGrenz> result = tbProduct.getItems().stream().filter(search).findFirst();
        if (result.isPresent())
            tbProduct.getSelectionModel().select(result.get());
        else
            showInformation("Produkt wurde nicht in der Tabelle gefunden.");
    }
    
    /**
     * Callback signature: {@code callback(Predicate&lt;ProduktGrenz&gt; filter)}
     */
    private void filterCallback(DialogEvent e) {
        try {
            Predicate<?> filter = e.get("filter", Predicate.class);
            if (filter == null)
                showError("Tabellenfilter hinzufügen fehlgeschlagen!");
            else {
                //noinspection CastCanBeRemovedNarrowingVariableType,unchecked
                tableFilters.add(new TableFilter<>((Predicate<ProduktGrenz>) filter));
                reloadProductTable(false);
                miRemoveFilters.setDisable(false);
            }
        } catch (ClassCastException ex) {
            LOGGER.warning("DialogCallback: Bad generic type on attribute 'filter'");
            showError("Tabellenfilter hinzufügen fehlgeschlagen!");
        }
    }
    
    /**
     * Callback signature: {@code callback(KategorieGrenz category)}
     */
    private void addCategoryCallback(DialogEvent e) {
        KategorieGrenz k = e.get("category", KategorieGrenz.class);
        if (k == null) {
            showError("Kategorie hinzufügen fehlgeschlagen!");
            return;
        }
        
        if (!kategorieSteuerung.addKategorie(k)) {
            LOGGER.warning("Failed to add category to database!");
            showError("Kategorie konnte nicht in Datenbank eingefügt werden!");
        } else {
            reloadCategoryTree(true);
            showInformation("Kategorie wurde erfolgreich in die Datenbank eingefügt.");
        }
    }
    
    /**
     * Callback signature: {@code callback(KategorieGrenz category)}
     */
    private void editCategoryCallback(DialogEvent e) {
        KategorieGrenz k = e.get("category", KategorieGrenz.class);
        if (k == null) {
            showError("Kategoriebearbeitung fehlgeschlagen!");
            return;
        }
        
        if (!kategorieSteuerung.updateKategorie(k)) {
            LOGGER.warning("Failed to update category in database!");
            showError("Kategorie konnte nicht bearbeitet werden!");
        } else {
            reloadAll();
            showInformation("Kategorie wurde erfolgreich bearbeitet.");
        }
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
    
    private void treeSelectionUpdate(TreeItem<CategoryView> c) {
        if (c == null || c.getParent() == null) {
            selectedCategory = null;
            categoryContext.setDisabled(true);
        } else {
            selectedCategory = c;
            categoryContext.setDisabled(false);
        }
        
        tableSelectionUpdate(null);
        reloadProductTable(false);
    }
    
    private void tableSelectionUpdate(ProduktGrenz p) {
        selectedProduct = p;
        if (p != null) {
            if (p.isAktiv())
                miActivateProduct.setText("Produkt deaktivieren");
            else
                miActivateProduct.setText("Produkt aktivieren");
            productContext.setDisabled(false);
        } else
            productContext.setDisabled(true);
    }
    
    
    @FXML
    private void reload() {
        reloadAll();
        showInformation("Daten wurden neu geladen.");
    }
    
    @FXML
    private void openMainMenu() {
        AdminApp.getInstance().showMainMenu();
    }
    
    @FXML
    private void closeWindow() {
        AdminApp.getInstance().getProductStage().close();
        AdminApp.getInstance().showMainMenu();
    }
    
    @FXML
    private void exit() {
        if (agreed(showYesNoDialog(null, "Möchten Sie das Programm beenden?", "Programm Beenden",
                                   "SWP - WAWI - Admin: Programm Beenden")))
            AdminApp.getInstance().exit();
    }
    
    @FXML
    private void addProduct() {
        // create dialog stage, pass currently selected category as parameter
        Stage dialog;
        Parameter c = new Parameter("categoryControl", kategorieSteuerung);
        Parameter l = new Parameter("warehouseControl", lagerSteuerung);
        Parameter s = null;
        if (selectedCategory != null)
            s = new Parameter("selectedCategory", selectedCategory.getValue().category);
        dialog = AdminApp.getInstance().createStage(stage, "/dialogs/product.fxml", c, l, s);
        dialog.setTitle("SWP - WAWI - Admin: Produkt Hinzufügen");
        dialogCallbacks.put(dialog, this::addProductCallback);
        dialog.setOnCloseRequest(this::unresolvedCallback);
        dialog.show();
    }
    
    @FXML
    private void editProduct() {
        if (selectedProduct != null) {
            Parameter c = new Parameter("categoryControl", kategorieSteuerung);
            Parameter l = new Parameter("warehouseControl", lagerSteuerung);
            Parameter p = new Parameter("product", selectedProduct);
            Stage dialog = AdminApp.getInstance().createStage(stage, "/dialogs/product.fxml", c, l, p);
            dialog.setTitle("SWP - WAWI - Admin: Produkt Bearbeiten");
            dialogCallbacks.put(dialog, this::editProductCallback);
            dialog.setOnCloseRequest(this::unresolvedCallback);
            dialog.show();
        }
    }
    
    @FXML
    private void switchProductState() {
        if (selectedProduct != null) {
            selectedProduct.setAktiv(!selectedProduct.isAktiv());
            if (produktSteuerung.updateProdukt(selectedProduct)) {
                tbProduct.refresh();
                tableSelectionUpdate(selectedProduct);
                showInformation("Produkt erfolgreich aktualisiert");
            } else
                showError("Produktaktualisierung fehlgeschlagen!");
        }
    }
    
    @FXML
    private void removeProduct() {
        if (selectedProduct != null) {
            if (agreed(showAlert(AlertType.CONFIRMATION, null,
                                 String.format("%d: %s", selectedProduct.getProduktId(), selectedProduct.getName()),
                                 "Möchten Sie das Produkt entfernen?",
                                 "SWP - WAWI - Admin: Produkt entfernen", REMOVE, CANCEL))) {
                List<BestellungspositionGrenz> lbg = new LinkedList<>();
                List<LieferpositionGrenz> llg = new LinkedList<>();
                bestellungSteuerung.getBestellungen().forEach(v -> lbg.addAll(v.getBestellungspositionGrenzList()));
                einlieferungSteuerung.getEinlieferungen().forEach(v -> llg.addAll(v.getLieferpositionenGrenz()));
                if (lbg.stream().anyMatch(v -> v.getProduktGrenz().getProduktId().equals(selectedProduct.getProduktId())) ||
                    llg.stream().anyMatch(v -> v.getProduktGrenz().getProduktId().equals(selectedProduct.getProduktId()))) {
                    selectedProduct.setAktiv(false);
                    if (!produktSteuerung.updateProdukt(selectedProduct)) {
                        LOGGER.warning("Failed to deactivate product!");
                        showError("Produkt konnte nicht deaktiviert werden!");
                    } else {
                        reloadProductTable(false);
                        showWarning("Produkt wurde bereits bestellt und kann daher nicht entfernt werden, " +
                                    "statt dessen wird das Produkt deaktiviert");
                    }
                } else {
                    if (produktSteuerung.deleteProdukt(selectedProduct.getProduktId())) {
                        productCache.remove(selectedProduct.getProduktId());
                        selectedProduct = null;
                        reloadProductTable(false);
                        showInformation("Produkt wurde entfernt.");
                    } else
                        showError("Produkt konnte nicht entfernt werden!");
                }
            }
        }
    }
    
    @FXML
    private void activateProductsByCount() {
        Parameter a = new Parameter("activate", true);
        Stage dialog = AdminApp.getInstance().createStage(stage, "/dialogs/activateByCount.fxml", a);
        dialog.setTitle("SWP - WAWI - Admin: Produkte Aktivieren");
        dialogCallbacks.put(dialog, this::activateByCountCallback);
        dialog.setOnCloseRequest(this::unresolvedCallback);
        dialog.show();
    }
    
    @FXML
    private void deactivateProductsByCount() {
        Parameter a = new Parameter("activate", false);
        Stage dialog = AdminApp.getInstance().createStage(stage, "/dialogs/activateByCount.fxml", a);
        dialog.setTitle("SWP - WAWI - Admin: Produkte Deaktivieren");
        dialogCallbacks.put(dialog, this::activateByCountCallback);
        dialog.setOnCloseRequest(this::unresolvedCallback);
        dialog.show();
    }
    
    @FXML
    private void searchProduct() {
        Parameter a = new Parameter("attributes", List.of(tcProductID, tcProductName, tcProductCategory, tcProductCount));
        Stage dialog = AdminApp.getInstance().createStage(stage, "/dialogs/search.fxml", a);
        dialog.setTitle("SWP - WAWI - Admin: Produkt Suchen");
        dialogCallbacks.put(dialog, this::searchCallback);
        dialog.setOnCloseRequest(this::unresolvedCallback);
        dialog.show();
    }
    
    @FXML
    private void showSubcategories() {
        showSubcategories = miShowSubcategories.isSelected();
        reloadProductTable(false);
    }
    
    @FXML
    private void filterByProductID() {
        Parameter a = new Parameter("attribute", tcProductID.getText());
        Parameter t = new Parameter("decimal", false);
        Parameter k = new Parameter("keyExtractor", (NumberKeyExtractor) p -> ((ProduktGrenz) p).getProduktId());
        Stage dialog = AdminApp.getInstance().createStage(stage, "/dialogs/filterNumber.fxml", a, t, k);
        dialog.setTitle("SWP - WAWI - Admin: Nach Produkt-ID filtern");
        dialogCallbacks.put(dialog, this::filterCallback);
        dialog.setOnCloseRequest(this::unresolvedCallback);
        dialog.show();
    }
    
    @FXML
    private void filterByProductName() {
        Parameter a = new Parameter("attribute", tcProductName.getText());
        Parameter k = new Parameter("keyExtractor", (StringKeyExtractor) p -> ((ProduktGrenz) p).getName());
        Stage dialog = AdminApp.getInstance().createStage(stage, "/dialogs/filterString.fxml", a, k);
        dialog.setTitle("SWP - WAWI - Admin: Nach Produktnamen filtern");
        dialogCallbacks.put(dialog, this::filterCallback);
        dialog.setOnCloseRequest(this::unresolvedCallback);
        dialog.show();
    }
    
    @FXML
    private void filterByCount() {
        Parameter a = new Parameter("attribute", tcProductCount.getText());
        Parameter t = new Parameter("decimal", false);
        Parameter k = new Parameter("keyExtractor", (NumberKeyExtractor) p -> ((ProduktGrenz) p).getStueckzahl());
        Stage dialog = AdminApp.getInstance().createStage(stage, "/dialogs/filterNumber.fxml", a, t, k);
        dialog.setTitle("SWP - WAWI - Admin: Nach Stückzahl filtern");
        dialogCallbacks.put(dialog, this::filterCallback);
        dialog.setOnCloseRequest(this::unresolvedCallback);
        dialog.show();
    }
    
    @FXML
    private void filterByActivationState() {
        Parameter a = new Parameter("attribute", tcProductActivated.getText());
        Parameter k = new Parameter("keyExtractor", (BooleanKeyExtractor) p -> ((ProduktGrenz) p).isAktiv());
        Stage dialog = AdminApp.getInstance().createStage(stage, "/dialogs/filterBoolean.fxml", a, k);
        dialog.setTitle("SWP - WAWI - Admin: Nach Aktivierungsstatus filtern");
        dialogCallbacks.put(dialog, this::filterCallback);
        dialog.setOnCloseRequest(this::unresolvedCallback);
        dialog.show();
    }
    
    @FXML
    private void removeFilters() {
        tableFilters.clear();
        miRemoveFilters.setDisable(true);
        reloadProductTable(false);
    }
    
    @FXML
    private void addCategory() {
        System.out.println(kategorieSteuerung.getKategorien().stream().map(KategorieGrenz::toString).collect(Collectors.joining("\n")));
        Stage dialog;
        Parameter c = new Parameter("categoryControl", kategorieSteuerung), s = null;
        if (selectedCategory != null)
            s = new Parameter("parent", selectedCategory.getValue().category);
        dialog = AdminApp.getInstance().createStage(stage, "/dialogs/category.fxml", c, s);
        dialog.setTitle("SWP - WAWI - Admin: Kategorie Hinzufügen");
        dialogCallbacks.put(dialog, this::addCategoryCallback);
        dialog.setOnCloseRequest(this::unresolvedCallback);
        dialog.show();
    }
    
    @FXML
    private void editCategory() {
        if (selectedCategory != null) {
            Parameter c = new Parameter("categoryControl", kategorieSteuerung);
            Parameter s = new Parameter("selectedCategory", selectedCategory.getValue().category);
            Stage dialog = AdminApp.getInstance().createStage(stage, "/dialogs/category.fxml", c, s);
            dialog.setTitle("SWP - WAWI - Admin: Kategorie Bearbeiten");
            dialogCallbacks.put(dialog, this::editCategoryCallback);
            dialog.setOnCloseRequest(this::unresolvedCallback);
            dialog.show();
        }
    }
    
    @FXML
    private void removeCategory() {
        if (selectedCategory != null) {
            // Test if the selected category has any associated products
            if (productCache.values().stream().anyMatch(v -> selectedCategory.getValue().equals(v.getKategorieGrenz()))) {
                showWarning("Kategorie enthält Produkte und kann daher nicht entfernt werden!");
                return;
            }
            
            final KategorieGrenz kategorie = selectedCategory.getValue().category();
            
            if (!agreed(showAlert(AlertType.CONFIRMATION, null,
                                  String.format("%d: %s", kategorie.getKategorieId(), kategorie.getName()),
                                  "Möchten Sie die ausgewählte Kategorie entfernen?",
                                  "SWP - WAWI - Admin: Kategorie entfernen", REMOVE, CANCEL)))
                return;
            
            // Update parent id of all subcategories of the selected category
            List<TreeItem<CategoryView>> l = new LinkedList<>(selectedCategory.getChildren());
            for (TreeItem<CategoryView> c : l) {
                KategorieGrenz k = c.getValue().category;
                k.setParentKategorieId(kategorie.getParentKategorieId());
                if (!kategorieSteuerung.updateKategorie(k)) {
                    // Attempt to repair database (old categories that were already updated)
                    LOGGER.severe("Failed to update category in database! Database might be inconsistent, attempting to repair...");
                    k.setParentKategorieId(kategorie.getKategorieId());
                    for (TreeItem<CategoryView> c2 : l) {
                        if (c == c2) {
                            LOGGER.info("Database successfully repaired");
                            break;
                        }
                        KategorieGrenz k2 = c2.getValue().category;
                        k2.setParentKategorieId(kategorie.getKategorieId());
                        if (!kategorieSteuerung.updateKategorie(k2)) {
                            LOGGER.severe("Failed to repair!");
                            break;
                        }
                    }
                    showError("Kategorie entfernen fehlgeschlagen!");
                    return;
                }
            }
            
            // Remove selected category
            if (!kategorieSteuerung.deleteKategorie(kategorie.getKategorieId())) {
                LOGGER.warning("Failed to remove category from database!");
                showError("Kategorie entfernen fehlgeschlagen!");
            } else {
                reloadCategoryTree(true);
                showInformation("Kategorie wurde aus Datenbank entfernt");
            }
        }
    }
    
    
}
