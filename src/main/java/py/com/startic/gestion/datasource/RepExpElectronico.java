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
public class RepExpElectronico {
    private Date fecha;
    private String accion;
     private String personal;
    private Integer observacion;

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public Integer getObservacion() {
        return observacion;
    }

    public void setObservacion(Integer observacion) {
        this.observacion = observacion;
    }
   
    
}
