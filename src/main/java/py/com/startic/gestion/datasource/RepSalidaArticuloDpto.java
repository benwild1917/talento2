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
public class RepSalidaArticuloDpto {
    
    private String Codigo;
    private String Articulo;
    private String Marca;
    private Date fechaSalida;
    private Integer nroFormulario;
    private Integer cantidad; 
    private String departamento;
    private String usuarioSalida;

    public String getArticulo() {
        return Articulo;
    }

  
    

    public String getCodigo() {
        return Codigo;
    }

    /**
     *
     * @return
     */
    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    public void setArticulo(String Articulo) {
        this.Articulo = Articulo;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String Marca) {
        this.Marca = Marca;
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

   
    
    
}
