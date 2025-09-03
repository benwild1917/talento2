package py.com.startic.gestion.controllers;

import py.com.startic.gestion.datasource.ReporteInventarioValorizadoAgregado;
import java.math.BigDecimal;
import java.math.BigInteger;
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

import py.com.startic.gestion.models.Inventarios;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import org.primefaces.event.CellEditEvent;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.datasource.DatosFCDoce;
import py.com.startic.gestion.datasource.RepFCDoce;
import py.com.startic.gestion.datasource.RepFCDos;
import py.com.startic.gestion.datasource.RepInventarioValorizadoSuma;
import py.com.startic.gestion.datasource.RepInventariosPorDias;
import py.com.startic.gestion.datasource.RepSalidaArticuloDepositoSuma;
import py.com.startic.gestion.models.Articulos;
import py.com.startic.gestion.models.DetallesInventario;
import py.com.startic.gestion.models.EstadosInventario;
import py.com.startic.gestion.datasource.ReportesInventario;
import py.com.startic.gestion.datasource.ReportesInventario2;
import py.com.startic.gestion.models.Empresas;
import py.com.startic.gestion.models.ReporteInventarioValorizado;
import py.com.startic.gestion.models.Sede;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "inventariosController")
@ViewScoped
public class InventariosController extends AbstractController<Inventarios> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private DetallesInventarioController detallesInventarioController;
    @Inject
    private EstadosInventarioController estadoController;
    @Inject
    private ArticulosController articulosController;
    @Inject
    private SedeController sedeController;

    private Collection<DetallesInventario> detalles;

    private DetallesInventario detallesSelected;
    private List<Sede> listadepositoFiltroInventario;

    private Date fechaHasta;
    private Date fechaDesde;
    private BigDecimal costoTotal;
    private Articulos articulo;
    private Usuarios usuario;
    private FiltroURL filtro = new FiltroURL();
    private DetallesInventario detalleInventario;
    private List<Articulos> listaArticulos;
    private Sede sede;
    private String vencimiento;
    private BigInteger cantEntrada;
    private BigInteger cantBaja;
    private BigInteger cantSalida;
    private BigInteger neto;
    private BigDecimal precioUnitario;
    private BigDecimal netoValorizado;
    private BigInteger cantInventario;

    public DetallesInventario getDetalleInventario() {
        return detalleInventario;
    }

    public void setDetalleInventario(DetallesInventario detalleInventario) {
        this.detalleInventario = detalleInventario;
    }

    public DetallesInventario getDetallesSelected() {
        return detallesSelected;
    }

    public void setDetallesSelected(DetallesInventario detallesSelected) {
        this.detallesSelected = detallesSelected;
    }

    public Collection<DetallesInventario> getDetalles() {
        return detalles;
    }

    public void setDetalles(Collection<DetallesInventario> detalles) {
        this.detalles = detalles;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }

    public InventariosController() {
        // Inform the Abstract parent controller of the concrete Inventarios Entity
        super(Inventarios.class);
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public List<Articulos> getListaArticulos() {
        return listaArticulos;
    }

    public void setListaArticulos(List<Articulos> listaArticulos) {
        this.listaArticulos = listaArticulos;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public List<Sede> getListadepositoFiltroInventario() {
        return listadepositoFiltroInventario;
    }

    public void setListadepositoFiltroInventario(List<Sede> listadepositoFiltroInventario) {
        this.listadepositoFiltroInventario = listadepositoFiltroInventario;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public BigInteger getCantInventario() {
        return cantInventario;
    }

    public void setCantInventario(BigInteger cantInventario) {
        this.cantInventario = cantInventario;
    }

    public BigInteger getCantEntrada() {
        return cantEntrada;
    }

    public void setCantEntrada(BigInteger cantEntrada) {
        this.cantEntrada = cantEntrada;
    }

    public BigInteger getCantBaja() {
        return cantBaja;
    }

    public void setCantBaja(BigInteger cantBaja) {
        this.cantBaja = cantBaja;
    }

    public BigInteger getCantSalida() {
        return cantSalida;
    }

    public void setCantSalida(BigInteger cantSalida) {
        this.cantSalida = cantSalida;
    }

    public BigInteger getNeto() {
        return neto;
    }

    public void setNeto(BigInteger neto) {
        this.neto = neto;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getNetoValorizado() {
        return netoValorizado;
    }

    public void setNetoValorizado(BigDecimal netoValorizado) {
        this.netoValorizado = netoValorizado;
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        estadoController.setSelected(null);

        seleccionar();
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usuario = (Usuarios) session.getAttribute("Usuarios");

        setItems(ejbFacade.getEntityManager().createNamedQuery("Inventarios.findOrdered", Inventarios.class).getResultList());
        if (!getItems2().isEmpty()) {
            Inventarios art = getItems2().iterator().next();
            setSelected(art);

            agregarAInventario();

            seleccionar();
            // detalles = art.getDetallesInventarioCollection();
            // detallesSelected = null;
        } else {
            setSelected(null);
        }
    }

    private List<Articulos> obtenerListaArticulos(Sede sede) {
        if (sede != null) {
            return this.ejbFacade.getEntityManager().createNamedQuery("Articulos.findByDeposito", Articulos.class
            ).setParameter("sede", sede).getResultList();
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

    private void seleccionar() {
        if (getSelected() != null) {
            ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
            detalles = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findByInventario", DetallesInventario.class).setParameter("inventario", getSelected()).getResultList();
        } else {
            detalles = null;
        }

        detallesSelected = null;

    }

    public boolean permisoAdminInventario() {
        return filtro.verifPermiso("adminInventario");
    }

    public String datePattern() {
        return "dd/MM/yyyy";
    }

    public String customFormatDate(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern());
            return format.format(date);
        }
        return "";
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        String key = event.getRowKey();

        // if (key != null && !((Integer) oldValue).equals((Integer) newValue)) {
        boolean ok = false;

        if (oldValue != null && newValue != null) {
            ok = key != null && !(oldValue).equals(newValue);
        } else {
            ok = !(oldValue == null && newValue == null);
        }

        if (ok) {
            detallesSelected = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findById", DetallesInventario.class).setParameter("id", Integer.valueOf(key)).getSingleResult();

            if (newValue instanceof Integer) {
                detallesSelected.setCantidad((Integer) newValue);
                if (detallesSelected.getCostoUnitario() == null) {
                    detallesSelected.setCostoUnitario(BigDecimal.ZERO);
                }
                detallesSelected.setCostoTotal(detallesSelected.getCostoUnitario().multiply(new BigDecimal(detallesSelected.getCantidad())));
            }

            if (newValue instanceof BigDecimal) {
                detallesSelected.setCostoUnitario((BigDecimal) newValue);
                detallesSelected.setCostoTotal(detallesSelected.getCostoUnitario().multiply(new BigDecimal(detallesSelected.getCantidad())));
            }

            if (newValue instanceof Date) {
                detallesSelected.setFechaVencimiento((Date) newValue);
            }
            //Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class).setParameter("codigo", detallesSelected.getArticulo().getCodigo()).getSingleResult();

            //art.setStock(art.getStock() + detallesSelected.getCantidad());
            // articulosController.setSelected(art);
            // articulosController.save(null);
            detallesInventarioController.setSelected(detallesSelected);
            detallesInventarioController.save(null);
            seleccionar();

        }
    }

    @Override
    public Collection<Inventarios> getItems() {
        return super.getItems2();

    }

    @Override
    public void delete(ActionEvent event) {

        if (getSelected() != null) {
            if ("PR".equals(getSelected().getEstado().getCodigo())) {

                Collection<DetallesInventario> col = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findByInventario", DetallesInventario.class).setParameter("inventario", getSelected()).getResultList();

                for (DetallesInventario det : col) {
                    detallesInventarioController.setSelected(det);
                    detallesInventarioController.delete(event);
                }
                super.delete(event);

                detalles = null;
                detallesSelected = null;
                setItems(ejbFacade.getEntityManager().createNamedQuery("Inventarios.findOrdered", Inventarios.class).getResultList());

            } else {
                JsfUtil.addErrorMessage("Solo se puede borrar un inventario en proceso");
            }
        }
    }

    public void cerrarInventario() {
        if (getSelected() != null) {
            if ("PR".equals(getSelected().getEstado().getCodigo())) {

                try {
                    Inventarios inv = ejbFacade.getEntityManager().createNamedQuery("Inventarios.findVigente", Inventarios.class).getSingleResult();

                    inv.setEstado(ejbFacade.getEntityManager().createNamedQuery("EstadosInventario.findByCodigo", EstadosInventario.class).setParameter("codigo", "CE").getSingleResult());

                    Inventarios invTmp = getSelected();

                    setSelected(inv);

                    save(null);

                    setSelected(invTmp);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                getSelected().setEstado(ejbFacade.getEntityManager().createNamedQuery("EstadosInventario.findByCodigo", EstadosInventario.class).setParameter("codigo", "TE").getSingleResult());

                getSelected().setFecha(ejbFacade.getSystemDate());

                save(null);

                Articulos art = null;

                Collection<DetallesInventario> col = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findByInventario", DetallesInventario.class).setParameter("inventario", getSelected()).getResultList();

                for (DetallesInventario det : col) {
                    art = det.getArticulo();
                    art.setStock(det.getCantidad());

                    articulosController.setSelected(art);
                    articulosController.save(null);

                }

                setItems(ejbFacade.getEntityManager().createNamedQuery("Inventarios.findOrdered", Inventarios.class).getResultList());

            } else {
                JsfUtil.addErrorMessage("El inventario ya se encuentra finalizado");
            }
        }
    }

    public void nuevoInventario() {
        Inventarios invEnProceso = null;
        try {
            invEnProceso = ejbFacade.getEntityManager().createNamedQuery("Inventarios.findEnProceso", Inventarios.class).getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (invEnProceso != null) {
            JsfUtil.addErrorMessage("Ya existe un inventario en proceso. Terminelo o borrelo antes de crear uno nuevo");
            return;
        }

        setSelected(prepareCreate(null));

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

        Date fecha = ejbFacade.getSystemDate();

        getSelected().setFechaHoraAlta(fecha);
        getSelected().setFechaHoraUltimoEstado(fecha);
        getSelected().setUsuarioAlta(usu);
        getSelected().setUsuarioUltimoEstado(usu);
        getSelected().setEmpresa(usu.getEmpresa());
        //getSelected().setSede(usu.getSede());

        getSelected().setEstado(ejbFacade.getEntityManager().createNamedQuery("EstadosInventario.findByCodigo", EstadosInventario.class).setParameter("codigo", "PR").getSingleResult());

        saveNew(null);

        Collection<Articulos> col = ejbFacade.getEntityManager().createNamedQuery("Articulos.findAll", Articulos.class).getResultList();
        for (Articulos art : col) {
            DetallesInventario det = detallesInventarioController.prepareCreate(null);

            det.setArticulo(art);
            det.setSede(art.getSede());
            det.setMarca(art.getMarca());
            det.setModelo(art.getModelo());
            det.setCantidad(0);
            det.setEmpresa(usu.getEmpresa());
            det.setFechaHoraAlta(fecha);
            det.setFechaHoraUltimoEstado(fecha);
            det.setUsuarioAlta(usu);
            det.setUsuarioUltimoEstado(usu);
            det.setInventario(getSelected());

            detallesInventarioController.setSelected(det);

            detallesInventarioController.saveNew2(null);
        }

        setItems(ejbFacade.getEntityManager().createNamedQuery("Inventarios.findOrdered", Inventarios.class).getResultList());

        setSelected(ejbFacade.getEntityManager().createNamedQuery("Inventarios.findEnProceso", Inventarios.class).getSingleResult());

        detalles = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findByInventario", DetallesInventario.class).setParameter("inventario", getSelected()).getResultList();

    }

    public void agregarAInventario() {
        Inventarios invEnProceso = null;
        try {
            invEnProceso = ejbFacade.getEntityManager().createNamedQuery("Inventarios.findEnProceso", Inventarios.class).getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (invEnProceso != null) {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            // boolean encontro = false;
            Collection<Articulos> col = ejbFacade.getEntityManager().createNamedQuery("Articulos.findAll", Articulos.class).getResultList();
            for (Articulos art : col) {
                /*
                encontro = false;
                Collection<DetallesInventario> colInv = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findByInventario", DetallesInventario.class).setParameter("inventario", invEnProceso).getResultList();

                for (DetallesInventario det : colInv) {
                    if (det.getArticulo().equals(art)) {
                        encontro = true;
                        break;
                    }
                }
                 */

                Collection<DetallesInventario> colInv = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findByInventarioANDArticulo", DetallesInventario.class).setParameter("inventario", invEnProceso).setParameter("articulo", art).getResultList();

                // if (!encontro) {
                if (colInv.isEmpty()) {
                    // DetallesInventario det = detallesInventarioController.prepareCreate(null);
                    DetallesInventario det = new DetallesInventario();

                    det.setArticulo(art);
                    det.setSede(art.getSede());
                    det.setCantidad(0);
                    det.setCostoUnitario(BigDecimal.ZERO);
                    det.setCostoTotal(BigDecimal.ZERO);
                    det.setEmpresa(usu.getEmpresa());
                    det.setFechaHoraAlta(fecha);
                    det.setFechaHoraUltimoEstado(fecha);
                    det.setUsuarioAlta(usu);
                    det.setUsuarioUltimoEstado(usu);
                    det.setInventario(invEnProceso);

                    detallesInventarioController.setSelected(det);

                    detallesInventarioController.saveNew2(null);
                }
            }
            /*
            setItems(ejbFacade.getEntityManager().createNamedQuery("Inventarios.findOrdered", Inventarios.class).getResultList());

            setSelected(ejbFacade.getEntityManager().createNamedQuery("Inventarios.findEnProceso", Inventarios.class).getSingleResult());

            detalles = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findByInventario", DetallesInventario.class).setParameter("inventario", getSelected()).getResultList();
             */
        }

    }

    public void prepareNuevoDetalleInventario() {
        detalleInventario = new DetallesInventario();
    }

    public void saveNewDetalle() {
        if (detalleInventario != null) {
            detalleInventario.setCantidad(0);
            Date fecha = ejbFacade.getSystemDate();
            detalleInventario.setFechaHoraAlta(fecha);
            detalleInventario.setFechaHoraUltimoEstado(fecha);
            detalleInventario.setInventario(getSelected());
            detalleInventario.setUsuarioAlta(usuario);
            detalleInventario.setUsuarioUltimoEstado(usuario);
            detalleInventario.setEmpresa(new Empresas(1));

            detallesInventarioController.setSelected(detalleInventario);
            detallesInventarioController.saveNew(null);
            seleccionar();
        }
    }

    /**
     * Sets the "items" attribute with a collection of DetallesInventario
     * entities that are retrieved from Inventarios?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for DetallesInventario page
     */
    public String navigateDetallesInventarioCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DetallesInventario_items", this.getSelected().getDetallesInventarioCollection());
        }
        return "/pages5/detallesInventario/index";
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
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareSede(ActionEvent event) {
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

    public void prepareReporteVtoValorizado() {
        fechaHasta = ejbFacade.getSystemDate();
        articulo = null;
    }

    public void prepareReporteFCDoce() {
        articulo = null;
    }

    public void prepareReporteFCDos() {
        articulo = null;
    }

    public void prepareReporteInventario() {
        articulo = null;
    }

    public void prepareReporteInventarioPorDias() {
        fechaDesde = ejbFacade.getSystemDate();
        fechaHasta = ejbFacade.getSystemDate();
        sede = null;
    }

    /*
    public void pdf() {

        HttpServletResponse httpServletResponse = null;
        if (getSelected() != null) {
            try {
                JRBeanCollectionDataSource beanCollectionDataSource = null;
                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
                // beanCollectionDataSource = new JRBeanCollectionDataSource(ejbFacade.getEntityManager().createNamedQuery("ReportesInventario.findByInventario", ReportesInventario.class).setParameter("inventario", getSelected().getId()).getResultList());

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

                String articuloStr = "";
                if (articulo != null) {
                    articuloStr = "and s.articulo = '" + articulo.getCodigo() + "' ";
                }

                javax.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                        "select s.codigo, s.articulo, m.descripcion as marca, o.descripcion as modelo, s.inventario, s.fecha, s.descripcion, s.simbolo, s.cant_inventario, s.cant_entrada, s.cant_salida, s.neto, s.stock "
                        + "from (select @reporte_inventario_hasta := str_to_date('" + format2.format(fechaHasta) + "', '%d/%m/%Y')) a,  reportes_inventario2 s left join marcas as m on (s.marca = m.id) left join modelos as o on (s.modelo = o.id) where "
                        + "s.inventario = " + getSelected().getId() + " "
                        + articuloStr
                        + " order by s.descripcion",
                         ReportesInventario.class);

                beanCollectionDataSource = new JRBeanCollectionDataSource(query.getResultList());

                HashMap map = new HashMap();

                Date fecha = ejbFacade.getSystemDate();

                map.put("fecha", format.format(fecha));
                map.put("hasta", format2.format(fechaHasta));
                map.put("estado", getSelected().getEstado().getDescripcion());

                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteInventarios.jasper");
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                FacesContext.getCurrentInstance().responseComplete();

            } catch (Exception e) {
                FacesContext.getCurrentInstance().getExf2ternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
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
    }
     */
    public boolean deshabilitarCreateDetalle() {
        if (getSelected() != null) {
            if (getSelected().getEstado() != null) {
                return !Constantes.ESTADO_INVENTARIO_PENDIENTE.equals(getSelected().getEstado().getCodigo());
            }
        }

        return true;
    }

    public List<ReportesInventario> obtenerDatosInventario(Date fecha) {

        try {
            Inventarios inv = ejbFacade.getEntityManager().createNamedQuery("Inventarios.findVigente", Inventarios.class).getSingleResult();

            return obtenerDatosInventario(fecha, null, inv);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ReportesInventario> obtenerDatosInventario2(Date fecha, Articulos articulo) {

        try {
            Inventarios inv = ejbFacade.getEntityManager().createNamedQuery("Inventarios.findVigente", Inventarios.class).getSingleResult();
            List<ReportesInventario2> rrr = obtenerDatosInventario2(fecha, articulo.getCodigo(), inv);

            List<ReportesInventario> retorno = new ArrayList<>();

            for (ReportesInventario2 r2 : rrr) {
                ReportesInventario ri = new ReportesInventario();
                ri.setArticulo(r2.getArticulo());
                ri.setCodigo(r2.getCodigo());
                ri.setMarca(r2.getMarca());
                ri.setSede(r2.getSede());
                ri.setInventario(r2.getInventario());
                ri.setFecha(r2.getFecha());
                ri.setDescripcion(r2.getDescripcion());
                ri.setSimbolo(r2.getSimbolo());
                ri.setCantInventario(r2.getCantInventario());
                ri.setCantEntrada(r2.getCantEntrada());
                ri.setCantSalida(r2.getCantSalida());
                ri.setCantBaja(r2.getCantBaja());
                ri.setNeto(r2.getNeto());
                ri.setPrecioUnitario(r2.getPrecioUnitario());
                ri.setStock(r2.getStock());
                ri.setVencimiento(r2.getVencimiento());
                ri.setNetoValorizado(r2.getNetoValorizado());
                retorno.add(ri);
            }

            return retorno;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ReportesInventario> obtenerDatosInventario(Date fechaHasta, String filtroArticulo, Inventarios inv) {

        // String where = " and IFNULL(s.stock,0) <> 0 ";
        String where = "";

        if (filtroArticulo != null) {
            where = filtroArticulo;

        }

        SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
        javax.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                "select s.codigo, s.articulo, ifnull(m.descripcion,'') as marca, ifnull(o.descripcion,'') as modelo, s.inventario, s.fecha, s.descripcion, s.simbolo, s.cant_inventario,s.sede, s.cant_entrada, s.cant_salida,s.cant_baja, s.neto, s.stock "
                + "from (select @reporte_inventario_hasta := str_to_date('" + format2.format(fechaHasta) + "', '%d/%m/%Y')) a,  reportes_inventario2 s left join marcas as m on (s.marca = m.id) left join modelos as o on (s.modelo = o.id) where "
                + "s.inventario = " + inv.getId() + " "
                + where
                + " order by s.descripcion",
                ReportesInventario.class);
        return query.getResultList();
    }

    public List<ReportesInventario2> obtenerDatosInventario2(Date fechaHasta, String articulo, Inventarios inv) {

        // String where = " and IFNULL(s.stock,0) <> 0 ";
        SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
        List<ReportesInventario2> rr = ejbFacade.getEntityManager()
                .createNamedQuery("ReportesInventario2.findByArtInvFecha", ReportesInventario2.class)
                .setParameter("inventario", inv.getId())
                .setParameter("articulo", articulo)
                .setParameter("fecha", fechaHasta)
                .getResultList();
        return rr;
    }

    public void pdf(boolean generarPdf) {

        if (getSelected() != null) {

            HttpServletResponse httpServletResponse = null;
            // if (getSelected() != null) {
            try {
                JRBeanCollectionDataSource beanCollectionDataSource = null;
                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
                // beanCollectionDataSource = new JRBeanCollectionDataSource(ejbFacade.getEntityManager().createNamedQuery("ReportesInventario.findByInventario", ReportesInventario.class).setParameter("inventario", getSelected().getId()).getResultList());

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

                String articuloStr = "";
                if (articulo != null) {
                    articuloStr = "and s.articulo = '" + articulo.getCodigo() + "' ";
                } else if (articulo == null) {
                    articuloStr = " and IFNULL(s.neto,0) <> 0 ";
                }
                /*  String sedeStr = "";
                if (sede != null) {
                    sedeStr = "and s.sede = '" + sede.getId() + "' ";
                }*/

 /*
                javax.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                        "select s.codigo, s.articulo, ifnull(m.descripcion,'') as marca, ifnull(o.descripcion,'') as modelo, s.inventario, s.fecha, s.descripcion, s.simbolo, s.cant_inventario, s.cant_entrada, s.cant_salida, s.neto, s.stock "
                        + "from (select @reporte_inventario_hasta := str_to_date('" + format2.format(fechaHasta) + "', '%d/%m/%Y')) a,  reportes_inventario2 s left join marcas as m on (s.marca = m.id) left join modelos as o on (s.modelo = o.id) where "
                        + "s.inventario = " + getSelected().getId() + " "
                        + articuloStr
                        + " order by s.descripcion",
                        ReportesInventario.class);
                
                List<ReportesInventario> resultado = query.getResultList();
                 */
                //List<ReportesInventario> resultado = obtenerDatosInventario(fechaHasta, articuloStr, getSelected());
                List<ReportesInventario2> rrr = obtenerDatosInventario2(fechaHasta, (articulo == null ? "" : articulo.getCodigo()), getSelected());
                List<ReportesInventario> resultado = new ArrayList<>();

                for (ReportesInventario2 r2 : rrr) {
                    ReportesInventario ri = new ReportesInventario();
                    ri.setArticulo(r2.getArticulo());
                    ri.setCodigo(r2.getCodigo());
                    ri.setMarca(r2.getMarca());
                    ri.setSede(r2.getSede());
                    ri.setInventario(r2.getInventario());
                    ri.setFecha(r2.getFecha());
                    ri.setDescripcion(r2.getDescripcion());
                    ri.setSimbolo(r2.getSimbolo());
                    ri.setCantInventario(r2.getCantInventario());
                    ri.setCantEntrada(r2.getCantEntrada());
                    ri.setCantSalida(r2.getCantSalida());
                    ri.setCantBaja(r2.getCantBaja());
                    ri.setNeto(r2.getNeto());
                    ri.setPrecioUnitario(r2.getPrecioUnitario());
                    ri.setStock(r2.getStock());
                    ri.setVencimiento(r2.getVencimiento());
                    ri.setNetoValorizado(r2.getNetoValorizado());
                    resultado.add(ri);
                }
                if (!resultado.isEmpty()) {
                    for (ReportesInventario res : resultado) {
                        int cantSalida = res.getCantSalida().intValue();
                        int cantEntrada = res.getCantEntrada().intValue();
                        int cantInv = res.getCantInventario().intValue();
                        /*
                        javax.persistence.Query query2 = ejbFacade.getEntityManager().createNativeQuery("select a.articulo as codigo, ifnull(a.costo_unitario,0) as precio_unitario, ifnull(date_format(a.fecha_vencimiento, '%d-%m-%Y'), '') as vencimiento from detalles_entrada_articulo as a, entradas_articulo as e\n"
                                + "where a.entrada_articulo = e.id and a.articulo = ?1\n"
                                + "and (e.fecha_entrada,e.fecha_hora_alta) in (select max(c.fecha_entrada), max(c.fecha_hora_alta) from detalles_entrada_articulo as b, entradas_articulo as c where b.entrada_articulo = c.id and b.articulo = a.articulo);", ReportesInventario.class).setParameter(1, res.getArticulo());
                         */
                        // + "union\n"
                        /*javax.persistence.Query query3 = ejbFacade.getEntityManager().createNativeQuery("select a.articulo as codigo, ifnull(a.costo_unitario,0) as precio_unitario, ifnull(date_format(a.fecha_vencimiento, '%d-%m-%Y'), '') as vencimiento from detalles_inventario as a, inventarios as e\n"
                                + "where a.inventario = e.id and a.articulo = ?1\n"
                                + "and (e.fecha,e.fecha_hora_alta) in (select max(c.fecha), max(c.fecha_hora_alta) from detalles_inventario as b, inventarios as c where b.inventario = c.id and b.articulo = a.articulo);", ReportesInventario.class).setParameter(1, res.getArticulo());
                       /*
                        // List<DetallesInventario> listInvLat = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findLastByArticulo", DetallesInventario.class).setParameter("articulo", articulo.getCodigo()).getResultList();
                         List<ReportesInventario> listInvLat = query3.getResultList();
                       //  List<ReportesInventario> inv = query2.getResultList();
                        /*
                        if(!listInvLat.isEmpty()){
                            res.setPrecioUnitario(listInvLat.get(0).getPrecioUnitario());
                            res.setVencimiento(listInvLat.get(0).getVencimiento());
                            BigDecimal resul = listInvLat.get(0).getPrecioUnitario() == null ? BigDecimal.ZERO : (listInvLat.get(0).getPrecioUnitario().multiply(new BigDecimal(res.getNeto())) == null ? BigDecimal.ZERO : listInvLat.get(0).getPrecioUnitario().multiply(new BigDecimal(res.getNeto())));
                            res.setNetoValorizado(resul);
                        }else {
                            res.setPrecioUnitario(BigDecimal.ZERO);
                            res.setVencimiento("");
                            res.setNetoValorizado(BigDecimal.ZERO);
                            }
                         */

                        Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class).setParameter("codigo", res.getArticulo()).getSingleResult();
                        res.setoGasto(art.getObjetoGasto() == null ? Integer.valueOf(0) : art.getObjetoGasto().getCodigo());
                    }
                }

                beanCollectionDataSource = new JRBeanCollectionDataSource(resultado);

                HashMap map = new HashMap();

                Date fecha = ejbFacade.getSystemDate();

                map.put("fecha", format.format(fecha));
                map.put("hasta", format2.format(fechaHasta));
                map.put("estado", getSelected().getEstado().getDescripcion());

                JasperPrint jasperPrint = null;
                ServletOutputStream servletOutputStream = null;

                if (generarPdf) {

                    String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteInventarios.jasper");
                    jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                    httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                    httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                    servletOutputStream = httpServletResponse.getOutputStream();
                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

                } else {
                    String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteInventarioExcel.jasper");
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
        }
    }

    public void ReporteVtoValorizado(boolean generarPdf) {

        if (getSelected() != null) {

            HttpServletResponse httpServletResponse = null;
            // if (getSelected() != null) {
            try {
                JRBeanCollectionDataSource beanCollectionDataSource = null;
                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
                // beanCollectionDataSource = new JRBeanCollectionDataSource(ejbFacade.getEntityManager().createNamedQuery("ReportesInventario.findByInventario", ReportesInventario.class).setParameter("inventario", getSelected().getId()).getResultList());

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

                String articuloStr = "";
                if (articulo != null) {
                    articuloStr = "and e.articulo = '" + articulo.getCodigo() + "' ";
                } else if (articulo == null) {
                    articuloStr = " and IFNULL(e.valorizado,0) <> 0 ";
                }

                List<RepInventarioValorizadoSuma> lista = obtenerInventarioAgrupadoPorVencimiento(articuloStr, getSelected().getId());

                beanCollectionDataSource = new JRBeanCollectionDataSource(lista);

                HashMap map = new HashMap();

                Date fecha = ejbFacade.getSystemDate();

                map.put("fecha", format.format(fecha));
                map.put("hasta", format2.format(fechaHasta));

                JasperPrint jasperPrint = null;
                ServletOutputStream servletOutputStream = null;

                if (generarPdf) {

                    String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteInventarioValorizado.jasper");
                    jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                    httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                    httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                    servletOutputStream = httpServletResponse.getOutputStream();
                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

                } else {
                    String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteInventarioValorizadoExcel.jasper");
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
        }
    }

    public void ReporteVtoValorizado2(boolean generarPdf) {

        if (getSelected() != null) {

            HttpServletResponse httpServletResponse = null;
            // if (getSelected() != null) {
            try {
                JRBeanCollectionDataSource beanCollectionDataSource = null;
                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
                // beanCollectionDataSource = new JRBeanCollectionDataSource(ejbFacade.getEntityManager().createNamedQuery("ReportesInventario.findByInventario", ReportesInventario.class).setParameter("inventario", getSelected().getId()).getResultList());

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

                String sedeStr = "";
                if (sede != null) {
                    sedeStr = "and e.sede = '" + sede.getId() + "' ";
                }

                List<RepInventarioValorizadoSuma> lista = obtenerInventarioAgrupadoPorVencimiento(sedeStr, getSelected().getId());

                beanCollectionDataSource = new JRBeanCollectionDataSource(lista);

                HashMap map = new HashMap();

                Date fecha = ejbFacade.getSystemDate();

                // map.put("fecha", format.format(fecha));
                map.put("desde", format2.format(fechaDesde));
                map.put("hasta", format2.format(fechaHasta));

                JasperPrint jasperPrint = null;
                ServletOutputStream servletOutputStream = null;

                if (generarPdf) {

                    String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteInventarioValorizado.jasper");
                    jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                    httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                    httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                    servletOutputStream = httpServletResponse.getOutputStream();
                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

                } else {
                    String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteInventarioValorizadoExcel.jasper");
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
        }
    }

    public List<DatosFCDoce> obtenerDatosFCDoce(Articulos articulo) {

        try {
            Inventarios inv = ejbFacade.getEntityManager().createNamedQuery("Inventarios.findVigente", Inventarios.class).getSingleResult();

            return obtenerDatosFCDoce(articulo, inv);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DatosFCDoce> obtenerDatosFCDoce(Articulos articulo, Inventarios inv) {

        javax.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                "select concat('+', '0', date_format(i.fecha , '%Y%m%d'), i.id, 0, 'INVENTARIO') as codigo,\n"
                + " 0 as entradaSalida,\n"
                + " i.fecha as fecha,\n"
                + " i.id as numero,\n"        
                + " 0 as idProcedenciaDestino,\n"
                + " 'INVENTARIO' as descripcionProcedenciaDestino,\n"
                + " sum(d.cantidad) as cantidad\n"
                + " from gestionstarticv2.inventarios i,\n"
                + "	gestionstarticv2.detalles_inventario d\n"
                + " where i.id = d.inventario\n"
                + " and d.articulo = ?5\n"
                + " and i.id = ?6\n"
                + " GROUP BY concat('+', '0', date_format(i.fecha , '%Y%m%d'), i.id, 0, 'INVENTARIO'),\n"
                + "i.fecha,\n"      
                + "i.id\n"
                + " UNION "
                + "select concat('+','0',date_format(s.fecha_entrada,'%Y%m%d'), s.nro_factura, e.id, e.nombre) as codigo, 0 as entradaSalida, s.fecha_entrada as fecha, s.nro_factura as numero, e.id as idProcedenciaDestino, e.nombre as descripcionProcedenciaDestino, sum(d.cantidad) as cantidad\n"
                + "                from entradas_articulo as s, detalles_entrada_articulo as d, proveedores as e \n"
                + "                where s.id = d.entrada_articulo\n"
                + "                and s.proveedor = e.id\n"
                + "                and s.inventario = ?3\n"
                + "                and d.articulo = ?1\n"
                + "                group by concat('+','0',date_format(s.fecha_entrada,'%Y%m%d'), s.nro_factura, e.id, e.nombre), s.fecha_entrada, s.nro_factura, e.id, e.nombre\n"
                + "                union\n"
                + "                select concat('-','0',date_format(s.fecha_salida,'%Y%m%d'), s.nro_formulario, e.id, e.nombre) as codigo, 1 as entrada_salida, s.fecha_salida as fecha, s.nro_formulario as numero, e.id as idProcedenciaDestino, e.nombre as descripcionProcedenciaDestino, sum(d.cantidad) as cantidad \n"
                + "                from salidas_articulo as s, detalles_salida_articulo as d, departamentos as e \n"
                + "                where s.id = d.salida_articulo\n"
                + "                and e.id = d.departamento\n"
                + "                and s.inventario = ?4\n"
                + "                and d.articulo = ?2\n"
                + "                group by concat('+','0',date_format(s.fecha_salida,'%Y%m%d'), s.nro_formulario, e.id, e.nombre), s.fecha_salida,s.nro_formulario, e.id, e.nombre\n"
                + "                order by fecha",
                DatosFCDoce.class)
                .setParameter(1, articulo.getCodigo())
                .setParameter(2, articulo.getCodigo())
                .setParameter(3, inv.getId())
                .setParameter(4, inv.getId())
                .setParameter(5, articulo.getCodigo())
                .setParameter(6, inv.getId());

        return query.getResultList();
    }

    public void pdfFCDoce() {
        if (getSelected() != null) {

            if (articulo == null) {
                JsfUtil.addErrorMessage("Debe elegir un artículo");
                return;
            }
            JRBeanCollectionDataSource beanCollectionDataSource = null;
            HashMap map = null;
            try {
            List<DatosFCDoce> resultado = obtenerDatosFCDoce(articulo, getSelected());

            javax.persistence.Query query2 = ejbFacade.getEntityManager().createNativeQuery("select a.articulo as codigo, ifnull(a.costo_unitario,0) as precio_unitario, ifnull(date_format(a.fecha_vencimiento, '%d-%m-%Y'), '') as vencimiento from detalles_entrada_articulo as a, entradas_articulo as e\n"
                    + "where a.entrada_articulo = e.id and a.articulo = ?1\n"
                    + "and e.fecha_entrada in (select max(c.fecha_entrada) from detalles_entrada_articulo as b, entradas_articulo as c where b.entrada_articulo = c.id and b.articulo = a.articulo) order by vencimiento asc;", ReportesInventario.class).setParameter(1, articulo.getCodigo());

            List<ReportesInventario> ultimaEntrada = query2.getResultList();
            BigDecimal precioPromedio;

            if (!ultimaEntrada.isEmpty()) {
                precioPromedio = ultimaEntrada.get(0).getPrecioUnitario();
            } else {
                precioPromedio = BigDecimal.ZERO;
            }
            List<DetallesInventario> listInvLat = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findLastByArticulo", DetallesInventario.class).setParameter("articulo", articulo.getCodigo()).getResultList();

            //List<ReportesInventario> inicialInventario = query3.getResultList();
            BigDecimal precioInv;

            if (!listInvLat.isEmpty()) {
                precioInv = listInvLat.get(0).getCostoUnitario();
            } else {
                precioInv = BigDecimal.ZERO;
            }

            List<DetallesInventario> listaDet = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findByInventario", DetallesInventario.class).setParameter("inventario", getSelected()).getResultList();

            int invInicial = 0;
            for (DetallesInventario ini : listaDet) {
                if (ini.getArticulo().getCodigo().equals(articulo.getCodigo())) {
                    invInicial = ini.getCantidad();

                    break;
                }
            }

            List<RepFCDoce> listaRepFCDoce = new ArrayList<>();

            RepFCDoce repFCDoce;

            Integer saldo = 0;
            
            BigDecimal vSaldoSum = BigDecimal.ZERO;
            for (DatosFCDoce res : resultado) {

                repFCDoce = new RepFCDoce();

                repFCDoce.setFecha(res.getFecha());
                repFCDoce.setNumero(res.getNumero());
                repFCDoce.setSede(res.getSede());
                repFCDoce.setProcedencia(res.getDescripcionProcedenciaDestino());

                if (res.getEntradaSalida() == 0) {
                    repFCDoce.setInvInicial(0);
                    repFCDoce.setPrecioPromedio(precioPromedio);
                    repFCDoce.setEntradas(res.getCantidad());
                    repFCDoce.setSalidas(0);
                    saldo = saldo + res.getCantidad();
                    repFCDoce.setvEntradas(precioPromedio.multiply(new BigDecimal(res.getCantidad())));
                    repFCDoce.setvSalidas(BigDecimal.ZERO);
                    repFCDoce.setTipo("Factura");
                    repFCDoce.setSaldo(saldo);
                    if (res.getDescripcionProcedenciaDestino().equals("INVENTARIO")) {
                        repFCDoce.setPrecioPromedio(precioInv!=null?precioInv:BigDecimal.ZERO);
                        repFCDoce.setEntradas(/*res.getCantidad()*/invInicial);
                        repFCDoce.setSalidas(0);
                        repFCDoce.setvEntradas((precioInv!=null?precioInv:BigDecimal.ZERO).multiply( res.getCantidad()!=null?(new BigDecimal(res.getCantidad())):BigDecimal.ZERO ));
                        repFCDoce.setvSalidas(BigDecimal.ZERO);
                        repFCDoce.setTipo("Inventario");
                       // repFCDoce.setSaldo(saldo);
                    }
                    vSaldoSum = vSaldoSum.add(repFCDoce.getvEntradas());
                    repFCDoce.setvSaldo(vSaldoSum);

                } else if (res.getEntradaSalida() != 0) {
                    repFCDoce.setInvInicial(0);
                    repFCDoce.setSalidas(res.getCantidad());
                    repFCDoce.setEntradas(0);
                    repFCDoce.setPrecioPromedio(precioPromedio);
                    repFCDoce.setTipo("Formulario");
                    saldo = saldo - res.getCantidad();
                    repFCDoce.setvSalidas(precioPromedio.multiply(new BigDecimal(res.getCantidad())));
                    repFCDoce.setvEntradas(BigDecimal.ZERO);
                    repFCDoce.setSaldo(saldo);

                } else {
                    repFCDoce.setEntradas(0);
                    repFCDoce.setSalidas(res.getCantidad());
                    repFCDoce.setEntradas(0);
                    repFCDoce.setPrecioPromedio(precioPromedio);
                    repFCDoce.setTipo("Formulario");
                    saldo = saldo - res.getCantidad();
                    repFCDoce.setvSalidas(precioInv.multiply(new BigDecimal(res.getCantidad())));
                    repFCDoce.setvEntradas(BigDecimal.ZERO);
                    repFCDoce.setSaldo(saldo);
                }
                vSaldoSum = vSaldoSum.subtract(repFCDoce.getvSalidas());
                repFCDoce.setvSaldo(vSaldoSum);
                listaRepFCDoce.add(repFCDoce);

            }
            beanCollectionDataSource = new JRBeanCollectionDataSource(listaRepFCDoce);

            map = new HashMap();

            Date fecha = ejbFacade.getSystemDate();
            map.put("descripcionArticulo", articulo.getDescripcion());
            map.put("descCodigo", articulo.getCodigo());
            map.put("descminimo", articulo.getStockCritico());
            map.put("desccritico", articulo.getStockCritico());
            map.put("descDeposito", articulo.getRubro().getSede().getDescripcion());
            } catch ( Exception e){
                System.out.println("ERROR EN REPORTE FC12" + e.getMessage());
                e.printStackTrace();
                System.out.println("___________ERROR REPORTE______________");
            }
            JasperPrint jasperPrint = null;
            ServletOutputStream servletOutputStream = null;

            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteRegistrodeBienesConsumoExcel.jasper");
            HttpServletResponse httpServletResponse = null;
            try {
                jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                httpServletResponse.addHeader("Content-disposition", "filename=reporte.xlsx");

                servletOutputStream = httpServletResponse.getOutputStream();

                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                exporter.exportReport();
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
    }

    public void pdfFCDos() {
        if (getSelected() != null) {

//            if (articulo == null) {
//                JsfUtil.addErrorMessage("Debe elegir un artículo");
//                return;
//}
            // List<DatosFCDoce> resultado = obtenerDatosFCDoce(articulo, getSelected());
            String articuloStr = "";

            if (articulo != null) {
                articuloStr = "and e.articulo = '" + articulo.getCodigo() + "' ";
            } else if (articulo == null) {
                articuloStr = " and IFNULL(e.valorizado,0) <> 0 ";
            }

            // List<ReportesInventario> resultado = obtenerDatosInventario(ejbFacade.getSystemDate(), articuloStr, getSelected());
            List<RepInventarioValorizadoSuma> resultado = ejbFacade.getEntityManager().createNativeQuery("select * from (select concat(inventario,articulo,e.costo_unitario,e.cant_entrada,sum(e.cant_salida)) as codigo, m.descripcion as marca, o.codigo as oGasto, o.descripcion as objetoGasto, e.inventario, e.articulo, a.descripcion as descripcion, u.simbolo as simbolo, costo_unitario, e.cant_entrada as suma_cant_entrada, sum(e.cant_salida) as suma_cant_salida, e.cant_entrada - sum(e.cant_salida) as suma_saldo, (e.cant_entrada - sum(e.cant_salida)) * costo_unitario as saldo_valorizado from reporte_inventario_valorizado AS e, articulos as a left join marcas as m on (a.marca = m.id) left join objetos_gasto as o on (a.objeto_gasto = o.id) left join unidades as u on (a.unidad = u.codigo) WHERE e.articulo = a.codigo AND e.inventario = ? " + articuloStr + " group by e.inventario, e.articulo, u.simbolo, e.costo_unitario, e.cant_entrada ORDER BY e.articulo, e.costo_unitario)c where c.saldo_valorizado <> 0;", RepInventarioValorizadoSuma.class).setParameter(1, getSelected().getId()).getResultList();

//            List<DetallesInventario> listaDet = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findByInventario", DetallesInventario.class).setParameter("inventario", getSelected()).getResultList();
//            int invInicial = 0;
//            for (DetallesInventario ini : listaDet) {
//                if (ini.getArticulo().getCodigo().equals(articulo.getCodigo())) {
//                    invInicial = ini.getCantidad();
//                    break;
//                }
//            }
            List<RepFCDos> listaRepFCDos = new ArrayList<>();

            RepFCDos repFCDos;

            // Integer saldo = invInicial;
            String cuenta = "2.5.1.01";
            String subCuenta = "2.5.1.01.01";
            // String analitico1 = "";
            //String analitico2 = "";

            for (RepInventarioValorizadoSuma res : resultado) {

                System.out.println("***********************");
                System.out.println("-------CANTIDAD:" + res.getSumaSaldo().toPlainString());
                System.out.println("------ENTRADA:" + res.getSumaCantEntrada().intValue());
                System.out.println("------SALIDA:" + res.getSumaCantSalida().intValue());

                repFCDos = new RepFCDos();

                repFCDos.setCuenta(cuenta);
                repFCDos.setSubCuenta(subCuenta);

                Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class).setParameter("codigo", res.getArticulo()).getSingleResult();

                repFCDos.setAnalitico1(art.getObjetoGasto() != null ? art.getObjetoGasto().getAnalitico_uno() : "");
                repFCDos.setAnalitico2(art.getObjetoGasto() != null ? art.getObjetoGasto().getAnalitico_dos() : "");
                repFCDos.setDescripcion(res.getArticulo() + "-" + res.getDescripcion());
                repFCDos.setUnidadM(res.getSimbolo());
                repFCDos.setCantidad(res.getSumaSaldo().intValue());
                repFCDos.setValorUnit(res.getCostoUnitario());
                repFCDos.setValorTotal(res.getSaldoValorizado());

//                
//                repFCDos.setCantidadF(0);
//                repFCDos.setValorUnitF(BigDecimal.ZERO);
//                repFCDos.setValorTotalF(repFCDos.getValorUnitF().multiply(new BigDecimal(repFCDos.getCantidadF())));
//
//               
//                Integer cantidadDif = repFCDos.getCantidad() - repFCDos.getCantidadF();
//                
//                if(cantidadDif > 0){
//                    repFCDos.setCantidadSob(cantidadDif);
//                    repFCDos.setValorSob(repFCDos.getValorTotal().subtract(repFCDos.getValorTotalF()));
//                    
//                    repFCDos.setCantidadFal(0);
//                    repFCDos.setValorFal(BigDecimal.ZERO);
//                }else{
//                    repFCDos.setCantidadFal(cantidadDif * -1);
//                    repFCDos.setValorFal((new BigDecimal(-1)).multiply(repFCDos.getValorTotal().subtract(repFCDos.getValorTotalF())));
//                    
//                    repFCDos.setCantidadSob(0);
//                    repFCDos.setValorSob(BigDecimal.ZERO);
//                }
//              
                listaRepFCDos.add(repFCDos);

            }

            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listaRepFCDos);

            HashMap map = new HashMap();

            JasperPrint jasperPrint = null;
            ServletOutputStream servletOutputStream = null;

            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteInventarioBienesEnExistencia.jasper");
            HttpServletResponse httpServletResponse = null;
            try {
                jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                httpServletResponse.addHeader("Content-disposition", "filename=reporte.xlsx");

                servletOutputStream = httpServletResponse.getOutputStream();

                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                exporter.exportReport();
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
    }

    private List<RepInventarioValorizadoSuma> obtenerInventarioAgrupadoPorVencimiento(String articuloStr, Integer inventario) {

        // ejbFacade.getEntityManager().createNativeQuery("select concat(inventario,articulo,date_format(fecha_vencimiento,'%Y%m%d')) as codigo, m.descripcion as marca, o.codigo as oGasto, o.descripcion as objetoGasto, e.inventario, e.articulo, a.descripcion as descripcion, case when date_format(e.fecha_vencimiento,'%d/%m/%Y') = '01/01/1900' then '' else date_format(e.fecha_vencimiento,'%d/%m/%Y') end as vencimiento, costo_unitario, sum(e.cant_entrada) as suma_cant_entrada, sum(e.cant_salida) as suma_cant_salida, sum(e.cant_entrada) - sum(e.cant_salida) as suma_saldo, sum(e.valorizado) as saldo_valorizado  from reporte_inventario_valorizado AS e, articulos as a left join marcas as m on (a.marca = m.id) left join objetos_gasto as o on (a.objeto_gasto = o.id) WHERE e.articulo = a.codigo AND e.inventario = ? " + articuloStr + " group by e.inventario, e.articulo, e.fecha_vencimiento, e.costo_unitario ORDER BY e.articulo, e.fecha_vencimiento, e.costo_unitario;", RepInventarioValorizadoSuma.class).setParameter(1, getSelected().getId()).getResultList();
        List<ReporteInventarioValorizadoAgregado> lista = ejbFacade.getEntityManager().createNativeQuery("select * from (select e.codigo, concat(inventario,articulo,date_format(fecha_vencimiento,'%Y%m%d')) as codigo_suma, m.descripcion as marca, e.tipo, o.codigo as oGasto, o.descripcion as objetoGasto, e.inventario, e.articulo, a.descripcion as descripcion, case when date_format(e.fecha_vencimiento,'%d/%m/%Y') = '01/01/1900' then '' else date_format(e.fecha_vencimiento,'%d/%m/%Y') end as fecha_vencimiento_string, costo_unitario, e.cant_entrada, SUM(e.cant_salida) cant_salida, e.detalle_entrada_articulo, e.detalle_inventario, ((e.cant_entrada * costo_unitario) - ( SUM(e.cant_salida) * costo_unitario)) valorizado from reporte_inventario_valorizado AS e, articulos as a left join marcas as m on (a.marca = m.id) left join objetos_gasto as o on (a.objeto_gasto = o.id) WHERE e.articulo = a.codigo AND e.inventario = ? " + articuloStr + " GROUP BY e.codigo, codigo_suma, m.descripcion, e.tipo, o.codigo, o.descripcion, e.inventario, e.articulo, a.descripcion, e.fecha_vencimiento, costo_unitario, e.cant_entrada, e.detalle_entrada_articulo, e.detalle_inventario ORDER BY e.fecha_vencimiento, e.detalle_entrada_articulo, e.detalle_inventario, e.articulo, e.costo_unitario) c where c.valorizado <> 0;", ReporteInventarioValorizadoAgregado.class).setParameter(1, getSelected().getId()).getResultList();

        Integer detalleEntradaArticuloAnt = 0;
        Integer detalleInventarioAnt = 0;
        List<RepInventarioValorizadoSuma> resp = new ArrayList<>();
        boolean primero = true;
        RepInventarioValorizadoSuma suma = null;
        String codigoSuma = "";
        ReporteInventarioValorizadoAgregado det = null;
        int i;
        for (i = 0; i < lista.size(); i++) {
            det = lista.get(i);
            if (!primero) {
                if (!codigoSuma.equals(det.getCodigoSuma())) {
                    resp.add(suma);
                    suma = new RepInventarioValorizadoSuma(det);
                    codigoSuma = det.getCodigoSuma();
                    detalleInventarioAnt = det.getDetalleInventario() == null ? 0 : det.getDetalleInventario().getId();
                    detalleEntradaArticuloAnt = det.getDetalleEntradaArticulo() == null ? 0 : det.getDetalleEntradaArticulo().getId();
                    continue;
                }

                if ("I".equals(det.getTipo())) {
                    if (!detalleInventarioAnt.equals(det.getDetalleInventario() == null ? 0 : det.getDetalleInventario().getId())) {
                        suma.setSumaCantEntrada(suma.getSumaCantEntrada().add(new BigDecimal(det.getCantEntrada())));
                    }
                    detalleInventarioAnt = det.getDetalleInventario().getId();
                }

                if ("E".equals(det.getTipo())) {
                    if (!detalleEntradaArticuloAnt.equals(det.getDetalleEntradaArticulo() == null ? 0 : det.getDetalleEntradaArticulo().getId())) {
                        suma.setSumaCantEntrada(suma.getSumaCantEntrada().add(new BigDecimal(det.getCantEntrada())));
                    }
                    detalleEntradaArticuloAnt = det.getDetalleEntradaArticulo().getId();
                }
                BigDecimal sall = suma.getSumaSaldo();
                int ent = det.getCantEntrada();
                int sal = det.getCantSalida();
                BigDecimal sumsal = suma.getSumaCantSalida();
                suma.setSumaCantSalida(suma.getSumaCantSalida().add(new BigDecimal(det.getCantSalida())));
                suma.setSumaSaldo(suma.getSumaSaldo().add(new BigDecimal(det.getCantEntrada() - det.getCantSalida())));
                suma.setSaldoValorizado(suma.getSaldoValorizado().add(det.getValorizado()));

            } else {
                suma = new RepInventarioValorizadoSuma(det);
                codigoSuma = det.getCodigoSuma();
                detalleInventarioAnt = det.getDetalleInventario() == null ? 0 : det.getDetalleInventario().getId();
                detalleEntradaArticuloAnt = det.getDetalleEntradaArticulo() == null ? 0 : det.getDetalleEntradaArticulo().getId();
                primero = false;
            }
        }

        if (i > 0) {
            resp.add(suma);
        }

        return resp;
    }

    public List<ReportesInventario> obtenerDatosInventario2(Date fechaDesde, Date fechaHasta, String filtroDeposito, Inventarios inv) {

        String where = "";

        if (filtroDeposito != null) {
            where = filtroDeposito;
        }

        SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
        javax.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                "select s.codigo, s.articulo, ifnull(m.descripcion,'') as marca, ifnull(o.descripcion,'') as modelo, s.inventario, s.fecha, s.descripcion,s.sede, s.simbolo, s.cant_inventario, s.cant_entrada, s.cant_salida,s.cant_baja, s.neto, s.stock "
                + "from (select @reporte_inventario_hasta := str_to_date('" + format2.format(fechaHasta) + "', '%d/%m/%Y')) a,  reportes_inventario2 s left join marcas as m on (s.marca = m.id) left join modelos as o on (s.modelo = o.id) where "
                + "s.inventario = " + inv.getId() + " "
                + where
                + " order by s.descripcion",
                ReportesInventario.class);
        return query.getResultList();
    }

    public void pdf2(boolean generarPdf) {

        if (getSelected() != null) {

            HttpServletResponse httpServletResponse = null;
            // if (getSelected() != null) {
            try {
                JRBeanCollectionDataSource beanCollectionDataSource = null;
                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
                // beanCollectionDataSource = new JRBeanCollectionDataSource(ejbFacade.getEntityManager().createNamedQuery("ReportesInventario.findByInventario", ReportesInventario.class).setParameter("inventario", getSelected().getId()).getResultList());

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

                String depositoStr = "";
                if (sede != null) {
                    depositoStr = "and s.sede = '" + sede.getId() + "' ";
                }

                List<ReportesInventario> resultado = obtenerDatosInventario2(fechaDesde, fechaHasta, depositoStr, getSelected());

                if (!resultado.isEmpty()) {
                    for (ReportesInventario res : resultado) {

                        javax.persistence.Query query2 = ejbFacade.getEntityManager().createNativeQuery("select a.articulo as codigo, ifnull(a.costo_unitario,0) as precio_unitario, ifnull(date_format(a.fecha_vencimiento, '%d-%m-%Y'), '') as vencimiento,a.sede as sede from detalles_entrada_articulo as a, entradas_articulo as e\n"
                                + "where a.entrada_articulo = e.id and a.articulo = ?1\n"
                                + "and (e.fecha_entrada, e.fecha_hora_alta) in (select max(c.fecha_entrada), max(c.fecha_hora_alta) from detalles_entrada_articulo as b, entradas_articulo as c where b.entrada_articulo = c.id and b.articulo = a.articulo);", ReportesInventario.class).setParameter(1, res.getArticulo());
                        List<ReportesInventario> inv = query2.getResultList();

                        if (!inv.isEmpty()) {
                            res.setPrecioUnitario(inv.get(0).getPrecioUnitario());
                            res.setVencimiento(inv.get(0).getVencimiento());
                            BigDecimal resul = inv.get(0).getPrecioUnitario() == null ? BigDecimal.ZERO : (inv.get(0).getPrecioUnitario().multiply(new BigDecimal(res.getNeto())) == null ? BigDecimal.ZERO : inv.get(0).getPrecioUnitario().multiply(new BigDecimal(res.getNeto())));
                            res.setNetoValorizado(resul);
                        } else {
                            res.setPrecioUnitario(BigDecimal.ZERO);
                            res.setVencimiento("");
                            res.setNetoValorizado(BigDecimal.ZERO);
                        }

                        Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class).setParameter("codigo", res.getArticulo()).getSingleResult();
                        res.setoGasto(art.getObjetoGasto() == null ? Integer.valueOf(0) : art.getObjetoGasto().getCodigo());
                    }
                }

                beanCollectionDataSource = new JRBeanCollectionDataSource(resultado);

                HashMap map = new HashMap();

                Date fecha = ejbFacade.getSystemDate();

                map.put("fechaDesde", format.format(fechaDesde));
                map.put("fechaHasta", format2.format(fechaHasta));
                map.put("estado", getSelected().getEstado().getDescripcion());

                JasperPrint jasperPrint = null;
                ServletOutputStream servletOutputStream = null;

                if (generarPdf) {

                    String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteInventariosPorDias.jasper");
                    jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                    httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                    httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                    servletOutputStream = httpServletResponse.getOutputStream();
                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

                } else {
                    String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteInventarioPorDiasExcel.jasper");
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
        }
    }

    public void pdfDeposito(boolean generarPdf) {

        if (listadepositoFiltroInventario != null) {
            if (!listadepositoFiltroInventario.isEmpty()) {

                HttpServletResponse httpServletResponse = null;
                try {
                    JRBeanCollectionDataSource beanCollectionDataSource = null;
                    JRBeanCollectionDataSource beanCollectionDataSource1 = null;

                    ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fechaHasta);
                    cal.add(Calendar.DATE, 1);
                    Date nuevaFechaHasta = cal.getTime();

                    List<ReportesInventario> lista = ejbFacade.getEntityManager().createNamedQuery("ReportesInventario.findByInventarioPorDias", ReportesInventario.class
                    ).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("sede", listadepositoFiltroInventario).getResultList();
                    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                    List<RepInventariosPorDias> listaDeposito = new ArrayList<>();
                    RepInventariosPorDias item = null;

                    for (ReportesInventario inv : lista) {

                        item = new RepInventariosPorDias();

                        item.setVencimiento(inv.getVencimiento());
                        item.setCodigo(inv.getCodigo());
                        item.setArticulo(inv.getArticulo());
                        item.setMarca(inv.getMarca());
                        item.setPrecioUnitario(inv.getPrecioUnitario());
                        item.setoGasto(inv.getoGasto());
                        item.setSimbolo(inv.getSimbolo());
                        item.setCantInventario(inv.getCantInventario());
                        item.setCantEntrada(inv.getCantEntrada());
                        item.setCantSalida(inv.getCantSalida());
                        item.setCantBaja(inv.getCantBaja());
                        item.setNeto(inv.getNeto());
                        item.setNetoValorizado(inv.getNetoValorizado());

                        listaDeposito.add(item);

                    }

                    beanCollectionDataSource = new JRBeanCollectionDataSource(listaDeposito);

                    HashMap map = new HashMap();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                    Date fecha = ejbFacade.getSystemDate();

                    //map.put("fecha", format.format(fecha));
                    map.put("fechaDesde", format2.format(fechaDesde));
                    map.put("fechaHasta", format2.format(fechaHasta));

                    String descDeposito = "";
                    for (Sede depo : listadepositoFiltroInventario) {
                        if (!"".equals(descDeposito)) {
                            descDeposito += ", ";
                        }

                        descDeposito += depo.getDescripcion();

                    }
                    map.put("descDeposito", descDeposito);

                    JasperPrint jasperPrint = null;
                    ServletOutputStream servletOutputStream = null;

                    if (generarPdf) {
                        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteInventariosPorDias.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                        servletOutputStream = httpServletResponse.getOutputStream();

                        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
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
                JsfUtil.addErrorMessage("Debe escoger un deposito.");
            }
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

}

/*
    public void pdfFCDos() {
        if (getSelected() != null) {

//            if (articulo == null) {
//                JsfUtil.addErrorMessage("Debe elegir un artículo");
//                return;
//}

            // List<DatosFCDoce> resultado = obtenerDatosFCDoce(articulo, getSelected());

            String articuloStr = "";
            if (articulo != null) {
                articuloStr = "and s.articulo = '" + articulo.getCodigo() + "' ";
            }

            List<ReportesInventario> resultado = obtenerDatosInventario(ejbFacade.getSystemDate(), articuloStr, getSelected());


//            List<DetallesInventario> listaDet = ejbFacade.getEntityManager().createNamedQuery("DetallesInventario.findByInventario", DetallesInventario.class).setParameter("inventario", getSelected()).getResultList();

//            int invInicial = 0;
//            for (DetallesInventario ini : listaDet) {
//                if (ini.getArticulo().getCodigo().equals(articulo.getCodigo())) {
//                    invInicial = ini.getCantidad();
//                    break;
//                }
//            }

            List<RepFCDos> listaRepFCDos = new ArrayList<>();

            RepFCDos repFCDos;

            // Integer saldo = invInicial;
            
            String cuenta = "2.5.1.01";
            String subCuenta = "2.5.1.01.01";
           // String analitico1 = "";
           //String analitico2 = "";

            for (ReportesInventario res : resultado) {

                repFCDos = new RepFCDos();

                javax.persistence.Query query2 = ejbFacade.getEntityManager().createNativeQuery("select a.articulo as codigo, ifnull(a.costo_unitario,0) as precio_unitario, ifnull(date_format(a.fecha_vencimiento, '%d-%m-%Y'), '') as vencimiento from detalles_entrada_articulo as a, entradas_articulo as e\n"
                        + "where a.entrada_articulo = e.id and a.articulo = ?1\n"
                        + "and e.fecha_entrada in (select max(c.fecha_entrada) from detalles_entrada_articulo as b, entradas_articulo as c where b.entrada_articulo = c.id and b.articulo = a.articulo);", ReportesInventario.class).setParameter(1, res.getArticulo());

                List<ReportesInventario> ultimaEntrada = query2.getResultList();

                BigDecimal precioPromedio;
                if (!ultimaEntrada.isEmpty()) {
                    precioPromedio = ultimaEntrada.get(0).getPrecioUnitario();
                } else {
                    precioPromedio = BigDecimal.ZERO;
                }
                
                repFCDos.setCuenta(cuenta);
                repFCDos.setSubCuenta(subCuenta);

                Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class).setParameter("codigo", res.getArticulo()).getSingleResult();
                
                repFCDos.setAnalitico1(art.getObjetoGasto() != null? art.getObjetoGasto().getAnalitico_uno():"");
                repFCDos.setAnalitico2(art.getObjetoGasto() != null? art.getObjetoGasto().getAnalitico_dos():"");
                repFCDos.setDescripcion(res.getArticulo() + "-" + res.getDescripcion());
                repFCDos.setUnidadM(res.getSimbolo());
                repFCDos.setCantidad(res.getCantInventario().add(res.getCantEntrada()).subtract(res.getCantSalida()).intValue());
                repFCDos.setValorUnit(precioPromedio);
                repFCDos.setValorTotal(repFCDos.getValorUnit().multiply(new BigDecimal(repFCDos.getCantidad())));
            
//                
//                repFCDos.setCantidadF(0);
//                repFCDos.setValorUnitF(BigDecimal.ZERO);
//                repFCDos.setValorTotalF(repFCDos.getValorUnitF().multiply(new BigDecimal(repFCDos.getCantidadF())));
//
//               
//                Integer cantidadDif = repFCDos.getCantidad() - repFCDos.getCantidadF();
//                
//                if(cantidadDif > 0){
//                    repFCDos.setCantidadSob(cantidadDif);
//                    repFCDos.setValorSob(repFCDos.getValorTotal().subtract(repFCDos.getValorTotalF()));
//                    
//                    repFCDos.setCantidadFal(0);
//                    repFCDos.setValorFal(BigDecimal.ZERO);
//                }else{
//                    repFCDos.setCantidadFal(cantidadDif * -1);
//                    repFCDos.setValorFal((new BigDecimal(-1)).multiply(repFCDos.getValorTotal().subtract(repFCDos.getValorTotalF())));
//                    
//                    repFCDos.setCantidadSob(0);
//                    repFCDos.setValorSob(BigDecimal.ZERO);
//                }
//              
                listaRepFCDos.add(repFCDos);
                
            }

            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listaRepFCDos);

            HashMap map = new HashMap();

            JasperPrint jasperPrint = null;
            ServletOutputStream servletOutputStream = null;

            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteInventarioBienesEnExistencia.jasper");
            HttpServletResponse httpServletResponse = null;
            try {
                jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                httpServletResponse.addHeader("Content-disposition", "filename=reporte.xlsx");

                servletOutputStream = httpServletResponse.getOutputStream();

                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                exporter.exportReport();
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
    }
}
 */
 /*        FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
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
    }
 */
 /* public void pdf2() {

            HttpServletResponse httpServletResponse = null;

            if (getSelected() != null) {
                 
            try {
                
                JRBeanCollectionDataSource beanCollectionDataSource = null;

                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
                beanCollectionDataSource = new JRBeanCollectionDataSource(ejbFacade.getEntityManager().createNamedQuery("ReportesInventarios.findByInventario", Inventarios.class).setParameter("inventario", getSelected().getId()).getResultList());

                HashMap map = new HashMap();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

                Date fecha = ejbFacade.getSystemDate();

                map.put("fecha", format.format(fecha));
                map.put("hasta", format2.format(fecha));

                JasperPrint jasperPrint = null;
                ServletOutputStream servletOutputStream = null;

                if (generarPdf) {

                    String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteInventarios.jasper");
                    jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                    httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                    httpServletResponse.addHeader("Content-disposition", "attachment;filename=reporte.pdf");

                    servletOutputStream = httpServletResponse.getOutputStream();

                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
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
 
 */
