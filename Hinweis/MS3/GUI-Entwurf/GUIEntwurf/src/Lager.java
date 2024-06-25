import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.*;


public class Lager {
    
    
    public static class Delivery {
        
        private final SimpleStringProperty id, type, cost, pos;
        
        public Delivery(String id, String type, String cost, String pos) {
            this.id = new SimpleStringProperty(id);
            this.type = new SimpleStringProperty(type);
            this.cost = new SimpleStringProperty(cost);
            this.pos = new SimpleStringProperty(pos);
        }
    
        public String getId() {
            return id.get();
        }
    
        public void setId(String id) {
            this.id.set(id);
        }
    
        public String getType() {
            return type.get();
        }
    
        public void setType(String type) {
            this.type.set(type);
        }
    
        public String getCost() {
            return cost.get();
        }
        
        public void setCost(String cost) {
            this.cost.set(cost);
        }
    
        public String getPos() {
            return pos.get();
        }
    
        public void setPos(String pos) {
            this.pos.set(pos);
        }
    
    }
    
    
    @FXML
    public TableView<Delivery> tbStock;
    
    @FXML
    public void initialize() {
        tbStock.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tbStock.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("type"));
        tbStock.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("cost"));
        tbStock.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("pos"));
        
        tbStock.getColumns().get(0).setComparator(Comparator.comparingInt(v -> Integer.parseInt(v.toString())));
        tbStock.getColumns().get(2).setComparator(Comparator.comparingDouble(v -> Double.parseDouble(v.toString())));
        tbStock.getColumns().get(3).setComparator(Comparator.comparingInt(v -> Integer.parseInt(v.toString())));
        
        tbStock.getItems().addAll(generateDeliveries(100));
    }
    
    
    private List<Delivery> generateDeliveries(int amount) {
        String[] type = {"Bestellung", "Einlieferung"};
        Random r = new Random();
        Set<Integer> posBuffer = new HashSet<>();
        
        List<Delivery> deliveries = new LinkedList<>();
        for (int i = 0; i < amount; i++) {
            int pos;
            do {
                pos = r.nextInt(99999) + 1;
            } while (posBuffer.contains(pos));
            posBuffer.add(pos);
            
            deliveries.add(new Delivery(
                    String.valueOf(i + 1),
                    type[r.nextInt(type.length)],
                    String.valueOf(r.nextInt(100000) / 100.),
                    String.valueOf(pos)
            ));
        }
        return deliveries;
    }
    
}
