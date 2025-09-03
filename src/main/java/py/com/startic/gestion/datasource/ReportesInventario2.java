/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.datasource;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "reportes_inventario2")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReportesInventario2.findAll", query = "SELECT r FROM ReportesInventario2 r")
    , @NamedQuery(name = "ReportesInventario2.findByCodigo", query = "SELECT r FROM ReportesInventario2 r WHERE r.codigo = :codigo")
    , @NamedQuery(name = "ReportesInventario2.findByArticulo", query = "SELECT r FROM ReportesInventario2 r WHERE r.articulo = :articulo")
    , @NamedQuery(name = "ReportesInventario2.findBySede", query = "SELECT r FROM ReportesInventario2 r WHERE r.sede = :sede")
    , @NamedQuery(name = "ReportesInventario2.findByInventario", query = "SELECT r FROM ReportesInventario2 r WHERE r.inventario = :inventario ORDER BY r.descripcion")
    , @NamedQuery(name = "ReportesInventario2.findByFecha", query = "SELECT r FROM ReportesInventario2 r WHERE r.fecha = :fecha")
    , @NamedQuery(name = "ReportesInventario2.findByInventarioPorDias", query = "SELECT r FROM ReportesInventario2 r WHERE r.fecha >= :fechaDesde AND r.fecha < :fechaHasta AND r.sede in :sede  ORDER BY  r.fecha ASC")
    , @NamedQuery(name = "ReportesInventario2.findByDescripcion", query = "SELECT r FROM ReportesInventario2 r WHERE r.descripcion = :descripcion")
    , @NamedQuery(name = "ReportesInventario2.findBySimbolo", query = "SELECT r FROM ReportesInventario2 r WHERE r.simbolo = :simbolo")
    , @NamedQuery(name = "ReportesInventario2.findByCantInventario", query = "SELECT r FROM ReportesInventario2 r WHERE r.cantInventario = :cantInventario")
    , @NamedQuery(name = "ReportesInventario2.findByCantEntrada", query = "SELECT r FROM ReportesInventario2 r WHERE r.cantEntrada = :cantEntrada")
    , @NamedQuery(name = "ReportesInventario2.findByCantSalida", query = "SELECT r FROM ReportesInventario2 r WHERE r.cantSalida = :cantSalida")
    , @NamedQuery(name = "ReportesInventario2.findByCantBaja", query = "SELECT r FROM ReportesInventario2 r WHERE r.cantBaja = :cantBaja")
    , @NamedQuery(name = "ReportesInventario2.findByNeto", query = "SELECT r FROM ReportesInventario2 r WHERE r.neto = :neto")
    , @NamedQuery(name = "ReportesInventario2.findByStock", query = "SELECT r FROM ReportesInventario2 r WHERE r.stock = :stock")
    , @NamedQuery(name = "ReportesInventario2.findByArtInvFecha", query = "SELECT r FROM ReportesInventario2 r WHERE (r.articulo = :articulo or :articulo is null or :articulo = '') and r.inventario = :inventario  and r.fecha <= :fecha and r.neto <> 0")
})
public class ReportesInventario2 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Size(max = 22)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "articulo")
    private String articulo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "sede")
    private String sede;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "marca")
    private String marca;
    @Basic(optional = false)
    @NotNull
    @Column(name = "inventario")
    private int inventario;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = true)
    @Column(name = "vencimiento")
    private String vencimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "simbolo")
    private String simbolo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_inventario")
    private BigInteger cantInventario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_entrada")
    private BigInteger cantEntrada;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_baja")
    private BigInteger cantBaja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_salida")
    private BigInteger cantSalida;
    @Basic(optional = false)
    @NotNull
    @Column(name = "neto")
    private BigInteger neto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stock")
    private BigInteger stock;
    @Basic(optional = true)
    @Column(name = "precio_unitario")
    private BigDecimal precioUnitario;
    @Basic(optional = true)
    @Column(name = "precio_inicial")
    private BigDecimal precioInicial;
    @Basic(optional = true)
    @Column(name = "neto_valorizado")
    private BigDecimal netoValorizado;
    @Basic(optional = true)
    @Column(name = "oGasto")
    private Integer oGasto;

    public ReportesInventario2() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public int getInventario() {
        return inventario;
    }

    public void setInventario(int inventario) {
        this.inventario = inventario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public BigInteger getCantInventario() {
        return cantInventario;
    }

    public void setCantInventario(BigInteger cantInventario) {
        this.cantInventario = cantInventario;
    }

    public BigInteger getCantEntrada() {
        return cantEntrada;
    }

    public void setCantEntrada(BigInteger cantEntrada) {
        this.cantEntrada = cantEntrada;
    }

    public BigInteger getCantSalida() {
        return cantSalida;
    }

    public void setCantSalida(BigInteger cantSalida) {
        this.cantSalida = cantSalida;
    }

    public BigInteger getNeto() {
        return neto;
    }

    public void setNeto(BigInteger neto) {
        this.neto = neto;
    }

  

    public BigInteger getStock() {
        return stock;
    }

    public void setStock(BigInteger stock) {
        this.stock = stock;
    }
    

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getNetoValorizado() {
        return netoValorizado;
    }

    public void setNetoValorizado(BigDecimal netoValorizado) {
        this.netoValorizado = netoValorizado;
    }

    public Integer getoGasto() {
        return oGasto;
    }

    public void setoGasto(Integer oGasto) {
        this.oGasto = oGasto;
    }

    public BigInteger getCantBaja() {
        return cantBaja;
    }

    public void setCantBaja(BigInteger cantBaja) {
        this.cantBaja = cantBaja;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public BigDecimal getPrecioInicial() {
        return precioInicial;
    }

    public void setPrecioInicial(BigDecimal precioInicial) {
        this.precioInicial = precioInicial;
    }

   
    
}