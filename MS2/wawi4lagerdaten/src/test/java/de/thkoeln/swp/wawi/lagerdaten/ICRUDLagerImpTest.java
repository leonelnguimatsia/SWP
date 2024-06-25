/*
Testklasse fuer die Implementierung von ICRUDLager
Autor: Cyntia Pola
Version: 0.1
Datum: 17.04.22
*/
package de.thkoeln.swp.wawi.lagerdaten;


import de.thkoeln.swp.wawi.lagerdaten.ICRUDLagerImpl;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Lager;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Lagerort;
import de.thkoeln.swp.wawi.wawidbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.wawi.wawidbmodel.services.IDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.junit.Assert.*;

public class ICRUDLagerImpTest {
    private ICRUDLagerImpl classUnderTest;
    private EntityManager em;

    /*@Before angenommen()
ANGENOMMEN der EntityManager wird korrekt geholt,
UND die Implementierung der ICRUDLager Schnittstelle wird als classUnderTest instanziiert,
UND der EntityManager wird per setEntityManager Methode der classUnderTest gesetzt,
UND die Transaktion von em wird gestartet,
UND die Daten der betreffenden Entitäten wurden in der DB gelöscht.
*/
@Before
public void angenommen() {
    IDatabase db = new IDatabaseImpl();
    db.useDevPU();
    em = db.getEntityManager();

    classUnderTest = new ICRUDLagerImpl();
    classUnderTest.setEntityManager(em);

    em.getTransaction().begin();

    // Alle Lager loeschen und Klassen mit verweisen auf Lager löschen
    Query deleteQuery = em.createQuery("DELETE FROM Lieferposition lp");
    deleteQuery.executeUpdate();
    deleteQuery = em.createQuery("DELETE FROM Bestellungsposition bp");
    deleteQuery.executeUpdate();
    deleteQuery = em.createQuery("DELETE FROM Lagerverkehr lv");
    deleteQuery.executeUpdate();
    deleteQuery = em.createQuery("DELETE FROM Produkt pr");
    deleteQuery.executeUpdate();
    deleteQuery = em.createQuery("DELETE FROM Lagerort lo");
    deleteQuery.executeUpdate();
    deleteQuery = em.createQuery("DELETE FROM Lager lg");
    deleteQuery.executeUpdate();
}

//@After AM ENDE wird die Transaktion zurück gesetzt.
@After
public void amEnde() {
    em.getTransaction().rollback();
}

/*@Test insertLager_00()
WENN die Methode insertLager mit einem Testlager aufgerufen wird,
UND die ID des Testlagers gleich null ist,
DANN sollte sie TRUE zurückliefern,
UND der Testlager sollte in der DB existieren.
*/

@Test
public void insertLager_00() {


    Lager testLager= new Lager(null, "Fokou", "Djeleng4");
    testLager.setLagerid(null);

    assertTrue(classUnderTest.insertLager(testLager));
    assertEquals(testLager,classUnderTest.getLagerById(testLager.getLagerid()));
}


/*@Test insertLager_01()
WENN die Methode insertLager mit einem Testlager aufgerufen wird,
UND die ID des Testlagers ungleich null ist,
DANN sollte sie FALSE zurückliefern,
UND die DB wurde nicht verändert.
 */

  @Test
    public void insertLager_01(){
      Lager lager= new Lager(74, "Fokou", "Djeleng4");
      lager.setLagerid(74);

      assertFalse(classUnderTest.insertLager(lager));
      int lagerId= lager.getLagerid();
      assertNull(classUnderTest.getLagerById(lagerId));
  }

/*@Test updateLager_00()
WENN ein Testlager in der DB existiert,
UND die Methode updateLager mit einem veränderten TestLager (aber gleicher ID) aufgerufen wird,
DANN sollte sie TRUE zurückliefern,
UND das Testlager sollte in der DB verändert sein.*/


    @Test
    public void updateLager_00(){
        Lager lager = new Lager(null, "Fokou", "Djeleng4");
        classUnderTest.insertLager(lager);
        Integer lagId = lager.getLagerid();

        lager.setName("ChangedTestLager");

        assertTrue(classUnderTest.updateLager(lager));


        assertEquals(lager, classUnderTest.getLagerById(lagId));
}

/*@Test updateLager_01()
WENN ein Testlager nicht in der DB existiert,
UND die Methode updateLager mit dem Testlager aufgerufen wird,
DANN sollte sie FALSE zurückliefern,
UND das Testlager sollte nicht in der DB existieren.*/
@Test
    public void updateLager_01(){
    Lager testLager = new Lager(78,"TestLagerUpadte","TestLager for Update");



assertFalse(classUnderTest.updateLager(testLager));
    assertNull(classUnderTest.getLagerById(78));
}


/* @Test  getLagerById_00()
WENN ein Testlager bereits in der DB existiert,
UND die Methode getLagerById mit der Id des Testlagers aufgerufen wird,
DANN sollte sie das Testlager zurückliefern.
* */
    @Test
    public void getLagerById_00(){
    Lager lager =new Lager(null,"Fokou", "Djeleng4");
    classUnderTest.insertLager(lager);
    int lagerid = lager.getLagerid();

    assertEquals(lager,classUnderTest.getLagerById(lagerid));

    }


    /*@Test  getLagerById_01()
WENN ein Testlager nicht in der DB existiert,
UND die Methode getLagerById mit der Id des Testlagers aufgerufen wird,
DANN sollte sie NULL zurückliefern.
*/
    @Test
    public void getLagerById_01(){
    assertNull(classUnderTest.getLagerById(78));
    }


    /*@Test getAlleLager_00()
WENN x (x>0) Lager in der DB existieren,
UND die Methode getAlleLager aufgerufen wird,
DANN sollte sie eine Liste mit x Lagern zurückliefern.
*/
    @Test
    public void getAlleLager_00(){
    Lager lagern = new Lager(null, "TestLager1","first lagern");
    classUnderTest.insertLager(lagern);

    lagern = new Lager(null,"TestLager2","second lagern") ;
    classUnderTest.insertLager(lagern);

    lagern = new Lager(null,"TestLager3","third lagern") ;
    classUnderTest.insertLager(lagern);

        List<Lager> alleLagern = classUnderTest.getAlleLager();

        assertEquals(3,alleLagern.size());
    }


    /*@Test getAlleLager_01()
WENN keine Lager in der DB existieren,
UND die Methode getAlleLager aufgerufen wird,
DANN sollte sie eine leere Liste zurückliefern.
    */
    @Test
    public void getAlleLager_01(){
    List<Lager> alleLagern = classUnderTest.getAlleLager();

    assertTrue(alleLagern.isEmpty());
    }


/*@Test getLagerortById_00()
WENN ein Testlagerort bereits in der DB existiert,
UND die Methode getLagerortById mit der Id des Testlagerorts aufgerufen wird,
DANN sollte sie den Testlagerort zurückliefern.
*/
    @Test
    public void getLagerortById_00(){
    Lager lagerForLagerort = new Lager(null,"LgOrtLager","Lageradresse");
    em.persist(lagerForLagerort);

     Lagerort testLagerort =new Lagerort(null,"Kadji", 7);
     testLagerort.setLager(lagerForLagerort);
        classUnderTest.insertLagerort(testLagerort);
        int lgortId = testLagerort.getLgortid();

        assertEquals(testLagerort,classUnderTest.getLagerortById(lgortId));

    }


    /*@Test getLagerortById_01()
WENN ein Testlagerort nicht in der DB existiert,
UND die Methode getLagerortById mit der Id des Testlagerorts aufgerufen wird,
DANN sollte sie NULL zurückliefern.
*/
    @Test
    public void getLagerortById_01(){
    Lagerort lagerForLagerort =classUnderTest.getLagerortById(45647);
    if(lagerForLagerort != null){
        classUnderTest.deleteLagerort(45647);
    }
        assertNull(classUnderTest.getLagerortById(45647));
    }



    /*@Test getLagerortListe_00()
WENN x (x>0) Lagerorte in der DB existieren,
UND die Methode getLagerortListe aufgerufen wird,
DANN sollte sie eine Liste mit x Lagerorten zurückliefern.
*/
    @Test
    public void getLagerortListe_00(){
    Lager lagerForLagerort = new Lager(null,"LgOrtLager","Lageradresse");
    em.persist(lagerForLagerort);


        Lagerort testLagerort = new Lagerort(null, "TestLagerOrt1",7);
        testLagerort.setLager(lagerForLagerort);
        classUnderTest.insertLagerort(testLagerort);

        testLagerort = new Lagerort(null,"TestLagerOrt2",7) ;
        testLagerort.setLager(lagerForLagerort);
        classUnderTest.insertLagerort(testLagerort);



        TypedQuery<Long> typedQuery = em.createQuery("SELECT count(*) FROM Lagerort",Long.class);
        long anzahlDB= typedQuery.getSingleResult();

        List<Lagerort> lagerortList  = classUnderTest.getLagerortListe();

        assertEquals(anzahlDB,lagerortList.size());

    }


    /*@Test getLagerortListe_01()
WENN keine Lagerorte in der DB existieren,
UND die Methode getLagerortListe aufgerufen wird,
DANN sollte sie eine leere Liste zurückliefern.
   */
    @Test
    public void getLagerortListe_01(){
    Query deleteQuerry = em.createQuery("DELETE FROM Lagerort lo");
    deleteQuerry.executeUpdate();



        List<Lagerort> lagerortList = classUnderTest.getLagerortListe();

        assertTrue(lagerortList.isEmpty());
    }


    /*@Test insertLagerort_00()
WENN die Methode insertLagerort mit einem Testlagerort aufgerufen wird,
UND die ID des Testlagerorts gleich null ist,
DANN sollte sie TRUE zurückliefern,
UND der Testlagerort sollte in der DB existieren.
   */
    @Test
    public void insertLagerort_00(){
    Lager lagerForLagerort = new Lager(null,"LgOrtLager","Lageradresse");
    em.persist(lagerForLagerort);

        Lagerort testLagerort= new Lagerort(null, "Kadji", 10);
        testLagerort.setLager(lagerForLagerort);

        assertTrue(classUnderTest.insertLagerort(testLagerort));
        assertEquals(testLagerort,classUnderTest.getLagerortById(testLagerort.getLgortid()));

    }

    /*@Test insertLagerort_01()
WENN die Methode insertLagerort mit einem Testlagerort aufgerufen wird,
UND die ID des Testlagerorts ungleich null ist,
DANN sollte sie FALSE zurückliefern,
UND die DB wurde nicht verändert.
*/
    @Test
    public void insertLagerort_01(){
        Lager lagerForLagerort = new Lager(null,"LgOrtLager","Lageradresse");
        em.persist(lagerForLagerort);

        Lagerort testLagerort= new Lagerort(8, "Kadji", 10);
        testLagerort.setLager(lagerForLagerort);

        assertFalse(classUnderTest.insertLagerort(testLagerort));
        assertNull(classUnderTest.getLagerortById(testLagerort.getLgortid()));
    }


    /*@Test updateLagerort_00()
WENN ein Testlagerort in der DB existiert,
UND die Methode updateLagerort mit einem veränderten Testlagerort (aber gleicher ID) aufgerufen wird,
DANN sollte sie TRUE zurückliefern,
UND der Testlagerort sollte in der DB verändert sein.
*/
    @Test
    public void updateLagerort_00(){
        Lager lagerForLagerort = new Lager(null,"LgOrtLager","Lageradresse");
        em.persist(lagerForLagerort);

        Lagerort testLagerort = new Lagerort(null,"Kadji",10);
        testLagerort.setLager(lagerForLagerort);
        classUnderTest.insertLagerort(testLagerort);

        lagerForLagerort.setName("Lageort Changed");
        

        assertTrue(classUnderTest.updateLagerort(testLagerort));

        assertEquals(testLagerort,classUnderTest.getLagerortById(testLagerort.getLgortid()));

    }

    /*@Test updateLagerort_01()
WENN ein Testlagerort nicht in der DB existiert,
UND die Methode updateLagerort mit dem Testlagerort aufgerufen wird,
DANN sollte sie FALSE zurückliefern,
UND der Testlagerort sollte nicht in der DB existieren.
    */
    @Test
    public void updateLagerort_01(){
    Lagerort testLagerort=classUnderTest.getLagerortById(8);
    if(testLagerort != null) {
        classUnderTest.deleteLagerort(8);
    }

        Lager lagerForLagerort = new Lager(null,"LgOrtLager","Lageradresse");
        em.persist(lagerForLagerort);

    testLagerort = new Lagerort(8,"Kadji", 10);
    testLagerort.setLager(lagerForLagerort);


        assertFalse(classUnderTest.updateLagerort(testLagerort));

        assertNull(classUnderTest.getLagerortById(8));
    }

    /*@Test deleteLagerort_00()
WENN ein Testlagerort in der DB existiert,
UND die Methode deleteLagerort mit der ID des Testlagerorts aufgerufen wird,
DANN sollte sie TRUE zurückliefern,
UND der Testlagerort sollte nicht mehr in der DB existieren.
*/
    @Test
    public void deleteLagerort_00(){
        Lager lagerForLagerort = new Lager(null,"LgOrtLager","Lageradresse");
        em.persist(lagerForLagerort);

        Lagerort  testLagerort =new Lagerort(null,"Kadji",10);
        testLagerort.setLager(lagerForLagerort);
        classUnderTest.insertLagerort(testLagerort);

        Integer lgortId = testLagerort.getLgortid();

        assertTrue(classUnderTest.deleteLagerort(lgortId));
        assertNull(classUnderTest.getLagerortById(lgortId));

    }

    /*@Test deleteLagerort_01()
WENN ein Testlagerort nicht in der DB existiert,
UND die Methode deleteLagerort mit der ID des Testlagerorts aufgerufen wird,
DANN sollte sie FALSE zurückliefern.
*/
    @Test
    public void deleteLagerort_01(){
        Lagerort  lagerort =new Lagerort(null,"Kadji",10);
        if(lagerort != null){
            classUnderTest.deleteLagerort(8);
        }

        assertFalse(classUnderTest.deleteLagerort(8));
    }
}
