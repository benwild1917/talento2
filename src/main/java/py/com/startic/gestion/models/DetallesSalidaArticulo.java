/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author grecia
 */
@Entity
@Table(name = "detalles_salida_articulo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetallesSalidaArticulo.findAll", query = "SELECT d FROM DetallesSalidaArticulo d")
    , @NamedQuery(name = "DetallesSalidaArticulo.findById", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.id = :id")
    , @NamedQuery(name = "DetallesSalidaArticulo.findByCantidad", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.cantidad = :cantidad")
    , @NamedQuery(name = "DetallesSalidaArticulo.findByNroFormulario", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.nroFormulario = :nroFormulario")
    , @NamedQuery(name = "DetallesSalidaArticulo.findByObjetoGasto", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.objetoGasto = :objetoGasto")
    , @NamedQuery(name = "DetallesSalidaArticulo.findByFechaVencimiento", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.fechaVencimiento = :fechaVencimiento")
    , @NamedQuery(name = "DetallesSalidaArticulo.findByCodigoSuministro", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.codigoSuministro = :codigoSuministro")
    , @NamedQuery(name = "DetallesSalidaArticulo.findByCodigoDNCP", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.codigoDNCP = :codigoDNCP")
    , @NamedQuery(name = "DetallesSalidaArticulo.findBySalidaArticulo", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.salidaArticulo = :salidaArticulo ORDER BY d.item")
    , @NamedQuery(name = "DetallesSalidaArticulo.findByArticulo", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.articulo = :articulo")
    , @NamedQuery(name = "DetallesSalidaArticulo.findByArticuloFechaAlta", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.fechaHoraAlta > :fechaAltaDesde AND d.fechaHoraAlta <= :fechaAltaHasta AND d.articulo = :articulo ORDER BY d.fechaHoraAlta")
    , @NamedQuery(name = "DetallesSalidaArticulo.findByArticuloFechaSalida", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.salidaArticulo.fechaSalida >= :fechaSalidaDesde AND d.salidaArticulo.fechaSalida < :fechaSalidaHasta AND d.articulo = :articulo ORDER BY d.salidaArticulo.fechaSalida, d.fechaHoraAlta")
    , @NamedQuery(name = "DetallesSalidaArticulo.findByListaArticuloFechaSalida", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.salidaArticulo.fechaSalida >= :fechaSalidaDesde AND d.salidaArticulo.fechaSalida < :fechaSalidaHasta AND d.articulo in :articulo ORDER BY d.salidaArticulo.fechaSalida, d.fechaHoraAlta")     
    , @NamedQuery(name = "DetallesSalidaArticulo.findByObjetosGastoFechaSalida", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.salidaArticulo.fechaSalida >= :fechaSalidaDesde AND d.salidaArticulo.fechaSalida < :fechaSalidaHasta AND d.articulo.objetoGasto = :objetosGasto ORDER BY d.salidaArticulo.fechaSalida, d.fechaHoraAlta")
    , @NamedQuery(name = "DetallesSalidaArticulo.findByListaObjetosGastoFechaSalida", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.salidaArticulo.fechaSalida >= :fechaSalidaDesde AND d.salidaArticulo.fechaSalida < :fechaSalidaHasta AND d.articulo.objetoGasto in :objetosGasto ORDER BY d.salidaArticulo.fechaSalida, d.fechaHoraAlta")
    , @NamedQuery(name = "DetallesSalidaArticulo.findByNroFormulario2", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.salidaArticulo.fechaSalida >= :fechaSalidaDesde AND d.salidaArticulo.fechaSalida < :fechaSalidaHasta AND d.nroFormulario in :nroFormulario ORDER BY d.salidaArticulo.fechaSalida, d.fechaHoraAlta")
    , @NamedQuery(name = "DetallesSalidaArticulo.findByDptoFechaAlta", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.fechaHoraAlta > :fechaAltaDesde AND d.fechaHoraAlta <= :fechaAltaHasta AND d.departamento = :departamento ORDER BY d.fechaHoraAlta")
    , @NamedQuery(name = "DetallesSalidaArticulo.findByProveedorFechaSalida", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.salidaArticulo.fechaSalida >= :fechaSalidaDesde AND d.salidaArticulo.fechaSalida < :fechaSalidaHasta AND d.entradaArticulo.proveedor in :proveedor ORDER BY d.salidaArticulo.fechaSalida, d.fechaHoraAlta")
    , @NamedQuery(name = "DetallesSalidaArticulo.findByDptoFechaSalida", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.salidaArticulo.fechaSalida >= :fechaSalidaDesde AND d.salidaArticulo.fechaSalida < :fechaSalidaHasta AND d.departamento = :departamento ORDER BY d.salidaArticulo.fechaSalida, d.fechaHoraAlta")
    , @NamedQuery(name = "DetallesSalidaArticulo.findByListaDptoFechaSalida", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.salidaArticulo.fechaSalida >= :fechaSalidaDesde AND d.salidaArticulo.fechaSalida < :fechaSalidaHasta AND d.departamento in :departamento ORDER BY d.salidaArticulo.fechaSalida, d.fechaHoraAlta")     
    , @NamedQuery(name = "DetallesSalidaArticulo.findByFechaHoraAlta", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "DetallesSalidaArticulo.findByBuscarNroFormulario", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.salidaArticulo.fechaSalida >= :fechaSalidaDesde AND d.salidaArticulo.fechaSalida < :fechaSalidaHasta AND d.nroFormulario = :nroFormulario") 
    , @NamedQuery(name = "DetallesSalidaArticulo.findByListaDepositoFechaSalida", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.salidaArticulo.fechaSalida >= :fechaSalidaDesde AND d.salidaArticulo.fechaSalida < :fechaSalidaHasta AND d.sede in :sede ORDER BY d.salidaArticulo.fechaSalida, d.fechaHoraAlta")     
    , @NamedQuery(name = "DetallesSalidaArticulo.findByFechaHoraUltimoEstado", query = "SELECT d FROM DetallesSalidaArticulo d WHERE d.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class DetallesSalidaArticulo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private int cantidad;
    @Basic(optional = true)
    @Column(name = "item")
    private int item;
    @Basic(optional = false)
    @NotNull
    //@Size(min = 1, max = 40)
    @Column(name = "nro_formulario")
    private Integer nroFormulario;
    
    @Basic(optional = true) 
    @Size(max = 40)
    @Column(name = "codigo_suministro")
    private String codigoSuministro;
    
    @JoinColumn(name = "objeto_gasto")
    @ManyToOne(optional = true)
    private ObjetosGasto objetoGasto;
    
  
    
    @Basic(optional = true)
    @Column(name = "fecha_vencimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;
    
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "codigo_DNCP")
    private String codigoDNCP;
    @Column(name = "costo_unitario")
    private BigDecimal costoUnitario;
    @Column(name = "costo_total")
    private BigDecimal costoTotal;
   
    @JoinColumn(name = "inventario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Inventarios inventario;
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
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios persona;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "articulo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Articulos articulo;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamento;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "salida_articulo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SalidasArticulo salidaArticulo;
    @Transient
    private Date fechaSalida;
    @JoinColumn(name = "modelo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Modelos modelo;
    @JoinColumn(name = "marca", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Marcas marca;
   // public String getDepartamento;
    
    @JoinColumn(name = "usuario_borrado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioBorrado;
    @Column(name = "fecha_hora_borrado") // Este nombre tiene que se igual al nombre de la columna de la tabla 
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;   
    @JoinColumn(name = "entrada_articulo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EntradasArticulo entradaArticulo;  
    @JoinColumn(name = "detalle_entrada_articulo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DetallesEntradaArticulo detalleEntradaArticulo;  
    @JoinColumn(name = "detalle_inventario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DetallesInventario detalleInventario;
    @JoinColumn(name = "sede", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Sede sede;
    @JoinColumn(name = "proveedor", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Proveedores proveedor;

    
    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }
   
    

    public Marcas getMarca() {
        return marca;
    }

    public void setMarca(Marcas marca) {
        this.marca = marca;
    }

    public Modelos getModelo() {
        return modelo;
    }

    public void setModelo(Modelos modelo) {
        this.modelo = modelo;
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

    public DetallesSalidaArticulo() {
    }

    public DetallesSalidaArticulo(Integer id) {
        this.id = id;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }
    

    public DetallesSalidaArticulo(Integer id, ObjetosGasto objetoGasto, int cantidad, Integer nroFormulario,String codigoDNCP, String codigoSuministro, Articulos articulo,Date fechaVencimiento, Date fechaHoraAlta, Date fechaHoraUltimoEstado, Usuarios usuarioBorrado ,Date fechaHoraBorrado) {
        this.id = id;
        this.cantidad = cantidad;
        this.nroFormulario = nroFormulario;
        this.codigoSuministro = codigoSuministro;
        this.objetoGasto = objetoGasto;
        this.fechaVencimiento = fechaVencimiento;
        this.codigoDNCP = codigoDNCP;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
        this.articulo= articulo;
        this.usuarioBorrado= usuarioBorrado; 
        this.fechaHoraBorrado=fechaHoraBorrado; 
        
    }

    public DetallesSalidaArticulo(DetallesSalidaArticulo det) {
        this.id = det.id;
        this.cantidad = det.cantidad;
        this.nroFormulario = det.nroFormulario;
        this.codigoSuministro = det.codigoSuministro;
        this.objetoGasto = det.objetoGasto;
        this.fechaVencimiento = det.fechaVencimiento;
        this.codigoDNCP = det.codigoDNCP;
        this.fechaHoraAlta = det.fechaHoraAlta;
        this.fechaHoraUltimoEstado = det.fechaHoraUltimoEstado;
        this.articulo= det.articulo;
        this.usuarioBorrado= det.usuarioBorrado; 
        this.fechaHoraBorrado=det.fechaHoraBorrado; 
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ObjetosGasto getObjetoGasto() {
        return objetoGasto;
    }

    public void setObjetoGasto(ObjetosGasto objetoGasto) {
        this.objetoGasto = objetoGasto;
    }

    public String getCodigoDNCP() {
        return codigoDNCP;
    }

    public void setCodigoDNCP(String codigoDNCP) {
        this.codigoDNCP = codigoDNCP;
    }

    public String getCodigoSuministro() {
        return codigoSuministro;
    }

    public void setCodigoSuministro(String codigoSuministro) {
        this.codigoSuministro = codigoSuministro;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public Inventarios getInventario() {
        return inventario;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public void setInventario(Inventarios inventario) {
        this.inventario = inventario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getNroFormulario() {
        return nroFormulario;
    }

    public void setNroFormulario(Integer nroFormulario) {
        this.nroFormulario = nroFormulario;
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

    public Usuarios getPersona() {
        return persona;
    }

    public void setPersona(Usuarios persona) {
        this.persona = persona;
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

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public SalidasArticulo getSalidaArticulo() {
        return salidaArticulo;
    }

    public void setSalidaArticulo(SalidasArticulo salidaArticulo) {
        this.salidaArticulo = salidaArticulo;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public EntradasArticulo getEntradaArticulo() {
        return entradaArticulo;
    }

    public void setEntradaArticulo(EntradasArticulo entradaArticulo) {
        this.entradaArticulo = entradaArticulo;
    }

    public DetallesEntradaArticulo getDetalleEntradaArticulo() {
        return detalleEntradaArticulo;
    }

    public void setDetalleEntradaArticulo(DetallesEntradaArticulo detalleEntradaArticulo) {
        this.detalleEntradaArticulo = detalleEntradaArticulo;
    }

    public DetallesInventario getDetalleInventario() {
        return detalleInventario;
    }

    public void setDetalleInventario(DetallesInventario detalleInventario) {
        this.detalleInventario = detalleInventario;
    }

    public Proveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedores proveedor) {
        this.proveedor = proveedor;
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
        if (!(object instanceof DetallesSalidaArticulo)) {
            return false;
        }
        DetallesSalidaArticulo other = (DetallesSalidaArticulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.DetallesSalidaArticulo[ id=" + id + " ]";
    }
       
}
