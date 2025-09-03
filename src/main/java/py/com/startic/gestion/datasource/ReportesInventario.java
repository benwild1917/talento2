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
@Table(name = "reportes_inventario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReportesInventario.findAll", query = "SELECT r FROM ReportesInventario r")
    , @NamedQuery(name = "ReportesInventario.findByCodigo", query = "SELECT r FROM ReportesInventario r WHERE r.codigo = :codigo")
    , @NamedQuery(name = "ReportesInventario.findByArticulo", query = "SELECT r FROM ReportesInventario r WHERE r.articulo = :articulo")
    , @NamedQuery(name = "ReportesInventario.findBySede", query = "SELECT r FROM ReportesInventario r WHERE r.sede = :sede")
    , @NamedQuery(name = "ReportesInventario.findByInventario", query = "SELECT r FROM ReportesInventario r WHERE r.inventario = :inventario ORDER BY r.descripcion")
    , @NamedQuery(name = "ReportesInventario.findByFecha", query = "SELECT r FROM ReportesInventario r WHERE r.fecha = :fecha")
    , @NamedQuery(name = "ReportesInventario.findByInventarioPorDias", query = "SELECT r FROM ReportesInventario r WHERE r.fecha >= :fechaDesde AND r.fecha < :fechaHasta AND r.sede in :sede  ORDER BY  r.fecha ASC")
    , @NamedQuery(name = "ReportesInventario.findByDescripcion", query = "SELECT r FROM ReportesInventario r WHERE r.descripcion = :descripcion")
    , @NamedQuery(name = "ReportesInventario.findBySimbolo", query = "SELECT r FROM ReportesInventario r WHERE r.simbolo = :simbolo")
    , @NamedQuery(name = "ReportesInventario.findByCantInventario", query = "SELECT r FROM ReportesInventario r WHERE r.cantInventario = :cantInventario")
    , @NamedQuery(name = "ReportesInventario.findByCantEntrada", query = "SELECT r FROM ReportesInventario r WHERE r.cantEntrada = :cantEntrada")
    , @NamedQuery(name = "ReportesInventario.findByCantSalida", query = "SELECT r FROM ReportesInventario r WHERE r.cantSalida = :cantSalida")
    , @NamedQuery(name = "ReportesInventario.findByCantBaja", query = "SELECT r FROM ReportesInventario r WHERE r.cantBaja = :cantBaja")
    , @NamedQuery(name = "ReportesInventario.findByNeto", query = "SELECT r FROM ReportesInventario r WHERE r.neto = :neto")
    , @NamedQuery(name = "ReportesInventario.findByStock", query = "SELECT r FROM ReportesInventario r WHERE r.stock = :stock")})
public class ReportesInventario implements Serializable {

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

    public ReportesInventario() {
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
