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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "kategorie")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Kategorie.findAll", query = "SELECT k FROM Kategorie k"),
    @NamedQuery(name = "Kategorie.findByParentkatid", query = "SELECT k FROM Kategorie k WHERE k.parentkatid = :parentkatid")
})
public class Kategorie implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "katid")
    private Integer katid;
    @Basic(optional = false)
    @Column(name = "parentkatid")
    private int parentkatid;
    @Basic(optional = false)
    @Lob
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Lob
    @Column(name = "beschreibung")
    private String beschreibung;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "kategorie")
    private List<Produkt> produktList;

    public Kategorie()
    {
    }

    public Kategorie(Integer katid)
    {
        this.katid = katid;
    }

    public Kategorie(Integer katid, int parentkatid, String name, String beschreibung)
    {
        this.katid = katid;
        this.parentkatid = parentkatid;
        this.name = name;
        this.beschreibung = beschreibung;
    }

    public Integer getKatid()
    {
        return katid;
    }

    public void setKatid(Integer katid)
    {
        this.katid = katid;
    }

    public int getParentkatid()
    {
        return parentkatid;
    }

    public void setParentkatid(int parentkatid)
    {
        this.parentkatid = parentkatid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getBeschreibung()
    {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung)
    {
        this.beschreibung = beschreibung;
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
        return (katid == null) ? 0 : katid.hashCode();
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Kategorie))
            return false;
        Kategorie other = (Kategorie) object;
        if ((this.katid == null && other.katid != null) || (this.katid != null && !this.katid.equals(other.katid)))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "wawi.datenhaltung.wawidbmodel.entities.Kategorie[ katid=" + katid + " ]";
    }
    
}
