/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.startic.gestion.datasource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author Notebook
 */
public class RepInventariosPorDias {

    private String codigo;
    private String articulo;
    private String sede;
    private String marca;
    private int inventario;
    private Date fecha;
    private String vencimiento;
    private String descripcion;
    private String simbolo;
    private BigInteger cantInventario;
    private BigInteger cantEntrada;
    private BigInteger cantBaja;
    private BigInteger cantSalida;
    private BigInteger neto;
    private BigDecimal precioUnitario;
    private BigDecimal netoValorizado;
    private Integer oGasto;

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

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
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

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
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

    public BigInteger getCantBaja() {
        return cantBaja;
    }

    public void setCantBaja(BigInteger cantBaja) {
        this.cantBaja = cantBaja;
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
    

}
