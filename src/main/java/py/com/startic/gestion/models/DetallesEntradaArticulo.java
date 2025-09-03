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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.exolab.castor.types.DateTime;

/**
 *
 * @author grecia
 */
@Entity
@Table(name = "detalles_entrada_articulo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetallesEntradaArticulo.findAll", query = "SELECT d FROM DetallesEntradaArticulo d")
    , @NamedQuery(name = "DetallesEntradaArticulo.findById", query = "SELECT d FROM DetallesEntradaArticulo d WHERE d.id = :id")
    , @NamedQuery(name = "DetallesEntradaArticulo.findByArticulo", query = "SELECT d FROM DetallesEntradaArticulo d WHERE d.articulo = :articulo")
    , @NamedQuery(name = "DetallesEntradaArticulo.findByArticuloFechaEntrada", query = "SELECT d FROM DetallesEntradaArticulo d WHERE d.entradaArticulo.fechaEntrada >= :fechaEntradaDesde AND d.entradaArticulo.fechaEntrada < :fechaEntradaHasta AND d.articulo in :articulo ORDER BY d.entradaArticulo.fechaEntrada, d.fechaHoraAlta")
    , @NamedQuery(name = "DetallesEntradaArticulo.findByObjetoGastoFechaEntrada", query = "SELECT d FROM DetallesEntradaArticulo d WHERE d.entradaArticulo.fechaEntrada >= :fechaEntradaDesde AND d.entradaArticulo.fechaEntrada < :fechaEntradaHasta AND d.articulo.objetoGasto = :objetoGasto ORDER BY d.entradaArticulo.fechaEntrada, d.fechaHoraAlta")
    , @NamedQuery(name = "DetallesEntradaArticulo.findByProveedorFechaEntrada", query = "SELECT d FROM DetallesEntradaArticulo d WHERE d.entradaArticulo.fechaEntrada >= :fechaEntradaDesde AND d.entradaArticulo.fechaEntrada < :fechaEntradaHasta AND d.proveedor in :proveedor ORDER BY d.entradaArticulo.fechaEntrada, d.fechaHoraAlta")
    , @NamedQuery(name = "DetallesEntradaArticulo.findBySedeFechaEntrada", query = "SELECT d FROM DetallesEntradaArticulo d WHERE d.entradaArticulo.fechaEntrada >= :fechaEntradaDesde AND d.entradaArticulo.fechaEntrada < :fechaEntradaHasta AND d.sede in :sede")
    , @NamedQuery(name = "DetallesEntradaArticulo.findByEntradaArticulo", query = "SELECT d FROM DetallesEntradaArticulo d WHERE d.entradaArticulo = :entradaArticulo")
    , @NamedQuery(name = "DetallesEntradaArticulo.findByCantidad", query = "SELECT d FROM DetallesEntradaArticulo d WHERE d.cantidad = :cantidad")
    , @NamedQuery(name = "DetallesEntradaArticulo.findByMarcas", query = "SELECT d FROM DetallesEntradaArticulo d WHERE d.marca = :marca")
    , @NamedQuery(name = "DetallesEntradaArticulo.findByModelos", query = "SELECT d FROM DetallesEntradaArticulo d WHERE d.modelo = :modelo")
    , @NamedQuery(name = "DetallesEntradaArticulo.findByNroFactura", query = "SELECT d FROM DetallesEntradaArticulo d WHERE d.nroFactura = :nroFactura")
    , @NamedQuery(name = "DetallesEntradaArticulo.findByNroLlamado", query = "SELECT d FROM DetallesEntradaArticulo d WHERE d.nroLlamado = :nroLlamado")
    , @NamedQuery(name = "DetallesEntradaArticulo.findByObjetoGasto", query = "SELECT d FROM DetallesEntradaArticulo d WHERE d.objetoGasto = :objetoGasto")
    , @NamedQuery(name = "DetallesEntradaArticulo.findByFechaHoraAlta", query = "SELECT d FROM DetallesEntradaArticulo d WHERE d.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "DetallesEntradaArticulo.findByArticulosPorVencer", query = "SELECT d FROM DetallesEntradaArticulo d WHERE  d.fechaVencimiento <= d.fechaHoraAlta ORDER BY d.fechaHoraAlta ASC")
    , @NamedQuery(name = "DetallesEntradaArticulo.findByFechaLlamado", query = "SELECT d FROM DetallesEntradaArticulo d WHERE d.fechaLlamado = :fechaLlamado")
    , @NamedQuery(name = "DetallesEntradaArticulo.findByFechaHoraUltimoEstado", query = "SELECT d FROM DetallesEntradaArticulo d WHERE d.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class DetallesEntradaArticulo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "articulo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Articulos articulo;
    @NotNull
    @Column(name = "fecha_llamado") // Este nombre tiene que se igual al nombre de la columna de la tabla 
    @Temporal(TemporalType.DATE)
    private Date fechaLlamado;   
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "codigo_dncp")
    private String codigoDNCP;  
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "codigo_suministro")
    private String codigoSuministro;
    @JoinColumn(name = "objeto_gasto", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private ObjetosGasto objetoGasto;
    @JoinColumn(name = "modelo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Modelos modelo;
    @JoinColumn(name = "marca", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Marcas marca;
    @JoinColumn(name = "usuario_borrado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioBorrado;
    @Column(name = "fecha_hora_borrado") // Este nombre tiene que se igual al nombre de la columna de la tabla 
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;   
   
    @JoinColumn(name = "tipo_iva", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TiposIva tipoIva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private int cantidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nro_factura")
    private String nroFactura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nro_llamado")
    private String nroLlamado; 
    @Column(name = "costo_unitario")
    private BigDecimal costoUnitario;
    @Column(name = "costo_total")
    private BigDecimal costoTotal;
    @Column(name = "iva_5")
    private BigDecimal iva_5;
    @Column(name = "iva_10")
    private BigDecimal iva_10;
    @Basic(optional = false)
    @Column(name = "exentas")
    private BigDecimal exentas;
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
    @Column(name = "fecha_vencimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;
    @JoinColumn(name = "inventario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Inventarios inventario;
    @JoinColumn(name = "proveedor", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Proveedores proveedor;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "entrada_articulo", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private EntradasArticulo entradaArticulo;
    @JoinColumn(name = "sede", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Sede sede;
    @Transient
    private Date fechaEntrada;

    public DetallesEntradaArticulo() {
    }

    public DetallesEntradaArticulo(Integer id) {
        this.id = id;
    }

    public DetallesEntradaArticulo(Integer id, Articulos articulo, int cantidad,Marcas marca, Modelos modelo, String nroFactura, String nroLlamado, Date fechaHoraAlta, Date fechaLlamado, Date fechaHoraUltimoEstado,Usuarios usuarioBorrado ,Date fechaHoraBorrado) {
        this.id = id;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.marca = marca;
        this.modelo = modelo;
        this.nroFactura = nroFactura;
        this.fechaLlamado = fechaLlamado;
        this.nroLlamado = nroLlamado;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
        this.usuarioBorrado= usuarioBorrado; 
        this.fechaHoraBorrado=fechaHoraBorrado; 
     }


    public TiposIva getTipoIva() {
        return tipoIva;
    }

    public void setTipoIva(TiposIva tipoIva) {
        this.tipoIva = tipoIva;
    }
    
    public Marcas getMarca() {
        return marca;
    }

    public void setMarcas(Marcas marca) {
        this.marca = marca;
    }
    
    public ObjetosGasto getObjetoGasto() {
        return objetoGasto;
    }

    public void setObjetoGasto(ObjetosGasto objetoGasto) {
        this.objetoGasto = objetoGasto;
    }

    public String getCodigoSuministro() {
        return codigoSuministro;
    }

    public void setCodigoSuministro(String codigoSuministro) {
        this.codigoSuministro = codigoSuministro;
    }

      public Modelos getModelo() {
        return modelo;
    }

    public void setModelo(Modelos modelo) {
        this.modelo = modelo;
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

    public BigDecimal getIva_5() {
        return iva_5;
    }

    public void setIva_5(BigDecimal iva_5) {
        this.iva_5 = iva_5;
    }

    public BigDecimal getIva_10() {
        return iva_10;
    }

    public void setIva_10(BigDecimal iva_10) {
        this.iva_10 = iva_10;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getExentas() {
        return exentas;
    }

    public void setExentas(BigDecimal exentas) {
        this.exentas = exentas;
    }

    
    public Date getFechaLlamado() {
        return fechaLlamado;
    }

    public void setFechaLlamado(Date fechaLlamado) {
        this.fechaLlamado = fechaLlamado;
    }

    public String getCodigoDNCP() {
        return codigoDNCP;
    }

    public void setCodigoDNCP(String codigoDNCP) {
        this.codigoDNCP = codigoDNCP;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
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

    public Inventarios getInventario() {
        return inventario;
    }

    public void setInventario(Inventarios inventario) {
        this.inventario = inventario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }

    public String getNroLlamado() {
        return nroLlamado;
    }

    public void setNroLlamado(String nroLlamado) {
        this.nroLlamado = nroLlamado;
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

    public Proveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedores proveedor) {
        this.proveedor = proveedor;
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

    public EntradasArticulo getEntradaArticulo() {
        return entradaArticulo;
    }

    public void setEntradaArticulo(EntradasArticulo entradaArticulo) {
        this.entradaArticulo = entradaArticulo;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public void setMarca(Marcas marca) {
        this.marca = marca;
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
        if (!(object instanceof DetallesEntradaArticulo)) {
            return false;
        }
        DetallesEntradaArticulo other = (DetallesEntradaArticulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return articulo.getDescripcion() + " (" + articulo.getUnidad().getSimbolo() + ")";
    }
   
}
