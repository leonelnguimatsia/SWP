package de.thkoeln.swp.wawi.wawidbmodel.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "lieferposition")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Lieferposition.findAll", query = "SELECT l FROM Lieferposition l"),
    @NamedQuery(name = "Lieferposition.findByAnzahl", query = "SELECT l FROM Lieferposition l WHERE l.anzahl = :anzahl"),
    @NamedQuery(name = "Lieferposition.findByKaufpreis", query = "SELECT l FROM Lieferposition l WHERE l.kaufpreis = :kaufpreis")
})
public class Lieferposition implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "lpid")
    private Integer lpid;
    @Basic(optional = false)
    @Column(name = "anzahl")
    private int anzahl;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "kaufpreis")
    private BigDecimal kaufpreis;
    @JoinColumn(name = "einlieferung", referencedColumnName = "elfid")
    @ManyToOne(optional = false)
    private Einlieferung einlieferung;
    @JoinColumn(name = "produkt", referencedColumnName = "prodid")
    @ManyToOne(optional = false)
    private Produkt produkt;

    public Lieferposition()
    {
    }

    public Lieferposition(Integer lpid)
    {
        this.lpid = lpid;
    }

    public Lieferposition(Integer lpid, int anzahl, BigDecimal kaufpreis)
    {
        this.lpid = lpid;
        this.anzahl = anzahl;
        this.kaufpreis = kaufpreis;
    }

    public Integer getLpid()
    {
        return lpid;
    }

    public void setLpid(Integer lpid)
    {
        this.lpid = lpid;
    }

    public int getAnzahl()
    {
        return anzahl;
    }

    public void setAnzahl(int anzahl)
    {
        this.anzahl = anzahl;
    }

    public BigDecimal getKaufpreis()
    {
        return kaufpreis;
    }

    public void setKaufpreis(BigDecimal kaufpreis)
    {
        this.kaufpreis = kaufpreis;
    }

    public Einlieferung getEinlieferung()
    {
        return einlieferung;
    }

    public void setEinlieferung(Einlieferung einlieferung)
    {
        this.einlieferung = einlieferung;
    }

    public Produkt getProdukt()
    {
        return produkt;
    }

    public void setProdukt(Produkt produkt)
    {
        this.produkt = produkt;
    }

    @Override
    public int hashCode()
    {
        return (lpid == null) ? 0 : lpid.hashCode();
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lieferposition))
            return false;
        Lieferposition other = (Lieferposition) object;
        if ((this.lpid == null && other.lpid != null) || (this.lpid != null && !this.lpid.equals(other.lpid)))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "wawi.datenhaltung.wawidbmodel.entities.Lieferposition[ lpid=" + lpid + " ]";
    }
    
}
