/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "sede")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sede.findAll", query = "SELECT s FROM Sede s"),
    @NamedQuery(name = "Sede.findById", query = "SELECT s FROM Sede s WHERE s.id = :id"),
    @NamedQuery(name = "Sede.findByDescripcion", query = "SELECT s FROM Sede s WHERE s.descripcion = :descripcion")})

public class Sede implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "sede")
    private Collection<Articulos> articulosCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "sede")
    private Collection<EntradasArticulo> entradasArticulosCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "sede")
    private Collection<PedidosArticulo> pedidosArticulosCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "sede")
    private Collection<SalidasArticulo> salidaArticuloCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "sede")
    private Collection<Inventarios> inventariosCollection;

    public Sede() {
    }

    public Sede(Integer id) {
        this.id = id;
    }

    public Sede(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public Collection<Articulos> getArticulosCollection() {
        return articulosCollection;
    }

    public void setArticulosCollection(Collection<Articulos> articulosCollection) {
        this.articulosCollection = articulosCollection;
    }

    @XmlTransient
    public Collection<EntradasArticulo> getEntradasArticulosCollection() {
        return entradasArticulosCollection;
    }

    public void setEntradasArticulosCollection(Collection<EntradasArticulo> entradasArticulosCollection) {
        this.entradasArticulosCollection = entradasArticulosCollection;
    }

    @XmlTransient
    public Collection<PedidosArticulo> getPedidosArticulosCollection() {
        return pedidosArticulosCollection;
    }

    public void setPedidosArticulosCollection(Collection<PedidosArticulo> pedidosArticulosCollection) {
        this.pedidosArticulosCollection = pedidosArticulosCollection;
    }

    @XmlTransient
    public Collection<SalidasArticulo> getSalidaArticuloCollection() {
        return salidaArticuloCollection;
    }

    public void setSalidaArticuloCollection(Collection<SalidasArticulo> salidaArticuloCollection) {
        this.salidaArticuloCollection = salidaArticuloCollection;
    }
     @XmlTransient
    public Collection<Inventarios> getInventariosCollection() {
        return inventariosCollection;
    }

    public void setInventariosCollection(Collection<Inventarios> inventariosCollection) {
        this.inventariosCollection = inventariosCollection;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sede)) {
            return false;
        }
        Sede other = (Sede) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.Sede[ id=" + id + " ]";
    }

}
