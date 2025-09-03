/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "formulario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Formulario.findAll", query = "SELECT f FROM Formulario f"),
    @NamedQuery(name = "Formulario.findById", query = "SELECT f FROM Formulario f WHERE f.id = :id"),
    @NamedQuery(name = "Formulario.findByMacroproceso", query = "SELECT f FROM Formulario f WHERE f.macroproceso = :macroproceso"),
    @NamedQuery(name = "Formulario.findByProceso", query = "SELECT f FROM Formulario f WHERE f.proceso = :proceso"),
    @NamedQuery(name = "Formulario.findBySubPoceso", query = "SELECT f FROM Formulario f WHERE f.subproceso= :subproceso"),
    @NamedQuery(name = "Formulario.findByColumna1", query = "SELECT f FROM Formulario f WHERE f.columna1 = :columna1"),
    @NamedQuery(name = "Formulario.findByColumna2", query = "SELECT f FROM Formulario f WHERE f.columna2 = :columna2"),
    @NamedQuery(name = "Formulario.findByColumna3", query = "SELECT f FROM Formulario f WHERE f.columna3= :columna3"),
    @NamedQuery(name = "Formulario.findByColumna4", query = "SELECT f FROM Formulario f WHERE f.columna4 = :columna4"),
    @NamedQuery(name = "Formulario.findByColumna5", query = "SELECT f FROM Formulario f WHERE f.columna5 =:columna5"),
    @NamedQuery(name = "Formulario.findByActividad", query = "SELECT f FROM Formulario f WHERE f.actividad = :actividad")})
public class Formulario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "actividad")
    private String actividad;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "macroproceso")
    private String macroproceso;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "proceso")
    private String proceso;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "subproceso")
    private String subproceso;
    @Size(max = 200)
    @Column(name = "columna_1")
    private String columna1;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "columna_2")
    private String columna2;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "columna_3")
    private String columna3;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "columna_4")
    private String columna4;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "columna_5")
    private String columna5;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas responsable;
    @JoinColumn(name = "grafico", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Simbolos grafico;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getColumna1() {
        return columna1;
    }

    public void setColumna1(String columna1) {
        this.columna1 = columna1;
    }

    public String getColumna2() {
        return columna2;
    }

    public void setColumna2(String columna2) {
        this.columna2 = columna2;
    }

    public String getColumna3() {
        return columna3;
    }

    public void setColumna3(String columna3) {
        this.columna3 = columna3;
    }

    public String getColumna4() {
        return columna4;
    }

    public void setColumna4(String columna4) {
        this.columna4 = columna4;
    }

    public String getColumna5() {
        return columna5;
    }

    public void setColumna5(String columna5) {
        this.columna5 = columna5;
    }

    public String getMacroproceso() {
        return macroproceso;
    }

    public void setMacroproceso(String macroproceso) {
        this.macroproceso = macroproceso;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getSubproceso() {
        return subproceso;
    }

    public void setSubproceso(String subproceso) {
        this.subproceso = subproceso;
    }

    public Personas getResponsable() {
        return responsable;
    }

    public void setResponsable(Personas responsable) {
        this.responsable = responsable;
    }

    public Simbolos getGrafico() {
        return grafico;
    }

    public void setGrafico(Simbolos grafico) {
        this.grafico = grafico;
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
        if (!(object instanceof Formulario)) {
            return false;
        }
        Formulario other = (Formulario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.Formulario[ id=" + id + " ]";
    }

}
