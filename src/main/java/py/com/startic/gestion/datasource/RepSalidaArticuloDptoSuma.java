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
public class RepSalidaArticuloDptoSuma {
    
    private String codigo;
    private String articulo;
    private Integer cantidad; 
    private String departamento;
    
    public RepSalidaArticuloDptoSuma(String codigo, String articulo, Integer cantidad, String departamento ){
        this.codigo = codigo;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.departamento = departamento;
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
    
    public String getDepartamento(){
        return this.departamento;
    }
    
    public void setDepartamento(String departamento){
        this.departamento = departamento;
    }

    
}
