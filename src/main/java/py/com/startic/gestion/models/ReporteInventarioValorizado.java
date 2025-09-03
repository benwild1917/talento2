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
 * @author eduardo
 */
@Entity
@Table(name = "reporte_inventario_valorizado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReporteInventarioValorizado.findAll", query = "SELECT e FROM ReporteInventarioValorizado e")
    , @NamedQuery(name = "ReporteInventarioValorizado.findByCodigo", query = "SELECT e FROM ReporteInventarioValorizado e WHERE e.codigo = :codigo")
    , @NamedQuery(name = "ReporteInventarioValorizado.findByStock", query = "SELECT e FROM ReporteInventarioValorizado e WHERE e.articulo.stock < 0")
    , @NamedQuery(name = "ReporteInventarioValorizado.findByArticulosPorVencer", query = "SELECT e FROM ReporteInventarioValorizado e WHERE  e.fechaVencimiento <= e.fechaHoraAlta  ")
    , @NamedQuery(name = "ReporteInventarioValorizado.findByArticulo", query = "SELECT e FROM ReporteInventarioValorizado e WHERE e.inventario = :inventario AND e.articulo = :articulo ORDER BY e.fechaVencimiento, e.fechaEntrada, e.costoUnitario")})
public class ReporteInventarioValorizado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1)
    @Column(name = "codigo")
    private String codigo;
    @JoinColumn(name = "inventario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Inventarios inventario;
    @JoinColumn(name = "detalle_entrada_articulo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DetallesEntradaArticulo detalleEntradaArticulo;
    @JoinColumn(name = "detalle_inventario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DetallesInventario detalleInventario;
    @JoinColumn(name = "articulo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Articulos articulo;
     @JoinColumn(name = "sede", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Sede sede;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_entrada")
    private int cantEntrada;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_salida")
    private int cantSalida;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "fecha_vencimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;
    @Column(name = "fecha_entrada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrada;
     @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Column(name = "costo_unitario")
    private BigDecimal costoUnitario;
    @Column(name = "costo_total")
    private BigDecimal costoTotal;
    @Column(name = "valorizado")
    private BigDecimal valorizado;
    
    public ReporteInventarioValorizado() {
    }

    public ReporteInventarioValorizado(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Inventarios getInventario() {
        return inventario;
    }

    public void setInventario(Inventarios inventario) {
        this.inventario = inventario;
    }

    public DetallesEntradaArticulo getDetalleEntradaArticulo() {
        return detalleEntradaArticulo;
    }

    public void setDetalleEntradaArticulo(DetallesEntradaArticulo detalleEntradaArticulo) {
        this.detalleEntradaArticulo = detalleEntradaArticulo;
    }

    public DetallesInventario getDetalleInventario() {
        return detalleInventario;
    }

    public void setDetalleInventario(DetallesInventario detalleInventario) {
        this.detalleInventario = detalleInventario;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }

    public int getCantEntrada() {
        return cantEntrada;
    }

    public void setCantEntrada(int cantEntrada) {
        this.cantEntrada = cantEntrada;
    }

    public int getCantSalida() {
        return cantSalida;
    }

    public void setCantSalida(int cantSalida) {
        this.cantSalida = cantSalida;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
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

    public BigDecimal getValorizado() {
        return valorizado;
    }

    public void setValorizado(BigDecimal valorizado) {
        this.valorizado = valorizado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReporteInventarioValorizado)) {
            return false;
        }
        ReporteInventarioValorizado other = (ReporteInventarioValorizado) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return codigo;
    }
    
}
