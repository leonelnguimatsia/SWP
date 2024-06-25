package de.thkoeln.swp.wawi.wawidbmodel.entities;

import java.io.Serializable;
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
@Table(name = "lagerort")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Lagerort.findAll", query = "SELECT l FROM Lagerort l"),
    @NamedQuery(name = "Lagerort.findByKapazitaet", query = "SELECT l FROM Lagerort l WHERE l.kapazitaet = :kapazitaet")
})
public class Lagerort implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "lgortid")
    private Integer lgortid;
    @Basic(optional = false)
    @Lob
    @Column(name = "bezeichnung")
    private String bezeichnung;
    @Basic(optional = false)
    @Column(name = "kapazitaet")
    private int kapazitaet;
    @JoinColumn(name = "lager", referencedColumnName = "lagerid")
    @ManyToOne(optional = false)
    private Lager lager;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lagerort")
    private List<Produkt> produktList;

    public Lagerort()
    {
    }

    public Lagerort(Integer lgortid)
    {
        this.lgortid = lgortid;
    }

    public Lagerort(Integer lgortid, String bezeichnung, int kapazitaet)
    {
        this.lgortid = lgortid;
        this.bezeichnung = bezeichnung;
        this.kapazitaet = kapazitaet;
    }

    public Integer getLgortid()
    {
        return lgortid;
    }

    public void setLgortid(Integer lgortid)
    {
        this.lgortid = lgortid;
    }

    public String getBezeichnung()
    {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung)
    {
        this.bezeichnung = bezeichnung;
    }

    public int getKapazitaet()
    {
        return kapazitaet;
    }

    public void setKapazitaet(int kapazitaet)
    {
        this.kapazitaet = kapazitaet;
    }

    public Lager getLager()
    {
        return lager;
    }

    public void setLager(Lager lager)
    {
        this.lager = lager;
    }

    @XmlTransient
    public List<Produkt> getProduktList()
    {
        return produktList;
    }

    public void setProduktList(List<Produkt> produktList)
    {
        this.produktList = produktList;
    }

    @Override
    public int hashCode()
    {
        return (lgortid == null) ? 0 : lgortid.hashCode();
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lagerort))
            return false;
        Lagerort other = (Lagerort) object;
        if ((this.lgortid == null && other.lgortid != null) || (this.lgortid != null && !this.lgortid.equals(other.lgortid)))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "wawi.datenhaltung.wawidbmodel.entities.Lagerort[ lgortid=" + lgortid + " ]";
    }
    
}
