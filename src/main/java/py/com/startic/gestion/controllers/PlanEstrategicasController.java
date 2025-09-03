package py.com.startic.gestion.controllers;

import java.awt.Component;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import py.com.startic.gestion.models.PlanEstrategicas;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
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
import org.primefaces.model.file.UploadedFile;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.datasource.RepAvanceAcciones;
import py.com.startic.gestion.models.Acciones;
import py.com.startic.gestion.models.Actividades;
import py.com.startic.gestion.models.DetallesPlanEstrategicas;
import py.com.startic.gestion.models.EstadosActividad;
import py.com.startic.gestion.models.EstadosInforme;
import py.com.startic.gestion.models.Periodo;
import py.com.startic.gestion.models.Programacion;
import py.com.startic.gestion.models.TiposDocumentosJudiciales;
import py.com.startic.gestion.models.TiposObjetivos;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "planEstrategicasController")
@ViewScoped
public class PlanEstrategicasController extends AbstractController<PlanEstrategicas> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private TiposObjetivosController tiposObjetivosController;
    @Inject
    private DetallesPlanEstrategicasController detallesPlanEstrategicasController;
    private Usuarios usuario;
    private List<Acciones> listaAcciones;
    private List<Programacion> listaProgramacion;
    private List<Actividades> listaActividades;
    private Collection<DetallesPlanEstrategicas> detalles;
    private DetallesPlanEstrategicas detalleSelected;
    private Acciones accion;
    private DetallesPlanEstrategicas detallesPlan;
    private Programacion programacion;
    private Actividades actividad;
    private TiposDocumentosJudiciales tiposDocumento;
    private PlanEstrategicas planEstrategicas;
    private TiposObjetivos tiposObjetivos;
    private HttpSession session;
    private double variable2;
    private String año;
    private String menu;
    private Date fechaDesde;
    private Date fechaHasta;
    private Integer activeTab;
    private UploadedFile file;
    private Periodo periodo;
    private String descripcionArchivo;
    List<EstadosActividad> ListEstadosActividad;

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        detalles = null;
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usuario = (Usuarios) session.getAttribute("Usuarios");

        menu = Constantes.NO;
        setItems(ejbFacade.getEntityManager().createNamedQuery("PlanEstrategicas.findOrdered", PlanEstrategicas.class).getResultList());
        if (!getItems2().isEmpty()) {
            PlanEstrategicas art = getItems2().iterator().next();
            setSelected(art);

        } else {
            setSelected(null);
        }
    }

    public PlanEstrategicas prepareCreate() {
        PlanEstrategicas plan = super.prepareCreate(null);
        detalles = null;
        tiposObjetivos = null;
        accion = null;
        periodo = null;
        return plan;
    }

    public List<Acciones> getListaAcciones() {
        return listaAcciones;
    }

    public void setListaAcciones(List<Acciones> listaAcciones) {
        this.listaAcciones = listaAcciones;
    }

    public Acciones getAccion() {
        return accion;
    }

    public void setAccion(Acciones accion) {
        this.accion = accion;
    }

    public double getVariable2() {
        return variable2;
    }

    public void setVariable2(double variable2) {
        this.variable2 = variable2;
    }

    public Integer getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(Integer activeTab) {
        this.activeTab = activeTab;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getDescripcionArchivo() {
        return descripcionArchivo;
    }

    public void setDescripcionArchivo(String descripcionArchivo) {
        this.descripcionArchivo = descripcionArchivo;
    }

    public String getAño() {
        return año;
    }

    public void setAño(String año) {
        this.año = año;
    }

    public TiposObjetivos getTiposObjetivos() {
        if (session.getAttribute("tipoSelected") != null) {
            tiposObjetivos = (TiposObjetivos) session.getAttribute("tipoSelected");
            session.removeAttribute("tipoSelected");
        }

        return tiposObjetivos;
    }

    public void setTiposObjetivos(TiposObjetivos tiposObjetivos) {
        this.tiposObjetivos = tiposObjetivos;
    }

    public Collection<DetallesPlanEstrategicas> getDetalles() {
        return detalles;
    }

    public void setDetalles(Collection<DetallesPlanEstrategicas> detalles) {
        this.detalles = detalles;
    }

    public DetallesPlanEstrategicas getDetalleSelected() {
        return detalleSelected;
    }

    public void setDetalleSelected(DetallesPlanEstrategicas detalleSelected) {
        this.detalleSelected = detalleSelected;
    }

    public Actividades getActividad() {
        return actividad;
    }

    public void setActividad(Actividades actividad) {
        this.actividad = actividad;
    }

    public PlanEstrategicas getPlanEstrategicas() {
        return planEstrategicas;
    }

    public void setPlanEstrategicas(PlanEstrategicas planEstrategicas) {
        this.planEstrategicas = planEstrategicas;
    }

    public List<Actividades> getListaActividades() {
        return listaActividades;
    }

    public void setListaActividades(List<Actividades> listaActividades) {
        this.listaActividades = listaActividades;
    }

    public List<Programacion> getListaProgramacion() {
        return listaProgramacion;
    }

    public void setListaProgramacion(List<Programacion> listaProgramacion) {
        this.listaProgramacion = listaProgramacion;
    }

    public PlanEstrategicasController() {
        // Inform the Abstract parent controller of the concrete PlanEstrategicas Entity
        super(PlanEstrategicas.class);
    }

    public Programacion getProgramacion() {
        return programacion;
    }

    public void setProgramacion(Programacion programacion) {
        this.programacion = programacion;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
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

    public List<EstadosActividad> getListEstadosActividad() {
        return ListEstadosActividad;
    }

    public void setListEstadosActividad(List<EstadosActividad> ListEstadosActividad) {
        this.ListEstadosActividad = ListEstadosActividad;
    }

    public DetallesPlanEstrategicas getDetallesPlan() {
        return detallesPlan;
    }

    public void setDetallesPlan(DetallesPlanEstrategicas detallesPlan) {
        this.detallesPlan = detallesPlan;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public TiposDocumentosJudiciales getTiposDocumento() {
        return tiposDocumento;
    }

    public void setTiposDocumento(TiposDocumentosJudiciales tiposDocumento) {
        this.tiposDocumento = tiposDocumento;
    }

    public void resetParents() {
        empresaController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);

        seleccionar();
    }

    public void buscarPorFecha() {

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        PlanEstrategicas plan = (PlanEstrategicas) session.getAttribute("PlanEstrategicas");

        //setItems(this.ejbFacade.getEntityManager().createNamedQuery("PlanEstrategicas.findByFecha", PlanEstrategicas.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).getResultList());
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("PlanEstrategicas.findByPeriodo", PlanEstrategicas.class).setParameter("año", año).getResultList());

        setSelected(null);
        resetParents();
    }

    private List<Acciones> obtenerListaTiposObjetivos(TiposObjetivos tiposObjetivos) {
        if (tiposObjetivos != null) {
            return this.ejbFacade.getEntityManager().createNamedQuery("Acciones.findByTiposObjetivos", Acciones.class
            ).setParameter("tiposObjetivos", tiposObjetivos).getResultList();
        }
        return null;
    }

    public void actualizarListas(TiposObjetivos tiposObjetivos) {
        listaAcciones = obtenerListaTiposObjetivos(tiposObjetivos);

    }

    public void resetearListas(TiposObjetivos tiposObjetivos) {
        accion = null;
        programacion = null;

        if (getSelected() != null) {
            getSelected().setAccion(null);
            getSelected().setProgramacion(null);

        }
        actualizarListas(tiposObjetivos);
    }

    public void agregar() {

        if (actividad == null) {
            JsfUtil.addErrorMessage("Debe seleccionar una Accion Operativa");
            return;
        }

        if (detalles == null) {
            detalles = new ArrayList<>();
        }

        boolean encontro = false;

        int contador = 0;

        for (DetallesPlanEstrategicas det : detalles) {
            contador++;
            if (det.getActividad().getId().equals(actividad.getId())) {
                detalles.remove(det);

                DetallesPlanEstrategicas dsa = detallesPlanEstrategicasController.prepareCreate(null);

                SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

                Date fecha = ejbFacade.getSystemDate();
                dsa.setPlanEstrategica(getSelected());
                dsa.setActividad(actividad);
                dsa.setTipoDocumentos(actividad.getTiposDocumento());
                dsa.setId(Integer.valueOf(format.format(fecha)));
                detalles.add(dsa);
                encontro = true;
                break;
                //}
            }
        }

        if (!encontro) {
            DetallesPlanEstrategicas dsa = detallesPlanEstrategicasController.prepareCreate(null);

            SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

            Date fecha = ejbFacade.getSystemDate();

            dsa.setItem(contador + 1);
            dsa.setPlanEstrategica(getSelected());
            dsa.setActividad(actividad);
            dsa.setTipoDocumentos(actividad.getTiposDocumento());
            dsa.setId(Integer.valueOf(format.format(fecha)));

            detalles.add(dsa);
        }

    }

    public void borrarDetalle() {
        if (detalles != null && detalleSelected != null) {
            for (DetallesPlanEstrategicas det : detalles) {
                if (det.getActividad().getId().equals(detalleSelected.getActividad().getId())) {
                    detalles.remove(det);
                    break;
                }
            }
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

            if (detalles != null) {
                if (detalles.isEmpty()) {
                    JsfUtil.addErrorMessage("Debe habar al menos un detalle");
                    return;
                }
            } else {
                JsfUtil.addErrorMessage("Debe haber al menos un detalle.");
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
            getSelected().setTiposObjetivos(tiposObjetivos);
            getSelected().setAccion(accion);
            getSelected().setPeriodo(periodo);
            //getSelected().setValorAnterior(detallesPlan.getVariable2());

            super.saveNew(event);

            for (DetallesPlanEstrategicas det : detalles) {
                det.setPlanEstrategica(getSelected());
                det.setFechaHoraUltimoEstado(fecha);
                det.setUsuarioUltimoEstado(usu);
                det.setFechaHoraAlta(fecha);
                det.setUsuarioAlta(usu);
                det.setEmpresa(usu.getEmpresa());
                det.setDepartamento(actividad.getDepartamento());
                det.setProgramacion(getSelected().getProgramacion());
                //det.setTipoDocumentos(actividad.getTiposDocumento());
                // det.setValorAnterior(getSelected().getVariable2());

                det.setId(null);

                detallesPlanEstrategicasController.setSelected(det);

                detallesPlanEstrategicasController.saveNew(event);

            }

        }

    }

    private void seleccionar() {
        if (getSelected() != null) {
            detalles = this.ejbFacade.getEntityManager().createNamedQuery("DetallesPlanEstrategicas.findByPlanEstrategicica", DetallesPlanEstrategicas.class).setParameter("planEstrategica", getSelected()).getResultList();
        } else {
            detalles = null;
        }

    }

    private List<Programacion> obtenerListaProgramacion(Acciones accion) {
        if (accion != null) {
            return this.ejbFacade.getEntityManager().createNamedQuery("Programacion.findByProgramacion", Programacion.class
            ).setParameter("accion", accion).getResultList();
        }
        return null;
    }

    public void actualizarListasProgramacion(Acciones accion) {
        listaProgramacion = obtenerListaProgramacion(accion);
    }

    public void resetearListasProgramacion(Acciones accion) {
        programacion = null;

        if (getSelected() != null) {
            getSelected().setProgramacion(null);

        }
        actualizarListasProgramacion(accion);
    }

    private List<Actividades> obtenerListaAcciones(Acciones accion) {
        if (accion != null) {
            return this.ejbFacade.getEntityManager().createNamedQuery("Actividades.findByAcciones", Actividades.class
            ).setParameter("accion", accion).getResultList();
        }
        return null;
    }

    public void actualizarListas(Acciones accion) {
        listaActividades = obtenerListaAcciones(accion);
        listaProgramacion = obtenerListaProgramacion(accion);
    }

    public void resetearListas(Acciones accion) {
        actividad = null;
        programacion = null;

        if (getSelected() != null) {
            getSelected().setActividad(null);
            getSelected().setProgramacion(null);

        }
        actualizarListas(accion);
    }

    public String calculaColor1() {
        if (getSelected() != null) {
            if (getSelected().getEstado().getCodigo().equals("RO")) {

                return "red";
            }
            if (getSelected().getEstado().getCodigo().equals("NA")) {
                return "yellow";
            }
            if (getSelected().getEstado().getCodigo().equals("VE")) {
                return "green";
            }
        }

        return null;

    }

    public String calculaColor() {
        if (getSelected() != null) {
            if (getSelected().getResultado() <= 10 && getSelected().getResultadoPresupuesto() <= 10) {

                return "red";
            }
            if (getSelected().getResultado() >= 60 && getSelected().getResultadoPresupuesto() >= 60) {
                return "yellow";
            }
            if (getSelected().getResultado() == 100 && getSelected().getResultadoPresupuesto() == 100) {
                return "green";
            }
        }
        return null;

    }

    public void actualizarMetasAlcanzada(PlanEstrategicas item) {
        setSelected(item);
        super.save(null);
        setSelected(null);
    }

    public void reporteAvanceResultado(boolean generarPdf) {

        HttpServletResponse httpServletResponse = null;
        try {
            JRBeanCollectionDataSource beanCollectionDataSource = null;

            ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

            // Calendar cal = Calendar.getInstance();
            // cal.setTime(fechaHasta);
            // cal.add(Calendar.DATE, 1);
            // Date nuevaFechaHasta = cal.getTime();
            List<PlanEstrategicas> lista = ejbFacade.getEntityManager().createNamedQuery("PlanEstrategicas.findByPeriodo", PlanEstrategicas.class
            ).setParameter("año", año).getResultList();

            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            List<RepAvanceAcciones> listafinal2 = new ArrayList<>();
            RepAvanceAcciones item = null;
            for (PlanEstrategicas det : lista) {

                item = new RepAvanceAcciones();

                item.setObjetivoDescripcion(det.getTiposObjetivos() == null ? "" : (det.getTiposObjetivos() == null ? "" : det.getTiposObjetivos().getObjetivo()));
                item.setAcciones(det.getProgramacion().getAccion() == null ? "" : (det.getProgramacion().getAccion() == null ? "" : det.getProgramacion().getAccion().getDescripcion()));
                item.setMeta(det.getProgramacion().getMeta());
                item.setIndicador(det.getProgramacion().getFormula());
                item.setVariable(det.getVariable2());
                item.setResultado(det.getResultado());
                item.setValorVariable(det.getProgramacion().getValorVariable());
                // item.setAño(det.getProgramacion().getAño());
                // item.setResultadoPresupuestario(det.getResultadoPresupuesto());

                listafinal2.add(item);

            }

            beanCollectionDataSource = new JRBeanCollectionDataSource(listafinal2);

            HashMap map = new HashMap();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            Date fecha = ejbFacade.getSystemDate();

            map.put("fecha", format.format(fecha));
            //map.put("fechaDesde", format2.format(fechaDesde));
            // map.put("fechaHasta", format2.format(fechaHasta));
            map.put("año", año);

            JasperPrint jasperPrint = null;
            ServletOutputStream servletOutputStream = null;
            if (generarPdf) {
                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteSalidaArticuloNroFormulario.jasper");
                jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                servletOutputStream = httpServletResponse.getOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

            } else {
                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteAvanceResultadoExcel.jasper");
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
