/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.startic.gestion.datasource;

import java.math.BigDecimal;
import java.util.Date;
import py.com.startic.gestion.models.ObjetosGasto;

/**
 *
 * @author Notebook
 */
public class RepEntradasArticuloPorDeposito {
    private String codArti;
    private String articulo;
    private String deposito;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal precioTotal;
    private String nLlamado; 
    private Integer oG;
    private Date fEntrada; 
    private String nFactura;
    private String proveedor;
    private String usuario;
    private String vencimiento; 

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

     
    private BigDecimal importeTotal;

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public String getCodArti() {
        return codArti;
    }

    public void setCodArti(String codArti) {
        this.codArti = codArti;
    }

    public String getDeposito() {
        return deposito;
    }

    public void setDeposito(String deposito) {
        this.deposito = deposito;
    }

   
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    
    public String getnLlamado() {
        return nLlamado;
    }

    public void setnLlamado(String nLlamado) {
        this.nLlamado = nLlamado;
    }

    public Integer getoG() {
        return oG;
    }

    public void setoG(Integer oG) {
        this.oG = oG;
    }

    public Date getfEntrada() {
        return fEntrada;
    }

    public void setfEntrada(Date fEntrada) {
        this.fEntrada = fEntrada;
    }

    public String getnFactura() {
        return nFactura;
    }

    public void setnFactura(String nFactura) {
        this.nFactura = nFactura;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }
   

}
