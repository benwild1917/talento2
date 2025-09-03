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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "estados_procesales_documentos_resolucion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadosProcesalesDocumentosResolucion.findAll", query = "SELECT e FROM EstadosProcesalesDocumentosResolucion e")
    , @NamedQuery(name = "EstadosProcesalesDocumentosResolucion.findById", query = "SELECT e FROM EstadosProcesalesDocumentosResolucion e WHERE e.id = :id")
    , @NamedQuery(name = "EstadosProcesalesDocumentosResolucion.findByDocumentoResolucion", query = "SELECT e FROM EstadosProcesalesDocumentosResolucion e WHERE e.documentoResolucion = :documentoResolucion ORDER BY e.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "EstadosProcesalesDocumentosResolucion.findByFechaHoraAlta", query = "SELECT e FROM EstadosProcesalesDocumentosResolucion e WHERE e.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "EstadosProcesalesDocumentosResolucion.findByFechaHoraUltimoEstado", query = "SELECT e FROM EstadosProcesalesDocumentosResolucion e WHERE e.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class EstadosProcesalesDocumentosResolucion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "documento_resolucion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DocumentosResolucion documentoResolucion;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "estado_procesal")
    private String estadoProcesal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @Basic(optional = true)
    @Column(name = "visible")
    private boolean visible;
    @Basic(optional = true)
    @Column(name = "fecha_hora_visible")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraVisible;

    public EstadosProcesalesDocumentosResolucion() {
    }

    public EstadosProcesalesDocumentosResolucion(Integer id) {
        this.id = id;
    }

    public EstadosProcesalesDocumentosResolucion(Integer id, String estadoProcesal, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.estadoProcesal = estadoProcesal;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DocumentosResolucion getDocumentoResolucion() {
        return documentoResolucion;
    }

    public void setDocumentoResolucion(DocumentosResolucion documentoResolucion) {
        this.documentoResolucion = documentoResolucion;
    }

   

    public String getEstadoProcesal() {
        return estadoProcesal;
    }

    public void setEstadoProcesal(String estadoProcesal) {
        this.estadoProcesal = estadoProcesal;
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

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Date getFechaHoraVisible() {
        return fechaHoraVisible;
    }

    public void setFechaHoraVisible(Date fechaHoraVisible) {
        this.fechaHoraVisible = fechaHoraVisible;
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
        if (!(object instanceof EstadosProcesalesDocumentosResolucion)) {
            return false;
        }
        EstadosProcesalesDocumentosResolucion other = (EstadosProcesalesDocumentosResolucion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.EstadosProcesalesDocumentosResolucion[ id=" + id + " ]";
    }
    
}
