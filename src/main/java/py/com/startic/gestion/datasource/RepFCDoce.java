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
public class RepFCDoce {
    private String numero;
    private Date fecha;
    private BigDecimal precioPromedio;
    private BigDecimal precioInv;
    private String procedencia;
    private Integer entradas;
    private Integer salidas;
    private Integer invInicial;
    private Integer saldo;
    private BigDecimal vEntradas;
    private BigDecimal vInventarios;
    private BigDecimal vSalidas;
    private BigDecimal vSaldo;
    private String tipo;
    private String sede;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getPrecioPromedio() {
        return precioPromedio;
    }

    public void setPrecioPromedio(BigDecimal precioPromedio) {
        this.precioPromedio = precioPromedio;
    }

    public String getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(String procedencia) {
        this.procedencia = procedencia;
    }

    public Integer getEntradas() {
        return entradas;
    }

    public void setEntradas(Integer entradas) {
        this.entradas = entradas;
    }

    public Integer getSalidas() {
        return salidas;
    }

    public void setSalidas(Integer salidas) {
        this.salidas = salidas;
    }

    public Integer getSaldo() {
        return saldo;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getvEntradas() {
        return vEntradas;
    }

    public void setvEntradas(BigDecimal vEntradas) {
        this.vEntradas = vEntradas;
    }

    public BigDecimal getvSalidas() {
        return vSalidas;
    }

    public void setvSalidas(BigDecimal vSalidas) {
        this.vSalidas = vSalidas;
    }

    public BigDecimal getvSaldo() {
        return vSaldo;
    }

    public void setvSaldo(BigDecimal vSaldo) {
        this.vSaldo = vSaldo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public Integer getInvInicial() {
        return invInicial;
    }

    public void setInvInicial(Integer invInicial) {
        this.invInicial = invInicial;
    }

    public BigDecimal getPrecioInv() {
        return precioInv;
    }

    public void setPrecioInv(BigDecimal precioInv) {
        this.precioInv = precioInv;
    }

  
    public BigDecimal getvInventarios() {
        return vInventarios;
    }

    public void setvInventarios(BigDecimal vInventarios) {
        this.vInventarios = vInventarios;
    }

   

    
    
}
