/*****************************************************************
 * Autor: Leonel Nguimatsia                                      *
 * @version: IntelliJ2021 JDK17                                  *
 * Hochschule: TH-Kloen                                          *
 * Ort: Deutz Technische Hochschule                              *
 * Webmail: leonel.nguimatsia_tsobguim@smail.th-koeln.de         *
 * Beschreibung: EntitaetKlasse ICRUDLieferantImpl               *
 *                                                               *
 *****************************************************************/

package de.thkoeln.swp.wawi.lagerdaten;

import de.thkoeln.swp.wawi.datenhaltungapi.ICRUDLieferant;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Lieferant;
import de.thkoeln.swp.wawi.wawidbmodel.exceptions.NoEntityManagerException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ICRUDLieferantImpl implements ICRUDLieferant {

    // Deklaration eines Attribut Logger für den DatenBankzugriff
    private static final Logger LOGGER = Logger.getLogger(ICRUDLieferantImpl.class.getName());

    // Deklaration der Attribut EntityManager für die DatenBank
    private EntityManager em;

    /**
     * Methode setEntityManager() mit Parameter:
     * @param entityManager
     */
    @Override
    public void setEntityManager(EntityManager entityManager) {

        this.em = entityManager;
    }

    /**
     * Getter Methode getLieferantById() der Typ Lieferant mit Parameter:
     * @param i
     * @return lieferantId
     */
    @Override
    public Lieferant getLieferantById(int i) {

        if(em == null){

            throw new NoEntityManagerException();
        }

        Lieferant lieferantId = em.find(Lieferant.class,i);

        return lieferantId;

    }

    /**
     * Getter Methode getLieferantenListe() der Typ List<Lieferant>
     * @return LieferantList
     */
    @Override
    public List<Lieferant> getLieferantenListe() {

        if(em == null){

            throw new NoEntityManagerException();
        }

        List<Lieferant> lieferantList = em.createNamedQuery("Lieferant.findAll",Lieferant.class).getResultList();

        return lieferantList;

    }

    /**
     * Methode insertLieferant() der Typ Boolean und mit Parameter:
     * @param lieferant
     * @return true, ob ein Objekt in die DatenBank erfolgreich hinzugefuegt wird, Ansonsten False
     */
    @Override
    public boolean insertLieferant(Lieferant lieferant) {

        if( em == null){

            throw new NoEntityManagerException();
        }

        if( lieferant == null || lieferant.getLfid() != null ){

            return false;
        }
        
        try{
            
            em.persist(lieferant);
            
        } catch (PersistenceException pe){

            LOGGER.log(Level.ALL,"insertLieferant",pe);
            pe.printStackTrace();
            return false;
        }

        return true;
        
    }

    /**
     * Methode updateLieferantder() Typ Boolean und mit Parameter:
     * @param lieferant
     * @return True, ob ein Update auf einem Objekt in die DatenBank erfolgreich gemacht wird, Ansonsten False
     */
    @Override
    public boolean updateLieferant(Lieferant lieferant) {

        if(em == null){

            throw new NoEntityManagerException();
        }

        if(lieferant == null){

            return  false;
        }

        Lieferant lieferantToUpdate = getLieferantById(lieferant.getLfid());

        if( lieferantToUpdate == null){

            return false;
        }

        em.merge(lieferant);

        return true;
    }

    /**
     * Methode deleteLieferant() der Typ boolean mit Parameter:
     * @param i
     * @return True, ob ein Objekt in die DantenBank erfolgreich geloescht wird, Ansonsten False
     */
    @Override
    public boolean deleteLieferant(int i) {

        if(em == null){

            throw new NoEntityManagerException();
        }

        Lieferant lieferantToDelete = getLieferantById(i);

        if(lieferantToDelete == null){

            return false;
        }

        em.remove(lieferantToDelete);

        return true;
    }
}
