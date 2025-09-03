package py.com.startic.gestion.controllers;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import py.com.startic.gestion.models.Articulos;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import org.apache.commons.io.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.datasource.RepCodArticulos;
import py.com.startic.gestion.datasource.RepEntradaObjetoGasto;
import py.com.startic.gestion.datasource.RepCodArticulos;
import py.com.startic.gestion.datasource.ReportesInventario;
import py.com.startic.gestion.models.DetallesEntradaArticulo;

import py.com.startic.gestion.models.DetallesInventario;
import py.com.startic.gestion.models.ObjetosGasto;
import py.com.startic.gestion.models.Sede;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "articulosController")
@ViewScoped
public class ArticulosController extends AbstractController<Articulos> {

    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private DetallesInventarioController detalleInventarioController;
    @Inject
    private InventariosController inventarioController;
    @Inject
    private SedeController sedeController;

    private Collection<Articulos> filtrados;
    private List<Articulos> listaArticuloFiltro;
    private Iterable<Articulos> lista;

    private Articulos articuloFiltro;
    private List<Articulos> listaArticuloFiltroEntrada;
    private Object fechaAlta;
    private List<ReportesInventario> listaInv;
    private List<Articulos> listaArticulos;
    private Articulos articulo;
    private Sede sede;
    private Date fechaDesde;
    private Date fechaHasta;
    private UploadedFile file1;
    private String nombreArchivo;

    public List<Articulos> getListaArticuloFiltro() {
        return listaArticuloFiltro;
    }

    public void setListaArticuloFiltro(List<Articulos> listaArticuloFiltro) {
        this.listaArticuloFiltro = listaArticuloFiltro;
    }

    public Collection<Articulos> getFiltrados() {
        return filtrados;
    }

    public void setFiltrados(Collection<Articulos> filtrados) {
        this.filtrados = filtrados;
    }

    public List<Articulos> getListaArticulos() {
        return listaArticulos;
    }

    public void setListaArticulos(List<Articulos> listaArticulos) {
        this.listaArticulos = listaArticulos;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public UploadedFile getFile1() {
        return file1;
    }

    public void setFile1(UploadedFile file1) {
        this.file1 = file1;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
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

    public ArticulosController() {
        // Inform the Abstract parent controller of the concrete Articulos Entity
        super(Articulos.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        Date fecha = ejbFacade.getSystemDate();
        listaInv = inventarioController.obtenerDatosInventario(fecha);

    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
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
            getSelected().setDescripcion(null);

        }
        actualizarListas(sede);
    }

    public BigInteger obtenerNeto(Articulos art) {

        BigInteger stock = BigInteger.ZERO;
        for (ReportesInventario rep : listaInv) {
            if (art.getCodigo().equals(rep.getArticulo())) {
                stock = rep.getStock();
                break;
            }
        }
        return stock;
    }

    @Override
    public void delete(ActionEvent event) {
        Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class).setParameter("codigo", getSelected().getCodigo()).getSingleResult();

        boolean borrar = true;

        Collection<DetallesInventario> colInv = new ArrayList<>();

        for (DetallesInventario det : art.getDetallesInventarioCollection()) {
            if ("TE".equals(det.getInventario().getEstado().getCodigo())
                    || "CE".equals(det.getInventario().getEstado().getCodigo())) {
                borrar = false;
            }
            if ("PR".equals(det.getInventario().getEstado().getCodigo())) {
                colInv.add(det);
            }
        }

        if (borrar) {

            if (!colInv.isEmpty()) {
                for (DetallesInventario inv : colInv) {
                    detalleInventarioController.setSelected(inv);
                    detalleInventarioController.delete(event);
                }
            }

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            getSelected().setFechaHoraBorrado(ejbFacade.getSystemDate());
            getSelected().setUsuarioBorrado(usu);

            super.save(event);

            super.delete(event);

            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('wDatalist').clearFilters()");
            //setItems(this.ejbFacade.getEntityManager().createNamedQuery("Articulos.findOrdered", Articulos.class).getResultList());

        } else {
            JsfUtil.addErrorMessage("No se puede borrar un articulo que ya fue incluido en un inventario vigente o cerrado");
        }
    }

    @Override
    public Collection<Articulos> getItems() {
        return this.ejbFacade.getEntityManager().createNamedQuery("Articulos.findOrdered", Articulos.class).getResultList();
    }
   

    /**
     * Sets the "items" attribute with a collection of DetallesSalidaArticulo
     * entities that are retrieved from Articulos?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for DetallesSalidaArticulo page
     */
    public String navigateDetallesSalidaArticuloCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DetallesSalidaArticulo_items", this.getSelected().getDetallesSalidaArticuloCollection());
        }
        return "/pages/detallesSalidaArticulo/index";
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
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareSede(ActionEvent event) {
        if (this.getSelected() != null && sedeController.getSelected() == null) {
            sedeController.setSelected(this.getSelected().getSede());
        }
    }

    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
            if (file1 != null) {

                byte[] bytes = null;
                try {
                    bytes = IOUtils.toByteArray(file1.getInputStream());
                } catch (IOException ex) {
                    Logger.getLogger(ArticulosController.class.getName()).log(Level.SEVERE, null, ex);
                }

                getSelected().setImagen(bytes);

            }
        }

        super.save(event);
    }

    /**
     * Store a new item in the data layer.
     *
     * @param event an event from the widget that wants to save a new Entity to
     * the data layer
     */
    @Override
    public void saveNew(ActionEvent event) {
        if (getSelected() != null) {

            getSelected().setStock(0);

            if (getSelected().getStockCritico() <= 0) {
                JsfUtil.addErrorMessage("El stock critico debe ser mayor a cero");
                return;
            }

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usu);
            getSelected().setEmpresa(usu.getEmpresa());
            if (file1 != null) {

                if (file1.getContent() != null) {
                    byte[] bytes = null;
                    try {
                        bytes = IOUtils.toByteArray(file1.getInputStream());
                    } catch (IOException ex) {
                        Logger.getLogger(ArticulosController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    getSelected().setImagen(bytes);
                } else {
                }
                // super.saveNew(null);
            }

            javax.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                    "select ifnull(max(CONVERT(codigo,SIGNED INTEGER)),0) as VALOR from articulos;", NroMesaEntrada.class);

            NroMesaEntrada cod = (NroMesaEntrada) query.getSingleResult();

            getSelected().setCodigo(String.valueOf(cod.getCodigo() + 1));

            super.saveNew(event);

            setSelected(ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class).setParameter("codigo", String.valueOf(cod.getCodigo() + 1)).getSingleResult());

            // requestContext = RequestContext.getCurrentInstance();
            //requestContext.execute("PF('wDatalist').update()");
        }
    }

    public void view() {

        try {
            //Articulos art = ejbFacade.find(selected.getCodigo());
            Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class
            ).setParameter("codigo", getSelected().getCodigo()).getSingleResult();
            byte[] arreglo = art.getImagen();

            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.getOutputStream().write(arreglo);
            response.getOutputStream().close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            FacesMessage message = new FacesMessage("Error");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void buscarArticulosPorFecha() {

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        Articulos art = (Articulos) session.getAttribute("Articulos");

        setItems(this.ejbFacade.getEntityManager().createNamedQuery("Articulos.findByFechaBusqueda", Articulos.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).setParameter("sede", sede).getResultList());

        setSelected(null);
        resetParents();
    }

    public void reporteCodigoArticulo() {

        if (listaArticuloFiltro != null) {
            HttpServletResponse httpServletResponse = null;
            try {
                JRBeanCollectionDataSource beanCollectionDataSource = null;

                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 1);

                List<RepCodArticulos> listaCa = new ArrayList<>();
                RepCodArticulos item = null;
                for (Articulos det : listaArticuloFiltro) {
                    item = new RepCodArticulos();

                    item.setObjetoGasto(det.getObjetoGasto() == null ? "" : det.getObjetoGasto().getDescripcion());
                    item.setCodigoOg(det.getObjetoGasto() == null ? 0 : det.getObjetoGasto().getCodigo());
                    item.setDescripcion(det.getDescripcion());
                    item.setMarca(det.getMarca() == null ? "" : det.getMarca().getDescripcion());
                    item.setCodigo(det.getCodigo());

                    listaCa.add(item);

                    //det.setFechaEntrada(det.getEntradaArticulo().getFechaEntrada());
                }

                beanCollectionDataSource = new JRBeanCollectionDataSource(listaCa);

                HashMap map = new HashMap();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

                Date fecha = ejbFacade.getSystemDate();

                map.put("fecha", format.format(fecha));

                String descArticulos = "";
                for (Articulos ca : listaArticuloFiltro) {
                    if (!"".equals(descArticulos)) {
                        descArticulos += ", ";
                    }

                    descArticulos += ca.getCodigo() + "-" + ca.getDescripcion();

                }

                map.put("descArticulo", descArticulos);

                JasperPrint jasperPrint = null;
                ServletOutputStream servletOutputStream = null;

                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteCodigoArticulo.jasper");
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
        } else {
            JsfUtil.addErrorMessage("Debe escoger un código del Objeto del Gasto.");
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

}
