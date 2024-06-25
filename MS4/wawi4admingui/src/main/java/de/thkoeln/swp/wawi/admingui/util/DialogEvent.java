package de.thkoeln.swp.wawi.admingui.util;


import javafx.event.*;

import java.util.*;
import java.util.logging.Logger;


public class DialogEvent extends Event implements Iterable<Parameter> {
    
    
    private static final Logger LOGGER = Logger.getLogger(DialogEvent.class.getName());
    
    public static final EventType<DialogEvent> INIT = new EventType<>("wawi.dialogInit");
    public static final EventType<DialogEvent> RETURN = new EventType<>("wawi.dialogReturn");
    
    public final Map<String, Parameter> PARAMETERS;
    public final Object SOURCE;
    
    
    public DialogEvent(boolean init, Object source, Parameter... params) {
        super(init ? INIT : RETURN);
        this.source = source;
        SOURCE = source;
        
        Map<String, Parameter> p = new HashMap<>();
        for (Parameter pr : params) {
            if (pr != null)
                p.put(pr.name(), pr);
        }
        PARAMETERS = Collections.unmodifiableMap(p);
    }
    
    public DialogEvent(boolean init, Object source, Collection<Parameter> params) {
        super(init ? INIT : RETURN);
        this.source = source;
        SOURCE = source;
        
        Map<String, Parameter> p = new HashMap<>();
        for (Parameter pr : params) {
            if (pr != null)
                p.put(pr.name(), pr);
        }
        PARAMETERS = Collections.unmodifiableMap(p);
    }
    
    
    public boolean contains(String key) {
        return PARAMETERS.containsKey(key);
    }
    
    public Object get(String key) {
        Parameter p = PARAMETERS.get(key);
        if (p == null)
            return null;
        return p.value();
    }
    
    public <T> T get(String key, Class<T> clazz) {
        Object o = get(key);
        if (!clazz.isInstance(o)) {
            LOGGER.warning(String.format("Bad type on parameter '%s', expected %s, got %s!", key,
                                         clazz.getName(), o == null ? "null" : o.getClass().getName()));
            return null;
        }
        return clazz.cast(o);
    }
    
    
    @Override
    public Iterator<Parameter> iterator() {
        return PARAMETERS.values().iterator();
    }
    
}
