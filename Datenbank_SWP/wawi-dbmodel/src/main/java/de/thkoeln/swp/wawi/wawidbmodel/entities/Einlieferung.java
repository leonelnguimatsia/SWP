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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "einlieferung")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Einlieferung.findAll", query = "SELECT e FROM Einlieferung e"),
    @NamedQuery(name = "Einlieferung.findByCreated", query = "SELECT e FROM Einlieferung e WHERE e.created = :created"),
    @NamedQuery(name = "Einlieferung.findByGesamtpreis", query = "SELECT e FROM Einlieferung e WHERE e.gesamtpreis = :gesamtpreis")
})
public class Einlieferung implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "elfid")
    private Integer elfid;
    @Basic(optional = false)
    @Column(name = "created")
    private LocalDate created;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "gesamtpreis")
    private BigDecimal gesamtpreis;
    @OneToMany(mappedBy = "einlieferung")
    private List<Lagerverkehr> lagerverkehrList;
    @JoinColumn(name = "lieferant", referencedColumnName = "lfid")
    @ManyToOne(optional = false)
    private Lieferant lieferant;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "einlieferung")
    private List<Lieferposition> lieferpositionList;

    public Einlieferung()
    {
    }

    public Einlieferung(Integer elfid)
    {
        this.elfid = elfid;
    }

    public Einlieferung(Integer elfid, LocalDate created, BigDecimal gesamtpreis)
    {
        this.elfid = elfid;
        this.created = created;
        this.gesamtpreis = gesamtpreis;
    }

    public Integer getElfid()
    {
        return elfid;
    }

    public void setElfid(Integer elfid)
    {
        this.elfid = elfid;
    }

    public LocalDate getCreated()
    {
        return created;
    }

    public void setCreated(LocalDate created)
    {
        this.created = created;
    }

    public BigDecimal getGesamtpreis()
    {
        return gesamtpreis;
    }

    public void setGesamtpreis(BigDecimal gesamtpreis)
    {
        this.gesamtpreis = gesamtpreis;
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

    public Lieferant getLieferant()
    {
        return lieferant;
    }

    public void setLieferant(Lieferant lieferant)
    {
        this.lieferant = lieferant;
    }

    @XmlTransient
    public List<Lieferposition> getLieferpositionList()
    {
        return lieferpositionList;
    }

    public void setLieferpositionList(List<Lieferposition> lieferpositionList)
    {
        this.lieferpositionList = lieferpositionList;
    }

    @Override
    public int hashCode()
    {
        return (elfid == null) ? 0 : elfid.hashCode();
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Einlieferung))
            return false;
        Einlieferung other = (Einlieferung) object;
        if ((this.elfid == null && other.elfid != null) || (this.elfid != null && !this.elfid.equals(other.elfid)))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "wawi.datenhaltung.wawidbmodel.entities.Einlieferung[ elfid=" + elfid + " ]";
    }
    
}
