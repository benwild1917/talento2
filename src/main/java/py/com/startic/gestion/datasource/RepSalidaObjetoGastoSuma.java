/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.startic.gestion.datasource;

/**
 *
 * @author Notebook
 */
public class RepSalidaObjetoGastoSuma {
    
    private String codArticulo;
    private String articulo;
    private Integer cantidad; 
    private Integer objetosGasto;
    private String oGDescripcion;

    
public RepSalidaObjetoGastoSuma(String codArticulo, String articulo, Integer cantidad, Integer objetosGasto,String oGDescripcion){
        this.codArticulo = codArticulo;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.objetosGasto = objetosGasto;
        this.oGDescripcion = oGDescripcion;
    }

     
   
    public String getCodArticulo() {
        return codArticulo;
    }

    public void setCodArticulo(String codArticulo) {
        this.codArticulo = codArticulo;
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

    public Integer getObjetosGasto() {
        return objetosGasto;
    }

    public void setObjetosGasto(Integer objetosGasto) {
        this.objetosGasto = objetosGasto;
    }

    public String getoGDescripcion() {
        return oGDescripcion;
    }

    public void setoGDescripcion(String oGDescripcion) {
        this.oGDescripcion = oGDescripcion;
    }
    
    
  
    
}
