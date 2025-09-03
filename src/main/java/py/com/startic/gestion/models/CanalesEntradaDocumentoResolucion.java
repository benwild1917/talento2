/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author grecia
 */
@Entity
@Table(name = "canales_entrada_documento_resolucion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CanalesEntradaDocumentoResolucion.findAll", query = "SELECT c FROM CanalesEntradaDocumentoResolucion c")
    , @NamedQuery(name = "CanalesEntradaDocumentoResolucion.findByCodigo", query = "SELECT c FROM CanalesEntradaDocumentoResolucion c WHERE c.codigo = :codigo")
    , @NamedQuery(name = "CanalesEntradaDocumentoResolucion.findByDescripcion", query = "SELECT c FROM CanalesEntradaDocumentoResolucion c WHERE c.descripcion = :descripcion")})
public class CanalesEntradaDocumentoResolucion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;

    public CanalesEntradaDocumentoResolucion() {
    }

    public CanalesEntradaDocumentoResolucion(String codigo) {
        this.codigo = codigo;
    }

    public CanalesEntradaDocumentoResolucion(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

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

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CanalesEntradaDocumentoResolucion)) {
            return false;
        }
        CanalesEntradaDocumentoResolucion other = (CanalesEntradaDocumentoResolucion) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models7.CanalesEntradaDocumentoResolucion[ codigo=" + codigo + " ]";
    }
    
}
