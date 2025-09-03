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
public class RepCodArticulos {
    private String codigo;
    private String descripcion;
     private String marca;
    private Integer codigoOg;
    private String objetoGasto;
  

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getCodigoOg() {
        return codigoOg;
    }

    public void setCodigoOg(Integer codigoOg) {
        this.codigoOg = codigoOg;
    }

    public String getObjetoGasto() {
        return objetoGasto;
    }

    public void setObjetoGasto(String objetoGasto) {
        this.objetoGasto = objetoGasto;
    }

    public void setArticulo(String descripcion) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
   


}
