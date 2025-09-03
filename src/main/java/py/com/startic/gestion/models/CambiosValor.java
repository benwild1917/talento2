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
@Table(name = "cambios_valor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CambiosValor.findAll", query = "SELECT o FROM CambiosValor o"),
    @NamedQuery(name = "CambiosValor.findOrdered", query = "SELECT o FROM CambiosValor o ORDER BY o.fechaHoraUltimoEstado DESC"),
    @NamedQuery(name = "CambiosValor.findByProgramacion", query = "SELECT o FROM CambiosValor o WHERE o.programacion = :programacion ORDER BY o.fechaHoraUltimoEstado DESC"),
    @NamedQuery(name = "CambiosValor.findById", query = "SELECT o FROM CambiosValor o WHERE o.id = :id"),
    @NamedQuery(name = "CambiosValor.findByFechaHoraAlta", query = "SELECT o FROM CambiosValor o WHERE o.fechaHoraAlta = :fechaHoraAlta")})
public class CambiosValor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @Basic(optional = false)
    @Column(name = "cantidad_original")
    private double cantidadOriginal;
    @Basic(optional = false)
    @Column(name = "cantidad_final")
    private double cantidadFinal;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "programacion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Programacion programacion;
    @JoinColumn(name = "detalles_plan_estrategica", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DetallesPlanEstrategicas detallesPlanEstrategica;

    public CambiosValor() {
    }

    public CambiosValor(Integer id) {
        this.id = id;
    }

    public CambiosValor(Integer id, String observacion, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Date getFechaHoraUltimoEstado() {
        return fechaHoraUltimoEstado;
    }

    public void setFechaHoraUltimoEstado(Date fechaHoraUltimoEstado) {
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public double getCantidadOriginal() {
        return cantidadOriginal;
    }

    public void setCantidadOriginal(double cantidadOriginal) {
        this.cantidadOriginal = cantidadOriginal;
    }

    public double getCantidadFinal() {
        return cantidadFinal;
    }

    public void setCantidadFinal(double cantidadFinal) {
        this.cantidadFinal = cantidadFinal;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Usuarios getUsuarioUltimoEstado() {
        return usuarioUltimoEstado;
    }

    public void setUsuarioUltimoEstado(Usuarios usuarioUltimoEstado) {
        this.usuarioUltimoEstado = usuarioUltimoEstado;
    }

    public DetallesPlanEstrategicas getDetallesPlanEstrategica() {
        return detallesPlanEstrategica;
    }

    public void setDetallesPlanEstrategica(DetallesPlanEstrategicas detallesPlanEstrategica) {
        this.detallesPlanEstrategica = detallesPlanEstrategica;
    }
    

    public Programacion getProgramacion() {
        return programacion;
    }

    public void setProgramacion(Programacion programacion) {
        this.programacion = programacion;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
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
        if (!(object instanceof CambiosValor)) {
            return false;
        }
        CambiosValor other = (CambiosValor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.ObservacionesDocumentosJudiciales[ id=" + id + " ]";
    }

}
