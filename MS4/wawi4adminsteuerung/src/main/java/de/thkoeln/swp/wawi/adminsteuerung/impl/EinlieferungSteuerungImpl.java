package de.thkoeln.swp.wawi.adminsteuerung.impl;


import de.thkoeln.swp.wawi.datenhaltungapi.ILagerService;
import de.thkoeln.swp.wawi.steuerungapi.admin.IEinlieferungSteuerung;
import de.thkoeln.swp.wawi.steuerungapi.grenz.EinlieferungGrenz;
import de.thkoeln.swp.wawi.wawidbmodel.impl.IDatabaseImpl;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;


/**
 * Implementation of the Interface {@link IEinlieferungSteuerung}
 *
 * @author Fabian Ullmann (WAWI4-Admin)
 * @since 16.06.2022
 */
public class EinlieferungSteuerungImpl implements IEinlieferungSteuerung {
    
    
    private static final Logger LOGGER = Logger.getLogger(EinlieferungSteuerungImpl.class.getName());
    
    private final ILagerService lagerService;       // static might be better
    
    
    /**
     * The required implementation of {@link ILagerService} is queried with {@link ServiceLoader#load(Class)},
     * the first implementation found by the {@link ServiceLoader} will always be used. <br/>
     * {@link IDatabaseImpl#getEntityManager()} is used to retrieve the required {@link javax.persistence.EntityManager}
     *
     * @throws RuntimeException if no implementations for {@link ILagerService} are available.
     */
    public EinlieferungSteuerungImpl() {
        lagerService = ServiceLoader.load(ILagerService.class).findFirst()
                                    .orElseThrow(() -> new RuntimeException("Missing implementation: ILagerService!"));
        lagerService.setEntityManager(new IDatabaseImpl().getEntityManager());
    }
    
    
    /**
     * Retrieves all deliveries stored in the database and maps them into their corresponding boundary objects.
     * @return A {@link List} containing the resulting boundary objects
     */
    @Override
    public List<EinlieferungGrenz> getEinlieferungen() {
        return lagerService.getAlleEinlieferungen().stream()
                           .map(EntityGrenzConverter::toGrenz).toList();
    }
    
    /**
     * Retrieves all deliveries in the specified timeframe from the database
     *
     * @param from the beginning of the timeframe
     * @param to   the end of the timeframe
     * @return A {@link List} containing the resulting boundary objects
     */
    @Override
    public List<EinlieferungGrenz> getEinlieferungenVonBisDatum(LocalDate from, LocalDate to) {
        return lagerService.getEinlieferungenByDatum(from, to).stream()
                           .map(EntityGrenzConverter::toGrenz).toList();
    }
    
    /**
     * Retrieves a delivery specified by its id, or null if there are no deliveries with this id.
     * @param id the id to search for
     * @return The delivery as boundary object or null
     */
    @Override
    public EinlieferungGrenz getEinlieferungById(int id) {
        return EntityGrenzConverter.toGrenz(lagerService.getEinlieferungById(id));
    }
    
    
}
