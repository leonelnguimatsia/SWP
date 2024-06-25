package de.thkoeln.swp.wawi.adminsteuerung.impl;


import de.thkoeln.swp.wawi.datenhaltungapi.*;
import de.thkoeln.swp.wawi.steuerungapi.admin.*;
import de.thkoeln.swp.wawi.steuerungapi.grenz.*;
import de.thkoeln.swp.wawi.wawidbmodel.impl.IDatabaseImpl;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;


/**
 * Implementation of the Interface {@link ILagerService}
 *
 * @author Fabian Ullmann (WAWI4-Admin)
 * @since 16.06.2022
 */
public class LagerSteuerungImpl implements ILagerSteuerung {
    
    
    private static final Logger LOGGER = Logger.getLogger(LagerSteuerungImpl.class.getName());
    
    private final ILagerService lagerService;
    
    
    /**
     * The required implementation of {@link ILagerService} is queried with {@link ServiceLoader#load(Class)},
     * the first implementation found by the {@link ServiceLoader} will always be used. <br/>
     * {@link IDatabaseImpl#getEntityManager()} is used to retrieve the required {@link javax.persistence.EntityManager}
     *
     * @throws RuntimeException if no implementations for {@link ILagerService} are available.
     */
    public LagerSteuerungImpl() {
        lagerService = ServiceLoader.load(ILagerService.class).findFirst()
                                    .orElseThrow(() -> new RuntimeException("Missing implementation: ILagerService!"));
        lagerService.setEntityManager(new IDatabaseImpl().getEntityManager());
    }
    
    
    /**
     * Retrieves all warehouse traffic stored in the database and maps them into their corresponding boundary objects.
     *
     * @return A {@link List} containing the resulting boundary objects
     */
    @Override
    public List<LagerverkehrGrenz> getLagerverkehre() {
        return lagerService.getGesamtLagerverkehr().stream().map(EntityGrenzConverter::toGrenz).toList();
    }
    
    /**
     * Retrieves all warehouse traffic in the specified timeframe from the database
     *
     * @param from the beginning of the timeframe
     * @param to   the end of the timeframe
     * @return A {@link List} containing the resulting boundary objects
     */
    @Override
    public List<LagerverkehrGrenz> getLagerverkehrVonBisDatum(LocalDate from, LocalDate to) {
        return lagerService.getLagerverkehrByDatum(from, to).stream()
                           .map(EntityGrenzConverter::toGrenz).toList();
    }
    
    /**
     * Retrieves all warehouse locations stored in the database and maps them into their corresponding boundary objects.
     *
     * @return A {@link List} containing the resulting boundary objects
     */
    @Override
    public List<LagerortGrenz> getLagerorte() {
        return lagerService.getLagerortListe().stream().map(EntityGrenzConverter::toGrenz).toList();
    }
    
    
}
