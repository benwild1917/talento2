/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.startic.gestion.datasource;


import java.util.Date;

/**
 *
 * @author Notebook
 */
public class RepSalidaArticuloDepositoSuma {
    
    private String codigo;
    private String articulo;
    private Integer cantidad; 
    private String deposito;
    
    public RepSalidaArticuloDepositoSuma(String codigo, String articulo, Integer cantidad, String deposito ){
        this.codigo = codigo;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.deposito = deposito;
    }

   
    
    
    public String getArticulo() {
        return articulo;
    }    

    public String getCodigo() {
        return codigo;
    }

    /**
     *
     * @return
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public String getDeposito() {
        return deposito;
    }

    public void setDeposito(String deposito) {
        this.deposito = deposito;
    }

   
   

    
}
