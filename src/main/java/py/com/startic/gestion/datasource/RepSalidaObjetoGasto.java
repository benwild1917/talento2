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
public class RepSalidaObjetoGasto {

   
    private Integer objetosGasto;
    private Integer nroFormulario;
    private String departamento;
    private String personaDestino;
    private String marca; 
    private String codArticulo; 
    private String articulo;
    private Integer cantidad;
    private Integer precio;
    private BigDecimal costoUnitario;
    private BigDecimal costoTotal;  
    private Integer total;
    private String modelo;
    private Date fechaSalida;

    public Integer getObjetosGasto() {
        return objetosGasto;
    }

    public void setObjetosGasto(Integer objetosGasto) {
        this.objetosGasto = objetosGasto;
    }

  
    public Date getFechaSalida() {
        return fechaSalida;
    }

    public Integer getNroFormulario() {
        return nroFormulario;
    }

    public void setNroFormulario(Integer nroFormulario) {
        this.nroFormulario = nroFormulario;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getPersonaDestino() {
        return personaDestino;
    }

    public void setPersonaDestino(String personaDestino) {
        this.personaDestino = personaDestino;
    }

    public String getCodArticulo() {
        return codArticulo;
    }

    public void setCodArticulo(String codArticulo) {
        this.codArticulo = codArticulo;
    }

   
    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }
    


    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

   

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
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


    
    
    
}
