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
public class RepSalidaArticulo {
    
    private String codigo;
    private String Articulo;
    private Date fechaSalida;
    private String vencimiento;
    private Integer nroFormulario;
    private String marca;
    private Integer cantidad;
    private BigDecimal costoUnitario;
    private BigDecimal costoTotal;
    private String departamento;
    private String usuarioSalida;
    private String usuarioPedido;
    private String deposito;

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    
    public String getArticulo() {
        return Articulo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setArticulo(String Articulo) {
        this.Articulo = Articulo;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

   
    public Integer getNroFormulario() {
        return nroFormulario;
    }

    public void setNroFormulario(Integer nroFormulario) {
        this.nroFormulario = nroFormulario;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getUsuarioSalida() {
        return usuarioSalida;
    }

    public void setUsuarioSalida(String usuarioSalida) {
        this.usuarioSalida = usuarioSalida;
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

    public String getDeposito() {
        return deposito;
    }

    public void setDeposito(String deposito) {
        this.deposito = deposito;
    }

    public String getUsuarioPedido() {
        return usuarioPedido;
    }

    public void setUsuarioPedido(String usuarioPedido) {
        this.usuarioPedido = usuarioPedido;
    }
    
   
}
