package de.thkoeln.swp.wawi.wawidbmodel.entities;

import java.io.Serializable;
import java.time.LocalDate;
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
@Table(name = "lagerverkehr")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Lagerverkehr.findAll", query = "SELECT l FROM Lagerverkehr l"),
    @NamedQuery(name = "Lagerverkehr.findByCreated", query = "SELECT l FROM Lagerverkehr l WHERE l.created = :created"),
    @NamedQuery(name = "Lagerverkehr.findByStatus", query = "SELECT l FROM Lagerverkehr l WHERE l.status = :status")
})
public class Lagerverkehr implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "lgvid")
    private Integer lgvid;
    @Basic(optional = false)
    @Column(name = "created")
    private LocalDate created;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @JoinColumn(name = "einlieferung", referencedColumnName = "elfid")
    @ManyToOne
    private Einlieferung einlieferung;
    @JoinColumn(name = "bestellung", referencedColumnName = "bid")
    @ManyToOne
    private Bestellung bestellung;
    @JoinColumn(name = "lager", referencedColumnName = "lagerid")
    @ManyToOne(optional = false)
    private Lager lager;

    public Lagerverkehr()
    {
    }

    public Lagerverkehr(Integer lgvid)
    {
        this.lgvid = lgvid;
    }

    public Lagerverkehr(Integer lgvid, LocalDate created, String status)
    {
        this.lgvid = lgvid;
        this.created = created;
        this.status = status;
    }

    public Integer getLgvid()
    {
        return lgvid;
    }

    public void setLgvid(Integer lgvid)
    {
        this.lgvid = lgvid;
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

    public Einlieferung getEinlieferung()
    {
        return einlieferung;
    }

    public void setEinlieferung(Einlieferung einlieferung)
    {
        this.einlieferung = einlieferung;
    }

    public Bestellung getBestellung()
    {
        return bestellung;
    }

    public void setBestellung(Bestellung bestellung)
    {
        this.bestellung = bestellung;
    }

    public Lager getLager()
    {
        return lager;
    }

    public void setLager(Lager lager)
    {
        this.lager = lager;
    }

    @Override
    public int hashCode()
    {
        return (lgvid == null) ? 0 : lgvid.hashCode();
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lagerverkehr))
            return false;
        Lagerverkehr other = (Lagerverkehr) object;
        if ((this.lgvid == null && other.lgvid != null) || (this.lgvid != null && !this.lgvid.equals(other.lgvid)))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "wawi.datenhaltung.wawidbmodel.entities.Lagerverkehr[ lgvid=" + lgvid + " ]";
    }
    
}
