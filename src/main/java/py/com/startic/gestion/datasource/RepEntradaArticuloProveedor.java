/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.startic.gestion.datasource;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Notebook
 */
public class RepEntradaArticuloProveedor {

    private String proveedor;
    private String nroLlamado;
    private String codigo;
    private Date fechaLlamado;
    private String nroFactura;
    private String articulo;
    private String marca;
    private Integer cantidad;
    private Date fechaEntrada;
    private BigDecimal precioUnitario;
    private BigDecimal importeTotal;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    
    
    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getNroLlamado() {
        return nroLlamado;
    }

    public void setNroLlamado(String nroLlamado) {
        this.nroLlamado = nroLlamado;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFechaLlamado() {
        return fechaLlamado;
    }

    public void setFechaLlamado(Date fechaLlamado) {
        this.fechaLlamado = fechaLlamado;
    }

    public String getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }
    

    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }

  
}
