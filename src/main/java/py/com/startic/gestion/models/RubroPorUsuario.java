/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ...
 */
@Entity
@Table(name = "rubro_por_usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RubroPorUsuario.findAll", query = "SELECT r FROM RubroPorUsuario r")
    , @NamedQuery(name = "RubroPorUsuario.findByFechaHoraAlta", query = "SELECT r FROM RubroPorUsuario r WHERE r.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "RubroPorUsuario.findByFechaHoraUltimoEstado", query = "SELECT r FROM RubroPorUsuario r WHERE r.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class RubroPorUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RubroPorUsuarioPK rubroPorUsuarioPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Estados estado;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "usurio", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuarios usuario;
    @JoinColumn(name = "rubro", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Rubros rubro;

    public RubroPorUsuario() {
    }

    public RubroPorUsuario(RubroPorUsuarioPK rubroPorUsuarioPK) {
        this.rubroPorUsuarioPK = rubroPorUsuarioPK;
    }

    public RubroPorUsuario(RubroPorUsuarioPK rubroPorUsuarioPK, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.rubroPorUsuarioPK = rubroPorUsuarioPK;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public RubroPorUsuario(int rubro, int rol) {
        this.rubroPorUsuarioPK = new RubroPorUsuarioPK(rubro, rol);
    }

    public RubroPorUsuarioPK getRubroRolesPK() {
        return rubroPorUsuarioPK;
    }

    public void setRubroRolesPK(RubroPorUsuarioPK rubroRolesPK) {
        this.rubroPorUsuarioPK = rubroRolesPK;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Date getFechaHoraUltimoEstado() {
        return fechaHoraUltimoEstado;
    }

    public void setFechaHoraUltimoEstado(Date fechaHoraUltimoEstado) {
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Usuarios getUsuarioUltimoEstado() {
        return usuarioUltimoEstado;
    }

    public void setUsuarioUltimoEstado(Usuarios usuarioUltimoEstado) {
        this.usuarioUltimoEstado = usuarioUltimoEstado;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

  

    public Rubros getRubro() {
        return rubro;
    }

    public void setRubro(Rubros rubro) {
        this.rubro = rubro;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rubroPorUsuarioPK != null ? rubroPorUsuarioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RubroPorUsuario)) {
            return false;
        }
        RubroPorUsuario other = (RubroPorUsuario) object;
        if ((this.rubroPorUsuarioPK == null && other.rubroPorUsuarioPK != null) || (this.rubroPorUsuarioPK != null && !this.rubroPorUsuarioPK.equals(other.rubroPorUsuarioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.RubroPorUsuario[ rubroPorUsuarioPK=" + rubroPorUsuarioPK + " ]";
    }
    
}
