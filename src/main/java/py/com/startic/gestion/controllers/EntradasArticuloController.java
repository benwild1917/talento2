package py.com.startic.gestion.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;

import py.com.startic.gestion.models.EntradasArticulo;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.primefaces.PrimeFaces;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.datasource.CantidadItem;
import py.com.startic.gestion.datasource.RepEntradaArticuloProveedor;
import py.com.startic.gestion.datasource.RepEntradaObjetoGasto;
import py.com.startic.gestion.datasource.RepEntradasArticulo;
import py.com.startic.gestion.datasource.RepEntradasArticuloPorDeposito;
import py.com.startic.gestion.datasource.RepNotaRecepcion;
import py.com.startic.gestion.models.Articulos;
import py.com.startic.gestion.models.BajaArticulos;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.DetallesEntradaArticulo;
import py.com.startic.gestion.models.Inventarios;
import py.com.startic.gestion.models.Marcas;
import py.com.startic.gestion.models.Modelos;
import py.com.startic.gestion.models.ObjetosGasto;
import py.com.startic.gestion.models.Proveedores;
import py.com.startic.gestion.models.Sede;
import py.com.startic.gestion.models.TiposIva;
import py.com.startic.gestion.models.Unidades;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "entradasArticuloController")
@ViewScoped
public class EntradasArticuloController extends AbstractController<EntradasArticulo> {

    @Inject
    private ProveedoresController proveedorController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private DetallesEntradaArticuloController detallesEntradaArticuloController;
    @Inject
    private ArticulosController articuloController;
    @Inject
    private SedeController sedeController;

    @Inject
    private ArticulosStockCriticoController articuloStockCriticoController;

    private Collection<DetallesEntradaArticulo> detalles;

    private DetallesEntradaArticulo detalleSelected;

    private Articulos articulo;
    private Sede sede;

    private Unidades unidad;

    private Integer cantidad;

    private EntradasArticulo entradaArticulo;
    private Date fechaDesde;
    private Date fechaHasta;
    private Date fechaDesdeEntrada;
    private Date fechaHastaEntrada;
    private BigDecimal costoUnitario;
    private BigDecimal totalCostoTotal;
    private BigDecimal totalIva_5;
    private BigDecimal totalIva_10;
    private BigDecimal totalIva;
    private BigDecimal subtotalIva_5;
    private BigDecimal subtotalIva_10;
    private BigDecimal subtotalExentas;
    private BigDecimal costoTotal;
    private BigDecimal totalGeneral;
    private TiposIva tiposIva;
    private Articulos articuloFiltro;
    private List<Articulos> listaArticuloFiltroEntrada;
    private Departamentos departamentoFiltro;
    private ObjetosGasto objetosgastoFiltro;
    private Proveedores proveedorFiltro;
    private Sede sedeFiltro;
    private ObjetosGasto objetoGasto;
    private String codigoDNCP;
    private String deposito;
    private String codigoSuministro;
    private Date fechaVencimiento;
    private Marcas marca;
    private Modelos modelo;
    private List<Proveedores> listaProveedorFiltroEntrada;
    private List<Sede> listaSedeFiltroEntrada;
    private List<Articulos> listaArticulos;
    private HttpSession session;
    private Usuarios usuario;

    @PostConstruct
    @Override
    public void initParams() {
        entradaArticulo = prepareCreate(null);
        fechaDesdeEntrada = ejbFacade.getSystemDateOnly(-30);
        fechaHastaEntrada = ejbFacade.getSystemDateOnly();
        fechaDesde = null;
        fechaHasta = null;
        super.initParams();
        buscarPorFechaEntrada();
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usuario = (Usuarios) session.getAttribute("Usuarios");
    }

    public BigDecimal getSubtotalIva_5() {
        return subtotalIva_5;
    }

    public void setSubtotalIva_5(BigDecimal subtotalIva_5) {
        this.subtotalIva_5 = subtotalIva_5;
    }

    public BigDecimal getSubtotalIva_10() {
        return subtotalIva_10;
    }

    public void setSubtotalIva_10(BigDecimal subtotalIva_10) {
        this.subtotalIva_10 = subtotalIva_10;
    }

    public BigDecimal getSubtotalExentas() {
        return subtotalExentas;
    }

    public void setSubtotalExentas(BigDecimal subtotalExentas) {
        this.subtotalExentas = subtotalExentas;
    }

    public TiposIva getTiposIva() {
        return tiposIva;
    }

    public void setTiposIva(TiposIva tiposIva) {
        this.tiposIva = tiposIva;
    }

    public BigDecimal getTotalCostoTotal() {
        return totalCostoTotal;
    }

    public void setTotalCostoTotal(BigDecimal totalCostoTotal) {
        this.totalCostoTotal = totalCostoTotal;
    }

    public BigDecimal getTotalIva() {
        return totalIva;
    }

    public void setTotalIva(BigDecimal totalIva) {
        this.totalIva = totalIva;
    }

    public BigDecimal getTotalIva_5() {
        return totalIva_5;
    }

    public void setTotalIva_5(BigDecimal totalIva_5) {
        this.totalIva_5 = totalIva_5;
    }

    public BigDecimal getTotalIva_10() {
        return totalIva_10;
    }

    public void setTotalIva_10(BigDecimal totalIva_10) {
        this.totalIva_10 = totalIva_10;
    }

    public List<Articulos> getListaArticuloFiltroEntrada() {
        return listaArticuloFiltroEntrada;
    }

    public void setListaArticuloFiltroEntrada(List<Articulos> listaArticuloFiltroEntrada) {
        this.listaArticuloFiltroEntrada = listaArticuloFiltroEntrada;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }
     public void prepareEdit() {
        if (getSelected() != null) {
            actualizarListas(getSelected().getSede());

        }

    }


    public Sede getSede() {

       /* if (session.getAttribute("sedeSelected") != null) {
            sede = (Sede) session.getAttribute("sedeSelected");
            session.removeAttribute("sedeSelected");
            actualizarListas(sede);
        }*/
            return sede;
        }

    

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public String getDeposito() {
        return deposito;
    }

    public void setDeposito(String deposito) {
        this.deposito = deposito;
    }

    public String getCodigoSuministro() {
        return codigoSuministro;
    }

    public ObjetosGasto getObjetosgastoFiltro() {
        return objetosgastoFiltro;
    }

    public void setObjetosgastoFiltro(ObjetosGasto objetosgastoFiltro) {
        this.objetosgastoFiltro = objetosgastoFiltro;
    }

    public void setCodigoSuministro(String codigoSuministro) {
        this.codigoSuministro = codigoSuministro;
    }

    public BigDecimal getTotalGeneral() {
        return totalGeneral;
    }

    public void setTotalGeneral(BigDecimal totalGeneral) {
        this.totalGeneral = totalGeneral;
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

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getCodigoDNCP() {
        return codigoDNCP;
    }

    public void setCodigoDNCP(String codigoDNCP) {
        this.codigoDNCP = codigoDNCP;
    }

    public ObjetosGasto getObjetoGasto() {
        return objetoGasto;
    }

    public void setObjetoGasto(ObjetosGasto objetoGasto) {
        this.objetoGasto = objetoGasto;
    }

    public List<Articulos> getListaArticulos() {
        return listaArticulos;
    }

    public void setListaArticulos(List<Articulos> listaArticulos) {
        this.listaArticulos = listaArticulos;
    }

    public ProveedoresController getProveedorController() {
        return proveedorController;
    }

    public void setProveedorController(ProveedoresController proveedorController) {
        this.proveedorController = proveedorController;
    }

    public UsuariosController getUsuarioAltaController() {
        return usuarioAltaController;
    }

    public void setUsuarioAltaController(UsuariosController usuarioAltaController) {
        this.usuarioAltaController = usuarioAltaController;
    }

    public UsuariosController getUsuarioUltimoEstadoController() {
        return usuarioUltimoEstadoController;
    }

    public void setUsuarioUltimoEstadoController(UsuariosController usuarioUltimoEstadoController) {
        this.usuarioUltimoEstadoController = usuarioUltimoEstadoController;
    }

    public EmpresasController getEmpresaController() {
        return empresaController;
    }

    public void setEmpresaController(EmpresasController empresaController) {
        this.empresaController = empresaController;
    }

    public DetallesEntradaArticuloController getDetallesEntradaArticuloController() {
        return detallesEntradaArticuloController;
    }

    public void setDetallesEntradaArticuloController(DetallesEntradaArticuloController detallesEntradaArticuloController) {
        this.detallesEntradaArticuloController = detallesEntradaArticuloController;
    }

    public ArticulosController getArticuloController() {
        return articuloController;
    }

    public void setArticuloController(ArticulosController articuloController) {
        this.articuloController = articuloController;
    }

    public ArticulosStockCriticoController getArticuloStockCriticoController() {
        return articuloStockCriticoController;
    }

    public void setArticuloStockCriticoController(ArticulosStockCriticoController articuloStockCriticoController) {
        this.articuloStockCriticoController = articuloStockCriticoController;
    }

    public List<Proveedores> getListaProveedorFiltroEntrada() {
        return listaProveedorFiltroEntrada;
    }

    public void setListaProveedorFiltroEntrada(List<Proveedores> listaProveedorFiltroEntrada) {
        this.listaProveedorFiltroEntrada = listaProveedorFiltroEntrada;
    }

    public List<Sede> getListaSedeFiltroEntrada() {
        return listaSedeFiltroEntrada;
    }

    public void setListaSedeFiltroEntrada(List<Sede> listaSedeFiltroEntrada) {
        this.listaSedeFiltroEntrada = listaSedeFiltroEntrada;
    }

    @Override
    public EntradasArticulo prepareCreate(ActionEvent event) {

        detalles = null;
        detalleSelected = null;
        articulo = null;
        cantidad = 0;
        costoUnitario = BigDecimal.ZERO;
        totalCostoTotal = BigDecimal.ZERO;
        costoTotal =BigDecimal.ZERO;
        totalIva = BigDecimal.ZERO;
        totalIva_5 = BigDecimal.ZERO;
        totalIva_10 = BigDecimal.ZERO;
        costoTotal = BigDecimal.ZERO;
        subtotalExentas = BigDecimal.ZERO;
        subtotalIva_5 = BigDecimal.ZERO;
        subtotalIva_10 = BigDecimal.ZERO;

        Inventarios inv = null;
        try {
            inv = ejbFacade.getEntityManager().createNamedQuery("Inventarios.findVigente", Inventarios.class).getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (inv == null) {
            JsfUtil.addErrorMessage("Para ingresar un movimiento debe haber al menos un inventario vigente");
            return null;
        }

        if (entradaArticulo != null) {
            entradaArticulo.setNroFactura("");
            entradaArticulo.setNroLlamado("");
            entradaArticulo.setProveedor(null);
           // entradaArticulo.setSede(null);
            entradaArticulo.setConcepto("");
            entradaArticulo.setInventario(inv);
            entradaArticulo.setFechaEntrada(ejbFacade.getSystemDate());
        }

        return super.prepareCreate(event);
    }

    public EntradasArticuloController() {
        // Inform the Abstract parent controller of the concrete EntradasArticulo Entity
        super(EntradasArticulo.class);
    }

    public Proveedores getProveedorFiltro() {
        return proveedorFiltro;
    }

    public void setProveedorFiltro(Proveedores proveedorFiltro) {
        this.proveedorFiltro = proveedorFiltro;
    }

    public Sede getSedeFiltro() {
        return sedeFiltro;
    }

    public void setSedeFiltro(Sede sedeFiltro) {
        this.sedeFiltro = sedeFiltro;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public EntradasArticulo getEntradaArticulo() {
        return entradaArticulo;
    }

    public void setEntradaArticulo(EntradasArticulo entradaArticulo) {
        this.entradaArticulo = entradaArticulo;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Date getFechaDesdeEntrada() {
        return fechaDesdeEntrada;
    }

    public void setFechaDesdeEntrada(Date fechaDesdeEntrada) {
        this.fechaDesdeEntrada = fechaDesdeEntrada;
    }

    public Date getFechaHastaEntrada() {
        return fechaHastaEntrada;
    }

    public void setFechaHastaEntrada(Date fechaHastaEntrada) {
        this.fechaHastaEntrada = fechaHastaEntrada;
    }

    public Unidades getUnidad() {
        return unidad;
    }

    public void setUnidad(Unidades unidad) {
        this.unidad = unidad;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public DetallesEntradaArticulo getDetalleSelected() {
        return detalleSelected;
    }

    public void setDetalleSelected(DetallesEntradaArticulo detalleSelected) {
        this.detalleSelected = detalleSelected;
    }

    public Collection<DetallesEntradaArticulo> getDetalles() {
        return detalles;
    }

    public void setDetalles(Collection<DetallesEntradaArticulo> detalles) {
        this.detalles = detalles;
    }

    public Articulos getArticuloFiltro() {
        return articuloFiltro;
    }

    public void setArticuloFiltro(Articulos articuloFiltro) {
        this.articuloFiltro = articuloFiltro;
    }

    public Departamentos getDepartamentoFiltro() {
        return departamentoFiltro;
    }

    public void setDepartamentoFiltro(Departamentos departamentoFiltro) {
        this.departamentoFiltro = departamentoFiltro;
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        proveedorController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        empresaController.setSelected(null);

        seleccionar();
    }
   

    private List<Articulos> obtenerListaArticulos(Sede sede) {
        if (sede != null) {
            return this.ejbFacade.getEntityManager().createNamedQuery("Articulos.findByDeposito", Articulos.class).setParameter("sede", sede).getResultList();
        }
        return null;
    }

    public void actualizarListas(Sede sede) {
        listaArticulos = obtenerListaArticulos(sede);
    }

    public void resetearListas(Sede sede) {
        articulo = null;

        if (getSelected() != null) {
            getSelected().setArticulo(null);

        }
        actualizarListas(sede);
    }

    @Override
    public Collection<EntradasArticulo> getItems() {
        return super.getItems2();
    }

    @Override
    public void delete(ActionEvent event) {

        if (getSelected() != null) {
            Articulos art = null;

            Collection<DetallesEntradaArticulo> col = ejbFacade.getEntityManager().createNamedQuery("DetallesEntradaArticulo.findByEntradaArticulo", DetallesEntradaArticulo.class).setParameter("entradaArticulo", getSelected()).getResultList();

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            for (DetallesEntradaArticulo det : col) {
                art = det.getArticulo();

                art.setStock(art.getStock() - det.getCantidad());

                articuloController.setSelected(art);

                articuloController.save(event);

                detallesEntradaArticuloController.setSelected(det);

                detallesEntradaArticuloController.getSelected().setFechaHoraBorrado(fecha);
                detallesEntradaArticuloController.getSelected().setUsuarioBorrado(usu);

                detallesEntradaArticuloController.save(event);

                detallesEntradaArticuloController.delete(event);
            }

            getSelected().setFechaHoraBorrado(fecha);
            getSelected().setUsuarioBorrado(usu);

            super.save(event);

            super.delete(event);

            /*
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaHasta);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("EntradasArticulo.findOrdered", EntradasArticulo.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).getResultList());
             */
            buscarPorFechaEntrada();
            setSelected(null);

            detalleSelected = null;
            detalles = null;
        }
    }

    public boolean verificarNroFacturaCreate() {
        return verificarNroFactura(entradaArticulo.getNroFactura());
    }

    public boolean verificarNroFacturaEdit() {
        return verificarNroFactura(getSelected().getNroFactura());
    }

    public boolean verificarNroFactura(String nro) {
        if (entradaArticulo != null) {
            if (entradaArticulo.getProveedor() != null) {
                if (entradaArticulo.getNroFactura() != null) {
                    CantidadItem cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from entradas_articulo where nro_factura = ?1 and proveedor = ?2", CantidadItem.class).setParameter(1, nro).setParameter(2, entradaArticulo.getProveedor().getId()).getSingleResult();

                    if (cant.getCantidad() > 0) {
                        JsfUtil.addErrorMessage("Nro de factura ya existe");
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void cargarDatosAdicionales() {
        if (articulo != null) {
            marca = articulo.getMarca();
            modelo = articulo.getModelo();
            objetoGasto = articulo.getObjetoGasto();
            codigoSuministro = articulo.getCodigoSuministro();
            sede = articulo.getSede();

            cantidad = 0;
            fechaVencimiento = null;
            costoUnitario = BigDecimal.ZERO;
            codigoDNCP = "";
            codigoSuministro = "";
        }
    }

    public String datePattern2() {
        return "dd/MM/yyyy";
    }

    public String customFormatDate2(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern2());
            return format.format(date);
        }
        return "";
    }

    public String datePattern() {
        return "dd/MM/yyyy HH:mm:ss";
    }

    public String customFormatDate(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern());
            return format.format(date);
        }
        return "";
    }

    public void resetFilters() {
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('wvArticulos').clearFilters()");
    }

    public void buscarPorFechaAlta() {
        if (fechaDesde == null || fechaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaHasta);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("EntradasArticulo.findOrdered", EntradasArticulo.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).getResultList());

            if (!getItems2().isEmpty()) {
                EntradasArticulo art = getItems2().iterator().next();
                setSelected(art);
                detalles = art.getDetallesEntradaArticuloCollection();

                totalCostoTotal = BigDecimal.ZERO;
                totalIva_5 = BigDecimal.ZERO;
                totalIva_10 = BigDecimal.ZERO;
                totalIva = BigDecimal.ZERO;
                for (DetallesEntradaArticulo det : detalles) {
//                    totalCostoTotal = totalCostoTotal.add(det.)
                }

                detalleSelected = null;
            } else {
                detalles = null;
                detalleSelected = null;
                setSelected(null);
            }
        }
    }

    public void buscarPorFechaEntrada() {
        if (fechaDesdeEntrada == null || fechaHastaEntrada == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaHastaEntrada);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHastaEntrada = cal.getTime();
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("EntradasArticulo.findFechaEntradaOrdered", EntradasArticulo.class).setParameter("fechaDesde", fechaDesdeEntrada).setParameter("fechaHasta", nuevaFechaHastaEntrada).getResultList());

            if (!getItems2().isEmpty()) {
                EntradasArticulo art = getItems2().iterator().next();
                setSelected(art);
                detalles = art.getDetallesEntradaArticuloCollection();
                detalleSelected = null;
            } else {
                detalles = null;
                detalleSelected = null;
                setSelected(null);
            }
        }
    }

    private void seleccionar() {
        if (getSelected() != null) {
            detalles = this.ejbFacade.getEntityManager().createNamedQuery("DetallesEntradaArticulo.findByEntradaArticulo", DetallesEntradaArticulo.class).setParameter("entradaArticulo", getSelected()).getResultList();
        } else {
            detalles = null;
        }

    }

    /**
     * Sets the "items" attribute with a collection of DetallesEntradaArticulo
     * entities that are retrieved from EntradasArticulo?cap_first and returns
     * the navigation outcome.
     *
     * @return navigation outcome for DetallesEntradaArticulo page
     */
    public String navigateDetallesEntradaArticuloCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DetallesEntradaArticulo_items", this.getSelected().getDetallesEntradaArticuloCollection());
        }
        return "/pages/detallesEntradaArticulo/index";
    }

    /**
     * Sets the "selected" attribute of the Proveedores controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareProveedor(ActionEvent event) {
        if (this.getSelected() != null && proveedorController.getSelected() == null) {
            proveedorController.setSelected(this.getSelected().getProveedor());
        }
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuarioAlta(ActionEvent event) {
        if (this.getSelected() != null && usuarioAltaController.getSelected() == null) {
            usuarioAltaController.setSelected(this.getSelected().getUsuarioAlta());
        }
    }

    /**
     * Sets the "selected" attribute of the Proveedores controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEntradasArticulo(ActionEvent event) {
        if (this.getSelected() != null && sedeController.getSelected() == null) {
            sedeController.setSelected(this.getSelected().getSede());
        }
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuarioUltimoEstado(ActionEvent event) {
        if (this.getSelected() != null && usuarioUltimoEstadoController.getSelected() == null) {
            usuarioUltimoEstadoController.setSelected(this.getSelected().getUsuarioUltimoEstado());
        }
    }

    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEmpresa(ActionEvent event) {
        if (this.getSelected() != null && empresaController.getSelected() == null) {
            empresaController.setSelected(this.getSelected().getEmpresa());
        }
    }

    public void borrarDetalleSeleccionado(DetallesEntradaArticulo item) {
        if (detalles != null && detalleSelected != null) {
            for (DetallesEntradaArticulo det : detalles) {
                if (det.getArticulo().getCodigo().equals(detalleSelected.getArticulo().getCodigo())) {
                    detalles.remove(det);
                    break;
                }
            }
        }
    }

    public void borrarDetalle() {
        if (detalles != null && detalleSelected != null) {
            for (DetallesEntradaArticulo det : detalles) {
                if (det.getArticulo().getCodigo().equals(detalleSelected.getArticulo().getCodigo())) {

                    totalCostoTotal = totalCostoTotal.subtract(det.getCostoTotal());
                    BigDecimal iva = BigDecimal.ZERO;
                    if (det.getTipoIva().getId() == 1) { // Exentas
                        subtotalExentas = subtotalExentas.subtract(det.getCostoTotal());
                    } else if (det.getTipoIva().getId() == 2) { // Iva 5
                        det.setIva_5(det.getCostoTotal());
                        iva = calcularIVA(det.getCostoTotal(), 5);
                        totalIva_5 = totalIva_5.subtract(iva);
                        subtotalIva_5 = subtotalIva_5.subtract(det.getCostoTotal());
                    } else if (det.getTipoIva().getId() == 3) { // Iva 10
                        det.setIva_10(det.getCostoTotal());
                        iva = calcularIVA(det.getCostoTotal(), 10);
                        totalIva_10 = totalIva_10.subtract(iva);
                        subtotalIva_10 = subtotalIva_10.subtract(det.getCostoTotal());
                    }

                    totalIva = totalIva.subtract(iva);

                    detalles.remove(det);
                    break;
                }
            }
        }
    }

    private BigDecimal calcularIVA(BigDecimal monto, Integer porcentaje) {
        BigDecimal div;
        if (porcentaje == 10) {
            div = new BigDecimal(11);
        } else {
            div = new BigDecimal(21);
        }

        return monto.divide(div, 0, RoundingMode.HALF_UP);
    }

    public void agregar() {
        if (articulo == null) {
            JsfUtil.addErrorMessage("Debe seleccionar el articulo");
            return;
        }

        if (costoUnitario == null) {
            JsfUtil.addErrorMessage("Debe especificar un costo unitario");
            return;
        } else if (costoUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            JsfUtil.addErrorMessage("El costo unitario debe ser mayor a cero");
            return;
        }

        if (cantidad == null) {
            JsfUtil.addErrorMessage("Debe especificar la cantidad");
            return;
        } else if (cantidad <= 0) {
            JsfUtil.addErrorMessage("La cantidad debe ser mayor a cero");
            return;
        }

        if (tiposIva == null) {
            JsfUtil.addErrorMessage("Debe seleccionar el ");
            return;
        }

        if (detalles == null) {
            detalles = new ArrayList<>();
        }

        if (totalCostoTotal == null) {
            totalCostoTotal = BigDecimal.ZERO;
        }
        /*
        boolean encontro = false;

        for (DetallesEntradaArticulo det : detalles) {
            if (det.getArticulo().getCodigo().equals(articulo.getCodigo())) {

                detalles.remove(det);
                BigDecimal iva = BigDecimal.ZERO;
                if(det.getTipoIva().getId() == 1){ // Exentas
                    subtotalExentas = subtotalExentas.subtract(det.getCostoTotal());
                }else if(det.getTipoIva().getId() == 2){ // Iva 5
                    iva = calcularIVA(det.getCostoTotal(),5);
                    totalIva_5 = totalIva_5.subtract(iva);
                    subtotalIva_5 = subtotalIva_5.subtract(det.getCostoTotal());
                }else{ // Iva 10
                    iva = calcularIVA(det.getCostoTotal(),10);
                    totalIva_10 = totalIva_10.subtract(iva);
                    subtotalIva_10 = subtotalIva_10.subtract(det.getCostoTotal());
                }

                totalIva = totalIva.add(iva);
                

                DetallesEntradaArticulo dea = new DetallesEntradaArticulo();

                SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

                Date fecha = ejbFacade.getSystemDate();

                dea.setCantidad(det.getCantidad() + cantidad);
                dea.setArticulo(articulo);

                dea.setModelo(modelo);
                dea.setObjetoGasto(objetoGasto);
                dea.setFechaVencimiento(fechaVencimiento);
                dea.setCodigoDNCP(codigoDNCP);
                dea.setCostoUnitario(costoUnitario);
                dea.setMarcas(marca);
                dea.setCodigoSuministro(codigoSuministro);
                dea.setCostoTotal(costoUnitario.multiply(new BigDecimal(dea.getCantidad())));
                
                dea.setTipoIva(tiposIva);
               
                dea.setExentas(BigDecimal.ZERO);
                dea.setIva_5(BigDecimal.ZERO);
                dea.setIva_10(BigDecimal.ZERO);
                
                iva = BigDecimal.ZERO;
                if(det.getTipoIva().getId() == 1){ // Exentas
                    dea.setExentas(dea.getCostoTotal());
                    subtotalExentas = subtotalExentas.add(dea.getCostoTotal());
                }else if(det.getTipoIva().getId() == 2){ // Iva 5
                    dea.setIva_5(dea.getCostoTotal());
                    iva = calcularIVA(dea.getCostoTotal(),5);
                    totalIva_5 = totalIva_5.add(iva);
                    subtotalIva_5 = subtotalIva_5.add(dea.getCostoTotal());
                }else{ // Iva 10
                    dea.setIva_10(dea.getCostoTotal());
                    iva = calcularIVA(dea.getCostoTotal(),10);
                    totalIva_10 = totalIva_10.add(iva);
                    subtotalIva_10 = subtotalIva_10.add(dea.getCostoTotal());
                }

                totalIva = totalIva.add(iva);
                
                dea.setId(Integer.valueOf(format.format(fecha)));
                detalles.add(dea);
                
                totalCostoTotal = totalCostoTotal.add(dea.getCostoTotal());
                
                encontro = true;
                break;
            }

        }
         */
        // if (!encontro) {
        DetallesEntradaArticulo dea = detallesEntradaArticuloController.prepareCreate(null);

        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

        Date fecha = ejbFacade.getSystemDate();

        dea.setCantidad(cantidad);
        dea.setArticulo(articulo);
        dea.setSede(articulo.getRubro().getSede());
        dea.setModelo(modelo);
        dea.setObjetoGasto(objetoGasto);
        dea.setFechaVencimiento(fechaVencimiento);
        dea.setCodigoDNCP(codigoDNCP);
        dea.setCostoUnitario(costoUnitario);
        dea.setMarcas(marca);
        dea.setCostoUnitario(costoUnitario);
        dea.setCostoTotal(costoUnitario.multiply(new BigDecimal(dea.getCantidad())));
        dea.setTipoIva(tiposIva);
        dea.setExentas(BigDecimal.ZERO);
        dea.setIva_5(BigDecimal.ZERO);
        dea.setIva_10(BigDecimal.ZERO);

        BigDecimal iva = BigDecimal.ZERO;
        if (tiposIva.getId() == 1) { // Exentas
            dea.setExentas(dea.getCostoTotal());
            subtotalExentas = subtotalExentas.add(dea.getCostoTotal());
        } else if (tiposIva.getId() == 2) { // Iva 5
            dea.setIva_5(dea.getCostoTotal());
            iva = calcularIVA(dea.getCostoTotal(), 5);
            totalIva_5 = totalIva_5.add(iva);
            subtotalIva_5 = subtotalIva_5.add(dea.getCostoTotal());
        } else { // Iva 10
            dea.setIva_10(dea.getCostoTotal());
            iva = calcularIVA(dea.getCostoTotal(), 10);
            totalIva_10 = totalIva_10.add(iva);
            subtotalIva_10 = subtotalIva_10.add(dea.getCostoTotal());
        }

        totalIva = totalIva.add(iva);

        dea.setId(Integer.valueOf(format.format(fecha)));
        dea.setInventario(entradaArticulo.getInventario());
        detalles.add(dea);

        totalCostoTotal = totalCostoTotal.add(dea.getCostoTotal());
        // }

        // RequestContext requestContext = RequestContext.getCurrentInstance();
        // requestContext.execute("PF('wvArticulos').clearFilters()");
    }

    @Override
    public void save(ActionEvent event) {
        // if (getSelected() != null) 
        {

            if (!verificarNroFacturaEdit()) {
                return;
            }

            if (getSelected() != null) {
                HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

                Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaHoraUltimoEstado(fecha);
                getSelected().setUsuarioUltimoEstado(usu);
            }

            super.save(event);
        }

        /**
         * Store a new item in the data layer.
         *
         * @param event an event from the widget that wants to save a new Entity
         * to the data layer
         */
    }

    @Override
    public void saveNew(ActionEvent event) {

        //if (getSelected() != null)
        {

            if (!verificarNroFacturaCreate()) {
                return;
            }

            if (detalles != null) {
                if (detalles.isEmpty()) {
                    JsfUtil.addErrorMessage("Debe haber al menos un detalle");
                    return;
                }
            } else {
                JsfUtil.addErrorMessage("Debe haber al menos un detalle.");
                return;
            }

            if (getSelected() != null) {

                HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

                Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

                Date fecha = ejbFacade.getSystemDate();

                entradaArticulo.setFechaHoraUltimoEstado(fecha);
                entradaArticulo.setUsuarioUltimoEstado(usu);
                entradaArticulo.setFechaHoraAlta(fecha);
                entradaArticulo.setUsuarioAlta(usu);
                entradaArticulo.setEmpresa(usu.getEmpresa());
                entradaArticulo.setSubtotal_5(subtotalIva_5);
                entradaArticulo.setSubtotal_10(subtotalIva_10);
                entradaArticulo.setSubtotal_exenta(subtotalExentas);
                entradaArticulo.setTotal_factura(totalCostoTotal);
                entradaArticulo.setTotal_iva(totalIva);
                entradaArticulo.setIva_5(totalIva_5);
                entradaArticulo.setIva_10(totalIva_10);
               // entradaArticulo.setSede(sede);

                setSelected(entradaArticulo);

                super.saveNew(event);

                for (DetallesEntradaArticulo det : detalles) {
                    det.setEntradaArticulo(getSelected());
                    det.setFechaHoraUltimoEstado(fecha);
                    det.setUsuarioUltimoEstado(usu);
                    det.setFechaHoraAlta(fecha);
                    det.setUsuarioAlta(usu);
                    det.setEmpresa(usu.getEmpresa());
                    det.setNroFactura(getSelected().getNroFactura());
                    det.setNroLlamado(getSelected().getNroLlamado());
                    det.setFechaLlamado(entradaArticulo.getFechaLlamado());
                    det.setProveedor(getSelected().getProveedor());
                    det.setCostoUnitario(costoUnitario);
                    det.setCostoTotal(costoUnitario.multiply(new BigDecimal(det.getCantidad())));
                    //det.setSede(sede);
                   
                   


                    det.setId(null);

                    Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class).setParameter("codigo", det.getArticulo().getCodigo()).getSingleResult();

                    art.setStock(art.getStock() + det.getCantidad());

                    articuloController.setSelected(art);

                    articuloController.save(event);

                    detallesEntradaArticuloController.setSelected(det);

                    detallesEntradaArticuloController.saveNew(event);

                }
                /*
            setItems(ejbFacade.getEntityManager().createNamedQuery("EntradasArticulo.findAll", EntradasArticulo.class).getResultList());

            if (getItems2().size() > 0) {
                EntradasArticulo art = getItems2().iterator().next();
                setSelected(art);
                detalles = ejbFacade.getEntityManager().createNamedQuery("DetallesEntradaArticulo.findByEntradaArticulo", DetallesEntradaArticulo.class).setParameter("entradaArticulo", art).getResultList();
                detalleSelected = null;
            } else {
                setSelected(null);
            }
                 */

                if (fechaDesde == null) {
                    fechaDesde = ejbFacade.getSystemDateOnly(-30);
                }
                if (fechaHasta == null) {
                    fechaHasta = ejbFacade.getSystemDateOnly();
                }

                buscarPorFechaEntrada();

                setSelected(null);

                articuloStockCriticoController.verifArticulosStockCritico();
            }

        }
    }

    public void pdf(boolean generarPdf) {

        if (listaArticuloFiltroEntrada != null) {
            if (!listaArticuloFiltroEntrada.isEmpty()) {
                HttpServletResponse httpServletResponse = null;
                try {
                    JRBeanCollectionDataSource beanCollectionDataSource = null;

                    ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fechaHastaEntrada);
                    cal.add(Calendar.DATE, 1);
                    Date nuevaFechaHasta = cal.getTime();

                    List<DetallesEntradaArticulo> lista = ejbFacade.getEntityManager().createNamedQuery("DetallesEntradaArticulo.findByArticuloFechaEntrada", DetallesEntradaArticulo.class).setParameter("fechaEntradaDesde", fechaDesdeEntrada).setParameter("fechaEntradaHasta", nuevaFechaHasta).setParameter("articulo", listaArticuloFiltroEntrada).getResultList();

                    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                    List<RepEntradasArticulo> listaFinal = new ArrayList<>();
                    RepEntradasArticulo item = null;
                    for (DetallesEntradaArticulo det : lista) {
                        item = new RepEntradasArticulo();

                        item.setCodArti(det.getArticulo().getCodigo());
                        item.setArticulo(det.getArticulo().getDescripcion());
                        item.setMarca(det.getMarca() == null ? "" : det.getMarca().getDescripcion());
                        item.setCantidad(det.getCantidad());
                        item.setPrecioUnitario(det.getCostoUnitario());
                        item.setPrecioTotal(det.getCostoTotal());

                        String fechaString = "";

                        if (det.getFechaVencimiento() != null) {
                            fechaString = format2.format(det.getFechaVencimiento());
                        }

                        item.setVencimiento(fechaString);
                        item.setnLlamado(det.getEntradaArticulo().getNroLlamado());
                        item.setoG(det.getArticulo().getObjetoGasto() == null ? 0 : det.getArticulo().getObjetoGasto().getCodigo());
                        item.setfEntrada(det.getEntradaArticulo().getFechaEntrada());
                        item.setProveedor(det.getProveedor() == null ? "" : det.getProveedor().getNombre());
                        item.setUsuario(det.getUsuarioAlta().getNombresApellidos());

                        item.setnFactura(det.getNroFactura());
                        // item.setObjetoGasto(det.getObjetoGasto()==null?0:det.getObjetoGasto().getCodigo());

                        listaFinal.add(item);

                        //det.setFechaEntrada(det.getEntradaArticulo().getFechaEntrada());
                    }

                    beanCollectionDataSource = new JRBeanCollectionDataSource(listaFinal);

                    HashMap map = new HashMap();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                    Date fecha = ejbFacade.getSystemDate();

                    map.put("fecha", format.format(fecha));
                    map.put("fechaDesde", format2.format(fechaDesdeEntrada));
                    map.put("fechaHasta", format2.format(fechaHastaEntrada));

                    String descArticulo = "";
                    for (Articulos art : listaArticuloFiltroEntrada) {
                        if (!"".equals(descArticulo)) {
                            descArticulo += ", ";
                        }

                        descArticulo += art.getCodigo() + "-" + art.getDescripcion();;
                        //descObjetosGasto += obj.getCodigo() + "-" + obj.getDescripcion();

                    }
                    map.put("descArticulo", descArticulo);

                    JasperPrint jasperPrint = null;
                    ServletOutputStream servletOutputStream = null;

                    if (generarPdf) {
                        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteEntradasArticulo.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                        servletOutputStream = httpServletResponse.getOutputStream();

                        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

                    } else {
                        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteEntradasArticuloExcel.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.xlsx");

                        servletOutputStream = httpServletResponse.getOutputStream();

                        JRXlsxExporter exporter = new JRXlsxExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                        exporter.exportReport();
                    }

                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                    FacesContext.getCurrentInstance().responseComplete();

                } catch (Exception e) {
                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                    e.printStackTrace();

                    if (httpServletResponse != null) {
                        if (httpServletResponse.getHeader("Content-disposition") == null) {
                            httpServletResponse.addHeader("Content-disposition", "inline");
                        } else {
                            httpServletResponse.setHeader("Content-disposition", "inline");
                        }

                    }
                    JsfUtil.addErrorMessage("No se pudo generar el reporte.");

                }
            } else {
                JsfUtil.addErrorMessage("Debe selecionar al menos un Artículo");
            }
        } else {
            JsfUtil.addErrorMessage("Debe selecionar al menos un Artículo");
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

    public void pdfProveedor(boolean generarPdf) {

        if (listaProveedorFiltroEntrada != null) {
            if (!listaProveedorFiltroEntrada.isEmpty()) {
                HttpServletResponse httpServletResponse = null;
                try {
                    JRBeanCollectionDataSource beanCollectionDataSource = null;

                    ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fechaHastaEntrada);
                    cal.add(Calendar.DATE, 1);
                    Date nuevaFechaHasta = cal.getTime();

                    List<DetallesEntradaArticulo> lista = ejbFacade.getEntityManager().createNamedQuery("DetallesEntradaArticulo.findByProveedorFechaEntrada", DetallesEntradaArticulo.class).setParameter("fechaEntradaDesde", fechaDesdeEntrada).setParameter("fechaEntradaHasta", nuevaFechaHasta).setParameter("proveedor", listaProveedorFiltroEntrada).getResultList();
                    List<RepEntradaArticuloProveedor> listaProveedor = new ArrayList<>();
                    RepEntradaArticuloProveedor item = null;
                    for (DetallesEntradaArticulo det : lista) {
                        item = new RepEntradaArticuloProveedor();
                        item.setProveedor(det.getProveedor() == null ? "" : det.getProveedor().getNombre());
                        item.setNroLlamado(det.getNroLlamado());
                        item.setFechaLlamado(det.getFechaLlamado());
                        item.setNroFactura(det.getEntradaArticulo().getNroFactura());
                        item.setFechaEntrada(det.getEntradaArticulo().getFechaEntrada());
                        item.setArticulo(det.getArticulo().getDescripcion());
                        item.setCodigo(det.getArticulo().getCodigo());
                        item.setMarca(det.getMarca() == null ? "" : det.getMarca().getDescripcion());

                        item.setCantidad(det.getCantidad());
                        item.setPrecioUnitario(det.getCostoUnitario());
                        item.setImporteTotal(det.getCostoTotal());

                        listaProveedor.add(item);

                        //det.setFechaEntrada(det.getEntradaArticulo().getFechaEntrada());
                    }

                    beanCollectionDataSource = new JRBeanCollectionDataSource(listaProveedor);

                    HashMap map = new HashMap();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

                    Date fecha = ejbFacade.getSystemDate();

                    map.put("fecha", format.format(fecha));
                    map.put("fechaDesde", format2.format(fechaDesdeEntrada));
                    map.put("fechaHasta", format2.format(fechaHastaEntrada));

                    String descProveedor = "";
                    for (Proveedores prov : listaProveedorFiltroEntrada) {
                        if (!"".equals(descProveedor)) {
                            descProveedor += ", ";
                        }

                        descProveedor += prov.getNombre();

                    }
                    map.put("descProveedor", descProveedor);

                    String reportPath = null;
                    JasperPrint jasperPrint = null;

                    ServletOutputStream servletOutputStream = null;
                    if (generarPdf) {
                        reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteEntradaArticulosPorProveedor.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                        servletOutputStream = httpServletResponse.getOutputStream();

                        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

                    } else {
                        reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteEntradasArticuloPorProveedorExcel.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.xlsx");

                        servletOutputStream = httpServletResponse.getOutputStream();

                        JRXlsxExporter exporter = new JRXlsxExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                        exporter.exportReport();
                    }

                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                    FacesContext.getCurrentInstance().responseComplete();

                } catch (Exception e) {
                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                    e.printStackTrace();

                    if (httpServletResponse != null) {
                        if (httpServletResponse.getHeader("Content-disposition") == null) {
                            httpServletResponse.addHeader("Content-disposition", "inline");
                        } else {
                            httpServletResponse.setHeader("Content-disposition", "inline");
                        }

                    }
                    JsfUtil.addErrorMessage("No se pudo generar el reporte.");

                }
            } else {
                JsfUtil.addErrorMessage("Debe escoger un proveedor.");
            }

            ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
        }
    }

    public void reporteObjetoGasto() {

        reporteObjetoGasto(false);
    }

    public void reporteObjetoGasto(boolean generarWord) {

        if (objetosgastoFiltro != null) {

            HttpServletResponse httpServletResponse = null;
            try {
                JRBeanCollectionDataSource beanCollectionDataSource = null;

                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                Calendar cal = Calendar.getInstance();
                cal.setTime(fechaHastaEntrada);
                cal.add(Calendar.DATE, 1);
                Date nuevaFechaHasta = cal.getTime();

                List<DetallesEntradaArticulo> lista = ejbFacade.getEntityManager().createNamedQuery("DetallesEntradaArticulo.findByObjetoGastoFechaEntrada", DetallesEntradaArticulo.class).setParameter("fechaEntradaDesde", fechaDesdeEntrada).setParameter("fechaEntradaHasta", nuevaFechaHasta).setParameter("objetoGasto", objetosgastoFiltro).getResultList();

                List<RepEntradaObjetoGasto> listaOg = new ArrayList<>();
                RepEntradaObjetoGasto item = null;
                for (DetallesEntradaArticulo det : lista) {
                    item = new RepEntradaObjetoGasto();

                    item.setObjetoGasto(det.getArticulo().getObjetoGasto().getCodigo());
                    item.setCodigo(det.getArticulo().getCodigo());
                    item.setArticulo(det.getArticulo().getDescripcion());
                    item.setCantidad(det.getCantidad());
                    item.setFechaLlamado(det.getEntradaArticulo().getFechaLlamado());
                    item.setFechaEntrada(det.getEntradaArticulo().getFechaEntrada());
                    item.setNroFactura(det.getNroFactura());
                    item.setMarca(det.getMarca() == null ? "" : det.getMarca().getDescripcion());
                    item.setProveedor(det.getProveedor() == null ? "" : det.getProveedor().getNombre());
                    item.setPrecioUnitario(det.getCostoUnitario());
                    item.setNroLlamado(det.getNroLlamado());

                    listaOg.add(item);

                    //det.setFechaEntrada(det.getEntradaArticulo().getFechaEntrada());
                }

                beanCollectionDataSource = new JRBeanCollectionDataSource(listaOg);

                HashMap map = new HashMap();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

                Date fecha = ejbFacade.getSystemDate();

                map.put("fecha", format.format(fecha));
                map.put("fechaDesde", format2.format(fechaDesdeEntrada));
                map.put("fechaHasta", format2.format(fechaHastaEntrada));
                map.put("descObjetoGasto", objetosgastoFiltro.getCodigo());

                JasperPrint jasperPrint = null;
                ServletOutputStream servletOutputStream = null;

                if (generarWord) {
                    String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteEntradaObjetoGastoWord.jasper");
                    jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                    httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                    httpServletResponse.addHeader("Content-disposition", "filename=reporte.docx");

                    servletOutputStream = httpServletResponse.getOutputStream();

                    JRExporter exporter = new JRDocxExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                    exporter.exportReport();
                } else {
                    String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteEntradaObjetoGastoExcel.jasper");
                    jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                    httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                    httpServletResponse.addHeader("Content-disposition", "filename=reporte.xlsx");

                    servletOutputStream = httpServletResponse.getOutputStream();

                    JRXlsxExporter exporter = new JRXlsxExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                    exporter.exportReport();
                }

                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                FacesContext.getCurrentInstance().responseComplete();

            } catch (Exception e) {
                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                e.printStackTrace();

                if (httpServletResponse != null) {
                    if (httpServletResponse.getHeader("Content-disposition") == null) {
                        httpServletResponse.addHeader("Content-disposition", "inline");
                    } else {
                        httpServletResponse.setHeader("Content-disposition", "inline");
                    }

                }
                JsfUtil.addErrorMessage("No se pudo generar el reporte.");

            }
        } else {
            JsfUtil.addErrorMessage("Debe escoger un código del Objeto del Gasto.");
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

    public boolean deshabilitarImprimirNota() {
        return getSelected() == null;
    }

    public void imprimirNota() {

        //if (objetosgastoFiltro != null) {
        HttpServletResponse httpServletResponse = null;
        try {
            JRBeanCollectionDataSource beanCollectionDataSource = null;

            ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaHastaEntrada);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();

            List<DetallesEntradaArticulo> lista = ejbFacade.getEntityManager().createNamedQuery("DetallesEntradaArticulo.findByEntradaArticulo", DetallesEntradaArticulo.class).setParameter("entradaArticulo", getSelected()).getResultList();

            List<RepNotaRecepcion> listaNota = new ArrayList<>();
            RepNotaRecepcion item = null;

            int contador = 1;

            for (DetallesEntradaArticulo det : lista) {
                item = new RepNotaRecepcion();
                item.setProveedor(det.getProveedor().getNombre());
                item.setCantidad(det.getCantidad());
                item.setArticulos(det.getArticulo().getDescripcion() + (det.getMarca() == null ? "" : ", marca " + det.getMarca().getDescripcion()));
                item.setPrecioUnitario(det.getCostoUnitario());
                item.setPrecioTotal(det.getCostoTotal());
                // item.setFechaRemision(det.getEntradaArticulo().getFechaRemision());
                // item.setFechaFactura(det.getEntradaArticulo().getFechaEntrada());
                // item.setNroFactura(det.getEntradaArticulo().getNroFactura());
                // item.setNroRemision(det.getEntradaArticulo().getNroRemision());

                item.setItem(contador);

                contador++;

                listaNota.add(item);

                //det.setFechaEntrada(det.getEntradaArticulo().getFechaEntrada());
            }

            beanCollectionDataSource = new JRBeanCollectionDataSource(listaNota);

            HashMap map = new HashMap();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

            Date fecha = ejbFacade.getSystemDate();

            map.put("nroFactura", getSelected().getNroFactura());
            map.put("nroRemision", getSelected().getNroRemision());
            map.put("proveedor", getSelected().getProveedor() == null ? "" : getSelected().getProveedor().getNombre());
            map.put("fechaRemision", getSelected().getFechaRemision());
            map.put("fechaFactura", getSelected().getFechaEntrada());

            JasperPrint jasperPrint = null;
            ServletOutputStream servletOutputStream = null;

            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteNotaRecepcionWord.jasper");
            jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "filename=reporte.docx");

            servletOutputStream = httpServletResponse.getOutputStream();

            JRExporter exporter = new JRDocxExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
            exporter.exportReport();


            /*
                    httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                    httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                    servletOutputStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                    
             */
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            e.printStackTrace();

            if (httpServletResponse != null) {
                if (httpServletResponse.getHeader("Content-disposition") == null) {
                    httpServletResponse.addHeader("Content-disposition", "inline");
                } else {
                    httpServletResponse.setHeader("Content-disposition", "inline");
                }

            }
            JsfUtil.addErrorMessage("No se pudo generar el reporte.");

        }
    }

    ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    public void pdf2(boolean generarPdf) {

        if (listaSedeFiltroEntrada != null) {
            if (!listaSedeFiltroEntrada.isEmpty()) {
                HttpServletResponse httpServletResponse = null;
                try {
                    JRBeanCollectionDataSource beanCollectionDataSource = null;

                    ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fechaHastaEntrada);
                    cal.add(Calendar.DATE, 1);
                    Date nuevaFechaHasta = cal.getTime();

                    List<DetallesEntradaArticulo> lista = ejbFacade.getEntityManager().createNamedQuery("DetallesEntradaArticulo.findBySedeFechaEntrada", DetallesEntradaArticulo.class).setParameter("fechaEntradaDesde", fechaDesdeEntrada).setParameter("fechaEntradaHasta", nuevaFechaHasta).setParameter("sede", listaSedeFiltroEntrada).getResultList();

                    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                    List<RepEntradasArticuloPorDeposito> listaFinal = new ArrayList<>();
                    RepEntradasArticuloPorDeposito item = null;
                    for (DetallesEntradaArticulo det : lista) {
                        item = new RepEntradasArticuloPorDeposito();

                        item.setCodArti(det.getArticulo().getCodigo());
                        item.setArticulo(det.getArticulo().getDescripcion());
                        item.setDeposito(det.getSede().getDescripcion());
                        item.setCantidad(det.getCantidad());
                        item.setPrecioUnitario(det.getCostoUnitario());
                        item.setPrecioTotal(det.getCostoTotal());

                        String fechaString = "";

                        if (det.getFechaVencimiento() != null) {
                            fechaString = format2.format(det.getFechaVencimiento());
                        }

                        item.setVencimiento(fechaString);
                        item.setnLlamado(det.getEntradaArticulo().getNroLlamado());
                        item.setoG(det.getArticulo().getObjetoGasto() == null ? 0 : det.getArticulo().getObjetoGasto().getCodigo());
                        item.setfEntrada(det.getEntradaArticulo().getFechaEntrada());
                        item.setProveedor(det.getProveedor() == null ? "" : det.getProveedor().getNombre());
                        item.setUsuario(det.getUsuarioAlta().getNombresApellidos());

                        item.setnFactura(det.getNroFactura());
                        // item.setObjetoGasto(det.getObjetoGasto()==null?0:det.getObjetoGasto().getCodigo());

                        listaFinal.add(item);

                        //det.setFechaEntrada(det.getEntradaArticulo().getFechaEntrada());
                    }

                    beanCollectionDataSource = new JRBeanCollectionDataSource(listaFinal);

                    HashMap map = new HashMap();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                    Date fecha = ejbFacade.getSystemDate();

                    map.put("fecha", format.format(fecha));
                    map.put("fechaDesde", format2.format(fechaDesdeEntrada));
                    map.put("fechaHasta", format2.format(fechaHastaEntrada));
                   // map.put("datasource1", beanCollectionDataSource);
                   
                       String descDeposito = "";
                    for (Sede dep : listaSedeFiltroEntrada) {
                        if (!"".equals(descDeposito)) {
                            descDeposito += ", ";
                        }

                       descDeposito += dep.getDescripcion();
                    }
                    map.put("descDeposito", descDeposito);

                    JasperPrint jasperPrint = null;
                    ServletOutputStream servletOutputStream = null;

                    if (generarPdf) {
                        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteEntradasArticuloPorDeposito.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                        servletOutputStream = httpServletResponse.getOutputStream();

                        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

                    } else {
                        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteEntradasArticuloExcel.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.xlsx");

                        servletOutputStream = httpServletResponse.getOutputStream();

                        JRXlsxExporter exporter = new JRXlsxExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                        exporter.exportReport();
                    }

                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                    FacesContext.getCurrentInstance().responseComplete();

                } catch (Exception e) {
                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                    e.printStackTrace();

                    if (httpServletResponse != null) {
                        if (httpServletResponse.getHeader("Content-disposition") == null) {
                            httpServletResponse.addHeader("Content-disposition", "inline");
                        } else {
                            httpServletResponse.setHeader("Content-disposition", "inline");
                        }

                    }
                    JsfUtil.addErrorMessage("No se pudo generar el reporte.");

                }
            } else {
                JsfUtil.addErrorMessage("Debe selecionar al menos un Depósito");
            }
        } else {
            JsfUtil.addErrorMessage("Debe selecionar al menos un Depósito");
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

}
