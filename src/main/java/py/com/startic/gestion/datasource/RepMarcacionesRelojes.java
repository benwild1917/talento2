/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.datasource;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
public class RepMarcacionesRelojes {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "ci")
    private String ci;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "nombres_apellidos")
    private String nombresApellidos;
    @Column(name = "minimo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date minimo;
    @Column(name = "maximo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date maximo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public Date getMinimo() {
        return minimo;
    }

    public void setMinimo(Date minimo) {
        this.minimo = minimo;
    }

    public Date getMaximo() {
        return maximo;
    }

    public void setMaximo(Date maximo) {
        this.maximo = maximo;
    }
    
}
