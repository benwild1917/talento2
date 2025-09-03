/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "objetos_gasto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ObjetosGasto.findAll", query = "SELECT r FROM ObjetosGasto r")
   , @NamedQuery(name = "ObjetosGasto.findById", query = "SELECT r FROM ObjetosGasto r WHERE r.id = :id")
   , @NamedQuery(name = "ObjetosGasto.findByCodigo", query = "SELECT r FROM ObjetosGasto r WHERE r.codigo = :codigo")
   , @NamedQuery(name = "ObjetosGasto.findOrdered", query = "SELECT r FROM ObjetosGasto r ORDER BY r.codigo")})
public class ObjetosGasto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Column(name = "codigo")
    private Integer codigo;
    @Size(min = 1, max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "analitico_uno")
    private String analitico_uno;
    @Column(name = "analitico_dos")
    private String analitico_dos;

    public ObjetosGasto() {
    }

    public ObjetosGasto(Integer id) {
        this.id = id;
    }

    public ObjetosGasto(Integer id, Integer codigo, String descripcion) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
      public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAnalitico_uno() {
        return analitico_uno;
    }

    public void setAnalitico_uno(String analitico_uno) {
        this.analitico_uno = analitico_uno;
    }

    public String getAnalitico_dos() {
        return analitico_dos;
    }

    public void setAnalitico_dos(String analitico_dos) {
        this.analitico_dos = analitico_dos;
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
        if (!(object instanceof ObjetosGasto)) {
            return false;
        }
        ObjetosGasto other = (ObjetosGasto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.objetogasto[ id=" + id + " ]";
    }
    
}
