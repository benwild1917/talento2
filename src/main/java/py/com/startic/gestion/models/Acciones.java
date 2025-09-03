/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author grecia
 */
@Entity
@Table(name = "acciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Acciones.findAll", query = "SELECT r FROM Acciones r")
    , @NamedQuery(name = "Acciones.findById", query = "SELECT r FROM Acciones r WHERE r.id = :id")
    ,@NamedQuery(name = "Acciones.findByTiposObjetivos", query = "SELECT r FROM Acciones r WHERE r.tiposObjetivos = :tiposObjetivos")
    , @NamedQuery(name = "Acciones.findByDescripcion", query = "SELECT r FROM Acciones r WHERE r.descripcion = :descripcion")})
public class Acciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private TiposObjetivos tiposObjetivos;
     @JoinColumn(name = "departamento_responsable", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamento;
   
   
    
    public Acciones() {
    }

    public Acciones(Integer id) {
        this.id = id;
    }

    public Acciones(Integer id, String descripcion) {
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

    public TiposObjetivos getTiposObjetivos() {
        return tiposObjetivos;
    }

    public void setTiposObjetivos(TiposObjetivos tiposObjetivos) {
        this.tiposObjetivos = tiposObjetivos;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
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
        if (!(object instanceof Acciones)) {
            return false;
        }
        Acciones other = (Acciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.Roles[ id=" + id + " ]";
    }
    
}
