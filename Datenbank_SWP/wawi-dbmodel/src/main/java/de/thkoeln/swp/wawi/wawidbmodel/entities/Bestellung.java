package de.thkoeln.swp.wawi.wawidbmodel.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "bestellung")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Bestellung.findAll", query = "SELECT b FROM Bestellung b"),
    @NamedQuery(name = "Bestellung.findByCreated", query = "SELECT b FROM Bestellung b WHERE b.created = :created"),
    @NamedQuery(name = "Bestellung.findByStatus", query = "SELECT b FROM Bestellung b WHERE b.status = :status"),
    @NamedQuery(name = "Bestellung.findByGesamtnetto", query = "SELECT b FROM Bestellung b WHERE b.gesamtnetto = :gesamtnetto"),
    @NamedQuery(name = "Bestellung.findByGesamtbrutto", query = "SELECT b FROM Bestellung b WHERE b.gesamtbrutto = :gesamtbrutto")
})
public class Bestellung implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "bid")
    private Integer bid;
    @Basic(optional = false)
    @Lob
    @Column(name = "lieferadresse")
    private String lieferadresse;
    @Basic(optional = false)
    @Lob
    @Column(name = "rechnungsadresse")
    private String rechnungsadresse;
    @Basic(optional = false)
    @Column(name = "created")
    private LocalDate created;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "gesamtnetto")
    private BigDecimal gesamtnetto;
    @Basic(optional = false)
    @Column(name = "gesamtbrutto")
    private BigDecimal gesamtbrutto;
    @OneToMany(mappedBy = "bestellung")
    private List<Lagerverkehr> lagerverkehrList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bestellung")
    private List<Bestellungsposition> bestellungspositionList;
    @JoinColumn(name = "kunde", referencedColumnName = "kid")
    @ManyToOne(optional = false)
    private Kunde kunde;

    public Bestellung()
    {
    }

    public Bestellung(Integer bid)
    {
        this.bid = bid;
    }

    public Bestellung(Integer bid, String lieferadresse, String rechnungsadresse, LocalDate created, String status, BigDecimal gesamtnetto, BigDecimal gesamtbrutto)
    {
        this.bid = bid;
        this.lieferadresse = lieferadresse;
        this.rechnungsadresse = rechnungsadresse;
        this.created = created;
        this.status = status;
        this.gesamtnetto = gesamtnetto;
        this.gesamtbrutto = gesamtbrutto;
    }

    public Integer getBid()
    {
        return bid;
    }

    public void setBid(Integer bid)
    {
        this.bid = bid;
    }

    public String getLieferadresse()
    {
        return lieferadresse;
    }

    public void setLieferadresse(String lieferadresse)
    {
        this.lieferadresse = lieferadresse;
    }

    public String getRechnungsadresse()
    {
        return rechnungsadresse;
    }

    public void setRechnungsadresse(String rechnungsadresse)
    {
        this.rechnungsadresse = rechnungsadresse;
    }

    public LocalDate getCreated()
    {
        return created;
    }

    public void setCreated(LocalDate created)
    {
        this.created = created;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public BigDecimal getGesamtnetto()
    {
        return gesamtnetto;
    }

    public void setGesamtnetto(BigDecimal gesamtnetto)
    {
        this.gesamtnetto = gesamtnetto;
    }

    public BigDecimal getGesamtbrutto()
    {
        return gesamtbrutto;
    }

    public void setGesamtbrutto(BigDecimal gesamtbrutto)
    {
        this.gesamtbrutto = gesamtbrutto;
    }

    @XmlTransient
    public List<Lagerverkehr> getLagerverkehrList()
    {
        return lagerverkehrList;
    }

    public void setLagerverkehrList(List<Lagerverkehr> lagerverkehrList)
    {
        this.lagerverkehrList = lagerverkehrList;
    }

    @XmlTransient
    public List<Bestellungsposition> getBestellungspositionList()
    {
        return bestellungspositionList;
    }

    public void setBestellungspositionList(List<Bestellungsposition> bestellungspositionList)
    {
        this.bestellungspositionList = bestellungspositionList;
    }

    public Kunde getKunde()
    {
        return kunde;
    }

    public void setKunde(Kunde kunde)
    {
        this.kunde = kunde;
    }

    @Override
    public int hashCode()
    {
        return (bid == null) ? 0 : bid.hashCode();
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bestellung))
            return false;
        Bestellung other = (Bestellung) object;
        if ((this.bid == null && other.bid != null) || (this.bid != null && !this.bid.equals(other.bid)))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "wawi.datenhaltung.wawidbmodel.entities.Bestellung[ bid=" + bid + " ]";
    }
    
}
