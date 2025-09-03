/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.startic.gestion.datasource;


import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Notebook
 */
@Entity
public class RepInventarioValorizadoSuma {
    @Id
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "articulo")
    private String articulo;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "marca")
    private String marca;
     @Column(name = "sede")
    private String sede;
    @Column(name = "oGasto")
    private Integer oGasto;
    @Column(name = "objetoGasto")
    private String objetoGasto;  
    @Column(name = "vencimiento")
    private String vencimiento;
    @Column(name = "costo_unitario")
    private BigDecimal costoUnitario;
    @Column(name = "suma_cant_entrada")
    private BigDecimal sumaCantEntrada;
    @Column(name = "suma_cant_salida")
    private BigDecimal sumaCantSalida;
    @Column(name = "suma_saldo")
    private BigDecimal sumaSaldo;
    @Column(name = "saldo_valorizado")
    private BigDecimal saldoValorizado;
    @Column(name = "simbolo")
    private String simbolo;

    public RepInventarioValorizadoSuma(ReporteInventarioValorizadoAgregado rep){
        this.articulo = rep.getArticulo().getCodigo();
        this.descripcion = rep.getArticulo().getDescripcion();
        this.marca = rep.getMarca();
        this.oGasto = rep.getoGasto();
        this.objetoGasto = rep.getObjetoGasto();
        this.costoUnitario = rep.getCostoUnitario();
        this.vencimiento = rep.getFechaVencimientoString();
        this.sumaCantEntrada = new BigDecimal(rep.getCantEntrada());
        this.sumaCantSalida = new BigDecimal(rep.getCantSalida());
        this.sumaSaldo = this.sumaCantEntrada.subtract(sumaCantSalida);
        this.saldoValorizado = rep.getValorizado();
        this.simbolo = rep.getSimbolo();
       // this.sede = rep.getSede().getDescripcion();
       
    }

    public RepInventarioValorizadoSuma(){
        
    }
   

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getoGasto() {
        return oGasto;
    }

    public void setoGasto(Integer oGasto) {
        this.oGasto = oGasto;
    }

    public String getObjetoGasto() {
        return objetoGasto;
    }

    public void setObjetoGasto(String objetoGasto) {
        this.objetoGasto = objetoGasto;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public BigDecimal getSumaCantEntrada() {
        return sumaCantEntrada;
    }

    public void setSumaCantEntrada(BigDecimal sumaCantEntrada) {
        this.sumaCantEntrada = sumaCantEntrada;
    }

    public BigDecimal getSumaCantSalida() {
        return sumaCantSalida;
    }

    public void setSumaCantSalida(BigDecimal sumaCantSalida) {
        this.sumaCantSalida = sumaCantSalida;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public BigDecimal getSumaSaldo() {
        return sumaSaldo;
    }

    public void setSumaSaldo(BigDecimal sumaSaldo) {
        this.sumaSaldo = sumaSaldo;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public BigDecimal getSaldoValorizado() {
        return saldoValorizado;
    }

    public void setSaldoValorizado(BigDecimal saldoValorizado) {
        this.saldoValorizado = saldoValorizado;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

  

}
