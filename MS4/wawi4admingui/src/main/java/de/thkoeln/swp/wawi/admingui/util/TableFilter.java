package de.thkoeln.swp.wawi.admingui.util;


import java.util.function.Predicate;


public record TableFilter<T>(Predicate<T> condition) implements Predicate<T> {
    
    @Override
    public boolean test(T t) {
        return condition.test(t);
    }
    
}
