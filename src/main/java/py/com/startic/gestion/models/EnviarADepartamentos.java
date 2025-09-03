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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author grecia
 */
@Entity
@Table(name = "enviar_a_departamentos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnviarADepartamentos.findAll", query = "SELECT d FROM EnviarADepartamentos d"),
    @NamedQuery(name = "EnviarADepartamentos.findById", query = "SELECT d FROM EnviarADepartamentos d WHERE d.id = :id"),
    @NamedQuery(name = "EnviarADepartamentos.findOrdered", query = "SELECT d FROM EnviarADepartamentos d WHERE d.responsable = :responsable  ORDER BY d.id DESC"),
    @NamedQuery(name = "EnviarADepartamentos.findByDetallesResolucion", query = "SELECT d FROM EnviarADepartamentos d WHERE d.documentosResoluciones = :documentosResoluciones"),
    @NamedQuery(name = "EnviarADepartamentos.findByDocumentoResolucion", query = "SELECT d FROM EnviarADepartamentos d WHERE d.detallesDestinatario = :detallesDestinatario ORDER BY d.id"),
    @NamedQuery(name = "EnviarADepartamentos.findByArchivo", query = "SELECT d FROM EnviarADepartamentos d WHERE d.fechaHoraAlta = :fechaHoraAlta")})

public class EnviarADepartamentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
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
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamento;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios responsable;
    @JoinColumn(name = "documento_resolucion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosResoluciones documentosResoluciones;
    @JoinColumn(name = "tipo_envio", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TiposEnvio tipoEnvio;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private EstadosDocumento estado;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TiposDocumentosJudiciales tipoDocumento;
    @JoinColumn(name = "responsable_anterior", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios responsableAnterior;
    @JoinColumn(name = "detalle_destinatario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DetallesDestinatarios detallesDestinatario;

    public EnviarADepartamentos() {
    }

    public EnviarADepartamentos(Integer id) {
        this.id = id;
    }

    public EnviarADepartamentos(Integer id, Date fechaHoraAlta) {
        this.id = id;
        this.fechaHoraAlta = fechaHoraAlta;

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

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
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

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public Usuarios getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuarios responsable) {
        this.responsable = responsable;
    }

    public DocumentosResoluciones getDocumentosResoluciones() {
        return documentosResoluciones;
    }

    public void setDocumentosResoluciones(DocumentosResoluciones documentosResoluciones) {
        this.documentosResoluciones = documentosResoluciones;
    }

    public TiposEnvio getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(TiposEnvio tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }

    public EstadosDocumento getEstado() {
        return estado;
    }

    public void setEstado(EstadosDocumento estado) {
        this.estado = estado;
    }

    public TiposDocumentosJudiciales getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TiposDocumentosJudiciales tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Usuarios getResponsableAnterior() {
        return responsableAnterior;
    }

    public void setResponsableAnterior(Usuarios responsableAnterior) {
        this.responsableAnterior = responsableAnterior;
    }

    public DetallesDestinatarios getDetallesDestinatario() {
        return detallesDestinatario;
    }

    public void setDetallesDestinatario(DetallesDestinatarios detallesDestinatario) {
        this.detallesDestinatario = detallesDestinatario;
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
        if (!(object instanceof EnviarADepartamentos)) {
            return false;
        }
        EnviarADepartamentos other = (EnviarADepartamentos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.EnviarADepartamentos[ id=" + id + " ]";
    }

}
