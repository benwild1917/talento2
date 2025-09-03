/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author grecia
 */
@Entity
@Table(name = "actividades")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Actividades.findAll", query = "SELECT p FROM Actividades p"),
    @NamedQuery(name = "Actividades.findById", query = "SELECT p FROM Actividades p WHERE p.id = :id"),
    @NamedQuery(name = "Actividades.findByAcciones", query = "SELECT p FROM Actividades p WHERE p.accion = :accion"),
    @NamedQuery(name = "Actividades.findByDescripcion", query = "SELECT p FROM Actividades p WHERE p.descripcion = :descripcion")})
public class Actividades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Lob
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "accion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Acciones accion;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamento;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TiposDocumentosJudiciales tiposDocumento;

    public Actividades() {
    }

    public Actividades(Integer id) {
        this.id = id;
    }

    public Actividades(Integer id, String descripcion, TiposDocumentosJudiciales tiposDocumento) {
        this.id = id;
        this.descripcion = descripcion;
        this.tiposDocumento = tiposDocumento;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Acciones getAccion() {
        return accion;
    }

    public void setAccion(Acciones accion) {
        this.accion = accion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public TiposDocumentosJudiciales getTiposDocumento() {
        return tiposDocumento;
    }

    public void setTiposDocumento(TiposDocumentosJudiciales tiposDocumento) {
        this.tiposDocumento = tiposDocumento;
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
        if (!(object instanceof Actividades)) {
            return false;
        }
        Actividades other = (Actividades) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.Actividades[ id=" + id + " ]";
    }

}
