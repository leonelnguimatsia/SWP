/*****************************************************************
 * Autor: Leonel Nguimatsia                                      *
 * @version: IntelliJ2021 JDK17                                  *
 * Hochschule: TH-Kloen                                          *
 * Ort: Deutz Technische Hochschule                              *
 * Webmail: leonel.nguimatsia_tsobguim@smail.th-koeln.de         *
 * Beschreibung: EntitaetKlasse ICRUDLieferantImplTest           *
 *                                                               *
 *****************************************************************/

package de.thkoeln.swp.wawi.lagerdaten;

import de.thkoeln.swp.wawi.wawidbmodel.entities.*;
import de.thkoeln.swp.wawi.wawidbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.wawi.wawidbmodel.services.IDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;


import java.util.List;

import static org.junit.Assert.*;


public class ICRUDLieferantImplTest {

    // Definition eines Objekts der Klasse ICRUDLieferantImpl fuer die Unit-Test
    private ICRUDLieferantImpl classUnderTest;

    // Deklaration der Attribut EntityManager für die DatenBank
    private EntityManager em;


    /**
     * @Before: angenommen()
     * ANGENOMMEN der EntityManager wird korrekt geholt,
     * UND die Implementierung der ICRUDLieferant Schnittstelle wird als classUnderTest instanziiert,
     * UND der EntityManager wird per setEntityManager Methode der classUnderTest gesetzt,
     * UND die Transaktion von em wird gestartet,
     * UND die Daten der betreffenden Entitäten wurden in der DB gelöscht
     */
    @Before
    public void angenommen(){

        IDatabase db = new IDatabaseImpl();
        db.useDevPU();
        em = db.getEntityManager();

        classUnderTest = new ICRUDLieferantImpl();
        classUnderTest.setEntityManager(em);
        em.getTransaction().begin();

        //Alle ggf. vorhandene Lieferanten Loeschen, dazu die Abhaengigkeiten beachten
        // Einlieferung verweis auf Lieferant, Lagerverkehr und LieferPosition verweisen auf Einlieferung
        Query deleteQuery = em.createQuery("DELETE FROM Lagerverkehr lv");
        deleteQuery.executeUpdate();

        deleteQuery = em.createQuery("DELETE FROM Lieferposition lp");
        deleteQuery.executeUpdate();

        deleteQuery = em.createQuery("DELETE FROM Einlieferung el");
        deleteQuery.executeUpdate();

        deleteQuery = em.createQuery("DELETE FROM Lieferant l");
        deleteQuery.executeUpdate();

    }

    /**
     * @After: amEnde()
     * AM ENDE wird die Transaktion zurück gesetzt
     */
    @After
    public void amEnde(){

        em.getTransaction().rollback();
    }

    /**
     * @Test getLieferantById_00()
     * WENN ein Testlieferant bereits in der DB existiert,
     * UND die Methode getLieferantById mit der Id des Testlieferanten aufgerufen wird,
     * DANN sollte sie den Testlieferanten zurückliefern.
     */
    @Test
    public void getLieferantById_00(){

        // Benötigte Objekt fuer Lieferant erzeugen
        Lieferant testLieferanten = new Lieferant(null,"Nguimatsia","Leonel","015257412365","leonel147gmail.com",true);
        em.persist(testLieferanten);
        int lieferantById = testLieferanten.getLfid();

        assertEquals(testLieferanten,classUnderTest.getLieferantById(lieferantById));
    }

    /**
     * @Test getLieferantById_01()
     * WENN ein Testlieferant nicht in der DB existiert,
     * UND die Methode getLieferantById mit der Id des Testlieferanten aufgerufen wird,
     * DANN sollte sie NULL zurückliefern.
     */
    @Test
    public void  getLieferantById_01(){

        // nach @before sollten keine Lieferanten mehr in der DB sein

        Lieferant testLieferanten = new Lieferant(null,"Tchoffo","Yannick","015257012315","yannick171gmail.com",true);
        em.persist(testLieferanten);
        em.remove(testLieferanten);
        int lieferantById = testLieferanten.getLfid();

        //Ueberprueft, ob die testLieferant NULL zurueckliefert
        assertNull(classUnderTest.getLieferantById(lieferantById));
    }

    /**
     * @Test getLieferantenListe_00()
     * WENN x (x>0) Lieferanten in der DB existieren,
     * UND die Methode getLieferantenListe aufgerufen wird,
     * DANN sollte sie eine Liste mit x Lieferanten zurückliefern.
     */
    @Test
    public void  getLieferantenListe_00(){

        // drei TestLieferanten anlegen und sicherstellen, dass sie in die DB geschrieben wurden
        Lieferant lieferanten = new Lieferant(null,"Takam","Joel","015251412065","joel224gmail.com",true);
        em.persist(lieferanten);

        lieferanten = new Lieferant(null,"Fokou","David","015251412331","david243gmail.com",true);
        em.persist(lieferanten);

        lieferanten = new Lieferant(null,"Kenfack","Audrey","015251412725","audrey237gmail.com",true);
        em.persist(lieferanten);

        List<Lieferant> lieferantList = classUnderTest.getLieferantenListe();

        //Ueberprueft, ob die testLieferant eine Liste mit 3 Lieferanten zurueckliefert
        assertEquals(3,lieferantList.size());

    }

    /**
     * @Test getLieferantenListe_01()
     * WENN keine Lieferanten in der DB existieren,
     * UND die Methode getLieferantenListe aufgerufen wird,
     * DANN sollte sie eine leere Liste zurückliefern.
     */
    @Test
    public void getLieferantenListe_01(){

        // nach @before sollten keine Lieferanten in der DB sein
        List<Lieferant> lieferantList = classUnderTest.getLieferantenListe();

        //Ueberprueft, ob die Liste der testLieferant leer ist
        assertTrue(lieferantList.isEmpty());

    }

    /**
     *  Test insertLieferant_00()
     * WENN die Methode insertLieferant mit einem Testlieferanten aufgerufen wird,
     * UND die ID des Testlieferanten gleich null ist,
     * DANN sollte sie TRUE zurückliefern,
     * UND der Testlieferant sollte in der DB existieren
     */
    @Test
    public void insertLieferant_00(){

        Lieferant testLieferanten = new Lieferant(null,"Schmitz","Franck","015257412365","franck49gmail.com",true);

        //Ueberprueft, ob die testLieferant TRUE zurückliefert
        assertTrue(classUnderTest.insertLieferant(testLieferanten));

        Integer insertLieferantId = testLieferanten.getLfid();
        //Ueberprueft, ob die testLieferant in der DB existiert
        assertEquals("Schmitz",em.find(Lieferant.class,insertLieferantId).getName());
        assertEquals("Franck",em.find(Lieferant.class,insertLieferantId).getVorname());
        assertEquals("015257412365",em.find(Lieferant.class,insertLieferantId).getTelefon());
        assertEquals("franck49gmail.com",em.find(Lieferant.class,insertLieferantId).getEmail());
        assertEquals(true,em.find(Lieferant.class,insertLieferantId).getAktiv());

    }

    /**
     *  @Test insertLieferant_01()
     * WENN die Methode insertLieferant mit einem Testlieferanten aufgerufen wird,
     * UND die ID des Testlieferanten ungleich null ist,
     * DANN sollte sie FALSE zurückliefern,
     * UND die DB wurde nicht verändert.
     */
    @Test
    public void insertLieferant_01(){

        //nach @before sollten keine Lieferanten in der DB sein
        Lieferant testLieferanten = new Lieferant(237,"Konate","Mohammed","015257412001","mohammed749gmail.com",true);
        Integer insertLieferantId = testLieferanten.getLfid();

        //Ueberprueft, ob die testLieferant FALSE zurückliefert
        assertFalse(classUnderTest.insertLieferant(testLieferanten));

        //Ueberprueft, ob die testLieferant in DB nicht verändert wurde
        assertNull(em.find(Lieferant.class,insertLieferantId));
    }

    /**
     * @Test updateLieferant_00()
     * WENN ein Testlieferant in der DB existiert,
     * UND die Methode updateLieferant mit einem veränderten Testlieferanten (aber gleicher ID)
     * aufgerufen wird,
     * DANN sollte sie TRUE zurückliefern,
     * UND der Testlieferant sollte in der DB verändert sein.
     */
    @Test
    public void updateLieferant_00(){

        //Testlieferant initialisieren und sicherstellen, dass sie in die DB geschrieben wurde
        Lieferant testLieferanten = new Lieferant(null,"Traore","Christian","015257412554","christian009gmail.com",true);
        em.persist(testLieferanten);
        Integer updateLieferantId = testLieferanten.getLfid();

        testLieferanten.setName("Moni");
        testLieferanten.setVorname("Idrissa");
        testLieferanten.setTelefon("015257412311");
        testLieferanten.setEmail("idrissa852@gmail.com");
        testLieferanten.setAktiv(true);

        //Ueberpruefen ob updateLieferant ausgefuehrt wurde
        assertTrue(classUnderTest.updateLieferant(testLieferanten));

        //Ueberpruefen, ob die Attribute des Lieferanten in der DB geändert wurde.

        assertEquals("Moni",em.find(Lieferant.class,updateLieferantId).getName());
        assertEquals("Idrissa",em.find(Lieferant.class,updateLieferantId).getVorname());
        assertEquals("015257412311",em.find(Lieferant.class,updateLieferantId).getTelefon());
        assertEquals("idrissa852@gmail.com",em.find(Lieferant.class,updateLieferantId).getEmail());
        assertEquals(true,em.find(Lieferant.class,updateLieferantId).getAktiv());

    }

    /**
     * @Test updateLieferant_01()
     * WENN ein Testlieferant nicht in der DB existiert,
     * UND die Methode updateLieferant mit dem Testlieferanten aufgerufen wird,
     * DANN sollte sie FALSE zurückliefern,
     * UND der Testlieferant sollte nicht in der DB existieren.
     */
    @Test
    public void updateLieferant_01(){

        // nach @before sollten keine Lieferanten in der DB vorhanden sein
        Lieferant testLieferanten = new Lieferant(null,"Traore","Christian","015257412554","christian009gmail.com",true);
        em.persist(testLieferanten);
        em.remove(testLieferanten);
        Integer updateLiefrantId = testLieferanten.getLfid();

        // Überprüft, ob der TestLieferant False zurückliefert
        assertFalse(classUnderTest.updateLieferant(testLieferanten));

        // Überprüft, ob der TestLieferant nicht mehr in der DB existieren
        assertNull(em.find(Lieferant.class,updateLiefrantId));

    }

    /**
     * @Test deleteLieferant_00()
     * WENN ein Testlieferant in der DB existiert,
     * UND die Methode deleteLieferant mit der ID des Testlieferanten aufgerufen wird,
     * DANN sollte sie TRUE zurückliefern,
     * UND der Testlieferant sollte nicht mehr in der DB existieren
     */
    @Test
    public void deleteLieferant_00(){

        // Testlieferant  initialisieren und in die DB schreiben
        Lieferant testLieferanten = new Lieferant(null,"Traore","Christian","015257412554","christian009gmail.com",true);
        em.persist(testLieferanten);

        Integer deleteLieferantId = testLieferanten.getLfid();

        // Überprüft, ob der TestLieferant True zurückliefert
        assertTrue(classUnderTest.deleteLieferant(deleteLieferantId));

        // Überprüft, ob der TestLieferant nicht mehr in der DB existieren
        assertNull(em.find(Lieferant.class,deleteLieferantId));
    }

    /**
     * @Test deleteLieferant_01()
     * WENN ein Testlieferant nicht in der DB existiert,
     * UND die Methode deleteLieferant mit der ID des Testlieferanten aufgerufen wird,
     * DANN sollte sie FALSE zurückliefern
     */
    @Test
    public void deleteLieferant_01(){

        // Nach @before sollten keine Lieferanten in der DB vorhanden sein

        Lieferant testLieferant = new Lieferant(null,"Adama","Louis","0152574120011","louis117gmail.com",true);
        em.persist(testLieferant);
        em.remove(testLieferant);

        Integer deleteLieferantId = testLieferant.getLfid();

        //Ueberprueft, ob die testLieferant FALSE zurueckliefert
        assertFalse(classUnderTest.deleteLieferant(deleteLieferantId));

    }

}
