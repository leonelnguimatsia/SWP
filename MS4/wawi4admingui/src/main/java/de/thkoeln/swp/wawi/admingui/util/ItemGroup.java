package de.thkoeln.swp.wawi.admingui.util;


import javafx.scene.Node;
import javafx.scene.control.MenuItem;

import java.util.*;
import java.util.function.Consumer;


public record ItemGroup(List<Node> nodes, List<MenuItem> items) {
    
    
    public void setDisabled(boolean disabled) {
        nodes.forEach(v -> v.setDisable(disabled));
        items.forEach(v -> v.setDisable(disabled));
    }
    
    public void forEach(Consumer<Node> nodeAction, Consumer<MenuItem> itemAction) {
        nodes.forEach(nodeAction);
        items.forEach(itemAction);
    }
    
    
    public static ItemGroup of(Object... members) {
        List<Node> ln = new LinkedList<>();
        List<MenuItem> lm = new LinkedList<>();
        for (Object o : members) {
            if (o instanceof Node n)
                ln.add(n);
            else if (o instanceof MenuItem m)
                lm.add(m);
            else if (o instanceof ItemGroup g) {
                ln.addAll(g.nodes);
                lm.addAll(g.items);
            }
            else
                throw new IllegalArgumentException("Group members must either be a Node, a MenuItem or another ItemGroup!");
        }
        
        return new ItemGroup(Collections.unmodifiableList(ln), Collections.unmodifiableList(lm));
    }
    
    
}
