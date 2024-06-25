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
@Table(name = "lager")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Lager.findAll", query = "SELECT l FROM Lager l")
})
public class Lager implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "lagerid")
    private Integer lagerid;
    @Basic(optional = false)
    @Lob
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Lob
    @Column(name = "addresse")
    private String addresse;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lager")
    private List<Lagerort> lagerortList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lager")
    private List<Lagerverkehr> lagerverkehrList;

    public Lager()
    {
    }

    public Lager(Integer lagerid)
    {
        this.lagerid = lagerid;
    }

    public Lager(Integer lagerid, String name, String addresse)
    {
        this.lagerid = lagerid;
        this.name = name;
        this.addresse = addresse;
    }

    public Integer getLagerid()
    {
        return lagerid;
    }

    public void setLagerid(Integer lagerid)
    {
        this.lagerid = lagerid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddresse()
    {
        return addresse;
    }

    public void setAddresse(String addresse)
    {
        this.addresse = addresse;
    }

    @XmlTransient
    public List<Lagerort> getLagerortList()
    {
        return lagerortList;
    }

    public void setLagerortList(List<Lagerort> lagerortList)
    {
        this.lagerortList = lagerortList;
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

    @Override
    public int hashCode()
    {
        return (lagerid == null) ? 0 : lagerid.hashCode();
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lager))
            return false;
        Lager other = (Lager) object;
        if ((this.lagerid == null && other.lagerid != null) || (this.lagerid != null && !this.lagerid.equals(other.lagerid)))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "wawi.datenhaltung.wawidbmodel.entities.Lager[ lagerid=" + lagerid + " ]";
    }
    
}
