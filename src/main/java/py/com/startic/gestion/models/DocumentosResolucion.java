/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author grecia
 */
@Entity
@Table(name = "documentos_resolucion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentosResolucion.findAll", query = "SELECT d FROM DocumentosResolucion d ORDER BY d.fechaHoraAlta desc"),
    @NamedQuery(name = "DocumentosResolucion.findById", query = "SELECT d FROM DocumentosResolucion d WHERE d.id = :id"),
    @NamedQuery(name = "DocumentosResolucion.findOrderedFechaAlta", query = "SELECT d FROM DocumentosResolucion d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoResolucion = :canalEntradaDocumentoResolucion ORDER BY d.fechaHoraAlta DESC"),
    @NamedQuery(name = "DocumentosResolucion.findOrderedDptoTipoDoc", query = "SELECT d FROM DocumentosResolucion d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.tipoDocumentoJudicial = :tipoDocumentoJudicial AND (d.canalEntradaDocumentoResolucion = :canalEntradaDocumentoResolucion OR d.departamento = :departamento) ORDER BY d.fechaHoraAlta DESC, d.fechaHoraUltimoEstado DESC"),
    @NamedQuery(name = "DocumentosResolucion.findByFechaHoraAlta", query = "SELECT d FROM DocumentosResolucion d WHERE d.fechaHoraAlta = :fechaHoraAlta"),
    @NamedQuery(name = "DocumentosResolucion.findByFechaResolucion", query = "SELECT d FROM DocumentosResolucion d WHERE d.fechaResolucion = :fechaResolucion"),
    @NamedQuery(name = "DocumentosResolucion.findByFechaHoraUltimoEstado", query = "SELECT d FROM DocumentosResolucion d WHERE d.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class DocumentosResolucion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "entrada_documento_resolucion", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private EntradasDocumentosResolucion entradaDocumentoResolucion;
    @JoinColumn(name = "observacion_documento_resolucion", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private ObservacionesDocumentosJudiciales observacionDocumentoResolucion;
    @JoinColumn(name = "estado_procesal_documento_resolucion", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private EstadosProcesalesDocumentosResolucion estadoProcesalDocumentoResolucion;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "descripcion")
    private String descripcion;
    @Lob
    @Size(max = 65535)
    @Column(name = "ultima_observacion")
    private String ultimaObservacion;
    @Lob
    @Size(max = 65535)
    @Column(name = "folios")
    private String folios;
    @Transient
    private String ultimaObservacionAux;
    @Basic(optional = true)
    @Column(name = "fecha_ultima_observacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltimaObservacion;
    @Transient
    private String estadoProcesalAux;
    @Basic(optional = true)
    @Size(min = 2, max = 2)
    @Column(name = "mostrar_web")
    private String mostrarWeb;
    @Basic(optional = true)
    @Column(name = "fecha_resolucion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaResolucion;
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
    @Basic(optional = true)
    @Column(name = "fecha_hora_borrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_elaboracion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraElaboracion;
    @Column(name = "fecha_hora_estado_procesal")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraEstadoProcesal;
    @JoinColumn(name = "tipo_documento_judicial", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TiposDocumentosJudiciales tipoDocumentoJudicial;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "usuario_ultima_observacion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioUltimaObservacion;
    @JoinColumn(name = "usuario_borrado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioBorrado;
    @JoinColumn(name = "usuario_elaboracion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioElaboracion;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios responsable;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Departamentos departamento;
    @JoinColumn(name = "departamento_anterior", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Departamentos departamentoAnterior;
    @JoinColumn(name = "responsable_anterior", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios responsableAnterior;
    @JoinColumn(name = "canal_entrada_documento_resolucion", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private CanalesEntradaDocumentoResolucion canalEntradaDocumentoResolucion;
    @JoinColumn(name = "usuario_estado_procesal", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioEstadoProcesal;
    @Column(name = "visto")
    private boolean visto;
    @JoinColumn(name = "usuario_visto", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioVisto;
    @JoinColumn(name = "estado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private EstadosDocumento estado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_visto")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraVisto;
    @Basic(optional = true)
    @Column(name = "fecha_final")
    @Temporal(TemporalType.DATE)
    private Date fechaFinal;
    @Size(max = 20)
    @Column(name = "nro_final")
    private String nroFinal;
    @Size(max = 20)
    @Column(name = "nomenclatura_final")
    private String nomenclaturaFinal;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "texto_final")
    private String textoFinal;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "codigo_archivo")
    private String codigoArchivo;

    /*
    @JoinColumn(name = "tipo_prioridad", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private TiposPrioridad tipoPrioridad;
     */
    public DocumentosResolucion() {
    }

    public DocumentosResolucion(Integer id) {
        this.id = id;
    }

    public DocumentosResolucion(Integer id, EntradasDocumentosResolucion entradaDocumentoResolucion, String descripcion, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.entradaDocumentoResolucion = entradaDocumentoResolucion;
        this.descripcion = descripcion;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMostrarWeb() {
        return mostrarWeb;
    }

    public void setMostrarWeb(String mostrarWeb) {
        this.mostrarWeb = mostrarWeb;
    }

    public Usuarios getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuarios responsable) {
        this.responsable = responsable;
    }

    public String getUltimaObservacionAux() {
        return ultimaObservacion;
    }

    public void setUltimaObservacionAux(String ultimaObservacionAux) {
        this.ultimaObservacionAux = ultimaObservacionAux;
    }

    public String getUltimaObservacionAntecedenteAux() {
        return ultimaObservacion;
    }

    public void setEstadoProcesalAux(String estadoProcesalAux) {
        this.estadoProcesalAux = estadoProcesalAux;
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

    public Date getFechaHoraElaboracion() {
        return fechaHoraElaboracion;
    }

    public void setFechaHoraElaboracion(Date fechaHoraElaboracion) {
        this.fechaHoraElaboracion = fechaHoraElaboracion;
    }

    public Usuarios getUsuarioElaboracion() {
        return usuarioElaboracion;
    }

    public void setUsuarioElaboracion(Usuarios usuarioElaboracion) {
        this.usuarioElaboracion = usuarioElaboracion;
    }

    public String getNomenclaturaFinal() {
        return nomenclaturaFinal;
    }

    public void setNomenclaturaFinal(String nomenclaturaFinal) {
        this.nomenclaturaFinal = nomenclaturaFinal;
    }

    public boolean isVisto() {
        return visto;
    }

    public void setVisto(boolean visto) {
        this.visto = visto;
    }

    public Usuarios getUsuarioVisto() {
        return usuarioVisto;
    }

    public void setUsuarioVisto(Usuarios usuarioVisto) {
        this.usuarioVisto = usuarioVisto;
    }

    public Date getFechaHoraVisto() {
        return fechaHoraVisto;
    }

    public void setFechaHoraVisto(Date fechaHoraVisto) {
        this.fechaHoraVisto = fechaHoraVisto;
    }

    public EntradasDocumentosResolucion getEntradaDocumentoResolucion() {
        return entradaDocumentoResolucion;
    }

    public void setEntradaDocumentoResolucion(EntradasDocumentosResolucion entradaDocumentoResolucion) {
        this.entradaDocumentoResolucion = entradaDocumentoResolucion;
    }

    public ObservacionesDocumentosJudiciales getObservacionDocumentoResolucion() {
        return observacionDocumentoResolucion;
    }

    public void setObservacionDocumentoResolucion(ObservacionesDocumentosJudiciales observacionDocumentoResolucion) {
        this.observacionDocumentoResolucion = observacionDocumentoResolucion;
    }

    public CanalesEntradaDocumentoResolucion getCanalEntradaDocumentoResolucion() {
        return canalEntradaDocumentoResolucion;
    }

    public void setCanalEntradaDocumentoResolucion(CanalesEntradaDocumentoResolucion canalEntradaDocumentoResolucion) {
        this.canalEntradaDocumentoResolucion = canalEntradaDocumentoResolucion;
    }

    public EstadosProcesalesDocumentosResolucion getEstadoProcesalDocumentoResolucion() {
        return estadoProcesalDocumentoResolucion;
    }

    public void setEstadoProcesalDocumentoResolucion(EstadosProcesalesDocumentosResolucion estadoProcesalDocumentoResolucion) {
        this.estadoProcesalDocumentoResolucion = estadoProcesalDocumentoResolucion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TiposDocumentosJudiciales getTipoDocumentoJudicial() {
        return tipoDocumentoJudicial;
    }

    public void setTipoDocumentoJudicial(TiposDocumentosJudiciales tipoDocumentoJudicial) {
        this.tipoDocumentoJudicial = tipoDocumentoJudicial;
    }

    public String getCodigoArchivo() {
        return codigoArchivo;
    }

    public void setCodigoArchivo(String codigoArchivo) {
        this.codigoArchivo = codigoArchivo;
    }

    public Date getFechaHoraEstadoProcesal() {
        return fechaHoraEstadoProcesal;
    }

    public void setFechaHoraEstadoProcesal(Date fechaHoraEstadoProcesal) {
        this.fechaHoraEstadoProcesal = fechaHoraEstadoProcesal;
    }

    public Usuarios getUsuarioEstadoProcesal() {
        return usuarioEstadoProcesal;
    }

    public void setUsuarioEstadoProcesal(Usuarios usuarioEstadoProcesal) {
        this.usuarioEstadoProcesal = usuarioEstadoProcesal;
    }

    public EstadosDocumento getEstado() {
        return estado;
    }

    public void setEstado(EstadosDocumento estado) {
        this.estado = estado;
    }

    public String getFolios() {
        return folios;
    }

    public void setFolios(String folios) {
        this.folios = folios;
    }
    

    @XmlTransient
    public boolean verifObs() {
        if (ultimaObservacion == null && ultimaObservacionAux != null) {
            return true;
        } else if (ultimaObservacion != null && ultimaObservacionAux == null) {
            return false;
        } else if (ultimaObservacion == null && ultimaObservacionAux == null) {
            return false;
        }

        return !ultimaObservacion.equals(ultimaObservacionAux);
    }

    @XmlTransient
    public void transferirObs() {
        ultimaObservacion = ultimaObservacionAux;
    }

    public Usuarios getUsuarioUltimaObservacion() {
        return usuarioUltimaObservacion;
    }

    public void setUsuarioUltimaObservacion(Usuarios usuarioUltimaObservacion) {
        this.usuarioUltimaObservacion = usuarioUltimaObservacion;
    }

    public String getUltimaObservacion() {
        return ultimaObservacion;
    }

    @XmlTransient
    public String getUltimaObservacionString() {
        if (ultimaObservacion != null) {
            return ultimaObservacion.replace("\n", "<br />");
        } else {
            return "";
        }
    }

    public void setUltimaObservacion(String ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }

    public Date getFechaUltimaObservacion() {
        return fechaUltimaObservacion;
    }

    public void setFechaUltimaObservacion(Date fechaUltimaObservacion) {
        this.fechaUltimaObservacion = fechaUltimaObservacion;
    }

    public Date getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(Date fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    @XmlTransient
    public String setDescripcionString() {
        if (descripcion != null) {
            return descripcion.replace("\n", "<br />");
        } else {
            return "";
        }
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

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public Departamentos getDepartamentoAnterior() {
        return departamentoAnterior;
    }

    public void setDepartamentoAnterior(Departamentos departamentoAnterior) {
        this.departamentoAnterior = departamentoAnterior;
    }

    public Usuarios getResponsableAnterior() {
        return responsableAnterior;
    }

    public void setResponsableAnterior(Usuarios responsableAnterior) {
        this.responsableAnterior = responsableAnterior;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getNroFinal() {
        return nroFinal;
    }

    public void setNroFinal(String nroFinal) {
        this.nroFinal = nroFinal;
    }

    public String getTextoFinal() {
        return textoFinal;
    }

    public void setTextoFinal(String textoFinal) {
        this.textoFinal = textoFinal;
    }

    /*
    public TiposPrioridad getTipoPrioridad() {
        return tipoPrioridad;
    }

    public void setTipoPrioridad(TiposPrioridad tipoPrioridad) {
        this.tipoPrioridad = tipoPrioridad;
    }
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentosResolucion)) {
            return false;
        }
        DocumentosResolucion other = (DocumentosResolucion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.gestionstartic.models.DocumentosResolucion[ id=" + id + " ]";
    }

}
