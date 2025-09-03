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
 * @author grecia
 */
@Entity
@Table(name = "archivos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Archivos.findAll", query = "SELECT t FROM Archivos t"),
    @NamedQuery(name = "Archivos.findById", query = "SELECT t FROM Archivos t WHERE t.id = :id"),
    @NamedQuery(name = "Archivos.findByDetallesPlanEstrategica", query = "SELECT t FROM Archivos t WHERE t.accionOperativa = :accionOperativa"),
    @NamedQuery(name = "Archivos.findByDocumentoResolucionOrdered", query = "SELECT t FROM Archivos t WHERE t.documentosResolucion = :documentosResolucion ORDER BY t.fechaHoraAlta"),
    @NamedQuery(name = "Archivos.findByDocumentoResolucionesOrdered", query = "SELECT t FROM Archivos t WHERE t.documentosResolucion = :documentosResolucion ORDER BY t.fechaHoraAlta"),
    @NamedQuery(name = "Archivos.findByDocumentoResolucion", query = "SELECT t FROM Archivos t WHERE t.documentosResolucion = :documentosResolucion ORDER BY t.fechaHoraAlta"),
    @NamedQuery(name = "Archivos.findByDocumentoPlanificacion", query = "SELECT t FROM Archivos t WHERE t.accionOperativa = :accionOperativa ORDER BY t.fechaHoraAlta DESC"),
    @NamedQuery(name = "Archivos.findByFechaHoraAlta", query = "SELECT t FROM Archivos t WHERE t.fechaHoraAlta = :fechaHoraAlta"),
    @NamedQuery(name = "Archivos.findByFechaHoraUltimoEstado", query = "SELECT t FROM Archivos t WHERE t.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class Archivos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "ruta")
    private String ruta;
   // @NotNull
    @Lob
    @Size(max = 65535)
    @Column(name = "descripcion")
    private String descripcion;
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
    @Basic(optional = false)
    @Column(name = "fecha_hora_borrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @JoinColumn(name = "usuario_borrado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioBorrado;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Departamentos departamento;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private EstadosActividad estado;
    @JoinColumn(name = "accion_operativa", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DetallesPlanEstrategicas accionOperativa;
    @JoinColumn(name = "documento_resolucion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DocumentosResoluciones documentosResolucion;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TiposDocumentosJudiciales tipoDocumento;
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DocumentosJudiciales documentosJudicial;
    @JoinColumn(name = "formulario_permiso", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FormulariosPermisos formulariosPermisos ;

    public Archivos() {
    }

    public Archivos(Integer id) {
        this.id = id;
    }

    public Archivos(Integer id, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
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

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public Date getFechaHoraBorrado() {
        return fechaHoraBorrado;
    }

    public void setFechaHoraBorrado(Date fechaHoraBorrado) {
        this.fechaHoraBorrado = fechaHoraBorrado;
    }

    public Usuarios getUsuarioBorrado() {
        return usuarioBorrado;
    }

    public void setUsuarioBorrado(Usuarios usuarioBorrado) {
        this.usuarioBorrado = usuarioBorrado;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public EstadosActividad getEstado() {
        return estado;
    }

    public void setEstado(EstadosActividad estado) {
        this.estado = estado;
    }

    public DetallesPlanEstrategicas getAccionOperativa() {
        return accionOperativa;
    }

    public void setAccionOperativa(DetallesPlanEstrategicas accionOperativa) {
        this.accionOperativa = accionOperativa;
    }

    public DocumentosResoluciones getDocumentosResolucion() {
        return documentosResolucion;
    }

    public void setDocumentosResolucion(DocumentosResoluciones documentosResolucion) {
        this.documentosResolucion = documentosResolucion;
    }

    public TiposDocumentosJudiciales getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TiposDocumentosJudiciales tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public DocumentosJudiciales getDocumentosJudicial() {
        return documentosJudicial;
    }

    public void setDocumentosJudicial(DocumentosJudiciales documentosJudicial) {
        this.documentosJudicial = documentosJudicial;
    }

    public FormulariosPermisos getFormulariosPermisos() {
        return formulariosPermisos;
    }

    public void setFormulariosPermisos(FormulariosPermisos formulariosPermisos) {
        this.formulariosPermisos = formulariosPermisos;
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
        if (!(object instanceof Archivos)) {
            return false;
        }
        Archivos other = (Archivos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return (ruta != null) ? ruta : "";
    }

}
