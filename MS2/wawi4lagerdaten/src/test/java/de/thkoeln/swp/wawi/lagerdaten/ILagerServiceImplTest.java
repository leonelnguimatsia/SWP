package de.thkoeln.swp.wawi.lagerdaten;


import de.thkoeln.swp.wawi.lagerdaten.ILagerServiceImpl;
import de.thkoeln.swp.wawi.wawidbmodel.entities.*;
import de.thkoeln.swp.wawi.wawidbmodel.impl.IDatabaseImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;


public class ILagerServiceImplTest {
    private ILagerServiceImpl classUnderTest;
    private EntityManager em;

    /*@Before: angenommen()
    ANGENOMMEN der EntityManager wird korrekt geholt,
    UND die Implementierung der ILagerService Schnittstelle wird als classUnderTest instanziiert,
    UND der EntityManager wird per setEntityManager Methode der classUnderTest gesetzt,
    UND die Transaktion von em wird gestartet,
    UND die Daten der betreffenden Entitäten wurden in der DB gelöscht.*/
    @Before
    public void angenommen() {

        classUnderTest = new ILagerServiceImpl();
        IDatabaseImpl db = new IDatabaseImpl();
        db.useDevPU();
        em = db.getEntityManager();

        classUnderTest.setEntityManager(em);
        em.getTransaction().begin();
        em.createQuery("delete from Lieferposition ").executeUpdate();
        em.createQuery("delete from Lagerverkehr ").executeUpdate();
        em.createQuery("delete from Einlieferung ").executeUpdate();
        em.createQuery("delete from Bestellungsposition ").executeUpdate();
        em.createQuery("delete from Bestellung ").executeUpdate();
        em.createQuery("delete from Nachricht ").executeUpdate();
        em.createQuery("delete from Kunde ").executeUpdate();
        em.createQuery("delete from Produkt ").executeUpdate();
        em.createQuery("delete from Lagerort ").executeUpdate();
    }

    /*@After: amEnde()
    AM ENDE wird die Transaktion zurück gesetzt.*/
    @After
    public void amEnde()
    {
        em.getTransaction().rollback();
    }

    /*@Test getAlleEinlieferungen_00()
    WENN x (x>0) Einlieferungen in der DB existieren,
    UND die Methode getAlleEinlieferungen aufgerufen wird,
    DANN sollte sie eine Liste mit x Einlieferungen zurückliefern.*/
    @Test
    public void getAlleEinlieferungen_00() {

        Einlieferung einlieferung = new Einlieferung(null,LocalDate.now(),BigDecimal.ONE) ;
        Lieferant lieferant = new Lieferant(null,"Sonne","Karl","12121023","Karl@mail",true);
        em.persist(lieferant);
        einlieferung.setLieferant(lieferant);
        em.persist(einlieferung);

        Einlieferung einlieferung2 = new Einlieferung(null,LocalDate.now(),BigDecimal.TEN) ;
        Lieferant lieferant1 = new Lieferant(null,"Mond","Anton","48869392","Anton@mail",true);
        em.persist(lieferant1);
        einlieferung2.setLieferant(lieferant1);
        em.persist(einlieferung2);

        int anzahlergebnis = 2;

       List<Einlieferung> ergebnis = classUnderTest.getAlleEinlieferungen();

        assertEquals(anzahlergebnis,ergebnis.size());
    }

    /*@Test getAlleEinlieferungen_01()
    WENN keine Einlieferungen in der DB existieren,
    UND die Methode getAlleEinlieferungen aufgerufen wird,
    DANN sollte sie eine leere Liste zurückliefern.*/
    @Test
    public void getAlleEinlieferungen_01(){

        List<Einlieferung> ergebnis = classUnderTest.getAlleEinlieferungen();

        assertTrue(ergebnis.isEmpty());
    }

    /*@Test getEinlieferungenByDatum_00()
    WENN x (x>0) Einlieferungen in der DB existieren,
    UND y (y<x) Einlieferungen in der DB existieren im Zeitraum von a bis b,
    UND die Methode getEinlieferungenByDatum mit Zeitraum von a bis b aufgerufen wird,
    DANN sollte sie eine Liste mit y Einlieferungen zurückliefern.*/
    @Test
    public void getEinlieferungenByDatum_00(){

        Einlieferung einlieferung1 = new Einlieferung(null,LocalDate.of(2022,1,5),BigDecimal.ONE);
        Lieferant lieferant1 = new Lieferant(null,"Sonne","Karl","12121023","Karl@mail",true);
        em.persist(lieferant1);
        einlieferung1.setLieferant(lieferant1);
        em.persist(einlieferung1);

        Einlieferung einlieferung2 = new Einlieferung(null,LocalDate.of(2022,2,5),BigDecimal.ONE);
        Lieferant lieferant2 = new Lieferant(null,"Mond","Anton","48869392","Anton@mail",true);
        em.persist(lieferant2);
        einlieferung2.setLieferant(lieferant2);
        em.persist(einlieferung2);

        int anzahlergebnis = 2;

        List<Einlieferung> ergebnis = classUnderTest.getEinlieferungenByDatum(LocalDate.of(2022,1,1),LocalDate.of(2022,3,1));

        assertEquals(anzahlergebnis,ergebnis.size());
    }

    /*@Test getEinlieferungenByDatum_01()
    WENN x (x>0) Einlieferungen in der DB existieren,
    UND keine Einlieferungen in der DB existieren im Zeitraum von a bis b,
    UND die Methode getAlleEinlieferungen mit Zeitraum von a bis b aufgerufen wird,
    DANN sollte sie eine leere Liste zurückliefern.*/
    @Test
    public void getEinlieferungenByDatum_01(){

        Einlieferung einlieferung = new Einlieferung(null,LocalDate.of(2001,1,1),BigDecimal.ONE);
        Lieferant lieferant1 = new Lieferant(null,"Mond","Anton","48869392","Anton@mail",true);
        em.persist(lieferant1);
        einlieferung.setLieferant(lieferant1);
        em.persist(einlieferung);

        List<Einlieferung> ergebnis = classUnderTest.getEinlieferungenByDatum(LocalDate.of(2001,2,23),LocalDate.of(2001,3,1));

        assertTrue(ergebnis.isEmpty());
    }

    /*@Test getLagerverkehrByDatum_00()
    WENN x (x>0) Lagerverkehre in der DB existieren,
    UND y (y<x) Lagerverkehre in der DB existieren im Zeitraum von a bis b,
    UND die Methode getLagerverkehrByDatum mit Zeitraum von a bis b aufgerufen wird,
    DANN sollte sie eine Liste mit y Lagerverkehren zurückliefern.*/
    @Test
    public void getLagerverkehrByDatum_00(){

        Lagerverkehr lagerverkehr = new Lagerverkehr(null,LocalDate.of(2022,1,1),"ok");
        Lager lager = new Lager(null,"Charly","lagerstr");
        em.persist(lager);
        lagerverkehr.setLager(lager);
        em.persist(lagerverkehr);

        Lagerverkehr lagerverkehr2 = new Lagerverkehr(null,LocalDate.of(2022,2,1),"ok");
        Lager lager2 = new Lager(null,"Berta","lagerusstr");
        em.persist(lager2);
        lagerverkehr2.setLager(lager2);
        em.persist(lagerverkehr2);

        int anzahlergebnis = 2;
        List<Lagerverkehr> ergebnis = classUnderTest.getLagerverkehrByDatum(LocalDate.of(2021,12,23),LocalDate.of(2022,3,1));

        assertEquals(anzahlergebnis,ergebnis.size());
    }

    /*@Test getLagerverkehrByDatum_01()
    WENN x (x>0) Lagerverkehre in der DB existieren,
    UND keine Lagerverkehre in der DB existieren im Zeitraum von a bis b,
    UND die Methode getLagerverkehrByDatum mit Zeitraum von a bis b aufgerufen wird,
    DANN sollte sie eine leere Liste zurückliefern.*/
    @Test
    public void getLagerverkehrByDatum_01(){

        Lagerverkehr lagerverkehr = new Lagerverkehr(2,LocalDate.of(2022,1,1),"ok");
        Lager lager = new Lager(null,"Huber","hubstr");
        em.persist(lager);
        lagerverkehr.setLager(lager);

        List<Lagerverkehr> ergebnis = classUnderTest.getLagerverkehrByDatum(LocalDate.of(2022,2,23),LocalDate.of(2022,3,1));

        assertTrue(ergebnis.isEmpty());

    }

    /*@Test getGesamtLagerverkehr_00()
    WENN x (x>0) Lagerverkehre in der DB existieren,
    UND die Methode getGesamtLagerverkehr aufgerufen wird,
    DANN sollte sie eine Liste mit x Lagerverkehren zurückliefern.*/
    @Test
    public void getGesamtLagerverkehr_00() {

        Lagerverkehr lagerverkehr = new Lagerverkehr(null,LocalDate.of(2022,1,1),"ok");
        Lager lager = new Lager(null,"Charly","lagerstr");
        em.persist(lager);
        lagerverkehr.setLager(lager);
        em.persist(lagerverkehr);

        Lagerverkehr lagerverkehr2 = new Lagerverkehr(null,LocalDate.of(2022,2,1),"ok");
        Lager lager2 = new Lager(null,"Berta","Bertaastr");
        em.persist(lager2);
        lagerverkehr2.setLager(lager2);
        em.persist(lagerverkehr2);

        int anzahlergebnis = 2;
        List<Lagerverkehr> ergebnis = classUnderTest.getGesamtLagerverkehr();

        assertEquals(anzahlergebnis,ergebnis.size());
    }

    /*@Test getGesamtLagerverkehr_01()
    WENN keine Lagerverkehre in der DB existieren,
    UND die Methode getGesamtLagerverkehr aufgerufen wird,
    DANN sollte sie eine leere Liste zurückliefern.*/
    @Test
    public void getGesamtLagerverkehr_01(){

        List<Lagerverkehr> ergebnis = classUnderTest.getGesamtLagerverkehr();

        assertTrue(ergebnis.isEmpty());
    }

    /*@Test getEinlieferungById_00()
    WENN eine Testeinlieferung bereits in der DB existiert,
    UND die Methode getEinlieferungById mit der Id der Testleinlieferung aufgerufen wird,
    DANN sollte sie die Testeinlieferung zurückliefern.*/
    @Test
    public void getEinlieferungById_00() {
        Einlieferung einlieferung = new Einlieferung(null,LocalDate.now(),BigDecimal.ONE);
        Lieferant lieferant = new Lieferant(null,"Stuhl","Sven","3i451212","stuhl@mail",true);
        em.persist(lieferant);
        einlieferung.setLieferant(lieferant);
        em.persist(einlieferung);

        Einlieferung ergebnis = classUnderTest.getEinlieferungById(einlieferung.getElfid());

        assertEquals(einlieferung,ergebnis);
    }

    /*@Test getEinlieferungById_01()
    WENN eine Testeinlieferung nicht in der DB existiert,
    UND die Methode getEinlieferungById mit der Id der Testeinlieferung aufgerufen wird,
    DANN sollte sie NULL zurückliefern.*/
    @Test
    public void getEinlieferungById_01(){

        Einlieferung einlieferung = new Einlieferung(2,LocalDate.now(),BigDecimal.ONE);

        Einlieferung ergebnis = classUnderTest.getEinlieferungById(2);

        assertNull(ergebnis);
   }

   /*@Test getLagerortById_00()
    WENN ein Testlagerort bereits in der DB existiert,
    UND die Methode getLagerortById mit der Id des Testlagerorts aufgerufen wird,
    DANN sollte sie den Testlagerort zurückliefern.*/
    @Test
    public void getLagerortById_00(){

        Lagerort lagerort = new Lagerort(null,"Testeinlieferung",5);
        Lager lager = new Lager(null,"lager","lagerusstr");
        em.persist(lager);
        lagerort.setLager(lager);
        em.persist(lagerort);

        Lagerort ergebnis = classUnderTest.getLagerortById(lagerort.getLgortid());

        assertEquals(lagerort,ergebnis);
}

    /*@Test getLagerortById_01()
    WENN ein Testlagerort nicht in der DB existiert,
    UND die Methode getLagerortById mit der Id des Testlagerorts aufgerufen wird,
    DANN sollte sie NULL zurückliefern.*/
    @Test
    public void getLagerortById_01(){

        Lagerort lagerort = new Lagerort(2,"Testeinlieferung",5);
        Lager lager = new Lager(null,"lager","lagerusstr");
        em.persist(lager);
        lagerort.setLager(lager);

        Lagerort ergebnis = classUnderTest.getLagerortById(2);

        assertNull(ergebnis);
    }

    /*@Test getLagerortListe_00()
    WENN x (x>0) Lagerorte in der DB existieren,
    UND die Methode getLagerortListe aufgerufen wird,
    DANN sollte sie eine Liste mit x Lagerorten zurückliefern.*/
    @Test
    public void getLagerortListe_00() {

        Lagerort lagerort = new Lagerort(null,"Testeinlieferung2",3);
        Lager lager = new Lager(null,"lagerus","strassestr");
        em.persist(lager);
        lagerort.setLager(lager);
        em.persist(lagerort);

        Lagerort lagerort2 = new Lagerort(null,"Testeinlieferung22",4);
        Lager lager2 = new Lager(null,"berta","bertstr");
        em.persist(lager2);
        lagerort2.setLager(lager2);
        em.persist(lagerort2);

        int anzahlergebnis = 2;
        List<Lagerort> ergebnis = classUnderTest.getLagerortListe();

        assertEquals(anzahlergebnis,ergebnis.size());

    }

    /*@Test getLagerortListe_01()
    WENN keine Lagerorte in der DB existieren,
    UND die Methode getLagerortListe aufgerufen wird,
    DANN sollte sie eine leere Liste zurückliefern.*/
    @Test
    public void getLagerortListe_01(){

    List<Lagerort> ergebnis = classUnderTest.getLagerortListe();

        assertTrue(ergebnis.isEmpty());
  }

}
