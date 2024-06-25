package de.thkoeln.swp.wawi.admingui.util;


import java.time.LocalDate;


public interface DateKeyExtractor {
    
    LocalDate extract(Object o);
    
}
