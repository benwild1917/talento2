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
public class RepSalidaArticuloPorDeposito {
    
    private String codigo;
    private String Articulo;
    private String deposito;
    private Date fechaSalida;
    private Integer nroFormulario;
    private Integer cantidad; 
    private String usuarioSalida;
    private String marca;
    private String departamento;
    private String vencimiento;
    private BigDecimal costoUnitario;
    private BigDecimal costoTotal;
    private String usuarioPedido;
    private String usuarioRetiro;
    private String usuarioAutoriza;


    public String getArticulo() {
        return Articulo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setArticulo(String Articulo) {
        this.Articulo = Articulo;
    }

    public String getDeposito() {
        return deposito;
    }

    public void setDeposito(String deposito) {
        this.deposito = deposito;
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

    public String getUsuarioSalida() {
        return usuarioSalida;
    }

    public void setUsuarioSalida(String usuarioSalida) {
        this.usuarioSalida = usuarioSalida;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
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

    public String getUsuarioPedido() {
        return usuarioPedido;
    }

    public void setUsuarioPedido(String usuarioPedido) {
        this.usuarioPedido = usuarioPedido;
    }

    public String getUsuarioRetiro() {
        return usuarioRetiro;
    }

    public void setUsuarioRetiro(String usuarioRetiro) {
        this.usuarioRetiro = usuarioRetiro;
    }

    public String getUsuarioAutoriza() {
        return usuarioAutoriza;
    }

    public void setUsuarioAutoriza(String usuarioAutoriza) {
        this.usuarioAutoriza = usuarioAutoriza;
    }

   
    
    
}
