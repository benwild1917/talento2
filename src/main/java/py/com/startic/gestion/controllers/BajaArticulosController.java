package py.com.startic.gestion.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import org.primefaces.PrimeFaces;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.datasource.RepDepositosArticulo;
import py.com.startic.gestion.datasource.RepSalidaArticulo;
import py.com.startic.gestion.models.ArchivosReporte;
import py.com.startic.gestion.models.Articulos;
import py.com.startic.gestion.models.BajaArticulos;
import py.com.startic.gestion.models.DetallesSalidaArticulo;
import py.com.startic.gestion.models.Empresas;
import py.com.startic.gestion.models.EntradasArticulo;
import py.com.startic.gestion.models.Inventarios;
import py.com.startic.gestion.models.Sede;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "bajaArticulosController")
@ViewScoped
public class BajaArticulosController extends AbstractController<BajaArticulos> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private ArticulosController articuloController;
  
    private BajaArticulos bajaArticulo;
    private EntradasArticulo entradaArticulo;
    private Articulos articuloFiltro;
    private Date fechaDesde;
    private Date fechaHasta;
    private Articulos articulo;
    private Sede sede;

   

    public BajaArticulosController() {
        // Inform the Abstract parent controller of the concrete EstadosDocumento Entity
        super(BajaArticulos.class);
    }

    public BajaArticulos getBajaArticulo() {
        return bajaArticulo;
    }

    public void setBajaArticulo(BajaArticulos bajaArticulo) {
        this.bajaArticulo = bajaArticulo;
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

    public Articulos getArticuloFiltro() {
        return articuloFiltro;
    }

    public void setArticuloFiltro(Articulos articuloFiltro) {
        this.articuloFiltro = articuloFiltro;
    }
    

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
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

  
    

    @Override
    public BajaArticulos prepareCreate(ActionEvent event) {
        BajaArticulos bajaArticulos = super.prepareCreate(event);

        return bajaArticulos;
    }

    public BajaArticulos prepareBajaArticulos(Articulos articulo) {
        BajaArticulos bajaArticulos = this.prepareCreate(null);
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

        bajaArticulos.setArticulo(articulo);
        bajaArticulos.setSede(articulo.getSede());
        bajaArticulos.setInventario(inv);

        return bajaArticulos;
    }

    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usuario = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
        }

        super.save(event);
    }

    public String saveNewDeposito() {
        if (getSelected() != null) {
            ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usuario = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usuario);
            getSelected().setEmpresa(new Empresas(1));

            // Forzar leer devuelta la tabla de precios, por si se agrego uno nuevo
            //getSelected().getLote().setVentasLoteCollection(ejbFacade.getEntityManager().createNamedQuery("VentasLote.findByLote", VentasLote.class).setParameter("lote", getSelected().getLote()).getResultList());
        }

        super.saveNew(null);

        Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class
        ).setParameter("codigo", getSelected().getArticulo().getCodigo()).getSingleResult();

        art.setStock(art.getStock() - getSelected().getCantidad());

        articuloController.setSelected(art);

        articuloController.save(null);
        return "/pages/DepositosArticulo/index";

    }

    public String saveNewBajaArticulos() {
        if (getSelected() != null) {
            ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usuario = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usuario);
            getSelected().setEmpresa(new Empresas(1));

            // Forzar leer devuelta la tabla de precios, por si se agrego uno nuevo
            //getSelected().getLote().setVentasLoteCollection(ejbFacade.getEntityManager().createNamedQuery("VentasLote.findByLote", VentasLote.class).setParameter("lote", getSelected().getLote()).getResultList());
        }

        super.saveNew(null);

        return "/pages/depositosArticulo/index";

    }

    public void buscarArticulosPorFecha() {

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        BajaArticulos baj = (BajaArticulos) session.getAttribute("BajaArticulos");

        setItems(this.ejbFacade.getEntityManager().createNamedQuery("BajaArticulos.findByFechaBusqueda", BajaArticulos.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).setParameter("sede", sede).getResultList());

        setSelected(null);
        resetParents();
    }
     public void imprimir(boolean generarPdf) {

        if (sede != null) {
            if (!sede.getDescripcion().isEmpty()) {

                HttpServletResponse httpServletResponse = null;
                try {
                    JRBeanCollectionDataSource beanCollectionDataSource = null;

                    ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                   // Calendar cal = Calendar.getInstance();
                    //cal.setTime(fecha);
                   // cal.add(Calendar.DATE, 1);
                   // Date nuevaFechaHasta = cal.getTime();

                    List<BajaArticulos> lista = ejbFacade.getEntityManager().createNamedQuery("BajaArticulos.findByFechaBusqueda", BajaArticulos.class
                    ).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).setParameter("sede", sede).getResultList();

                    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                    List<RepDepositosArticulo> listafinal2 = new ArrayList<>();
                    RepDepositosArticulo item = null;
                    for (BajaArticulos det : lista) {

                        item = new RepDepositosArticulo();

                        item.setArticulo(det.getArticulo().getDescripcion());
                       
                        String fechaString = "";
                        System.out.println("-----------------------");
               
                        item.setFecha(det.getFechaHoraAlta());
                        item.setDescripcion(det.getDescripcion());
                        item.setCantidad(det.getCantidad());
                        item.setArticulo(det.getArticulo().getDescripcion());
                        item.setSede(det.getArticulo().getSede().getDescripcion());
                        item.setUsuario(det.getUsuarioAlta().getNombresApellidos());
                        
                        listafinal2.add(item);

                        //det.setFechaSalida(det.getSalidaArticulo().getFechaSalida());
                    }

                    beanCollectionDataSource = new JRBeanCollectionDataSource(listafinal2);

                    HashMap map = new HashMap();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                    Date fecha = ejbFacade.getSystemDate();

                    map.put("fecha", format.format(fecha));
                    map.put("fechaDesde", format2.format(fechaDesde));
                    map.put("fechaHasta", format2.format(fechaHasta));
                    //map.put("articulo", getSelected().getArticulo().getDescripcion());

                    JasperPrint jasperPrint = null;
                    ServletOutputStream servletOutputStream = null;
                    if (generarPdf) {
                        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteDepositoArticulo.jasper");
                        jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                        servletOutputStream = httpServletResponse.getOutputStream();
                        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

                    } else {
                        String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteSalidaArticuloExcel.jasper");
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

    public void verDoc(ArchivosReporte doc) {

        HttpServletResponse httpServletResponse = null;
        try {
            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.setContentType("application/pdf");
            // httpServletResponse.setHeader("Content-Length", String.valueOf(getSelected().getDocumento().length));
            httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

            byte[] fileByte = null;

            try {
                if (doc.getArchivo() != null) {
                    fileByte = Files.readAllBytes(new File("/D:/ReporteNroFormulario" + "/" + doc.getArchivo()).toPath());
                }
                // fileByte = Files.readAllBytes(new File(par.getRutaArchivos() + "/" + doc.getArchivo()).toPath());
            } catch (IOException ex) {
                JsfUtil.addErrorMessage("No tiene documento adjunto");
            }

            servletOutputStream.write(fileByte);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.verdoc", "true", Collections.<String, Object>emptyMap());
            e.printStackTrace();

            if (httpServletResponse != null) {
                if (httpServletResponse.getHeader("Content-disposition") == null) {
                    httpServletResponse.addHeader("Content-disposition", "inline");
                } else {
                    httpServletResponse.setHeader("Content-disposition", "inline");
                }

            }
            JsfUtil.addErrorMessage("No se pudo generar el reporte.");
        } finally {

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
}
