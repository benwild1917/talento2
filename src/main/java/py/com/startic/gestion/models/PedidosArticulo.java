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
 * @author grecia
 */
@Entity
@Table(name = "pedidos_articulo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PedidosArticulo.findAll", query = "SELECT s FROM PedidosArticulo s ORDER BY s.fechaHoraAlta DESC")
    , @NamedQuery(name = "PedidosArticulo.findOrderedNroFormularioAsignado", query = "SELECT d FROM PedidosArticulo d WHERE d.nroFormulario = :nroFormulario AND d.departamento = :departamento ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "PedidosArticulo.findOrderedNroFormularioAsignadoPersona", query = "SELECT d FROM PedidosArticulo d WHERE d.nroFormulario = :nroFormulario AND d.usuarioAlta = :usuario ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "PedidosArticulo.findOrderedNroFormulario", query = "SELECT s FROM PedidosArticulo s WHERE s.nroFormulario = :nroFormulario ORDER BY s.fechaHoraAlta DESC")
    , @NamedQuery(name = "PedidosArticulo.findOrdered", query = "SELECT s FROM PedidosArticulo s WHERE s.fechaHoraAlta >= :fechaDesde AND s.fechaHoraAlta <= :fechaHasta ORDER BY s.fechaHoraAlta DESC")
    , @NamedQuery(name = "PedidosArticulo.findPedidosPendientes", query = "SELECT s FROM PedidosArticulo s WHERE s.estado.codigo = '4'  AND s.responsable.departamento = :departamento ORDER BY s.fechaHoraAlta DESC")
    ,@NamedQuery(name = "PedidosArticulo.findPedidosPorAutorizar", query = "SELECT s FROM PedidosArticulo s WHERE s.estado.codigo = '2'  AND s.responsable.departamento = :departamento ORDER BY s.fechaHoraAlta DESC")
    , @NamedQuery(name = "PedidosArticulo.findOrderedFechaAltaAsignado", query = "SELECT d FROM PedidosArticulo d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.departamento = :departamento ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "PedidosArticulo.findOrderedFechaAltaAsignadoPersona", query = "SELECT d FROM PedidosArticulo d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.usuarioAlta = :usuario ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "PedidosArticulo.findById", query = "SELECT s FROM PedidosArticulo s WHERE s.id = :id")
    , @NamedQuery(name = "PedidosArticulo.findByNroFormulario", query = "SELECT s FROM PedidosArticulo s WHERE s.nroFormulario = :nroFormulario")
    , @NamedQuery(name = "PedidosArticulo.findByFechaHoraAlta", query = "SELECT s FROM PedidosArticulo s WHERE s.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "PedidosArticulo.findByFechaHoraUltimoEstado", query = "SELECT s FROM PedidosArticulo s WHERE s.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class PedidosArticulo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    //@Size(max = 40)
    @Column(name = "nro_formulario")
    private Integer nroFormulario;
    @Basic(optional = true)
    @Size(max = 500)
    @Column(name = "personas")
    private String personas;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios responsable;
    @JoinColumn(name = "salida_articulo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SalidasArticulo salidaArticulo;
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
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "pedidoArticulo")
    private Collection<DetallesPedidoArticulo> detallesPedidoArticuloCollection;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios persona;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamento;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private EstadosPedidoArticulo estado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "sede", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Sede sede;
    @JoinColumn(name = "articulo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Articulos articulo;
    @JoinColumn(name = "responsable_autoriza", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios responsableAutoriza;
    @JoinColumn(name = "responsable_retiro", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios responsableRetiro;
    

    public PedidosArticulo() {
    }

    public PedidosArticulo(Integer id) {
        this.id = id;
    }

    public PedidosArticulo(Integer id, Integer nroFormulario, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.nroFormulario = nroFormulario;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SalidasArticulo getSalidaArticulo() {
        return salidaArticulo;
    }

    public void setSalidaArticulo(SalidasArticulo salidaArticulo) {
        this.salidaArticulo = salidaArticulo;
    }

    public EstadosPedidoArticulo getEstado() {
        return estado;
    }

    public void setEstado(EstadosPedidoArticulo estado) {
        this.estado = estado;
    }

    public String getPersonas() {
        return personas;
    }

    public void setPersonas(String personas) {
        this.personas = personas;
    }

    public Usuarios getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuarios responsable) {
        this.responsable = responsable;
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

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }
    
    

    @XmlTransient
    public Collection<DetallesPedidoArticulo> getDetallesPedidoArticuloCollection() {
        return detallesPedidoArticuloCollection;
    }

    public void setDetallesPedidoArticuloCollection(Collection<DetallesPedidoArticulo> detallesPedidoArticuloCollection) {
        this.detallesPedidoArticuloCollection = detallesPedidoArticuloCollection;
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
        if (!(object instanceof PedidosArticulo)) {
            return false;
        }
        PedidosArticulo other = (PedidosArticulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.PedidosArticulo[ id=" + id + " ]";
    }
    
}
