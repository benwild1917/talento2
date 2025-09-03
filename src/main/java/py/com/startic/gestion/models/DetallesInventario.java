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
 * @author eduardo
 */
@Entity
@Table(name = "detalles_inventario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetallesInventario.findAll", query = "SELECT d FROM DetallesInventario d"),
    @NamedQuery(name = "DetallesInventario.findById", query = "SELECT d FROM DetallesInventario d WHERE d.id = :id"),
    @NamedQuery(name = "DetallesInventario.findByCantidad", query = "SELECT d FROM DetallesInventario d WHERE d.cantidad = :cantidad") // , @NamedQuery(name = "DetallesInventario.findByPrecioUnitario", query = "SELECT d FROM DetallesInventario d WHERE d.precioUnitario = :precioUnitario")
    // , @NamedQuery(name = "DetallesInventario.findByFechaVencimiento", query = "SELECT d FROM DetallesInventario d WHERE d.fechaVencimiento = :fechaVencimiento")
    ,
    @NamedQuery(name = "DetallesInventario.findByInventario", query = "SELECT d FROM DetallesInventario d WHERE d.inventario = :inventario ORDER BY d.articulo.descripcion"),
    @NamedQuery(name = "DetallesInventario.findByInventarioPorDias", query = "SELECT d FROM DetallesInventario d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta < :fechaHasta AND d.articulo.rubro.sede in :sede ORDER BY  d.fechaHoraAlta"),     
    @NamedQuery(name = "DetallesInventario.findByInventarioANDArticulo", query = "SELECT d FROM DetallesInventario d WHERE d.inventario = :inventario AND d.articulo = :articulo"),
    @NamedQuery(name = "DetallesInventario.findByInventarioPorDiasp", query = "SELECT d FROM DetallesInventario d WHERE d.inventario = :inventario AND d.articulo = :articulo"),
    @NamedQuery(name = "DetallesInventario.findByArticulosPorVencer", query = "SELECT d FROM DetallesInventario d WHERE d.fechaVencimiento <= d.fechaHoraAlta ORDER BY d.fechaHoraAlta ASC"),
    @NamedQuery(name = "DetallesInventario.findByFechaHoraAlta", query = "SELECT d FROM DetallesInventario d WHERE d.fechaHoraAlta = :fechaHoraAlta"),
    @NamedQuery(name = "DetallesInventario.findByFechaHoraUltimoEstado", query = "SELECT d FROM DetallesInventario d WHERE d.fechaHoraUltimoEstado = :fechaHoraUltimoEstado"),
    @NamedQuery(name = "DetallesInventario.findLastByArticulo", query = "SELECT d FROM DetallesInventario d WHERE d.inventario.fecha = (select max(dd.inventario.fecha) FROM DetallesInventario dd where dd.articulo.codigo = :articulo) and d.articulo.codigo = :articulo")
})
public class DetallesInventario implements Serializable {

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
    @JoinColumn(name = "inventario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Inventarios inventario;
    @JoinColumn(name = "articulo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Articulos articulo;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "marca", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Marcas marca;
    @JoinColumn(name = "modelo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Modelos modelo;
    @Basic(optional = true)
    @Column(name = "fecha_vencimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;
    @Column(name = "costo_unitario")
    private BigDecimal costoUnitario;
    @Column(name = "costo_total")
    private BigDecimal costoTotal;
    @JoinColumn(name = "sede", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Sede sede;

    public DetallesInventario() {
    }

    public DetallesInventario(Integer id) {
        this.id = id;
    }

    public DetallesInventario(Integer id, int cantidad, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.cantidad = cantidad;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Marcas getMarca() {
        return marca;
    }

    public void setMarca(Marcas marca) {
        this.marca = marca;
    }

    public Modelos getModelo() {
        return modelo;
    }

    public void setModelo(Modelos modelo) {
        this.modelo = modelo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
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

    public Inventarios getInventario() {
        return inventario;
    }

    public void setInventario(Inventarios inventario) {
        this.inventario = inventario;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
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

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
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
        if (!(object instanceof DetallesInventario)) {
            return false;
        }
        DetallesInventario other = (DetallesInventario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.DetallesInventario[ id=" + id + " ]";
    }

}
