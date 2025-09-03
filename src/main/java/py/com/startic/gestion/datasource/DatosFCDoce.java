/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.startic.gestion.datasource;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Notebook
 */
@Entity
@Table(name = "datosFCDoce")
@XmlRootElement
public class DatosFCDoce {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = true)
    @Column(name = "entradaSalida")
    private Integer entradaSalida;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = true)
    @Column(name = "idProcedenciaDestino")
    private Integer idProcedenciaDestino;
    @Basic(optional = true)
    @Column(name = "descripcionProcedenciaDestino")
    private String descripcionProcedenciaDestino;
    @Basic(optional = true)
    @Column(name = "cantidad")
    private Integer cantidad;
    @Basic(optional = true)
    @Column(name = "numero")
    private String numero;
     @Column(name = "sede")
    private String sede;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getEntradaSalida() {
        return entradaSalida;
    }

    public void setEntradaSalida(Integer entradaSalida) {
        this.entradaSalida = entradaSalida;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getIdProcedenciaDestino() {
        return idProcedenciaDestino;
    }

    public void setIdProcedenciaDestino(Integer idProcedenciaDestino) {
        this.idProcedenciaDestino = idProcedenciaDestino;
    }

    public String getDescripcionProcedenciaDestino() {
        return descripcionProcedenciaDestino;
    }

    public void setDescripcionProcedenciaDestino(String descripcionProcedenciaDestino) {
        this.descripcionProcedenciaDestino = descripcionProcedenciaDestino;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

}
