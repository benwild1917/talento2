/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author eduardo
 */
@Embeddable
public class RubroPorUsuarioPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "rubro")
    private int rubro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usuario")
    private int usuario;

    public RubroPorUsuarioPK() {
    }

    public RubroPorUsuarioPK(int rubro, int usuario) {
        this.rubro = rubro;
        this.usuario = usuario;
    }

    public int getRubro() {
        return rubro;
    }

    public void setRubro(int rubro) {
        this.rubro = rubro;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) rubro;
        hash += (int) usuario;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RubroPorUsuarioPK)) {
            return false;
        }
        RubroPorUsuarioPK other = (RubroPorUsuarioPK) object;
        if (this.rubro != other.rubro) {
            return false;
        }
        if (this.usuario != other.usuario) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.RubroPorUsuarioPK[ rubro=" + rubro + ", rol=" + usuario + " ]";
    }
    
}
