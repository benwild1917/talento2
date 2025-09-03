/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author grecia
 */
@Entity
@Table(name = "salidas_articulo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SalidasArticulo.findAll", query = "SELECT s FROM SalidasArticulo s ORDER BY s.fechaHoraAlta DESC"),
    @NamedQuery(name = "SalidasArticulo.findOrdered", query = "SELECT s FROM SalidasArticulo s WHERE s.fechaHoraAlta >= :fechaDesde AND s.fechaHoraAlta <= :fechaHasta ORDER BY s.fechaHoraAlta DESC"),
    @NamedQuery(name = "SalidasArticulo.findVerNroFormulario", query = "SELECT s FROM SalidasArticulo s WHERE s.fechaHoraAlta >= :fechaDesde AND s.fechaHoraAlta <= :fechaHasta and s.nroFormulario=:nroFormulario ORDER BY s.fechaHoraAlta DESC"),
    @NamedQuery(name = "SalidasArticulo.findOrderedNroFormularioAsignado", query = "SELECT d FROM SalidasArticulo d WHERE d.nroFormulario = :nroFormulario AND d.departamento = :departamento ORDER BY d.fechaHoraUltimoEstado DESC"),
    @NamedQuery(name = "SalidasArticulo.findOrderedNroFormulario", query = "SELECT s FROM SalidasArticulo s WHERE s.nroFormulario = :nroFormulario ORDER BY s.fechaHoraAlta DESC"),
    @NamedQuery(name = "SalidasArticulo.findOrderedNroFormularioAsignadoPersona", query = "SELECT d FROM PedidosArticulo d WHERE d.nroFormulario = :nroFormulario AND d.usuarioAlta = :usuario ORDER BY d.fechaHoraUltimoEstado DESC"),
    @NamedQuery(name = "SalidasArticulo.findOrderedAsc", query = "SELECT s FROM SalidasArticulo s WHERE s.fechaHoraAlta >= :fechaDesde AND s.fechaHoraAlta <= :fechaHasta ORDER BY s.fechaHoraAlta ASC"),
    @NamedQuery(name = "SalidasArticulo.findOrderedFechaAltaAsignado", query = "SELECT d FROM SalidasArticulo d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.departamento = :departamento ORDER BY d.fechaHoraUltimoEstado DESC"),
    @NamedQuery(name = "SalidasArticulo.findOrdered", query = "SELECT s FROM SalidasArticulo s WHERE s.fechaHoraAlta >= :fechaDesde AND s.fechaHoraAlta <= :fechaHasta ORDER BY s.fechaHoraAlta DESC"),
    @NamedQuery(name = "SalidasArticulo.findOrderedFechaAltaAsignadoPersona", query = "SELECT d FROM SalidasArticulo d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.usuarioAlta = :usuario ORDER BY d.fechaHoraUltimoEstado DESC"),
    @NamedQuery(name = "SalidasArticulo.findFechaEntradaOrdered", query = "SELECT s FROM SalidasArticulo s WHERE s.fechaSalida >= :fechaDesde AND s.fechaSalida <= :fechaHasta ORDER BY s.fechaSalida DESC, s.fechaHoraAlta DESC"),
    @NamedQuery(name = "SalidasArticulo.findFechaEntradaOrderedAsc", query = "SELECT s FROM SalidasArticulo s WHERE s.fechaSalida >= :fechaDesde AND s.fechaSalida <= :fechaHasta ORDER BY s.fechaSalida DESC, s.fechaHoraAlta DESC"),
    @NamedQuery(name = "SalidasArticulo.findById", query = "SELECT s FROM SalidasArticulo s WHERE s.id = :id"),
    @NamedQuery(name = "SalidasArticulo.findByArchivoReporte", query = "SELECT s FROM SalidasArticulo s WHERE s.archivosReporte = :archivosReporte"),
    @NamedQuery(name = "SalidasArticulo.findByNroFormulario", query = "SELECT s FROM SalidasArticulo s WHERE s.nroFormulario = :nroFormulario"),
    @NamedQuery(name = "SalidasArticulo.findByFechaHoraAlta", query = "SELECT s FROM SalidasArticulo s WHERE s.fechaHoraAlta = :fechaHoraAlta"),
    @NamedQuery(name = "SalidasArticulo.findByArchivo", query = "SELECT s FROM SalidasArticulo s WHERE s.archivo = :archivo"),
    @NamedQuery(name = "SalidasArticulo.findByFechaHoraUltimoEstado", query = "SELECT s FROM SalidasArticulo s WHERE s.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class SalidasArticulo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "archivo")
    private String archivo;
    //@Size(max = 40)
    @Column(name = "nro_formulario")
    private Integer nroFormulario;
    @Basic(optional = true)
    @Size(max = 500)
    @Column(name = "personas")
    private String personas;
    @JoinColumn(name = "inventario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Inventarios inventario;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_salida")
    @Temporal(TemporalType.DATE)
    private Date fechaSalida;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "salidaArticulo")
    private Collection<DetallesSalidaArticulo> detallesSalidaArticuloCollection;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios persona;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamento;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "usuario_borrado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioBorrado;
    @Column(name = "fecha_hora_borrado") // Este nombre tiene que se igual al nombre de la columna de la tabla 
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @Column(name = "total_salida")
    private BigDecimal total_salida;
    @JoinColumn(name = "sede", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Sede sede;
    @Column(name = "firmar_multiple")
    private boolean firmarMultiple;
    @JoinColumn(name = "articulo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Articulos articulo;
    @JoinColumn(name = "archivo_reporte", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private ArchivosReporte archivosReporte;
     @JoinColumn(name = "responsable_autoriza", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios responsableAutoriza;
    @JoinColumn(name = "responsable_retiro", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios responsableRetiro;
   
    // @Size(max = 40) solo para limitar string
    @Column(name = "entrada_articulo")
    private Integer entradaArticulo;

    public Integer getEntradaArticulo() {
        return entradaArticulo;
    }

    public void setEntradaArticulo(Integer entradaArticulo) {
        this.entradaArticulo = entradaArticulo;
    }

    public SalidasArticulo() {
    }

    public SalidasArticulo(Integer id) {
        this.id = id;
    }

    public Usuarios getUsuarioBorrado() {
        return usuarioBorrado;
    }

    public void setUsuarioBorrado(Usuarios usuarioBorrado) {
        this.usuarioBorrado = usuarioBorrado;
    }

    public Date getFechaHoraBorrado() {
        return fechaHoraBorrado;
    }

    public void setFechaHoraBorrado(Date fechaHoraBorrado) {
        this.fechaHoraBorrado = fechaHoraBorrado;
    }

    public BigDecimal getTotal_salida() {
        return total_salida;
    }

    public void setTotal_salida(BigDecimal total_salida) {
        this.total_salida = total_salida;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public SalidasArticulo(Integer id, Integer nroFormulario, Date fechaHoraAlta, Date fechaHoraUltimoEstado, Usuarios usuarioBorrado, Date fechaHoraBorrado) {
        this.id = id;
        this.nroFormulario = nroFormulario;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
        this.usuarioBorrado = usuarioBorrado;
        this.fechaHoraBorrado = fechaHoraBorrado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPersonas() {
        return personas;
    }

    public void setPersonas(String personas) {
        this.personas = personas;
    }

    public Inventarios getInventario() {
        return inventario;
    }

    public void setInventario(Inventarios inventario) {
        this.inventario = inventario;
    }

    public Integer getNroFormulario() {
        return nroFormulario;
    }

    public void setNroFormulario(Integer nroFormulario) {
        this.nroFormulario = nroFormulario;
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

    @XmlTransient
    public Collection<DetallesSalidaArticulo> getDetallesSalidaArticuloCollection() {
        return detallesSalidaArticuloCollection;
    }

    public void setDetallesSalidaArticuloCollection(Collection<DetallesSalidaArticulo> detallesSalidaArticuloCollection) {
        this.detallesSalidaArticuloCollection = detallesSalidaArticuloCollection;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public Usuarios getPersona() {
        return persona;
    }

    public void setPersona(Usuarios persona) {
        this.persona = persona;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
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

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }

    public boolean isFirmarMultiple() {
        return firmarMultiple;
    }

    public void setFirmarMultiple(boolean firmarMultiple) {
        this.firmarMultiple = firmarMultiple;
    }

    public ArchivosReporte getArchivosReporte() {
        return archivosReporte;
    }

    public void setArchivosReporte(ArchivosReporte archivosReporte) {
        this.archivosReporte = archivosReporte;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public Usuarios getResponsableAutoriza() {
        return responsableAutoriza;
    }

    public void setResponsableAutoriza(Usuarios responsableAutoriza) {
        this.responsableAutoriza = responsableAutoriza;
    }

    public Usuarios getResponsableRetiro() {
        return responsableRetiro;
    }

    public void setResponsableRetiro(Usuarios responsableRetiro) {
        this.responsableRetiro = responsableRetiro;
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
        if (!(object instanceof SalidasArticulo)) {
            return false;
        }
        SalidasArticulo other = (SalidasArticulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(nroFormulario);
    }
}
