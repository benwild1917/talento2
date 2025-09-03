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
 * @author grecia
 */
@Entity
@Table(name = "usuario_tipos_documentos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioTiposDocumentos.findAll", query = "SELECT f FROM UsuarioTiposDocumentos f")
    , @NamedQuery(name = "UsuarioTiposDocumentos.findByPantalaInicial", query = "SELECT f FROM UsuarioTiposDocumentos f WHERE  f.usuarioTiposDocumentosPK.tipoDocumento = :tipoDocumento and f.usuarioTiposDocumentosPK.usuario = :usuario and f.estado.codigo ='AC'")
    , @NamedQuery(name = "UsuarioTiposDocumentos.findByEmpresa", query = "SELECT f FROM UsuarioTiposDocumentos f WHERE f.empresa = :empresa")})
public class UsuarioTiposDocumentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsuarioTiposDocumentosPK usuarioTiposDocumentosPK;
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
    @JoinColumn(name = "tipo_documento", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TiposDocumentosJudiciales tiposDocumentosJudiciales;
    @JoinColumn(name = "usuario", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuarios usuarios;


    public UsuarioTiposDocumentos() {
    }

    public UsuarioTiposDocumentos(UsuarioTiposDocumentosPK usuarioTiposDocumentosPK) {
        this.usuarioTiposDocumentosPK = usuarioTiposDocumentosPK;
    }

    public UsuarioTiposDocumentos(UsuarioTiposDocumentosPK usuarioTiposDocumentosPK, Empresas empresa) {
        this.usuarioTiposDocumentosPK = usuarioTiposDocumentosPK;
        this.empresa = empresa;
    }

    public UsuarioTiposDocumentos(String tipoDocumento, Integer usuario) {
        this.usuarioTiposDocumentosPK = new UsuarioTiposDocumentosPK( tipoDocumento, usuario);
    }

    public UsuarioTiposDocumentosPK getUsuarioTiposDocumentosPK() {
        return usuarioTiposDocumentosPK;
    }

    public void setUsuarioTiposDocumentosPK(UsuarioTiposDocumentosPK usuarioTiposDocumentosPK) {
        this.usuarioTiposDocumentosPK = usuarioTiposDocumentosPK;
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

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    public Usuarios getUsuarioUltimoEstado() {
        return usuarioUltimoEstado;
    }

    public void setUsuarioUltimoEstado(Usuarios usuarioUltimoEstado) {
        this.usuarioUltimoEstado = usuarioUltimoEstado;
    }

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    public TiposDocumentosJudiciales getTiposDocumentosJudiciales() {
        return tiposDocumentosJudiciales;
    }

    public void setTiposDocumentosJudiciales(TiposDocumentosJudiciales tiposDocumentosJudiciales) {
        this.tiposDocumentosJudiciales = tiposDocumentosJudiciales;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuarioTiposDocumentosPK != null ? usuarioTiposDocumentosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioTiposDocumentos)) {
            return false;
        }
        UsuarioTiposDocumentos other = (UsuarioTiposDocumentos) object;
        if ((this.usuarioTiposDocumentosPK == null && other.usuarioTiposDocumentosPK != null) || (this.usuarioTiposDocumentosPK != null && !this.usuarioTiposDocumentosPK.equals(other.usuarioTiposDocumentosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models13.UsuarioTiposDocumentos[ usuarioTiposDocumentosPK=" + usuarioTiposDocumentosPK + " ]";
    }
    
}
