/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author grecia
 */
@Entity
@Table(name = "entradas_articulo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EntradasArticulo.findAll", query = "SELECT e FROM EntradasArticulo e ORDER BY e.fechaHoraAlta DESC")
    , @NamedQuery(name = "EntradasArticulo.findOrdered", query = "SELECT s FROM EntradasArticulo s WHERE s.fechaHoraAlta >= :fechaDesde AND s.fechaHoraAlta <= :fechaHasta ORDER BY s.fechaHoraAlta DESC")
    , @NamedQuery(name = "EntradasArticulo.findFechaEntradaOrdered", query = "SELECT s FROM EntradasArticulo s WHERE s.fechaEntrada >= :fechaDesde AND s.fechaEntrada <= :fechaHasta ORDER BY s.fechaEntrada DESC, s.fechaHoraAlta DESC")
    , @NamedQuery(name = "EntradasArticulo.findById", query = "SELECT e FROM EntradasArticulo e WHERE e.id = :id")
    , @NamedQuery(name = "EntradasArticulo.findByConcepto", query = "SELECT e FROM EntradasArticulo e WHERE e.concepto = :concepto")
    , @NamedQuery(name = "EntradasArticulo.findByNroFactura", query = "SELECT e FROM EntradasArticulo e WHERE e.nroFactura = :nroFactura")
    , @NamedQuery(name = "EntradasArticulo.findByNroLlamado", query = "SELECT e FROM EntradasArticulo e WHERE e.nroLlamado = :nroLlamado")
    , @NamedQuery(name = "EntradasArticulo.findByFechaHoraAlta", query = "SELECT e FROM EntradasArticulo e WHERE e.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "EntradasArticulo.findByFechaHoraUltimoEstado", query = "SELECT e FROM EntradasArticulo e WHERE e.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class EntradasArticulo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "concepto")
    private String concepto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nro_factura")
    private String nroFactura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nro_llamado")
    private String nroLlamado;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_llamado")
    @Temporal(TemporalType.DATE)
    private Date fechaLlamado;
    @Column(name = "fecha_entrada")
    @Temporal(TemporalType.DATE)
    private Date fechaEntrada;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "entradaArticulo")
    private Collection<DetallesEntradaArticulo> detallesEntradaArticuloCollection;
    @JoinColumn(name = "inventario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Inventarios inventario;
    @JoinColumn(name = "proveedor", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Proveedores proveedor;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "sede", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Sede sede;
    @JoinColumn(name = "articulo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Articulos articulo;
   
    
    @Column(name = "iva_5")
    private BigDecimal iva_5;
    @Column(name = "iva_10")
    private BigDecimal iva_10;
    
    @Column(name = "subtotal_exenta")
    private BigDecimal subtotal_exenta;
    @Column(name = "subtotal_5")
    private BigDecimal subtotal_5;
    @Column(name = "subtotal_10")
    private BigDecimal subtotal_10;
    @Column(name = "total_iva")
    private BigDecimal total_iva; 
    @Column(name = "total_factura")
    private BigDecimal total_factura;
 
 
 


    
    @JoinColumn(name = "usuario_borrado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioBorrado;
    @Column(name = "fecha_hora_borrado") // Este nombre tiene que se igual al nombre de la columna de la tabla 
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;   
   
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nro_remision")
    private String nroRemision;
    @NotNull
    @Column(name = "fecha_remision")
    @Temporal(TemporalType.DATE)
    private Date fechaRemision; 

    public String getNroRemision() {
        return nroRemision;
    }

    public void setNroRemision(String nroRemision) {
        this.nroRemision = nroRemision;
    }

    public Date getFechaRemision() {
        return fechaRemision;
    }

    public void setFechaRemision(Date fechaRemision) {
        this.fechaRemision = fechaRemision;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }


    
    public EntradasArticulo() {
    }

    public BigDecimal getIva_5() {
        return iva_5;
    }

    public void setIva_5(BigDecimal iva_5) {
        this.iva_5 = iva_5;
    }

    public BigDecimal getIva_10() {
        return iva_10;
    }

    public void setIva_10(BigDecimal iva_10) {
        this.iva_10 = iva_10;
    }

    public BigDecimal getSubtotal_exenta() {
        return subtotal_exenta;
    }

    public void setSubtotal_exenta(BigDecimal subtotal_exenta) {
        this.subtotal_exenta = subtotal_exenta;
    }

    public BigDecimal getSubtotal_5() {
        return subtotal_5;
    }

    public void setSubtotal_5(BigDecimal subtotal_5) {
        this.subtotal_5 = subtotal_5;
    }

    public BigDecimal getSubtotal_10() {
        return subtotal_10;
    }

    public void setSubtotal_10(BigDecimal subtotal_10) {
        this.subtotal_10 = subtotal_10;
    }

    public BigDecimal getTotal_iva() {
        return total_iva;
    }

    public void setTotal_iva(BigDecimal total_iva) {
        this.total_iva = total_iva;
    }

    public BigDecimal getTotal_factura() {
        return total_factura;
    }

    public void setTotal_factura(BigDecimal total_factura) {
        this.total_factura = total_factura;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }
    
    
    
    

    public EntradasArticulo(Integer id) {
        this.id = id;
    }

    public Usuarios getUsuarioBorrado() {
        return usuarioBorrado;
    }

    public void setUsuarioBorrado(Usuarios usuarioBorrado) {
        this.usuarioBorrado = usuarioBorrado;
    }

    public Date getFechaHoraBorrado() {
        return fechaHoraBorrado;
    }

    public void setFechaHoraBorrado(Date fechaHoraBorrado) {
        this.fechaHoraBorrado = fechaHoraBorrado;
    }

    public EntradasArticulo(Integer id, String concepto, String nroFactura, String nroLlamado, Date fechaHoraAlta, Date fechaHoraUltimoEstado, Usuarios usuarioBorrado ,Date fechaHoraBorrado) {
        this.id = id;
        this.concepto = concepto;
        this.nroFactura = nroFactura;
        this.nroLlamado = nroLlamado;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
        this.usuarioBorrado= usuarioBorrado; 
        this.fechaHoraBorrado=fechaHoraBorrado; 
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaLlamado() {
        return fechaLlamado;
    }

    public void setFechaLlamado(Date fechaLlamado) {
        this.fechaLlamado = fechaLlamado;
    }

    public Inventarios getInventario() {
        return inventario;
    }

    public void setInventario(Inventarios inventario) {
        this.inventario = inventario;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }

    public String getNroLlamado() {
        return nroLlamado;
    }

    public void setNroLlamado(String nroLlamado) {
        this.nroLlamado = nroLlamado;
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

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    @XmlTransient
    public Collection<DetallesEntradaArticulo> getDetallesEntradaArticuloCollection() {
        return detallesEntradaArticuloCollection;
    }

    public void setDetallesEntradaArticuloCollection(Collection<DetallesEntradaArticulo> detallesEntradaArticuloCollection) {
        this.detallesEntradaArticuloCollection = detallesEntradaArticuloCollection;
    }

    public Proveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedores proveedor) {
        this.proveedor = proveedor;
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

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
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
        if (!(object instanceof EntradasArticulo)) {
            return false;
        }
        EntradasArticulo other = (EntradasArticulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return concepto;
    }
}


