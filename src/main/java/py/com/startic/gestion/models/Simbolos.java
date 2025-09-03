/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "simbolos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Simbolos.findAll", query = "SELECT g FROM Simbolos g")
    , @NamedQuery(name = "Simbolos.findById", query = "SELECT g FROM Simbolos g WHERE g.id = :id")
    , @NamedQuery(name = "Simbolos.findBySignificado", query = "SELECT g FROM Simbolos g WHERE g.significado = :significado")})
public class Simbolos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "significado")
    private String significado;
   
     public Simbolos() {
    }

    public Simbolos(Integer id) {
        this.id = id;
    }
     public Simbolos(Integer id, String significado) {
        this.id = id;
        this.significado= significado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSignificado() {
        return significado;
    }

    public void setSignificado(String significado) {
        this.significado = significado;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Simbolos)) {
            return false;
        }
        Simbolos other = (Simbolos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.Simbolos[ id=" + id + " ]";
    }
    
}
