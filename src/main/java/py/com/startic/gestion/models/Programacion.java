/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author grecia
 */
@Entity
@Table(name = "programacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Programacion.findAll", query = "SELECT p FROM Programacion p"),
    @NamedQuery(name = "Programacion.findById", query = "SELECT p FROM Programacion p WHERE p.id = :id  ORDER BY p.año DESC"),
    @NamedQuery(name = "Programacion.findOrdered", query = "SELECT p FROM Programacion p ORDER BY p.id DESC"),
    @NamedQuery(name = "Programacion.findByProgramacion", query = "SELECT p FROM Programacion p WHERE p.accion = :accion"),
    @NamedQuery(name = "Programacion.findByValorVariable", query = "SELECT p FROM Programacion p WHERE p.valorVariable = :valorVariable"),
    @NamedQuery(name = "Programacion.findByTiposObjetivos", query = "SELECT p FROM Programacion p WHERE p.tiposObjetivos = :tiposObjetivos and p.accion = :accion"),
    @NamedQuery(name = "Programacion.findByFormula", query = "SELECT p FROM Programacion p WHERE p.formula = :formula")})
public class Programacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "formula")
    private String formula;
    @Column(name = "valor_variable")
    private double valorVariable;
    @Size(min = 1, max = 10)
    @Column(name = "año")
     private String año;
    @Basic(optional = false)
    // @Size(min = 1, max = 45)
    @Column(name = "meta")
    private Integer meta;
    @Column(name = "linea_base")
    private Integer lineaBase;
    @Basic(optional = false)
    @Column(name = "programa_presupuestario")
    private double programaPresupuestario;
    @JoinColumn(name = "accion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Acciones accion;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private TiposObjetivos tiposObjetivos;

    public Programacion() {
    }

    public Programacion(Integer id) {
        this.id = id;
    }

    public Programacion(Integer id, String formula, Integer meta, double valorVariable) {
        this.id = id;
        this.formula = formula;
        this.meta = meta;
        this.valorVariable = valorVariable;

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

    public double getValorVariable() {
        return valorVariable;
    }

    public void setValorVariable(double valorVariable) {
        this.valorVariable = valorVariable;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getAño() {
        return año;
    }

    public void setAño(String año) {
        this.año = año;
    }
    

    public Integer getLineaBase() {
        return lineaBase;
    }

    public void setLineaBase(Integer lineaBase) {
        this.lineaBase = lineaBase;
    }

    public double getProgramaPresupuestario() {
        return programaPresupuestario;
    }

    public void setProgramaPresupuestario(double programaPresupuestario) {
        this.programaPresupuestario = programaPresupuestario;
    }

    public Integer getMeta() {
        return meta;
    }

    public void setMeta(Integer meta) {
        this.meta = meta;
    }

    public TiposObjetivos getTiposObjetivos() {
        return tiposObjetivos;
    }

    public void setTiposObjetivos(TiposObjetivos tiposObjetivos) {
        this.tiposObjetivos = tiposObjetivos;
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
        if (!(object instanceof Programacion)) {
            return false;
        }
        Programacion other = (Programacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.PlanEstrategicas[ id=" + id + " ]";
    }

}
