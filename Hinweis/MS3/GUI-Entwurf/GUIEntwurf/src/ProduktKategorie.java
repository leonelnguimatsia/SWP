import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.*;


public class ProduktKategorie {
    
    
    public static class Product {
        
        private final SimpleStringProperty id, name, category, n, activated;
        
        public Product(String id, String name, String category, String n, String activated) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.category = new SimpleStringProperty(category);
            this.n = new SimpleStringProperty(n);
            this.activated = new SimpleStringProperty(activated);
        }
    
        public String getId() {
            return id.get();
        }
    
        public void setId(String id) {
            this.id.set(id);
        }
    
        public String getName() {
            return name.get();
        }
    
        public void setName(String name) {
            this.name.set(name);
        }
    
        public String getCategory() {
            return category.get();
        }
        
        public void setCategory(String category) {
            this.category.set(category);
        }
    
        public String getN() {
            return n.get();
        }
    
        public void setN(String n) {
            this.n.set(n);
        }
    
        public String getActivated() {
            return activated.get();
        }
    
        public void setActivated(String activated) {
            this.activated.set(activated);
        }
    
    }
    
    
    @FXML
    public TreeView<String> treeCategory;
    
    @FXML
    public TableView<Product> tbProducts;
   
    @FXML
    public void initialize() throws IOException {
        Image icon = new Image(new URL("file:res/treeCategoryIcon.png").openStream());
        
        TreeItem<String> root = new TreeItem<>("Alle Kategorien");
        root.getChildren().add(new TreeItem<>("Kategorie 1", new ImageView(icon)));
        TreeItem<String> k2 = new TreeItem<>("Kategorie 2", new ImageView(icon));
        k2.getChildren().add(new TreeItem<>("Kategorie 2.1", new ImageView(icon)));
        root.getChildren().add(k2);
        root.getChildren().add(new TreeItem<>("Kategorie 3", new ImageView(icon)));
        
        root.setExpanded(true);
        treeCategory.setRoot(root);
        
        tbProducts.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tbProducts.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tbProducts.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("category"));
        tbProducts.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("n"));
        tbProducts.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("activated"));
        
        tbProducts.getColumns().get(0).setComparator(Comparator.comparingInt(v -> Integer.parseInt(v.toString())));
        tbProducts.getColumns().get(3).setComparator(Comparator.comparingInt(v -> Integer.parseInt(v.toString())));
        
        tbProducts.getItems().addAll(generateProducts(100));
    }
    
    
    private List<Product> generateProducts(int amount) {
        String[] categories = {"Kategorie 1", "Kategorie 2", "Kategorie 2.1", "Kategorie 3"};
        String[] activated = {"✓", "✕"};
        Random r = new Random();
        
        List<Product> products = new LinkedList<>();
        for (int i = 0; i < amount; i++) {
            products.add(new Product(
                    String.valueOf(i + 1),
                    "Beispiel " + (char) ('A' + r.nextInt(26)) + (char) ('A' + r.nextInt(26)),
                    categories[r.nextInt(categories.length)],
                    String.valueOf(r.nextInt(10000)),
                    activated[r.nextInt(activated.length)]
            ));
        }
        return products;
    }
    
    
}
