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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "articulos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Articulos.findAll", query = "SELECT a FROM Articulos a")
    , @NamedQuery(name = "Articulos.findOrdered", query = "SELECT a FROM Articulos a ORDER BY a.descripcion")
    , @NamedQuery(name = "Articulos.findByCodigo", query = "SELECT a FROM Articulos a WHERE a.codigo = :codigo")
    , @NamedQuery(name = "Articulos.findByCodigoStock", query = "SELECT a FROM Articulos a WHERE a.stock < 0")
    , @NamedQuery(name = "Articulos.findByMarcas", query = "SELECT a FROM Articulos a WHERE a.marca = :marca")
    , @NamedQuery(name = "Articulos.findByDescripcion", query = "SELECT a FROM Articulos a WHERE a.descripcion = :descripcion")
    , @NamedQuery(name = "Articulos.findByStock", query = "SELECT a FROM Articulos a WHERE a.stock = :stock")
    ,@NamedQuery(name = "Articulos.findByDeposito", query = "SELECT a FROM Articulos a WHERE a.rubro.sede = :sede  ")
    ,@NamedQuery(name = "Articulos.findByPorUsuario", query = "SELECT a FROM Articulos a WHERE a.rubro = :rubro") 
     ,@NamedQuery(name = "Articulos.findByListaArticulos", query = "SELECT a FROM Articulos a WHERE a.rubro in (SELECT  u.usuarios FROM RolesPorUsuarios u WHERE u.roles in (SELECT r.rubro FROM RubroPorUsuario r WHERE r.usuario = :usuario  ) AND u.usuarios.estado.codigo = 'AC')  ORDER BY a.descripcion")
    ,@NamedQuery(name = "Articulos.findByVerImagen", query = "SELECT a FROM Articulos a WHERE a.imagen = :imagen")
    ,@NamedQuery(name = "Articulos.findBySede", query = "SELECT a FROM Articulos a WHERE a.sede = :sede")
    , @NamedQuery(name = "Articulos.findByStockCritico", query = "SELECT a FROM Articulos a WHERE a.stockCritico >= a.stock")
    , @NamedQuery(name = "Articulos.findByFechaHoraAlta", query = "SELECT a FROM Articulos a WHERE a.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "Articulos.findByFechaHoraUltimoEstado", query = "SELECT a FROM Articulos a WHERE a.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")
    , @NamedQuery(name = "Articulos.findByFechaBusqueda", query = "SELECT a FROM Articulos a WHERE a.fechaHoraAlta  >= :fechaDesde and a.fechaHoraAlta <= :fechaHasta  and a.rubro.sede = :sede")
    , @NamedQuery(name = "Articulos.findByListaArticulo", query = "SELECT a FROM Articulos a WHERE a in :articulos")    
    , @NamedQuery(name = "Articulos.findByEmpresa", query = "SELECT a FROM Articulos a WHERE a.empresa = :empresa")})
public class Articulos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "codigo")
    private String codigo;
    @ManyToOne(optional = true)
    @JoinColumn(name = "marca", referencedColumnName = "id") // Los atributos que corresponden a tablas usan @JoinColumn en vez de @Column
    private Marcas marca;
    @ManyToOne(optional = true)
    @JoinColumn(name = "modelo", referencedColumnName = "id")
    private Modelos modelo; 
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = true) // Le ponemos TRUE a este para que sea opcional. Le sacamos tambien el @NotNull
    @Size(max = 200) // Le sacamos el min para que no tenga una cantidad minima de caracteres y pueda ser opcional
    @Column(name = "codigo_suministro")
    private String codigoSuministro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stock")
    private int stock;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stock_critico")
    private int stockCritico;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = false)
    @Lob
    @Column(name = "imagen")
    private byte[] imagen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "unidad", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Unidades unidad;
    @JoinColumn(name = "rubro", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Rubros rubro;
    @ManyToOne(optional = true) // Este debe ir en TRUE si queres que sea opcional, o sea, que en la tabla permita NULL's
    @JoinColumn(name = "objeto_gasto", referencedColumnName = "id") // Este @JoinColumn siempre tiene que tener cuando es una columna de una tabla. Y los nombres de columna deben estar separados por _ si tiene mas de una palabra
    private ObjetosGasto objetoGasto; // El nombre debe ser en singular y con la primera letra en mayuscula si tiene mas de una palabra
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "articulo")
    private Collection<DetallesSalidaArticulo> detallesSalidaArticuloCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "articulo")
    private Collection<DetallesEntradaArticulo> detallesEntradaArticuloCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "articulo")
    private Collection<DetallesInventario> detallesInventarioCollection;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    
    @JoinColumn(name = "usuario_borrado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioBorrado;
    @Column(name = "fecha_hora_borrado") // Este nombre tiene que se igual al nombre de la columna de la tabla 
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado; 
    @JoinColumn(name = "sede", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Sede sede;
   

    public String getCodigoSuministro() {
        return codigoSuministro;
    }

    public void setCodigoSuministro(String codigoSuministro) {
        this.codigoSuministro = codigoSuministro;
    }
    
    public Articulos() {
    }

    public Articulos(String codigo) {
        this.codigo = codigo;
    }

    public Marcas getMarca() {
        return marca;
    }

    public void setMarca(Marcas marca) {
        this.marca = marca;
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

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
    

    
    
    public Articulos(String codigo, String descripcion, int stock, Marcas marca, int stockCritico, Date fechaHoraAlta, Date fechaHoraUltimoEstado, String empresa,  Usuarios usuarioBorrado ,Date fechaHoraBorrado) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.stock = stock;
        this.marca = marca;
        this.stockCritico = stockCritico;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
        this.usuarioBorrado= usuarioBorrado; 
        this.fechaHoraBorrado=fechaHoraBorrado; 
    }

    public Modelos getModelo() {
        return modelo;
    }

    public void setModelo(Modelos modelo) {
        this.modelo = modelo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Rubros getRubro() {
        return rubro;
    }

    public void setRubro(Rubros rubro) {
        this.rubro = rubro;
    }

    public ObjetosGasto getObjetoGasto() {
        return objetoGasto;
    }

    public void setObjetoGasto(ObjetosGasto objetoGasto) {
        this.objetoGasto = objetoGasto;
    }
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Unidades getUnidad() {
        return unidad;
    }

    public void setUnidad(Unidades unidad) {
        this.unidad = unidad;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStockCritico() {
        return stockCritico;
    }

    public void setStockCritico(int stockCritico) {
        this.stockCritico = stockCritico;
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

    public Collection<DetallesInventario> getDetallesInventarioCollection() {
        return detallesInventarioCollection;
    }

    @XmlTransient
    public void setDetallesInventarioCollection(Collection<DetallesInventario> detallesInventarioCollection) {
        this.detallesInventarioCollection = detallesInventarioCollection;
    }

    @XmlTransient
    public Collection<DetallesSalidaArticulo> getDetallesSalidaArticuloCollection() {
        return detallesSalidaArticuloCollection;
    }

    public void setDetallesSalidaArticuloCollection(Collection<DetallesSalidaArticulo> detallesSalidaArticuloCollection) {
        this.detallesSalidaArticuloCollection = detallesSalidaArticuloCollection;
    }
    
    @XmlTransient
    public Collection<DetallesEntradaArticulo> getDetallesEntradaArticuloCollection() {
        return detallesEntradaArticuloCollection;
    }

    public void setDetallesEntradaArticuloCollection(Collection<DetallesEntradaArticulo> detallesEntradaArticuloCollection) {
        this.detallesEntradaArticuloCollection = detallesEntradaArticuloCollection;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Articulos)) {
            return false;
        }
        Articulos other = (Articulos) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descripcion;
    }
    
    @XmlTransient
    public String getDescripcionLarga() {
        // return descripcion + " (" + stock + " " + unidad.getSimbolo() + ")";
        return descripcion;
    }

    

}
