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
@Table(name = "detalles_pedido_articulo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetallesPedidoArticulo.findAll", query = "SELECT d FROM DetallesPedidoArticulo d"),
    @NamedQuery(name = "DetallesPedidoArticulo.findById", query = "SELECT d FROM DetallesPedidoArticulo d WHERE d.id = :id"),
    @NamedQuery(name = "DetallesPedidoArticulo.findByCantidad", query = "SELECT d FROM DetallesPedidoArticulo d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "DetallesPedidoArticulo.findByNroFormulario", query = "SELECT d FROM DetallesPedidoArticulo d WHERE d.nroFormulario = :nroFormulario"),
    @NamedQuery(name = "DetallesPedidoArticulo.findByPedidoArticulo", query = "SELECT d FROM DetallesPedidoArticulo d WHERE d.pedidoArticulo = :pedidoArticulo"),
    @NamedQuery(name = "DetallesPedidoArticulo.findByArticulo", query = "SELECT d FROM DetallesPedidoArticulo d WHERE d.articulo = :articulo"),
    @NamedQuery(name = "DetallesPedidoArticulo.findByArticuloFechaAlta", query = "SELECT d FROM DetallesPedidoArticulo d WHERE d.fechaHoraAlta > :fechaAltaDesde AND d.fechaHoraAlta <= :fechaAltaHasta AND d.articulo = :articulo ORDER BY d.fechaHoraAlta"),
    @NamedQuery(name = "DetallesPedidoArticulo.findByDptoFechaAlta", query = "SELECT d FROM DetallesPedidoArticulo d WHERE d.fechaHoraAlta > :fechaAltaDesde AND d.fechaHoraAlta <= :fechaAltaHasta AND d.departamento = :departamento ORDER BY d.fechaHoraAlta"),
    @NamedQuery(name = "DetallesPedidoArticulo.findByFechaHoraAlta", query = "SELECT d FROM DetallesPedidoArticulo d WHERE d.fechaHoraAlta = :fechaHoraAlta"),
    @NamedQuery(name = "DetallesPedidoArticulo.findByFechaHoraUltimoEstado", query = "SELECT d FROM DetallesPedidoArticulo d WHERE d.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class DetallesPedidoArticulo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private int cantidad;
    @Basic(optional = true)
    @Column(name = "item")
    private int item;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad_pedida")
    private int cantidadPedida;
    @Basic(optional = true)
    @Column(name = "cantidad_director")
    private int cantidadDirector;
    @Basic(optional = true)
    @Column(name = "cantidad_jefe")
    private int cantidadJefe;
    @Basic(optional = true)
   // @Size(max = 40)
    @Column(name = "nro_formulario")
    private Integer nroFormulario;
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
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios persona;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "articulo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Articulos articulo;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamento;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "pedido_articulo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PedidosArticulo pedidoArticulo;
    @JoinColumn(name = "sede", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Sede sede;

    public DetallesPedidoArticulo() {
    }

    public DetallesPedidoArticulo(Integer id) {
        this.id = id;
    }

    public DetallesPedidoArticulo(Integer id, int cantidad, Integer nroFormulario, Articulos articulo, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.cantidad = cantidad;
        this.nroFormulario = nroFormulario;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
        this.articulo = articulo;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidadPedida() {
        return cantidadPedida;
    }

    public void setCantidadPedida(int cantidadPedida) {
        this.cantidadPedida = cantidadPedida;
    }

    public int getCantidadDirector() {
        return cantidadDirector;
    }

    public void setCantidadDirector(int cantidadDirector) {
        this.cantidadDirector = cantidadDirector;
    }

    public int getCantidadJefe() {
        return cantidadJefe;
    }

    public void setCantidadJefe(int cantidadJefe) {
        this.cantidadJefe = cantidadJefe;
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

    public Usuarios getPersona() {
        return persona;
    }

    public void setPersona(Usuarios persona) {
        this.persona = persona;
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

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public PedidosArticulo getPedidoArticulo() {
        return pedidoArticulo;
    }

    public void setPedidoArticulo(PedidosArticulo pedidoArticulo) {
        this.pedidoArticulo = pedidoArticulo;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
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
        if (!(object instanceof DetallesPedidoArticulo)) {
            return false;
        }
        DetallesPedidoArticulo other = (DetallesPedidoArticulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.DetallesPedidoArticulo[ id=" + id + " ]";
    }

}
