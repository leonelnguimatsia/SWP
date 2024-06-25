package de.thkoeln.swp.wawi.wawidbmodel.entities;

import java.io.Serializable;
import java.time.LocalDate;
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
@Table(name = "kunde")
@XmlRootElement
@NamedQueries(
{
    @NamedQuery(name = "Kunde.findAll", query = "SELECT k FROM Kunde k"),
    @NamedQuery(name = "Kunde.findByCreated", query = "SELECT k FROM Kunde k WHERE k.created = :created")
})
public class Kunde implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "kid")
    private Integer kid;
    @Basic(optional = false)
    @Lob
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Lob
    @Column(name = "vorname")
    private String vorname;
    @Basic(optional = false)
    @Lob
    @Column(name = "adresse")
    private String adresse;
    @Basic(optional = false)
    @Column(name = "created")
    private LocalDate created;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "kunde")
    private List<Nachricht> nachrichtList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "kunde")
    private List<Bestellung> bestellungList;

    public Kunde()
    {
    }

    public Kunde(Integer kid)
    {
        this.kid = kid;
    }

    public Kunde(Integer kid, String name, String vorname, String adresse, LocalDate created)
    {
        this.kid = kid;
        this.name = name;
        this.vorname = vorname;
        this.adresse = adresse;
        this.created = created;
    }

    public Integer getKid()
    {
        return kid;
    }

    public void setKid(Integer kid)
    {
        this.kid = kid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVorname()
    {
        return vorname;
    }

    public void setVorname(String vorname)
    {
        this.vorname = vorname;
    }

    public String getAdresse()
    {
        return adresse;
    }

    public void setAdresse(String adresse)
    {
        this.adresse = adresse;
    }

    public LocalDate getCreated()
    {
        return created;
    }

    public void setCreated(LocalDate created)
    {
        this.created = created;
    }

    @XmlTransient
    public List<Nachricht> getNachrichtList()
    {
        return nachrichtList;
    }

    public void setNachrichtList(List<Nachricht> nachrichtList)
    {
        this.nachrichtList = nachrichtList;
    }

    @XmlTransient
    public List<Bestellung> getBestellungList()
    {
        return bestellungList;
    }

    public void setBestellungList(List<Bestellung> bestellungList)
    {
        this.bestellungList = bestellungList;
    }

    @Override
    public int hashCode()
    {
        return (kid == null) ? 0 : kid.hashCode();
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Kunde))
            return false;
        Kunde other = (Kunde) object;
        if ((this.kid == null && other.kid != null) || (this.kid != null && !this.kid.equals(other.kid)))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "wawi.datenhaltung.wawidbmodel.entities.Kunde[ kid=" + kid + " ]";
    }
    
}
